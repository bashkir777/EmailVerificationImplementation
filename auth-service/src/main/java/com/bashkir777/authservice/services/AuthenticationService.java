package com.bashkir777.authservice.services;

import com.bashkir777.authservice.data.dao.UserService;
import com.bashkir777.authservice.data.entities.OTPToken;
import com.bashkir777.authservice.data.entities.User;
import com.bashkir777.authservice.dto.*;
import com.bashkir777.authservice.services.enums.Role;
import com.bashkir777.authservice.services.exceptions.OTPExpired;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final OTPService otpService;
    private final ConfirmationProducer confirmationProducer;
    private final JwtService jwtService;

    public OperationInfo register(RegisterRequest registerRequest)
            throws JsonProcessingException, BadCredentialsException {
        OperationInfo answer = userService.register(registerRequest);

        String otp = otpService.generateOtp();

        otpService.saveOtpToken(registerRequest.getEmail(), otp);

        confirmationProducer.produceMessage(
                ConfirmationMessage.builder()
                        .email(registerRequest.getEmail())
                        .firstname(registerRequest.getFirstname())
                        .otp(otp)
                        .build()
        );
        return answer;
    }

    public TokenPair verifyOtp(VerificationRequest verificationRequest, Role role)
            throws OTPExpired, BadCredentialsException{

        OTPToken otpInDB = otpService.getOtpTokenByUser(
                userService.getUserByEmail(
                        verificationRequest.getEmail()
                )
        );

        if((new Date()).after(otpInDB.getExpirationDate())){
            throw new OTPExpired();
        }

        if(!otpInDB.getOtp().equals(verificationRequest.getOtp())){
            throw new BadCredentialsException("Invalid OTP");
        }

        return jwtService.createTokenPair(verificationRequest.getEmail(), role);
    }

    public OperationInfo login(LoginRequest loginRequest)
            throws BadCredentialsException, JsonProcessingException {
        User user = userService.getUserByEmail(
                loginRequest.getEmail()
        );

        if(!user.getPassword().equals(loginRequest.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }
        String otp = otpService.generateOtp();

        otpService.saveOtpToken(loginRequest.getEmail(), otp);

        confirmationProducer.produceMessage(
                ConfirmationMessage.builder()
                        .email(user.getEmail())
                        .firstname(user.getFirstname())
                        .otp(otp)
                        .build()
        );
        return OperationInfo.builder()
                .message("Email containing verification code has been send")
                .success(true).build();
    }
}
