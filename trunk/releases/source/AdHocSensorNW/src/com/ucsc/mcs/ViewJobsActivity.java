/**
 * 
 */
package com.ucsc.mcs;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ucsc.mcs.constants.CommonConstants;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * @author thisara
 *
 */
public class ViewJobsActivity extends ListActivity {

	private String username;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ServiceInvoker serviceInvoker = new ServiceInvoker();
		List<Map<String, String>> dataList = null;
		
		SharedPreferences settings = getSharedPreferences("UserDetails", MODE_PRIVATE);
		username = settings.getString(CommonConstants.USERNAME, "");
		
		try {
			dataList = serviceInvoker.viewJobs("0000-0000-0000", username);

			SimpleAdapter adpter = new SimpleAdapter(this, dataList, R.layout.viewjob_row, new String[] { ServiceInvoker.VIEWJOB_ID,
					ServiceInvoker.VIEWJOB_DATATIME, ServiceInvoker.VIEWJOB_LAT, ServiceInvoker.VIEWJOB_LONG }, new int[] { R.id.JOBVIEW_ID,
					R.id.JOBVIEW_DATE, R.id.JOBVIEW_LAT, R.id.JOBVIEW_LONG });
			this.setListAdapter(adpter);
			
		} catch (RuntimeException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
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

		Toast.makeText(this, "You selected: " + selectedRec.get(ServiceInvoker.VIEWJOB_DATATIME)+"  "+selectedRec.get(ServiceInvoker.VIEWJOB_SENSORNAME), Toast.LENGTH_LONG).show();

	}

	
}
