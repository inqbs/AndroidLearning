package com.example.test4webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

	WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		webView = findViewById(R.id.webView);

		WebSettings settings = webView.getSettings();
		settings.setAllowFileAccess(true);  //  파일접근 허용
		settings.setAllowFileAccessFromFileURLs(true);
		settings.setJavaScriptEnabled(true);    //  JavaScript 허용
		settings.setDomStorageEnabled(true);    //  Web Storage 허용

		//  하드웨어 가속 허용
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

		webView.setWebChromeClient(new MyChromeClient());
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

		//  링크 클릭시 앱의 WebView에서 이동되도록
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

			if(getResources().getString(R.string.site_url).equals(request.getUrl().getHost()))
				return false;

			Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
			startActivity(intent);
			return true;
		}

	}

	//  WebView에서 input:file을 처리하기 위한 코드

	public class MyChromeClient extends WebChromeClient {

		// For Android Version < 3.0
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			//System.out.println("WebViewActivity OS Version : " + Build.VERSION.SDK_INT + "\t openFC(VCU), n=1");
			mUploadMessage = uploadMsg;
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType(TYPE_IMAGE);
			startActivityForResult(intent, INPUT_FILE_REQUEST_CODE);
		}

		// For 3.0 <= Android Version < 4.1
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

			openFileChooser(uploadMsg, acceptType, "");
		}

		// For Android 4.1+

		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

			openFileChooser(uploadMsg, acceptType);
		}

		// For Android 5.0+

		public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadFile, WebChromeClient.FileChooserParams fileChooserParams) {


			if(mFilePathCallback !=null) {
				mFilePathCallback.onReceiveValue(null);
				mFilePathCallback = null;
			}

			mFilePathCallback = uploadFile;
			Intent i =new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");

			startActivityForResult(Intent.createChooser(i, "File Chooser"), INPUT_FILE_REQUEST_CODE);

			return true;

		}


		private void imageChooser() {
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
				// Create the File where the photo should go
				File photoFile = null;
				try {
					photoFile = createImageFile();
					takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
				} catch (IOException ex) {
					// Error occurred while creating the File
					Log.e(getClass().getName(), "Unable to create Image File", ex);
				}

				// Continue only if the File was successfully created
				if (photoFile != null) {
					mCameraPhotoPath = "file:"+photoFile.getAbsolutePath();
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(photoFile));
				} else {
					takePictureIntent = null;
				}
			}

			Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
			contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
			contentSelectionIntent.setType(TYPE_IMAGE);

			Intent[] intentArray;
			if(takePictureIntent != null) {
				intentArray = new Intent[]{takePictureIntent};
			} else {
				intentArray = new Intent[0];
			}

			Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
			chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
			chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
			chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

			startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
		}


	}
	//변수
	private static final String TYPE_IMAGE = "image/*";
	private static final int INPUT_FILE_REQUEST_CODE = 1;

	private ValueCallback<Uri> mUploadMessage;
	private ValueCallback<Uri[]> mFilePathCallback;
	private String mCameraPhotoPath;

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File imageFile = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
		);
		return imageFile;
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == INPUT_FILE_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					if (mFilePathCallback == null) {
						super.onActivityResult(requestCode, resultCode, data);
						return;
					}
					Uri[] results = new Uri[]{data.getData()};

					mFilePathCallback.onReceiveValue(results);
					mFilePathCallback = null;
				} else {
					if (mUploadMessage == null) {
						super.onActivityResult(requestCode, resultCode, data);
						return;
					}
					Uri result = data.getData();

					Log.d(getClass().getName(), "openFileChooser : " + result);
					mUploadMessage.onReceiveValue(result);
					mUploadMessage = null;
				}
			} else {
				if (mFilePathCallback != null) mFilePathCallback.onReceiveValue(null);
				if (mUploadMessage != null) mUploadMessage.onReceiveValue(null);
				mFilePathCallback = null;
				mUploadMessage = null;
				super.onActivityResult(requestCode, resultCode, data);
			}
		}
	}
}
