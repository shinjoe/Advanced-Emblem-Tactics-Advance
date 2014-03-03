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
			// Create new server socket
			mServerSocket = new ServerSocket();
			mServerSocket.setReuseAddress(true);
			mServerSocket.bind(new InetSocketAddress(mPort));
			Socket clientSocket = null;
			
			// Loop until interrupted/killed
			currentThread();
			while (!Thread.interrupted()) {
				// Accept client connection, will block
				clientSocket = mServerSocket.accept();
				
				// Set peer IP address (group owner only)
				if (MainActivity.mPeerAddress == null) {
					String peerAddress = clientSocket.getInetAddress().getHostAddress();
					MainActivity.mPeerAddress = peerAddress;
					mActivity.runOnUiThread(new Runnable() {
	
						@Override
						public void run() {
							Toast.makeText(mActivity.getApplicationContext(), "Connected to peer w/ IP: " + MainActivity.mPeerAddress, Toast.LENGTH_SHORT).show();
						}
						
					});
				}
				
				// Read client input
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
			// Server socket can be closed by main thread, ServerSocket.accept() will throw exception
			// and thread will be stopped here
			Log.d(MainActivity.TAG, "Server thread exception: " + e.toString());
			return;
		}
	}
	
	private void updateView() {
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// Perform game-related processing
				
				if (mActivity.getInGame()) {	
					JSONObject fromClient = null;
					try
					{	
						fromClient = new JSONObject(mFromClient);
						Game curGame = mActivity.getGame();
						int type = fromClient.getInt("type");
						if(type == 0)
							curGame.tilemap.updateUnit(fromClient.getInt("newX"), fromClient.getInt("newY"),
													   fromClient.getInt("prevX"), fromClient.getInt("prevY"));
						else if (type == 1)
							curGame.tilemap.updateUnit(fromClient.getInt("atkedX"), fromClient.getInt("atkedY"),
													   fromClient.getInt("newHP"));
						else {
							curGame.curTurn = fromClient.getInt("turn");
							curGame.displayTurnReady();
							Toast.makeText(mActivity.getApplicationContext(), "Your turn", Toast.LENGTH_SHORT).show();
						}
					}
					catch(JSONException e) {
						e.printStackTrace();
						System.err.println("data send failure");
						Log.d(MainActivity.TAG, "Receive coord failure: " + e.toString());
					}
					
					Toast.makeText(mActivity.getApplicationContext(), mFromClient, Toast.LENGTH_SHORT).show();
				}
				// For connection phase, can probably do without
				else
					Toast.makeText(mActivity.getApplicationContext(), mFromClient + " from " + MainActivity.mPeerAddress, Toast.LENGTH_SHORT).show();
			}
			
		});
	}
	
	// Lets MainActivity stop thread
	public ServerSocket getServerSocket() {
		return mServerSocket;
	}
}
