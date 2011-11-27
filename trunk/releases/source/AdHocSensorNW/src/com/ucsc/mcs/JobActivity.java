/**
 * 
 */
package com.ucsc.mcs;

import java.sql.Timestamp;

import com.ucsc.mcs.constants.CommonConstants;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author thisara
 *
 */
public class JobActivity extends Activity implements OnClickListener{
	
	private TextView txtJobIdVal, txtSensorVal, txtLatVal, txtLongVal, txtLocRangeVal, txtStartVal, txtEndVal, txtFreqVal, txtTimePeriodVal, txtNodesVal, txtDescVal, txtJobStatusVal;
	private Button btnJobEdit, btnJobViewData, btnJobEmailData;
	
	private Bundle bundle;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job);
		
		bundle = getIntent().getExtras();
		
		btnJobEdit = (Button) findViewById(R.id.btnJobEdit);
		btnJobViewData = (Button) findViewById(R.id.btnJobViewData);
		btnJobEmailData = (Button) findViewById(R.id.btnJobEmail);
		btnJobEdit.setOnClickListener(this);
		btnJobViewData.setOnClickListener(this);
		btnJobEmailData.setOnClickListener(this);
		
		txtJobIdVal = (TextView) findViewById(R.id.txtViewJobIdVal); 
		txtSensorVal = (TextView) findViewById(R.id.txtViewSensorVal); 
		txtLatVal = (TextView) findViewById(R.id.txtViewLatVal); 
		txtLongVal = (TextView) findViewById(R.id.txtViewLonVal);
		txtLocRangeVal = (TextView) findViewById(R.id.txtViewLocRangeVal);
		txtStartVal = (TextView) findViewById(R.id.txtViewStartVal);
		txtEndVal = (TextView) findViewById(R.id.txtViewEndVal);
		txtFreqVal = (TextView) findViewById(R.id.txtViewFreqVal);
		txtTimePeriodVal = (TextView) findViewById(R.id.txtViewTimePeriodVal);
		txtNodesVal = (TextView) findViewById(R.id.txtViewNodesVal);
		txtDescVal = (TextView) findViewById(R.id.txtViewDescVal);
		txtJobStatusVal = (TextView) findViewById(R.id.txtViewJobStatusVal);
		
		txtJobIdVal.setText((String)bundle.get(CommonConstants.VIEWJOB_ID));
		txtSensorVal.setText((String)bundle.get(CommonConstants.VIEWJOB_SENSORNAME));
		txtLatVal.setText((String)bundle.get(CommonConstants.VIEWJOB_LAT));
		txtLongVal.setText((String)bundle.get(CommonConstants.VIEWJOB_LONG));
		txtLocRangeVal.setText((String)bundle.get(CommonConstants.VIEWJOB_LOCRANGE));
		int jobStatus = Integer.parseInt((String) bundle.get(CommonConstants.VIEWJOB_STATUS));
		if (jobStatus == CommonConstants.JOB_STATUS_RUNNING) {
			txtJobStatusVal.setText("Running");
		} else if (jobStatus == CommonConstants.JOB_STATUS_COMPLETED) {
			txtJobStatusVal.setText("Completed");
		} else if (jobStatus == CommonConstants.JOB_STATUS_EXPIRED) {
			txtJobStatusVal.setText("Unsuccessful");
		}
		
		long starttimeMillis = Long.parseLong((String)bundle.get(CommonConstants.VIEWJOB_STARTTIME));
		if(starttimeMillis!=0){
			txtStartVal.setText(new Timestamp(starttimeMillis).toString());
		}else{
			txtStartVal.setText("N/A");
		}
		
		long endtimeMillis = Long.parseLong((String)bundle.get(CommonConstants.VIEWJOB_EXPIRETIME));
		if(endtimeMillis!=0){
			txtEndVal.setText(new Timestamp(endtimeMillis).toString());
		}else{
			txtEndVal.setText("N/A");
		}
		
		txtFreqVal.setText((String)bundle.get(CommonConstants.VIEWJOB_FREQ));
		txtTimePeriodVal.setText((String)bundle.get(CommonConstants.VIEWJOB_TIMEPERIOD));
		txtNodesVal.setText((String)bundle.get(CommonConstants.VIEWJOB_NODES));
		String desc = (String) bundle.get(CommonConstants.VIEWJOB_DESC);
		if (desc.length()>0 && !desc.equalsIgnoreCase("null")) {
			txtDescVal.setText(desc);
		}else{
			txtDescVal.setText("N/A");
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {

		Long jobId = Long.parseLong((String) bundle.get(CommonConstants.VIEWJOB_ID));

		SharedPreferences settings = getSharedPreferences("UserDetails", MODE_PRIVATE);
		String username = settings.getString(CommonConstants.USERNAME, "");

		if (v.getId() == R.id.btnJobEdit) {

			Intent editJob = new Intent(this, EditJobActivity.class);
			editJob.putExtras(bundle);
			startActivity(editJob);

		} else if (v.getId() == R.id.btnJobViewData) {
			
			Intent viewJobData = new Intent(this, ViewDataActivity.class);
			viewJobData.putExtras(bundle);
			startActivity(viewJobData);

		} else if (v.getId() == R.id.btnJobEmail) {

			ServiceInvoker invoker = new ServiceInvoker();
			Boolean emailSent = invoker.emailData(jobId, username);
			if (emailSent) {
				Toast.makeText(this, "Data for the Job ID: " + jobId + " sent to user mail address.", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Failed sending data for Job ID: " + jobId + ".", Toast.LENGTH_LONG).show();
			}

		}

	}

}
