/**
 * 
 */
package com.ucsc.mcs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author thisara
 * 
 */
public class SensorDBHelper extends SQLiteOpenHelper {

	static final String TAG = SensorDBHelper.class.getSimpleName();

	static final String DB_NAME = "sensor_nw";
	static final int DB_VERSION = 13;

	static final String JOB_TABLE = "job";
	static final String JOB_ID = "id";
	static final String JOB_DATETIME = "datetime";
	static final String JOB_START_TIME = "start_time";
	static final String JOB_EXPIRE_TIME = "expire_time";
	static final String JOB_SENSOR_ID = "sensor_id";
	static final String JOB_FREQUENCY = "frequency";
	static final String JOB_LATITUDE = "latitude";
	static final String JOB_LONGITUDE = "longitude";
	static final String JOB_LOC_RANGE = "loc_range";
	static final String JOB_USER_ID = "user_id";
	static final String JOB_STATUS = "status";
	static final int JOB_STATUS_NEW = 1;
	static final int JOB_STATUS_RUNNING = 2;
	static final int JOB_STATUS_HOLD = 3;
	static final int JOB_STATUS_DONE = 4;

	static final String USER_TABLE = "user";
	static final String USER_ID = "id";
	static final String USER_NAME = "username";
	static final String USER_PW = "password";
	static final String USER_FULL_NAME = "fullname";
	static final String USER_RANK = "rank";
	static final String USER_IMEI = "imei";
	static final String USER_DATETIME = "datetime";

	static final String DATA_TABLE = "data";
	static final String DATA_ID = "id";
	static final String DATA_JOB_ID = "job_id";
	static final String DATA_IMEI = "imei";
	static final String DATA_DATETIME = "datetime";
	static final String DATA_LONGITUDE = "longitude";
	static final String DATA_LATITUDE = "latitude";
	static final String DATA_READING = "reading";

	static final String SENSOR_TABLE = "sensor";
	static final String SENSOR_ID = "id";
	static final String SENSOR_NAME = "name";
	static final String SENSOR_DESCRIPTION = "description";
	static final String SENSOR_DATETIME = "datetime";

	// DbHelper Constructor
	public SensorDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		Log.d(TAG, "DbHelper: Constructor");
	}

	// Called only once, first time the DB is created
	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.d(TAG, "DbHelper: OnCreate()");

		String sensorTable = "create table " + SENSOR_TABLE + " (" + SENSOR_ID + " integer primary key," + SENSOR_NAME + " text not null,"
				+ SENSOR_DATETIME + " integer not null," + SENSOR_DESCRIPTION + " text);";

		String userTable = "create table " + USER_TABLE + " (" + USER_ID + " integer primary key," + USER_NAME + " text not null," + USER_PW
				+ " text not null," + USER_FULL_NAME + " text not null," + USER_IMEI + " text not null," + USER_DATETIME + " integer not null,"
				+ USER_RANK + " integer not null);";

		String jobTable = "create table " + JOB_TABLE + " (" + JOB_ID + " integer primary key, " + JOB_DATETIME + " integer not null, "
				+ JOB_SENSOR_ID + " integer not null, " + JOB_FREQUENCY + " integer not null, " + JOB_LATITUDE + " double not null, " + JOB_LONGITUDE
				+ " double not null, " + JOB_LOC_RANGE + " double not null, " + JOB_USER_ID + " integer not null, " + JOB_START_TIME
				+ " integer not null, " + JOB_EXPIRE_TIME + " integer not null, " + JOB_STATUS + " integer not null, foreign key (" + JOB_SENSOR_ID
				+ ") references " + SENSOR_TABLE + " (" + SENSOR_ID + ") );";

		String dataTable = "create table " + DATA_TABLE + " (" + DATA_ID + " integer primary key autoincrement, " + DATA_IMEI + " text, "
				+ DATA_DATETIME + " integer not null, " + DATA_LATITUDE + " double not null, " + DATA_LONGITUDE + " double not null, " + DATA_READING
				+ " double not null, " + DATA_JOB_ID + " integer not null, foreign key (" + DATA_JOB_ID + ") references " + JOB_TABLE + " (" + JOB_ID
				+ ") );";

		//db.beginTransaction();
		db.execSQL(sensorTable);
		db.execSQL(userTable);
		db.execSQL(jobTable);
		db.execSQL(dataTable);
		//db.endTransaction();

		Log.d(TAG, "onCreated sql: \n" + sensorTable + "\n" + userTable + "\n" + jobTable + "\n" + dataTable);
	}

	// Called whenever newVersion != oldVersion
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

		//database.beginTransaction();
		database.execSQL("DROP TABLE IF EXISTS " + SENSOR_TABLE);
		database.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
		database.execSQL("DROP TABLE IF EXISTS " + JOB_TABLE);
		database.execSQL("DROP TABLE IF EXISTS " + DATA_TABLE);
		//database.endTransaction();
		onCreate(database);
		Log.d(TAG, "Database udated from version " + oldVersion + " to " + newVersion);
	}

}
