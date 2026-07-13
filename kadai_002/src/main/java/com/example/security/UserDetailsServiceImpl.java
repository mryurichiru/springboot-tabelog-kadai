package com.example.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;    
    
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;        
    }
    
    
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        System.out.println("入力メールアドレス: " + email);

        User user = userRepository.findByEmail(email);
        if (user == null || !user.getEnabled()) {
            throw new UsernameNotFoundException("ユーザーが存在しません");
        }
        
        System.out.println("user = " + user);

        String userRoleName = user.getRole().getName();

        System.out.println("role = " + userRoleName);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userRoleName));

        return new UserDetailsImpl(user, authorities);
    }
}