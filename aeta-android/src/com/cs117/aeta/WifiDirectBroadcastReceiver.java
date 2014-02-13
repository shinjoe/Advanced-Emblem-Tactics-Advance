package com.cs117.aeta;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
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

    private PeerListListener peerListListener = new PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

        	peers = mActivity.getArrayList();
            peers.clear();
            peers.addAll(peerList.getDeviceList());

            // If an AdapterView is backed by this data, notify it
            // of the change.  For instance, if you have a ListView of available
            // peers, trigger an update.
            mActivity.getListAdapter().notifyDataSetChanged();
            if (peers.size() == 0) {
            	Toast.makeText(mActivity.getApplicationContext(), "No friends. :(", Toast.LENGTH_LONG).show();
            }
            else {
            	Toast.makeText(mActivity.getApplicationContext(), "Yay! :)", Toast.LENGTH_LONG).show();
            }
        }
    };

	public WifiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
			MainActivity activity) {
		this.mManager = manager;
		this.mChannel = channel;
		this.mActivity = activity;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			// Check to see if Wi-Fi is enabled and notify appropriate activity
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				Toast toast = Toast.makeText(context, "Wi-Fi Direct Enabled", Toast.LENGTH_LONG);
				toast.show();
				mActivity.setIsWifiP2pEnabled(true);
			} else {
				Toast toast = Toast.makeText(context, "Wi-Fi Direct Disabled", Toast.LENGTH_LONG);
				toast.show();
				mActivity.setIsWifiP2pEnabled(false);
			}
		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			// Call WifiP2pManager.requestPeers() to get a list of current peers
			if (mManager != null) {
	            mManager.requestPeers(mChannel, peerListListener);
	        }
		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
			// Respond to new connection or disconnections
		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
			// Respond to this device's Wi-Fi state changing
		}
	}
}
