package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Category;
import com.example.form.CategoryCreateForm;
import com.example.form.CategoryEditForm;
import com.example.repository.CategoryRepository;

@Service

public class CategoryService {
	private final CategoryRepository categoryRepository;
	
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	@Transactional
	public void create(CategoryCreateForm form) {

	    Category category = new Category();

	    category.setName(form.getName());

	    categoryRepository.save(category);
	}
	
	@Transactional
	public void update(CategoryEditForm form) {

	    Category category = categoryRepository.getReferenceById(form.getId());

	    category.setName(form.getName());

	    categoryRepository.save(category);
	}
}
