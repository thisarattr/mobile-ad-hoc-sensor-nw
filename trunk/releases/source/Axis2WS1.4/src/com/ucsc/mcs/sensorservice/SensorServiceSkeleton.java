/**
 * SensorServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */
package com.ucsc.mcs.sensorservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


/**
 * SensorServiceSkeleton java skeleton for the axisService
 */
public class SensorServiceSkeleton {

	private static final String ROW_DELEMETER = "\n";
	private static final String DATA_DELEMETER = ",";
	private static final double RADIAN = 0.017453293;
	private static final Logger LOG = Logger.getLogger(SensorServiceSkeleton.class.getCanonicalName());
	
	/**
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	private Connection getMySqlConnection() throws NamingException, SQLException {
		
		Context initCtx;
		initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource) envCtx.lookup("jdbc/sensornw");

		return ds.getConnection();
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param parameters
	 */

	public com.ucsc.mcs.sensorservice.Area CalculateRectArea(com.ucsc.mcs.sensorservice.Parameters parameters) {
		Area area = new Area();
		float value = parameters.getParameters().getHeight() * parameters.getParameters().getWidth();
		area.setArea(new Float(value).intValue());
		return area;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param input
	 */

	public com.ucsc.mcs.sensorservice.Out GetMessage(com.ucsc.mcs.sensorservice.Input input) {
		Out out = new Out();
		out.setOut("This is the input message:" + input.getInput());

		Connection conn;
		PreparedStatement prepStmt;
		
		try {
			conn = getMySqlConnection();
			prepStmt = conn
					.prepareStatement("insert into job (datetime, sensor_id, frequency, location, time_period, nodes, user_id, description) values (now(),1,1,'loc',2,2,2,'desc')");
			prepStmt.execute();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return out;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param loginRequestType
	 */

	public com.ucsc.mcs.sensorservice.LoginResponseType Login(com.ucsc.mcs.sensorservice.LoginRequestType loginRequestType) {
		boolean isLogin = false;
		Connection conn;
		PreparedStatement prepStmt;
		ResultSet resultSet;

		try {
			conn = getMySqlConnection();
			prepStmt = conn.prepareStatement("select password from user where username='" + loginRequestType.getUsername() + "'");
			resultSet = prepStmt.executeQuery();
			if (resultSet.next()) {
				String pw = resultSet.getString("password");
				if (loginRequestType.getPassword().equals(pw)) {
					isLogin = true;
				}
			}
			resultSet.close();
			prepStmt.close();
			conn.close();
			resultSet=null;
			prepStmt=null;
			conn=null;
			
		} catch (NamingException e) {
			LOG.log(Level.SEVERE, "Error occurred while select Login. Original stacktrace: " + e.toString());
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Error occurred while select Login. Original stacktrace: " + e.toString());
		}
		LoginResponseType res = new LoginResponseType();
		res.setLoginResponseType(isLogin);
		return res;
	}
	
	/**
	 * Auto generated method signature
	 * 
	 * @param uploadDataReuestType
	 */

	public com.ucsc.mcs.sensorservice.UploadDataResponseType UploadData(com.ucsc.mcs.sensorservice.UploadDataReuestType uploadDataReuestType) {

		Connection conn;
		PreparedStatement prepStmt;
		String sql;

		//Data should be on this order. job_id, imei, datetime, latitude, longitude, reading
		String[] rawArray = uploadDataReuestType.getData().split(ROW_DELEMETER);
		UploadDataResponseType responseType = new UploadDataResponseType();

		try {
			conn = getMySqlConnection();
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO data(job_id, imei, datetime, latitude, longitude, reading) VALUES(?,?,?,?,?,?)";
			prepStmt = conn.prepareStatement(sql); 

			for (int i = 0; i < rawArray.length; i++) {
				String[] raw = rawArray[i].split(DATA_DELEMETER);

				prepStmt.setLong(1, Long.parseLong(raw[0]));
				prepStmt.setString(2, raw[1]);
				prepStmt.setTimestamp(3, new java.sql.Timestamp(Long.parseLong(raw[2])));
				prepStmt.setDouble(4, Double.parseDouble(raw[3]));
				prepStmt.setDouble(5, Double.parseDouble(raw[4]));
				prepStmt.setDouble(6, Double.parseDouble(raw[5]));
				prepStmt.addBatch();
			}
			
			int[] array = prepStmt.executeBatch();
			boolean isSuccess = true;
			for (int i = 0; i < array.length; i++) {
				if (array[i] != 1) {
					isSuccess = false;
					break;
				}
			}
			if (isSuccess) {
				conn.commit();
				//Update user rank by increment by 1.
				sql = "UPDATE user SET rank=rank+1 WHERE imei=?";
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setString(1, uploadDataReuestType.getImei());
				prepStmt.execute();
				
			} else {
				conn.rollback();
			}
			responseType.setUploadDataResponseType(isSuccess);
			
		} catch (NamingException e) {
			LOG.log(Level.SEVERE, "Error occurred while upload data. Original stacktrace: " + e.toString());
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Error occurred while upload data. Original stacktrace: " + e.toString());
		}

		return responseType;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param getJobsRequestType
	 */

	public com.ucsc.mcs.sensorservice.GetJobsResponseType GetJobs(com.ucsc.mcs.sensorservice.GetJobsRequestType getJobsRequestType) {
		
		Connection conn;
		Statement stmt;
		PreparedStatement prepStmt;
		ResultSet resultSet;
		
		GetJobsResponseType jobsResponseType = new GetJobsResponseType();
		
		double nodeLat = getJobsRequestType.getCurrentLatitude();
		double nodeLon = getJobsRequestType.getCurrentLongitude();
		String currJobSet = getJobsRequestType.getRunningJobs();

		
		try {
			conn = getMySqlConnection();

			/* 
			 * All the job selection logic goes with the sql.
			 * 
			 * 1. Select the jobs that are close the given node is selected by following sql using the given distance
			 * 	  dist = arccos(sin(lat1) · sin(lat2) + cos(lat1) · cos(lat2) · cos(lon1 - lon2)) · R	
			 *    SELECT * FROM job WHERE acos(sin(latitude*0.017453293)*sin(48.8583*0.017453293) + cos(latitude*0.017453293) * cos(48.8583*0.017453293) * cos(longitude*0.017453293 - 2.2945*0.017453293)) * 6371 <= 5837
			 * 2. number of nodes(nodes needed to complete the job) should be greater than the node_count which is the number nodes currently do that job. 
			 *    That is this job still need more nodes to do its work.
			 * 3. job expire_date must be satisfied. It should fall ahead of now().
			 * 4. if job start_time is null, start_time will be now()
			 *    if job start_time is not null and its already in past, then start_time is now() and if still start_time is in future, it will be the start_time.
			 * 5. if expire_time is null, expire_time will be (start_time + time_period). 
			 *    if expire_time is not null and when (start_time + time_period), if the total is below the expire_time then total time will be the expire_time, 
			 *    else expire_time will be itself.
			 * 6. First order by starttime ascending order to find the closest jobs to the current time. 
			 *    Then inner join job table with user table and order by user.rank DESC to get the highest rank users job with priority within the closest jobs.
			 * 7. DateTime on sql can not assign as null it will always be "0000-00-00 00:00:00" there for we need to check for "UNIX_TIMESTAMP(field)=0". 
			 *    This will say if the field is 0 or not.
			 * 
			 */
			
			String sql = "SELECT j.id,j.datetime,j.sensor_id,tempjob.start_time,IF(UNIX_TIMESTAMP(expire_time)=0,ADDTIME(tempjob.start_time,CONCAT(time_period,':00:00')),IF(ADDTIME(tempjob.start_time,CONCAT(time_period,':00:00'))<expire_time,ADDTIME(tempjob.start_time,CONCAT(time_period,':00:00')),expire_time)) expire_time ,j.frequency,j.time_period,j.longitude,j.latitude,j.loc_range,j.user_id " +
					"FROM (SELECT IF(UNIX_TIMESTAMP(start_time)=0,now(),IF(start_time<now(),now(),start_time)) start_time,id FROM job) tempjob INNER JOIN job j on j.id=tempjob.id INNER JOIN user u ON j.user_id=u.id " +
					"WHERE acos(sin(latitude*"+RADIAN+") * sin("+nodeLat*RADIAN+") + cos(latitude*"+RADIAN+") * cos("+nodeLat*RADIAN+") * cos(longitude*"+RADIAN+"-"+nodeLon*RADIAN +")) * 6371 <= loc_range " +
					"AND j.id NOT IN ("+currJobSet+") AND node_count<nodes AND IF(UNIX_TIMESTAMP(expire_time)=0,true,expire_time>=now()) ORDER BY tempjob.start_time ASC, u.rank DESC";
		
			
			System.out.println(sql);
			prepStmt = conn.prepareStatement(sql); 
			resultSet = prepStmt.executeQuery();
			
			//id, datetime, sensor_id, frequency, latitude, longitude, loc_range, user_id, start_time, expire_time
			StringBuffer job = new StringBuffer();
			if(resultSet.first()){
				job.append(resultSet.getInt("id")+DATA_DELEMETER);
				job.append(resultSet.getTimestamp("datetime").getTime()+DATA_DELEMETER);
				job.append(resultSet.getInt("sensor_id")+DATA_DELEMETER);
				job.append(resultSet.getInt("frequency")+DATA_DELEMETER);
				job.append(resultSet.getDouble("latitude")+DATA_DELEMETER);
				job.append(resultSet.getDouble("longitude")+DATA_DELEMETER);
				job.append(resultSet.getDouble("loc_range")+DATA_DELEMETER);
				job.append(resultSet.getInt("user_id")+DATA_DELEMETER);
				job.append(resultSet.getTimestamp("start_time").getTime()+DATA_DELEMETER);
				job.append(resultSet.getTimestamp("expire_time").getTime());
				//job.append(resultSet.getInt("status"));
				
				//Increment the node count by to indicate number of nodes processing this job.
				sql = "UPDATE job SET node_count=node_count+1 WHERE id=?";
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setInt(1, resultSet.getInt("id"));
				prepStmt.execute();
				jobsResponseType.setGetJobsResponseType(job.toString());
			}else{
				jobsResponseType.setGetJobsResponseType(" ");
			}
			
			
		} catch (NamingException e) {
			LOG.log(Level.SEVERE, "Error occurred while select jobs data. Original stacktrace: " + e.toString());
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Error occurred while select jobs data. Original stacktrace: " + e.toString());
		}
		
		return jobsResponseType;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param getReferenceDataRequestType
	 */

	public com.ucsc.mcs.sensorservice.GetReferenceDataResponseType GetReferenceData(
			com.ucsc.mcs.sensorservice.GetReferenceDataRequestType getReferenceDataRequestType) {
		
		Connection conn;
		PreparedStatement prepStmt;
		ResultSet resultSet;

		GetReferenceDataResponseType responseType = new GetReferenceDataResponseType();

		try {
			conn = getMySqlConnection();
			String sql = "SELECT id, name, description, datetime FROM sensor";
			prepStmt = conn.prepareStatement(sql);
			resultSet = prepStmt.executeQuery();

			StringBuffer sensor = new StringBuffer();
			while (resultSet.next()) {
				sensor.append(resultSet.getInt("id") + DATA_DELEMETER);
				sensor.append(resultSet.getString("name") + DATA_DELEMETER);
				sensor.append(resultSet.getString("description") + DATA_DELEMETER);
				sensor.append(resultSet.getTimestamp("datetime").getTime());
				if (!resultSet.isLast()) {
					sensor.append(ROW_DELEMETER);
				}
			}
			responseType.setGetReferenceDataResponseType(sensor.toString());
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Error occurred while get reference data. Original stacktrace: " + e.toString());
		} catch (NamingException e) {
			LOG.log(Level.SEVERE, "Error occurred while get reference data. Original stacktrace: " + e.toString());
		}

		return responseType;
	}
	
	/**
	 * Auto generated method signature
	 * 
	 * @param emailDataRequestType
	 */

	public com.ucsc.mcs.sensorservice.EmailDataResponseType EmailData(com.ucsc.mcs.sensorservice.EmailDataRequestType emailDataRequestType) {
		// TODO : fill this with the necessary business logic
		throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#EmailData");
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param addJobRequestType
	 */

	public com.ucsc.mcs.sensorservice.AddJobResponseType AddJob(com.ucsc.mcs.sensorservice.AddJobRequestType addJobRequestType) {
		
		Connection conn;
		PreparedStatement prepStmt;
		ResultSet resultSet;
		
		AddJobResponseType addJobResponse = new AddJobResponseType();
		String sqlJobId = "SELECT id FROM user WHERE username=? OR imei=?";
		String sqlJobInsrt = "INSERT INTO job (sensor_id,nodes,node_count,user_id,latitude,longitude,loc_range,start_time,expire_time,frequency,time_period,description,datetime) " +
								"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,now())";
		boolean isSuccess = false;
		long userId=-1;
		
		try {
			conn = getMySqlConnection();
			prepStmt = conn.prepareStatement(sqlJobId);
			prepStmt.setString(1, addJobRequestType.getUsername());
			prepStmt.setString(2, addJobRequestType.getImei());
			resultSet = prepStmt.executeQuery();
			if (resultSet.first() && resultSet.isLast()) {
				userId = resultSet.getLong(1);
			}else{
				LOG.log(Level.SEVERE, "Error occurred while Adding new job. Invalid username or imei. ");
			}

			if (userId != -1) {
				prepStmt = conn.prepareStatement(sqlJobInsrt);
				prepStmt.setInt(1, addJobRequestType.getSensorType());
				prepStmt.setInt(2, addJobRequestType.getNodes());
				prepStmt.setInt(3, 0); //node count should start from 0.
				prepStmt.setLong(4, userId);
				prepStmt.setFloat(5, addJobRequestType.getLatitude());
				prepStmt.setFloat(6, addJobRequestType.getLongitude());
				prepStmt.setFloat(7, addJobRequestType.getLocRange());
				prepStmt.setTimestamp(8, new Timestamp(addJobRequestType.getStarttime()));
				prepStmt.setTimestamp(9, new Timestamp(addJobRequestType.getEndtime()));
				prepStmt.setInt(10, addJobRequestType.getFrequency());
				prepStmt.setInt(11, addJobRequestType.getTimePeriod());
				prepStmt.setString(12, addJobRequestType.getDescription());
				prepStmt.execute();
				isSuccess=true;
			}
		} catch (NamingException e) {
			LOG.log(Level.SEVERE, "Error occurred while Adding new job. Original stacktrace: " + e.toString());
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Error occurred while Adding new job. Original stacktrace: " + e.toString());
		}
		addJobResponse.setAddJobResponseType(isSuccess);
		return addJobResponse;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param subscribeRequestType
	 */

	public com.ucsc.mcs.sensorservice.SubscribeResponseType Subscribe(com.ucsc.mcs.sensorservice.SubscribeRequestType subscribeRequestType) {
		
		Connection conn;
		PreparedStatement prepStmt;
		boolean isSuccess=false;
		
		SubscribeResponseType subscribeResponse = new SubscribeResponseType();
		
		String sql = "INSERT INTO user (username,password,fullname,imei,rank,email,datetime) values (?,?,?,?,?,?,now())";
		
		try {
			conn = getMySqlConnection();
			prepStmt=conn.prepareStatement(sql);
			prepStmt.setString(1, subscribeRequestType.getUsername());
			prepStmt.setString(2, subscribeRequestType.getPassword());
			prepStmt.setString(3, subscribeRequestType.getFullname());
			prepStmt.setString(4, subscribeRequestType.getImei());
			prepStmt.setInt(5,0);// rank stars with 0 and increments with each upload.
			prepStmt.setString(6, subscribeRequestType.getEmail());
			prepStmt.execute();
			isSuccess=true;
		} catch (NamingException e) {
			LOG.log(Level.SEVERE, "Error occurred while subscribing nwe user:"+subscribeRequestType.getUsername()+". Original stacktrace: " + e.toString());
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Error occurred while subscribing nwe user:"+subscribeRequestType.getUsername()+". Username already exists. Try another one. Original stacktrace: " + e.toString());
		}
		subscribeResponse.setSubscribeResponseType(isSuccess);
		return subscribeResponse;
	}


	/**
	 * Auto generated method signature
	 * 
	 * @param viewDataRequestType
	 */

	public com.ucsc.mcs.sensorservice.ViewDataResponseType ViewData(com.ucsc.mcs.sensorservice.ViewDataRequestType viewDataRequestType) {
		Connection conn;
		PreparedStatement prepStmt;
		ResultSet resultSet;
		//boolean isSuccess=false;
		
		ViewDataResponseType viewDataResponse = new ViewDataResponseType();
		
		String sql = "SELECT d.id, d.datetime,d.latitude,d.longitude,d.reading " +
				"FROM data d, (SELECT j.id FROM job j INNER JOIN user u ON j.user_id=u.id WHERE username=? AND j.id=?) tmp " +
				"WHERE d.job_id=tmp.id";
		StringBuffer output = new StringBuffer();
		
		try {
			conn = getMySqlConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, viewDataRequestType.getUsername());
			prepStmt.setLong(2, viewDataRequestType.getJobId());
			resultSet = prepStmt.executeQuery();
			
			while(resultSet.next()){
				output.append(resultSet.getLong("id")+DATA_DELEMETER);
				output.append(resultSet.getTimestamp("datetime").getTime()+DATA_DELEMETER);
				output.append(resultSet.getDouble("latitude")+DATA_DELEMETER);
				output.append(resultSet.getDouble("longitude")+DATA_DELEMETER);
				output.append(resultSet.getDouble("reading"));
				if(!resultSet.isLast()){
					output.append(ROW_DELEMETER);
				}
			}
			
			if(output.toString().isEmpty()){
				//Query returned null no error occurred.
				output.append("No records found on the requested job. Job need to be added by the same user to retrieve them.");
			}
			
		} catch (NamingException e) {
			LOG.log(Level.SEVERE, "Error occurred while retrieving data for user:"+viewDataRequestType.getUsername()+" and jobId:"+viewDataRequestType.getJobId()+". Original stacktrace: " + e.toString());
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Error occurred while retrieving data for user:"+viewDataRequestType.getUsername()+" and jobId:"+viewDataRequestType.getJobId()+". Original stacktrace: " + e.toString());
		}
		if(output.toString().isEmpty()){
			//This is empty because error occurred, Otherwise there will be error message at least.
			output.append("Error occurred while retrieving data for user:"+viewDataRequestType.getUsername()+" and jobId:"+viewDataRequestType.getJobId());
		}
		viewDataResponse.setViewDataResponseType(output.toString());
		return viewDataResponse;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param viewJobRequestType
	 */

	public com.ucsc.mcs.sensorservice.ViewJobResponseType ViewJob(com.ucsc.mcs.sensorservice.ViewJobRequestType viewJobRequestType) {
		Connection conn;
		PreparedStatement prepStmt;
		ResultSet resultSet;
		//boolean isSuccess=false;
		
		ViewJobResponseType viewJobResponse = new ViewJobResponseType();
		// id, sensor_name, start_time, expire_time, frequency, time_period, latitude, longitude, loc_range, nodes, description, datetime
		String sql = "SELECT j.id,s.name,UNIX_TIMESTAMP(j.start_time) start_time,UNIX_TIMESTAMP(j.expire_time) expire_time,j.frequency,j.time_period,j.latitude,j.longitude,j.loc_range,j.nodes,j.description,j.datetime " +
				"FROM job j INNER JOIN user u ON j.user_id=u.id INNER JOIN sensor s ON j.sensor_id=s.id WHERE u.username=?";
		StringBuffer output = new StringBuffer();
		
		try {
			conn = getMySqlConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, viewJobRequestType.getUsername());

			resultSet = prepStmt.executeQuery();

			while (resultSet.next()) {
				output.append(resultSet.getLong("id") + DATA_DELEMETER);
				output.append(resultSet.getString("name") + DATA_DELEMETER);
				// start_time and expire_time is taken as unix_timestamp from DB, cos otherwise if this becomes 0 then there is a issue when converting into timestamp.
				output.append(resultSet.getLong("start_time") + DATA_DELEMETER);
				output.append(resultSet.getLong("expire_time") + DATA_DELEMETER);
				output.append(resultSet.getInt("frequency") + DATA_DELEMETER);
				output.append(resultSet.getInt("time_period") + DATA_DELEMETER);
				output.append(resultSet.getDouble("latitude") + DATA_DELEMETER);
				output.append(resultSet.getDouble("longitude") + DATA_DELEMETER);
				output.append(resultSet.getDouble("loc_range") + DATA_DELEMETER);
				output.append(resultSet.getInt("nodes") + DATA_DELEMETER);
				output.append(resultSet.getString("description") + DATA_DELEMETER);
				output.append(resultSet.getTimestamp("datetime").getTime());
				if (!resultSet.isLast()) {
					output.append(ROW_DELEMETER);
				}
			}
			if (output.toString().isEmpty()) {
				output.append("No Jobs added by you.");
			}
		} catch (NamingException e) {
			LOG.log(Level.SEVERE, "Error occurred while retrieving Job data for user:" + viewJobRequestType.getUsername() + ". Original stacktrace: "
					+ e.toString());
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Error occurred while retrieving Job data for user:" + viewJobRequestType.getUsername() + ". Original stacktrace: "
					+ e.toString());
		}
		if (output.toString().isEmpty()) {
			output.append("Error occurred while retrieving Job data for user:" + viewJobRequestType.getUsername() + ".");
		}
		viewJobResponse.setViewJobResponseType(output.toString());
		return viewJobResponse;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param passwordRecoverRequestType
	 */

	public com.ucsc.mcs.sensorservice.PasswordRecoverResponseType PasswordRecover(
			com.ucsc.mcs.sensorservice.PasswordRecoverRequestType passwordRecoverRequestType) {
		// TODO : fill this with the necessary business logic
		throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#PasswordRecover");
	}

}
