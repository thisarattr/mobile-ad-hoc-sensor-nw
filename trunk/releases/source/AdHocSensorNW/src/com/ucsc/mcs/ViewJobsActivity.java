/**
 * 
 */
package com.ucsc.mcs;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author thisara
 *
 */
public class ViewJobsActivity extends Activity {

	private ListView listVwJobs;
	private String lv_arr[]={"Android","iPhone","BlackBerry","AndroidPeople","Thisara","Thilanka","Rupasinghe","Terra Nova","BSG","Lost"};
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewjobs);
		
		listVwJobs=(ListView)findViewById(R.id.listVwJobs);
		// By using setAdpater method in listview we an add string array in list.
		listVwJobs.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , lv_arr));
	}

}
