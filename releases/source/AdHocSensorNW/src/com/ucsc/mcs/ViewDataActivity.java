/**
 * 
 */
package com.ucsc.mcs;

import java.util.List;
import java.util.Map;

import com.ucsc.mcs.constants.CommonConstants;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * @author thisara
 *
 */
public class ViewDataActivity extends Activity {
	
	private ListView listViewJobData ;
	private String username;
	private Button btnEmailFooter;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewdata);
		
		Bundle b = getIntent().getExtras();
		Long jobId = Long.valueOf((String)b.get(CommonConstants.VIEWJOB_ID));
		
		listViewJobData = (ListView) findViewById(R.id.listViewData);
		btnEmailFooter = (Button) findViewById(R.id.btnEmailFooter);
		
		ServiceInvoker serviceInvoker = new ServiceInvoker();
		List<Map<String, String>> dataList = null;
		
		SharedPreferences settings = getSharedPreferences("UserDetails", MODE_PRIVATE);
		username = settings.getString(CommonConstants.USERNAME, "");
		
		try {
			dataList = serviceInvoker.viewData(jobId, username);

			SimpleAdapter adapter = new SimpleAdapter(this, dataList, R.layout.viewdata_row, new String[] { CommonConstants.VIEWDATA_ID,
					CommonConstants.VIEWDATA_TIMESTAMP, CommonConstants.VIEWDATA_LAT, CommonConstants.VIEWDATA_LONG,
					CommonConstants.VIEWDATA_READING, CommonConstants.VIEWDATA_USER }, new int[] { R.id.JOBDATA_ID, R.id.JOBDATA_DATE,
					R.id.JOBDATA_LAT, R.id.JOBDATA_LONG, R.id.JOBDATA_READING, R.id.JOBDATA_USER });
		
			listViewJobData.setAdapter(adapter);

			
		} catch (RuntimeException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
		//listViewJobData.setOnItemLongClickListener(this);
	    //registerForContextMenu(listViewJobData);
	}

}
