package com.cs117.aeta;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import android.widget.Toast;

public class ClientThread extends Thread {

	private MainActivity mActivity;
	private String mPeerAddress = "";
	private String mMessage = "";
	private int mPort = 0;
	
	public ClientThread(MainActivity activity, String peerAddress, String msg, int port) {
		this.mActivity = activity;
		this.mPeerAddress = peerAddress;
		this.mMessage = msg;
		this.mPort = port;
	}
	
	@Override
	public void run() {
		try {
			if (mPeerAddress.equals("")) {
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(mActivity.getApplicationContext(), "No peer IP address.", Toast.LENGTH_SHORT).show();
					}
					
				});
				
				return;
			}
			
			Socket socket = new Socket(mPeerAddress, mPort);
			
			PrintStream out = new PrintStream(socket.getOutputStream());
			out.println(mMessage);
						
			out.close();
			socket.close();
		} catch (IOException e) {
			return;
		}
	}
}
