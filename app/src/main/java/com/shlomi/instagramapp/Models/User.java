package com.shlomi.instagramapp.Models;

import com.shlomi.instagramapp.Utils.ModelFirebase;

import java.util.HashMap;

public class User {
    private String id;
    private String profile_image;
    private String userName;
    private String password;
    private String email;

    public User(){
        this.id = "";
        this.email = "";
        this.password="";
        this.userName = "";
        this.profile_image="";
    }

    public User(String userId, String userName, String email, String password, String profile_image_url){
        this.id = userId;
        this.email = email;
        this.password=password;
        this.userName = userName;
        this.profile_image = profile_image_url;
    }

    public HashMap<String,Object> toMap(String userId, String userName, String email,String password,String profile_image_url) {
        User user = new User(userId,userName, email,password,profile_image_url);
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", userId);
        result.put("userName", userName);
        result.put("email", email);
        result.put("password", password);
        result.put("profile_image", profile_image);

        return result;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getUserName() {
        return userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image = profile_image_url;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
