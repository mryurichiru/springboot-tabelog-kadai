package com.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.User;
import com.example.form.PasswordEditForm;
import com.example.form.UserEditForm;
import com.example.repository.UserRepository;
import com.example.repository.VerificationTokenRepository;
import com.example.security.UserDetailsImpl;
import com.example.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	private final UserRepository userRepository;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final VerificationTokenRepository verificationTokenRepository;

	public UserController(
			UserRepository userRepository,
			UserService userService,
			PasswordEncoder passwordEncoder,
			VerificationTokenRepository verificationTokenRepository) {

		this.userRepository = userRepository;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.verificationTokenRepository = verificationTokenRepository;
	}

	@GetMapping
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());

		model.addAttribute("user", user);

		return "user/index";
	}

	//会員情報編集
	@GetMapping("/edit")
	public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
		UserEditForm userEditForm = new UserEditForm(user.getId(), user.getName(), user.getFurigana(),
				user.getEmail(), user.getPhoneNumber(), user.getDateOfBirth(), user.getOccupation());

		model.addAttribute("userEditForm", userEditForm);

		return "user/edit";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		// メールアドレスが変更されており、かつ登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
		if (userService.isEmailChanged(userEditForm) && userService.isEmailRegistered(userEditForm.getEmail())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
			bindingResult.addError(fieldError);
		}

		if (bindingResult.hasErrors()) {
			return "user/edit";
		}

		userService.update(userEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");

		return "redirect:/user";
	}

	//パスワード変更
	@GetMapping("/password/edit")
	public String editPassword(Model model) {
		model.addAttribute("passwordEditForm", new PasswordEditForm());
		return "password_edit";
	}

	@PostMapping("/password/update")
	public String updatePassword(
			@ModelAttribute @Validated PasswordEditForm passwordEditForm,
			BindingResult bindingResult,
			Authentication authentication,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "password_edit";
		}

		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();

		User user = userDetailsImpl.getUser();
		//現在のパスワード確認
		if (!passwordEncoder.matches(
				passwordEditForm.getCurrentPassword(),
				user.getPassword())) {

			bindingResult.rejectValue(
					"currentPassword",
					"error.currentPassword",
					"現在のパスワードが違います");
			return "password_edit";
		}
		//確認用が一致しているかのチェック
		if (!passwordEditForm.getNewPassword()
				.equals(passwordEditForm.getPasswordConfirmation())) {

			bindingResult.rejectValue(
					"passwordConfirmation",
					"error.passwordConfirmation",
					"確認用パスワードが一致しません");
			return "password_edit";
		}

		//パスワード変更完了
		user.setPassword(passwordEncoder.encode(
				passwordEditForm.getNewPassword()));
		userRepository.save(user);

		redirectAttributes.addFlashAttribute(
				"successMessage",
				"パスワードを変更しました");
		return "redirect:/user";
	}

	//会員削除
	@GetMapping("/delete")
	public String deleteConfirm() {
		return "user/delete";
	}

	@PostMapping("/delete")
	public String delete(
			Authentication authentication,
			HttpServletRequest request,
			HttpServletResponse response) {

		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();

		User user = userDetailsImpl.getUser();
		
		// token削除
	    verificationTokenRepository.deleteByUser(user);
	    
		//ユーザー削除
		userRepository.delete(user);

		//ログアウト
		new SecurityContextLogoutHandler()
				.logout(request, response, authentication);

		return "redirect:/?withdrawal";

	}
}
