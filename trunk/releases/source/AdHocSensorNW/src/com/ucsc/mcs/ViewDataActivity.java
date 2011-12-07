/**
 * 
 */
package com.ucsc.mcs;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ucsc.mcs.constants.CommonConstants;

/**
 * @author thisara
 *
 */
public class ViewDataActivity extends Activity implements OnClickListener{
	
	private ListView listViewJobData ;
	private String username;
	private Button btnEmailFooter;
	private Long jobId;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewdata);
		
		Bundle b = getIntent().getExtras();
		jobId = Long.valueOf((String)b.get(CommonConstants.VIEWJOB_ID));
		
		listViewJobData = (ListView) findViewById(R.id.listViewData);
		btnEmailFooter = (Button) findViewById(R.id.btnEmailFooter);
		btnEmailFooter.setOnClickListener(this);
		
		ServiceInvoker serviceInvoker = new ServiceInvoker();
		List<Map<String, String>> dataList = null;
		
		// Retrieve application session data.
		SharedPreferences settings = getSharedPreferences(CommonConstants.PREF_USER_DETAILS, MODE_PRIVATE);
		username = settings.getString(CommonConstants.USERNAME, "");
		
		try {
			dataList = serviceInvoker.viewData(jobId, username);

			if (!dataList.isEmpty()) {
				SimpleAdapter adapter = new SimpleAdapter(this, dataList, R.layout.viewdata_row, new String[] { CommonConstants.VIEWDATA_ID,
						CommonConstants.VIEWDATA_TIMESTAMP, CommonConstants.VIEWDATA_LAT, CommonConstants.VIEWDATA_LONG,
						CommonConstants.VIEWDATA_READING, CommonConstants.VIEWDATA_USER }, new int[] { R.id.JOBDATA_ID, R.id.JOBDATA_DATE,
						R.id.JOBDATA_LAT, R.id.JOBDATA_LONG, R.id.JOBDATA_READING, R.id.JOBDATA_USER });

				listViewJobData.setAdapter(adapter);
			} else {
				Toast.makeText(this, "No Records found!", Toast.LENGTH_LONG).show();
			}

			
		} catch (RuntimeException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {

		if (v.getId() == btnEmailFooter.getId()) {

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
