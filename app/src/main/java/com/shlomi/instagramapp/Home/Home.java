package com.shlomi.instagramapp.Home;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.shlomi.instagramapp.Cache.CacheModel;
import com.shlomi.instagramapp.Cache.UserEntity;
import com.shlomi.instagramapp.Models.User;
import com.shlomi.instagramapp.Utils.ButtonNavigationViewHelper;
import com.shlomi.instagramapp.R;

public class Home extends AppCompatActivity {
    private CacheModel appCache = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        appCache = new CacheModel(Home.this);

        // set navigation
        BottomNavigationView bn = findViewById(R.id.bottom_navigationViewBar);
        ButtonNavigationViewHelper.enableNavigation(Home.this, bn);
        bn.getMenu().getItem(0).setIcon(R.drawable.ic_home_solid);

        setupUniversalImageLoad();
        setupViewPager();

        if(appCache!=null){
            addCurrentUser();
        }
    }

    private void addCurrentUser(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user!=null) {

                    String[] users = new String[1];
                    users[0] = user.getId();

                    // add user to cache if not exists
                    if (appCache.getDb().users().getByIds(users).size() == 0) {
                        UserEntity userEntity = new UserEntity(user.getId(), user.getEmail(), user.getPassword(), user.getProfile_image(), user.getUserName());
                        appCache.getDb().users().insertAll(userEntity);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setupUniversalImageLoad() {
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
            .resetViewBeforeLoading(true)
            .cacheOnDisk(true)
            .cacheInMemory(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .displayer(new FadeInBitmapDisplayer(300))
            .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .defaultDisplayImageOptions(defaultOptions)
            .memoryCache(new WeakMemoryCache())
            .diskCacheSize(100 * 1024 * 1024)
            .build();

        ImageLoader.getInstance().init(config);
    }

    private void setupViewPager() {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new HomeFragment());
        //adapter.addFragment(new CameraFragment());
        //adapter.addFragment(new MessagesFragment());

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_instagram_icon);
        //tabLayout.getTabAt(1).setIcon(R.drawable.ic_camera);
        //tabLayout.getTabAt(2).setIcon(R.drawable.ic_sendmessage);
    }
}
