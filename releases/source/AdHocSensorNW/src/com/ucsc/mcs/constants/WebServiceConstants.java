/**
 * 
 */
package com.ucsc.mcs.constants;

/**
 * @author thisara
 *
 */
public class WebServiceConstants {	

	public static final String NAMESPACE = "http://mcs.ucsc.com/SensorService/";
	public static final String URL = "http://192.168.1.110:8090/Axis2WS1.4/services/SensorService";
	//public static final String URL = "http://www.mobilesensornw.hostjava.net/Axis2WS1.4/services/SensorService";
	//public static final String URL = "http://192.168.43.28:8090/Axis2WS1.4/services/SensorService";
	//public static final String URL = "https://192.168.1.110:8443/Axis2WS1.4/services/SensorService";
	
	
	public static final String SOAP_ACTION = "http://mcs.ucsc.com/SensorService/GetMessage";
	public static final String METHOD_NAME = "input";
	
	public static final String SOAP_ACTION_AREA = "http://mcs.ucsc.com/SensorService/NewOperation";
	public static final String REQUEST_TYPE_AREA = "parameters";
	
	public static final String SOAP_ACTION_LOGIN = "http://mcs.ucsc.com/SensorService/Login";
	public static final String REQUEST_TYPE_LOGIN = "LoginRequestType";
	
	public static final String SOAP_ACTION_UPLOAD_DATA = "http://mcs.ucsc.com/SensorService/UploadData";
	public static final String REQUEST_TYPE_UPLOAD_DATA = "UploadDataReuestType";
	
	public static final String SOAP_ACTION_GET_JOBS = "http://mcs.ucsc.com/SensorService/GetJobs";
	public static final String REQUEST_TYPE_GET_JOBS = "GetJobsRequestType";
	
	public static final String SOAP_ACTION_GET_Reference_Data = "http://mcs.ucsc.com/SensorService/GetReferenceData";
	public static final String REQUEST_TYPE_GET_Reference_Data = "GetReferenceDataRequestType";
	
	public static final String SOAP_ACTION_VIEW_JOB = "http://mcs.ucsc.com/SensorService/ViewJob";
	public static final String REQUEST_TYPE_VIEW_JOB = "ViewJobRequestType";
	
	public static final String SOAP_ACTION_ADD_JOB = "http://mcs.ucsc.com/SensorService/AddJob";
	public static final String REQUEST_TYPE_ADD_JOB = "AddJobRequestType";
	
	public static final String SOAP_ACTION_VIEW_DATA = "http://mcs.ucsc.com/SensorService/ViewData";
	public static final String REQUEST_TYPE_VIEW_DATA = "ViewDataRequestType";
	
	public static final String SOAP_ACTION_SUBSCRIBE = "http://mcs.ucsc.com/SensorService/Subscribe";
	public static final String REQUEST_TYPE_SUBSCRIBE = "SubscribeRequestType";
	
	public static final String SOAP_ACTION_EMAIL_DATA = "http://mcs.ucsc.com/SensorService/EmailData";
	public static final String REQUEST_TYPE_EMAIL_DATA = "EmailDataRequestType";
	
	public static final String SOAP_ACTION_PASSWORD_RECOVER = "http://mcs.ucsc.com/SensorService/PasswordRecover";
	public static final String REQUEST_TYPE_PASSWORD_RECOVER = "PasswordRecoverRequestType";
	
	public static final String SOAP_ACTION_EDIT_JOB = "http://mcs.ucsc.com/SensorService/EditJob";
	public static final String REQUEST_TYPE_EDIT_JOB = "EditJobRequestType";
	
	public static final String SOAP_ACTION_EDIT_USER = "http://mcs.ucsc.com/SensorService/EditUser";
	public static final String REQUEST_TYPE_EDIT_USER  = "EditUserRequestType";
	
	
}
