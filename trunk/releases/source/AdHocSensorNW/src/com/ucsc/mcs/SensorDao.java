/**
 * 
 */
package com.ucsc.mcs;

import java.sql.SQLException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author thisara
 * 
 */
public class SensorDao {

	static final String TAG = SensorDao.class.getSimpleName();

	private Context context;
	private SQLiteDatabase database;
	private SensorDBHelper dbHelper;

	// SensorDao constructor
	public SensorDao(Context context) {
		this.context = context;
		Log.d(TAG, "SensorDao Constructor: Initialized data");
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public SensorDao open() throws SQLException {
		dbHelper = new SensorDBHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * 
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * @param table
	 * @param values
	 */
	public void insertOrIgnore(String table, ContentValues values) {

		database.insertOrThrow(table, null, values);
		Log.d(TAG, "Data inserted into table " + table + " and values are " + values.toString());

	}

	/**
	 * @return
	 */
	public Cursor getAllData() {
		//Upload Data for this sequence job_id, imei, datetime, latitude, longitude, reading
		String[] selectColumns = new String[] { SensorDBHelper.DATA_JOB_ID, SensorDBHelper.DATA_IMEI, SensorDBHelper.DATA_DATETIME,
				SensorDBHelper.DATA_LATITUDE, SensorDBHelper.DATA_LONGITUDE, SensorDBHelper.DATA_READING };
		Cursor cursor = database.query(SensorDBHelper.DATA_TABLE, selectColumns, null, null, null, null, null);
		Log.d(TAG, "Data retrieved from table and cursor returned " + cursor.toString());
		return cursor;
	}
	
	/**
	 * @return
	 */
	public Cursor getLiveJobs() {

		String[] selectColumns = new String[] { SensorDBHelper.JOB_ID, SensorDBHelper.JOB_DATETIME, SensorDBHelper.JOB_START_TIME,
				SensorDBHelper.JOB_EXPIRE_TIME, SensorDBHelper.JOB_FREQUENCY, SensorDBHelper.JOB_LATITUDE, SensorDBHelper.JOB_LONGITUDE,
				SensorDBHelper.JOB_LOC_RANGE, SensorDBHelper.JOB_SENSOR_ID, SensorDBHelper.JOB_STATUS, SensorDBHelper.JOB_USER_ID, };
		Cursor cursor = database.query(SensorDBHelper.JOB_TABLE, selectColumns, SensorDBHelper.JOB_STATUS+" in (1,2)", null, null, null, null);

		Log.d(TAG, "Data retrieved from table and cursor returned " + cursor.toString());

		return cursor;
	}
	
	/**
	 * @param table
	 * @param whereClause
	 */
	public void deleteRecords(String table, String whereClause){
		database.delete(table, whereClause, null);
		Log.d(TAG, "Delete records from the table "+table+ " where "+whereClause);
	}

	/**
	 * @param table
	 * @param updateValues
	 * @param whereClause
	 */
	public void updateRecords(String table, ContentValues updateValues, String whereClause){
		database.update(table, updateValues, whereClause, null);
		Log.d(TAG, "Update records from the table "+table+" set values "+updateValues.toString()+" where "+whereClause);
	}
	
}
