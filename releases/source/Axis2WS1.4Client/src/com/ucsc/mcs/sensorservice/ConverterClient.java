package com.ucsc.mcs.sensorservice;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import com.ucsc.mcs.sensorservice.SensorServiceStub.Dimensions;
import com.ucsc.mcs.sensorservice.SensorServiceStub.GetJobsRequestType;
import com.ucsc.mcs.sensorservice.SensorServiceStub.Input;
import com.ucsc.mcs.sensorservice.SensorServiceStub.LoginRequestType;
import com.ucsc.mcs.sensorservice.SensorServiceStub.Parameters;
import com.ucsc.mcs.sensorservice.SensorServiceStub.UploadDataResponseType;
import com.ucsc.mcs.sensorservice.SensorServiceStub.UploadDataReuestType;


public class ConverterClient {

	public static void main(String[] args) {
		try {
			SensorServiceStub stub = new SensorServiceStub();
			Parameters params = new Parameters();
			Dimensions dims = new Dimensions();
			dims.setHeight(10.0f);
			dims.setWidth(5.0f);
			params.setParameters(dims);
			//Area areaValue = stub.CalculateRectArea(params);
			//System.out.println("Height:" + dims.getHeight() + " Width:" + dims.getWidth() + " = " + "Area:" + areaValue.getArea());

			Input input = new Input();
			input.setInput("input Message!!!");
			//Out out = stub.GetMessage(input);
			//System.out.println("Output GetMessage: " + out.getOut());
			
			LoginRequestType login = new LoginRequestType();
			login.setUsername("thisara");
			login.setPassword("thilanka");
			login.setImei("0000-000-000-0");
			//System.out.println("loign authenticated!!! " + stub.Login(login).getLoginResponseType());
			
			UploadDataReuestType dataReuestType = new UploadDataReuestType();

			dataReuestType.setData("2,0000-000-000-0,1315637768421,1.0,2.0,3.00\n3,1000-000-000-0,1315637708357,2.0,3.0,4.00");
			dataReuestType.setImei("0000-000-000-0");
			//UploadDataResponseType res = stub.UploadData(dataReuestType);
			//System.out.println(res);
			
			GetJobsRequestType jobsRequestType = new GetJobsRequestType();
			jobsRequestType.setCurrentLatitude(48.8583f);
			jobsRequestType.setCurrentLongitude(2.2945f);
			jobsRequestType.setRunningJobs("2,3");
			System.out.println(stub.GetJobs(jobsRequestType).getGetJobsResponseType());

		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
}
