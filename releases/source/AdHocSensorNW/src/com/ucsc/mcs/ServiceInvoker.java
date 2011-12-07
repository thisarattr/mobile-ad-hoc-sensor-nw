/**
 * 
 */
package com.ucsc.mcs;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.ucsc.mcs.constants.CommonConstants;
import com.ucsc.mcs.constants.WebServiceConstants;

/**
 * @author thisara
 * 
 */
public class ServiceInvoker {

	private static final String TAG = ServiceInvoker.class.getSimpleName();
	

	/**
	 * @param username
	 * @param password
	 * @param imei
	 * @return
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public static String login(final String username, final String password, final String imei) {
		
		String output=null;
		SoapObject request = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.REQUEST_TYPE_LOGIN);
		request.addProperty("username", username);
		request.addProperty("password", password);
		request.addProperty("imei", imei);
		
		System.setProperty("javax.net.ssl.trustStore", "sensor_nw.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "thilanka");
		System.setProperty("javax.net.ssl.keyStore", "sensor_nw.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "thilanka");
		
		String trustStore = System.getProperty("javax.net.ssl.trustStore");
		System.out.println("This is Trust::"+trustStore+"::");
		

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);

		HttpTransportSE ht = new HttpTransportSE(WebServiceConstants.URL);
		
		try {
			ht.call(WebServiceConstants.SOAP_ACTION_LOGIN, envelope);
			SoapPrimitive sp = (SoapPrimitive) envelope.bodyIn;
			output = sp.toString();
		} catch (IOException e) {
			Log.d(TAG, "Error occur when invoking Login service. Original Error:" + e.toString());
			output="Error occur when invoking Login service." + e.getMessage();
		} catch (XmlPullParserException e) {
			Log.d(TAG, "Error occur when invoking Login service. Original Error:" + e.toString());
			output="Server not responding. Please check network connection." + e.getMessage();
		}
		

		return output;
	}

	/**
	 * @param latitude
	 * @param longitude
	 * @param runningJobs
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public static Boolean getJobs(final float latitude, final float longitude, final String runningJobs, final SensorDao dao) {

		SoapObject request = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.REQUEST_TYPE_GET_JOBS);
		request.addProperty("currentLatitude", latitude);
		request.addProperty("currentLongitude", longitude);
		request.addProperty("runningJobs", runningJobs);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);

		// For float marshaling. This is needed if u r using types that are not
		// defined on PropertyInfo class.
		Marshal floatMarshal = new MarshalFloat();
		floatMarshal.register(envelope);

		HttpTransportSE ht = new HttpTransportSE(WebServiceConstants.URL);

		try {
			ht.call(WebServiceConstants.SOAP_ACTION_GET_JOBS, envelope);
		} catch (IOException e) {
			Log.d(TAG, "Error occur when invoking GetJobs service. Original Error:" + e.toString());
		} catch (XmlPullParserException e) {
			Log.d(TAG, "Error occur when invoking GetJobs service. Original Error:" + e.toString());
		}

		SoapPrimitive sp = (SoapPrimitive) envelope.bodyIn;
		String data = sp.toString(); 
		if (data.trim().length() > 0) {
			String[] jobData = data.split(CommonConstants.DATA_DELEMETER);
			// id, datetime, sensor_id, frequency, latitude, longitude, loc_range, user_id, start_time, expire_time
			if (jobData.length > 0) {
				ContentValues values = new ContentValues();
				values.put(SensorDBHelper.JOB_ID, jobData[0]);
				values.put(SensorDBHelper.JOB_DATETIME, jobData[1]);
				values.put(SensorDBHelper.JOB_SENSOR_ID, jobData[2]);
				values.put(SensorDBHelper.JOB_FREQUENCY, jobData[3]);
				values.put(SensorDBHelper.JOB_LATITUDE, jobData[4]);
				values.put(SensorDBHelper.JOB_LONGITUDE, jobData[5]);
				values.put(SensorDBHelper.JOB_LOC_RANGE, jobData[6]);
				values.put(SensorDBHelper.JOB_USER_ID, jobData[7]);
				values.put(SensorDBHelper.JOB_START_TIME, jobData[8]);
				values.put(SensorDBHelper.JOB_EXPIRE_TIME, jobData[9]);
				values.put(SensorDBHelper.JOB_STATUS, 1); // 1 means new job to be started.
				dao.insertOrIgnore(SensorDBHelper.JOB_TABLE, values);
			}
		}else{
			Log.i(TAG, "No jobs on the server to match the mobile criteria.");
		}

		return true;
	}
	
	/**
	 * @param data
	 * @param imei
	 * @return
	 */
	public static boolean uploadData(final String imei, final String username, final SensorDao dao) {

		//Data should be on this order. job_id, imei, datetime, latitude, longitude, reading
		StringBuffer data = new StringBuffer();
		Cursor dataCur = dao.getAllData();

		if (dataCur.moveToPosition(0)) {
			do {
				data.append(dataCur.getLong(dataCur.getColumnIndex(SensorDBHelper.DATA_JOB_ID)) + CommonConstants.DATA_DELEMETER);
				data.append(dataCur.getString(dataCur.getColumnIndex(SensorDBHelper.DATA_IMEI)) + CommonConstants.DATA_DELEMETER);
				data.append(dataCur.getLong(dataCur.getColumnIndex(SensorDBHelper.DATA_DATETIME)) + CommonConstants.DATA_DELEMETER);
				data.append(dataCur.getDouble(dataCur.getColumnIndex(SensorDBHelper.DATA_LATITUDE)) + CommonConstants.DATA_DELEMETER);
				data.append(dataCur.getDouble(dataCur.getColumnIndex(SensorDBHelper.DATA_LONGITUDE)) + CommonConstants.DATA_DELEMETER);
				data.append(dataCur.getDouble(dataCur.getColumnIndex(SensorDBHelper.DATA_READING)));
				if (!dataCur.isLast()) {
					data.append(CommonConstants.ROW_DELEMETER);
				}
			} while (dataCur.moveToNext());
		}
		dataCur.close();
		
		boolean isUploadSuccess = false;
		if (data.length() > 0) {
			SoapObject request = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.REQUEST_TYPE_UPLOAD_DATA);
			request.addProperty("imei", imei);
			request.addProperty("data", data.toString());
			request.addProperty("username", username);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = false;
			envelope.setOutputSoapObject(request);

			HttpTransportSE ht = new HttpTransportSE(WebServiceConstants.URL);

			try {
				ht.call(WebServiceConstants.SOAP_ACTION_UPLOAD_DATA, envelope);
			} catch (IOException e) {
				Log.d(TAG, "Error occur when invoking UploadData service. Original Error:" + e.toString());
			} catch (XmlPullParserException e) {
				Log.d(TAG, "Error occur when invoking UploadData service. Original Error:" + e.toString());
			}catch (Exception e) {
				Log.d(TAG, "Error occur when invoking UploadData service. Original Error:" + e.toString());
			}
			SoapPrimitive sp = (SoapPrimitive) envelope.bodyIn;
			isUploadSuccess = Boolean.parseBoolean(sp.toString());
		}else{
			isUploadSuccess = true;
			Log.d(TAG, "No record on data table to upload!!!");
		}
		
