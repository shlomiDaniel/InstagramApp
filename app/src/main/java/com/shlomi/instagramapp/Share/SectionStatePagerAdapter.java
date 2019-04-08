package com.shlomi.instagramapp.Share;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SectionStatePagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> flist = new ArrayList<>();
    private final HashMap<Fragment,Integer> mfragments = new HashMap<>();
    private final HashMap<String,Integer> mfragmentsNum = new HashMap<>();
    private final  HashMap<Integer,String>mfragmentsNames = new HashMap<>();

    public SectionStatePagerAdapter(FragmentManager fm){
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

    public  void addFragments(Fragment fragment,String fragment_name){
        flist.add(fragment);
        mfragments.put(fragment,flist.size()-1);
        mfragmentsNum.put(fragment_name,flist.size()-1);
        mfragmentsNames.put(flist.size()-1,fragment_name);

    }

    public Integer getFragmentNumber(String fragmentName){
        if(mfragmentsNum.containsKey(fragmentName)){
            return mfragmentsNum.get(fragmentName);
        }
            else
            {
                return null;
            }

    }
    public Integer getFragmentNumber(Fragment fragment){
        if(mfragmentsNum.containsKey(fragment)){
            return mfragmentsNum.get(fragment);
        }
        else
        {
            return null;
        }

    }
    public String getFragmentName(Integer fragmentNumber){

        if(mfragmentsNames.containsKey(fragmentNumber)){
            return mfragmentsNames.get(fragmentNumber);
        }else
        {
            return null;
        }

    }


}
