	CREATE EVENT sensor_event
		ON SCHEDULE EVERY 60 minute ENABLE
		DO
		  BEGIN
				DECLARE done INT DEFAULT FALSE;
				DECLARE expiretime TIMESTAMP;
				DECLARE nodesvar,nodecountvar,jobid,freq,timeperiod,totrec,datarec,datanodes INT;
				DECLARE temp VARCHAR(500);
				DECLARE curjob CURSOR FOR SELECT id,expire_time,time_period,frequency,nodes,node_count,(nodes*time_period*60/frequency) rec FROM sensor_nw.job WHERE status=1;
				DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

				OPEN curjob;
				
				job_loop: LOOP
				INSERT INTO sensor_nw.log (value) VALUES ('LOOP STARTED');
							
					IF done THEN
						INSERT INTO sensor_nw.log (value) VALUES ('LOOP EXIT, DONE TRUE');
						LEAVE job_loop;
					END IF;
					
					FETCH curjob INTO jobid,expiretime,timeperiod,freq,nodesvar,nodecountvar,totrec;
					INSERT INTO sensor_nw.log (value) VALUES ('LOOP Fetch completed.');
					
					SET datarec = (SELECT count(*) datarec FROM sensor_nw.data WHERE job_id=jobid);
					SET datanodes = (SELECT count(distinct imei) FROM sensor_nw.data WHERE job_id=jobid);

					SET temp = CONCAT('Job values_',jobid,'_',expiretime,'_',timeperiod,'_',freq,'_',nodesvar,'_',nodecountvar,'_',totrec);
					INSERT INTO sensor_nw.log (value) VALUES (temp);
					INSERT INTO sensor_nw.log (value) VALUES (CONCAT('Data count values ',datarec,'_',datanodes));
					
					IF totrec <= datarec AND nodesvar <= datanodes THEN
						UPDATE sensor_nw.job SET status=2 WHERE id=jobid;
						INSERT INTO sensor_nw.log (value) VALUES (CONCAT(jobid,' marked complete.'));
					ELSE
						IF UNIX_TIMESTAMP(expiretime)!=0 AND expiretime < now() THEN
							UPDATE sensor_nw.job SET status=3 WHERE id=jobid;
							INSERT INTO sensor_nw.log (value) VALUES (CONCAT(jobid,' marked unsuccessful.'));
						ELSE
							IF nodesvar <= nodecountvar THEN
								UPDATE sensor_nw.job SET status=1,node_count=nodesvar-1 WHERE id=jobid;
								INSERT INTO sensor_nw.log (value) VALUES (CONCAT(jobid,' still running set node count.'));
							ELSE
								INSERT INTO sensor_nw.log (value) VALUES ('Nothing happend every job is running in order.');
							END IF;
						END IF;
					END IF;
				INSERT INTO sensor_nw.log (value) VALUES (CONCAT(jobid,' end of loop.'));
				END LOOP;
				CLOSE curjob;
				INSERT INTO sensor_nw.log (value) VALUES ('Cursor closed.');
			END;
