package com.shlomi.instagramapp.Profile;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.signin.SignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.SignInActivity;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class ProfileActivity extends AppCompatActivity  {

    private TextView email;
    private TextView pass;
    private Button logOut;
    private FirebaseAuth firebaseAuth;
   private static  String TAG = "Profile_activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.layout_bottom_navogation);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        }
        setupToolBar();
         setTitle("                                                ");
        FirebaseUser user = firebaseAuth.getCurrentUser();

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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


}
