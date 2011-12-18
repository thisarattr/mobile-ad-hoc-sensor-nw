/**
 * 
 */
package com.ucsc.mcs;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.ucsc.mcs.constants.CommonConstants;

/**
 * @author thisara
 *
 */
public class ViewJobsActivity extends ListActivity implements OnItemLongClickListener {

	private static final int EDIT_JOB_ID = 1;
	private static final int VIEW_DATA_ID = 2;
	private static final int VIEW_JOB_ID = 3;
	private static final int EMAIL_DATA_ID = 4;
	private static final String VIEW_JOB = "View Job";
	private static final String VIEW_DATA = "View Data";
	private static final String EDIT_JOB = "Edit Job";
	private static final String EMAIL_DATA = "Email Data";
	private String username;
	private ListView listViewJobList;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		listViewJobList = getListView();
		ServiceInvoker serviceInvoker = new ServiceInvoker();
		List<Map<String, String>> jobList = null;
		
		// Retrieve application session data.
		SharedPreferences settings = getSharedPreferences(CommonConstants.PREF_USER_DETAILS, MODE_PRIVATE);
		username = settings.getString(CommonConstants.USERNAME, "");
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		
		try {
			jobList = serviceInvoker.viewJobs(imei, username);

			SimpleAdapter adpter = new SimpleAdapter(this, jobList, R.layout.viewjob_row, new String[] { CommonConstants.VIEWJOB_ID,
					CommonConstants.VIEWJOB_DATATIME, CommonConstants.VIEWJOB_LAT, CommonConstants.VIEWJOB_LONG }, new int[] { R.id.JOBVIEW_ID,
					R.id.JOBVIEW_DATE, R.id.JOBVIEW_LAT, R.id.JOBVIEW_LONG });
			this.setListAdapter(adpter);
			
		} catch (RuntimeException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
	    this.getListView().setOnItemLongClickListener(this);
	    registerForContextMenu(listViewJobList);
	}

	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		// Get the item that was clicked
		Map<String, String> selectedRec = (Map<String, String>) this.getListAdapter().getItem(position);
		
		Bundle bundle = new Bundle();
		for (Entry<String, String> element : selectedRec.entrySet()) {
			bundle.putString(element.getKey(), element.getValue());
		}
		//Intent editJob = new Intent(this, EditJobActivity.class);
		//editJob.putExtras(bundle);
		Intent job = new Intent(this, JobActivity.class);
		job.putExtras(bundle);
		startActivity(job);

		Toast.makeText(this, "You selected: " + selectedRec.get(CommonConstants.VIEWJOB_DATATIME)+"  "+selectedRec.get(CommonConstants.VIEWJOB_SENSORNAME), Toast.LENGTH_LONG).show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android.widget.AdapterView,
	 * android.view.View, int, long)
	 */
	public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View,
	 * android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if (listViewJobList.getId() == v.getId()) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			// Get the item that was clicked
			Map<String, String> selectedRec = (Map<String, String>) this.getListAdapter().getItem(info.position);
			String jobId = selectedRec.get(CommonConstants.VIEWJOB_ID);
			menu.setHeaderTitle("Job ID: " + jobId);

			menu.add(Menu.NONE, EDIT_JOB_ID, EDIT_JOB_ID, EDIT_JOB);
			menu.add(Menu.NONE, VIEW_DATA_ID, VIEW_DATA_ID, VIEW_DATA);
			menu.add(Menu.NONE, VIEW_JOB_ID, VIEW_JOB_ID, VIEW_JOB);
			menu.add(Menu.NONE, EMAIL_DATA_ID, EMAIL_DATA_ID, EMAIL_DATA);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		int menuItemId = item.getItemId();
		
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		// Get the item that was clicked
		Map<String, String> selectedRec = (Map<String, String>) this.getListAdapter().getItem(info.position);
		Long jobId = Long.parseLong(selectedRec.get(CommonConstants.VIEWJOB_ID));
		int jobStatus = Integer.parseInt(selectedRec.get(CommonConstants.VIEWJOB_STATUS));
		
		Bundle bundle = new Bundle();
		for (Entry<String, String> element : selectedRec.entrySet()) {
			bundle.putString(element.getKey(), element.getValue());
		}

		if (menuItemId == EDIT_JOB_ID) {

			if (jobStatus == CommonConstants.JOB_STATUS_RUNNING) {
				Intent editJob = new Intent(this, EditJobActivity.class);
				editJob.putExtras(bundle);
				startActivity(editJob);
				Toast.makeText(
						this,
						"You selected: " + selectedRec.get(CommonConstants.VIEWJOB_DATATIME) + "  "
								+ selectedRec.get(CommonConstants.VIEWJOB_SENSORNAME), Toast.LENGTH_LONG).show();

			} else {
				Toast.makeText(this, "You can edit running jobs only.", Toast.LENGTH_LONG).show();
			}

		} else if (menuItemId == VIEW_DATA_ID) {

			Intent viewJobData = new Intent(this, ViewDataActivity.class);
			viewJobData.putExtras(bundle);
			startActivity(viewJobData);
			
		} else if (menuItemId == VIEW_JOB_ID) {
			
			Intent job = new Intent(this, JobActivity.class);
			job.putExtras(bundle);
			startActivity(job);

		} else if (menuItemId == EMAIL_DATA_ID) {

			ServiceInvoker invoker = new ServiceInvoker();
			Boolean emailSent = invoker.emailData(jobId, username);
			if (emailSent) {
				Toast.makeText(this, "Data for the Job ID: " + jobId + " sent to user mail address.", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Failed sending data for Job ID: " + jobId + ".", Toast.LENGTH_LONG).show();
			}
		}

		return true;
	}
}
