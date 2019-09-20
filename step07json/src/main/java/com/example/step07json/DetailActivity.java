package com.example.step07json;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

	private Toolbar toolbar;
	private MemberDto dto;

	private EditText inputName, inputAddr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getSupportActionBar().hide();
		setContentView(R.layout.activity_detail);

		dto = (MemberDto) getIntent().getSerializableExtra("dto");

		toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle(dto.getName());

		inputName = findViewById(R.id.inputName);
		inputAddr = findViewById(R.id.inputAddr);

		inputName.setText(dto.getName());
		inputAddr.setText(dto.getAddr());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.tollbar_menu, menu);
		return true;
	}
}
