/**
 * 
 */
package com.ucsc.mcs;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

/**
 * @author thisara
 *
 */
public class PrefsActivity extends PreferenceActivity {
	
	private static final String TAG = "PrefsActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Preference activity created.");
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}

}
