package com.shlomi.instagramapp.Profile;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
import com.shlomi.instagramapp.Home.Home;
import com.shlomi.instagramapp.Models.Photo;
import com.shlomi.instagramapp.Models.User;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Share.SectionStatePagerAdapter;
import com.shlomi.instagramapp.Utils.SignInActivity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewPostActivity extends AppCompatActivity implements Serializable , PopupMenu.OnMenuItemClickListener {

    private  DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private ImageView imagePost;
    private String photo_id;
    private String user_id;
    private String user_name;

    private Photo photo;
    private AppCompatImageView like_active;
    private AppCompatImageView like_deactive;
    private TextView post_description;
    private TextView post_number_of_likes;
    private TextView post_user_name;
    private ImageView user_profile_image;
    private ImageView imgBackArrow;
    private boolean new_image = false;
    private ImageView mcomment;
    private ImageButton img_button;


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
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mcomment =  findViewById(R.id.comment_id);
        // init
        like_active.setVisibility(View.INVISIBLE);
        post_user_name.setText("");
        post_description.setText("");
        post_number_of_likes.setText(R.string.no_likes);


        Photo mp;
        try{
            mp = getIntent().getExtras().getParcelable("photo");
        }catch (NullPointerException ex){}
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

        if(user_id!= null){

            setUserName(user_id);

        }else{
            setUserName( "" );
        }
        if(photo_id != null && user_id!= null){

            displayPost(photo_id, user_id);
        }
        img_button = findViewById( R.id.ivElipses);
        if(!user_id.equals(mAuth.getCurrentUser().getUid())){
            img_button.setVisibility( View.INVISIBLE );

        }

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

//        post_option.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick ( View v ) {
//
//            }
//        } );



        mcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference();

                Query query = reference.child("user_photos").child(user_id);

                query.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange ( @NonNull DataSnapshot dataSnapshot ) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Photo p = null;
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            p = snap.getValue(Photo.class);
                            if(p.getPhoto_id()!=null){

                                if(p.getPhoto_id().equals(photo_id)){
                                    break;
                                }
                            }

                        }

                        if(p!=null){
                            Intent intent = new Intent(ViewPostActivity.this, ViewCommentActivity.class);
                            intent.putExtra( "photo",p );
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled ( @NonNull DatabaseError databaseError ) {

                    }
                } );

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
                setLike(user_id, user_name, photo_id);
            }
        });

        like_active.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setLike(user_id, user_name, photo_id);
            }
        });
    }


    @Override
    public boolean onMenuItemClick ( MenuItem item ) {
        switch (item.getItemId()){
            case R.id.deletePost:
                deletePost(photo_id,user_id);
                break;

            case R.id.editPost:
                editPost( photo_id,user_id );
                break;

                default:
                    return false;
        }
        return true;
    }

    public void showPopup( View v){
        if(user_id.equals(mAuth.getCurrentUser().getUid()  )){
            PopupMenu popupMenu = new PopupMenu(this,v);

            popupMenu.setOnMenuItemClickListener( this );
            popupMenu.inflate( R.menu.popup_menu );
            popupMenu.show();
        }

    }

    private void setLike(final String user_id, final String user_name,  final String photo_id){
         reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("user_photos").child(user_id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                Photo p = null;
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    p = snap.getValue(Photo.class);
                    if(p.getPhoto_id()!=null){
                        if(p.getPhoto_id().equals(photo_id)){
                            break;
                        }
                    }

                }

                if(p!=null){
                    if(p.getLikes() == null){
                        p.setLikes(new ArrayList<String>());
                    }

                    if(!p.getLikes().contains(user_name)) {
                        p.getLikes().add(user_name);
                        reference.child("user_photos").child(user_id).child(photo_id).setValue(p);
                        like_deactive.setVisibility(View.GONE);
                        like_active.setVisibility(View.VISIBLE);
                    }else{
                        p.getLikes().remove(user_name);
                        reference.child("user_photos").child(user_id).child(photo_id).setValue(p);
                        like_deactive.setVisibility(View.VISIBLE);
                        like_active.setVisibility(View.GONE);
                    }

                    post_number_of_likes.setText( p.getLikes().size() + " likes" );
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

                    if(p.getPhoto_id()!=null){
                        if(p.getPhoto_id().equals(photo_id)){
                            photo = p;
                            break;
                        }

                    }

                }

                // default behaviour
                like_deactive.setVisibility(View.VISIBLE);
                like_active.setVisibility(View.GONE);

                if(photo!=null) {
                    if (photo.getLikes() != null)
                        if (photo.getLikes().size() > 0) {
                            like_deactive.setVisibility(View.GONE);
                            like_active.setVisibility(View.VISIBLE);
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

    public void editPost(final String photo_id, final String user_id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        final Query query = reference.child("user_photos").child(user_id).child(photo_id).child( "caption" );
        final Query query2 = reference.child("photos").child(photo_id).child( "caption" );


        AlertDialog.Builder builder = new AlertDialog.Builder(ViewPostActivity.this);
        builder.setTitle("Edit Post:");

        final EditText inputFiled = new EditText( ViewPostActivity.this );
        inputFiled.setText("TODO: add caption from database..");
        builder.setView( inputFiled);

        builder.setPositiveButton( "Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick ( DialogInterface dialog, int which ) {
                ((DatabaseReference) query).setValue( inputFiled.getText().toString() );
                ((DatabaseReference) query2).setValue( inputFiled.getText().toString() );

                post_description.setText( inputFiled.getText().toString() );
                Toast.makeText( ViewPostActivity.this, "Post Update success",Toast.LENGTH_SHORT ).show();

            }
        } );

        builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick ( DialogInterface dialog, int which ) {
            dialog.cancel();
            }
        } );
        Dialog dialog = builder.create();
        dialog.show();
        displayPost( photo_id,user_id );

    }

    public void deletePost(final String photo_id, final String user_id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("user_photos").child(user_id).child(photo_id);
        query.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange ( @NonNull DataSnapshot dataSnapshot ) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    ds.getRef().removeValue();

                }

            }

            @Override
            public void onCancelled ( @NonNull DatabaseError databaseError ) {
                Toast.makeText( ViewPostActivity.this, databaseError.getMessage(),Toast.LENGTH_SHORT ).show();

            }
        } );


        Query query2 = reference.child("photos").child(photo_id);
        query2.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange ( @NonNull DataSnapshot dataSnapshot ) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    ds.getRef().removeValue();
                }

                Toast.makeText( ViewPostActivity.this, "Post deleted succses",Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onCancelled ( @NonNull DatabaseError databaseError ) {

            }
        } );

        Intent intent = new Intent(this, Home.class );
        startActivity(intent);
    }
}
