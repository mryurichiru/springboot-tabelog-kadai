package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Review;
import com.example.entity.Shop;
import com.example.entity.User;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
	//店舗ごとのレビュー一覧
	public Page<Review> findByShopOrderByCreatedAtDesc(
			Shop shop,
			Pageable pageable);
	
	//ユーザーがレビュー済み
	boolean existsByUserAndShop(
			User user,
			Shop shop);

	//詳細画面用最新6件
	List<Review> findTop6ByShopOrderByCreatedAtDesc(Shop shop);
	
	
	//会員削除用
	void deleteByUser(User user);
}
