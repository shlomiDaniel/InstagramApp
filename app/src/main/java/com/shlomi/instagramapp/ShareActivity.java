package com.shlomi.instagramapp;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ShareActivity extends AppCompatActivity {
    private static String TAG = "shareActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG,"started");
        setupNevigationView();
    }

    private void setupNevigationView(){
      //  BottomNavigationView bn = (BottomNavigationView)findViewById(R.id.bottom_navigationViewBar);
      //  ButtonNavigationViewHelper.enableNavigation(ShareActivity.this,bn);
    }
}
