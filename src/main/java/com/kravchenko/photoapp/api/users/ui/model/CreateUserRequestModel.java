package com.kravchenko.photoapp.api.users.ui.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserRequestModel {

    @NotBlank(message = "First name can't be null")
    @Size(min = 2, message = "First name can't be less than 2 symbols")
    private String firstName;
    @NotBlank(message = "Last name can't be null")
    @Size(min = 2, message = "Last name can't be less than 2 symbols")
    private String lastName;
    @NotBlank(message = "email can't be null")
    @Email
    private String email;
    @NotBlank(message = "password can't be null")
    @Size(min = 8, max = 16, message = "password must be 8-16 symbols")
    private String password;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
