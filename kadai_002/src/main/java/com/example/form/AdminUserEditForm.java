package com.example.form;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUserEditForm {

    @NotNull
    private Integer id;

    @NotBlank(message = "氏名を入力してください。")
    private String name;

    @NotBlank(message = "フリガナを入力してください。")
    private String furigana;

    @NotBlank(message = "メールアドレスを入力してください。")
    @Email(message = "メールアドレスの形式が正しくありません。")
    private String email;

    @NotBlank(message = "電話番号を入力してください。")
    private String phoneNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String occupation;

    @NotNull(message = "会員種別を選択してください。")
    private Integer membershipType;

    @NotNull(message = "ロールを選択してください。")
    private Integer roleId;

    @NotNull(message = "有効・無効を選択してください。")
    private Boolean enabled;
}