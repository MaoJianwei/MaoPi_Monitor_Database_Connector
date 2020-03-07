package com.maojianwei.pi.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PiAddResult {
    public static final int ADD_OK = 0;
    public static final int ADD_FAIL_QUEUE = -1;
    public static final int ADD_FAIL_QUEUE_NO_SAPCE_TIMEOUT = -2;

    private int errCode;

    // for deserializer
    protected PiAddResult() {
        errCode = ADD_OK;
    }

    // for serializer, and for App usage
    public PiAddResult(int errCode) {
        this.errCode = errCode;
    }

    // for deserializer
    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    // for serializer, and for App usage
    public int getErrCode() {
        return errCode;
    }
}
