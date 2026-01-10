package com.notifly.backend.User.Repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notifly.backend.User.Entity.User;


@Repository
public interface UserRepository extends JpaRepository<User,Integer>{
    public Optional<User> findByUsername(String username);

    // Added to check existence before creating a user
    public boolean existsByUsername(String username);

    // If duplicates exist in DB, this returns all matches so callers can handle gracefully
    public List<User> findAllByUsername(String username);

    public boolean existsByEmail(String email);

    public boolean existsByPhoneNumber(String phoneNumber);
} 
