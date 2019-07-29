package com.shlomi.instagramapp.Firebase;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shlomi.instagramapp.Home.HomeActivity;
import com.shlomi.instagramapp.Models.Photo;
import com.shlomi.instagramapp.Models.User;
import com.shlomi.instagramapp.Models.UserAccountSetting;
import com.shlomi.instagramapp.Models.UserSetting;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Share.ShareActivity;
import com.shlomi.instagramapp.Utils.FilePath;
import com.shlomi.instagramapp.Utils.ImageManager;
import com.shlomi.instagramapp.Utils.StringManipulation;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class ModelFirebase {

    public Context mcontext;
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseStorage firebaseStorage;
    private Context context;
    public DatabaseReference databaseReference;
    public String userId;
    private double mPhotoUploadProgress = 0;

    public ModelFirebase(Context context) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        this.context = context;
        if (firebaseAuth.getCurrentUser() != null) {
            userId = firebaseAuth.getCurrentUser().getUid();
        }
    }

    public int getImageCount(DataSnapshot dataSnapshot) {
        int count = 0;

        for (DataSnapshot ds : dataSnapshot.child("user_photos")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid()).getChildren()) {
            count++;

        }

        return count;

    }

    public void RegisterNewEmail(final String email, String pass, String userName) {
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(context, "authentication Faild", Toast.LENGTH_LONG).show();
                } else if (task.isSuccessful()) {
                    userId = firebaseAuth.getCurrentUser().getUid();

                    sendVereficationEmail();
                    Toast.makeText(context, "Success user Createded", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void sendVereficationEmail() {
        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {
                    Toast.makeText(mcontext, "couldnt send verefication email.", Toast.LENGTH_SHORT).show();
                }
                }
            });
        }
    }

    public UserSetting getUserAccoountSetting(DataSnapshot dataSnapshot) {
        UserAccountSetting setting = new UserAccountSetting();
        User user = new User();
        UserSetting userSetting = new UserSetting(user, setting);

        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            if (ds.getKey().equals("users")) {

                try {
                    user.setUserName(
                        ds.child(userId).getValue(User.class).getUserName()

                    );
                    user.setEmail(
                        ds.child(userId).getValue(User.class).getEmail()

                    );
                    user.setPassword(
                        ds.child(userId).getValue(User.class).getPassword()

                    );
                    user.setProfile_image_url(
                        ds.child(userId).getValue(User.class).getProfile_image()
                    );
                } catch (NullPointerException ex) {
                    Log.d("error", "nullpointer ex from users");
                }
            }

            if (ds.getKey().equals("userAcountSetting")) {

                try {
                    setting.setDisplay_name(
                        ds.child(userId).getValue(UserAccountSetting.class).getDisplay_name()

                    );
                    setting.setDescription(
                        ds.child(userId).getValue(UserAccountSetting.class).getDescription()
                    );

                    setting.setProfile_photo(
                        ds.child(userId).getValue(UserAccountSetting.class).getProfile_photo()
                    );

                    setting.setPosts(
                        ds.child(userId).getValue(UserAccountSetting.class).getPosts()
                    );

                    setting.setFollowers(
                        ds.child(userId).getValue(UserAccountSetting.class).getFollowers()
                    );
                    setting.setFollowing(
                        ds.child(userId).getValue(UserAccountSetting.class).getFollowing()
                    );
                    setting.setWebsite(
                        ds.child(userId).getValue(UserAccountSetting.class).getWebsite()
                    );
                    setting.setUserName(
                        ds.child(userId).getValue(UserAccountSetting.class).getUserName()
                    );
                } catch (NullPointerException ex) {
                    Log.d("error", "null pointer ex");
                }
            }
        }

        return new UserSetting(user, setting);
    }

    public void updateUserName(String userName) {
        firebaseDatabase.getReference().child("users").child(userId).child(mcontext.getString(R.string.filed_user_name)).setValue(userName);
        firebaseDatabase.getReference().child("userAcountSetting").child(userId).child(mcontext.getString(R.string.filed_user_name)).setValue(userName);
    }

    public void updateEmail(String email) {
        firebaseDatabase.getReference().child("users").child(userId).child("email").setValue(email);
        // firebaseDatabase.getReference().child("userAcountSetting").child(userId).child(mcontext.getString(R.string.filed_user_name)).setValue(email);
    }

    public void updateUserAcountSetting(String displayName, String userName, String description, String website) {
        if (userName != null) {
            firebaseDatabase.getReference().child("users").child(userId).child("userName").setValue(userName);
            firebaseDatabase.getReference().child("userAcountSetting").child(userId).child("userName").setValue(userName);
        }

        if (description != null) {
            firebaseDatabase.getReference().child("userAcountSetting").child(userId).child("description").setValue(description);

        }

        if (website != null) {
            firebaseDatabase.getReference().child("userAcountSetting").child(userId).child("website").setValue(website);

        }

        if (displayName != null) {
            firebaseDatabase.getReference().child("userAcountSetting").child(userId).child("display_name").setValue(displayName);
        }
    }

    public void uploadNewPhoto(String photo_type, final String caption, int count, String imgUrl, Bitmap bm) {
        FilePath filePath = new FilePath();
        //    String a = mcontext.getString(R.string.new_photo);
        final boolean a = true;
        String b = photo_type;
        //photo_type.equals(mcontext.getString(R.string.new_photo)
        if (photo_type != "profile_photo") {
            if (bm == null) {
                bm = ImageManager.getBitmap(imgUrl);
            }

            final StorageReference storageReference = firebaseStorage.getReference().child(filePath.FIRE_BASE_IMAGE_STORAGE + "/" + userId + "/photo" + (count + 1));
            UploadTask uploadTask = null;
            byte[] bytes = ImageManager.getByteFromBitMap(bm, 100);
            uploadTask = storageReference.putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                    String photoLink = uri.toString();
                    Toast.makeText(context, "photo update succsess", Toast.LENGTH_LONG).show();
                    if (caption != null) {
                        addPhotoToDataBase(caption, photoLink);
                    } else {
                        addPhotoToDataBase("", photoLink);
                    }
                    }
                });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "photo update failed", Toast.LENGTH_LONG).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    // double progress = (100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                            .getTotalByteCount());
                    if (progress - 15 > mPhotoUploadProgress) {
                        Toast.makeText(context, "photo upload in progress" + String.format("%.0f", progress), Toast.LENGTH_SHORT).show();
                        mPhotoUploadProgress = progress;
                    }
                }
            });
        }

        if (photo_type.equals("profile_photo")) {
            final StorageReference storageReference = firebaseStorage.getReference().child(filePath.FIRE_BASE_IMAGE_STORAGE + "/" + userId + "/profile_image");
            if (bm == null) {
                bm = ImageManager.getBitmap(imgUrl);

            }
            UploadTask uploadTask = null;
            byte[] bytes = ImageManager.getByteFromBitMap(bm, 100);
            uploadTask = storageReference.putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                        StorageReference ref = storageReference.child("images/" + "profile_image.png");
                        String userId = firebaseAuth.getCurrentUser().getUid();
                        ref = storageReference.child("photos").child("users").child(userId).child("profile_image.png");

                        String photoLink = uri.toString();
                        Toast.makeText(context, "Profile Photo updated succsess", Toast.LENGTH_LONG).show();
                        setProfilePhoto(photoLink);
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "photo update failed", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                // double progress = (100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                        .getTotalByteCount());
                if (progress - 15 > mPhotoUploadProgress) {
                    Toast.makeText(context, "photo upload in progress" + String.format("%.0f", progress), Toast.LENGTH_SHORT).show();
                    mPhotoUploadProgress = progress;
                }
                }
            });
        }
    }

    private String getTimestap() {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        sfd.setTimeZone(TimeZone.getTimeZone("Israel"));
        return sfd.format(new Date());
    }

    private void addPhotoToDataBase(String caption, String url) {
        String tags = StringManipulation.getTags(caption);
        String newPhotoKey = databaseReference.child("photos").push().getKey();
        Photo photo = new Photo();
        photo.setCaption(caption);
        photo.setDate_created(getTimestap());
        photo.setImage_path(url);
        photo.setTags(tags);
        photo.setPhoto_id(newPhotoKey);
        photo.setUser_id(firebaseAuth.getCurrentUser().getUid());
        databaseReference.child(("user_photos")).child(firebaseAuth.getCurrentUser().getUid()).child(newPhotoKey).setValue(photo);
        databaseReference.child(("photos")).child(newPhotoKey).setValue(photo);
    }

    private void setProfilePhoto(String url) {
        databaseReference.child("users").child(userId).child("profile_image").setValue(url);
    }
}


class MainApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MainApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MainApplication.context;
    }
}