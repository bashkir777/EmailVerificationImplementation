package com.bashkir777.authservice.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bashkir777.authservice.dto.*;
import com.bashkir777.authservice.services.AuthenticationService;
import com.bashkir777.authservice.services.enums.Role;
import com.bashkir777.authservice.services.exceptions.OTPExpired;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
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
    public ResponseEntity<OperationInfo> registerUser(@Valid @RequestBody RegisterRequest registerRequest)
            throws BadCredentialsException, JsonProcessingException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<OperationInfo> loginRequest(@Valid @RequestBody LoginRequest loginRequest)
            throws BadCredentialsException, JsonProcessingException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authenticationService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessToken> registerUser(@RequestBody RefreshToken refreshToken)
            throws JWTVerificationException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authenticationService.refresh(refreshToken));
    }

    @ExceptionHandler({JWTVerificationException.class, BadCredentialsException.class, OTPExpired.class, JsonProcessingException.class})
    private ResponseEntity<OperationInfo> badCredentials(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(OperationInfo.builder().success(false)
                        .message(exception.getMessage()).build());
    }

}
