package com.meinab.smsgateway.exceptions;

public class UserNotFound extends RuntimeException{
    public UserNotFound(String message){
        super(message);
    }
}
