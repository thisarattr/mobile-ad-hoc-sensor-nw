/**
 * 
 */
package com.ucsc.mcs;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import com.ucsc.mcs.constants.WebServiceConstants;
import com.ucsc.mcs.dto.Dimensions;

/**
 * @author thisara
 * 
 */
public class ServiceCallTestActivity extends Activity {

	/** Called when the activity is first created. */

	TextView viewAllRecords;

	private static final String TAG = ServiceCallTestActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_test);

		// Initialize UI components
		viewAllRecords = (TextView) findViewById(R.id.txtViewAllRecords);

/*		SoapObject request = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.METHOD_NAME);
		request.addAttribute("input", "This is the INPUT TEXT!!!");
		//request.setProperty(1, "test");

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);

		HttpTransportSE ht = new HttpTransportSE(WebServiceConstants.URL);

		try {
			ht.call(WebServiceConstants.SOAP_ACTION, envelope);
			// tv.setText("http set");
			SoapPrimitive sp =  (SoapPrimitive)envelope.bodyIn;
			viewAllRecords.setText("Msg from service: " + sp.toString());
			System.out.println("response message:" + sp.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		//This dimentions is needed if you hv seperate dimentions tag on request obj, this case it does not hv such,
		Dimensions dimensions = new Dimensions();
		dimensions.setWidth(5.0f);
		dimensions.setHeight(10.0f);
		
		
		SoapObject request = new SoapObject(WebServiceConstants.NAMESPACE, WebServiceConstants.REQUEST_TYPE_AREA);
		//request.addProperty("dimensions",dimensions);
		request.addProperty("width",5.0f);
		request.addProperty("height",10.0f);


		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		
		//envelope.addMapping(WebServiceConstants.NAMESPACE, Dimensions.DIMS_CLASS.getSimpleName(), Dimensions.DIMS_CLASS);

		// For float marshaling. This is needed if u r using types that are not defined on PropertyInfo class.
		Marshal floatMarshal = new MarshalFloat();
		floatMarshal.register(envelope);

		HttpTransportSE ht = new HttpTransportSE(WebServiceConstants.URL);
		
		SensorDao dao =null;

		try {
			dao = new SensorDao(this);
			dao.open();
			
			ht.call(WebServiceConstants.SOAP_ACTION_AREA, envelope);
			// tv.setText("http set");
			SoapPrimitive sp =  (SoapPrimitive)envelope.bodyIn;
			viewAllRecords.setText("Msg from service: " + sp.toString());
			System.out.println("response message:" + sp.toString());
			
			Cursor cur = dao.getAllData();
			//dao.deleteAll(SensorDBHelper.DATA_TABLE);
			
			if (cur.moveToPosition(0)) {
				StringBuffer row = new StringBuffer();
				do {
					row.append("JobID" + cur.getString(cur.getColumnIndex(SensorDBHelper.DATA_JOB_ID))+" ");
					row.append("Latitude"+cur.getString(cur.getColumnIndex(SensorDBHelper.DATA_LATITUDE))+" ");
					row.append("Longitude"+cur.getString(cur.getColumnIndex(SensorDBHelper.DATA_LONGITUDE))+" ");
					row.append("Reading"+cur.getString(cur.getColumnIndex(SensorDBHelper.DATA_READING))+" ");
					row.append("Node"+cur.getString(cur.getColumnIndex(SensorDBHelper.DATA_IMEI))+" \n");
					//row.append(cur.getInt(cur.getColumnIndex("rank"))+"/n/n");
				} while (cur.moveToNext());
				viewAllRecords.setText(row.toString());
			}
			cur.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		//ServiceInvoker.getJobs(48.8583f, 2.2945f, "2,3", dao);
		viewAllRecords.setText("All Records from DB");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
