package com.example.eventbooking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eventbooking.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /** Find by unique email (for login / registration checks). */
    Optional<User> findByEmail(String email);

    /** Check if an email is already taken. */
    boolean existsByEmail(String email);
}