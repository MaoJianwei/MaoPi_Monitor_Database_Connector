package com.maojianwei.pi.rest;

import com.maojianwei.pi.PiData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

@RestController
@Order(2)
@RequestMapping("/MaoPi")
public class PiDataWebController {

    private final String INVALID_TIMESTAMP_PREFIX = "0000"; // invalid datetime: 0000-00-00 00:00:00
    private final String INVALID_TIMESTAMP_SQL = "2015-08-10 08:10:00"; // SING Girls


    private final Logger logger = LoggerFactory.getLogger(PiDataWebController.class);

    @Autowired
    private PiDataWebService piDataWebService;

    @RequestMapping(value = "/addData/", method = RequestMethod.POST)
    public PiAddResult addData(@RequestBody String piData) {
        piData = piData
                .replaceAll("\\+", " ")
                .replaceAll("%2C", ",")
                .replaceAll("%3B", ";")
                .replaceAll("%3D", "=")
                .replaceAll("%3A", ":");

        return new PiAddResult(piDataWebService.addPiData(parsePiData(piData)));
    }

    @RequestMapping(value = "/status")
    public Map<String, String> getStatus() {
        return piDataWebService.getStatus();
    }

    private PiData parsePiData(String piDataStr) {
        PiData piData = new PiData();
        try {
            String[] datas = piDataStr.split(";");
            for (String data : datas) {
                if (data.length() > 0) {
                    String[] kv = data.split("=");
                    switch (kv[0]) {
                        case "IP":
                            piData.setIP(kv[1]);
                            break;
                        case "Count":
                            piData.setCount(parseInt(kv[1]));
                            break;
                        case "CPU":
                            piData.setCPU(parseInt(kv[1]));
                            break;
                        case "CPU_Temp":
                            piData.setCPU_Temp(parseDouble(kv[1]));
                            break;
                        case "GPU_Temp":
                            piData.setGPU_Temp(parseDouble(kv[1]));
                            break;
                        case "Temperature":
                            piData.setTemperature(parseDouble(kv[1]));
                            break;
                        case "SysTime":
                            piData.setSysTime(parseTime(kv[1]));
                            break;
                        case "GpsTime":
                            piData.setGpsTime(parseTime(kv[1]));
                            break;
                        case "GPS":
                            parseGPS(piData, kv[1]);
                            break;
                        case "Last_GPS":
                            parseLastGPS(piData, kv[1]);
                            break;
                        case "Located_Times":
                            piData.setLocated_Times(parseInt(kv[1]));
                            break;
                        case "NodeName":
                            // disable remote store the privacy
                            piData.setNodeName(kv[1]
                                    .replaceAll("mao", "")
                                    .replaceAll("Mao", "")
                                    .replaceAll("big", "")
                                    .replaceAll("Big", ""));
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
        }

        return piData;
    }

    private String parseTime(String timeData) {
        return !timeData.substring(0, 4).equals(INVALID_TIMESTAMP_PREFIX)
                ? timeData
                : INVALID_TIMESTAMP_SQL;
    }

    private void parseGPS(PiData piData, String gpsData) {
        String[] gps = gpsData.split(",");
        if (gps.length == 3) {
            // disable remote store the location
//            piData.setGPS_Latitude(gps[0].equals("lost") ? 0 : parseDouble(gps[0]));
//            piData.setGPS_Longitude(gps[1].equals("lost") ? 0 : parseDouble(gps[1]));
            piData.setGPS_Satellite(parseInt(gps[2]));
        }
    }

    private void parseLastGPS(PiData piData, String lastGpsData) {
        String[] lastGps = lastGpsData.split(",");
        if (lastGps.length == 4) {
            // disable remote store the location
//            piData.setLast_GPS_Latitude(lastGps[0].equals("lost") ? 0 : parseDouble(lastGps[0]));
//            piData.setLast_GPS_Longitude(lastGps[1].equals("lost") ? 0 : parseDouble(lastGps[1]));
            piData.setLast_GPS_Satellite(parseInt(lastGps[2]));
            piData.setLast_GPS_Seen(parseTime(lastGps[3]));
        }
    }
}
