package com.vanatel.sidar.Model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String companyEmailAddress;

    @NotBlank(message = "Password is required")
    private String companyPassword;

    public LoginRequest() {
    }

    public LoginRequest(String companyEmailAddress, String companyPassword) {
        this.companyEmailAddress = companyEmailAddress;
        this.companyPassword = companyPassword;
    }

    public String getCompanyEmailAddress() {
        return companyEmailAddress;
    }

    public void setCompanyEmailAddress() {
        this.companyEmailAddress = companyEmailAddress;
    }

    public String getCompanyPassword() {
        return companyPassword;
    }

    public void setCompanyPassword() {
        this.companyPassword = companyPassword;
    }
}
