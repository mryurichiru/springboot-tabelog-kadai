package com.example.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.PasswordResetToken;
import com.example.entity.User;
import com.example.form.PasswordResetForm;
import com.example.form.PasswordResetRequestForm;
import com.example.repository.UserRepository;
import com.example.service.MailService;
import com.example.service.PasswordResetService;
import com.example.service.UserService;

@Controller
@RequestMapping("/password/reset")
public class PasswordResetController {

    private final UserRepository userRepository;
    private final PasswordResetService passwordResetService;
    private final MailService mailService;
    private final UserService userService;

    public PasswordResetController(
            UserRepository userRepository,
            PasswordResetService passwordResetService,
            MailService mailService,
            UserService userService) {

        this.userRepository = userRepository;
        this.passwordResetService = passwordResetService;
        this.mailService = mailService;
        this.userService = userService;
    }

    // メールアドレス入力画面
    @GetMapping
    public String index(Model model) {

        model.addAttribute(
                "passwordResetRequestForm",
                new PasswordResetRequestForm());

        return "password/request";
    }

    // パスワード再設定メール送信
    @PostMapping("/send")
    public String send(
            @ModelAttribute @Validated PasswordResetRequestForm form,
            BindingResult bindingResult,
            HttpServletRequest request,
            Model model) {
         if (bindingResult.hasErrors()) {
            return "password/request";
        }

         User user = userRepository.findByEmail(form.getEmail());

      // 登録済みかつ認証済みユーザーのみ送信
      if (user != null && Boolean.TRUE.equals(user.getEnabled())) {

          String requestUrl = request.getScheme()
                  + "://"
                  + request.getServerName()
                  + ":"
                  + request.getServerPort();

          mailService.sendPasswordResetMail(user, requestUrl);
      }

        model.addAttribute(
                "successMessage",
                "パスワード再設定用メールを送信しました。");

        return "password/send";
    }

    // パスワード入力画面
    @GetMapping("/{token}")
    public String edit(
            @PathVariable String token,
            Model model) {

        PasswordResetToken passwordResetToken =
                passwordResetService.findByToken(token);

        if (passwordResetToken == null
                || passwordResetService.isExpired(passwordResetToken)) {

            return "redirect:/login";
        }

        PasswordResetForm form = new PasswordResetForm();
        form.setToken(token);

        model.addAttribute("passwordResetForm", form);

        return "password/edit";
    }

    // パスワード更新
    @PostMapping("/update")
    public String update(
            @ModelAttribute @Validated PasswordResetForm form,
            BindingResult bindingResult) {
    	
    	System.out.println("update() 開始");
    	System.out.println("token = " + form.getToken());
    	System.out.println("newPassword = " + form.getNewPassword());
    	System.out.println("confirmation = " + form.getPasswordConfirmation());


        if (!userService.isSamePassword(
                form.getNewPassword(),
                form.getPasswordConfirmation())) {

            bindingResult.rejectValue(
                    "passwordConfirmation",
                    "error.passwordConfirmation",
                    "パスワードが一致しません。");
        }
        System.out.println("BindingError = " + bindingResult.hasErrors());
        if (bindingResult.hasErrors()) {
            return "password/edit";
        }

        PasswordResetToken token =
                passwordResetService.findByToken(form.getToken());

        System.out.println("tokenEntity = " + token);

        if (token == null
                || passwordResetService.isExpired(token)) {

            return "redirect:/login";
        }

        passwordResetService.updatePassword(
                token,
                form.getNewPassword());
        
        System.out.println("更新完了");

        return "redirect:/login?resetSuccess";
    }
}