package com.bashkir777.authservice.services;

import com.bashkir777.authservice.data.dao.UserService;
import com.bashkir777.authservice.data.entities.OTPToken;
import com.bashkir777.authservice.data.entities.User;
import com.bashkir777.authservice.data.repositories.OTPTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class OTPService {

    private final OTPTokenRepository otpTokenRepository;

    private UserService userService;

    private final long OTP_TIME_ALIVE_MILLIS = 2 * 60 * 1000;

    @Autowired
    public OTPService(OTPTokenRepository otpTokenRepository){
        this.otpTokenRepository = otpTokenRepository;
    }

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    private final StringBuilder sb = new StringBuilder();
    private final Random random = new Random();

    public String generateOtp(){
        sb.setLength(0);
        for(int i = 0; i < 6; i++){
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    @Transactional
    public void saveOtpToken(String email, String otp) throws BadCredentialsException {
        User user = userService.getUserByEmail(email);

        Optional<OTPToken> optionalOtp = otpTokenRepository.findByUser(user);
        if(optionalOtp.isPresent()){
            otpTokenRepository.deleteByUser(user);
            otpTokenRepository.flush();
        }

        OTPToken otpToken = OTPToken.builder()
                .user(user)
                .otp(otp)
                .expirationDate(new Date(System.currentTimeMillis() + OTP_TIME_ALIVE_MILLIS))
                .build();

        otpTokenRepository.save(otpToken);
        
    }

    public OTPToken getOtpTokenByUser(User user) throws BadCredentialsException{
        return otpTokenRepository.findByUser(user)
                .orElseThrow(()-> new BadCredentialsException("OTP not found"));
    }

    public void deleteOtpTokenByUser(User user) throws BadCredentialsException{
        otpTokenRepository.deleteByUser(user);
    }
}
