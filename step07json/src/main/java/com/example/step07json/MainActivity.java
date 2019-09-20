package com.example.step07json;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity
		implements Util.RequestListener, View.OnClickListener,
		AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

	// 실제 멤버 정보가 담길 list
	private List<MemberDto> memberList = new ArrayList<>();

	//  뷰에서 보여줄 내용 list
	private List<String> members = new ArrayList<>();
	private ArrayAdapter<String> adapter;

	//  기타 view
	private SwipeRefreshLayout mSwipeRefresh;
	private TextView inputName, inputAddr;
	private Button sendBtn;
	private ListView listView;

	//  Util에서 사용할 코드
	private static final int REQ_GET_DATA = 1;
	private static final int REQ_SEND_DATA = 2;
	private static final int REQ_DELETE_DATA = 3;

	//  요청 URL
	private static final String URL_GET_DATA = "http://192.168.0.111:8888/spring05/android/getList.do";
	private static final String URL_SEND_DATA = "http://192.168.0.111:8888/spring05/android/insert.do";
	private static final String URL_DELETE_DATA = "http://192.168.0.111:8888/spring05/android/delete.do";

	/*  필드선언 끝 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//  끌어내려 갱신하는 레이아웃 리스너
		mSwipeRefresh = findViewById(R.id.swipe_refresh_layout);
		mSwipeRefresh.setOnRefreshListener(() -> getData());

		//  목록 뷰 정의
		listView = findViewById(R.id.listView);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, members);
		listView.setAdapter(adapter);

		//  목록뷰 리스너 정의
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);

		//  버튼 정의
		sendBtn = findViewById(R.id.sendBtn);
		sendBtn.setOnClickListener(this);

		//  입력란 정의
		inputName = findViewById(R.id.inputName);
		inputAddr = findViewById(R.id.inputAddr);

		//  입력란 포커스 조정
		inputName.setNextFocusDownId(inputAddr.getId());
		inputAddr.setNextFocusRightId(sendBtn.getId());


		//  앱 실행 후 실행
		getData();
	}

	public void getData(){
		Util.sendGetRequest(this.REQ_GET_DATA, URL_GET_DATA,null,this);
	}

	/*
		{}:JSON-object, []:JSON-array
	 */

	@Override
	public void onSuccess(int requestId, Map<String, Object> result) {

		switch(requestId){
			case REQ_GET_DATA:
			//  response => response.json(); then res=>jsondata = res.data;
				memberList.clear();
				members.clear();

				String jsonData = (String) result.get("data");

				try {
					JSONArray arr = new JSONArray(jsonData);
					IntStream.range(0,arr.length())
							.forEachOrdered(i->{
								try {
									JSONObject obj = arr.getJSONObject(i);

									//  얻은 데이터를 Map/Dto에 저장
									MemberDto dto = new MemberDto();
									dto.setNum(obj.getInt("num"));
									dto.setName(obj.getString("name"));
									dto.setAddr(obj.getString("addr"));

									members.add(dto.getName());
									memberList.add(dto);

								} catch (JSONException e) {
									e.printStackTrace();
								}
							});

					Toast.makeText(
							this,
							"갱신 되었습니다.",
							Toast.LENGTH_LONG).show();

				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
				listView.smoothScrollToPosition(members.size()-1);

				if (mSwipeRefresh.isRefreshing())
					mSwipeRefresh.setRefreshing(false);

			break;

			case REQ_SEND_DATA:
				try {
					JSONObject obj = new JSONObject((String) result.get("data"));
					if(obj.getBoolean("isSuccess")){
						String msg = inputName.getText()
								+"님을 추가했습니다.";
						Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				getData();

				inputName.setText("");inputAddr.setText("");
				break;

			case REQ_DELETE_DATA:
				try {
					JSONObject obj = new JSONObject((String) result.get("data"));
					if(obj.getBoolean("isSuccess")){
						String msg = "성공적으로 처리되었습니다.";
						Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				getData();
				break;

		}

	}

	@Override
	public void onFail(int requestId, Map<String, Object> result) {

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.sendBtn:
				Map<String, String> param = new HashMap<>();
				param.put("name",inputName.getText()+"");
				param.put("addr",inputAddr.getText()+"");

				Util.sendPostRequest(this.REQ_SEND_DATA, URL_SEND_DATA, param, this);
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		MemberDto dto = memberList.get(position);
		Intent intent = new Intent(this,DetailActivity.class);
		intent.putExtra("dto",dto);
		startActivity(intent);
		//  Activity 이동
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

		//  익명 local innerClass에서 바깥의 지역변수를 참조하려면 final 변수여야한다.
		final MemberDto dto = memberList.get(position);

		new AlertDialog.Builder(this).setMessage(
				dto.getNum()+":"+dto.getName()+"회원을 삭제 하시겠습니까?")
				.setPositiveButton("OK", (dialog, which) -> {
							Map<String, String> param = new HashMap<>();
							param.put("num",dto.getNum()+"");
							Util.sendGetRequest(this.REQ_DELETE_DATA, URL_DELETE_DATA, param, MainActivity.this);
						})
				.setNegativeButton("NO",null)
				.create().show();

		return false;
	}

}
