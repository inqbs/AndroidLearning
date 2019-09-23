package com.example.step08web3j;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	private EditText inputAddr;
	private Web3j web3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btnGet).setOnClickListener(this);

		inputAddr = findViewById(R.id.inputAddress);

		String url = "https://ropsten.infura.io/v3/9b5c92af0c384fb291b6c0f5991184c7";

		web3 = Web3jFactory.build(new HttpService(url));
	}

	@Override
	public void onClick(View v) {
		String address = inputAddr.getText().toString();
		new GetBalanceTask().execute(address);

	}

	private class GetBalanceTask extends AsyncTask<String, String, String>{


		@Override
		protected String doInBackground(String... strings) {
			String ether = "";

			try {
				EthGetBalance balance = web3.ethGetBalance(strings[0],
						DefaultBlockParameterName.LATEST)
						.sendAsync().get();

				BigInteger wei = balance.getBalance();
				ether = Convert.fromWei(wei.toString(), Convert.Unit.ETHER).toString();

			} catch (Exception e) {
				publishProgress(e.getMessage());
				cancel(true);
			}
			return ether;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);

			new AlertDialog.Builder(MainActivity.this)
					.setMessage(s+"Ether의 잔액이 있습니다.")
					.setNeutralButton("확인", null)
					.create().show();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			new AlertDialog.Builder(MainActivity.this)
					.setMessage("작업이 실패했습니다.")
					.setNeutralButton("확인", null)
					.create().show();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			Toast.makeText(MainActivity.this, values[0], Toast.LENGTH_LONG).show();
		}
	}

}
