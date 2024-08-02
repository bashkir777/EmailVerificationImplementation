package com.bashkir777.authservice.data.repositories;

import com.bashkir777.authservice.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> getUserByEmail(String email);
    void deleteUserByEmail(String email);
}
