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
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * @author thisara
 * 
 */
public class AddJobActivity extends Activity implements OnClickListener {

	static final int ID_START_DATEPICKER = 0;
	static final int ID_START_TIMEPICKER = 1;
	static final int ID_END_DATEPICKER = 3;
	static final int ID_END_TIMEPICKER = 4;

	private Spinner spinSensorType;
	private EditText editTxtLatitude, editTxtLongitude, editTxtLocRange, editTxtFreq, editTxtTimePeriod, editTxtNodes, editTxtDesc;
	private Button btnStartDate, btnStartTime, btnEndDate, btnEndTime, btnJobDiscard, btnJobSave, btnJobReset;
	private int startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute;
	private String imei;
	private ServiceInvoker serviceInvoker;

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
			Calendar startDatetime = Calendar.getInstance();
			startDatetime.set(startYear, startMonth, startDay, startHour, startMinute);
			Calendar endDatetime = Calendar.getInstance();
			endDatetime.set(endYear, endMonth, endDay, endHour, endMinute);
			boolean val = startDatetime.before(endDatetime);
			
			if (spinSensorType.getSelectedItem().toString().length() == 0 || editTxtLatitude.getText().length() == 0
					|| editTxtLongitude.getText().length() == 0 || editTxtLocRange.getText().length() == 0 || editTxtNodes.getText().length() == 0
					|| editTxtFreq.getText().length() == 0 || editTxtTimePeriod.getText().length() == 0) {
				Toast.makeText(AddJobActivity.this,
						"Input Validation failed! All fields should be not null and the end date should be after the start date.", Toast.LENGTH_LONG)
						.show();
			} else {
				boolean issuccess=serviceInvoker.addJob(1, Float.parseFloat(editTxtLatitude.getText().toString()), Float.parseFloat(editTxtLongitude.getText()
						.toString()), Float.parseFloat(editTxtLocRange.getText().toString()), startDatetime.getTimeInMillis(), endDatetime
						.getTimeInMillis(), Integer.parseInt(editTxtFreq.getText().toString()), Integer.parseInt(editTxtTimePeriod.getText()
						.toString()), Integer.parseInt(editTxtNodes.getText().toString()), imei, editTxtDesc.getText().toString());
				if(issuccess){
					//TODO: when add job completes go back to the previos step or viewjob.
					Toast.makeText(AddJobActivity.this, "Job Added Successfully.", Toast.LENGTH_LONG).show();
					Intent home = new Intent(v.getContext(), HomeActivity.class);
					startActivityForResult(home, CommonConstants.HOME_REQ_ID);
					
				}else{
					Toast.makeText(AddJobActivity.this, "Job Added Failed! Try again.", Toast.LENGTH_LONG).show();
				}
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

}
