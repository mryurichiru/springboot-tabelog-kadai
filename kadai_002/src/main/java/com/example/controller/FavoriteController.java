package com.example.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.Favorite;
import com.example.entity.Shop;
import com.example.repository.FavoriteRepository;
import com.example.repository.ShopRepository;
import com.example.security.UserDetailsImpl;

@Controller
@RequestMapping("/favorites")

public class FavoriteController {
	private final ShopRepository shopRepository;
	private final FavoriteRepository favoriteRepository;

	public FavoriteController(
			ShopRepository shopRepository, FavoriteRepository favoriteRepository) {
		this.shopRepository = shopRepository;
		this.favoriteRepository = favoriteRepository;
	}

	/*お気に入り登録*/
	@PostMapping("/{shopId}/create")
	public String create(
			@PathVariable Integer shopId,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Shop shop = shopRepository.getReferenceById(shopId);

		if (!favoriteRepository.existsByUserAndShop(
		        userDetails.getUser(),shop)) {
		Favorite favorite = new Favorite();

		favorite.setUser(userDetails.getUser());
		favorite.setShop(shop);

		favoriteRepository.save(favorite);
		}
		return "redirect:/shops/" + shopId;
	}
	
//お気に入り一覧表示
	@GetMapping
	public String favorites(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@PageableDefault(size = 10) Pageable pageable,
			Model model) {

		Page<Favorite> favoritePage =
				favoriteRepository.findByUserOrderByCreatedAtDesc(userDetails.getUser(),pageable);
		
		model.addAttribute("favoritePage", favoritePage);
		model.addAttribute("user", userDetails.getUser());
		
		return "favorites/index";
	}
	
	//お気に入り削除
	@PostMapping("/{shopId}/delete")
	public String delete(
			@PathVariable Integer shopId,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Shop shop = shopRepository.getReferenceById(shopId);

		Favorite favorite = favoriteRepository.findByUserAndShop(userDetails.getUser(), shop);

		if (favorite != null) {favoriteRepository.delete(favorite);
		}
		return "redirect:/shops/" + shopId;
	}
}