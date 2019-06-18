package com.shlomi.instagramapp.Models;

import java.util.HashMap;

public class UserAccountSetting {
    private String description;
    private String display_name;
    private long followers;
    private long following;
    private String posts;
    private String profile_photo;
    private String userName;
    private String website;

    public UserAccountSetting(String description, String display_name, long  followers, long following, String posts, String profile_photo, String userName, String website) {
        this.description = description;
        this.display_name = display_name;
        this.followers = followers;
        this.following = following;
        this.posts = posts;
        this.profile_photo = profile_photo;
        this.userName = userName;
        this.website = website;
    }

    public UserAccountSetting() {
        this.description = "";
        this.display_name = "";
        this.followers = 0;
        this.following = 0;
        this.posts = "";
        this.profile_photo = "";
        this.userName = "";
        this.website = "";
    }

    public String getUserName() {
        return userName;
    }

    public long getFollowers() {
        return followers;
    }

    public long getFollowing() {
        return following;
    }

    public String getPosts() {
        return posts;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public String getWebsite() {
        return website;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public void setFollowing(long following) {
        this.following = following;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


    public HashMap<String,Object> toMap(String description, String display_name, long followers, long following, String posts, String profile_photo, String userName, String website) {
        UserAccountSetting settings = new UserAccountSetting(description,display_name,followers,
                following,posts,profile_photo,userName,website);

        HashMap<String, Object> result = new HashMap<>();
        result.put("description",description);
        result.put("display_name",display_name);
        result.put("followers",followers);
        result.put("following",following);
        result.put("posts",posts);
        result.put("profile_photo",profile_photo);
        result.put("userName",userName);
        result.put("website",website);


        return result;
    }

    @Override
    public String toString() {
        return "UserAccountSetting{" +
                "description='" + description + '\'' +
                ", display_name='" + display_name + '\'' +
                ", followers='" + followers + '\'' +
                ", following='" + following + '\'' +
                ", posts='" + posts + '\'' +
                ", profile_photo='" + profile_photo + '\'' +
                ", userName='" + userName + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}

