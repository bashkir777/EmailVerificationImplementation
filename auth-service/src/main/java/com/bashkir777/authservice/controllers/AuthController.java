package com.bashkir777.authservice.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bashkir777.authservice.dto.*;
import com.bashkir777.authservice.services.AuthenticationService;
import com.bashkir777.authservice.services.enums.Role;
import com.bashkir777.authservice.services.exceptions.OTPExpired;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/verify-otp")
    public ResponseEntity<TokenPair> verifyOtp(@Valid @RequestBody VerificationRequest verificationRequest)
            throws OTPExpired {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authenticationService.verifyOtp(verificationRequest, Role.USER));
    }

    @PostMapping("/register")
    public ResponseEntity<OperationInfo> register(@Valid @RequestBody RegisterRequest registerRequest)
            throws BadCredentialsException, JsonProcessingException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<OperationInfo> login(@Valid @RequestBody LoginRequest loginRequest)
            throws BadCredentialsException, JsonProcessingException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authenticationService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessToken> refresh(@RequestBody RefreshTokenDTO refreshTokenDTO)
            throws JWTVerificationException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authenticationService.refresh(refreshTokenDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<OperationInfo> logout(@RequestBody RefreshTokenDTO refreshTokenDTO)
            throws JWTVerificationException, BadCredentialsException {
        authenticationService.logout(refreshTokenDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(OperationInfo.builder().success(true)
                        .message("You have logged out successfully").build());
    }

    @GetMapping("/send-otp/{email}")
    public ResponseEntity<OperationInfo> sendOtpForPasswordReset(@Email @PathVariable String email)
            throws BadCredentialsException, JsonProcessingException {
        authenticationService.sendConfirmationMessage(ConfirmationMessage.builder()
                .email(email)
                .build());
        return ResponseEntity.status(HttpStatus.OK).body(OperationInfo.builder()
                        .message("Confirmation code successfully send")
                        .success(true)
                .build());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<OperationInfo> resetPassword(@Valid @RequestBody ResetPassword resetPassword)
            throws BadCredentialsException, OTPExpired {
        return ResponseEntity.status(HttpStatus.OK).body(
                authenticationService.resetPassword(resetPassword)
        );
    }

    @ExceptionHandler({JWTVerificationException.class, BadCredentialsException.class, OTPExpired.class, JsonProcessingException.class})
    private ResponseEntity<OperationInfo> badCredentials(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(OperationInfo.builder().success(false)
                        .message(exception.getMessage()).build());
    }

}
