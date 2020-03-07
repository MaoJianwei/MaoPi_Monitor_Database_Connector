package com.maojianwei.pi;

public class PiData {

    private final String INVALID_TIMESTAMP_SQL = "2015-08-10 08:10:00"; // SING Girls

    public String IP;
    public Integer Count;
    public Integer CPU;
    public Double CPU_Temp;
    public Double GPU_Temp;
    public Double Temperature;
    public String SysTime;
    public String GpsTime;

    public Double GPS_Latitude;
    public Double GPS_Longitude;
    public Integer GPS_Satellite;


    public Double Last_GPS_Latitude;
    public Double Last_GPS_Longitude;
    public Integer Last_GPS_Satellite;
    public String Last_GPS_Seen;

    public Integer Located_Times;
    public String NodeName;


    public PiData() {
        // default for bad-parsed data
        IP = "";
        Count = -1;
        CPU = 0;
        CPU_Temp = 0.0;
        GPU_Temp = 0.0;
        Temperature = 0.0;
        SysTime = INVALID_TIMESTAMP_SQL;
        GpsTime = INVALID_TIMESTAMP_SQL;
        GPS_Latitude = 0.0;
        GPS_Longitude = 0.0;
        GPS_Satellite = 0;
        Last_GPS_Latitude = 0.0;
        Last_GPS_Longitude = 0.0;
        Last_GPS_Satellite = 0;
        Last_GPS_Seen = INVALID_TIMESTAMP_SQL;
        Located_Times = 0;
        NodeName = "";
    }

    @Override
    public String toString() {
        return "PiData{" +
                "IP='" + IP + '\'' +
                ", Count=" + Count +
                ", CPU=" + CPU +
                ", CPU_Temp=" + CPU_Temp +
                ", GPU_Temp=" + GPU_Temp +
                ", Temperature=" + Temperature +
                ", SysTime='" + SysTime + '\'' +
                ", GpsTime='" + GpsTime + '\'' +
                ", GPS_Latitude=" + GPS_Latitude +
                ", GPS_Longitude=" + GPS_Longitude +
                ", GPS_Satellite=" + GPS_Satellite +
                ", Last_GPS_Latitude=" + Last_GPS_Latitude +
                ", Last_GPS_Longitude=" + Last_GPS_Longitude +
                ", Last_GPS_Satellite=" + Last_GPS_Satellite +
                ", Last_GPS_Seen='" + Last_GPS_Seen + '\'' +
                ", Located_Times=" + Located_Times +
                ", NodeName='" + NodeName + '\'' +
                '}';
    }


    // following are getter & setter


    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public Integer getCount() {
        return Count;
    }

    public void setCount(Integer count) {
        Count = count;
    }

    public Integer getCPU() {
        return CPU;
    }

    public void setCPU(Integer CPU) {
        this.CPU = CPU;
    }

    public Double getCPU_Temp() {
        return CPU_Temp;
    }

    public void setCPU_Temp(Double CPU_Temp) {
        this.CPU_Temp = CPU_Temp;
    }

    public Double getGPU_Temp() {
        return GPU_Temp;
    }

    public void setGPU_Temp(Double GPU_Temp) {
        this.GPU_Temp = GPU_Temp;
    }

    public Double getTemperature() {
        return Temperature;
    }

    public void setTemperature(Double temperature) {
        Temperature = temperature;
    }

    public String getSysTime() {
        return SysTime;
    }

    public void setSysTime(String sysTime) {
        SysTime = sysTime;
    }

    public String getGpsTime() {
        return GpsTime;
    }

    public void setGpsTime(String gpsTime) {
        GpsTime = gpsTime;
    }

    public Double getGPS_Latitude() {
        return GPS_Latitude;
    }

    public void setGPS_Latitude(Double GPS_Latitude) {
        this.GPS_Latitude = GPS_Latitude;
    }

    public Double getGPS_Longitude() {
        return GPS_Longitude;
    }

    public void setGPS_Longitude(Double GPS_Longitude) {
        this.GPS_Longitude = GPS_Longitude;
    }

    public Integer getGPS_Satellite() {
        return GPS_Satellite;
    }

    public void setGPS_Satellite(Integer GPS_Satellite) {
        this.GPS_Satellite = GPS_Satellite;
    }

    public Double getLast_GPS_Latitude() {
        return Last_GPS_Latitude;
    }

    public void setLast_GPS_Latitude(Double last_GPS_Latitude) {
        Last_GPS_Latitude = last_GPS_Latitude;
    }

    public Double getLast_GPS_Longitude() {
        return Last_GPS_Longitude;
    }

    public void setLast_GPS_Longitude(Double last_GPS_Longitude) {
        Last_GPS_Longitude = last_GPS_Longitude;
    }

    public Integer getLast_GPS_Satellite() {
        return Last_GPS_Satellite;
    }

    public void setLast_GPS_Satellite(Integer last_GPS_Satellite) {
        Last_GPS_Satellite = last_GPS_Satellite;
    }

    public String getLast_GPS_Seen() {
        return Last_GPS_Seen;
    }

    public void setLast_GPS_Seen(String last_GPS_Seen) {
        Last_GPS_Seen = last_GPS_Seen;
    }

    public Integer getLocated_Times() {
        return Located_Times;
    }

    public void setLocated_Times(Integer located_Times) {
        Located_Times = located_Times;
    }

    public String getNodeName() {
        return NodeName;
    }

    public void setNodeName(String nodeName) {
        NodeName = nodeName;
    }
}
