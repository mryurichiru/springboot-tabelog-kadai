package com.example.form;

import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ShopRegisterForm {
	@NotBlank(message = "店舗名を入力してください。")
	private String name;

	private MultipartFile imageFile;

	@NotBlank(message = "説明を入力してください。")
	private String description;

	@NotBlank(message = "電話番号を入力してください")
	private String phoneNumber;

	@NotBlank(message = "メールアドレスを入力してください")
	@Email
	private String email;

	@NotNull(message = "予算を選択してください。")
	private Integer priceRange;
	
	@NotNull(message = "開店時間を入力してください")
	private LocalTime openingTime;

	@NotNull(message = "閉店時間を入力してください")
	private LocalTime closingTime;

	@NotBlank(message = "定休日を入力してください")
	private String holiday;

	@NotBlank(message = "郵便番号を入力してください")
	private String postalCode;

	@NotBlank(message = "住所を入力してください")
	private String address;
	
	private List<Integer> categoryIds;

}
