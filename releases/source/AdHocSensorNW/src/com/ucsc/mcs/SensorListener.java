/**
 * 
 */
package com.ucsc.mcs;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * @author thisara
 *
 */
public class SensorListener implements SensorEventListener {
	
	private static final String TAG = SensorListener.class.getSimpleName();
	
	private float x;
	private float y;
	private float z;
	private float mean;

	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
	 */
	public void onAccuracyChanged(Sensor paramSensor, int paramInt) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	public void onSensorChanged(SensorEvent event) {
		
		x = event.values[0];
		y = event.values[1];
		z = event.values[2];
		mean = (x + y + z) / 3;	
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @return the z
	 */
	public float getZ() {
		return z;
	}

	/**
	 * @return the mean
	 */
	public float getMean() {
		return mean;
	}

	
	
}
