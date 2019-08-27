package com.shlomi.instagramapp.Profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shlomi.instagramapp.Models.Photo;
import com.shlomi.instagramapp.R;

public class ViewCommentsFragment extends Fragment {
    private static final String TAG = "View Comments Fragment";

//    public ViewCommentsFragment(){
//        super();
//        setArguments(new Bundle());
//    }

    //private Photo mphoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fregment_new_comment,container,false);

//    try{
//
//
//        mphoto = getPhotoFromBundle();
//    }catch (NullPointerException ex){}

        return view;
    }


    private Photo getPhotoFromBundle(){
        Bundle bundle = this.getArguments();
        if(bundle!=null){
            return bundle.getParcelable("photo");
        }else{
            return null;
        }
    }
}
