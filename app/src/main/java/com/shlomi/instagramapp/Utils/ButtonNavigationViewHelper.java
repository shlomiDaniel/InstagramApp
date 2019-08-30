package com.shlomi.instagramapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import com.shlomi.instagramapp.Home.Home;
import com.shlomi.instagramapp.Profile.ProfileActivity;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Search.SearchActivity;
import com.shlomi.instagramapp.Share.ShareActivity;

public class ButtonNavigationViewHelper {
    public static void enableNavigation(final Context context, BottomNavigationView bn) {
        bn.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_house: {
                        Intent intent = new Intent(context, Home.class);
                        context.startActivity(intent);
                    }
                    break;

                    case R.id.ic_alert: {
                        // Intent intent1 = new Intent(context,Home.class);
                        // context.startActivity(intent1);
                    }
                    break;

                    case R.id.ic_android: {
                        Intent intent2 = new Intent(context, ProfileActivity.class);
                        context.startActivity(intent2);
                    }
                    break;

                    case R.id.ic_circle: {
                        Intent intent3 = new Intent(context, ShareActivity.class);
                        context.startActivity(intent3);
                    }
                    break;

                    case R.id.ic_search: {
                        Intent intent4 = new Intent(context, SearchActivity.class);
                        context.startActivity(intent4);
                    }
                    break;
                }

                return false;
            }
        });
    }
}
