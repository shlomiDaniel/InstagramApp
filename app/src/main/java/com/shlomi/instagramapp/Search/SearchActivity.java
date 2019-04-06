package com.shlomi.instagramapp.Search;

//import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.design.widget.*;

import com.shlomi.instagramapp.ButtonNavigationViewHelper;
import com.shlomi.instagramapp.R;

//import com.shlomi.ButtonNavigationViewHelper.ButtonNavigationViewHelper;

public class SearchActivity extends AppCompatActivity {
    private static String TAG = "Search_activity";
    private final int activity_num  = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toast.makeText(this,"search",Toast.LENGTH_SHORT).show();
        Log.d(TAG,"Started");
    }

    public void setupButonNavigation(){
        BottomNavigationView bn = (BottomNavigationView)findViewById(R.id.bottom_navigationViewBar);
       // ButtonNavigationViewHelper.setupButtonNavigationView(bn);
        // Intent intent2 = new Intent(ProfileActivity.this, Home.class);
        // startActivity(intent2);
        ButtonNavigationViewHelper.enableNavigation(SearchActivity.this,bn);

        Menu menu = bn.getMenu();
        MenuItem menuItem = menu.getItem(activity_num);
        menuItem.setChecked(true);
    }


}
