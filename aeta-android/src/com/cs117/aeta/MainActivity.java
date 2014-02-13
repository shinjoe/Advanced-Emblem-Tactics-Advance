package com.cs117.aeta;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
	
	public static int SERVER_PORT = 8888;
	public static int CLIENT_PORT = 8889;
	
	private Button mDiscoverButton;
	private Button mPlayButton;
	private Button mGroupButton;
	private Button mDisconnectButton;
	private Button mTestButton;
	
	private ListView mPeerList;
	private View mGameView;
	
	private WifiP2pManager mManager;
	private Channel mChannel;
	private WifiDirectBroadcastReceiver mReceiver;
	private IntentFilter mIntentFilter;
	private WifiP2pDevice mPeer;
	
	private boolean mWifiP2pEnabled;
	
	private ArrayList<WifiP2pDevice> mPeerArrayList;
	private ArrayAdapter<WifiP2pDevice> mPeerAdapter;
	
	private ActionResolverAndroid mResolver;
	
	private String mServerAddress;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        mWifiP2pEnabled = false;
        
        // Create handler to call native Android APIs
        mResolver = new ActionResolverAndroid(this);
        
        mIntentFilter = new IntentFilter();
        
        // Indicates change in Wi-Fi P2P status.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
             
        // Indicates change in list of available peers.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
             
        // Indicates change in Wi-Fi P2P connectivity.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
             
        // Indicates change in device's details.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        
        // Create Wi-Fi P2P manager, channel, and broadcast receiver
        mManager = (WifiP2pManager) this.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, this.getMainLooper(), null);
        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);
        
        mDiscoverButton = (Button) findViewById(R.id.discover_button);
        mDiscoverButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
					
					@Override
					public void onSuccess() {
						Toast.makeText(getApplicationContext(), "It worked!", Toast.LENGTH_SHORT).show();
						mPeerArrayList.clear();
						mPeerAdapter.notifyDataSetChanged();
					}
					
					@Override
					public void onFailure(int reason) {
						Toast.makeText(getApplicationContext(), "Try again.", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
        
        mPlayButton = (Button) findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mWifiP2pEnabled)
					setContentView(mGameView);
				else
					Toast.makeText(getApplicationContext(), "Nope. Turn on Wifi Direct first. ^_^", Toast.LENGTH_SHORT).show();
			}
		});
        
        mGroupButton = (Button) findViewById(R.id.group_button);
        mGroupButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mManager.createGroup(mChannel, new ActionListener() {

					@Override
					public void onSuccess() {
						Toast.makeText(getApplicationContext(), "Trying to make a group here.", Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onFailure(int reason) {
						Toast.makeText(getApplicationContext(), "No group for you!", Toast.LENGTH_SHORT).show();
					}
				
				});
			}
		});
        
        mDisconnectButton = (Button) findViewById(R.id.disconnect_button);
        mDisconnectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mManager.removeGroup(mChannel, new ActionListener() {

					@Override
					public void onSuccess() {
						Toast.makeText(getApplicationContext(), "Disconnected.", Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onFailure(int reason) {
						Toast.makeText(getApplicationContext(), "Nope, you're staying here.", Toast.LENGTH_SHORT).show();
					}
					
				});
				mPeer = null;
				mPeerArrayList.clear();
				mPeerAdapter.notifyDataSetChanged();
			}
		});
        
        mTestButton = (Button) findViewById(R.id.test_button);
        /*mTestButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setContentView(R.layout.test);
				((TextView) findViewById(R.id.textView2)).setText(mPeer.deviceName);
			}
		});*/
        
        mPeerList = (ListView) findViewById(R.id.peer_list);
        mPeerArrayList = new ArrayList<WifiP2pDevice>();
        mPeerAdapter = new ArrayAdapter<WifiP2pDevice>(getApplicationContext(), android.R.layout.simple_list_item_1, mPeerArrayList);
        mPeerList.setAdapter(mPeerAdapter); 
        mPeerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int pos,
					long id) {
				mPeer = (WifiP2pDevice) mPeerList.getItemAtPosition(pos);
				Toast.makeText(getApplicationContext(), mPeer.deviceName, Toast.LENGTH_SHORT).show();
				
				WifiP2pConfig config = new WifiP2pConfig();
				config.deviceAddress = mPeer.deviceAddress;
				config.wps.setup = WpsInfo.PBC;
				
				mManager.connect(mChannel, config, new ActionListener() {

					@Override
					public void onSuccess() {
						// WifiDirectBroadcastReceiver will notify us
						Toast.makeText(getApplicationContext(), "Trying to connect...", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(int reason) {
						Toast.makeText(getApplicationContext(), "Herp a derp, connection failed.", Toast.LENGTH_SHORT).show();
					}
					
				});		
			}

		});
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
        mGameView = initializeForView(new Game(), cfg);
    }

	public ArrayAdapter<WifiP2pDevice> getListAdapter() {
		return mPeerAdapter;
	}

	public ArrayList<WifiP2pDevice> getArrayList() {
		return mPeerArrayList;
	}

	public void setIsWifiP2pEnabled(boolean enabled) {
		mWifiP2pEnabled = enabled;
	}
	
	public void setServerAddress(String serverAddress) {
		mServerAddress = serverAddress;
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
}