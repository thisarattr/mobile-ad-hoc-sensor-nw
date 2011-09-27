/**
 * 
 */
package com.ucsc.mcs;

import java.sql.SQLException;
import java.sql.Timestamp;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ucsc.mcs.constants.CommonConstants;

/**
 * @author thisara
 * 
 */
public class RecorderService extends Service {

	private static final String TAG = RecorderService.class.getSimpleName();

	static final long MIN_DELAY = 60000; // a minute
	static final long UPLOAD_TIMEOUT = 60000*60*2;
	static final long JOB_CHECK_TIMEOUT = 60000*15;
	private boolean runFlag = false;

	private Updater updater;
	private SensorApplication sensorApplication;
	private SensorDao sensorDao;

	// Sensor related
	private SensorManager mSensorManager;
	private Sensor mMagnetometer;
	private SensorListener mSensorListener;

	// Location related
	private LocationManager mlocManager;
	private SensorLocationListener mlocListener;
	
	//Phone imei
	private String imei = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(TAG, "onBind");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {

		super.onCreate();
		sensorApplication = (SensorApplication) getApplication();
		updater = new Updater();
		sensorDao = new SensorDao(this);

		try {
			sensorDao.open();
		} catch (SQLException e) {
			e.printStackTrace();
			Log.d(TAG, e.toString());
		}

		// Adding sensor manager or sense the magnetic field change.
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mSensorListener = new SensorListener();
		mSensorManager.registerListener(mSensorListener, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
		
		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new SensorLocationListener();
		
		if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
			Log.d(TAG, "GPS provider enabled and using.");
		} else if (mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
			Log.d(TAG, "GPS provider disabled and using network provider.");
		} else {
			//send dialog box saying gps disabled.
			Log.d(TAG, "GPS provider and network provider both disabled.");
		}
		
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		
		//Sync with the server database.
		//get the all unfinished and valid jobs from the database.
		//Upload Data for this sequence job_id, imei, datetime, latitude, longitude, reading
		ServiceInvoker.sync(mlocListener.getLatitude(), mlocListener.getLongitute(), imei, sensorDao);
		
		Log.d(TAG, "onCreated");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();

		mSensorManager.unregisterListener(mSensorListener);
		this.runFlag = false;
		this.updater.interrupt();
		this.updater = null;
		this.sensorApplication.setServiceRunning(false);
		// sensorDao.close();

		Log.d(TAG, "onDestroyed");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (!runFlag) {

			this.runFlag = true;
			this.updater.start();
			((SensorApplication) super.getApplication()).setServiceRunning(true);

			Log.d(TAG, "onStarted");
		}
		return START_STICKY;
	}

	/**
	 * This inner class defines the tasks of the thread which will handle the
	 * network related operations.
	 * 
	 * @author thisara
	 * 
	 */
	private class Updater extends Thread {

		public Updater() {
			super("RecordService-Updater");
		}

