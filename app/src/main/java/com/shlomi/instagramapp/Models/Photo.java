package com.shlomi.instagramapp.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Photo {
    private String caption;
    private String date_created;
    private String image_path;
    private String photo_id;
    private String user_id;
    private String tags;
    private ArrayList<String> likes;
    private String lng;
    private String lat;

    public Photo() { }

    public Photo(String caption, String date_created, String image_path, String photo_id, String user_id, String tags) {
        this.caption = caption;
        this.date_created = date_created;
        this.image_path = image_path;
        this.photo_id = photo_id;
        this.user_id = user_id;
        this.tags = tags;
        this.likes = new ArrayList<>();
    }

    public String getLat () {
        return lat;
    }

    public String getLng () {
        return lng;
    }

    public void setLat ( String lat ) {
        this.lat = lat;
    }

    public void setLng ( String lng ) {
        this.lng = lng;
    }

    public void setLikes( ArrayList<String> likes) {
        this.likes = likes;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setTags(String tags) { this.tags = tags; }

    public String getCaption() {
        return caption;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getTags() {
        return tags;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }
}
