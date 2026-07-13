package com.example.service;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.entity.VerificationToken;
import com.example.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService {
	private final VerificationTokenRepository verificationTokenRepository;

	public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
		this.verificationTokenRepository = verificationTokenRepository;
	}

	@Transactional
	public void create(User user, String token) {

	    VerificationToken verificationToken =
	            verificationTokenRepository.findByUser(user);

	    if (verificationToken == null) {
	        verificationToken = new VerificationToken();
	        verificationToken.setUser(user);
	    }

	    verificationToken.setToken(token);

	    verificationTokenRepository.save(verificationToken);
	}
	
	// トークンの文字列で検索した結果を返す
	public VerificationToken getVerificationToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}
	
	//仮登録メール再送
	@Transactional
	public VerificationToken recreate(User user) {

	    // 古いトークンを取得
	    VerificationToken verificationToken =
	            verificationTokenRepository.findByUser(user);

	    // 新しいトークンを発行
	    verificationToken.setToken(java.util.UUID.randomUUID().toString());

	    return verificationTokenRepository.save(verificationToken);
	}
}
