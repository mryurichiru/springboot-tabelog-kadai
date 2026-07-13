package com.example.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.Review;
import com.example.entity.Shop;
import com.example.form.ReservationInputForm;
import com.example.repository.CategoryRepository;
import com.example.repository.FavoriteRepository;
import com.example.repository.ReviewRepository;
import com.example.repository.ShopRepository;
import com.example.security.UserDetailsImpl;

@Controller
@RequestMapping("/shops")
public class ShopController {
	private final ShopRepository shopRepository;
	private final FavoriteRepository favoriteRepository;
	private final ReviewRepository reviewRepository;
	private final CategoryRepository categoryRepository;

	public ShopController(ShopRepository shopRepository, FavoriteRepository favoriteRepository,
			ReviewRepository reviewRepository,
			CategoryRepository categoryRepository) {
		this.shopRepository = shopRepository;
		this.favoriteRepository = favoriteRepository;
		this.reviewRepository = reviewRepository;
		this.categoryRepository = categoryRepository;
	}

	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "sort", required = false) String sort,
			@RequestParam(name = "priceRange", required = false) Integer priceRange,
			Pageable pageable,
			Model model) {

		Sort sortOrder = Sort.by(Direction.ASC, "id");

		if ("createdAtDesc".equals(sort)) {
		    sortOrder = Sort.by(Direction.DESC, "createdAt");

		} else if ("nameAsc".equals(sort)) {
		    sortOrder = Sort.by(Direction.ASC, "name");

		} else if ("nameDesc".equals(sort)) {
		    sortOrder = Sort.by(Direction.DESC, "name");
		} else if ("priceAsc".equals(sort)) {
		    sortOrder = Sort.by(Direction.ASC, "priceRange");

		} else if ("priceDesc".equals(sort)) {
		    sortOrder = Sort.by(Direction.DESC, "priceRange");
		}

		pageable = PageRequest.of(
		    pageable.getPageNumber(),
		    10,
		    sortOrder
		);
		
		Page<Shop> shopPage;

		if (keyword != null && !keyword.isEmpty() && category != null && !category.isEmpty()) {

		    shopPage = shopRepository.findByCategoryAndKeyword(
		            category,
		            keyword,
		            pageable);

		} else if (keyword != null && !keyword.isEmpty()) {

		    shopPage = shopRepository.findByNameContainingOrAddressContaining(
		            keyword,
		            keyword,
		            pageable);

		} else if (category != null && !category.isEmpty()) {

		    shopPage = shopRepository.findByCategories_Name(category, pageable);

		} else if (priceRange != null) {

		    shopPage = shopRepository.findByPriceRangeLessThanEqual(
		            priceRange,
		            pageable);

		} else if (priceRange != null) {

		    shopPage = shopRepository.findByPriceRangeLessThanEqual(
		            priceRange,
		            pageable);

		} else {

		    shopPage = shopRepository.findAll(pageable);
		}

		model.addAttribute("shopPage", shopPage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("selectedCategory", category);
		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("sort", sort);
		model.addAttribute("priceRange", priceRange);

		return "shops/index";
	}

	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id,
			Model model,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Shop shop = shopRepository.getReferenceById(id);
		boolean isFavorite = false;
		boolean hasReviewed = false;

		if (userDetails != null) {
			isFavorite = favoriteRepository.existsByUserAndShop(
					userDetails.getUser(),
					shop);
			model.addAttribute("isFavorite", isFavorite);

			hasReviewed = reviewRepository.existsByUserAndShop(
					userDetails.getUser(),
					shop);

			model.addAttribute("loginUser", userDetails.getUser());

		}
		List<Review> reviews = reviewRepository.findTop6ByShopOrderByCreatedAtDesc(shop);

		model.addAttribute("shop", shop);
		model.addAttribute("reviews", reviews);

		model.addAttribute("reservationInputForm", new ReservationInputForm());
		model.addAttribute("isFavorite", isFavorite);
		model.addAttribute("hasReviewed", hasReviewed);

		if (userDetails != null) {
			model.addAttribute("loginUser", userDetails.getUser());
		}

		return "shops/show";
	}

	//レビュー一覧表示
	@GetMapping("/{shopId}/reviews")
	public String index(@PathVariable Integer shopId,
			@PageableDefault(size = 10) Pageable pageable,
			Model model,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Shop shop = shopRepository.getReferenceById(shopId);
		Page<Review> reviewPage = reviewRepository.findByShopOrderByCreatedAtDesc(
				shop,
				pageable);
		if (userDetails != null) {
			model.addAttribute("loginUser", userDetails.getUser());
		}

		model.addAttribute("shop", shop);
		model.addAttribute("reviewPage", reviewPage);

		return "reviews/index";
	}
}
