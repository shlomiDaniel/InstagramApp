package com.shlomi.instagramapp;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.signin.SignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView email;
    private TextView pass;
    private Button logOut;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        BottomNavigationView bn = (BottomNavigationView)findViewById(R.id.bottom_navigationViewBar);
//        //  enableNavigation(Home.this,bn);
//
//        ButtonNavigationViewHelper.enableNavigation(ProfileActivity.this,bn);
        setContentView(R.layout.layout_bottom_navogation);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        email = (TextView) findViewById(R.id.emailText);
        pass = (TextView) findViewById(R.id.passwordText);
        logOut = (Button)findViewById(R.id.logOut);
        logOut.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,SignInActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        email.setText("hello : " + user.getEmail());
    }

    @Override
    public void onClick(View v) {
       if(v == logOut){
           firebaseAuth.signOut();
           finish();
           startActivity(new Intent(this, SignInActivity.class));
       }
    }


}
