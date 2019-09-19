package com.example.step02pagemove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button moveBtn = findViewById(R.id.moveBtn);
		moveBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		//  Activity를 open하기 위해선 intent객체 생성이 필요
		Intent intent = new Intent(this,signInTest.class);
		startActivity(intent);
	}
}
