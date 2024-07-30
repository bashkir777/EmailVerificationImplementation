package com.bashkir777.authservice.data.dao;

import com.bashkir777.authservice.data.entities.User;
import com.bashkir777.authservice.data.repositories.UserRepository;
import com.bashkir777.authservice.dto.OperationInfo;
import com.bashkir777.authservice.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByEmail(String email) throws BadCredentialsException {
        return userRepository.getUserByEmail(email).orElseThrow(
                () -> new BadCredentialsException("The user with this email was not found"));
    }

    public OperationInfo register(RegisterRequest registerRequest) throws BadCredentialsException {

        User user = User.builder().email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .disabled(true).build();

        userRepository.save(user);

        return OperationInfo.builder()
                .success(true)
                .message("User successfully registered")
                .build();
    }
}
