package com.cs117.wifi;

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
import android.widget.Toast;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.cs117.aeta.Game;
import com.cs117.aeta.R;

public class MainActivity extends AndroidApplication {
	
	public static final int SERVER_PORT = 8800;
	public static final int CLIENT_PORT = 8900;
	public static final String TAG = "MainActivity";
	
	private Button mDiscoverButton;
	private Button mPlayButton;
	private Button mGroupButton;
	private Button mDisconnectButton;
	
	private ListView mPeerList;
	private View mGameView;
	
	private WifiP2pManager mManager;
	private Channel mChannel;
	private WifiDirectBroadcastReceiver mReceiver;
	private IntentFilter mIntentFilter;
	private WifiP2pDevice mPeer;
	
	private boolean mWifiP2pEnabled = false;
	
	private ArrayList<WifiP2pDevice> mPeerArrayList;
	private ArrayAdapter<WifiP2pDevice> mPeerAdapter;
	
	private ActionResolverAndroid mResolver;
	
	private boolean mIsGroupOwner = false;
	private boolean mInGame = false;

	static volatile String mPeerAddress = null;
	
	private Game mGame;
	
	private Thread mServerThread = null;
	private Thread mClientThread = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        // Create handler to call native Android APIs
        mResolver = new ActionResolverAndroid(this);
        
        // Create intent filter for broadcast receiver
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        
        // Create Wi-Fi P2P manager, channel, and broadcast receiver
        mManager = (WifiP2pManager) this.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, this.getMainLooper(), null);
        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);
        
        mDiscoverButton = (Button) findViewById(R.id.discover_button);
        mDiscoverButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				discoverPeers();
			}
		});
        
        mPlayButton = (Button) findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mWifiP2pEnabled)
				{
					mInGame = true;
					setContentView(mGameView);
				}
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
						//Toast.makeText(getApplicationContext(), "Forming posse...", Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onFailure(int reason) {
						Toast.makeText(getApplicationContext(), "No group for you! " + reason, Toast.LENGTH_SHORT).show();
						Log.d(TAG, "Create group failure. Reason: " + reason);
					}
				
				});
			}
		});
        
        mDisconnectButton = (Button) findViewById(R.id.disconnect_button);
        mDisconnectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				disconnect();
				discoverPeers();
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
				mPeer = (WifiP2pDevice) mPeerList.getItemAtPosition(pos);
				Toast.makeText(getApplicationContext(), mPeer.deviceName, Toast.LENGTH_SHORT).show();
				
				WifiP2pConfig config = new WifiP2pConfig();
				config.deviceAddress = mPeer.deviceAddress;
				config.wps.setup = WpsInfo.PBC;
				
				mManager.connect(mChannel, config, new ActionListener() {

					@Override
					public void onSuccess() {
						//Toast.makeText(getApplicationContext(), "Trying to connect...", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(int reason) {
						Toast.makeText(getApplicationContext(), "Herp a derp, connection failed. " + reason, Toast.LENGTH_SHORT).show();
						Log.d(TAG, "Connect failure. Reason: " + reason);
					}
					
				});		
			}

		});
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
        mGame = new Game(mResolver);
        mGameView = initializeForView(mGame, cfg);
        
        //disconnect();
        discoverPeers();
    }

    public Game getGame(){
    	return mGame;
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
	
	public void setPeerAddress(String peerAddress) {
		mPeerAddress = peerAddress;
	}
	
	public String getPeerAddress() {
		return mPeerAddress;
	}
	
	public void setIsGroupOwner(boolean isOwner) {
		mIsGroupOwner = isOwner;
	}
	
	public boolean isGroupOwner() {
		return mIsGroupOwner;
	}
	
	// Creates listening threads, always on
	public void createServerThread() {
		if (mIsGroupOwner)
			mServerThread = new ServerThread(this, SERVER_PORT);
		else
			mServerThread = new ServerThread(this, CLIENT_PORT);
		new Thread(mServerThread).start();
	}
	
	// Creates sending threads, return after sending
	public void createClientThread(String msg) {
		if (mIsGroupOwner)
			mClientThread = new ClientThread(this, msg, CLIENT_PORT);
		else
			mClientThread = new ClientThread(this, msg, SERVER_PORT);
		new Thread(mClientThread).start();
	}
	
	public void cleanUp() {
		mPeer = null;
		if (mPeerAdapter != null && mPeerArrayList != null) {
			mPeerArrayList.clear();
			mPeerAdapter.notifyDataSetChanged();
		}
		if (mServerThread != null) {
			mServerThread.interrupt();
			mServerThread = null;
		}
		if (mClientThread != null) {
			mClientThread.interrupt();
			mClientThread = null;
		}
		mPeerAddress = null;
		mIsGroupOwner = false;
		mInGame = false;
	}
	
    public boolean getInGame() {
    	return mInGame;
    }
	
	public void discoverPeers() {
		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
			
			@Override
			public void onSuccess() {
				//Toast.makeText(getApplicationContext(), "Looking for friends...", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(int reason) {
				Toast.makeText(getApplicationContext(), "Try again. " + reason, Toast.LENGTH_SHORT).show();
				Log.d(TAG, "Discover peer failure. Reason: " + reason);
			}
		});
	}
	
	public void disconnect() {
		mManager.removeGroup(mChannel, new ActionListener() {
			
			@Override
			public void onSuccess() {
				//Toast.makeText(getApplicationContext(), "Disconnecting...", Toast.LENGTH_SHORT).show();
			}
				
			@Override
			public void onFailure(int reason) {
				Toast.makeText(getApplicationContext(), "Nope, you're staying here. " + reason, Toast.LENGTH_SHORT).show();
				Log.d(TAG, "Remove group failure. Reason: " + reason);
			}
				
		});
		mManager.cancelConnect(mChannel, new ActionListener() {

			@Override
			public void onSuccess() {
				//Toast.makeText(getApplicationContext(), "Cancelling connection...", Toast.LENGTH_SHORT).show();	
			}
				
			@Override
			public void onFailure(int reason) {
				Toast.makeText(getApplicationContext(), "Can't cancel connection... whoops", Toast.LENGTH_SHORT).show();
				Log.d(TAG, "Cancel connect failure. Reason: " + reason);
			}
				
		});
		
		cleanUp();
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
    
    @Override
    public void onStop() {
    	super.onStop();
    	disconnect();
    }
}
