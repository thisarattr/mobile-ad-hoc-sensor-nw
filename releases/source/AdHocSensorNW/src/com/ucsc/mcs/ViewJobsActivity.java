/**
 * 
 */
package com.ucsc.mcs;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ucsc.mcs.constants.CommonConstants;

import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * @author thisara
 *
 */
public class ViewJobsActivity extends ListActivity implements OnItemLongClickListener {

	private static final int EDIT_JOB_ID = 1;
	private static final int VIEW_DATA_ID = 2;
	private static final int VIEW_JOB_ID = 3;
	private static final String VIEW_JOB = "View Job";
	private static final String VIEW_DATA = "View Data";
	private static final String EDIT_JOB = "Edit Job";
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
		
		SharedPreferences settings = getSharedPreferences("UserDetails", MODE_PRIVATE);
		username = settings.getString(CommonConstants.USERNAME, "");
		
		try {
			jobList = serviceInvoker.viewJobs("0000-0000-0000", username);

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
		Intent editJob = new Intent(this, EditJobActivity.class);
		editJob.putExtras(bundle);
		startActivity(editJob);

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

		if (menuItemId == EDIT_JOB_ID) {

			Bundle bundle = new Bundle();
			for (Entry<String, String> element : selectedRec.entrySet()) {
				bundle.putString(element.getKey(), element.getValue());
			}
			Intent editJob = new Intent(this, EditJobActivity.class);
			editJob.putExtras(bundle);
			startActivity(editJob);

			Toast.makeText(this, "You selected: " + selectedRec.get(CommonConstants.VIEWJOB_DATATIME)+"  "+selectedRec.get(CommonConstants.VIEWJOB_SENSORNAME), Toast.LENGTH_LONG).show();

		} else if (menuItemId == VIEW_DATA_ID) {

			Bundle bundle = new Bundle();
			for (Entry<String, String> element : selectedRec.entrySet()) {
				bundle.putString(element.getKey(), element.getValue());
			}
			Intent viewJobData = new Intent(this, ViewDataActivity.class);
			viewJobData.putExtras(bundle);
			startActivity(viewJobData);
			
		} else if (menuItemId == VIEW_JOB_ID) {

		}

		return true;
	}
}
