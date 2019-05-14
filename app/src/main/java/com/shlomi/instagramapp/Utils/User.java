package com.shlomi.instagramapp.Utils;

import java.util.HashMap;

public class User {
    private String profile_image_url;
    private ModelFirebase firebase;
    private String userName;
    private String password;
    private String email;


   public User(){
         this.email = "";
         this.password="";
         this.userName = "";
         this.profile_image_url="";

    }
    public User(String userId,String userName,String email,String password,String profile_image_url){
        this.email = email;
        this.password=password;
        this.userName = userName;
        this.profile_image_url = profile_image_url;

    }

    public HashMap<String,Object> toMap(String userId, String userName, String email,String password,String profile_image_url) {
        User user = new User(userId,userName, email,password,profile_image_url);
        HashMap<String, Object> result = new HashMap<>();
        result.put("id",userId);
        result.put("userName",userName);
        result.put("email",email);
        result.put("password",password);
        result.put("profile_image_url",profile_image_url);

        return result;
    }


}
