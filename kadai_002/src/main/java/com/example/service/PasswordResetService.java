package com.example.service;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.entity.PasswordResetToken;
import com.example.entity.User;
import com.example.repository.PasswordResetTokenRepository;
import com.example.repository.UserRepository;

@Service
public class PasswordResetService {

	private final PasswordResetTokenRepository passwordResetTokenRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public PasswordResetService(
			PasswordResetTokenRepository passwordResetTokenRepository,
			UserRepository userRepository,
			PasswordEncoder passwordEncoder) {

		this.passwordResetTokenRepository = passwordResetTokenRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	//トークン作成 
	@Transactional
	public PasswordResetToken create(User user) {

		passwordResetTokenRepository.deleteByUser(user);

		PasswordResetToken token = new PasswordResetToken();

		token.setUser(user);
		token.setToken(UUID.randomUUID().toString());
		token.setExpiresAt(LocalDateTime.now().plusMinutes(30));

		return passwordResetTokenRepository.save(token);
	}

	//トークン取得
	public PasswordResetToken findByToken(String token) {
	    return passwordResetTokenRepository.findByToken(token);
	}

	//有効期限チェック
	public boolean isExpired(PasswordResetToken token) {

		return token.getExpiresAt()
				.isBefore(LocalDateTime.now());
	}

	//パスワード更新
	@Transactional
	public void updatePassword(
			PasswordResetToken token,
			String newPassword) {

		User user = token.getUser();

		user.setPassword(
				passwordEncoder.encode(newPassword));

		userRepository.save(user);

		passwordResetTokenRepository.delete(token);
	}
}