package com.instag.vijay.fasttrending;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 16/9/17.
 */

public class UserModel {

    @SerializedName("username")
    private String username;

    @SerializedName("profileName")
    private String profileName;

    @SerializedName("password")
    private String password;

    @SerializedName("name")
    private String name;

    @SerializedName("gender")
    private String gender;

    @SerializedName("state")
    private String state;

    @SerializedName("country")
    private String country;

    @SerializedName("contact_number")
    private String contact_number;

    @SerializedName("aboutme")
    private String aboutme;

    @SerializedName("email")
    private String email;

    @SerializedName("privacyOn")
    private String privacyOn;

    @SerializedName("profile_image")
    private String serverimage;

    @SerializedName("cover_image")
    private String coverimage;

    @SerializedName("from_token")
    private String from_token;

    @SerializedName("to_token")
    private String to_token;

    public String getFrom_token() {
        return from_token;
    }

    public void setFrom_token(String from_token) {
        this.from_token = from_token;
    }

    public String getTo_token() {
        return to_token;
    }

    public void setTo_token(String to_token) {
        this.to_token = to_token;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getPrivacyOn() {
        return privacyOn;
    }

    public void setPrivacyOn(String privacyOn) {
        this.privacyOn = privacyOn;
    }

    public String getCoverimage() {
        return coverimage;
    }

    public void setCoverimage(String coverimage) {
        this.coverimage = coverimage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getServerimage() {
        return serverimage;
    }

    public void setServerimage(String serverimage) {
        this.serverimage = serverimage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }
}
