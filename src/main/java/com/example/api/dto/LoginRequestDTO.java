package com.example.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "Email doit être valide")
    private String mail;

    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    private String password;
}
