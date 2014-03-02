package com.cs117.wifi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import com.cs117.aeta.Game;

import android.util.Log;
import android.widget.Toast;

public class ServerThread extends Thread {

	private MainActivity mActivity;
	private String mFromClient = "";
	private int mPort;
	private ServerSocket mServerSocket = null;;
	
	public ServerThread(MainActivity activity, int port) {
		this.mActivity = activity;
		this.mPort = port;
	}
	
	@Override
	public void run() {
		try {
			mServerSocket = new ServerSocket();
			mServerSocket.setReuseAddress(true);
			mServerSocket.bind(new InetSocketAddress(mPort));
			Socket clientSocket = null;
			
			currentThread();
			while (!Thread.interrupted()) {
				clientSocket = mServerSocket.accept();
				if (MainActivity.mPeerAddress == null) {
					String peerAddress = clientSocket.getInetAddress().getHostAddress();
					MainActivity.mPeerAddress = peerAddress;
					mActivity.runOnUiThread(new Runnable() {
	
						@Override
						public void run() {
							Toast.makeText(mActivity.getApplicationContext(), "Connected to peer w/ IP: " + mActivity.getPeerAddress(), Toast.LENGTH_SHORT).show();
						}
						
					});
				}
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				mFromClient = in.readLine();
				
				updateView();
				
				in.close();
				clientSocket.close();
			}
			
			if (clientSocket != null)
				clientSocket.close();
			mServerSocket.close();	
		} catch (IOException e) {
			Log.d(MainActivity.TAG, "Server thread exception: " + e.toString());
			return;
		}
	}
	
	private void updateView() {
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (mActivity.getInGame())
				{	
					JSONObject fromClient = null;
					try
					{	
						fromClient = new JSONObject(mFromClient);
						Game curGame = mActivity.getGame();
						curGame.getTileMap().updateUnit(fromClient.getInt("newX"), fromClient.getInt("newY"),
													fromClient.getInt("prevX"), fromClient.getInt("prevY"));
					}
					catch(JSONException e) {
						//e.printStackTrace();
						//System.err.println("receive coord failure");
						Log.d(MainActivity.TAG, "receive coord failure: " + e.toString());
					}
					
					Toast.makeText(mActivity.getApplicationContext(), mFromClient, Toast.LENGTH_SHORT).show();
				}
				else
					Toast.makeText(mActivity.getApplicationContext(), mFromClient + " from " + mActivity.getPeerAddress(), Toast.LENGTH_SHORT).show();
			}
			
		});
	}
	
	public ServerSocket getServerSocket() {
		return mServerSocket;
	}
}
