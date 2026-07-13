package com.example.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.example.entity.User;
import com.example.form.AdminUserEditForm;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.service.MailService;
import com.example.service.UserService;

@Controller
@RequestMapping("/admin/users")

public class AdminUserController {
	private final UserRepository userRepository;
	private final UserService userService;
	private final RoleRepository roleRepository;
	private final MailService mailService;

	public AdminUserController(UserRepository userRepository, UserService userService, RoleRepository roleRepository, MailService mailService) {
		this.userRepository = userRepository;
		this.userService = userService;
		this.roleRepository = roleRepository;
		this.mailService = mailService;

	}

	/*@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
	    Page<User> userPage;
	    
	    if (keyword != null && !keyword.isEmpty()) {
	        userPage = userRepository.findByNameLikeOrFuriganaLike("%" + keyword + "%", "%" + keyword + "%", pageable);                   
	    } else {
	        userPage = userRepository.findAll(pageable);
	    }        
	    
	    model.addAttribute("userPage", userPage);        
	    model.addAttribute("keyword", keyword);                
	    
	    return "admin/users/index";
	}*/

	@GetMapping
	public String index(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String phoneNumber,
			@RequestParam(required = false) Integer membershipType,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) {

		Page<User> userPage = userRepository.search(
				name,
				email,
				phoneNumber,
				membershipType,
				pageable);

		model.addAttribute("userPage", userPage); // ←これを追加
		model.addAttribute("name", name);
		model.addAttribute("email", email);
		model.addAttribute("phoneNumber", phoneNumber);
		model.addAttribute("membershipType", membershipType);

		return "admin/users/index";
	}

	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model) {
		User user = userRepository.getReferenceById(id);

		model.addAttribute("user", user);

		return "admin/users/show";
	}

	//編集ページ
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable(name = "id") Integer id, Model model) {
		User user = userRepository.getReferenceById(id);

		AdminUserEditForm adminUserEditForm = new AdminUserEditForm(user.getId(), user.getName(), user.getFurigana(),
				user.getEmail(), user.getPhoneNumber(), user.getDateOfBirth(), user.getOccupation(),
				user.getMembershipType(), user.getRole().getId(), user.getEnabled());

		model.addAttribute("adminUserEditForm", adminUserEditForm);
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("user", user);

		return "admin/users/edit";
	}

	@PostMapping("/{id}/update")
	public String update(@PathVariable(name = "id") Integer id,
			@ModelAttribute @Validated AdminUserEditForm adminUserEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "admin/users/edit";
		}

		userService.update(adminUserEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");

		return "redirect:/admin/users";
	}

	//削除
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		User user = userRepository.getReferenceById(id);
		userService.deleteUser(user);

		redirectAttributes.addFlashAttribute("successMessage", "会員を削除しました。");

		return "redirect:/admin/users";
	}
	
	//仮登録メール再送
	@PostMapping("/{id}/resend")
	public String resend(
	        @PathVariable Integer id,
	        HttpServletRequest request,
	        RedirectAttributes redirectAttributes) {

		 String requestUrl =
		            request.getRequestURL().toString()
		                   .replace(request.getRequestURI(), "");

		    userService.resendVerificationMail(id, requestUrl);

		    redirectAttributes.addFlashAttribute(
		            "successMessage",
		            "仮登録メールを再送しました。");

		    return "redirect:/admin/users/" + id;
		}
	
	//csv出力
	@GetMapping("/csv")
	public ResponseEntity<byte[]> exportCsv(
	        @RequestParam(required = false) String name,
	        @RequestParam(required = false) String email,
	        @RequestParam(required = false) String phoneNumber,
	        @RequestParam(required = false) Integer membershipType) {

	    List<User> users = userRepository.searchForCsv(
	            name,
	            email,
	            phoneNumber,
	            membershipType);

	    String csv = userService.createCsv(users);

	    byte[] bytes = ("\uFEFF" + csv).getBytes(StandardCharsets.UTF_8);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("text/csv"));
	    headers.setContentDisposition(
	            ContentDisposition.attachment()
	                    .filename("users.csv")
	                    .build());

	    return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
	}
	
	//集計機能
	@GetMapping("/statistics")
	public String statistics(Model model) {

	    model.addAttribute("totalUsers", userRepository.count());
	    model.addAttribute("freeUsers", userRepository.countByMembershipType(0));
	    model.addAttribute("paidUsers", userRepository.countByMembershipType(1));
	    model.addAttribute("enabledUsers", userRepository.countByEnabled(true));
	    model.addAttribute("disabledUsers", userRepository.countByEnabled(false));

	    return "admin/users/statistics";
	}
}