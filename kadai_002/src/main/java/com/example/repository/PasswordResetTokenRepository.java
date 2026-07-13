package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.PasswordResetToken;
import com.example.entity.User;

public interface PasswordResetTokenRepository extends JpaRepository< PasswordResetToken, Integer> {
	PasswordResetToken findByToken(String token);
	    
	PasswordResetToken findByUser(User user);
	    
	    @Modifying
	    @Transactional
	    @Query("DELETE FROM PasswordResetToken vt WHERE vt.user = :user")
	    void deleteByUser(User user);
	}