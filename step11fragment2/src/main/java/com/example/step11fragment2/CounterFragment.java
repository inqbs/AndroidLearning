package com.example.step11fragment2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

//  터치횟수를 카운트해서 표시하는 Fragment
public class CounterFragment extends Fragment implements View.OnTouchListener {

	private int cnt;
	private TextView textCount;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_count, container);

		textCount = view.findViewById(R.id.textCount);

		view.setOnTouchListener(this);

		return view;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		textCount.setText(getString(R.string.txt_count)+(++cnt));

		if(cnt%100==0) publishCount();
		return false;
	}

	public int getCnt() {
		return cnt;
	}

	public void publishCount(){
		//  이 Fragment를 제어하는 액티비티의 참조값
		FragmentActivity fa = getActivity();    //  FragmentActivity Type으로 소환

		if(!(fa instanceof CounterFragmentListener)) return;    //  미 구현이라면 실행 끝

		((CounterFragmentListener)fa).onPublishEvent(cnt);

	}

}
