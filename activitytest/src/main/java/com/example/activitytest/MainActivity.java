package com.example.activitytest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.moveBtn1).setOnClickListener(this);
		findViewById(R.id.moveBtn2).setOnClickListener(this);
		findViewById(R.id.moveBtn3).setOnClickListener(this);
		findViewById(R.id.moveBtn4).setOnClickListener(this);
		findViewById(R.id.moveBtn5).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent intent = null;

		switch(v.getId()){
			case R.id.moveBtn1:
				intent = new Intent(this,TabActivity.class);
				break;
			case R.id.moveBtn2:
				intent = new Intent(this, ViewModelActivity.class);
				break;
			case R.id.moveBtn3:
				Log.i("Main","moveBtn3");
				intent = new Intent(this, DrawerActivity.class);
				break;
			default:
			//  case intent == null:
				return;
		}

		startActivity(intent);
	}
}
