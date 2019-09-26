package com.example.step12webview;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		WebView webView = findViewById(R.id.webView);

		WebSettings ws = webView.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setDomStorageEnabled(true);

		webView.setWebViewClient(new WebViewClient());

		//  asset 폴더의 index.html을 실행
		webView.loadUrl("file:///android_asset/www/index.html");

	}
}
