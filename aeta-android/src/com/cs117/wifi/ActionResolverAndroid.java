package com.cs117.wifi;

/***
 *
 * This class is adapted from the code snippets at: http://octagen.at/2013/01/native-android-ui-and-libgdx/
 * 
 * @author Octagen Lab
 * 
 */
import org.json.JSONException;
import org.json.JSONObject;

import com.cs117.connection.ActionResolver;


import android.os.Handler;
import android.widget.Toast;

public class ActionResolverAndroid implements ActionResolver {
	
	private Handler mUiThread;
	private MainActivity mActivity;
	
	public ActionResolverAndroid(MainActivity activity) {
		mUiThread = new Handler();
		this.mActivity = activity;
	}

	@Override
	public void showShortToast(final CharSequence toastMessage) {
		mUiThread.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mActivity.getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
			}
			
		});		
	}

	@Override
	public void showLongToast(final CharSequence toastMessage) {
		mUiThread.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mActivity.getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
			}
			
		});	
	}
	
	@Override
	public void sendCoordinates(int prevX, int prevY, int newX, int newY) {
		
		JSONObject coords = new JSONObject();
		try{
			coords.put("type", 0);
			coords.put("prevX", prevX);
			coords.put("prevY", prevY);
			coords.put("newX", newX);
			coords.put("newY", newY);
		}
		catch(JSONException e) {
			e.printStackTrace();
			System.err.println("send coord failure");
		}
		mActivity.createClientThread(coords.toString());
	}

	public void sendAtkRes(int atkingX, int atkingY, int atkedX, int atkedY, int newHP)
	{
		JSONObject atkRes = new JSONObject();
		try 
		{
			atkRes.put("type",  1);
			atkRes.put("atkedX", atkedX);
			atkRes.put("atkedY", atkedY);
			atkRes.put("newHP",  newHP);
			atkRes.put("atkingX", atkingX);
			atkRes.put("atkingY", atkingY);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			System.err.println("Send attack results failure");
		}
		
		mActivity.createClientThread(atkRes.toString());
	}
	
	public void sendEndTurn(int nextTurn) {
		JSONObject turn = new JSONObject();
		try {
			turn.put("type", 2);
			turn.put("turn", nextTurn);
		} catch (JSONException e) {
			e.printStackTrace();
			System.err.println("Send next turn failure");
		}
		
		mActivity.createClientThread(turn.toString());
	}
}
