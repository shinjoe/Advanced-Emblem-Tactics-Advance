package com.cs117.aeta;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MenuActivity extends Activity {
	
	private Button mDiscoverButton;
	private Button mPlayButton;
	private ListView mPeerList;
	
	private ArrayList peerArrayList;
	private ArrayAdapter peerAdapter;
	
	private final IntentFilter mIntentFilter = new IntentFilter();
	
	private WifiP2pManager mManager;
	private Channel mChannel;
	private BroadcastReceiver mReceiver;
	
	private boolean mWifiP2pEnabled = false;
	
	public ArrayAdapter getListAdapter(){
		return peerAdapter;
	}
	
	public ArrayList getArrayList()
	{
		return peerArrayList;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        // Indicates change in Wi-Fi P2P status.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        
        // Indicates change in list of available peers.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        
        // Indicates change in Wi-Fi P2P connectivity.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        
        // Indicates change in device's details.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        
        mDiscoverButton = (Button) findViewById(R.id.discover_button);
        mDiscoverButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast toast = Toast.makeText(getApplicationContext(), "Another test", Toast.LENGTH_LONG);
				toast.show();
				mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

			        @Override
			        public void onSuccess() {
			        	Toast toast = Toast.makeText(getApplicationContext(), "You actually have friends", Toast.LENGTH_LONG);
			        	toast.show();
			        }

			        @Override
			        public void onFailure(int reasonCode) {
			            Toast toast = Toast.makeText(getApplicationContext(), "No peers", Toast.LENGTH_LONG);
			            toast.show();
			        }
				});
			}
		});
        
        mPeerList = (ListView) findViewById(R.id.peer_list);
        peerArrayList = new ArrayList();
        peerAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, peerArrayList);
        mPeerList.setAdapter(peerAdapter);
        //derp.clear();
        
        mPlayButton = (Button) findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// start MainActivity (if connected to peer)
				if (mWifiP2pEnabled) {
					// TODO: send necessary information to activity
					Intent i = new Intent(MenuActivity.this, MainActivity.class);
					startActivity(i);
				} else {
					Toast toast = Toast.makeText(getApplicationContext(), "Nope. Turn on Wi-Fi Direct first.", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});
        
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	registerReceiver(mReceiver, mIntentFilter);
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	unregisterReceiver(mReceiver);
    }
    public void setIsWifiP2pEnabled(boolean enable) {
    	this.mWifiP2pEnabled = enable;
    }
}
