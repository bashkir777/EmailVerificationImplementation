package com.bashkir777.authservice.services;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bashkir777.authservice.data.dao.RefreshTokenService;
import com.bashkir777.authservice.data.entities.RefreshToken;
import com.bashkir777.authservice.data.dao.UserService;
import com.bashkir777.authservice.data.entities.OTPToken;
import com.bashkir777.authservice.data.entities.User;
import com.bashkir777.authservice.dto.*;
import com.bashkir777.authservice.services.enums.Role;
import com.bashkir777.authservice.services.enums.TokenType;
import com.bashkir777.authservice.services.exceptions.OTPExpired;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final OTPService otpService;
    private final ConfirmationProducer confirmationProducer;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public void sendConfirmationMessage(ConfirmationMessage confirmationMessage)
            throws JsonProcessingException, BadCredentialsException {
        userService.getUserByEmail(confirmationMessage.getEmail());
        otpService.saveOtpToken(confirmationMessage.getEmail()
                , otpService.generateOtp());
        confirmationProducer.produceMessage(confirmationMessage);
    }


    @Transactional
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

    @Transactional
    private User validateOtpAndGetUser(String email, String otp)
            throws OTPExpired, BadCredentialsException {
        OTPToken otpInDB = otpService.getOtpTokenByUser(
                userService.getUserByEmail(email)
        );

        if ((new Date()).after(otpInDB.getExpirationDate())) {
            throw new OTPExpired();
        }

        if (!otpInDB.getOtp().equals(otp)) {
            throw new BadCredentialsException("Invalid OTP");
        }

        return otpInDB.getUser();
    }

    @Transactional
    public TokenPair verifyOtp(VerificationRequest verificationRequest, Role role)
            throws OTPExpired, BadCredentialsException {

        User user = validateOtpAndGetUser(verificationRequest.getEmail(), verificationRequest.getOtp());

        TokenPair answer = jwtService.createTokenPair(verificationRequest.getEmail(), role);

        refreshTokenService.saveRefreshToken(
                RefreshToken.builder()
                        .refreshToken(answer.getRefreshToken())
                        .user(user)
                        .build()
        );

        return answer;
    }

    @Transactional
    public OperationInfo login(LoginRequest loginRequest)
            throws BadCredentialsException, JsonProcessingException {
        User user = userService.getUserByEmail(
                loginRequest.getEmail()
        );

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
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

    @Transactional
    public AccessToken refresh(RefreshTokenDTO refreshToken)
            throws JWTVerificationException, BadCredentialsException {

        DecodedJWT decodedJWT
                = jwtService.decodeAndValidateToken(refreshToken.getRefreshToken());

        String email = decodedJWT.getSubject();

        refreshTokenService.getRefreshTokenByUser(
                userService.getUserByEmail(email)
        );

        Role role = Role.valueOf(decodedJWT.getClaim("role").asString());

        return AccessToken.builder().accessToken(jwtService
                .createJwt(email, TokenType.ACCESS, role)).build();

    }

    @Transactional
    public void logout(RefreshTokenDTO refreshToken)
            throws JWTVerificationException, BadCredentialsException {
        DecodedJWT decodedJWT
                = jwtService.decodeAndValidateToken(refreshToken.getRefreshToken());

        String email = decodedJWT.getSubject();
        refreshTokenService.deleteRefreshTokenByUser(
            userService.getUserByEmail(email)
        );
    }

    @Transactional
    public OperationInfo resetPassword(ResetPassword resetPassword)
            throws OTPExpired, BadCredentialsException {
        User user = validateOtpAndGetUser(
                resetPassword.getEmail(), resetPassword.getOtp()
        );
        user.setPassword(resetPassword.getNewPassword());
        return OperationInfo.builder()
                .success(true)
                .message("Password has been changed successfully")
                .build();
    }
}
