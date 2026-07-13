package com.example.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.User;
import com.example.form.AdminProfileEditForm;
import com.example.form.PasswordEditForm;
import com.example.security.UserDetailsImpl;
import com.example.service.UserService;

@Controller
@RequestMapping("/admin/profile")
public class AdminProfileController {
	private final UserService userService;
	
	public AdminProfileController(UserService userService) {
        this.userService = userService;
    }
	@GetMapping
	public String show(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			Model model) {

		model.addAttribute("user", userDetails.getUser());

		return "admin/profile/show";
	}

	@GetMapping("/password/edit")
	public String editPassword(Model model) {

		model.addAttribute("passwordEditForm",
				new PasswordEditForm());

		return "admin/profile/password/edit";
	}
	
	@GetMapping("/edit")
	public String edit(
	        @AuthenticationPrincipal UserDetailsImpl userDetails,
	        Model model) {

	    User user = userDetails.getUser();

	    AdminProfileEditForm form =
	            new AdminProfileEditForm(
	                    user.getId(),
	                    user.getName(),
	                    user.getFurigana(),
	                    user.getEmail(),
	                    user.getPhoneNumber(),
	                    user.getDateOfBirth(),
	                    user.getOccupation());

	    model.addAttribute("adminProfileEditForm", form);

	    return "admin/profile/edit";
	}

	@PostMapping("/update")
	public String update(
	        @ModelAttribute("adminProfileEditForm")
	        @Validated AdminProfileEditForm form,
	        BindingResult bindingResult,
	        RedirectAttributes redirectAttributes) {

	    if (bindingResult.hasErrors()) {
	        return "admin/profile/edit";
	    }

	    userService.updateProfile(form);

	    redirectAttributes.addFlashAttribute(
	            "successMessage",
	            "プロフィールを更新しました。");

	    return "redirect:/admin/profile";
	}
}