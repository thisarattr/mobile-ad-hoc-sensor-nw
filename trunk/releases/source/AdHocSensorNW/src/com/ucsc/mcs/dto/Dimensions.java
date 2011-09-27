/**
 * 
 */
package com.ucsc.mcs.dto;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

/**
 * @author thisara
 * 
 */
public class Dimensions implements KvmSerializable {

	public static Class DIMS_CLASS = Dimensions.class;
	private Float width;
	private Float height;

	public Dimensions() {
	}

	public Dimensions(SoapObject obj) {
		super();
		this.width = Float.parseFloat(obj.getProperty("width").toString());
		this.height = Float.parseFloat(obj.getProperty("height").toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ksoap2.serialization.KvmSerializable#getProperty(int)
	 */
	public Object getProperty(int arg0) {

		Object obj = null;
		switch (arg0) {
		case 0: {
			obj = width;
			break;
		}
		case 1: {
			obj = height;
			break;
		}
		}
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ksoap2.serialization.KvmSerializable#getPropertyCount()
	 */
	public int getPropertyCount() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ksoap2.serialization.KvmSerializable#getPropertyInfo(int,
	 * java.util.Hashtable, org.ksoap2.serialization.PropertyInfo)
	 */
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo propInfo) {

		switch (index) {
		case 0:
			propInfo.type = Float.class;
			propInfo.name = "width";
			break;
		case 1:
			propInfo.type = Float.class;
			propInfo.name = "height";
			break;
		default:
			break;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ksoap2.serialization.KvmSerializable#setProperty(int,
	 * java.lang.Object)
	 */
	public void setProperty(int index, Object value) {

		switch (index) {
		case 0:
			width = Float.parseFloat(value.toString());
			break;
		case 1:
			height = Float.parseFloat(value.toString());
			break;
		default:
			break;
		}

	}

	/**
	 * @return the width
	 */
	public Float getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(Float width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public Float getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(Float height) {
		this.height = height;
	}

}
