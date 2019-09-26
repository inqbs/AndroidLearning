package com.example.activitytest.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.activitytest.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

	//  탭 제목 : res/values/string.xml
	@StringRes
	private static final int[] TAB_TITLES = new int[]{
			R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4, R.string.tab_text_5
	};

	//  Activity의 참조값
	private final Context mContext;

	//  생성자의 인자: Context(Activity), FragmentManager 객체
	public SectionsPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
	}

	//  ViewPager가 호출하는 Fragment를 return하는 메소드
	@Override
	public Fragment getItem(int position) {
		// 기본값: 해당 탭 index의 Fragment의 참조값을 return
		return PlaceholderFragment.newInstance(position + 1);

		//  기존 Fragment를 return하는 것도 가능

	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		//  해당 탭 index의 tab제목을 return
		return mContext.getResources().getString(TAB_TITLES[position]);
	}

	//  전체 페이지의 갯수 return
	@Override
	public int getCount() {
		return TAB_TITLES.length;
	}
}