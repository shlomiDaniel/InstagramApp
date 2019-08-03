package com.shlomi.instagramapp.Share;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shlomi.instagramapp.Firebase.ModelFirebase;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Utils.UniversalImageLoader;

public class NextActivity extends AppCompatActivity {
    private FirebaseDatabase mfirebasedatabase;
    private DatabaseReference myRef;
    private static final String TAG = "NextActivity";
    private ModelFirebase modelFirebase;
    private FirebaseAuth firebaseAuth;
    private Bitmap bitmap;
    private int imageCount = 0;
    private EditText photoDescription;
    private String imgUrl;
    private FirebaseAuth mAuth;
    private String mAppend = "file:/";
    private Intent intent;
    private ProgressBar progressBar;
    private TextView share;
    private ImageView backArrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_next);

        mAuth = FirebaseAuth.getInstance();
        mfirebasedatabase = FirebaseDatabase.getInstance();
        myRef = mfirebasedatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        modelFirebase = new ModelFirebase(NextActivity.this);
        photoDescription = findViewById(R.id.photoDescription);
        backArrow = findViewById(R.id.ivBackArrow);
        share = findViewById(R.id.tvShare);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Upload the image to firebase

                final String description = photoDescription.getText().toString();

                if(description.equals("")){
                    Toast.makeText(NextActivity.this, "Please write something to describe your photo.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!imgUrl.equals("")) {
                    Toast.makeText(NextActivity.this, "uploading photo...", Toast.LENGTH_SHORT).show();

                    if (intent.hasExtra(getString(R.string.selected_img))) {
                        imgUrl = intent.getStringExtra(getString(R.string.selected_img));
                        modelFirebase.uploadNewPhoto(getString(R.string.new_photo), description, imageCount, imgUrl, null);
                    } else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
                        bitmap = intent.getParcelableExtra(getString(R.string.selected_bitmap));
                        modelFirebase.uploadNewPhoto(getString(R.string.new_photo), description, imageCount, null, bitmap);
                    }
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

    private void setImage() {
        intent = getIntent();
        ImageView img = findViewById(R.id.imgShare);

        if (intent.hasExtra(getString(R.string.selected_img))) {
            imgUrl = intent.getStringExtra(getString(R.string.selected_img));
            UniversalImageLoader.setIamge(imgUrl, img, null, mAppend);
        } else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
            bitmap = intent.getParcelableExtra(getString(R.string.selected_bitmap));
            img.setImageBitmap(bitmap);
        }
    }
}