		@Override
		public void run() {

			RecorderService recService = RecorderService.this;

			while (recService.runFlag) {
				Log.d(TAG, "Updater running");

				Cursor cursor = sensorDao.getLiveJobs();
				if (cursor.moveToFirst()) {// job found and continue to record.
					long jobId = cursor.getLong(cursor.getColumnIndex(SensorDBHelper.JOB_ID));
					int jobSensorId = cursor.getInt(cursor.getColumnIndex(SensorDBHelper.JOB_SENSOR_ID));
					double jobLatitude = cursor.getDouble(cursor.getColumnIndex(SensorDBHelper.JOB_LATITUDE));
					double jobLongitude = cursor.getDouble(cursor.getColumnIndex(SensorDBHelper.JOB_LONGITUDE));
					double jobLocRange = cursor.getDouble(cursor.getColumnIndex(SensorDBHelper.JOB_LOC_RANGE));
					long jobStarttime = cursor.getLong(cursor.getColumnIndex(SensorDBHelper.JOB_START_TIME));
					long jobEndtime = cursor.getLong(cursor.getColumnIndex(SensorDBHelper.JOB_EXPIRE_TIME));
					long jobFrequency = cursor.getInt(cursor.getColumnIndex(SensorDBHelper.JOB_FREQUENCY));
					int jobStatus = cursor.getInt(cursor.getColumnIndex(SensorDBHelper.JOB_STATUS));
					long uploadConter = 0;

					while (jobStatus == SensorDBHelper.JOB_STATUS_NEW || jobStatus == SensorDBHelper.JOB_STATUS_RUNNING) {

						// increment the upload counter by to identify the upload time.
						uploadConter = +1;
						// Upload data
						if (UPLOAD_TIMEOUT <= uploadConter * jobFrequency * MIN_DELAY) {
							boolean isUploadSuccess = ServiceInvoker.uploadData(imei, sensorDao);
							if (isUploadSuccess) {
								uploadConter = 0;
							}
						}

						long timeNow = System.currentTimeMillis();
						double latitude = mlocListener.getLatitude();
						double longitude = mlocListener.getLongitute();
						double reading = 0.0;
						if (jobSensorId == 1) {
							// this is megenetic field sensor
							reading = mSensorListener.getMean();
						}
						// dist = arccos(sin(lat1) · sin(lat2) + cos(lat1) · cos(lat2) · cos(lon1 - lon2)) · R (6371)
						double dist = Math.cos(Math.sin(jobLatitude * CommonConstants.RADIAN) * Math.sin(latitude * CommonConstants.RADIAN)
								+ Math.cos(jobLatitude * CommonConstants.RADIAN) * Math.cos(latitude * CommonConstants.RADIAN)
								* Math.cos(jobLongitude * CommonConstants.RADIAN - longitude * CommonConstants.RADIAN))
								* CommonConstants.R;

						if (dist <= jobLocRange && jobEndtime >= timeNow && jobStarttime <= timeNow) {

							if (jobStatus == SensorDBHelper.JOB_STATUS_NEW) {
								// mark the job as running
								jobStatus = SensorDBHelper.JOB_STATUS_RUNNING;
								updateJobStatus(jobStatus, jobId);
							}
							try {

								ContentValues values = new ContentValues();
								values.clear();
								values.put(SensorDBHelper.DATA_DATETIME, timeNow);
								values.put(SensorDBHelper.DATA_JOB_ID, jobId);
								values.put(SensorDBHelper.DATA_LATITUDE, latitude);
								values.put(SensorDBHelper.DATA_LONGITUDE, longitude);
								values.put(SensorDBHelper.DATA_IMEI, imei);
								values.put(SensorDBHelper.DATA_READING, reading);

								sensorDao.insertOrIgnore(SensorDBHelper.DATA_TABLE, values);
								Thread.sleep(MIN_DELAY * jobFrequency);

							} catch (InterruptedException e) {
								Log.e(TAG, "Error occured while updating Data table!!! Original stacktrace: " + e.toString());
								recService.runFlag = false;
							}
						} else {
							if (jobEndtime < timeNow || dist > jobLocRange) {
								//Job done OR job hold, out of range.
								if (jobEndtime < timeNow) {
									jobStatus = SensorDBHelper.JOB_STATUS_DONE; // job done
								} else if (dist > jobLocRange) {
									jobStatus = SensorDBHelper.JOB_STATUS_HOLD; // job hold, out of range
								}
								updateJobStatus(jobStatus, jobId);
								ServiceInvoker.sync(latitude, longitude, imei, sensorDao);
								Cursor jobCur = sensorDao.getLiveJobs();
								if (jobCur.moveToFirst()) {
									// job found and continue to record.
									jobId = jobCur.getLong(jobCur.getColumnIndex(SensorDBHelper.JOB_ID));
									jobSensorId = jobCur.getInt(jobCur.getColumnIndex(SensorDBHelper.JOB_SENSOR_ID));
									jobLatitude = jobCur.getDouble(jobCur.getColumnIndex(SensorDBHelper.JOB_LATITUDE));
									jobLongitude = jobCur.getDouble(jobCur.getColumnIndex(SensorDBHelper.JOB_LONGITUDE));
									jobLocRange = jobCur.getDouble(jobCur.getColumnIndex(SensorDBHelper.JOB_LOC_RANGE));
									jobStarttime = jobCur.getLong(jobCur.getColumnIndex(SensorDBHelper.JOB_START_TIME));
									jobEndtime = jobCur.getLong(jobCur.getColumnIndex(SensorDBHelper.JOB_EXPIRE_TIME));
									jobFrequency = jobCur.getInt(jobCur.getColumnIndex(SensorDBHelper.JOB_FREQUENCY));
									jobStatus = jobCur.getInt(jobCur.getColumnIndex(SensorDBHelper.JOB_STATUS));
									uploadConter = 0;
								}

							} else if (jobStarttime > timeNow) {
								// job yet to start. sleep thread until job starts.
								try {
									Log.i(TAG, "Job found but it is due to start on "+new Timestamp(jobStarttime)+". Therefore thread will go to sleep!!!");
									Thread.sleep(jobStarttime - timeNow);
								} catch (InterruptedException e) {
									Log.e(TAG, "Error occured while make thread sleep until job start!!! Original stacktrace: " + e.toString());
								}
								jobStatus = SensorDBHelper.JOB_STATUS_RUNNING;
								updateJobStatus(jobStatus, jobId);
							}
						}// end of if else
					}// end of job while
				}else{// no jobs find on the system
					try {
						Log.i(TAG, "There are no jobs found and thread will go to sleep for "+JOB_CHECK_TIMEOUT+"ms!!!");
						Thread.sleep(JOB_CHECK_TIMEOUT);
						ServiceInvoker.sync(mlocListener.getLatitude(), mlocListener.getLongitute(), imei, sensorDao);
					} catch (InterruptedException e) {
						Log.e(TAG, "Error occured while make thread sleep cos no jobs found!!! Original stacktrace: " + e.toString());
					}
				}
			}// end of run flag while
		}// end of run()
		
		
		
		/**
		 * @param status
		 * @param jobId
		 */
		private void updateJobStatus(final int status, final long jobId){
			ContentValues updateVals = new ContentValues();
			updateVals.put(SensorDBHelper.JOB_STATUS, status);
			sensorDao.updateRecords(SensorDBHelper.JOB_TABLE, updateVals, "id="+jobId);
		}

	}

}