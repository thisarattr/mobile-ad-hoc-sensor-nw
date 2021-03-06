package com.ucsc.mcs;

import java.sql.SQLException;
import java.util.List;

import com.ucsc.mcs.constants.CommonConstants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnSharedPreferenceChangeListener, OnClickListener, SensorEventListener {

	private static final String TAG = HomeActivity.class.getSimpleName();

	private TextView txtReadingValue, txtXVal, txtYVal, txtZVal, txtSensors, txtHomeLongLbl, txtHomeLatLbl, txtHomeLongVal, txtHomeLatVal, txtGpsDisable;
	private Button btnProfile, btnViewJob, btnAddJob, btnSync, btnGpsEnable;

	private SensorManager sensorManager;
	private Sensor magnetometer;
	private List<Sensor> sensorList;
	private SharedPreferences prefs;
	
	private String username, imei;
	private Location loc;
	private SensorDao sensorDao;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Initialize shared preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		//PreferenceManager.setDefaultValues(this, CommonConstants.PREF_USER_DETAILS, MODE_PRIVATE, R.xml.prefs, true);
		
		// Access saved preferences
		String uname = prefs.getString("username", "");
		//Access application session preferences
		SharedPreferences settings = getSharedPreferences(CommonConstants.PREF_USER_DETAILS, MODE_PRIVATE);
		username = settings.getString(CommonConstants.USERNAME, "");
		
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		
		sensorDao = new SensorDao(this);

		try {
			sensorDao.open();
		} catch (SQLException e) {
			e.printStackTrace();
			Log.d(TAG, e.toString());
		}

		// Initialize UI components
		txtReadingValue = (TextView) findViewById(R.id.txtViewReadingVal);
		txtXVal = (TextView) findViewById(R.id.txtViewXVla);
		txtYVal = (TextView) findViewById(R.id.txtViewYVal);
		txtZVal = (TextView) findViewById(R.id.txtViewZVal);
		txtSensors = (TextView) findViewById(R.id.txtViewSensors);
		txtHomeLatLbl = (TextView) findViewById(R.id.txtViewHomeLatLbl);
		txtHomeLongLbl = (TextView) findViewById(R.id.txtViewHomeLongLbl);
		txtHomeLatVal = (TextView) findViewById(R.id.txtViewHomeLatVal);
		txtHomeLongVal = (TextView) findViewById(R.id.txtViewHomeLongVal);
		txtGpsDisable = (TextView) findViewById(R.id.txtViewGpsDisable);
		
		btnProfile = (Button) findViewById(R.id.btnProfile);
		btnSync =  (Button) findViewById(R.id.btnSync);
		btnAddJob =  (Button) findViewById(R.id.btnAddJob);
		btnViewJob =  (Button) findViewById(R.id.btnViewJob);
		btnGpsEnable =  (Button) findViewById(R.id.btnGpsEnable);
		btnProfile.setOnClickListener(this);
		btnSync.setOnClickListener(this);
		btnAddJob.setOnClickListener(this);
		btnViewJob.setOnClickListener(this);
		btnGpsEnable.setOnClickListener(this);

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
		

		LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		SensorLocationListener locListener = new SensorLocationListener();
		

		if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locListener);
			loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		} else if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, locListener);
			loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		} else {
			txtGpsDisable.setVisibility(1);
			btnGpsEnable.setVisibility(1);
			btnSync.setEnabled(false);
		}
		
		if(loc!=null){
			txtHomeLongVal.setText(loc.getLongitude()+"");
			txtHomeLatVal.setText("   "+loc.getLatitude());
		}

		sensorList = sensorManager.getSensorList(RESULT_OK);
		StringBuffer sensorNames = new StringBuffer();

		for (Sensor sensor : sensorList) {
			sensorNames.append(sensor.getName() + "  \n");
		}
		txtSensors.setText(sensorNames);

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemMenuPerfs:
			startActivity(new Intent(this, PrefsActivity.class));
			break;
		case R.id.itemServiceStart:
			startService(new Intent(this, RecorderService.class));
			break;
		case R.id.itemServiceStop:
			stopService(new Intent(this, RecorderService.class));
			break;

		}
		return true;

	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
	 */
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		
		SharedPreferences settings = getSharedPreferences(CommonConstants.PREF_USER_DETAILS, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, sharedPreferences.getString(key, ""));
		editor.commit();
		
		Toast.makeText(HomeActivity.this, "Shared Preferences "+key+" changed!", Toast.LENGTH_LONG).show();

	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {

		if (v.getId() == R.id.btnProfile) {
			Intent updateProfile = new Intent(v.getContext(), UpdateProfileActivity.class);
			startActivityForResult(updateProfile, CommonConstants.UPDATE_PROFILE_REQ_ID);
			
		}else if(v.getId() == R.id.btnGpsEnable){
			Intent gspSettings = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(gspSettings, 0);
			
		}else if(v.getId() == R.id.btnAddJob){
			Intent addJob = new Intent(v.getContext(), AddJobActivity.class);
			startActivityForResult(addJob, CommonConstants.ADD_JOB_REQ_ID);
			
		}else if(v.getId() == R.id.btnViewJob){
			Intent viewJob = new Intent(v.getContext(), ViewJobsActivity.class);
			startActivityForResult(viewJob, CommonConstants.VIEW_JOB_REQ_ID);
			
		}else if(v.getId() == R.id.btnSync){
			//Intent pwReset = new Intent(v.getContext(), PasswordRecoverActivity.class);
			//startActivityForResult(pwReset, CommonConstants.PW_RESET_REQ_ID);
			
			ServiceInvoker invoker = new ServiceInvoker();
			invoker.sync(loc.getLatitude(), loc.getLongitude(), imei, username, sensorDao);
			
			Toast.makeText(HomeActivity.this, "Client successfully synchronized with the server.", Toast.LENGTH_LONG).show();
		}

	}

	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
	 */
	public void onAccuracyChanged(Sensor paramSensor, int paramInt) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	public void onSensorChanged(SensorEvent event) {
		
		txtReadingValue
				.setText(String.valueOf(Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2))));
		txtXVal.setText(String.valueOf(event.values[0]));
		txtYVal.setText(String.valueOf(event.values[1]));
		txtZVal.setText(String.valueOf(event.values[2]));
		
	}
}