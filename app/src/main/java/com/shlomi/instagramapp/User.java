package com.shlomi.instagramapp;

import java.util.HashMap;
import java.util.Map;

public class User {
    private ModelFirebase firebase;
    private String userName;
    private String password;
    private String email;


   public User(){
         this.email = "";
         this.password="";
         this.userName = "";

    }
    public User(String userId,String userName,String email,String password){
        this.email = email;
        this.password=password;
        this.userName = userName;

    }

    public HashMap<String,Object> toMap(String userId, String userName, String email,String password) {
        User user = new User(userId,userName, email,password);
        HashMap<String, Object> result = new HashMap<>();
        result.put("id",userId);
        result.put("userName",userName);
        result.put("email",email);
        result.put("password",password);

        return result;
    }


}
