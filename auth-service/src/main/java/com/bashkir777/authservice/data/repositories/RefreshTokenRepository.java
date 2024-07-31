package com.bashkir777.authservice.data.repositories;

import com.bashkir777.authservice.data.entities.RefreshToken;
import com.bashkir777.authservice.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> getRefreshTokenByUser(User user);
    void deleteRefreshTokenByUser(User user);
}
