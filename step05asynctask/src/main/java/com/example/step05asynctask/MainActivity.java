package com.example.step05asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/*
*   App이 Launch되고 사용되는 스레드를 UI스레드라고 한다.
*   UI 스레드에서는 소요시간이 많거나 불확실한 작업x (OS가 프로세스를 kill할 수 있음)
*   Download, HTTP 요청 등의 작업은 새로운 스레드를 만들어야 함.
*   UI 업데이트는 UI스레드에서만 가능.
*
*   위 문제를 개선하기 위한 AsyncTask
*/

public class MainActivity extends AppCompatActivity {

	private TextView progressText;
	private ProgressBar progressBar;
	private Button btn;
	private List<ImageView> resultImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		progressText = findViewById(R.id.progressText);
		progressBar = findViewById(R.id.progressBar);
		btn = findViewById(R.id.startBtn);

		resultImage = new ArrayList<>();
		resultImage.add(findViewById(R.id.imageResult1));
		resultImage.add(findViewById(R.id.imageResult2));
		resultImage.add(findViewById(R.id.imageResult3));
		resultImage.add(findViewById(R.id.imageResult4));
		resultImage.add(findViewById(R.id.imageResult5));
		resultImage.add(findViewById(R.id.imageResult6));

		progressBar.setMax(100);

	}

	//  AsyncTask 객체를 생성, 실행하는 메소드
	public void start(View v){
		new CounterTask().execute("로또테스트");
	}

	//  AsyncTask를 상속받은 객체
	public class CounterTask extends AsyncTask<String, Integer, List<Bitmap>> {
		//  AsyncTask<a,b,c>의 3가지 제네릭타입
		//  a : parameter Type, b : execute Type(진행중일떄 return할 데이터), c: return Type
		//  b는 onProgressUpdate의 인자로 전달됨.
		//  필요없는 객체는 Void 선언 (ex: (Void, String, Object), (Integer, String , Void) ...)

		//  Background에서 작업할 내용
		//  UI 스레드는 아님

		@Override
		protected List<Bitmap> doInBackground(String... strings) {
			Set<Integer> lotto = new HashSet<>();
			String baseURL = "http://10.0.2.2:5500/img-";   //  LocalHost in Emulator
			List<Bitmap> lottoImg = new ArrayList<>();

			//  번호 생성 (HashSet을 통해 중복값 제거)
			Random rand = new Random();
			IntStream.range(0,6).forEach(i -> {
				//  HashSet.add()는 add의 결과를 boolean으로 return
				while(!lotto.add(rand.nextInt(45)+1));
			});

			AtomicInteger progress = new AtomicInteger();

			//  번호에 맞는 이미지 불러오기
			lotto.stream().sorted().forEachOrdered(i->{
				try {
					URL url = new URL(baseURL+i+".jpg");

					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setDoInput(true);
					conn.connect();

					InputStream is = conn.getInputStream();
					lottoImg.add(BitmapFactory.decodeStream(is));
					publishProgress((progress.incrementAndGet()) /6*100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			return lottoImg;
		}

		//  execute 전에 실행할 메소드
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressText.setText("0%");
			progressBar.setProgress(0,false);
			btn.setText("중단");
		}

		//  execute 도중에 실행할 메소드
		//  publishProgress를 통해 호출됨
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);

			//  이 메소드는 UI스레드에서 실행된다.
			progressText.setText(values[0]+"%");
			progressBar.setProgress(values[0], true);
		}

		//  execute가 끝난후 실행할 메소드
		@Override
		protected void onPostExecute(List<Bitmap> list) {

			super.onPostExecute(list);

			// 매개변수는 doInBackground의 return 데이터이다.
			//  이 메소드는 UI스레드에서 실행된다.
			if(list.size()>0){
				progressText.setText("Success!");
				IntStream.range(0,list.size()).forEach(i->{
					resultImage.get(i).setImageBitmap(list.get(i));
					progressBar.setProgress(100,true);
				});
				/*
				for(int i=0; i<list.size(); i++){
					resultImage.get(i).setImageBitmap(list.get(i));
					progressBar.setProgress(100,true);
				}
				 */
			}else{
				progressText.setText("Failed..");
			}
			btn.setText("생성하기");
		}

	}
}
