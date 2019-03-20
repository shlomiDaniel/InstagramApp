package com.shlomi.instagramapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Home extends AppCompatActivity {

    private static String TAG = "Home_Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG,"Started");
    }
    private void setupNevigationView(){

    }
}
