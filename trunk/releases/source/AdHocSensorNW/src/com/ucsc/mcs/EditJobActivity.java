/**
 * 
 */
package com.ucsc.mcs;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
public class EditJobActivity extends Activity implements OnClickListener {
	
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
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.editjob);
		
		Bundle b = getIntent().getExtras();
		
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
		spinSensorType.setEnabled(false);

		final Calendar startDate = Calendar.getInstance();
		startDate.setTimeInMillis(Long.parseLong((String)b.get(ServiceInvoker.VIEWJOB_STARTTIME)));
		startYear = startDate.get(Calendar.YEAR);
		startMonth = startDate.get(Calendar.MONTH);// month is 0 based.
		startDay = startDate.get(Calendar.DAY_OF_MONTH);
		startHour = startDate.get(Calendar.HOUR_OF_DAY);
		startMinute = startDate.get(Calendar.MINUTE);
		
		final Calendar endDate = Calendar.getInstance();
		endDate.setTimeInMillis(Long.parseLong((String)b.get(ServiceInvoker.VIEWJOB_EXPIRETIME)));
		endYear = endDate.get(Calendar.YEAR);
		endMonth = endDate.get(Calendar.MONTH);// month is 0 based.
		endDay = endDate.get(Calendar.DAY_OF_MONTH);
		endHour = endDate.get(Calendar.HOUR_OF_DAY);
		endMinute = endDate.get(Calendar.MINUTE);

		String formatStartDate = startDay + "/" + (startMonth+1) + "/" + startYear;
		String formatStartTime = startHour + ":" + startMinute;
		btnStartDate.setText(formatStartDate);
		btnStartTime.setText(formatStartTime);
		String formatEndDate = endDay + "/" + (endMonth+1) + "/" + endYear;
		String formatEndTime = endHour + ":" + endMinute;
		btnEndDate.setText(formatEndDate);
		btnEndTime.setText(formatEndTime);
		
		editTxtLatitude.setText((String)b.get(ServiceInvoker.VIEWJOB_LAT));
		editTxtLongitude.setText((String)b.get(ServiceInvoker.VIEWJOB_LONG));
		editTxtLocRange.setText((String)b.get(ServiceInvoker.VIEWJOB_LOCRANGE));
		editTxtFreq.setText((String)b.get(ServiceInvoker.VIEWJOB_FREQ));
		editTxtTimePeriod.setText((String)b.get(ServiceInvoker.VIEWJOB_TIMEPERIOD));
		editTxtNodes.setText((String)b.get(ServiceInvoker.VIEWJOB_NODES));
		editTxtDesc.setText((String)b.get(ServiceInvoker.VIEWJOB_DESC));
	}


	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
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
			Toast.makeText(EditJobActivity.this, "- onCreateDialog(ID_START_DATEPICKER) -", Toast.LENGTH_LONG).show();
			return new DatePickerDialog(this, startdateSetListner, startYear, startMonth, startDay);

		case ID_START_TIMEPICKER:
			Toast.makeText(EditJobActivity.this, "- onCreateDialog(ID_START_TIMEPICKER) -", Toast.LENGTH_LONG).show();
			return new TimePickerDialog(this, starttimeSetListner, startHour, startMinute, true);

		case ID_END_DATEPICKER:
			Toast.makeText(EditJobActivity.this, "- onCreateDialog(ID_END_DATEPICKER) -", Toast.LENGTH_LONG).show();
			return new DatePickerDialog(this, enddateSetListner, startYear, startMonth, startDay);

		case ID_END_TIMEPICKER:
			Toast.makeText(EditJobActivity.this, "- onCreateDialog(ID_END_TIMEPICKER) -", Toast.LENGTH_LONG).show();
			return new TimePickerDialog(this, endtimeSetListner, startHour, startMinute, true);

		default:
			return null;
		}
	}
}
