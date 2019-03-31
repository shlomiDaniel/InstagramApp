package com.shlomi.instagramapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    private static String TAG = "Home_Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG,"Started");
         BottomNavigationView bn = (BottomNavigationView)findViewById(R.id.bottom_navigationViewBar);
        //  enableNavigation(Home.this,bn);

          ButtonNavigationViewHelper.enableNavigation(Home.this,bn);
       // enableNavigation();
    }



}
