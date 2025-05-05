package com.example.catalog.dto;

import com.example.catalog.services.validation.UserInsertValid;

@SuppressWarnings("unused")
@UserInsertValid
public class UserInsertDTO extends UserDTO {

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
