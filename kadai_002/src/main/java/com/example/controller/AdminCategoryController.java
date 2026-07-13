package com.example.controller;

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

import com.example.entity.Category;
import com.example.form.CategoryCreateForm;
import com.example.form.CategoryEditForm;
import com.example.repository.CategoryRepository;
import com.example.service.CategoryService;
@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {
	private final CategoryRepository categoryRepository;
	private final CategoryService categoryService;
	
	
	public AdminCategoryController(
			CategoryRepository categoryRepository,
			CategoryService categoryService) {
		
		this.categoryRepository = categoryRepository;
		this.categoryService = categoryService;
	}
	
	@GetMapping
	public String index(Model model) {

	    model.addAttribute("categoryList", categoryRepository.findAll());

	    return "admin/categories/index";
	}
	
	@GetMapping("/create")
	public String create(Model model) {

	    model.addAttribute("categoryCreateForm",
	            new CategoryCreateForm());

	    return "admin/categories/create";
	}

	@PostMapping("/create")
	public String store(
	        @ModelAttribute @Validated CategoryCreateForm categoryCreateForm,
	        BindingResult bindingResult,
	        RedirectAttributes redirectAttributes) {

	    if (bindingResult.hasErrors()) {
	        return "admin/categories/create";
	    }

	    categoryService.create(categoryCreateForm);

	    redirectAttributes.addFlashAttribute(
	            "successMessage",
	            "カテゴリを登録しました。");

	    return "redirect:/admin/categories";
	}
//編集
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable Integer id, Model model) {

	    Category category = categoryRepository.getReferenceById(id);

	    CategoryEditForm form =
	            new CategoryEditForm(
	                    category.getId(),
	                    category.getName());

	    model.addAttribute("categoryEditForm", form);

	    return "admin/categories/edit";
	}
	
	//更新処理
		@PostMapping("/{id}/update")
	public String update(
	        @PathVariable Integer id,
	        @ModelAttribute @Validated CategoryEditForm categoryEditForm,
	        BindingResult bindingResult,
	        RedirectAttributes redirectAttributes) {

	    if (bindingResult.hasErrors()) {
	        return "admin/categories/edit";
	    }

	    categoryService.update(categoryEditForm);

	    redirectAttributes.addFlashAttribute(
	            "successMessage",
	            "カテゴリを編集しました。");

	    return "redirect:/admin/categories";
	}
	//削除
		@PostMapping("/{id}/delete")
		public String delete(
		        @PathVariable Integer id,
		        RedirectAttributes redirectAttributes) {

		    categoryRepository.deleteById(id);

		    redirectAttributes.addFlashAttribute(
		            "successMessage",
		            "カテゴリを削除しました。");

		    return "redirect:/admin/categories";
		}
	
}
