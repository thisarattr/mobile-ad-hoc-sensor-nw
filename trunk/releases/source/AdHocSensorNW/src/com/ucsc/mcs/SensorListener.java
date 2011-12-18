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
	
	private Float x = 0.0f;
	private Float y = 0.0f;
	private Float z = 0.0f;
	private Float mean = 0.0f;

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
		mean = Double.valueOf(Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)))).floatValue();	
	}

	/**
	 * @return the x
	 */
	public Float getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public Float getY() {
		return y;
	}

	/**
	 * @return the z
	 */
	public Float getZ() {
		return z;
	}

	/**
	 * @return the mean
	 */
	public Float getMean() {
		return mean;
	}

	
	
}
