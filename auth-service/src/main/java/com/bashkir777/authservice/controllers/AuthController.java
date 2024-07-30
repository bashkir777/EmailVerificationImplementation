package com.bashkir777.authservice.controllers;

import com.bashkir777.authservice.data.dao.UserService;
import com.bashkir777.authservice.dto.OperationInfo;
import com.bashkir777.authservice.dto.RegisterRequest;
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

    @PostMapping("/register")
    private ResponseEntity<OperationInfo> registerUser(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.register(registerRequest));
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<OperationInfo> badCredentials(BadCredentialsException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(OperationInfo.builder().success(false)
                        .message(exception.getMessage()).build());
    }

}
