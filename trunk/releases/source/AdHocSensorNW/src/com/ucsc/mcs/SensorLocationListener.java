/**
 * 
 */
package com.ucsc.mcs;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * @author thisara
 *
 */
public class SensorLocationListener implements LocationListener {

	private static final String TAG = SensorLocationListener.class.getSimpleName();
	
	private Double latitude=0.0;
	private Double longitute=0.0;
	
	public void onLocationChanged(Location location) {
		
		latitude = location.getLatitude();
		latitude = location.getLongitude();

		Log.d(TAG, "Latitute:"+latitude +"\nLongitude:"+longitute);
		
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the longitute
	 */
	public Double getLongitute() {
		return longitute;
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

}
