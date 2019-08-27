package com.shlomi.instagramapp.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shlomi.instagramapp.Models.Comment;
import com.shlomi.instagramapp.Models.Photo;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Utils.CommentListAdapter;
import com.shlomi.instagramapp.Utils.GridPhotoAdapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class ViewCommentActivity extends AppCompatActivity implements Serializable {


    private DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private ImageView backArrow,mcheckMark;
    private EditText mComment;
    private ArrayList<Comment> comments;
    private Photo mp;
    private ListView mlistview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fregment_new_comment);
        backArrow = findViewById(R.id.backArrow);
        mcheckMark =findViewById(R.id.postComment);
        mlistview = findViewById(R.id.listViewComments);
        mComment = findViewById(R.id.comment);

            try{


        mp =getIntent().getExtras().getParcelable("photo");
    }catch (NullPointerException ex){}




        comments = new ArrayList<>();
        Comment f = new Comment();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
         f.setComment(mp.getCaption());
        f.setUser_id(mp.getUser_id());
        f.setDate_created(mp.getDate_created());
        CommentListAdapter adapter = new CommentListAdapter(getApplicationContext(),R.layout.layout_comment,comments);
        mlistview.setAdapter(adapter);
            mcheckMark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(!mComment.getText().toString().equals("")){
                  addNewComment(mComment.getText().toString());

                  mComment.setText("");
                    }else{

                       Toast.makeText(getApplicationContext(),"enter Comment",Toast.LENGTH_SHORT).show();
                   }
                }
            });
        setQuery();
    }

    private Photo getPhotoFromBundle(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            return bundle.getParcelable("photo");
        }else{
            return null;
        }
    }

    private String getTimestap() {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        sfd.setTimeZone(TimeZone.getTimeZone("Israel"));
        return sfd.format(new Date());
    }

    private void addNewComment(String newComment){
        String commentID =reference.push().getKey();
        Comment comment = new Comment();
        comment.setComment(newComment);
        comment.setDate_created(getTimestap());
        comment.setUser_id(mAuth.getCurrentUser().getUid());


        if(mp.getPhoto_id()!=null && commentID!=null){
            reference.child(("photos")).child(mp.getPhoto_id()).child(getString(R.string.filed_comment))
                    .child(commentID).setValue(comment);

            reference.child(("user_photos")).child(mAuth.getCurrentUser().getUid()).child(mp.getPhoto_id()).child(getString(R.string.filed_comment))
                    .child(commentID).setValue(comment);

        }


       // reference.child("user_photos").child(user_id).child(photo_id).setValue(p);

    }


    private void setQuery(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child( "photos" ).child( mp.getPhoto_id()).child("comments").addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded ( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {


                for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()) {

                    Photo photo = new Photo();
//                    Map<String,Object> objectMap = (HashMap<String,Object>)singleSnapShot.getValue();
//                    photo.setCaption(objectMap.get( "caption" ).toString());
//                    photo.setCaption(objectMap.get( "tags" ).toString());
//                    photo.setCaption(objectMap.get( "photo_id" ).toString());
//                    photo.setCaption(objectMap.get( "user_id" ).toString());
//                    photo.setCaption(objectMap.get( "date_created" ).toString());
//                    photo.setCaption(objectMap.get( "image_path" ).toString());


                    ArrayList<Comment>commentArrayList = new ArrayList<Comment>();
                    commentArrayList.clear();
                    for(DataSnapshot snapshot  : singleSnapShot.child("comments").getChildren() ){
                        Comment comment = new Comment();
                        comment.setUser_id( snapshot.getValue(Comment.class).getUser_id());
                        comment.setComment( snapshot.getValue(Comment.class).getComment());
                        comment.setDate_created( snapshot.getValue(Comment.class).getDate_created());
                        commentArrayList.add( comment );

                    }
                    photo.setComments( commentArrayList );

                    mp = photo;
                }
            }

            @Override
            public void onChildChanged ( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {

            }

            @Override
            public void onChildRemoved ( @NonNull DataSnapshot dataSnapshot ) {

            }

            @Override
            public void onChildMoved ( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {

            }

            @Override
            public void onCancelled ( @NonNull DatabaseError databaseError ) {

            }
        } );
        if(mp.getPhoto_id()!=null) {
            Query query = reference.child( "photos" ).
                    orderByChild( ("photo_id") ).equalTo( mp.getPhoto_id() );


            query.addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange ( @NonNull DataSnapshot dataSnapshot ) {



                }

                @Override
                public void onCancelled ( @NonNull DatabaseError databaseError ) {

                }
            } );
        }



    }







}
