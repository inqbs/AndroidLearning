package com.example.activitytest.ui.viewmodel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.activitytest.R;

public class ViewModelFragment extends Fragment {

	private ViewModelViewModel mViewModel;

	public static ViewModelFragment newInstance() {
		return new ViewModelFragment();
	}

	/*
		Fragment 생성 -> 활성화 -> 활성화 후 작업
		onCreate() => onCreateView() => onActivityCreated()
	 */

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.view_model_fragment, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mViewModel = ViewModelProviders.of(this).get(ViewModelViewModel.class);
		// TODO: Use the ViewModel
	}

}
