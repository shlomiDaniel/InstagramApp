package com.shlomi.instagramapp.Share;

import android.content.pm.PackageManager;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.Gallery;
import android.widget.TableLayout;

import com.shlomi.instagramapp.Home.PagerAdapter;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Utils.Permissions;

public class ShareActivity extends AppCompatActivity {
    private static String TAG = "shareActivity";
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG,"started");
        setupNevigationView();

        if(checkPermissionArray(Permissions.PERMISSIONS )){

        }else{
            verfyPermission(Permissions.PERMISSIONS);
        }
        setupViewPager();
    }

    private void setupViewPager(){
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());

        viewPager = (ViewPager)findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        TabLayout tableLayout = (TabLayout)findViewById(R.id.tabsBottom);
        tableLayout.setupWithViewPager(viewPager);
        tableLayout.getTabAt(0).setText("Gallery");
        tableLayout.getTabAt(1).setText("Photo");

    }

    public int getCurrentTabNum(){
        return viewPager.getCurrentItem();
    }


    public boolean checkPermissionArray(String [] arr){

        for(int i=0;i<arr.length;i++){
            String check = arr[i];
            if(!checkPermissions(check)){
                return false;
            }

        }
        return  true;
    }

    public boolean checkPermissions(String permission){
        int per = ActivityCompat.checkSelfPermission(ShareActivity.this,permission);
        if(per!= PackageManager.PERMISSION_GRANTED){
            return false;
        }else
        {
            return true;
        }
    }

    public  void verfyPermission(String [] arr){
        ActivityCompat.requestPermissions(
                ShareActivity.this,
                arr,
                1

        );

    }

    private void setupNevigationView(){
      //  BottomNavigationView bn = (BottomNavigationView)findViewById(R.id.bottom_navigationViewBar);
      //  ButtonNavigationViewHelper.enableNavigation(ShareActivity.this,bn);
    }
}
