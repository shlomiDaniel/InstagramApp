package com.shlomi.instagramapp.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SectionStatePagerAdapter extends FragmentPagerAdapter {

    public final List<Fragment> mfragmentList = new ArrayList<>();
    private final HashMap<Fragment,Integer> mfragment = new HashMap<>();
    private final HashMap<String,Integer> mfragmentNums = new HashMap<>();
    private final HashMap<Integer,String> mfragmentNames = new HashMap<>();

    public SectionStatePagerAdapter(FragmentManager fragment){
       super(fragment);
   }

    @Override
    public Fragment getItem(int pos) {
        return mfragmentList.get(pos);
    }

    @Override
    public int getCount() {
        return mfragmentList.size();
    }

    public void addFragment(Fragment fragment,String name){
        mfragmentList.add(fragment);
        mfragment.put(fragment,mfragmentList.size() -1);
        mfragmentNums.put(name,mfragmentList.size()-1);
        mfragmentNames.put(mfragmentList.size()-1,name);}

        public Integer getFragmentNum(String name){
        if(mfragmentNums.containsKey(name))
        {
            return mfragmentNums.get(name);
        }else{}
        return null;
        }

    public Integer getFragmentNum(Fragment fragment){
        if(mfragmentNums.containsKey(fragment))
        {
            return mfragmentNums.get(fragment);
        }else{
            return null;

        }

    }

    public String getFragmentName(Integer fragmentNum){
        if(mfragmentNames.containsKey(fragmentNum))
        {
          return mfragmentNames.get(fragmentNum);

        }else{
            return null;

        }

    }


}
