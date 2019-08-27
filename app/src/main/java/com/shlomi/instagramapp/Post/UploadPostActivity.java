package com.shlomi.instagramapp.Post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shlomi.instagramapp.Cache.CacheModel;
import com.shlomi.instagramapp.Firebase.ModelFirebase;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Utils.UniversalImageLoader;

public class UploadPostActivity extends AppCompatActivity {
    private FirebaseDatabase mfirebasedatabase;
    private DatabaseReference myRef;
    private ModelFirebase modelFirebase;
    private FirebaseAuth firebaseAuth;
    private Bitmap bitmap;
    private EditText photoDescription;
    private String imgUrl;
    private FirebaseAuth mAuth;
    private String mAppend = "file:/";
    private Intent intent;
    private Button share;
    private ImageView backArrow;
    private ImageView img;
    private CacheModel appCache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        mAuth = FirebaseAuth.getInstance();
        mfirebasedatabase = FirebaseDatabase.getInstance();
        myRef = mfirebasedatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        photoDescription = findViewById(R.id.photoDescription);
        backArrow = findViewById(R.id.ivBackArrow);
        share = findViewById(R.id.tvShare);
        intent = getIntent();
        img = findViewById(R.id.imgShare);
        appCache = new CacheModel(UploadPostActivity.this);
        modelFirebase = new ModelFirebase(UploadPostActivity.this, appCache);

        // Show selected image
        if (intent.hasExtra(getString(R.string.selected_img))) {
            imgUrl = intent.getStringExtra(getString(R.string.selected_img));
            UniversalImageLoader.setIamge(imgUrl, img, null, mAppend);
        } else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
            bitmap = intent.getParcelableExtra(getString(R.string.selected_bitmap));
            img.setImageBitmap(bitmap);
        }

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
                Toast.makeText(UploadPostActivity.this, "Please write something to describe your photo.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!imgUrl.equals("")) {
                share.setEnabled(false);
                share.setAlpha(0.5f);
                Toast.makeText(UploadPostActivity.this, "uploading photo...", Toast.LENGTH_SHORT).show();

                if (intent.hasExtra(getString(R.string.selected_img))) {
                    imgUrl = intent.getStringExtra(getString(R.string.selected_img));
                    modelFirebase.uploadNewPhoto(getString(R.string.new_photo), description, imgUrl, null, UploadPostActivity.this);
                } else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
                    bitmap = intent.getParcelableExtra(getString(R.string.selected_bitmap));
                    modelFirebase.uploadNewPhoto(getString(R.string.new_photo), description, null, bitmap, UploadPostActivity.this);
                }
            }
            }
        });
    }
}
