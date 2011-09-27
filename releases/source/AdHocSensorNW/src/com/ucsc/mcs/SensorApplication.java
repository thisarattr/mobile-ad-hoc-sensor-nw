/**
 * 
 */
package com.ucsc.mcs;

import android.app.Application;

/**
 * @author thisara
 *
 */
public class SensorApplication extends Application {
	
	private boolean serviceRunning;
	
	//This method cam use to implement applciation wide operation. Like login to the server or something.
	//Refer to the pg.103 of reference book.

	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	/* (non-Javadoc)
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	/**
	 * @return the serviceRunning
	 */
	public boolean isServiceRunning() {
		return serviceRunning;
	}

	/**
	 * @param serviceRunning the serviceRunning to set
	 */
	public void setServiceRunning(boolean serviceRunning) {
		this.serviceRunning = serviceRunning;
	}
	
	

}
