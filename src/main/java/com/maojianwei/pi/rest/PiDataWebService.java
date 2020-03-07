package com.maojianwei.pi.rest;

import com.maojianwei.pi.PiData;

import java.util.Map;

public interface PiDataWebService {

    int addPiData(PiData piData);
    Map<String, String> getStatus();
}
