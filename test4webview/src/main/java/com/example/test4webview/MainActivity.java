package com.example.test4webview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

	WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		webView = findViewById(R.id.webView);

		WebSettings settings = webView.getSettings();
		settings.setAllowFileAccess(true);  //  파일접근 허용
		settings.setJavaScriptEnabled(true);    //  JavaScript 허용
		settings.setDomStorageEnabled(true);    //  Web Storage 허용

		//  하드웨어 가속 허용
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

		webView.setWebViewClient(new MyWebViewClient());

		webView.loadUrl("http://192.168.0.93:8888/project/");

	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
			if(webView.canGoBack()) webView.goBack();
			else finish();
		}
		return super.onKeyDown(keyCode,event);
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

			Log.d("R.String.site_url", getResources().getString(R.string.site_url));
			Log.d("request.getUrl().getHost()",request.getUrl().getHost());

			if(getResources().getString(R.string.site_url).equals(request.getUrl().getHost()))
				return false;

			Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
			startActivity(intent);
			return true;
		}
	}


}
