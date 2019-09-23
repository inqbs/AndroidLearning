package com.example.step10etherwallet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigInteger;

public class DetailActivity extends AppCompatActivity {

	private Web3j web3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		String walletPath = getIntent().getStringExtra("wallet");

		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle(walletPath);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//  플로팅 버튼
		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(view ->
			Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
				.setAction("Action", null).show()
		);

		//  이더리움 지갑 정보 표시
		String url = "https://ropsten.infura.io/v3/9b5c92af0c384fb291b6c0f5991184c7";
		web3 = Web3jFactory.build(new HttpService(url));

		String pwd = getIntent().getStringExtra("pwd");

		Credentials cre = null;

		try {
			cre = WalletUtils.loadCredentials(pwd,
					getExternalFilesDir(null).getAbsoluteFile()+"/"+walletPath);
			String address = cre.getAddress();
			new GetBalanceTask().execute(address);

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(this.getLocalClassName()+":",e.getMessage());
		}

	}


	//Ether 잔액을 얻어오는 비동키 테스크
	class GetBalanceTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... strings) {
			//파라미터로 전달된 지갑주소
			String address = strings[0];

			EthGetBalance balance = null;
			String ether = null;
			try {
				// wei 단위의 잔액을 얻어온다.
				balance = web3.ethGetBalance(address,
						DefaultBlockParameterName.LATEST).sendAsync().get();
				BigInteger wei = balance.getBalance();
				// wei 단위를 ether 단위로 환산해서 문자열로 만든다.
				ether = Convert.fromWei(wei.toString(), Convert.Unit.ETHER).toString();
			} catch (Exception e) {
				//에러 메세지
				String errorMessage = e.getMessage();
				//에러 메세지를 진행중에 발행한다.
				publishProgress(errorMessage);
				//비동기 작업을 취소한다.
				cancel(true);
			}
			//결과 값을 리턴한다.
			return ether;
		}

		// publishProgress() 메소드를 호출하면 호출되는 메소드
		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			//UI 스레드
			Toast.makeText(DetailActivity.this,
					values[0], Toast.LENGTH_LONG).show();
		}

		//비동기 작업을 취소 했을때 호출되는 메소드
		@Override
		protected void onCancelled() {
			super.onCancelled();
			//UI 스레드
			new AlertDialog.Builder(DetailActivity.this)
					.setMessage("작업이 실패 했습니다.")
					.setNeutralButton("확인", null)
					.create()
					.show();
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			//여기는 UI 스레드
			new AlertDialog.Builder(DetailActivity.this)
					.setMessage(s + " Ether 의 잔액이 있습니다.")
					.setNeutralButton("확인", null)
					.create()
					.show();
		}
	}

}
