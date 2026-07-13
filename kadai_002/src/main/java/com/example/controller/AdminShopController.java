package com.example.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Shop;
import com.example.form.ShopEditForm;
import com.example.form.ShopRegisterForm;
import com.example.repository.CategoryRepository;
import com.example.repository.FavoriteRepository;
import com.example.repository.ReviewRepository;
import com.example.repository.ShopRepository;
import com.example.service.ShopService;

@Controller
@RequestMapping("/admin/shops")
public class AdminShopController {
	private final ShopRepository shopRepository;
	private final FavoriteRepository favoriteRepository;
	private final ReviewRepository reviewRepository;
	private final ShopService shopService;
	private final CategoryRepository categoryRepository;
	
	public AdminShopController(ShopRepository shopRepository,
			FavoriteRepository favoriteRepository,
			ReviewRepository reviewRepository,
			ShopService shopService,
			CategoryRepository categoryRepository) {
		this.shopRepository = shopRepository;
		this.favoriteRepository = favoriteRepository;
		this.reviewRepository = reviewRepository;
		this.shopService = shopService;
		this.categoryRepository = categoryRepository;
	}

	@GetMapping
	public String index(
	        @RequestParam(name = "keyword", required = false) String keyword,
	        @RequestParam(name = "category", required = false) String category,
	        @RequestParam(name = "priceRange", required = false) Integer priceRange,
	        /*@PageableDefault(
	                page = 0,
	                size = 10,
	                sort = "id",
	                direction = Direction.ASC)*/
	        Pageable pageable,
	        Model model) {
		Sort sortOrder = Sort.by(Direction.ASC, "id");
		pageable = PageRequest.of(
			    pageable.getPageNumber(),
			    10,
			    sortOrder
			);
		
	    Page<Shop> shopPage;

	    if (keyword != null && !keyword.isEmpty()
	            && category != null && !category.isEmpty()) {

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

	        shopPage = shopRepository.findByCategories_Name(
	                category,
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
	model.addAttribute("priceRange", priceRange);
	

	    return "admin/shops/index";
	}

	//詳細ページ表示
	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model) {
		Shop shop = shopRepository.getReferenceById(id);

		model.addAttribute("shop", shop);

		return "admin/shops/show";
	}

	//登録ページ表示	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("shopRegisterForm", new ShopRegisterForm());
		model.addAttribute("categories", categoryRepository.findAll());
		return "admin/shops/register";
	}

	// 登録処理
	@PostMapping("/create")
	public String create(@ModelAttribute @Validated ShopRegisterForm shopRegisterForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes,Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute(
					"categories",
					categoryRepository.findAll());
			return "admin/shops/register";
		}
		shopService.create(shopRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "店舗を登録しました。");

		return "redirect:/admin/shops";
	}
	
	// 編集ページ
	 @GetMapping("/{id}/edit")
	    public String edit(@PathVariable(name = "id") Integer id, Model model) {
	        Shop shop = shopRepository.getReferenceById(id);
	        String imageName = shop.getImageName();
	        
	        List<Integer> categoryIds = shop.getCategories()
	                .stream()
	                .map(category -> category.getId())
	                .toList();
	        
	        ShopEditForm shopEditForm = new ShopEditForm(shop.getId(), shop.getName(), null, shop.getDescription(), shop.getPhoneNumber(),shop.getEmail(), shop.getPriceRange(), shop.getOpeningTime(), shop.getClosingTime(), shop.getHoliday(), shop.getPostalCode(), shop.getAddress(),categoryIds);
	        
	        model.addAttribute("imageName", imageName);
	        model.addAttribute("shopEditForm", shopEditForm);
	        model.addAttribute("categories", categoryRepository.findAll());
	        
	        return "admin/shops/edit";
	    }    
	 
	 @PostMapping("/{id}/update")
	 public String update( @PathVariable(name = "id") Integer id,@ModelAttribute @Validated ShopEditForm shopEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
	        if (bindingResult.hasErrors()) {
	            return "admin/shops/edit";
	        }
	        
	        shopService.update(shopEditForm);
	        redirectAttributes.addFlashAttribute("successMessage", "店舗情報を編集しました。");
	        
	        return "redirect:/admin/shops";
	    }
	 
	 
	 //削除
	 @PostMapping("/{id}/delete")
	 public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
	        shopRepository.deleteById(id);
            
	        redirectAttributes.addFlashAttribute("successMessage", "店舗を削除しました。");
	        
	        return "redirect:/admin/shops";
	 }
}