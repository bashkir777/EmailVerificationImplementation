package com.bashkir777.authservice.data.repositories;

import com.bashkir777.authservice.data.entities.OTPToken;
import com.bashkir777.authservice.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPTokenRepository extends JpaRepository<OTPToken, Integer> {
    void deleteOTPTokenByUser(User user);
}
