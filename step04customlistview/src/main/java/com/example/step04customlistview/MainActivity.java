package com.example.step04customlistview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.step04customlistview.adapter.CountryAdapter;
import com.example.step04customlistview.dto.CountryDto;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

	private ListView listView;
	private List<CountryDto> countries = new ArrayList<>();
	CountryAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//ListView 의 참조값 얻어와서 맴버필드에 저장하기
		listView = findViewById(R.id.listView);
		//Model 에 Sample 데이터 넣어주기
		countries.add(new CountryDto(R.drawable.austria, "오스트리아"));
		countries.add(new CountryDto(R.drawable.belgium, "벨기에"));
		countries.add(new CountryDto(R.drawable.brazil, "브라질"));
		countries.add(new CountryDto(R.drawable.france, "프랑스"));
		countries.add(new CountryDto(R.drawable.germany, "독일"));
		countries.add(new CountryDto(R.drawable.greece, "그리스"));
		countries.add(new CountryDto(R.drawable.israel, "이스라엘"));
		countries.add(new CountryDto(R.drawable.italy, "이탈리아"));
		countries.add(new CountryDto(R.drawable.japan, "일본"));
		countries.add(new CountryDto(R.drawable.korea, "대한민국"));
		countries.add(new CountryDto(R.drawable.poland, "폴란드"));
		countries.add(new CountryDto(R.drawable.spain, "스페인"));
		countries.add(new CountryDto(R.drawable.usa, "미국"));

		//Adapter 객체 생성하기
		adapter = new CountryAdapter
				(this, R.layout.listview_cell, countries);
		//Adapter 를 ListView 에 연결하기
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//  Detail Activity로 이동할 Intent 객체 생성 후 start
		Intent intent = new Intent(this, DetailActivity.class);

		//  intent에는 여러 데이터를 함께 보낼 수있다. (Object x)
		//  intent.putExtra("key",new Object());    //  일부를 제외하고 Object는 보낼 수 없다.
		intent.putExtra("dto",countries.get(position)); //  Serializable된 Object는 보낼 수 있다.

		startActivity(intent);
	}
}
