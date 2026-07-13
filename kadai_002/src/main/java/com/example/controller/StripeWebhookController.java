package com.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
@Controller
@RequestMapping("/stripe")
public class StripeWebhookController {
	private final UserRepository userRepository;

	@Value("${stripe.webhook-secret}")
	private String endpointSecret;

	public StripeWebhookController(UserRepository userRepository) {
	    this.userRepository = userRepository;
	}
	
	//CustomerID取得
	@PostMapping("/webhook")
	public ResponseEntity<String> webhook(
	        @RequestBody String payload,
	        @RequestHeader("Stripe-Signature") String sigHeader) {

	 	    Event event;
	    try {
	        event = Webhook.constructEvent(
	                payload,
	                sigHeader,
	                endpointSecret);

	        System.out.println(event.getType());
	        
	    } catch (SignatureVerificationException e) {
	    	 e.printStackTrace();
	        return ResponseEntity.badRequest().body("");
	    }

	    if ("checkout.session.completed".equals(event.getType())) {

	        Session session = (Session) event.getDataObjectDeserializer()
	                .getObject()
	                .orElseThrow();

	        Integer userId =
	                Integer.valueOf(session.getMetadata().get("userId"));

	        User user =
	                userRepository.findById(userId)
	                        .orElseThrow();

	        user.setStripeCustomerId(session.getCustomer());
	        user.setStripeSubscriptionId(session.getSubscription());


	        userRepository.save(user);

	        User savedUser = userRepository.findById(user.getId()).orElseThrow();

	    }

	    return ResponseEntity.ok("");
	}
	
	
}
