package com.example.step03listview;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

	private List<String> friends;
	private ArrayAdapter<String> adapter;
	private EditText inputName;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		listView = findViewById(R.id.listView);

		friends = new ArrayList<>();
		friends.add("김구라");
		friends.add("해골");
		friends.add("원숭이");

		IntStream.range(1,20).forEach(i->friends.add("친구"+i));

		//  ListView에 표시하기 위해서는 adapter가 필요
		adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,friends);
		listView.setAdapter(adapter);

		findViewById(R.id.btnAdd).setOnClickListener(this);
		inputName = findViewById(R.id.inputName);

	}

	@Override
	public void onClick(View v) {
		friends.add(inputName.getText().toString());    //  입력값을 읽어옴
		inputName.setText("");  //  입력란 공란으로
		adapter.notifyDataSetChanged(); //  모델의 데이터 변경을 어뎁터에 알림
		listView.smoothScrollToPosition(friends.size() - 1);    //  리스트 뷰의 스크롤(마지막 번쨰 까지)
		//  마지막 번째는 목록.size()의 -1;
	}

	public void prev(View v){
		this.finish();
	}
}
