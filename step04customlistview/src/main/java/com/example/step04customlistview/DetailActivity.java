package com.example.step04customlistview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.step04customlistview.dto.CountryDto;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

	CountryDto dto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		//  MainActivity에서 전달한 Intent 객체는 getIntent()로 획득가능

		dto = (CountryDto)(getIntent().
				getSerializableExtra("dto"));

		ImageView imageView = findViewById(R.id.detailImage);
		TextView textView = findViewById(R.id.detailName);

		imageView.setImageResource(dto.getImageResId());
		textView.setText(dto.getName());

		imageView.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("https://www.google.com/search?q="+dto.getName()));
		startActivity(intent);
	}
}
