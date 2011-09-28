package com.ucsc.mcs;

import java.util.List;

import com.ucsc.mcs.constants.CommonConstants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends Activity implements OnSharedPreferenceChangeListener, OnClickListener {

	private static final String TAG = HomeActivity.class.getSimpleName();

	private TextView txtReadingValue, txtXVal, txtYVal, txtZVal, txtSensors, txtBtnClicked, txtGpsLbl, txtGpsVal, txtGpsDisable;
	private Button btnViewData, btnViewJob, btnAddJob, btnSync, btnGpsEnable;

	private SensorManager mSensorManager;
	private Sensor mMagnetometer;
	private SensorListener mSensorListener= new SensorListener();
	private List<Sensor> sensorList;
	private SharedPreferences prefs;

	private LocationManager mlocManager;
	private SensorLocationListener mlocListener;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Initialize shared preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		// Access saved preferences
		String username = prefs.getString("username", "");

		// Initialize UI components
		txtReadingValue = (TextView) findViewById(R.id.txtViewReadingVal);
		txtXVal = (TextView) findViewById(R.id.txtViewXVla);
		txtYVal = (TextView) findViewById(R.id.txtViewYVal);
		txtZVal = (TextView) findViewById(R.id.txtViewZVal);
		txtSensors = (TextView) findViewById(R.id.txtViewSensors);
		txtBtnClicked = (TextView) findViewById(R.id.txtViewBtnClicked);
		txtGpsLbl = (TextView) findViewById(R.id.txtViewGpsLocLbl);
		txtGpsVal = (TextView) findViewById(R.id.txtViewGpsVal);
		txtGpsDisable = (TextView) findViewById(R.id.txtViewGpsDisable);
		
		btnViewData = (Button) findViewById(R.id.btnViewData);
		btnSync =  (Button) findViewById(R.id.btnSync);
		btnAddJob =  (Button) findViewById(R.id.btnAddJob);
		btnViewJob =  (Button) findViewById(R.id.btnViewJob);
		btnGpsEnable =  (Button) findViewById(R.id.btnGpsEnable);
		btnViewData.setOnClickListener(this);
		btnSync.setOnClickListener(this);
		btnAddJob.setOnClickListener(this);
		btnViewJob.setOnClickListener(this);
		btnGpsEnable.setOnClickListener(this);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		//mSensorListener = new SensorListener();
		mSensorManager.registerListener(mSensorListener, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
		
		txtReadingValue.setText(String.valueOf(mSensorListener.getMean()));
		txtXVal.setText(String.valueOf(mSensorListener.getX()));
		txtYVal.setText(String.valueOf(mSensorListener.getY()));
		txtZVal.setText(String.valueOf(mSensorListener.getZ()));

		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new SensorLocationListener();
		Location loc = null;

		if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mlocListener);
			loc = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		} else if (mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, mlocListener);
			loc = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		} else {
			txtGpsDisable.setVisibility(1);
			btnGpsEnable.setVisibility(1);
		}
		
		if(loc!=null){
			txtGpsVal.setText(loc.getLatitude()+" : " + loc.getLongitude());
		}

		sensorList = mSensorManager.getSensorList(RESULT_OK);
		StringBuffer sensorNames = new StringBuffer();

		for (Sensor sensor : sensorList) {
			sensorNames.append(sensor.getName() + "  \n");
		}
		txtSensors.setText(sensorNames);

	}

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(mSensorListener, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(mSensorListener);
		// mlocManager.removeUpdates(mlocListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;

	}

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

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub

	}

	public void onClick(View v) {

		if (v.getId() == R.id.btnViewData) {
			Intent viewAllData = new Intent(v.getContext(), DisplayRecordActivity.class);
			startActivityForResult(viewAllData, CommonConstants.VIEW_DATA_REQ_ID);
			txtBtnClicked.setText("Button Clicked!!!");
			
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
			///ServiceInvoker.sy
		}

	}
}