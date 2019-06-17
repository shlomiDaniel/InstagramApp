package com.shlomi.instagramapp.Utils;

public class StringManipulation {
    public static String expandUserName(String userName){
        return userName.replace("."," ");
    }
    public static String  condenseUserName(String userName){
        return userName.replace(" ",".");
    }



}
