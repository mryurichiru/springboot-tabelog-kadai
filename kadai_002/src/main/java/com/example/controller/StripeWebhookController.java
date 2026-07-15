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
import com.stripe.model.Subscription;
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
//サブスク契約
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
	    
	 // サブスク解約
	    else if ("customer.subscription.deleted".equals(event.getType())) {

	    	// サブスクリプション情報を取得
	    	Subscription subscription = (Subscription) event.getDataObjectDeserializer()
	    			.getObject()
	    			.orElseThrow();

	    	// サブスクリプションに紐づく「Stripeの顧客ID（cus_xxx）」を取得
	    	String stripeCustomerId = subscription.getCustomer();

	    	// 顧客IDを元に、DBから対象のユーザーを検索
	    	// 💡 userRepositoryに「findByStripeCustomerId」メソッドが定義されている前提です
	    	User user = userRepository.findByStripeCustomerId(stripeCustomerId)
	    			.orElseThrow(() -> new RuntimeException("該当するStripe顧客IDのユーザーが存在しません: " + stripeCustomerId));

	    	// 💡 【更新】解約されたので、無料会員（0）に設定する
	    	user.setMembershipType(User.FREE_MEMBER); 

	    	// 解約されたので、Stripe関連のIDをクリアにする（任意ですが、クリアしておくと親切です）
	    	user.setStripeSubscriptionId(null);

	    	// DBに保存
	    	userRepository.save(user);

	    	System.out.println("Stripe顧客ID: " + stripeCustomerId + " のユーザーのサブスクを解約し、無料会員へダウングレードしました。");
	    }
	    return ResponseEntity.ok("");
	}
	
	
}
