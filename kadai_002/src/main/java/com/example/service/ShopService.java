package com.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Category;
import com.example.entity.Shop;
import com.example.form.ShopEditForm;
import com.example.form.ShopRegisterForm;
import com.example.repository.CategoryRepository;
import com.example.repository.ShopRepository;

@Service
public class ShopService {
	private final ShopRepository shopRepository;
	private final CategoryRepository categoryRepository;

	public ShopService(ShopRepository shopRepository, CategoryRepository categoryRepository) {
		this.shopRepository = shopRepository;
		this.categoryRepository = categoryRepository;
	}

	//登録
	@Transactional
	public void create(ShopRegisterForm shopRegisterForm) {
		Shop shop = new Shop();
		
		MultipartFile imageFile = shopRegisterForm.getImageFile();
		 if (!imageFile.isEmpty()) {
		 String imageName = imageFile.getOriginalFilename(); 
		 String hashedImageName = generateNewFileName(imageName);
		 Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
		 copyImageFile(imageFile, filePath);
		 
		 shop.setImageName(hashedImageName);
		}

		shop.setName(shopRegisterForm.getName());
		shop.setDescription(shopRegisterForm.getDescription());
		shop.setPhoneNumber(shopRegisterForm.getPhoneNumber());
		shop.setEmail(shopRegisterForm.getEmail());
		shop.setPriceRange(shopRegisterForm.getPriceRange());

		shop.setOpeningTime(shopRegisterForm.getOpeningTime());
		shop.setClosingTime(shopRegisterForm.getClosingTime());
		shop.setHoliday(shopRegisterForm.getHoliday());

		shop.setPostalCode(shopRegisterForm.getPostalCode());
		shop.setAddress(shopRegisterForm.getAddress());
		
		List<Category> categories =
		        categoryRepository.findAllById(
		                shopRegisterForm.getCategoryIds());

		shop.setCategories(categories);

		shopRepository.save(shop);
	}

	//編集
	@Transactional
	public void update(ShopEditForm shopEditForm) {
		Shop shop = shopRepository.getReferenceById(shopEditForm.getId());
		MultipartFile imageFile = shopEditForm.getImageFile();
		
			if(!imageFile.isEmpty()) {
				String imageName = imageFile.getOriginalFilename();
				String hashedImageName = generateNewFileName(imageName);
		    Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
		    copyImageFile(imageFile, filePath);
		    shop.setImageName(hashedImageName);
		}
		
		shop.setName(shopEditForm.getName());
		shop.setDescription(shopEditForm.getDescription());
		shop.setPhoneNumber(shopEditForm.getPhoneNumber());
		shop.setEmail(shopEditForm.getEmail());
		shop.setPriceRange(shopEditForm.getPriceRange());
		shop.setOpeningTime(shopEditForm.getOpeningTime());
		shop.setClosingTime(shopEditForm.getClosingTime());
		shop.setHoliday(shopEditForm.getHoliday());
		shop.setPostalCode(shopEditForm.getPostalCode());
		shop.setAddress(shopEditForm.getAddress());
		List<Category> categories =
		        categoryRepository.findAllById(
		                shopEditForm.getCategoryIds());

		shop.setCategories(categories);

		shopRepository.save(shop);
	}

	public Shop findById(Integer id) {
		return shopRepository.findById(id).orElseThrow();
	}
	
	private String generateNewFileName(String fileName) {
	    return java.util.UUID.randomUUID().toString() + "_" + fileName;
	}
	
	private void copyImageFile(
	        MultipartFile imageFile,
	        Path filePath) {

	    try {

	        Files.copy(imageFile.getInputStream(), filePath);

	    } catch (IOException e) {

	        throw new RuntimeException(e);
	    }
	}
	
}
