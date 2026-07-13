package com.example.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordResetForm {

    private String token;

    @NotBlank(message = "新しいパスワードを入力してください。")
    @Size(min = 8, message = "パスワードは8文字以上で入力してください。")
    private String newPassword;

    @NotBlank(message = "確認用パスワードを入力してください。")
    private String passwordConfirmation;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}