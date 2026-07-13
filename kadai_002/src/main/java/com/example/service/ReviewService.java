package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Review;
import com.example.entity.Shop;
import com.example.entity.User;
import com.example.form.ReviewForm;
import com.example.repository.ReviewRepository;
import com.example.repository.ShopRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ShopRepository shopRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            ShopRepository shopRepository) {

        this.reviewRepository = reviewRepository;
        this.shopRepository = shopRepository;
    }

    // 投稿
    @Transactional
    public void create(Integer shopId, User user, ReviewForm form) {

        Shop shop = shopRepository.getReferenceById(shopId);

        if (reviewRepository.existsByUserAndShop(user, shop)) {
            return;
        }

        Review review = new Review();

        review.setShop(shop);
        review.setUser(user);
        review.setRating(form.getRating());
        review.setComment(form.getComment());

        reviewRepository.save(review);
    }

    // 編集
    @Transactional
    public Review update(ReviewForm form) {

        Review review = reviewRepository.getReferenceById(form.getId());

        review.setRating(form.getRating());
        review.setComment(form.getComment());

        return reviewRepository.save(review);
    }

    // 削除
    @Transactional
    public void delete(Integer reviewId) {

        Review review = reviewRepository.getReferenceById(reviewId);

        reviewRepository.delete(review);
    }

}