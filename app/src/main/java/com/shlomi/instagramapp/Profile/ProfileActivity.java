package com.shlomi.instagramapp.Profile;

import android.content.Intent;
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
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shlomi.instagramapp.Cache.CacheModel;
import com.shlomi.instagramapp.Cache.PhotoEntity;
import com.shlomi.instagramapp.Cache.UserEntity;
import com.shlomi.instagramapp.Firebase.ModelFirebase;
import com.shlomi.instagramapp.Models.Photo;
import com.shlomi.instagramapp.Models.User;
import com.shlomi.instagramapp.Models.UserAccountSetting;
import com.shlomi.instagramapp.Models.UserSetting;
import com.shlomi.instagramapp.Post.ViewPostActivity;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Utils.ButtonNavigationViewHelper;
import com.shlomi.instagramapp.Utils.GridPhotoAdapter;
import com.shlomi.instagramapp.Utils.InetConnection;
import com.shlomi.instagramapp.Utils.SignInActivity;
import com.shlomi.instagramapp.Utils.UniversalImageLoader;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private ModelFirebase modelFirebase;
    private FirebaseAuth firebaseAuth;
    private ImageView profile_photo;
    private TextView numberOfPhotos;
    private TextView proileName;
    private TextView emailtText;
    private GridView gridView;
    private DatabaseReference myRef;
    private final int activity_num = 1;
    private final int NUM_GRID_COLUMS = 3;
    private ArrayList<Photo> photos;
    private Button editProfile;
    private CacheModel appCache;
    private InetConnection inet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        photos = new ArrayList<>();
        gridView = findViewById(R.id.gridView);
        myRef = FirebaseDatabase.getInstance().getReference();
        emailtText = findViewById(R.id.email_id);
        numberOfPhotos = findViewById(R.id.numberOfPhotos);
        profile_photo = findViewById(R.id.profile_photo);
        proileName = findViewById(R.id.proileName);
        firebaseAuth = FirebaseAuth.getInstance();
        editProfile = findViewById(R.id.editProfileButtonid);
        appCache = new CacheModel(ProfileActivity.this);
        inet = new InetConnection(1000);

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        }

        setupToolBar();
        setTitle("");
        setupButonNavigation();
        initImageLoader();
        setupGridView();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, accountSettingsActivity.class);
                intent.putExtra(getString(R.string.calling_activity),getString(R.string.profile_activity));
                startActivity(intent);
            }
        });

        if(inet.isInternetAvailable()) {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    setProfileInfo(modelFirebase.getUserAccoountSetting(dataSnapshot));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }else{
            UserEntity u = appCache.getDb().users().getUser();
            UserSetting userSetting = new UserSetting(new User(u.getId(), u.getUserName(), u.getEmail(), u.getPassword(), u.getProfile_image()));
            setProfileInfo(userSetting);
        }
    }

    public void setupButonNavigation() {
        BottomNavigationView bn = findViewById(R.id.bottom_navigationViewBar);
        ButtonNavigationViewHelper.enableNavigation(ProfileActivity.this, bn);
        bn.getMenu().getItem(4).setIcon(R.drawable.ic_user_solid);

        modelFirebase = new ModelFirebase(getApplicationContext());
        Menu menu = bn.getMenu();
        MenuItem menuItem = menu.getItem(activity_num);
        menuItem.setChecked(true);
    }

    private void setupGridView() {
        // check for internet connection
        if(inet.isInternetAvailable()){
            //load from firebase
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            Query query = reference.child("user_photos").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()) {
                        Photo photo = singleSnapShot.getValue(Photo.class);
                        photos.add(photo);

                        // cache user photos
                        if(appCache.getDb().photos().getById(photo.getPhoto_id()) == null){
                            PhotoEntity photo_entity = new PhotoEntity(photo.getPhoto_id(), photo.getCaption(), photo.getDate_created(), photo.getImage_path(), photo.getUser_id(),photo.getTags());
                            appCache.getDb().photos().insertAll(photo_entity);
                        }
                    }

                    int gridWidth = getResources().getDisplayMetrics().widthPixels;
                    int imgWidth = gridWidth / NUM_GRID_COLUMS;

                    gridView.setColumnWidth(imgWidth);

                    GridPhotoAdapter adapter = new GridPhotoAdapter(ProfileActivity.this, R.layout.layaout_grid_imgview, "", photos);
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
        }else{
            //load from cache
            List<PhotoEntity> cachedPhotos = appCache.getDb().photos().getAll();

            for(PhotoEntity p : cachedPhotos){
                if(p.getUser_id().equals(firebaseAuth.getCurrentUser().getUid())) {
                    Photo photo = new Photo(p.getCaption(), p.getDate(), p.getImage(), p.getPhoto_id(), p.getUser_id(), p.getTags());
                    photos.add(photo);
                }
            }

            int gridWidth = getResources().getDisplayMetrics().widthPixels;
            int imgWidth = gridWidth / NUM_GRID_COLUMS;

            gridView.setColumnWidth(imgWidth);

            GridPhotoAdapter adapter = new GridPhotoAdapter(ProfileActivity.this, R.layout.layaout_grid_imgview, "", photos);
            gridView.setAdapter(adapter);

            if(photos.size() > 0) {
                numberOfPhotos.setText(getString(R.string.totalUploadedPhotos, photos.size()));
            }
            else {
                numberOfPhotos.setText(getString(R.string.noPhotosUploaded));
            }

            gridView.setOnItemClickListener(itemClickListener);
        }
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
        emailtText.setText(user.getEmail());

        proileName.setText(user.getUserName());
        if(!user.getProfile_image().equals("")) {
            Picasso.get().load(user.getProfile_image()).into(profile_photo);
        }
    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(ProfileActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);

        ImageView profile_image_view = findViewById(R.id.profile_menu);
        profile_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, accountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
