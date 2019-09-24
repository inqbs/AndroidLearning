package com.example.step10etherwallet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

	//  Etherium
	private Web3j web3;
	private Credentials cre;

	//  현재계정
	private String currentAddress;
	private double currentBalance;

	//  UI View
	private TextView textAddr, textBalance;
	private EditText inputAddr, inputEth;
	private FloatingActionButton fab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		String walletPath = getIntent().getStringExtra("wallet");

		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle(walletPath);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//  UI VIew
		textAddr = findViewById(R.id.textAddr);
		textBalance = findViewById(R.id.textBalance);
		inputAddr = findViewById(R.id.inputAddr);
		inputEth = findViewById(R.id.inputEther);

		//  플로팅 버튼
		fab = findViewById(R.id.fab);
		fab.setOnClickListener(this);

		//  이더리움 지갑 정보 표시
		String url = "https://ropsten.infura.io/v3/9b5c92af0c384fb291b6c0f5991184c7";
		web3 = Web3jFactory.build(new HttpService(url));

		String pwd = Optional.ofNullable(getIntent().getStringExtra("pwd")).orElse("test");

		try {
			//  인증서 객체
			cre = WalletUtils.loadCredentials(pwd,
					getExternalFilesDir(null).getAbsoluteFile()+"/"+walletPath);
		} catch (Exception e) {
			Log.e(this.getLocalClassName()+":",e.getMessage());
		}

		currentAddress = cre.getAddress();
		textAddr.setText(currentAddress);
		new GetBalanceTask().execute(currentAddress);

	}

	//  액션바 뒤로가기 버튼
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch(item.getItemId()){
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.fab:
				//  송금
				double currentBalance = Double.parseDouble(textBalance.getText()+"");
				double sendEth = Double.parseDouble(inputEth.getText()+"");
				if(currentBalance > sendEth)
					new SendTask().execute(inputAddr.getText().toString(),sendEth+"");
				else
					Toast.makeText(this, "잔액이 부족합니다", Toast.LENGTH_SHORT).show();
				break;
		}
	}

	class SendTask extends AsyncTask<String, Void, TransactionReceipt> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			Toast.makeText(DetailActivity.this, "송금 시작...", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected TransactionReceipt doInBackground(String... strings) {
			String address = strings[0];
			double eth = Double.parseDouble(strings[1]);

			//  영수증 객체
			TransactionReceipt receipt = null;
			try {
				receipt = Transfer.sendFunds(web3, cre,
						address, BigDecimal.valueOf(eth), Convert.Unit.ETHER)
							.sendAsync().get();
			} catch (Exception e) {
				Log.e("SendTask",e.getMessage());
				e.printStackTrace();
			}

			return receipt;
		}

		@Override
		protected void onPostExecute(TransactionReceipt transactionReceipt) {
			super.onPostExecute(transactionReceipt);

			if(transactionReceipt!=null){
				//  트렌젝션이 담긴 블럭의 Hash
				String blockHash = transactionReceipt.getBlockHash();
				//  사용 Gas
				BigInteger usedGas = transactionReceipt.getGasUsed();

				Log.d("blockHash", blockHash);
				Log.d("usedGas", usedGas+"");

				Toast.makeText(DetailActivity.this, "작업완료", Toast.LENGTH_LONG).show();

				//  잔고 업데이트
				new GetBalanceTask().execute(currentAddress);
			}else{
				Toast.makeText(DetailActivity.this, "통신 실패", Toast.LENGTH_LONG).show();
			}

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

			Snackbar.make(findViewById(R.id.tableLayout), "작업이 실패했습니다.", Snackbar.LENGTH_SHORT).show();

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

			textBalance.setText(s + "Ether");
		}
	}


}
