package com.example.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.security.UserDetailsImpl;
import com.example.service.StripeService;

@Controller
@RequestMapping("/subscription")
public class SubscriptionController {
	private final UserRepository userRepository;
	private final StripeService stripeService;
	
		public SubscriptionController(UserRepository userRepository, StripeService stripeService) {
		this.userRepository = userRepository;
		this.stripeService = stripeService;
	}

	//規約確認画面
	@GetMapping("/create")
	public String create(Authentication authentication, Model model) {

	    UserDetailsImpl userDetailsImpl =
	        (UserDetailsImpl) authentication.getPrincipal();

	    User user = userDetailsImpl.getUser();

	    model.addAttribute("user", user);
	    
		return "subscription/create";
	}
	
	//決済確認画面
	@PostMapping("/checkout")
	public String checkout(
			Authentication authentication,
			HttpServletRequest httpServletRequest) {
		
		UserDetailsImpl userDetailsImpl =
				(UserDetailsImpl) authentication.getPrincipal();
		User user = userDetailsImpl.getUser();
		
		String sessionUrl = stripeService.createStripeSession(user, httpServletRequest);
		
		return "redirect:"+ sessionUrl;
	}

	//決済完了
		@GetMapping("/success")
		public String success(Authentication authentication) {
			UserDetailsImpl userDetailsImpl =
					(UserDetailsImpl) authentication.getPrincipal();
			User user = userDetailsImpl.getUser();
			
			// DBから最新のユーザー情報を取得する
			User latestUser = userRepository.findById(user.getId()).orElseThrow();
			
			// 最新のユーザー情報に対して会員種別を変更
			latestUser.setMembershipType(User.PAID_MEMBER);
			userRepository.save(latestUser);
			
	
			userDetailsImpl.getUser().setMembershipType(User.PAID_MEMBER);
			userDetailsImpl.getUser().setStripeCustomerId(latestUser.getStripeCustomerId());
			userDetailsImpl.getUser().setStripeSubscriptionId(latestUser.getStripeSubscriptionId());
			
			return "redirect:/?paid";
		}
		
//StripePortalSession作成
	@GetMapping("/portal")
	public String portal(Authentication authentication) {

	    UserDetailsImpl userDetails =
	            (UserDetailsImpl) authentication.getPrincipal();

	    User user = userDetails.getUser();

	    String url = stripeService.createCustomerPortal(user);

	    return "redirect:" + url;
	}
	
}