package com.example.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Review;
import com.example.entity.Shop;
import com.example.form.ReviewForm;
import com.example.repository.ReviewRepository;
import com.example.repository.ShopRepository;
import com.example.security.UserDetailsImpl;
import com.example.service.ReviewService;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

	private final ReviewRepository reviewRepository;
	private final ShopRepository shopRepository;
	private final ReviewService reviewService;

	
	public ReviewController(
			ReviewRepository reviewRepository,
			ShopRepository shopRepository,
			ReviewService reviewService) {
	this.reviewRepository = reviewRepository;
	this.shopRepository = shopRepository;
	this.reviewService = reviewService;
}
	
	//レビュー投稿
	@GetMapping("/{shopId}/create")
	public String create(
			@PathVariable Integer shopId,
			Model model,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		Shop shop = shopRepository.getReferenceById(shopId);
		ReviewForm form = new ReviewForm();
		
		if (reviewRepository.existsByUserAndShop(
		        userDetails.getUser(),
		        shop)) {

		    return "redirect:/shops/" + shopId;
		}
		
		form.setRating(5);
		
		model.addAttribute("shop",shop);
		model.addAttribute("reviewForm",form);
		
		return "reviews/create";
		
		
	}

	
	
	@PostMapping("/{shopId}/create")
	public String create(
			@PathVariable Integer shopId, Model model,
			@ModelAttribute @Validated ReviewForm form,
			BindingResult bindingResult,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		if (bindingResult.hasErrors()) {
			Shop shop = shopRepository.getReferenceById(shopId);
			model.addAttribute("shop",shop);
			
			return "reviews/create";
		}
		
		Shop shop = shopRepository.getReferenceById(shopId);
		if (reviewRepository.existsByUserAndShop(
		        userDetails.getUser(),
		        shop)) {

		    return "redirect:/shops/" + shopId;
		}
		reviewService.create(
		        shopId,
		        userDetails.getUser(),
		        form);
		
		return "redirect:/shops/" + shopId;
	}
			
	//レビュー編集
	@GetMapping("/{reviewId}/edit")
	public String edit(
			@PathVariable Integer reviewId,
			Model model,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {

		Review review = reviewRepository.getReferenceById(reviewId);

		ReviewForm form = new ReviewForm();
		
		if (!review.getUser().getId().equals(userDetails.getUser().getId())) {
		    throw new AccessDeniedException("不正アクセス");
		}

		form.setId(review.getId());
		form.setRating(review.getRating());
		form.setComment(review.getComment());

		model.addAttribute("shop", review.getShop());
		model.addAttribute("reviewForm", form);

		return "reviews/edit";
	}

	@PostMapping("/update")
	public String update(
			@ModelAttribute @Validated ReviewForm form,
			BindingResult bindingResult,
			Model model,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {

		if (bindingResult.hasErrors()) {
			 Review review = reviewRepository.getReferenceById(form.getId());

			    model.addAttribute("shop", review.getShop());
			    
			return "reviews/edit";
		}

		Review review = reviewRepository.getReferenceById(form.getId());
		
		if (!review.getUser().getId().equals(userDetails.getUser().getId())) {
		    throw new AccessDeniedException("不正アクセス");
		}

		reviewService.update(form);

		return "redirect:/shops/" +
				review.getShop().getId();
	}

	//レビュー削除
	@PostMapping("/{reviewId}/delete")
	public String delete(@PathVariable Integer reviewId,
			RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		Review review = reviewRepository.getReferenceById(reviewId);
		
		if (!review.getUser().getId().equals(userDetails.getUser().getId())) {
	        throw new AccessDeniedException("不正アクセス");
	    }
		
		Integer shopId = review.getShop().getId();

		reviewService.delete(reviewId);
		redirectAttributes.addFlashAttribute(
				"successMessage", "レビューを削除しました。");

		return "redirect:/shops/" + shopId;
	}
}