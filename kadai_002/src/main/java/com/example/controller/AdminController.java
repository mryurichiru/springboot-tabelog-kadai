package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.repository.ReservationRepository;
import com.example.repository.ReviewRepository;
import com.example.repository.ShopRepository;
import com.example.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private final UserRepository userRepository;
	private final ShopRepository shopRepository;
	private final ReviewRepository reviewRepository;
	private final ReservationRepository reservationRepository;
	
	public AdminController(UserRepository userRepository,ShopRepository shopRepository, ReviewRepository reviewRepository, ReservationRepository reservationRepository) {
		this.userRepository = userRepository;
		this.shopRepository = shopRepository;
		this.reviewRepository = reviewRepository;
		this.reservationRepository = reservationRepository;
	}
	
	 @GetMapping
	 public String index(Model model) {

	     model.addAttribute("totalUsers", userRepository.count());
	     model.addAttribute("paidUsers", userRepository.countByMembershipType(1));
	     model.addAttribute("freeUsers", userRepository.countByMembershipType(0));
	     model.addAttribute("disabledUsers", userRepository.countByEnabled(false));
	     model.addAttribute("userCount", userRepository.count());
	     model.addAttribute("shopCount", shopRepository.count());
	     model.addAttribute("reservationCount", reservationRepository.count());
	     model.addAttribute("reviewCount", reviewRepository.count());

	     return "admin/index";
	 }
}
