package com.cs117.aeta;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;

public class ClientAsyncTask extends AsyncTask<Void, Void, Void> {

	private String mServerAddress;
	private String mMessage;
	
	public ClientAsyncTask(String serverAddress, String msg) {
		this.mServerAddress = serverAddress;
		this.mMessage = msg;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		try {
			Socket socket = new Socket(mServerAddress, MainActivity.SERVER_PORT);
			PrintStream out = new PrintStream(socket.getOutputStream());
			out.println(mMessage);
			out.close();
			socket.close();
		} catch (UnknownHostException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return null;
	}

}
