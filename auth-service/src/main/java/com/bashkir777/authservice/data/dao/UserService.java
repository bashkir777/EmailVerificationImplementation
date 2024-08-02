package com.bashkir777.authservice.data.dao;

import com.bashkir777.authservice.data.entities.User;
import com.bashkir777.authservice.data.repositories.UserRepository;
import com.bashkir777.authservice.dto.OperationInfo;
import com.bashkir777.authservice.dto.RegisterRequest;
import com.bashkir777.authservice.services.OTPService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private  OTPService otpService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public void setOTPService(@Lazy OTPService otpService){
        this.otpService = otpService;
    }

    public User getUserByEmail(String email) throws BadCredentialsException {
        return userRepository.getUserByEmail(email).orElseThrow(
                () -> new BadCredentialsException("The user with this email was not found"));
    }

    public OperationInfo register(RegisterRequest registerRequest) throws BadCredentialsException {

        User user = User.builder().email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .disabled(true).build();

        userRepository.save(user);

        return OperationInfo.builder()
                .success(true)
                .message("User successfully registered")
                .build();
    }

    @Transactional
    public void deleteUserByEmail(String email){
        Optional<User> optionalUser = userRepository.getUserByEmail(email);
        if(optionalUser.isPresent()){
            otpService.deleteOtpTokenByUser(optionalUser.get());
            userRepository.deleteUserByEmail(email);
        }
    }
}
