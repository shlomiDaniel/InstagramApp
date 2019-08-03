package com.shlomi.instagramapp.Profile;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shlomi.instagramapp.Firebase.ModelFirebase;
import com.shlomi.instagramapp.Models.Photo;
import com.shlomi.instagramapp.Models.User;
import com.shlomi.instagramapp.Models.UserAccountSetting;
import com.shlomi.instagramapp.Models.UserSetting;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Share.SectionStatePagerAdapter;
import com.shlomi.instagramapp.Utils.ButtonNavigationViewHelper;
import com.shlomi.instagramapp.Utils.GridImageAdapter;
import com.shlomi.instagramapp.Utils.SignInActivity;
import com.shlomi.instagramapp.Utils.UniversalImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private TextView userName;
    private TextView followers;
    private TextView following;
    private TextView email;
    private TextView posts;
    private TextView name;
    private Button logOut;
    private ModelFirebase modelFirebase;
    private FirebaseAuth firebaseAuth;
    private ImageView profile_photo;
    private TextView numberOfPhotos;
    private TextView proileName;
    private TextView emailtText;
    private GridView gridView;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ProgressBar progressBar;
    private FirebaseDatabase mfirebasedatabase;
    private DatabaseReference myRef;
    private static String TAG = "Profile_activity";
    private final int activity_num = 1;
    private final int NUM_GRID_COLUMS = 3;
    private ArrayList<Photo> photos;
    private SectionStatePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        photos = new ArrayList<>();
        email = (TextView) findViewById(R.id.email_id);
        gridView = (GridView) findViewById(R.id.gridView);
        myRef = FirebaseDatabase.getInstance().getReference();
        emailtText = (TextView) findViewById(R.id.email_id);
        numberOfPhotos = (TextView) findViewById(R.id.numberOfPhotos);
        profile_photo = (ImageView) findViewById(R.id.profile_photo);
        proileName = findViewById(R.id.proileName);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        setupToolBar();
        setTitle("");
        setupButonNavigation();
        initImageLoader();
        setupGridView();

        Button editProfile = findViewById(R.id.editProfileButtonid);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(ProfileActivity.this, accountSettingsActivity.class);
            intent.putExtra(getString(R.string.calling_activity),getString(R.string.profile_activity));
            startActivity(intent);
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setProfileInfo(modelFirebase.getUserAccoountSetting(dataSnapshot));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void setupButonNavigation() {
        BottomNavigationView bn = (BottomNavigationView) findViewById(R.id.bottom_navigationViewBar);
        ButtonNavigationViewHelper.enableNavigation(ProfileActivity.this, bn);
        modelFirebase = new ModelFirebase(getApplicationContext());
        Menu menu = bn.getMenu();
        MenuItem menuItem = menu.getItem(activity_num);
        menuItem.setChecked(true);
    }

    private void setupGridView() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("user_photos").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()) {
                    photos.add(singleSnapShot.getValue(Photo.class));
                }

                int gridWidth = getResources().getDisplayMetrics().widthPixels;
                int imgWidth = gridWidth / NUM_GRID_COLUMS;

                gridView.setColumnWidth(imgWidth);

                ArrayList<String> imgUrls = new ArrayList<>();

                for (int i = 0; i < photos.size(); i++) {
                    imgUrls.add(photos.get(i).getImage_path());
                }

                GridImageAdapter adapter = new GridImageAdapter(ProfileActivity.this, R.layout.layaout_grid_imgview, "", imgUrls);
                gridView.setAdapter(adapter);

                if(photos.size() > 0) {
                    numberOfPhotos.setText(getString(R.string.totalUploadedPhotos, photos.size()));
                }
                else {
                    numberOfPhotos.setText(getString(R.string.noPhotosUploaded));
                }

                gridView.setOnItemClickListener(itemClickListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<Photo> getPhotos(){
        return this.photos;
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            final ArrayList<Photo> all_photos = getPhotos();
            final Photo photo = all_photos.get(position);

            Bundle bundle = new Bundle();
            bundle.putString("photo_id", photo.getPhoto_id());
            bundle.putString("user_id", photo.getUser_id());

            Intent intent  = new Intent(ProfileActivity.this, ViewPostActivity.class);
            intent.putExtra("photo_id", photo.getPhoto_id());
            intent.putExtra("user_id", photo.getUser_id());

            startActivity(intent);
        }
    };

    private void setProfileInfo(UserSetting userSetting) {
        User user = userSetting.getUser();
        UserAccountSetting userAccountSetting = userSetting.getUserAccountSetting();
        emailtText.setText(user.getEmail());

        proileName.setText(user.getUserName());
        Picasso.get().load(user.getProfile_image()).into(profile_photo);
    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(ProfileActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);

        ImageView profile_image_view = (ImageView) findViewById(R.id.profile_menu);
        profile_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(ProfileActivity.this, accountSettingsActivity.class);
            startActivity(intent);
            }
        });
    }
}
