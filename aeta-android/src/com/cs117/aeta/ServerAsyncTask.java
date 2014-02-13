package com.cs117.aeta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

public class ServerAsyncTask extends AsyncTask<Void, Void, Void> {

	private ServerSocket mServerSocket;
	private Socket mClientSocket;
	private MainActivity mActivity;
	
	public ServerAsyncTask(MainActivity activity) {
		this.mActivity = activity;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		try {
			mServerSocket = new ServerSocket(MainActivity.SERVER_PORT);
			mClientSocket = mServerSocket.accept();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
			
			final String str = in.readLine();
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					//Toast.makeText(mActivity.getApplicationContext(), str, Toast.LENGTH_LONG).show();
					((EditText)mActivity.findViewById(R.id.editText1)).setText(str);
				}
				
			});
			
			in.close();
			mServerSocket.close();
		} catch (IOException e) {
			// herp a derp
			return null;
		}
		return null;
	}

}
