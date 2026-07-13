package com.example.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PasswordResetRequestForm {

    @NotBlank(message = "メールアドレスを入力してください。")
    @Email(message = "メールアドレスの形式で入力してください。")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}