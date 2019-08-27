package com.shlomi.instagramapp.Post;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shlomi.instagramapp.Models.Photo;
import com.shlomi.instagramapp.Models.User;
import com.shlomi.instagramapp.Profile.ProfileActivity;
import com.shlomi.instagramapp.Profile.accountSettingsActivity;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Utils.SignInActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class ViewPostActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageView imagePost;
    private String photo_id;
    private String user_id;
    private String user_name;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Photo photo;
    private AppCompatImageView like_active;
    private AppCompatImageView like_deactive;
    private TextView post_description;
    private TextView post_number_of_likes;
    private TextView post_user_name;
    private ImageView user_profile_image;
    private ImageView imgBackArrow;
    private boolean new_image = false;
    private FirebaseUser currentUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_view_post);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        imagePost = findViewById(R.id.post_img);
        like_deactive = findViewById(R.id.like_deactive);
        like_active = findViewById(R.id.like_active);
        post_description = findViewById(R.id.post_description);
        post_number_of_likes = findViewById(R.id.post_number_of_likes);
        user_profile_image = findViewById(R.id.user_profile_image);
        post_user_name = findViewById(R.id.post_user_name);
        imgBackArrow = findViewById(R.id.imgBackArrow);
        currentUser = mAuth.getCurrentUser();

        // init
        like_deactive.setVisibility(View.VISIBLE);
        like_active.setVisibility(View.GONE);
        post_user_name.setText("");
        post_description.setText("");
        post_number_of_likes.setText(R.string.no_likes);

        if (currentUser == null) {
            finish();
            startActivity(new Intent(this, SignInActivity.class));
            return;
        }

        // get data sent to this intent
        Bundle data = getIntent().getExtras();
        photo_id = data.getString("photo_id");
        user_id = data.getString("user_id");
        new_image = data.getBoolean("new_image");

        setUserName(user_id);

        imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if(new_image){
                    Intent intent = new Intent(ViewPostActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
            }
        });

        post_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPostActivity.this, accountSettingsActivity.class);
                intent.putExtra(getString(R.string.calling_activity),getString(R.string.profile_activity));
                intent.putExtra("back_to_post", true);
                intent.putExtra("photo_id", photo_id);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                finish();
            }
        });

        user_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPostActivity.this, accountSettingsActivity.class);
                intent.putExtra(getString(R.string.calling_activity),getString(R.string.profile_activity));
                startActivity(intent);
                finish();
            }
        });

        like_deactive.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            setLike(user_id, photo_id, currentUser.getUid());
            }
        });

        like_active.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            setLike(user_id, photo_id, currentUser.getUid());
            }
        });
    }

    private void setLike(final String user_id, final String photo_id, final String clicked_user_id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("user_photos").child(user_id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                Photo p = null;
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    p = snap.getValue(Photo.class);
                    if(p.getPhoto_id().equals(photo_id)){
                       break;
                    }
                }

                if(p!=null){
                    if(p.getLikes() == null){
                        p.setLikes(new ArrayList<String>());
                    }

                    if(!p.getLikes().contains(clicked_user_id)) {
                        p.getLikes().add(clicked_user_id);
                        reference.child("user_photos").child(user_id).child(photo_id).setValue(p);
                        like_deactive.setVisibility(View.GONE);
                        like_active.setVisibility(View.VISIBLE);
                    }else{
                        p.getLikes().remove(clicked_user_id);
                        reference.child("user_photos").child(user_id).child(photo_id).setValue(p);
                        like_deactive.setVisibility(View.VISIBLE);
                        like_active.setVisibility(View.GONE);
                    }

                    post_number_of_likes.setText(String.format(Locale.US,"%d likes", p.getLikes().size()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUserName(final String user_id){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child("users")
        .child(user_id)
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onCancelled(DatabaseError error) {

            }

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user_name = (snapshot.getValue(User.class).getUserName());
                post_user_name.setText(user_name);
                Picasso.get().load(snapshot.getValue(User.class).getProfile_image()).into(user_profile_image);
                displayPost(photo_id, user_id);
            }
        });
    }

    private void displayPost(final String photo_id, final String user_id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("user_photos").child(user_id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Photo p;
                photo = null;
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    p = snap.getValue(Photo.class);
                    if(p.getPhoto_id().equals(photo_id)){
                        photo = p;
                        break;
                    }
                }

                // default behaviour
                like_deactive.setVisibility(View.VISIBLE);
                like_active.setVisibility(View.GONE);

                if(photo!=null) {
                    if (photo.getLikes() != null)
                        if (photo.getLikes().size() > 0) {
                            post_number_of_likes.setText(String.format(Locale.US,"%d likes", photo.getLikes().size()));

                            if(photo.getLikes().contains(currentUser.getUid())) {
                                like_deactive.setVisibility(View.GONE);
                                like_active.setVisibility(View.VISIBLE);
                            }
                        }

                    Picasso.get().load(photo.getImage_path()).into(imagePost);
                    post_description.setText(photo.getCaption());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
