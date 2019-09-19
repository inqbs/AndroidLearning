package com.example.step01layout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

/*
	Context > Activity > AppCompatActivity > MainActivity
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //  R.layout : res/layout; activity_main : activity_main.xml;
        setContentView(R.layout.activity_main);

        //  Send 버튼의 참조값
	    //  findViewById(int n)의 n은 res내의 요소를 16자리 수로 부여한 정수값
	    //  @+id/sendBtn == R.id.sendBtn;
	    Button sendBtn = findViewById(R.id.sendBtn);
	    //  이 Activity에 OnCLickListener를 구현하고, 그것을 sendBtn의 리스너로 등록
	    sendBtn.setOnClickListener(this);

        //  Type
	    Activity a = this;  //  Activity에서 상속받은 MainActivity
	    //String b = this;  //  String에서 상속받지 않으므로 컴파일 에러
	    Context b = this;   //  Context에서 상속받은 MainActivity

    }

    //  버튼 클릭시 호출
    public void btnClicked(View view){
        Log.i("클릭 테스트", "클릭 됨");

        //  토스트 알림
        Toast.makeText(this, "버튼 눌림", Toast.LENGTH_SHORT).show();
        //  makeText의 파라미터중 context
    }

    public void btnClicked2(View view){
        Toast.makeText(this, "다른 버튼이 누림", Toast.LENGTH_LONG).show();
    }


	@Override
	public void onClick(View v) {
    	//findViewById(R.id.inputColorCode).
		String colorCode = String.format("#%06x",new Random().nextInt(0xFFFFFF + 1));
		Toast.makeText(this,"결과 : "+colorCode, Toast.LENGTH_SHORT).show();
		findViewById(R.id.bg).setBackgroundColor(Color.parseColor(colorCode));
	}
}
