package com.bashkir777.authservice.controllers;

import com.bashkir777.authservice.data.dao.UserService;
import com.bashkir777.authservice.dto.ConfirmationMessage;
import com.bashkir777.authservice.dto.OperationInfo;
import com.bashkir777.authservice.dto.RegisterRequest;
import com.bashkir777.authservice.services.ConfirmationProducer;
import com.bashkir777.authservice.services.OTPService;
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

    private final UserService userService;
    private final ConfirmationProducer confirmationProducer;
    private final OTPService otpService;

    @PostMapping("/register")
    private ResponseEntity<OperationInfo> registerUser(@Valid @RequestBody RegisterRequest registerRequest)
            throws BadCredentialsException, JsonProcessingException {

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

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(answer);
    }

    @ExceptionHandler({BadCredentialsException.class, JsonProcessingException.class})
    private ResponseEntity<OperationInfo> badCredentials(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(OperationInfo.builder().success(false)
                        .message(exception.getMessage()).build());
    }

}
