package com.shlomi.instagramapp.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Parcelable {
    private String caption;
    private String date_created;
    private String image_path;
    private String photo_id;
    private String user_id;
    private String tags;
    private ArrayList<String> likes;
    private List<Comment> comments;


    public Photo() { }

//    public Photo(String caption, String date_created, String image_path, String photo_id, String user_id, String tags) {
//        this.caption = caption;
//        this.date_created = date_created;
//        this.image_path = image_path;
//        this.photo_id = photo_id;
//        this.user_id = user_id;
//        this.tags = tags;
//        this.likes = new ArrayList<>();
//       // this.comments = new ArrayList<>(  );
//    }

    public Photo ( String caption, String date_created, String image_path, String photo_id, String user_id, String tags, ArrayList<String> likes, ArrayList<Comment> comments ) {
        this.caption = caption;
        this.date_created = date_created;
        this.image_path = image_path;
        this.photo_id = photo_id;
        this.user_id = user_id;
        this.tags = tags;
        this.likes = new ArrayList<>();
        this.comments = comments;
    }

//    public List<Comment> getComments () {
//        return comments;
//    }
//
    public void setComments ( List<Comment> comments ) {
        this.comments = comments;
    }

    //    public Photo ( String caption, String date_created, String image_path, String photo_id, String user_id, String tags, ArrayList<String> likes, ArrayList<Comment> comments ) {
//        this.caption = caption;
//        this.date_created = date_created;
//        this.image_path = image_path;
//        this.photo_id = photo_id;
//        this.user_id = user_id;
//        this.tags = tags;
//        this.likes = likes;
//        this.comments = comments;
//    }



//    public void setComments ( ArrayList<Comment> comments ) {
//        this.comments = comments;
//    }
////
//    public List<Comment> getComments () {
//        return comments;
//    }

    protected Photo ( Parcel in ) {
        caption = in.readString();
        date_created = in.readString();
        image_path = in.readString();
        photo_id = in.readString();
        user_id = in.readString();
        tags = in.readString();
        likes = in.createStringArrayList();


    }


    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel ( Parcel in ) {
            return new Photo( in );
        }

        @Override
        public Photo[] newArray ( int size ) {
            return new Photo[size];
        }
    };

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



    @Override
    public int hashCode () {
        return super.hashCode();
    }

    @Override
    public int describeContents () {
        return 0;
    }
//
    @Override
    public void writeToParcel ( Parcel dest, int flags ) {
        dest.writeString( caption );
        dest.writeString( date_created );
        dest.writeString( image_path );
        dest.writeString( photo_id );
        dest.writeString( user_id );
        dest.writeString( tags );
        dest.writeStringList( likes );
    }
}
