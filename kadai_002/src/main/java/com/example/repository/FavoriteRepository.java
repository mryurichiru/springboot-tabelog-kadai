package com.example.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Favorite;
import com.example.entity.Shop;
import com.example.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite,Integer>{
	public Page<Favorite> findByUserOrderByCreatedAtDesc(User user,Pageable pageable);
		
		Favorite findByUserAndShop(User user, Shop shop);
		boolean existsByUserAndShop(User user, Shop shop);
		
		//会員削除用
		void deleteByUser(User user);
}
