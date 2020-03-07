package com.maojianwei.pi.rest;

import com.maojianwei.pi.PiData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static com.maojianwei.pi.rest.PiAddResult.*;


@Service
@Order(1)
public class PiDataWebCore implements PiDataWebService {

    private static final Integer DATAQUEUE_MAX_LENGTH = 200;
    private static final Integer DATAQUEUE_DRAIN_NUMBER = 100;

    private static final String URL = "jdbc:mysql://pi-monitor.maojianwei.com:3333/pidata?serverTimezone=Asia/Shanghai";
    private static final String NAME = "root";
    private static final String PASSWORD = "sing0810";

    private static final Integer DATABASE_CONNECTION_REFRESH_INTERVAL_SECOND = 180; // 3 minute

    private static final String SQL_DB_INIT_CREATE_PIDATA_TABLE = "create table pi " +
            "(IP nvarchar(20) null, " +
            "Count integer null, " +
            "CPU integer null, " +
            "CPU_Temp double null, " +
            "GPU_Temp double null, " +
            "Temperature double null, " +
            "SysTime timestamp null, " +
            "GpsTime timestamp null, " +
            "GPS_Latitude double null, " +
            "GPS_Longitude double null, " +
            "GPS_Satellite integer null, " +
            "Last_GPS_Latitude double null, " +
            "Last_GPS_Longitude double null, " +
            "Last_GPS_Satellite integer null, " +
            "Last_GPS_Seen timestamp null, " +
            "Located_Times integer null, " +
            "NodeName nvarchar(30) null )";
    private static final String SQL_DB_INSERT_PIDATA = "INSERT INTO pi " +
            "(IP, Count, CPU, CPU_Temp, GPU_Temp, Temperature, " +
            "SysTime, GpsTime, " +
            "GPS_Latitude, GPS_Longitude, GPS_Satellite, " +
            "Last_GPS_Latitude, Last_GPS_Longitude, Last_GPS_Satellite, Last_GPS_Seen, " +
            "Located_Times, NodeName) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final Logger logger = LoggerFactory.getLogger(PiDataWebCore.class);


    private volatile Connection conn;
    private LinkedBlockingQueue<PiData> dataQueue = new LinkedBlockingQueue<>();
    private ExecutorService taskPool = Executors.newCachedThreadPool();

    public PiDataWebCore() {
        taskPool.submit(new PiWriteDatabaseTask());
    }

    @Override
    public int addPiData(PiData piData) {
//        logger.info("DataQueue len: {}", dataQueue.size());
        try {
            if (dataQueue.offer(piData, 500, TimeUnit.MILLISECONDS)) {
                return ADD_OK;
            }
        } catch (Exception e) {
            logger.warn("Fail to add data to queue, {}", e.getMessage());
            return ADD_FAIL_QUEUE;
        }
        logger.warn("Fail to add data to queue, timeout.");
        return ADD_FAIL_QUEUE_NO_SAPCE_TIMEOUT;
    }

    @Override
    public Map<String, String> getStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("DataQueueLen", String.valueOf(dataQueue.size()));
        try {
            status.put("DBConnClosed", String.valueOf(conn.isClosed()));
        } catch (Exception e) {
            status.put("DBConnClosed", String.valueOf(false));
        }
        status.put("TaskPoolShutdown", String.valueOf(taskPool.isShutdown()));
        status.put("TaskPoolTerminated", String.valueOf(taskPool.isTerminated()));
        return status;
    }

    private class RefreshDatabaseConnectionTask implements Runnable {
        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(DATABASE_CONNECTION_REFRESH_INTERVAL_SECOND*1000);
                } catch (InterruptedException e) {
                    break;
                }
                initDatabase();
            }
        }
    }

    private class PiWriteDatabaseTask implements Runnable {

        private final Logger logger = LoggerFactory.getLogger(PiWriteDatabaseTask.class);

        @Override
        public void run() {
            if (!initDatabase())
                return;

            taskPool.submit(new RefreshDatabaseConnectionTask());

            while (true) {
                avoidQueueOverflow();
                try {
                    PiData piData = dataQueue.poll(1000, TimeUnit.MILLISECONDS);
                    if (piData != null) {
                        commitToDatabase(piData);
                    }
                } catch (Exception e) {
                    logger.warn("Fail to write database, {}", e.getMessage());
                }
            }
        }
    }

    private boolean initDatabase() {
        while (true) {
            try {
//                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(URL, NAME, PASSWORD);
                break;
            } catch (SQLException e) {
                avoidQueueOverflow();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ee) {
                    return false;
                }
            }
        }

        try {
            PreparedStatement ps = conn.prepareStatement(SQL_DB_INIT_CREATE_PIDATA_TABLE);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            // table existed
            return true;
        }
        return true;
    }

    private boolean commitToDatabase(PiData piData) {
        try {
            PreparedStatement ps = conn.prepareStatement(SQL_DB_INSERT_PIDATA);
            ps.setString(1, piData.getIP());
            ps.setInt(2, piData.getCount());
            ps.setInt(3, piData.getCPU());
            ps.setDouble(4, piData.getCPU_Temp());
            ps.setDouble(5, piData.getGPU_Temp());
            ps.setDouble(6, piData.getTemperature());
            ps.setString(7, piData.getSysTime());
            ps.setString(8, piData.getGpsTime());
            ps.setDouble(9, piData.getGPS_Latitude());
            ps.setDouble(10, piData.getGPS_Longitude());
            ps.setInt(11, piData.getGPS_Satellite());
            ps.setDouble(12, piData.getLast_GPS_Latitude());
            ps.setDouble(13, piData.getLast_GPS_Longitude());
            ps.setInt(14, piData.getLast_GPS_Satellite());
            ps.setString(15, piData.getLast_GPS_Seen());
            ps.setInt(16, piData.getLocated_Times());
            ps.setString(17, piData.getNodeName());

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private void avoidQueueOverflow() {
        if (dataQueue.size() > DATAQUEUE_MAX_LENGTH) {
            for (int i = 0; i < DATAQUEUE_DRAIN_NUMBER; i++) {
                try {
                    dataQueue.poll(100, TimeUnit.MILLISECONDS);
                } catch (Exception e) {}
            }
        }
    }
}
