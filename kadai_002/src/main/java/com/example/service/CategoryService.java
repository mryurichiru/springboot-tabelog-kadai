package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Category;
import com.example.form.CategoryCreateForm;
import com.example.form.CategoryEditForm;
import com.example.repository.CategoryRepository;
import com.example.repository.ShopRepository;

@Service

public class CategoryService {
	private final CategoryRepository categoryRepository;
	private final ShopRepository shopRepository;
	
	
	public CategoryService(CategoryRepository categoryRepository, ShopRepository shopRepository) {
		this.categoryRepository = categoryRepository;
		this.shopRepository = shopRepository;
	}
	
	@Transactional
	public void create(CategoryCreateForm form) {

	    Category category = new Category();

	    category.setName(form.getName());

	    categoryRepository.save(category);
	}
	
	@Transactional
	public void update(CategoryEditForm form) {

		Category category = categoryRepository.findById(form.getId())
		        .orElseThrow(() -> new IllegalArgumentException("カテゴリが存在しません。"));

	    category.setName(form.getName());

	    categoryRepository.save(category);
	}
	
	@Transactional
	public void delete(Integer id) {

	    Category category = categoryRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("カテゴリが存在しません。"));

	    long count = shopRepository.countByCategories_Id(id);

	    if (count > 0) {
	        throw new IllegalStateException(
	                "このカテゴリは" + count + "店舗で使用されているため削除できません。");
	    }

	    categoryRepository.delete(category);
	}
}

