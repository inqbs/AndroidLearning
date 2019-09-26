package com.example.activitytest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.activitytest.ui.viewmodel.ViewModelFragment;

public class ViewModelActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_model_activity);


		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()  //  FragmentManger를 get
					.replace(R.id.container, ViewModelFragment.newInstance())   // a를 b로 replace
					.commitNow();   //  실행
		}
	}
}
