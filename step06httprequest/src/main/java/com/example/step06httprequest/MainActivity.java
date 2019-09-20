package com.example.step06httprequest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Util.RequestListener {

	private EditText inputUrl, consoleArea;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		inputUrl = findViewById(R.id.inputURL);
		consoleArea = findViewById(R.id.console);
		consoleArea.setKeyListener(null);

		findViewById(R.id.requestBtn).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		String url = inputUrl.getText().toString();
		//  new GetHttpRequest().execute(url);
		Util.sendGetRequest(1,url,null, this);
	}

	@Override
	public void onSuccess(int requestId, Map<String, Object> result) {

	}

	@Override
	public void onFail(int requestId, Map<String, Object> result) {

	}

	class GetHttpRequest extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... strings) {
			HttpURLConnection conn;
			String result="";
			try {
				URL url = new URL(strings[0]);
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(10000);

				conn.setDoOutput(true);
				conn.setRequestMethod("GET");
				conn.setDefaultUseCaches(false);
				int responseCode = conn.getResponseCode();

				//서버에서 출력하는 문자열을 누적시킬 객체
				StringBuilder builder = new StringBuilder(); //객체 생성

				Log.i("code",responseCode+"");

				if (responseCode == HttpURLConnection.HTTP_OK) {
					//InputStreamReader 객체 얻어오기
					InputStreamReader isr =
							new InputStreamReader(conn.getInputStream());
					BufferedReader br = new BufferedReader(isr);

					//반복문 돌면서 읽어온 문자열을 객체에 저장
					/*
					while(true){
						String line = br.readLine();
						if(line==null)break;
						builder.append(line);
					}*/
					for (String line=br.readLine();line!=null;line=br.readLine())
						builder.append(line);

					isr.close();
					br.close();
				}

				//  StringBuilder에 누적된 문자열을 String
				result = builder.toString();

			} catch (IOException e) {
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			consoleArea.setText(s);
		}
	}

}
