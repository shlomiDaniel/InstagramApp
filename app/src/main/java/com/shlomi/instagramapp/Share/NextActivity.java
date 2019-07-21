package com.shlomi.instagramapp.Share;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shlomi.instagramapp.Firebase.ModelFirebase;
import com.shlomi.instagramapp.R;
import com.google.firebase.database.FirebaseDatabase;
import com.shlomi.instagramapp.Utils.FileSearch;
import com.shlomi.instagramapp.Utils.UniversalImageLoader;


public class NextActivity extends AppCompatActivity {
    private FirebaseDatabase mfirebasedatabase;
    private DatabaseReference myRef;
    private static final String TAG = "NextActivity";
    private ModelFirebase modelFirebase;
    private FirebaseAuth firebaseAuth;
    private int imageCount = 0;
    private EditText mCaption;
    private String imgUrl;
   private  FirebaseAuth mAuth;
    private String mAppend = "file:/";


    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        mAuth = FirebaseAuth.getInstance();
        mfirebasedatabase =    FirebaseDatabase.getInstance();
        myRef = mfirebasedatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        modelFirebase = new ModelFirebase(NextActivity.this);
        mCaption = (EditText) findViewById(R.id.caption);
        ImageView backArrow = (ImageView)findViewById(R.id.ivBackArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView share = (TextView)findViewById(R.id.tvShare);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //upload the image to firebase
                Toast.makeText(NextActivity.this,"uploading photo",Toast.LENGTH_SHORT).show();
                String caption = mCaption.getText().toString();
                if(imgUrl!=""){
                    modelFirebase.uploadNewPhoto(getString(R.string.new_photo),caption,imageCount,imgUrl);

                }
            }
        });
         setImage();

         myRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 imageCount = modelFirebase.getImageCount(dataSnapshot);

             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
             }
         });

    }

    private void setImage(){
        Intent intent = getIntent();
        ImageView img = (ImageView)findViewById(R.id.imgShare);
        imgUrl = intent.getStringExtra(getString(R.string.selected_img));
        UniversalImageLoader.setIamge(imgUrl,img,null,mAppend);

//        if(intent.hasExtra("selected_img")){
//            FirebaseStorage storage = FirebaseStorage.getInstance();
//            StorageReference storageRef = storage.getReference();
//            //storage = FirebaseStorage.getInstance();
//            StorageReference photoReference = storageRef.child("photos").child("users").child(mAuth.getCurrentUser().getUid()).child("profile_image.png");
//            photoReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                   // photoReference.child()
//
//
//
//
//
//
//
//
//
//                }
//            });
//
//        }





    }



    private void someMethod(){


    }


}
