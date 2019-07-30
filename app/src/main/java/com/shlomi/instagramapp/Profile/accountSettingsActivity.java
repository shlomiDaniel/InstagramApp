package com.shlomi.instagramapp.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.shlomi.instagramapp.Firebase.ModelFirebase;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Share.SectionStatePagerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class accountSettingsActivity extends AppCompatActivity {

    private Context mcontext;
    private SectionStatePagerAdapter pagerAdapter;
    private ViewPager vp;
    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount_setting);
        vp = findViewById(R.id.container);
        rl = findViewById(R.id.relayout1);

        setupSetting();
        ImageView imageView = (ImageView) findViewById(R.id.profile_menu);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupFragments();
        getIncomingIntant();
    }

    @Override
    protected void onDestroy() {
        Intent current_intent = getIntent();
        if (current_intent.hasExtra("back_to_post")) {
            Bundle data = getIntent().getExtras();
            final String photo_id = data.getString("photo_id");
            final String user_id = data.getString("user_id");

            Intent intent  = new Intent(accountSettingsActivity.this, ViewPostActivity.class);
            intent.putExtra("photo_id", photo_id);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        }

        super.onDestroy();
    }

    private void getIncomingIntant() {
        Intent intent = getIntent();

        if (intent.hasExtra(getString(R.string.selected_img)) || intent.hasExtra(getString(R.string.selected_bitmap))) {
            if (intent.getStringExtra("return_to_fragment").equals("Edit Profile")) {
                if (intent.hasExtra(getString(R.string.selected_img))) {
                    ModelFirebase modelFirebase = new ModelFirebase(accountSettingsActivity.this);
                    modelFirebase.uploadNewPhoto("profile_photo", null, 0, intent.getStringExtra(getString(R.string.selected_img)), null);

                } else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
                    ModelFirebase modelFirebase = new ModelFirebase(accountSettingsActivity.this);
                    modelFirebase.uploadNewPhoto("profile_photo", null, 0,null, (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap)));
                }
            }
        }

        if (intent.hasExtra(getString(R.string.calling_activity))) {
            setViewPager(pagerAdapter.getFragmentNumber(getString(R.string.edit_profile_fragment)));
        }
    }

    private void setViewPager(int fragmentNum) {
        rl.setVisibility(View.GONE);
        vp.setAdapter(pagerAdapter);
        vp.setCurrentItem(fragmentNum);
    }

    private void setupFragments() {
        pagerAdapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragments(new EditProfileFragment(), getString(R.string.editProfile));
        pagerAdapter.addFragments(new SignOutFragment(), getString(R.string.sign_out));
    }

    private void setupSetting() {

        ListView list = (ListView) findViewById(R.id.idacountsetting);

        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.editProfile));
        options.add(getString(R.string.sign_out));

        Context context = getApplicationContext();

        ArrayAdapter adapter = new ArrayAdapter(accountSettingsActivity.this, android.R.layout.simple_list_item_1, options);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            setViewPager(position);
            }
        });
    }
}
