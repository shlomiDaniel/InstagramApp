package com.shlomi.instagramapp.Profile;

import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shlomi.instagramapp.Home.HomeActivity;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Search.SearchActivity;
import com.shlomi.instagramapp.Utils.ButtonNavigationViewHelper;
import com.shlomi.instagramapp.Utils.GridImageAdapter;
import com.shlomi.instagramapp.Utils.SignInActivity;
import com.shlomi.instagramapp.Utils.UniversalImageLoader;
import com.shlomi.instagramapp.Utils.User;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity  {

    private TextView email;
    private TextView pass;
    private Button logOut;
    private FirebaseAuth firebaseAuth;
    private ImageView profile_image;
    private ProgressBar progressBar;
   private static  String TAG = "Profile_activity";
    private final int activity_num  = 1;

    public void setupButonNavigation(){
        BottomNavigationView bn = (BottomNavigationView)findViewById(R.id.bottom_navigationViewBar);
        // ButtonNavigationViewHelper.setupButtonNavigationView(bn);
        // Intent intent2 = new Intent(ProfileActivity.this, Home.class);
        // startActivity(intent2);
        ButtonNavigationViewHelper.enableNavigation(ProfileActivity.this,bn);

        Menu menu = bn.getMenu();
        MenuItem menuItem = menu.getItem(activity_num);
        menuItem.setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_profile);
        super.onCreate(savedInstanceState);

       setupButonNavigation();

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        }
        setupToolBar();
       // setProfile_image();
         setTitle("                                                ");
        FirebaseUser user = firebaseAuth.getCurrentUser();
       initImageLoader();
        setupActivityWidgets();
       setProfile_image();
       tempGridSetup();
    }
    private void setProfile_image(){


        UniversalImageLoader.setIamge("http://www.teknoustam.com/wp-content/uploads/2018/06/android.png"
                ,profile_image,null,"");
    }

    private void setupActivityWidgets(){
//        progressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
//        progressBar.setVisibility(View.GONE);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        profile_image = (ImageView) findViewById(R.id.profile_image);
        TextView userNameText = (TextView)findViewById(R.id.usernameid);
        userNameText.setText(user.getDisplayName());

        profile_image = (ImageView) findViewById(R.id.profile_image);
        TextView emailtText = (TextView)findViewById(R.id.email_id);
        emailtText.setText(user.getEmail());

         DatabaseReference mDatabase ;
        //mDatabase.child("users").child(userId).child("username").setValue(name);
        mDatabase = FirebaseDatabase.getInstance().getReference();
         //String name = mDatabase.child("users").child(firebaseAuth.getUid());
        //String name = mDatabase.child("users").child(firebaseAuth.getUid()).child("userName");
    }

    public void setupImageGreedView(ArrayList<String>imgUrls){
        GridView gridView = findViewById(R.id.gridView);
        int gridwidth = getResources().getDisplayMetrics().widthPixels;
        int imgWidth = gridwidth/3;
        gridView.setColumnWidth(imgWidth);
        GridImageAdapter adapter = new GridImageAdapter(ProfileActivity.this,R.layout.layaout_grid_imgview,"",imgUrls);
        gridView.setAdapter(adapter);
    }

    private  void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(ProfileActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setupToolBar(){

        Toolbar toolbar = (Toolbar)findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);

        ImageView profile_image_view = (ImageView)findViewById(R.id.profile_menu);
        profile_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"ONCLICK NAVIGATION ACOUNT SETTING");
                Intent intent = new Intent(ProfileActivity.this,accountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void tempGridSetup(){
        ArrayList<String> imgUrls = new ArrayList<>();
        imgUrls.add("https://upload.wikimedia.org/wikipedia/commons/f/f9/Phoenicopterus_ruber_in_S%C3%A3o_Paulo_Zoo.jpg");
        imgUrls.add("https://previews.123rf.com/images/underworld/underworld1402/underworld140200575/25668343-%E9%9D%9E%E5%B8%B8%E3%81%AB%E9%95%B7%E3%81%84%E3%81%8F%E3%81%A1%E3%81%B0%E3%81%97%E3%82%92%E6%8C%81%E3%81%A4%E7%8F%8D%E3%81%97%E3%81%84%E3%83%94%E3%83%B3%E3%82%AF%E3%81%AE%E3%82%AA%E3%82%A6%E3%83%A0%E9%B3%A5.jpg");
        imgUrls.add("https://previews.123rf.com/images/sergioboccardo/sergioboccardo1503/sergioboccardo150300132/38731688-%E3%83%96%E3%83%A9%E3%82%B8%E3%83%AB%E3%81%AE%E9%BB%84%E8%89%B2%E7%9B%AE%E9%BB%92%E3%81%84%E3%81%8F%E3%81%A1%E3%81%B0%E3%81%97%E3%81%A8%E3%83%95%E3%83%A9%E3%83%9F%E3%83%B3%E3%82%B4.jpg");
        imgUrls.add("https://upload.wikimedia.org/wikipedia/commons/8/8e/American_Flamingo_-_Phoenicopterus_ruber.jpg");
        imgUrls.add("https://upload.wikimedia.org/wikipedia/commons/f/f9/Phoenicopterus_ruber_in_S%C3%A3o_Paulo_Zoo.jpg");
        imgUrls.add("https://previews.123rf.com/images/underworld/underworld1402/underworld140200575/25668343-%E9%9D%9E%E5%B8%B8%E3%81%AB%E9%95%B7%E3%81%84%E3%81%8F%E3%81%A1%E3%81%B0%E3%81%97%E3%82%92%E6%8C%81%E3%81%A4%E7%8F%8D%E3%81%97%E3%81%84%E3%83%94%E3%83%B3%E3%82%AF%E3%81%AE%E3%82%AA%E3%82%A6%E3%83%A0%E9%B3%A5.jpg");
        imgUrls.add("https://previews.123rf.com/images/sergioboccardo/sergioboccardo1503/sergioboccardo150300132/38731688-%E3%83%96%E3%83%A9%E3%82%B8%E3%83%AB%E3%81%AE%E9%BB%84%E8%89%B2%E7%9B%AE%E9%BB%92%E3%81%84%E3%81%8F%E3%81%A1%E3%81%B0%E3%81%97%E3%81%A8%E3%83%95%E3%83%A9%E3%83%9F%E3%83%B3%E3%82%B4.jpg");
        imgUrls.add("https://upload.wikimedia.org/wikipedia/commons/8/8e/American_Flamingo_-_Phoenicopterus_ruber.jpg");
        imgUrls.add("https://upload.wikimedia.org/wikipedia/commons/f/f9/Phoenicopterus_ruber_in_S%C3%A3o_Paulo_Zoo.jpg");
        imgUrls.add("https://previews.123rf.com/images/underworld/underworld1402/underworld140200575/25668343-%E9%9D%9E%E5%B8%B8%E3%81%AB%E9%95%B7%E3%81%84%E3%81%8F%E3%81%A1%E3%81%B0%E3%81%97%E3%82%92%E6%8C%81%E3%81%A4%E7%8F%8D%E3%81%97%E3%81%84%E3%83%94%E3%83%B3%E3%82%AF%E3%81%AE%E3%82%AA%E3%82%A6%E3%83%A0%E9%B3%A5.jpg");
        imgUrls.add("https://previews.123rf.com/images/sergioboccardo/sergioboccardo1503/sergioboccardo150300132/38731688-%E3%83%96%E3%83%A9%E3%82%B8%E3%83%AB%E3%81%AE%E9%BB%84%E8%89%B2%E7%9B%AE%E9%BB%92%E3%81%84%E3%81%8F%E3%81%A1%E3%81%B0%E3%81%97%E3%81%A8%E3%83%95%E3%83%A9%E3%83%9F%E3%83%B3%E3%82%B4.jpg");
        imgUrls.add("https://upload.wikimedia.org/wikipedia/commons/8/8e/American_Flamingo_-_Phoenicopterus_ruber.jpg");

        setupImageGreedView(imgUrls);


    }


}
