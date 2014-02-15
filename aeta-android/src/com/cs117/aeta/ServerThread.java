package com.cs117.aeta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.widget.EditText;
import android.widget.Toast;

public class ServerThread extends Thread {

	private MainActivity mActivity;
	private String mFromClient = "";
	private String mPeerAddress = "";
	private int mPort;
	
	public ServerThread(MainActivity activity, int port) {
		this.mActivity = activity;
		this.mPort = port;
	}
	
	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(mPort);
			
			currentThread();
			while (!Thread.interrupted()) {
				Socket clientSocket = serverSocket.accept();
				if (mPeerAddress.equals("")) {
					mPeerAddress = clientSocket.getInetAddress().getHostAddress();
					mActivity.setPeerAddress(mPeerAddress);
				}
				
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(mActivity.getApplicationContext(), "peer IP: " + mPeerAddress, Toast.LENGTH_SHORT).show();
					}
					
				});
				
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				mFromClient = in.readLine();
				
				updateView();
				
				in.close();
				clientSocket.close();
			}
			
			serverSocket.close();	
		} catch (IOException e) {
			return;
		}
	}
	
	public void updateView() {
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (mFromClient.contains("Touch/Click"))
					Toast.makeText(mActivity.getApplicationContext(), mFromClient, Toast.LENGTH_LONG).show();
				else
					((EditText)mActivity.findViewById(R.id.editText1)).setText(mFromClient);
			}
			
		});
	}
}
