package com.example.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.service.MailService;

@Component
public class SignupEventListener {
	private final MailService mailService;

	public SignupEventListener(MailService mailService) {
		this.mailService = mailService;
	}

	@EventListener
	private void onSignupEvent(SignupEvent signupEvent) {

		mailService.sendVerificationMail(
				signupEvent.getUser(),
				signupEvent.getRequestUrl());
	}
}