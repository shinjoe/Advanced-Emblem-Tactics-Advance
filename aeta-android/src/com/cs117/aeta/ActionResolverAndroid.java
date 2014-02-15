package com.cs117.aeta;

/***
 *
 * This class is adapted from the code snippets at: http://octagen.at/2013/01/native-android-ui-and-libgdx/
 * 
 * @author Octagen Lab
 * 
 */
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
	public void sendCoordinates(CharSequence coordinates) {
		mActivity.createClientThread(coordinates.toString());
	}
}
