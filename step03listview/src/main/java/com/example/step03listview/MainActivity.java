package com.example.step03listview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

	private List<String> friends;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//  res/의 android:id가 listView인 것을 참조
		//  in activity_main.xml
		ListView listView = findViewById(R.id.listView);

		friends = new ArrayList<>();
		friends.add("김구라");
		friends.add("해골");
		friends.add("원숭이");
		friends.add("주뎅");
		friends.add("덩어");

		IntStream.range(0,50).forEach(i->friends.add("친구"+i));

		//  ListView에 표시하기 위해서는 adapter가 필요
		//  리스트뷰의 하위 뷰중, 보여지는 최소개수만큼의 내용을 표시해주는 역할

		ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,friends);
		listView.setAdapter(adapter);

		//  ListView의 클릭 리스너
		listView.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		/*
			onItemClick(adapterView : ListView,
				View : CellView,
				position : event.target,
				id: event.target에 있는 모델의 primary key
			);
		 */

		//  토스트 알림
		Toast.makeText(this, friends.get(position), Toast.LENGTH_LONG).show();

		//  다이얼로그 알림
		new AlertDialog.Builder(this)
				.setTitle("선택한 친구는")
				.setMessage(friends.get(position))
				.setNeutralButton("확인",null)
				.create().show();
	}

	public void next(View v){
		startActivity(new Intent(this, Main2Activity.class));
	}

}
