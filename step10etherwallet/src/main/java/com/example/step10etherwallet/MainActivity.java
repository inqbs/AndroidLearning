package com.example.step10etherwallet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
		implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

	private static final int REQ_MAKE_WALLET = 0;

	private ListView listView;
	private ArrayAdapter<String> adapter;
	private List<String> walletList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)

	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		//  리스트 뷰
		listView = findViewById(R.id.listView);
		adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,walletList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);

		/*
		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(view ->
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show());
		 */

		//  추가버튼 클릭시 동작
		findViewById(R.id.addBtn).setOnClickListener(v -> {
			int isAllow = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
			//  퍼미션 체크
			if (isAllow != PackageManager.PERMISSION_GRANTED){
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						REQ_MAKE_WALLET);
				return;
			}
			makeWallet();
		});

		showWalletList();
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch(requestCode){
			case REQ_MAKE_WALLET:
				if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
					makeWallet();
				else
					Toast.makeText(this, "권한 설정을 유효화 해주세요", Toast.LENGTH_SHORT).show();
				break;
		}
	}


	//  지갑 목록 보여주는 메소드
	void showWalletList(){
		walletList.clear();

		//  생성된 지갑 목록
		File[] files = getExternalFilesDir(null).listFiles();
		Arrays.stream(files).forEachOrdered(e->walletList.add(e.getName()));
	}


	//  지갑 만드는 메소드
	void makeWallet(){
		//  password_input.xml을 활용한 View 객체 생성
		LayoutInflater inflater = LayoutInflater.from(this);
		View pwdView = inflater.inflate(R.layout.password_input,
				findViewById(R.id.linearLayout),false);

		//  inputPwd에서 값을 얻기 위한 정의
		final EditText inputPwd = pwdView.findViewById(R.id.inputPwd);

		new AlertDialog.Builder(this)
				.setTitle("비밀번호를 입력하세요")
				.setView(pwdView)
				.setPositiveButton("확인", (dialog, which) -> {
					String pwd = inputPwd.getText().toString();

					//  지갑정보를 저장할 json파일 생성
					File file = getExternalFilesDir(null);
					String fileName = new String();
					try {
						fileName = WalletUtils.generateLightNewWalletFile(pwd, file);

						//  파일 저장 확인
						String path = file.getAbsolutePath();
						String location = path+"/"+fileName;

						//  인증 객체
						Credentials cre = WalletUtils.loadCredentials(pwd, location);

						//  지갑주소
						String address = cre.getAddress();

						//  공개키
						String publicKey = Numeric.toHexStringWithPrefix(
								cre.getEcKeyPair().getPublicKey()
						);

						//  개인키
						String privateKey = Numeric.toHexStringWithPrefix(
								cre.getEcKeyPair().getPrivateKey()
						);

						Log.d("생성경로", location);
						Log.i("주소", address);
						Log.i("공개키", publicKey);
						Log.i("개인키", privateKey);

						Toast.makeText(this, "새 지갑이 생성되었습니다.", Toast.LENGTH_SHORT).show();

						showWalletList();

					} catch (Exception e) {
						Log.e("makeWallet", e.getMessage());
					}


				})
				.setNegativeButton("취소",null)
				.create().show();
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//  password_input.xml을 활용한 View 객체 생성
		LayoutInflater inflater = LayoutInflater.from(this);
		View pwdView = inflater.inflate(R.layout.password_input,
				findViewById(R.id.linearLayout),false);

		//  inputPwd에서 값을 얻기 위한 정의
		final EditText inputPwd = pwdView.findViewById(R.id.inputPwd);

		new AlertDialog.Builder(this)
				.setTitle("비밀번호를 입력해 주세요")
				.setView(pwdView)
				.setPositiveButton("입력", (dialog, which) -> {

					//  이부분 빈값이 return
					String pwd = inputPwd.getText().toString();

					if(!TextUtils.isEmpty(pwd)){
						Intent intent=new Intent(MainActivity.this,
								DetailActivity.class);
						intent.putExtra("wallet", walletList.get(position));
						intent.putExtra("pwd", pwd);
						startActivity(intent);
					}else{
						Toast.makeText(this, "Pwd 공란 : "+inputPwd.length(), Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton("취소", null)
				.create().show();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		int isAllow = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (isAllow != PackageManager.PERMISSION_GRANTED){
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
					REQ_MAKE_WALLET);
			return true;
		}
		new AlertDialog.Builder(this)
				.setTitle("경고")
				.setMessage("이 지갑을 삭제하시겠습니까?")
				.setPositiveButton("삭제", (dialog, which) -> {

				})
				.setNegativeButton("취소", null)
				.create().show();
		return true;
	}
}
