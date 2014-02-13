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

import com.badlogic.gdx.backends.android.AndroidApplication;

public class ActionResolverAndroid implements ActionResolver {
	
	private Handler mUiThread;
	private AndroidApplication mAppContext;
	
	public ActionResolverAndroid(AndroidApplication appContext) {
		mUiThread = new Handler();
		this.mAppContext = appContext;
	}

	@Override
	public void showShortToast(final CharSequence toastMessage) {
		mUiThread.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mAppContext, toastMessage, Toast.LENGTH_SHORT).show();
			}
			
		});		
	}

	@Override
	public void showLongToast(final CharSequence toastMessage) {
		mUiThread.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mAppContext, toastMessage, Toast.LENGTH_LONG).show();
			}
			
		});	
	}
}
