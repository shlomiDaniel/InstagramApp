package com.shlomi.instagramapp.Cache;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class PhotoEntity {
    @PrimaryKey
    @NonNull
    private String photo_id;

    @ColumnInfo(name = "caption")
    private String caption;

    @ColumnInfo(name = "date_created")
    private String date;

    @ColumnInfo(name = "image_path")
    private String image;

    @ColumnInfo(name = "user_id")
    private String user_id;

    @ColumnInfo(name = "tags")
    private String tags;

    public PhotoEntity(String photo_id, String caption, String date, String image, String user_id, String tags){
        this.photo_id = photo_id;
        this.caption = caption;
        this.date = date;
        this.image = image;
        this.tags = tags;
    }

    @NonNull
    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(@NonNull String photo_id) {
        this.photo_id = photo_id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
