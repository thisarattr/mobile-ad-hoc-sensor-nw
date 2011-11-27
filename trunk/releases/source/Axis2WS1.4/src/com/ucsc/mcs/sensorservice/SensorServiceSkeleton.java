/**
 * SensorServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */
package com.ucsc.mcs.sensorservice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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
	
	private DataSource ds = null; 
	
	/**
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	private Connection getMySqlConnection() throws NamingException, SQLException {

		if (ds == null) {
			Context initCtx;
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			ds = (DataSource) envCtx.lookup("jdbc/sensornw");
		}

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
			
			String password = loginRequestType.getPassword();
			byte[] bytesPassword = password.getBytes("UTF-8");

			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] passwordDigest = md.digest(bytesPassword);
			String md5Password = new String(passwordDigest);
			
			conn = getMySqlConnection();
			String sql = "select count(*) val from user where username=? and password=?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, loginRequestType.getUsername());
			prepStmt.setString(2, md5Password);
			resultSet = prepStmt.executeQuery();
			
			if (resultSet.next()) {
				Long count = resultSet.getLong("val");
				if (count == 1) {
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
		} catch (UnsupportedEncodingException e) {
			LOG.log(Level.SEVERE, "Error occurred while select Login. Original stacktrace: " + e.toString());
		} catch (NoSuchAlgorithmException e) {
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
			
			long userId = -1;
			String sqlUsername = "SELECT id FROM user WHERE username=?";
			PreparedStatement prepStmtUsername = conn.prepareStatement(sqlUsername);
			prepStmtUsername.setString(1, uploadDataReuestType.getUsername());
			ResultSet resultUsername = prepStmtUsername.executeQuery();
			if (resultUsername.first()) {
				userId = resultUsername.getLong("id");
			}
			
			sql = "INSERT INTO data(job_id, imei, datetime, latitude, longitude, reading, user_id) VALUES(?,?,?,?,?,?,?)";
			prepStmt = conn.prepareStatement(sql); 

			for (int i = 0; i < rawArray.length; i++) {
				String[] raw = rawArray[i].split(DATA_DELEMETER);

				prepStmt.setLong(1, Long.parseLong(raw[0]));
				prepStmt.setString(2, raw[1]);
				prepStmt.setTimestamp(3, new java.sql.Timestamp(Long.parseLong(raw[2])));
				prepStmt.setDouble(4, Double.parseDouble(raw[3]));
				prepStmt.setDouble(5, Double.parseDouble(raw[4]));
				prepStmt.setDouble(6, Double.parseDouble(raw[5]));
				prepStmt.setLong(7, userId);
				prepStmt.addBatch();
			}
			System.out.println(new Timestamp(System.currentTimeMillis()).toString()+": "+ rawArray.length+" numbers of records trying upload by user "+uploadDataReuestType.getUsername());
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
				System.out.println(new Timestamp(System.currentTimeMillis()).toString()+": "+uploadDataReuestType.getUsername()+" user: Data upload successful.");
				
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
			 *    This will say if the field is 0 or not. And UNIX_TIMESTAMP is from seconds but java side it except time in millisecond when converting into
			 *    data formats. Therefore multiply by 1000.
			 * 
			 */
			
			String sql = "SELECT j.id,j.datetime,j.sensor_id,tempjob.start_time,IF(UNIX_TIMESTAMP(expire_time)=0,ADDTIME(tempjob.start_time,CONCAT(time_period,':00:00')),IF(ADDTIME(tempjob.start_time,CONCAT(time_period,':00:00'))<expire_time,ADDTIME(tempjob.start_time,CONCAT(time_period,':00:00')),expire_time)) expire_time ,j.frequency,j.time_period,j.longitude,j.latitude,j.loc_range,j.user_id,j.status " +
					"FROM (SELECT IF(UNIX_TIMESTAMP(start_time)=0,now(),IF(start_time<now(),now(),start_time)) start_time,id FROM job) tempjob INNER JOIN job j on j.id=tempjob.id INNER JOIN user u ON j.user_id=u.id " +
					"WHERE acos(sin(latitude*"+RADIAN+") * sin("+nodeLat*RADIAN+") + cos(latitude*"+RADIAN+") * cos("+nodeLat*RADIAN+") * cos(longitude*"+RADIAN+"-"+nodeLon*RADIAN +")) * 6371 <= loc_range " +
					"AND j.id NOT IN ("+currJobSet+") AND node_count<nodes AND IF(UNIX_TIMESTAMP(expire_time)=0,true,expire_time>=now()) ORDER BY tempjob.start_time ASC, u.rank DESC";
		
			
			System.out.println(new Timestamp(System.currentTimeMillis()).toString()+ ": " +sql);
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
				job.append(resultSet.getTimestamp("expire_time").getTime()+DATA_DELEMETER);
				job.append(resultSet.getInt("status"));
				
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
		
		Connection conn;
		PreparedStatement prepStmt;
		ResultSet resultSet;
		boolean isSuccess=false;
		
		String username = emailDataRequestType.getImei();
		Long jobId = emailDataRequestType.getJobId();
		
		
		String sql = "SELECT d.id, d.datetime,d.latitude,d.longitude,d.reading, tmp.username, tmp.email " +
				"FROM data d, (SELECT j.id, u.username, u.email FROM job j INNER JOIN user u ON j.user_id=u.id WHERE username=? AND j.id=?) tmp " +
				"WHERE d.job_id=tmp.id";
		StringBuffer output = new StringBuffer();
		
		try {
			conn = getMySqlConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, username);
			prepStmt.setLong(2, jobId);
			resultSet = prepStmt.executeQuery();
			
			// Prepare email text.
			String userEmail = null;
			StringBuffer emailText = new StringBuffer();
			emailText.append("This mail was sent by Mobile Ad-Hoc Sensor Nwtwork. Attached file contains data for following job," + ROW_DELEMETER + ROW_DELEMETER);
			emailText.append("Job ID\t\t: " + jobId + ROW_DELEMETER);
			emailText.append("Username\t: " + username + ROW_DELEMETER);
			emailText.append("Job Expire\t: " + ROW_DELEMETER); // retrieve the job status updated by the DB trigger.
			
			while(resultSet.next()){
				if (output.length() == 0) {
					output.append("ID" + DATA_DELEMETER + "Date" + DATA_DELEMETER + "Time" + DATA_DELEMETER + "Latitude" + DATA_DELEMETER
							+ "Longitude" + DATA_DELEMETER + "Reading" + DATA_DELEMETER + "Username" + ROW_DELEMETER);
				}
				output.append(resultSet.getLong("id")+DATA_DELEMETER);
				output.append(resultSet.getTimestamp("datetime")+DATA_DELEMETER);
				output.append(resultSet.getDouble("latitude")+DATA_DELEMETER);
				output.append(resultSet.getDouble("longitude")+DATA_DELEMETER);
				output.append(resultSet.getDouble("reading")+DATA_DELEMETER);
				output.append(resultSet.getString("username"));
				if(!resultSet.isLast()){
					output.append(ROW_DELEMETER);
				}
				if(userEmail==null){
					userEmail = resultSet.getString("email");
				}
			}
			
			if(output.toString().isEmpty()){
				output.append("No records found on the requested job. Job need to be added by the same user to retrieve them.");
				
			}else{	//Send mail
				
				Session session = null;
				Context initCtx = new InitialContext();
				Context envCtx = (Context) initCtx.lookup("java:comp/env");
				session = (Session) envCtx.lookup("mail/JavaMail");
				
				// Create file
				String filename = username+"_"+jobId+"_"+new Date()+".csv";
				File file = new File(filename);
				Writer writer = new BufferedWriter(new FileWriter(file));
				writer.write(output.toString());
				writer.close();

				// Mail message
				MimeMessage msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress("thisarattr@gmail.com"));
				InternetAddress to[] = new InternetAddress[1];
				to[0] = new InternetAddress(userEmail);
				msg.setRecipients(Message.RecipientType.TO, to);
				msg.setSubject("Ad-hoc Sensor data for Job ID:"+jobId);
				msg.setSentDate(new Date());

				// Mail text content
				MimeBodyPart mailMsgTxt = new MimeBodyPart();
				mailMsgTxt.setText(emailText.toString());

				// Mail attachment
				MimeBodyPart mailMsgAttchment = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(filename);
				mailMsgAttchment.setDataHandler(new DataHandler(fds));
				mailMsgAttchment.setFileName(fds.getName());

				// All mail content
				Multipart mailParts = new MimeMultipart();
				mailParts.addBodyPart(mailMsgTxt);
				mailParts.addBodyPart(mailMsgAttchment);
				msg.setContent(mailParts);

				// send mail
				Transport.send(msg);
				isSuccess=true;
			}
			
		} catch (NamingException e) {
			LOG.log(Level.SEVERE, "Error occurred while retrieving data for user:"+emailDataRequestType.getImei()+" and jobId:"+emailDataRequestType.getJobId()+". Original stacktrace: " + e.toString());
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Error occurred while retrieving data for user:"+emailDataRequestType.getImei()+" and jobId:"+emailDataRequestType.getJobId()+". Original stacktrace: " + e.toString());
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Error occurred while retrieving data for user:"+emailDataRequestType.getImei()+" and jobId:"+emailDataRequestType.getJobId()+". Original stacktrace: " + e.toString());
			e.printStackTrace();
		} catch (AddressException e) {
			LOG.log(Level.SEVERE, "Error occurred while retrieving data for user:"+emailDataRequestType.getImei()+" and jobId:"+emailDataRequestType.getJobId()+". Original stacktrace: " + e.toString());
			e.printStackTrace();
		} catch (MessagingException e) {
			LOG.log(Level.SEVERE, "Error occurred while retrieving data for user:"+emailDataRequestType.getImei()+" and jobId:"+emailDataRequestType.getJobId()+". Original stacktrace: " + e.toString());
			e.printStackTrace();
		}
		if(output.toString().isEmpty()){
			//This is empty because error occurred, Otherwise there will be error message at least.
			output.append("Error occurred while retrieving data for user:"+emailDataRequestType.getImei()+" and jobId:"+emailDataRequestType.getJobId());
		}
		
		EmailDataResponseType emailDataResponseType = new EmailDataResponseType();
		emailDataResponseType.setEmailDataResponseType(isSuccess);
		return emailDataResponseType;
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
				isSuccess = true;
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
			String password = subscribeRequestType.getPassword();
			byte[] bytesPassword = password.getBytes("UTF-8");

			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] passwordDigest = md.digest(bytesPassword);
			
			conn = getMySqlConnection();
			prepStmt=conn.prepareStatement(sql);
			prepStmt.setString(1, subscribeRequestType.getUsername());
			prepStmt.setString(2, new String(passwordDigest));
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
		} catch (UnsupportedEncodingException e) {
			LOG.log(Level.SEVERE, "Error occurred while subscribing nwe user:"+subscribeRequestType.getUsername()+". Original stacktrace: " + e.toString());
		} catch (NoSuchAlgorithmException e) {
			LOG.log(Level.SEVERE, "Error occurred while subscribing nwe user:"+subscribeRequestType.getUsername()+". Original stacktrace: " + e.toString());
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
		
		String sql = "SELECT d.id, d.datetime,d.latitude,d.longitude,d.reading, tmp.username " +
				"FROM data d, (SELECT j.id, u.username FROM job j INNER JOIN user u ON j.user_id=u.id WHERE username=? AND j.id=?) tmp " +
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
				output.append(resultSet.getDouble("reading")+DATA_DELEMETER);
				output.append(resultSet.getString("username"));
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
		String errorMsg = null;
		//boolean isSuccess=false;
		
		ViewJobResponseType viewJobResponse = new ViewJobResponseType();
		// id, sensor_name, start_time, expire_time, frequency, time_period, latitude, longitude, loc_range, nodes, description, datetime
		String sql = "SELECT j.id, s.name, UNIX_TIMESTAMP(j.start_time)*1000 start_time, UNIX_TIMESTAMP(j.expire_time)*1000 expire_time, j.frequency, j.time_period, j.latitude, " +
				"j.longitude, j.loc_range, j.nodes, j.description, UNIX_TIMESTAMP(j.datetime)*1000 datetime, j.status " +
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
				/*start_time and expire_time is taken as unix_timestamp from DB, cos otherwise if this becomes 0. And UNIX_TIMESTAMP is from seconds 
				but ordinary timestamp except time in millisecond when converting into date formats. Therefore multiply by 1000.*/
				output.append(resultSet.getLong("start_time") + DATA_DELEMETER);
				output.append(resultSet.getLong("expire_time") + DATA_DELEMETER);
				output.append(resultSet.getInt("frequency") + DATA_DELEMETER);
				output.append(resultSet.getInt("time_period") + DATA_DELEMETER);
				output.append(resultSet.getDouble("latitude") + DATA_DELEMETER);
				output.append(resultSet.getDouble("longitude") + DATA_DELEMETER);
				output.append(resultSet.getDouble("loc_range") + DATA_DELEMETER);
				output.append(resultSet.getInt("nodes") + DATA_DELEMETER);
				output.append(resultSet.getString("description") + DATA_DELEMETER);
				output.append(resultSet.getLong("datetime") + DATA_DELEMETER);
				output.append(resultSet.getLong("status"));
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
			errorMsg = e.getMessage();
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Error occurred while retrieving Job data for user:" + viewJobRequestType.getUsername() + ". Original stacktrace: "
					+ e.toString());
			errorMsg = "SQL error occurred";
		}
		if (errorMsg != null) {
			output = new StringBuffer("Error occurred while retrieving Job data for user:" + viewJobRequestType.getUsername() + ". " + errorMsg);
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

		Connection conn;
		PreparedStatement prepStmt = null;
		ResultSet resultSet;

		PasswordRecoverResponseType responseType = new PasswordRecoverResponseType();
		boolean isAcctFound = false;

		String username = passwordRecoverRequestType.getUsername();
		String email = passwordRecoverRequestType.getEmail();
		String sql = null;

		try {
			conn = getMySqlConnection();

			if (username != null && username.length() > 0 && email != null && email.length() > 0) {
				sql = "SELECT username, fullname, email  FROM user WHERE username=? AND email=?";
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setString(1, username);
				prepStmt.setString(2, email);
			} else if (username != null && username.length() > 0) {
				sql = "SELECT username, fullname, email  FROM user WHERE username=?";
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setString(1, username);
			} else if (email != null && email.length() > 0) {
				sql = "SELECT username, fullname, email  FROM user WHERE email=?";
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setString(1, email);
			}

			if (prepStmt != null) {
				resultSet = prepStmt.executeQuery();
				if (resultSet.getFetchSize() == 1) {
					if (resultSet.first()) {
						String usernameVal = resultSet.getString("username");
						String fullnameVal = resultSet.getString("fullname");
						String emailVal = resultSet.getString("email");
						String password = generatePassword();
						
						//MD5 password digest.
						byte[] bytesPassword = password.getBytes("UTF-8");
						MessageDigest md = MessageDigest.getInstance("MD5");
						byte[] passwordDigest = md.digest(bytesPassword);
						String md5Password = new String(passwordDigest);

						StringBuffer emailTxt = new StringBuffer();
						emailTxt.append("Dear ").append(fullnameVal).append(",\n\n\n");
						emailTxt.append("You have requested to reset your password for the Ad-hoc Sensor Network profile. And your new password is as follows, \n\n");
						emailTxt.append("\tNew Password : '").append(password).append("'\n\n");
						emailTxt.append("You can log into the system using this new password, but please change this password after first time you logged in.\n\n");
						emailTxt.append("Ad-hoc Sensor Network Team.");
						sendEmail(emailTxt.toString(), "Ad-hoc Sensor NW password reset", emailVal);
						
						sql = "UPDATE user set password=? WHERE username=?";
						prepStmt = conn.prepareStatement(sql);
						prepStmt.setString(1, md5Password);
						prepStmt.setString(2, usernameVal);
						prepStmt.executeUpdate();
						isAcctFound = true;

					}
				}
			}

		} catch (NamingException e) {
			LOG.log(Level.SEVERE, "Error occurred while password Reset operation. Original stacktrace: " + e.toString());
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Error occurred while password Reset operation. Original stacktrace: " + e.toString());
		} catch (UnsupportedEncodingException e) {
			LOG.log(Level.SEVERE, "Error occurred while password Reset operation. Original stacktrace: " + e.toString());
		} catch (NoSuchAlgorithmException e) {
			LOG.log(Level.SEVERE, "Error occurred while password Reset operation. Original stacktrace: " + e.toString());
		}

		responseType.setPasswordRecoverResponseType(isAcctFound);
		return responseType;
	}
	
	/**
	 * @return
	 */
	private String generatePassword() {

		Random rng = new Random();
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		int length = 8;

		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		}
		return new String(text);
	}
	
	/**
	 * @param msgTxt
	 * @param subject
	 * @param toAddress
	 */
	private void sendEmail(String msgTxt, String subject, String toAddress) {
		
		try {
			
			Session session = null;
			Context initCtx;
			initCtx = new InitialContext();

			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			session = (Session) envCtx.lookup("mail/JavaMail");

			// Mail message
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("thisarattr@gmail.com"));
			InternetAddress to[] = new InternetAddress[1];
			to[0] = new InternetAddress(toAddress);
			msg.setRecipients(Message.RecipientType.TO, to);
			msg.setSubject(subject);
			msg.setSentDate(new Date());

			// Mail text content
			MimeBodyPart mailMsgTxt = new MimeBodyPart();
			mailMsgTxt.setText(msgTxt);

			// All mail content
			Multipart mailParts = new MimeMultipart();
			mailParts.addBodyPart(mailMsgTxt);
			msg.setContent(mailParts);

			// send mail
			Transport.send(msg);

		} catch (NamingException e) {
			LOG.log(Level.SEVERE, "Error occurred while sending email. Original stacktrace: " + e.toString());
		} catch (AddressException e) {
			LOG.log(Level.SEVERE, "Error occurred while sending email. Original stacktrace: " + e.toString());
		} catch (MessagingException e) {
			LOG.log(Level.SEVERE, "Error occurred while sending email. Original stacktrace: " + e.toString());
		}
	}


	/**
	 * Auto generated method signature
	 * 
	 * @param editUserRequestType
	 */

	public com.ucsc.mcs.sensorservice.EditUserResponseType EditUser(com.ucsc.mcs.sensorservice.EditUserRequestType editUserRequestType) {
		// TODO : fill this with the necessary business logic
		throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#EditUser");
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param editJobRequestType
	 */

	public com.ucsc.mcs.sensorservice.EditJobResponseType EditJob(com.ucsc.mcs.sensorservice.EditJobRequestType editJobRequestType) {
		
		Connection conn;
		PreparedStatement prepStmt;
		
		EditJobResponseType editJobResponse = new EditJobResponseType();

		String sqlJobUpdate = "UPDATE job j, user u SET j.nodes=?,j.latitude=?,j.longitude=?,j.loc_range=?,j.start_time=?,j.expire_time=?,j.frequency=?,j.time_period=?,j.description=? " +
								"WHERE j.user_id=u.id AND j.id=? AND u.username=?";
		boolean isSuccess = false;
		
		try {
			conn = getMySqlConnection();
			
			prepStmt = conn.prepareStatement(sqlJobUpdate);
			prepStmt.setInt(1, editJobRequestType.getNodes());
			prepStmt.setFloat(2, editJobRequestType.getLatitude());
			prepStmt.setFloat(3, editJobRequestType.getLongitude());
			prepStmt.setFloat(4, editJobRequestType.getLocRange());
			//prepStmt.setTimestamp(5, (editJobRequestType.getStarttime()==0)?null:new Timestamp(editJobRequestType.getStarttime())); This is to set null when time is not set.
			prepStmt.setTimestamp(5, new Timestamp(editJobRequestType.getStarttime()));
			prepStmt.setTimestamp(6, new Timestamp(editJobRequestType.getEndtime()));
			prepStmt.setInt(7, editJobRequestType.getFrequency());
			prepStmt.setInt(8, editJobRequestType.getTimePeriod());
			prepStmt.setString(9, editJobRequestType.getDescription());
			prepStmt.setLong(10, editJobRequestType.getJobId());
			prepStmt.setString(11, editJobRequestType.getUsername());
			prepStmt.execute();
			isSuccess=true;
			
		} catch (NamingException e) {
			LOG.log(Level.SEVERE, "Error occurred while Updating new job. Original stacktrace: " + e.toString());
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Error occurred while Updating new job. Original stacktrace: " + e.toString());
		}
		editJobResponse.setEditJobResponseType(isSuccess);
		return editJobResponse;
	}
}
