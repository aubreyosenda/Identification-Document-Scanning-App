package com.android.example.cameraxapp.Model;

import com.google.firebase.Timestamp;

public class UserModel {

    private String phone;
    private String username;
    private Timestamp createdTimestamp;
    private String profileImageUri;
    private String fullName;
    private String email;

    public UserModel() {
    }

    public UserModel(String phone, String username, Timestamp createdTimestamp, String profileImageUri, String fullName, String email) {
        this.phone = phone;
        this.username = username;
        this.createdTimestamp = createdTimestamp;
        this.profileImageUri = profileImageUri;
        this.fullName = fullName;
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        this.profileImageUri = profileImageUri;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}