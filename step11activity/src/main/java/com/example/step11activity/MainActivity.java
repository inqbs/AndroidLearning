package com.example.step11activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		findViewById(R.id.btnAlert).setOnClickListener(this);
		findViewById(R.id.btnMove).setOnClickListener(this);

		Log.i(this.getLocalClassName(), "onCreate() 호출");
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btnAlert:
				new AlertDialog.Builder(this)
						.setMessage(R.string.msg_alert)
						.setNeutralButton(R.string.btn_confirm, null)
						.create().show();
				break;
			case R.id.btnMove:
				startActivity(new Intent(this, SubActivity.class));
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.i(this.getLocalClassName(), "onResume() 호출");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(this.getLocalClassName(), "onPause() 호출");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(this.getLocalClassName(),"onStart() 호출");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(this.getLocalClassName(),"onStop() 호출");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i(this.getLocalClassName(),"onRestart() 호출");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(this.getLocalClassName(),"onDestroy() 호출");
	}

}
