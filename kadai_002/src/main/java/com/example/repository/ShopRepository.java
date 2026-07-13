package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
	
	Page<Shop> findByNameContainingOrAddressContaining(
			String nameKeyword,
			String addressKeyword,
			Pageable pageable);

	Page<Shop> findByCategories_Name(
			String categoryName,
			Pageable pageable);

	@Query("""
		    SELECT DISTINCT s
		    FROM Shop s
		    JOIN s.categories c
		    WHERE c.name = :categoryName
		    AND (
		        s.name LIKE %:keyword%
		        OR s.address LIKE %:keyword%
		    )
		""")
		Page<Shop> findByCategoryAndKeyword(
		        @Param("categoryName") String categoryName,
		        @Param("keyword") String keyword,
		        Pageable pageable);
	
	Page<Shop> findByPriceRangeLessThanEqual(
		    Integer priceRange,
		    Pageable pageable
		);

	List<Shop> findTop4ByOrderByCreatedAtDesc();
}