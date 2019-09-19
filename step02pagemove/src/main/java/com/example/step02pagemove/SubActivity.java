package com.example.step02pagemove;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity implements View.OnClickListener {

	private EditText inputEmail, inputPwd;
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub);

		inputEmail = findViewById(R.id.inputEmail);
		inputPwd = findViewById(R.id.inputPwd);

		findViewById(R.id.loginBtn).setOnClickListener(this);

		textView = findViewById(R.id.textView);

		findViewById(R.id.endBtn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		Log.i("target",v.getId()+"");

		//  EventListener의 메소드의 파라미터 View에는 이벤트 타겟이 매개변수로 들어옴
		//  각 컴포넌트에 할당된 ID를 통해 eventTarget을 구분한다.
		//  id는 v.getId();

		switch(v.getId()){
			case R.id.loginBtn:
				//  문자열을 EditText에서 얻어오기
				String email = inputEmail.getText().toString();
				String pwd = inputPwd.getText().toString();

				//  얻어온 문자열을 TextView에 출력
				textView.setText(email+":"+pwd);

				//  EditText 내용 제거
				inputEmail.setText("");
				inputPwd.setText("");
				break;

			case R.id.endBtn:
				this.finish();
				break;
		}

	}
}
