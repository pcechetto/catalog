package com.example.catalog.dto;

import com.example.catalog.services.validation.UserInsertValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@SuppressWarnings("unused")
@UserInsertValid
public class UserInsertDTO extends UserDTO {

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must have 8 characters")
    private String password;

    public UserInsertDTO() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
