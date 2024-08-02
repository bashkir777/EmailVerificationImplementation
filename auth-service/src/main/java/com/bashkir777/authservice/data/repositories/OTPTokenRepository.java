package com.bashkir777.authservice.data.repositories;

import com.bashkir777.authservice.data.entities.OTPToken;
import com.bashkir777.authservice.data.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPTokenRepository extends JpaRepository<OTPToken, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM OTPToken otp WHERE otp.user = :user")
    void deleteByUser(@Param("user") User user);
    Optional<OTPToken> findByUser(User user);
}
