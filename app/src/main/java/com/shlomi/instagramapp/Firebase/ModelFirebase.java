package com.shlomi.instagramapp.Firebase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shlomi.instagramapp.Models.Photo;
import com.shlomi.instagramapp.Models.User;
import com.shlomi.instagramapp.Models.UserAccountSetting;
import com.shlomi.instagramapp.Models.UserSetting;
import com.shlomi.instagramapp.Profile.ViewPostActivity;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Utils.FilePath;
import com.shlomi.instagramapp.Utils.ImageManager;
import com.shlomi.instagramapp.Utils.StringManipulation;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class ModelFirebase {
    private Context mcontext;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private Context context;
    private DatabaseReference databaseReference;
    private String userId;
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

    public void uploadNewPhoto(String photo_type, final String caption, String imgUrl, Bitmap bm, final AppCompatActivity sourceActivity) {
        FilePath filePath = new FilePath();

        // new post
        if (!photo_type.equals("profile_photo")) {
            if (bm == null) {
                bm = ImageManager.getBitmap(imgUrl);
            }

            final StorageReference storageReference = firebaseStorage.getReference().child(filePath.FIRE_BASE_IMAGE_STORAGE + "/" + userId + "/" + UUID.randomUUID().toString());

            UploadTask uploadTask = null;
            byte[] bytes = ImageManager.getByteFromBitMap(bm, 100);
            uploadTask = storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        Bundle data;

                        @Override
                        public void onSuccess(Uri uri) {
                            String photoLink = uri.toString();

                            if (caption != null) {
                                data = addPhotoToDataBase(caption, photoLink);
                            } else {
                                data = addPhotoToDataBase("", photoLink);
                            }

                            Intent intent = new Intent(sourceActivity, ViewPostActivity.class);
                            final String photo_id = data.getString("photo_id");
                            final String user_id = data.getString("user_id");
                            intent.putExtra("photo_id", photo_id);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("new_image", true);
                            context.startActivity(intent);
                        }
                    });
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Unable to add image to storage reference", Toast.LENGTH_LONG).show();
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                final double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                if (progress - 15 > mPhotoUploadProgress) {
                    Toast.makeText(context, "photo upload in progress: " + String.format(Locale.US,"%.0f", progress) + "%", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, "Profile Photo updated success", Toast.LENGTH_LONG).show();
                    setProfilePhoto(photoLink);
                    }
                });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Unable to add image to storage reference", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                // double progress = (100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                        .getTotalByteCount());
                if (progress - 15 > mPhotoUploadProgress) {
                    Toast.makeText(context, "photo upload in progress: " + String.format(Locale.US,"%.0f", progress) + "%", Toast.LENGTH_SHORT).show();
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

    private Bundle addPhotoToDataBase(String caption, String url) {
        String tags = StringManipulation.getTags(caption);
        String newPhotoKey = databaseReference.child("photos").push().getKey();

        Bundle data = new Bundle();
        Photo photo = new Photo();

        photo.setCaption(caption);
        photo.setDate_created(getTimestap());
        photo.setImage_path(url);
        photo.setTags(tags);
        photo.setPhoto_id(newPhotoKey);
        photo.setUser_id(firebaseAuth.getCurrentUser().getUid());

        data.putString("photo_id", photo.getPhoto_id());
        data.putString("user_id" ,photo.getUser_id());

        databaseReference.child(("user_photos")).child(firebaseAuth.getCurrentUser().getUid()).child(newPhotoKey).setValue(photo);
        databaseReference.child(("photos")).child(newPhotoKey).setValue(photo);

        return data;
    }

    private void setProfilePhoto(String url) {
        databaseReference.child("users").child(userId).child("profile_image").setValue(url);
    }
}