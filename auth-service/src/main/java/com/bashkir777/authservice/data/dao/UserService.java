package com.bashkir777.authservice.data.dao;

import com.bashkir777.authservice.data.entities.User;
import com.bashkir777.authservice.data.repositories.UserRepository;
import com.bashkir777.authservice.dto.OperationInfo;
import com.bashkir777.authservice.dto.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByEmail(String email) throws BadCredentialsException {
        return userRepository.getUserByEmail(email).orElseThrow(
                () -> new BadCredentialsException("The user with this email was not found"));
    }

    @Transactional
    public OperationInfo register(RegisterRequest registerRequest) throws BadCredentialsException {

        Optional<User> optionalUser = userRepository.getUserByEmail(registerRequest.getEmail());
        if(optionalUser.isPresent()){
            User userInDb = optionalUser.get();
            if(!userInDb.getDisabled()){
                throw new BadCredentialsException("User with this email already exists");
            }else{
                userRepository.delete(userInDb);
                userRepository.flush();
            }
        }

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
