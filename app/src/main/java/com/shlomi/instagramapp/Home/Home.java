package com.shlomi.instagramapp.Home;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.shlomi.instagramapp.Utils.ButtonNavigationViewHelper;
import com.shlomi.instagramapp.R;

public class Home extends AppCompatActivity {

    private static String TAG = "Home_Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG,"Started");
         BottomNavigationView bn = (BottomNavigationView)findViewById(R.id.bottom_navigationViewBar);
          ButtonNavigationViewHelper.enableNavigation(Home.this,bn);
      setupViewPager();
    }

      private void setupViewPager(){
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CameraFragment());
        adapter.addFragment(new MessagesFragment());
        adapter.addFragment(new HomeFragment());
        ViewPager viewPager = (ViewPager)findViewById(R.id.container);
        viewPager.setAdapter(adapter);

          TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
          tabLayout.setupWithViewPager(viewPager);
          tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
          tabLayout.getTabAt(1).setIcon(R.drawable.ic_instagram_icon);
          tabLayout.getTabAt(2).setIcon(R.drawable.ic_sendmessage);      }

}
