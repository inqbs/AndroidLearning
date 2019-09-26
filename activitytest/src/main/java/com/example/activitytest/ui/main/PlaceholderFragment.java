package com.example.activitytest.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.activitytest.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

	private static final String ARG_SECTION_NUMBER = "section_number";

	private PageViewModel pageViewModel;

	//  외부에서 호출하는 static method
	//  샘플 Fragment를 return
	public static PlaceholderFragment newInstance(int index) {

		//  Fragment 생성
		PlaceholderFragment fragment = new PlaceholderFragment();
		//  Bundle(Fragment에 전달) 생성
		Bundle bundle = new Bundle();
		//  Bundle에 {ARG_SECTION_NUMBER : index} put
		bundle.putInt(ARG_SECTION_NUMBER, index);
		//  fragment의 argument로 전달
		fragment.setArguments(bundle);
		return fragment;
	}

	//  Fragment가 초기에 생성될시
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
		int index = 1;
		if (getArguments() != null) {
			index = getArguments()  //  argument를 get
					.getInt(ARG_SECTION_NUMBER);    //  해당 키의 value값을 int로 get
		}
		pageViewModel.setIndex(index);
	}

	//  Activity에서 Fragment를 호출했을때
	@Override
	public View onCreateView(
			@NonNull LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_tab, container, false);
		final TextView textView = root.findViewById(R.id.section_label);
		pageViewModel.getText().observe(this, s -> textView.setText(s));
		return root;
	}
}