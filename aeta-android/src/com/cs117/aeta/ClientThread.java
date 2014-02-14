package com.cs117.aeta;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ClientThread extends Thread {

	private String mPeerAddress;
	private String mMessage;
	private int mPort;
	
	public ClientThread(MainActivity activity, String peerAddress, String msg, int port) {
		this.mPeerAddress = peerAddress;
		this.mMessage = msg;
		this.mPort = port;
	}
	
	@Override
	public void run() {
		try {
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
