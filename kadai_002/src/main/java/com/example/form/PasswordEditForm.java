package com.example.form;

import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class PasswordEditForm {
	
	@NotBlank
	private String currentPassword;
	
	@NotBlank
	@Length(min=8)
	private String newPassword;
	
	@NotBlank
	private String passwordConfirmation;

}
