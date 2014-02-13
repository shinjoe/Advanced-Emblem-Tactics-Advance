package com.cs117.aeta;

import java.util.ArrayList;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
	
	private Button mDiscoverButton;
	private Button mPlayButton;
	private ListView mPeerList;
	private View mGameView;
	
	private WifiP2pManager mManager;
	private Channel mChannel;
	private WifiDirectBroadcastReceiver mReceiver;
	private IntentFilter mIntentFilter;
	private boolean mWifiP2pEnabled;
	
	private ArrayList<WifiP2pDevice> mPeerArrayList;
	private ArrayAdapter<WifiP2pDevice> mPeerAdapter;
	
	private ActionResolverAndroid mResolver;
	
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
        
        mPeerList = (ListView) findViewById(R.id.peer_list);
        mPeerArrayList = new ArrayList<WifiP2pDevice>();
        mPeerAdapter = new ArrayAdapter<WifiP2pDevice>(getApplicationContext(), android.R.layout.simple_list_item_1, mPeerArrayList);
        mPeerList.setAdapter(mPeerAdapter); 
        mPeerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int pos,
					long id) {
				WifiP2pDevice dev = (WifiP2pDevice) mPeerList.getItemAtPosition(pos);
				Toast.makeText(getApplicationContext(), dev.toString(), Toast.LENGTH_SHORT).show();
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