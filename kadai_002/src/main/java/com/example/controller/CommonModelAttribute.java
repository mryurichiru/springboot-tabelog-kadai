package com.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.security.UserDetailsImpl;

@ControllerAdvice
public class CommonModelAttribute {

    private final UserRepository userRepository;

    public CommonModelAttribute(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute("membershipType")
    public Integer membershipType(Authentication authentication) {

        // 未ログイン
        if (authentication == null
                || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return null;
        }

        // DBから最新のユーザー情報を取得
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

        System.out.println("ControllerAdvice membershipType = " + user.getMembershipType());

        
        return user.getMembershipType();
    }
}