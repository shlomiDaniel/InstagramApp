package com.shlomi.instagramapp.Home;

import android.print.PrinterId;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {
    private static String TAG = "pager_adapter";
    private final List<Fragment> list = new ArrayList<>();


    public PagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
       return list.get(pos);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void addFragment(Fragment fragment){
        list.add(fragment);

    }


}