		if(isUploadSuccess && data.length()>0){
			dao.deleteRecords(SensorDBHelper.DATA_TABLE, null);
		}else if(!isUploadSuccess && data.length()>0){
			//retry
		}

		return isUploadSuccess;
	}
	
	/**
	 * @param latitude
	 * @param longitude
	 * @param imei
	 * @param dao
	 * @return
	 */
	public static boolean sync(final Double latitude, final Double longitude, final String imei, final String username, final SensorDao dao){
		
		//Upload local data to server.
		boolean uploadData = uploadData(imei, username, dao);
		//delete onHold and expired jobs from local sqlite databse.
		long now = System.currentTimeMillis();
		dao.deleteRecords(SensorDBHelper.JOB_TABLE, SensorDBHelper.JOB_STATUS+" in (3,4) OR "+SensorDBHelper.JOB_EXPIRE_TIME+"<"+now);
		
		//get live jobs from local sqlite database.
		Cursor jobCur = dao.getLiveJobs();
		boolean getJobs=true;
		if (!jobCur.moveToFirst()) {
			//sync jobs with local sqlite database.
			getJobs = getJobs(latitude.floatValue(), longitude.floatValue(), "0", dao);	
		}
		
		return (uploadData && getJobs);
	}
	
	/**
	 * @param sensorId
	 * @param latitude
	 * @param longitude
	 * @param locRange
	 * @param starttime
	 * @param endtime
	 * @param frequency
	 * @param timePeriod
	 * @param nodes
	 * @param imei
	 * @param description
	 * @return
	 */
	public boolean addJob(final int sensorId, final float latitude, final float longitude, final float locRange, final long starttime,
			final long endtime, final int frequency, final int timePeriod, final int nodes, final String imei, final String description, final String username) {
		boolean isSuccess = false;

		SoapObject request = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.REQUEST_TYPE_ADD_JOB);
		request.addProperty("sensorType", sensorId);
		request.addProperty("latitude", latitude);
		request.addProperty("longitude", longitude);
		request.addProperty("locRange", locRange);
		request.addProperty("starttime", starttime);
		request.addProperty("endtime", endtime);
		request.addProperty("frequency", frequency);
		request.addProperty("timePeriod", timePeriod);
		request.addProperty("Nodes", nodes);
		request.addProperty("username", username);
		request.addProperty("imei", imei);
		request.addProperty("description", description);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);

		// For float marshaling. This is needed if u r using types that are not
		// defined on PropertyInfo class.
		Marshal floatMarshal = new MarshalFloat();
		floatMarshal.register(envelope);

		HttpTransportSE ht = new HttpTransportSE(WebServiceConstants.URL);

		try {
			ht.call(WebServiceConstants.SOAP_ACTION_ADD_JOB, envelope);
		} catch (IOException e) {
			Log.d(TAG, "Error occur when invoking GetJobs service. Original Error:" + e.toString());
		} catch (XmlPullParserException e) {
			Log.d(TAG, "Error occur when invoking GetJobs service. Original Error:" + e.toString());
		}
		
		SoapPrimitive sp = (SoapPrimitive) envelope.bodyIn;
		isSuccess=Boolean.parseBoolean(sp.toString());
		return isSuccess;

	}
	
	
	/**
	 * @param username
	 * @param password
	 * @param fullname
	 * @param imei
	 * @param email
	 * @return
	 */
	public boolean signup(final String username, final String password, final String fullname, final String imei, final String email){
		boolean isSuccess=false;
		
		SoapObject request = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.REQUEST_TYPE_SUBSCRIBE);
		request.addProperty("username", username);
		request.addProperty("password", password);
		request.addProperty("imei", imei);
		request.addProperty("email", email);
		request.addProperty("fullname", fullname);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);

		// For float marshaling. This is needed if u r using types that are not
		// defined on PropertyInfo class.
		Marshal floatMarshal = new MarshalFloat();
		floatMarshal.register(envelope);

		HttpTransportSE ht = new HttpTransportSE(WebServiceConstants.URL);

		try {
			ht.call(WebServiceConstants.SOAP_ACTION_SUBSCRIBE, envelope);
		} catch (IOException e) {
			Log.d(TAG, "Error occur when invoking GetJobs service. Original Error:" + e.toString());
		} catch (XmlPullParserException e) {
			Log.d(TAG, "Error occur when invoking GetJobs service. Original Error:" + e.toString());
		}
		
		SoapPrimitive sp = (SoapPrimitive) envelope.bodyIn;
		isSuccess=Boolean.parseBoolean(sp.toString());
		
		return isSuccess;
	}
	
	/**
	 * @param imei
	 * @param username
	 * @return
	 */
	public List<Map<String, String>> viewJobs(final String imei, final String username){
		
		SoapObject request = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.REQUEST_TYPE_VIEW_JOB);
		request.addProperty("imei", imei);
		request.addProperty("username", username);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);

		// For float marshaling. This is needed if u r using types that are not
		// defined on PropertyInfo class.
		Marshal floatMarshal = new MarshalFloat();
		floatMarshal.register(envelope);

		HttpTransportSE ht = new HttpTransportSE(WebServiceConstants.URL);

		try {
			ht.call(WebServiceConstants.SOAP_ACTION_VIEW_JOB, envelope);
		} catch (IOException e) {
			Log.d(TAG, "Error occur when invoking GetJobs service. Original Error:" + e.toString());
		} catch (XmlPullParserException e) {
			Log.d(TAG, "Error occur when invoking GetJobs service. Original Error:" + e.toString());
		}
		
		SoapPrimitive sp = (SoapPrimitive) envelope.bodyIn;
		String jobs=sp.toString();
		
		List<Map<String, String>> jobList = null;
		
		if (jobs != null && jobs.length()>0 && !jobs.contains("Error")) {
			String[] rowArray = jobs.split(CommonConstants.ROW_DELEMETER);
			jobList = new ArrayList<Map<String,String>>(); 
			
			for (int i = 0; i < rowArray.length; i++) {
				Map<String, String> dataMap = new HashMap<String, String>();
				String[] dataArray = rowArray[i].split(CommonConstants.DATA_DELEMETER);
				dataMap.put(CommonConstants.VIEWJOB_ID, dataArray[0]);
				dataMap.put(CommonConstants.VIEWJOB_SENSORNAME, dataArray[1]);
				dataMap.put(CommonConstants.VIEWJOB_STARTTIME, dataArray[2]);
				dataMap.put(CommonConstants.VIEWJOB_EXPIRETIME, dataArray[3]);
				dataMap.put(CommonConstants.VIEWJOB_FREQ, dataArray[4]);
				dataMap.put(CommonConstants.VIEWJOB_TIMEPERIOD, dataArray[5]);
				dataMap.put(CommonConstants.VIEWJOB_LAT, dataArray[6]);
				dataMap.put(CommonConstants.VIEWJOB_LONG, dataArray[7]);
				dataMap.put(CommonConstants.VIEWJOB_LOCRANGE, dataArray[8]);
				dataMap.put(CommonConstants.VIEWJOB_NODES, dataArray[9]);
				dataMap.put(CommonConstants.VIEWJOB_DESC, dataArray[10]);
				dataMap.put(CommonConstants.VIEWJOB_DATATIME, new Timestamp(Long.parseLong(dataArray[11])).toString());
				dataMap.put(CommonConstants.VIEWJOB_STATUS, dataArray[12]);
				jobList.add(dataMap);
			}
		}else{
			throw new RuntimeException(jobs);
		}
		
		return jobList;
	}
	
	/**
	 * @param sensorId
	 * @param latitude
	 * @param longitude
	 * @param locRange
	 * @param starttime
	 * @param endtime
	 * @param frequency
	 * @param timePeriod
	 * @param nodes
	 * @param imei
	 * @param description
	 * @param jobId
	 * @param username
	 * @return
	 */
	public boolean editJob(final int sensorId, final float latitude, final float longitude, final float locRange, final long starttime,
			final long endtime, final int frequency, final int timePeriod, final int nodes, final String imei, final String description, final long jobId, final String username){
		
		boolean isSuccess = false;

		SoapObject request = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.REQUEST_TYPE_EDIT_JOB);
		request.addProperty("sensorType", sensorId);
		request.addProperty("latitude", latitude);
		request.addProperty("longitude", longitude);
		request.addProperty("locRange", locRange);
		request.addProperty("starttime", starttime);
		request.addProperty("endtime", endtime);
		request.addProperty("frequency", frequency);
		request.addProperty("timePeriod", timePeriod);
		request.addProperty("Nodes", nodes);
		request.addProperty("username", username);
		request.addProperty("imei", imei);
		request.addProperty("description", description);
		request.addProperty("jobId", jobId);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);

		// For float marshaling. This is needed if u r using types that are not
		// defined on PropertyInfo class.
		Marshal floatMarshal = new MarshalFloat();
		floatMarshal.register(envelope);

		HttpTransportSE ht = new HttpTransportSE(WebServiceConstants.URL);

		try {
			ht.call(WebServiceConstants.SOAP_ACTION_EDIT_JOB, envelope);
		} catch (IOException e) {
			Log.d(TAG, "Error occur when invoking GetJobs service. Original Error:" + e.toString());
		} catch (XmlPullParserException e) {
			Log.d(TAG, "Error occur when invoking GetJobs service. Original Error:" + e.toString());
		}
		
		SoapPrimitive sp = (SoapPrimitive) envelope.bodyIn;
		isSuccess=Boolean.parseBoolean(sp.toString());
		return isSuccess;
	}
	

	/**
	 * @param jobId
	 * @param username
	 * @return
	 */
	public List<Map<String, String>> viewData(final Long jobId, final String username){
		
		SoapObject request = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.REQUEST_TYPE_VIEW_DATA);
		request.addProperty("jobId", jobId);
		request.addProperty("username", username);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);

		// For float marshaling. This is needed if u r using types that are not
		// defined on PropertyInfo class.
		Marshal floatMarshal = new MarshalFloat();
		floatMarshal.register(envelope);

		HttpTransportSE ht = new HttpTransportSE(WebServiceConstants.URL);

		try {
			ht.call(WebServiceConstants.SOAP_ACTION_VIEW_DATA, envelope);
		} catch (IOException e) {
			Log.d(TAG, "Error occur when invoking Get Data for Job ID: "+ jobId+". Original Error:" + e.toString());
		} catch (XmlPullParserException e) {
			Log.d(TAG, "Error occur when invoking Get Data for Job ID: "+ jobId+". Original Error:" + e.toString());
		}
		
		SoapPrimitive sp = (SoapPrimitive) envelope.bodyIn;
		String jobs=sp.toString();
		
		List<Map<String, String>> dataList = null;
		
		if (jobs != null && jobs.length()>0 && !jobs.contains("Error")) {
			String[] rowArray = jobs.split(CommonConstants.ROW_DELEMETER);
			dataList = new ArrayList<Map<String,String>>(); 
			
			for (int i = 0; i < rowArray.length; i++) {
				Map<String, String> dataMap = new HashMap<String, String>();
				String[] dataArray = rowArray[i].split(CommonConstants.DATA_DELEMETER);
				dataMap.put(CommonConstants.VIEWDATA_ID, dataArray[0]);
				dataMap.put(CommonConstants.VIEWDATA_TIMESTAMP, new Timestamp(Long.parseLong(dataArray[1])).toString());
				dataMap.put(CommonConstants.VIEWDATA_LAT, dataArray[2]);
				dataMap.put(CommonConstants.VIEWDATA_LONG, dataArray[3]);
				dataMap.put(CommonConstants.VIEWDATA_READING, dataArray[4]);
				dataMap.put(CommonConstants.VIEWDATA_USER, dataArray[5]);
				dataList.add(dataMap);
			}
		}else{
			throw new RuntimeException(jobs);
		}
		
		return dataList;
	}
	

	/**
	 * @param jobId
	 * @param username
	 * @return
	 */
	public Boolean emailData(final Long jobId, final String username) {

		SoapObject request = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.REQUEST_TYPE_EMAIL_DATA);
		request.addProperty("jobId", jobId);
		request.addProperty("imei", username);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);

		// For float marshaling. This is needed if u r using types that are not
		// defined on PropertyInfo class.
		Marshal floatMarshal = new MarshalFloat();
		floatMarshal.register(envelope);

		HttpTransportSE ht = new HttpTransportSE(WebServiceConstants.URL);

		try {
			ht.call(WebServiceConstants.SOAP_ACTION_EMAIL_DATA, envelope);
		} catch (IOException e) {
			Log.d(TAG, "Error occur when invoking Email Data for Job ID: " + jobId + ". Original Error:" + e.toString());
		} catch (XmlPullParserException e) {
			Log.d(TAG, "Error occur when invoking Email Data for Job ID: " + jobId + ". Original Error:" + e.toString());
		}

		SoapPrimitive sp = (SoapPrimitive) envelope.bodyIn;
		return Boolean.parseBoolean(sp.toString());
	}
	
	/**
	 * @param username
	 * @param email
	 * @param imei
	 * @return
	 */
	public Boolean passwordRecover(final String username, final String email, final String imei){
		
		SoapObject request = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.REQUEST_TYPE_PASSWORD_RECOVER);
		request.addProperty("username", username);
		request.addProperty("email", email);
		request.addProperty("imei", imei);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);

		// For float marshaling. This is needed if u r using types that are not
		// defined on PropertyInfo class.
		Marshal floatMarshal = new MarshalFloat();
		floatMarshal.register(envelope);

		HttpTransportSE ht = new HttpTransportSE(WebServiceConstants.URL);

		try {
			ht.call(WebServiceConstants.SOAP_ACTION_PASSWORD_RECOVER, envelope);
		} catch (IOException e) {
			Log.d(TAG, "Error occur when recover password for user: " + username + " : "+email+ ". Original Error:" + e.toString());
		} catch (XmlPullParserException e) {
			Log.d(TAG, "Error occur when recover password for user: " + username + " : "+email+ ". Original Error:" + e.toString());
		}

		SoapPrimitive sp = (SoapPrimitive) envelope.bodyIn;
		return Boolean.parseBoolean(sp.toString());

	}

}
