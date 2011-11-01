/**
 * 
 */
package com.ucsc.mcs;

import java.util.Calendar;

import com.ucsc.mcs.constants.CommonConstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * @author thisara
 * 
 */
public class AddJobActivity extends Activity implements OnClickListener, OnCheckedChangeListener {

	static final int ID_START_DATEPICKER = 0;
	static final int ID_START_TIMEPICKER = 1;
	static final int ID_END_DATEPICKER = 3;
	static final int ID_END_TIMEPICKER = 4;
	
	private static final String TAG = AddJobActivity.class.getSimpleName();

	private Spinner spinSensorType;
	private EditText editTxtLatitude, editTxtLongitude, editTxtLocRange, editTxtFreq, editTxtTimePeriod, editTxtNodes, editTxtDesc;
	private Button btnStartDate, btnStartTime, btnEndDate, btnEndTime, btnJobDiscard, btnJobSave, btnJobReset;
	private CheckBox chkBxStarttime, chkBxEndtime;
	private int startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute;
	private String imei;
	private ServiceInvoker serviceInvoker;
	private String username;
	
	// Location related
	private LocationManager mlocManager;
	private SensorLocationListener mlocListener;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addjob);
		
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		
		serviceInvoker = new ServiceInvoker();

		spinSensorType = (Spinner) findViewById(R.id.spinSensorType);
		editTxtLatitude = (EditText) findViewById(R.id.editTxtLat);
		editTxtLongitude = (EditText) findViewById(R.id.editTxtLon);
		editTxtLocRange = (EditText) findViewById(R.id.editTxtLocRange);
		editTxtFreq = (EditText) findViewById(R.id.editTxtFreqJob);
		editTxtTimePeriod = (EditText) findViewById(R.id.editTxtTimePeriodJob);
		editTxtNodes = (EditText) findViewById(R.id.editTxtNodes);
		editTxtDesc = (EditText) findViewById(R.id.editTxtDesc);
		btnStartDate = (Button) findViewById(R.id.btnJobStartDate);
		btnStartTime = (Button) findViewById(R.id.btnJobStartTime);
		btnEndDate = (Button) findViewById(R.id.btnJobEndDate);
		btnEndTime = (Button) findViewById(R.id.btnJobEndTime);
		btnJobDiscard=(Button) findViewById(R.id.btnJobDiscard);
		btnJobReset=(Button) findViewById(R.id.btnJobReset);
		btnJobSave=(Button) findViewById(R.id.btnJobSave);
		btnStartDate.setOnClickListener(this);
		btnStartTime.setOnClickListener(this);
		btnEndDate.setOnClickListener(this);
		btnEndTime.setOnClickListener(this);
		btnJobDiscard.setOnClickListener(this);
		btnJobReset.setOnClickListener(this);
		btnJobSave.setOnClickListener(this);
		chkBxStarttime = (CheckBox) findViewById(R.id.chkBxStarttimeAdd);
		chkBxEndtime = (CheckBox) findViewById(R.id.chkBxEndtimeAdd);
		chkBxStarttime.setOnCheckedChangeListener(this);
		chkBxEndtime.setOnCheckedChangeListener(this);

		String[] sensorItems = new String[] { "Magnetic", "Temperature", "Motion" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sensorItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spinSensorType.setAdapter(adapter);

		final Calendar now = Calendar.getInstance();
		startYear = endYear = now.get(Calendar.YEAR);
		startMonth = endMonth = now.get(Calendar.MONTH);// month is 0 based.
		startDay = endDay = now.get(Calendar.DAY_OF_MONTH);
		startHour = endHour = now.get(Calendar.HOUR_OF_DAY);
		startMinute = endMinute = now.get(Calendar.MINUTE);

		String date = startDay + "/" + (startMonth+1) + "/" + startYear;
		String time = startHour + ":" + startMinute;
		btnStartDate.setText(date);
		btnStartTime.setText(time);
		btnEndDate.setText(date);
		btnEndTime.setText(time);
		
		SharedPreferences settings = getSharedPreferences("UserDetails", MODE_PRIVATE);
		username = settings.getString(CommonConstants.USERNAME, "");
		
		// Location service initialization
		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new SensorLocationListener();
		Location loc = null;
		
		if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
			loc = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			Log.d(TAG, "GPS provider enabled and using.");
		} else if (mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
			loc = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			Log.d(TAG, "GPS provider disabled and using network provider.");
		} else {
			//send dialog box saying gps disabled.
			Toast.makeText(AddJobActivity.this, "Location Services are disables. Enable it to continue.", Toast.LENGTH_LONG).show();
			Log.d(TAG, "GPS provider and network provider both disabled.");
			Intent gspSettings = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(gspSettings, 0);
		}
		
		if (loc != null) {
			mlocListener.setLatitude(loc.getLatitude());
			mlocListener.setLongitute(loc.getLongitude());
		}
		
		editTxtLatitude.setText(mlocListener.getLatitude().toString());
		editTxtLongitude.setText(mlocListener.getLongitute().toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(final View v) {

		if (v.getId() == R.id.btnJobStartDate) {
			showDialog(ID_START_DATEPICKER);

		} else if (v.getId() == R.id.btnJobStartTime) {
			showDialog(ID_START_TIMEPICKER);

		} else if (v.getId() == R.id.btnJobEndDate) {
			showDialog(ID_END_DATEPICKER);

		} else if (v.getId() == R.id.btnJobEndTime) {
			showDialog(ID_END_TIMEPICKER);

		} else if (v.getId() == R.id.btnJobDiscard) {

			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Discard, Are you sure?");
			alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent home = new Intent(v.getContext(), HomeActivity.class);
					startActivityForResult(home, CommonConstants.HOME_REQ_ID);
					return;
				}
				
			});
			alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			alertDialog.show();

		} else if (v.getId() == R.id.btnJobReset) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Reset fields, Are you sure?");
			alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					editTxtLatitude.setText("");
					editTxtLongitude.setText("");
					editTxtLocRange.setText("");
					editTxtNodes.setText("");
					editTxtFreq.setText("");
					editTxtTimePeriod.setText("");
					editTxtDesc.setText("");
					return;
				}
				
			});
			alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			alertDialog.show();

		} else if (v.getId() == R.id.btnJobSave) {
			Calendar startDatetime = null;
			Calendar endDatetime = null;
			boolean validStartEndTime = true;
			
			if (chkBxStarttime.isChecked()) {
				startDatetime = Calendar.getInstance();
				startDatetime.set(startYear, startMonth, startDay, startHour, startMinute);
			}
			if (chkBxEndtime.isChecked()) {
				endDatetime = Calendar.getInstance();
				endDatetime.set(endYear, endMonth, endDay, endHour, endMinute);
				validStartEndTime = Calendar.getInstance().before(endDatetime);
			}
			if (validStartEndTime && startDatetime != null && endDatetime != null) {
				validStartEndTime = startDatetime.before(endDatetime);
			}
			if (validStartEndTime) {
				if (spinSensorType.getSelectedItem().toString().length() == 0 || editTxtLatitude.getText().length() == 0
						|| editTxtLongitude.getText().length() == 0 || editTxtLocRange.getText().length() == 0
						|| editTxtNodes.getText().length() == 0 || editTxtFreq.getText().length() == 0 || editTxtTimePeriod.getText().length() == 0) {
					Toast.makeText(AddJobActivity.this,
							"Input Validation failed! All fields should be not null and the end date should be after the start date.",
							Toast.LENGTH_LONG).show();
				} else {
					boolean issuccess = serviceInvoker.addJob(1, Float.parseFloat(editTxtLatitude.getText().toString()), Float
							.parseFloat(editTxtLongitude.getText().toString()), Float.parseFloat(editTxtLocRange.getText().toString()), (startDatetime!=null)?startDatetime
							.getTimeInMillis():0, (endDatetime!=null)?endDatetime.getTimeInMillis():0, Integer.parseInt(editTxtFreq.getText().toString()), Integer
							.parseInt(editTxtTimePeriod.getText().toString()), Integer.parseInt(editTxtNodes.getText().toString()), imei, editTxtDesc
							.getText().toString(), username);
					if (issuccess) {
						// when add job completes go back to the previous step or viewjob.
						Toast.makeText(AddJobActivity.this, "Job Added Successfully.", Toast.LENGTH_LONG).show();
						Intent home = new Intent(v.getContext(), HomeActivity.class);
						startActivityForResult(home, CommonConstants.HOME_REQ_ID);

					} else {
						Toast.makeText(AddJobActivity.this, "Job Added Failed! Try again.", Toast.LENGTH_LONG).show();
					}
				}
			} else {
				Toast.makeText(AddJobActivity.this, "Invalid StartTime or ExpireTime!", Toast.LENGTH_LONG).show();
			}
		}
		
	}

	/**
	 * 
	 */
	private DatePickerDialog.OnDateSetListener startdateSetListner = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			startYear = year;
			startMonth = monthOfYear; // month is 0 based.
			startDay = dayOfMonth;
			String date = startDay + "/" + (startMonth+1) + "/" + startYear;
			btnStartDate.setText(date);
		}
	};

	/**
	 * 
	 */
	private DatePickerDialog.OnDateSetListener enddateSetListner = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			endYear = year;
			endMonth = monthOfYear; // month is 0 based.
			endDay = dayOfMonth;
			String date = endDay + "/" + (endMonth+1) + "/" + endYear;
			btnEndDate.setText(date);
		}
	};

	/**
	 * 
	 */
	private TimePickerDialog.OnTimeSetListener starttimeSetListner = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			startHour = hourOfDay;
			startMinute = minute;
			String time = startHour + ":" + startMinute;
			btnStartTime.setText(time);
		}
	};

	/**
	 * 
	 */
	private TimePickerDialog.OnTimeSetListener endtimeSetListner = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			endHour = hourOfDay;
			endMinute = minute;
			String time = endHour + ":" + endMinute;
			btnEndTime.setText(time);
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {

		case ID_START_DATEPICKER:
			Toast.makeText(AddJobActivity.this, "- onCreateDialog(ID_START_DATEPICKER) -", Toast.LENGTH_LONG).show();
			return new DatePickerDialog(this, startdateSetListner, startYear, startMonth, startDay);

		case ID_START_TIMEPICKER:
			Toast.makeText(AddJobActivity.this, "- onCreateDialog(ID_START_TIMEPICKER) -", Toast.LENGTH_LONG).show();
			return new TimePickerDialog(this, starttimeSetListner, startHour, startMinute, true);

		case ID_END_DATEPICKER:
			Toast.makeText(AddJobActivity.this, "- onCreateDialog(ID_END_DATEPICKER) -", Toast.LENGTH_LONG).show();
			return new DatePickerDialog(this, enddateSetListner, startYear, startMonth, startDay);

		case ID_END_TIMEPICKER:
			Toast.makeText(AddJobActivity.this, "- onCreateDialog(ID_END_TIMEPICKER) -", Toast.LENGTH_LONG).show();
			return new TimePickerDialog(this, endtimeSetListner, startHour, startMinute, true);

		default:
			return null;
		}
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		if (buttonView.getId() == R.id.chkBxStarttimeAdd) {
			if (!isChecked) {
				btnStartDate.setEnabled(false);
				btnStartTime.setEnabled(false);
			}else{
				btnStartDate.setEnabled(true);
				btnStartTime.setEnabled(true);
			}
		}else if(buttonView.getId() == R.id.chkBxEndtimeAdd){
			if (!isChecked) {
				btnEndDate.setEnabled(false);
				btnEndTime.setEnabled(false);
			}else{
				btnEndDate.setEnabled(true);
				btnEndTime.setEnabled(true);
			}
		}
	}

}
