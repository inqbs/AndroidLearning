package com.example.step09sdcard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

	private final int REQ_WRITE_EXTERNAL_STORAGE = 0;
	private final int REQ_READ_EXTERNAL_STORAGE = 1;
	private final int REQ_READ_FILE = 2;
	private final int REQ_WRITE_FILE = 3;

	private EditText inputMsg;
	Button btnSave, btnRead;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		inputMsg = findViewById(R.id.inputMsg);

		//  저장버튼 클릭시
		btnSave = findViewById(R.id.btnSave);
		btnSave.setOnClickListener(v -> {
			int isAllow = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
			//  퍼미션 체크
			if(isAllow != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						REQ_WRITE_EXTERNAL_STORAGE);
				return;
			}

			Intent intent = new Intent()
					.setType("text/*")
					.setAction(Intent.ACTION_CREATE_DOCUMENT);
			startActivityForResult(intent, REQ_WRITE_FILE);
		});

		//  읽기 버튼 클릭시
		btnRead = findViewById(R.id.btnRead);
		btnRead.setOnClickListener(v->{
			Intent intent = new Intent()
					.setType("*/*")
					.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, REQ_READ_FILE);
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode == RESULT_OK){

			String path = data.getData().getPath();

			Log.i("data.getData()", path);

			switch(requestCode){
				case REQ_READ_FILE:
					inputMsg.setText(readData(path));
					break;
				case REQ_WRITE_FILE:
					saveData(path);
					break;
			}
		}else{
			return;
		}

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		switch(requestCode){
		case REQ_WRITE_EXTERNAL_STORAGE:
			if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
				//  Allowed
				btnSave.callOnClick();
			}else{
				//  Denied
				Toast.makeText(this, "저장소 권한은 필수 권한 입니다.", Toast.LENGTH_SHORT).show();
			}
			break;
		case REQ_READ_EXTERNAL_STORAGE:
			if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
				Toast.makeText(this, "저장소 권한은 필수 권한 입니다.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	//  데이터 읽기
	public String readData(String readPath){
		int isAllow = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
		//  퍼미션 체크
		if(isAllow != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
					REQ_READ_EXTERNAL_STORAGE);
			return "";
		}

		//  퍼미션 허용시

		//savePath = getExternalFilesDir(null).getAbsolutePath();
		//File file = new File(savePath+"/memo.txt");
		File file = new File(readPath);
		StringBuilder sb = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String data = "";
			while((data = br.readLine()) != null)
				sb.append(data);
			br.close();
			Toast.makeText(this, "파일 읽기 성공", Toast.LENGTH_SHORT).show();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		sb.trimToSize();
		return sb.toString();
	}

	//  외부저장장치에 저장
	public boolean saveData(String savePath){
		boolean result = false;

		//  데이터 취득
		String msg = inputMsg.getText().toString();
		//String savePath = getExternalFilesDir(null).getAbsolutePath();

		Log.d("저장경로",savePath);

		//  파일객체생성
		File file = new File(savePath);
		FileWriter fw = null;

		try {
			boolean isExisted = !file.createNewFile();  //  생성여부를 boolean으로 반환

			Log.i("파일존재여부",isExisted+"");

			fw = new FileWriter(file, true);
			fw.append(msg);
			fw.append("\r\n");

			result = true;

		} catch (IOException e) {
			Log.e("저장중 오류",e.getMessage());
		}finally {
			try {
				if(fw!=null)fw.close();
			} catch (IOException e) {}
		}

		return result;
	}
}
