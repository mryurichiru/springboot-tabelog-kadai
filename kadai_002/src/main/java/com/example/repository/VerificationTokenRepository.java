package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.User;
import com.example.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository< VerificationToken, Integer> {
    VerificationToken findByToken(String token);
    
    VerificationToken findByUser(User user);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM VerificationToken vt WHERE vt.user = :user")
    void deleteByUser(User user);
}