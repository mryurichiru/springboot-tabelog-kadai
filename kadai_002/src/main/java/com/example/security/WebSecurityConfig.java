package com.example.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

	private final LoginSuccessHandler loginSuccessHandler;

	public WebSecurityConfig(LoginSuccessHandler loginSuccessHandler) {
	    this.loginSuccessHandler = loginSuccessHandler;
	}
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/stripe/webhook")
        )
        .authorizeHttpRequests((requests) -> requests                
            		.requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**","/shops", "/shops/{id}", "/password/**", "/company", "/terms", "/stripe/webhook" ).permitAll()  // すべてのユーザーにアクセスを許可するURL
            		.requestMatchers("/admin/**").hasRole("ADMIN")  // 管理者にのみアクセスを許可するURL
            		.requestMatchers("/reviews/**", "/favorites", "/favorites/**", "/reservations", "/reservations/**").authenticated()
            		.anyRequest().authenticated()                   // 上記以外のURLはログインが必要（会員または管理者のどちらでもOK）
            )
            .formLogin((form) -> form
                .loginPage("/login")              // ログインページのURL
                .loginProcessingUrl("/login")     // ログインフォームの送信先URL
                .successHandler(loginSuccessHandler)  // ログイン成功時のリダイレクト先URL
                .failureUrl("/login?error")       // ログイン失敗時のリダイレクト先URL
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/?loggedOut")  // ログアウト時のリダイレクト先URL
                .permitAll()
            );            
            
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner runner(PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println(passwordEncoder.encode("password"));
        };
    }
}