package com.example.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Review;
import com.example.repository.ReviewRepository;
import com.example.service.ReviewService;

@Controller
@RequestMapping("/admin/reviews")
public class AdminReviewController {

	private final ReviewRepository reviewRepository;
	private final ReviewService reviewService;

	public AdminReviewController(ReviewRepository reviewRepository, ReviewService reviewService) {
		this.reviewRepository = reviewRepository;
		this.reviewService = reviewService;
	}

	//一覧表示
	@GetMapping
	public String index(
			@RequestParam(name = "page", defaultValue = "0") int page,
			Model model) {

		Pageable pageable = PageRequest.of(
			    page,
			    10,
			    Sort.by("createdAt").descending()
			);

		Page<Review> reviewPage = reviewRepository.findAll(pageable);

		model.addAttribute("reviewPage", reviewPage);

		return "admin/reviews/index";
	}

	//詳細表示
	@GetMapping("/{id}")
	public String show(
			@PathVariable Integer id,
			Model model) {

		Review review = reviewRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("レビューが存在しません。"));

		model.addAttribute("review", review);

		return "admin/reviews/show";
	}
//削除
	@PostMapping("/{id}/delete")
	public String delete(
	        @PathVariable Integer id,
	        RedirectAttributes redirectAttributes) {

	    reviewService.delete(id);

	    redirectAttributes.addFlashAttribute(
	            "successMessage",
	            "レビューを削除しました。");

	    return "redirect:/admin/reviews";
	}
	

}