package com.bashkir777.authservice.data.repositories;

import com.bashkir777.authservice.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> getUserByEmail(String email);
}
