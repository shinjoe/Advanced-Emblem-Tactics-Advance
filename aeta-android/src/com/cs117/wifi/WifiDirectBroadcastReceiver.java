package com.cs117.wifi;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.widget.Toast;

/**
 * 
 * This class is adapted from the Android Developer "Wi-Fi Peer-to-Peer" and 
 * "Creating P2P Connections with Wi-Fi" guides at
 * http://developer.android.com/guide/topics/connectivity/wifip2p.html and
 * http://developer.android.com/training/connect-devices-wirelessly/wifi-direct.html.
 * 
 * @author Android
 *
 */

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

	private WifiP2pManager mManager;
	private Channel mChannel;
	private MainActivity mActivity;
	
	private ArrayList<WifiP2pDevice> peers;

	// PeerListListener updates peer list in MainActivity on event
    private PeerListListener peerListListener = new PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

        	peers = mActivity.getArrayList();
            peers.clear();
            peers.addAll(peerList.getDeviceList());
            mActivity.getListAdapter().notifyDataSetChanged();
            
        }
    };

	public WifiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
			MainActivity activity) {
		this.mManager = manager;
		this.mChannel = channel;
		this.mActivity = activity;
	}
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		String action = intent.getAction();

		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			// Check to see if Wi-Fi is enabled and notify appropriate activity
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				mActivity.setIsWifiP2pEnabled(true);
				mActivity.discoverPeers();
				if (mManager != null)
					mManager.requestPeers(mChannel, peerListListener);
			} else {
				mActivity.setIsWifiP2pEnabled(false);
			}
		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			// Call WifiP2pManager.requestPeers() to get a list of current peers
			if (mManager != null) {
	            mManager.requestPeers(mChannel, peerListListener);
	        }
		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
			// Respond to new connection or disconnections
			if (mManager != null) {
				// Get network info
				NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
				
				if (networkInfo.isConnected()) {
					mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
						
						@Override
						public void onConnectionInfoAvailable(WifiP2pInfo info) {
							// Get group owner IP address
							String ownerAddr = info.groupOwnerAddress.getHostAddress();
							
							mManager.requestPeers(mChannel, peerListListener);
							
							// Create server and client threads in MainActivity
							if (info.groupFormed && info.isGroupOwner) {
								Toast.makeText(context, "You are on Red Team", Toast.LENGTH_SHORT).show();
								mActivity.setIsGroupOwner(true);
								mActivity.createServerThread();
							} else if (info.groupFormed) {
								Toast.makeText(context, "You are on Blue Team", Toast.LENGTH_SHORT).show();
								mActivity.setIsGroupOwner(false);
								// Peer is group owner
								MainActivity.mPeerAddress = ownerAddr;
								mActivity.createServerThread();
								mActivity.createClientThread("\"I challenge you!!\"");
							} else {
								Toast.makeText(context, "Group not formed.", Toast.LENGTH_SHORT).show();
							}
						}
					});
				} else {
				}
			}
		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
			// Respond to this device's Wi-Fi state changing
			
		}
	}
}
