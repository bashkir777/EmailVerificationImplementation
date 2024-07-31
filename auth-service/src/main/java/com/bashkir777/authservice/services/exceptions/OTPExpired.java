package com.bashkir777.authservice.services.exceptions;

public class OTPExpired extends Exception{
    public OTPExpired(){
        super("OTP expired. Please, request a new one");
    }
}
