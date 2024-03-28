package com.meinab.smsgateway.exceptions;

public class DeviceNotFound extends RuntimeException{
    public DeviceNotFound(String message){
        super(message);
    }
}
