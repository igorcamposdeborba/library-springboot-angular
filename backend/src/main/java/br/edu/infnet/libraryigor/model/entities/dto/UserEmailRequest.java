package br.edu.infnet.libraryigor.model.entities.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class UserEmailRequest {
    @Email
    @NotNull
    private String email;

    public UserEmailRequest(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
}
