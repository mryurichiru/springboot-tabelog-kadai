package com.example.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.entity.Shop;
import com.example.repository.ShopRepository;

@Controller
public class HomeController {
	private final ShopRepository shopRepository;

	public HomeController(ShopRepository shopRepository) {
		this.shopRepository = shopRepository;
	}

	@GetMapping("/")
	public String index(Authentication authentication, Model model) {

	    List<Shop> newShops =
	            shopRepository.findTop4ByOrderByCreatedAtDesc();

	    model.addAttribute("newShops", newShops);

	    return "index";
	}
	
	// 会社概要ページへのルーティング
    @GetMapping("/company")
    public String company() {
        return "company";
    }

    // 利用規約ページへのルーティング
    @GetMapping("/terms")
    public String terms() {
        return "terms";
    }
}