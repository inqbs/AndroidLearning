package com.example.step04customlistview.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.step04customlistview.R;
import com.example.step04customlistview.dto.CountryDto;

import java.util.List;

public class CountryAdapter extends BaseAdapter {

	//  필요 필드
	private Context context;
	private int layoutRes;  //  Layout xml
	private List<CountryDto> list;  //  모델 (CountryDTO)
	LayoutInflater inflater;    //  레이아웃 전개자 객체

	public CountryAdapter(Context context, int layoutRes, List<CountryDto> list) {
		this.context = context;
		this.layoutRes = layoutRes;
		this.list = list;

		//  레이아웃 전개자 객체는 xml문서를 전개해서 view객체를 만들어 줄 수 있는 객체
		inflater = LayoutInflater.from(context);    //  레이아웃 전개자 객체 참조값 get

	}

	@Override
	public int getCount() {
		return list.size(); //  모델의 전체 개수
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);  //  해당 인덱스의 item을 return
	}

	@Override
	public long getItemId(int position) {
		//  인자로 전달된 index에 해당되는 item의 primary 값을 리턴
		//  없으므로 임의로 index를 return
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		//처음에는 view 에 null 이 전달된다.
		if(view == null){
			//레이아웃 전개자 객체를 이용해서 셀 View 를 만든다.
			view = inflater.inflate(layoutRes, parent, false);
			//로그에 출력해보기
			Log.e("getView()","view == null");
		}
		//  최초 draw시 필요한 view만큼 생성되며, 이후엔 view객체를 만들 필요없이
		//  기 작성된 view를 재활용해서 return

		//셀에 전개된 객체의 참조값을 얻어온다.
		ImageView imageView = (ImageView)
				view.findViewById(R.id.imageView);
		TextView textView = (TextView)
				view.findViewById(R.id.textView);
		//position 에 해당하는 데이터를 모델에서 불러온다.
		CountryDto dto = list.get(position);
		//셀에 데이터를 출력한다
		imageView.setImageResource(dto.getImageResId());
		textView.setText(dto.getName());
		//구성된 셀 뷰를 리턴해준다.
		return view;
	}
}
