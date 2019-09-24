package com.example.step11fragment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CounterFragmentListener {

	CounterFragment cf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.moveBtn).setOnClickListener(this);
		findViewById(R.id.btnGetCount).setOnClickListener(this);

		//  Fragment의 참조값?
		//  findViewById(R.id.countFragment) -> fragment는 view가 아니다.

		//  FragmentManager에서 fineFragmentById를 통해 찾을 수 있다.
		FragmentManager fm = getSupportFragmentManager();
		cf = (CounterFragment) fm.findFragmentById(R.id.countFragment);

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btnGetCount:
				Toast.makeText(this, cf.getCnt()+"회", Toast.LENGTH_SHORT).show();
				break;
			case R.id.moveBtn:
				startActivity(new Intent(this,SubActivity.class));
				break;
		}
	}

	@Override
	public void onPublishEvent(int cnt) {
		Toast.makeText(this, cnt/100+"번 경과", Toast.LENGTH_SHORT).show();
	}
}
