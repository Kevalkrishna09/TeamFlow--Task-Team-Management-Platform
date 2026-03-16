package com.keval.teamflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class RegisterDTO {
    @NonNull
    @NotBlank()
    private String name;

    @Email
    @NonNull
    @NotBlank
    private String email;

    @NonNull
    @NotBlank
    private String password;
}