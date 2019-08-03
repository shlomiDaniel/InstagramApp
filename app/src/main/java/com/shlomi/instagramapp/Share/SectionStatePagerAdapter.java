package com.shlomi.instagramapp.Share;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SectionStatePagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> flist = new ArrayList<>();
    private final HashMap<Fragment, Integer> mFragments = new HashMap<>();
    private final HashMap<String, Integer> mFragmentsNum = new HashMap<>();
    private final HashMap<Integer, String> mFragmentsNames = new HashMap<>();

    public SectionStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        return flist.get(pos);
    }

    @Override
    public int getCount() {
        return flist.size();
    }

    public void addFragments(Fragment fragment, String fragment_name) {
        flist.add(fragment);
        mFragments.put(fragment, flist.size() - 1);
        mFragmentsNum.put(fragment_name, flist.size() - 1);
        mFragmentsNames.put(flist.size() - 1, fragment_name);
    }

    public Integer getFragmentNumber(String fragmentName) {
        if (mFragmentsNum.containsKey(fragmentName)) {
            return mFragmentsNum.get(fragmentName);
        } else {
            return null;
        }
    }

    public Integer getFragmentNumber(Fragment fragment) {
        if (mFragmentsNum.containsKey(fragment)) {
            return mFragmentsNum.get(fragment);
        } else {
            return null;
        }
    }

    public String getFragmentName(Integer fragmentNumber) {

        if (mFragmentsNames.containsKey(fragmentNumber)) {
            return mFragmentsNames.get(fragmentNumber);
        } else {
            return null;
        }
    }
}
