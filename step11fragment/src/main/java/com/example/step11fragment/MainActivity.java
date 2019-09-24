package com.example.step11fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//  activity_main의 TextView
		TextView tv1 = findViewById(R.id.textView);
		Log.i("activity_main의 TextView", tv1.getText()+"");

		//  Fragment_my의 TextView
		TextView tv2 = findViewById(R.id.textView2);
		Log.i("Fragment_my의 TextView", tv2.getText()+"");

	}

	public static class MyFragment extends Fragment {

		//  프레그먼트 영역을 채울 View를 return
		@Nullable
		@Override
		public View onCreateView(@NonNull LayoutInflater inflater,
		                         @Nullable ViewGroup container,
		                         @Nullable Bundle savedInstanceState) {
			View view =inflater.inflate(R.layout.fragment_my,container);

			//  Fragment_my의 TextView는 view.findViewById
			//TextView tv2 = findViewById(R.id.textView2);
			TextView tv2 = view.findViewById(R.id.textView2);

			return view;
		}
	}

}
