package com.bashkir777.authservice.data.repositories;

import com.bashkir777.authservice.data.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
}
