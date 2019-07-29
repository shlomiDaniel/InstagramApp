package com.shlomi.instagramapp.Profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.FirebaseError;
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
import com.shlomi.instagramapp.R;
import com.squareup.picasso.Picasso;

public class ViewPostActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageView imagePost;
    private String photo_id;
    private String user_id;
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
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // init
        like_active.setVisibility(View.INVISIBLE);
        post_user_name.setText("");
        post_description.setText("");
        post_number_of_likes.setText(R.string.no_likes);

        if (currentUser == null) {
            // TODO: go to login page
            return;
        }

        // get data sent to this intent
        Bundle data = getIntent().getExtras();
        photo_id = data.getString("photo_id");
        user_id = data.getString("user_id");

        settUserName(user_id);
        displayPost(photo_id, user_id);

        imgBackArrow = this.findViewById(R.id.imgBackArrow);
        imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void settUserName(final String user_id){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child("users")
        .child(user_id)
        .addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onCancelled(DatabaseError error) {

            }

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                post_user_name.setText(snapshot.getValue(User.class).getUserName());
            }
        });
    }

    private void displayPost(final String photo_id, final String user_id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("user_photos").child(user_id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Photo p = snap.getValue(Photo.class);
                    if(p.getPhoto_id().equals(photo_id)){
                        photo = p;
                        break;
                    }
                }

                Picasso.get().load(photo.getImage_path()).into(imagePost);
                post_description.setText(photo.getCaption());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
