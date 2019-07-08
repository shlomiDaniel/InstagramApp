package com.shlomi.instagramapp.Utils;

public class StringManipulation {
    public static String expandUserName(String userName){
        return userName.replace("."," ");
    }
    public static String  condenseUserName(String userName){
        return userName.replace(" ",".");
    }
    public static String getTags(String str){
      if(str.indexOf("#")>0){
          StringBuilder sb = new StringBuilder();
          char [] charArray = str.toCharArray();
          boolean found = false;
          for(char c : charArray){
              if(c=='#'){
                  found = true;
                  sb.append(c);
              }else{
                  if(found==true){
                      sb.append(c);
                  }
              }
              if(c==' '){
                  found = false;
              }
          }
          String s = sb.toString().replace(" ", "").replace("#",",#");
          return  s.substring(1,s.length());

      }
        return str;

    }


}
