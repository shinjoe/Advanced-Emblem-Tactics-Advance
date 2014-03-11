package com.cs117.wifi;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import android.util.Log;
import android.widget.Toast;

public class ClientThread extends Thread {

	private MainActivity mActivity;
	private String mMessage = "";
	private int mPort = 0;
	
	public ClientThread(MainActivity activity, String msg, int port) {
		this.mActivity = activity;
		this.mMessage = msg;
		this.mPort = port;
	}
	
	@Override
	public void run() {
		try {
			// Stop thread if unable to send
			if (MainActivity.mPeerAddress == null) {
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
					}
					
				});
				
				return;
			}
			
			Socket socket = new Socket(MainActivity.mPeerAddress, mPort);
			
			// Send client output
			PrintStream out = new PrintStream(socket.getOutputStream());
			out.println(mMessage);
						
			out.close();
			socket.close();
		} catch (IOException e) {
			Log.d(MainActivity.TAG, "Client thread exception: " + e.toString());
			return;
		}
	}
}
