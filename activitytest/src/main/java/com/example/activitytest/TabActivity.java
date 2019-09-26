package com.example.activitytest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.activitytest.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class TabActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//  레이아웃 구성
		setContentView(R.layout.activity_tab);

		/*

		 */

		//  ViewPager에 Fragment를 공급하는 Adapter
		SectionsPagerAdapter sectionsPagerAdapter =
				new SectionsPagerAdapter(this, getSupportFragmentManager());

		//  ViewPager의 참조값
		ViewPager viewPager = findViewById(R.id.view_pager);

		//  ViewPager의 Adapter 연결
		//  Fragment는 focus된 탭과 그 전후탭의 Fragment만 활성화 됨
		viewPager.setAdapter(sectionsPagerAdapter);

		//  TabLayout의 참조값
		TabLayout tabs = findViewById(R.id.tabs);

		//  TabLayout의 ViewPager 연결
		tabs.setupWithViewPager(viewPager);

		//  우하단의 플로팅버튼
		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(view ->
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
				.setAction("Action", null).show()
		);
	}
}