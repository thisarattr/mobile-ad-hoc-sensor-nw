/**
 * 
 */
package com.ucsc.mcs.constants;

import android.R.integer;

/**
 * @author thisara
 *
 */
public class CommonConstants {
	
	public static final String ROW_DELEMETER = "\n";
	public static final String DATA_DELEMETER = ",";
	public static final double RADIAN = 0.017453293;
	public static final long R = 6371; //radius of the earth in KM

	
	public static final int HOME_REQ_ID = 1;
	public static final int ADD_JOB_REQ_ID = 2;
	public static final int VIEW_DATA_REQ_ID = 3;
	public static final int SIGNUP_REQ_ID = 4;
	public static final int LOGIN_REQ_ID = 5;
	public static final int VIEW_JOB_REQ_ID = 6;
	
	public static String USERNAME="username";
	public static String PASSWORD="password";
	
	
	public static final String VIEWJOB_ID = "id";
	public static final String VIEWJOB_SENSORNAME = "sensor_name";
	public static final String VIEWJOB_STARTTIME = "start_time";
	public static final String VIEWJOB_EXPIRETIME = "expire_time";
	public static final String VIEWJOB_FREQ = "frequency";
	public static final String VIEWJOB_TIMEPERIOD = "time_period";
	public static final String VIEWJOB_LAT = "latitude";
	public static final String VIEWJOB_LONG = "longitude";
	public static final String VIEWJOB_LOCRANGE = "loc_range";
	public static final String VIEWJOB_NODES = "nodes";
	public static final String VIEWJOB_DESC = "description";
	public static final String VIEWJOB_DATATIME = "datetime";
	public static final String VIEWJOB_STATUS = "status";
	
	
	public static final String VIEWDATA_ID = "id";
	public static final String VIEWDATA_TIMESTAMP = "datetime";
	public static final String VIEWDATA_LAT = "latitude";
	public static final String VIEWDATA_LONG = "longitude";
	public static final String VIEWDATA_READING = "reading";
	public static final String VIEWDATA_USER = "username";
	
	public static final int JOB_STATUS_RUNNING = 1;
	public static final int JOB_STATUS_COMPLETED = 2;
	public static final int JOB_STATUS_EXPIRED = 3;
}
