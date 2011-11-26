CREATE EVENT myevent
    ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 HOUR
    DO
      UPDATE myschema.mytable SET mycol = mycol + 1;
      
      
      
      
CREATE EVENT myevent
    ON SCHEDULE EVERY 1 minute ENABLE
    DO
      UPDATE sensor_nw.data d SET d.reading = d.reading + 1 WHERE d.id=537;
      
      
      
mysql -h localhost -u root -p
show processlist;
SET GLOBAL event_scheduler=ON;


delimiter |

CREATE EVENT e
    ON SCHEDULE
      EVERY 5 SECOND
    DO
      BEGIN
        DECLARE v INTEGER;
        DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN END;

        SET v = 0;

        WHILE v < 5 DO
          INSERT INTO t1 VALUES (0);
          UPDATE t2 SET s1 = s1 + 1;
          SET v = v + 1;
        END WHILE;
    END |

delimiter ;

========================================================================
-- Job Status --
-- 1. runnung
-- 2. completed
-- 3. unsuccessful but expired

-- Scheduled time should be the data upload timeout defined at recorder service.

CREATE EVENT myevent1
    ON SCHEDULE EVERY 15 minute ENABLE
    DO
      BEGIN
			DECLARE done INT DEFAULT FALSE;
			DECLARE expiretime TIMESTAMP;
			DECLARE nodes,nodecount,jobid,freq,timeperiod,totrec,datarec,datanodes INT;
			DECLARE curjob CURSOR FOR SELECT id,expire_time,time_period,frequency,nodes,node_count,(nodes*time_period*60/frequency) rec FROM sensor_nw.job WHERE status=1;
			DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

			OPEN curjob;
			
			job_loop: LOOP
			
				IF done THEN
					LEAVE job_loop;
				END IF;
				
				FETCH curjob INTO jobid,expiretime,timeperiod,freq,nodes,nodecount,totrec;
				
				DECLARE curnumdata CURSOR FOR SELECT count(*) datarec FROM sensor_nw.data WHERE job_id=jobid;
				DECLARE curnodedata CURSOR FOR SELECT count(distinct imei) FROM sensor_nw.data WHERE job_id=jobid;
				OPEN curnumdata;
				OPEN curnodedata;
				FETCH curnumdata INTO datarec;
				FETCH curnodedata INTO datanodes;
				
				IF totrec <= datarec AND nodes <= datanodes THEN
					UPDATE sensor_nw.job SET status=2 WHERE id=jobid;
				ELSE
					IF UNIX_TIMESTAMP(expiretime)!=0 AND expiretime < now() THEN
						UPDATE sensor_nw.job SET status=3 WHERE id=jobid;
					ELSE
						IF nodes <= nodecount THEN
							UPDATE sensor_nw.job SET status=1,node_count=nodes-1 WHERE id=jobid;
						END IF;
					END IF;
				END IF;
				
				CLOSE curnumdata;
				CLOSE curnodedata;
			END LOOP;
			CLOSE curjob;
		END;
      
