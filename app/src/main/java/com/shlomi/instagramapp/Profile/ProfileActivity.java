package com.shlomi.instagramapp.Profile;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
//
//        BottomNavigationView bn = (BottomNavigationView)findViewById(R.id.bottom_navigationViewBar);
//        //  enableNavigation(Home.this,bn);
//
//        ButtonNavigationViewHelper.enableNavigation(ProfileActivity.this,bn);
        setContentView(R.layout.layout_bottom_navogation);
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();

        setContentView(R.layout.activity_profile);
        //setContentView(R.layout.textmenu);
        //email = (TextView) findViewById(R.id.emailText);
       // pass = (TextView) findViewById(R.id.passwordText);
      //  logOut = (Button)findViewById(R.id.logOut);
       // logOut.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        }
        setupToolBar();
         setTitle("                                                ");
        FirebaseUser user = firebaseAuth.getCurrentUser();

      //  email.setText("hello : " + user.getEmail());
    }

//    @Override
//    public void onClick(View v) {
//       if(v == logOut){
//           firebaseAuth.signOut();
//           finish();
//           startActivity(new Intent(this, SignInActivity.class));
//       }
//    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupToolBar(){

       // Toolbar toolbar = (Toolbar)findViewById(R.id.profileToolBar);
        Toolbar toolbar = (Toolbar)findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Log.d(TAG,"onmenu click : click menu item : "+ menuItem);
                switch (menuItem.getItemId()){
                    case R.id.profileMenu:
                        Log.d(TAG,"navigation");

                }

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu,menu);
       getMenuInflater().inflate(R.menu.profile_menu,menu);

        return true;
    }
}
