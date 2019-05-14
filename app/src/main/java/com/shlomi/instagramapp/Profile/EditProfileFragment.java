package com.shlomi.instagramapp.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shlomi.instagramapp.Utils.UniversalImageLoader;
import com.shlomi.instagramapp.R;

public class EditProfileFragment extends Fragment {

private ImageView profilePhoto;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile,container,false);

        profilePhoto = (ImageView)view.findViewById(R.id.profile_photo);
       // initImageLoader();
        setProfileImage();
        ImageView backArrow = (ImageView)view.findViewById(R.id.profile_menu);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //getActivity().finish();

//                Intent intent = new Intent(getActivity(), accountSettingsActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                getActivity().finish();

            }
        });



        return view;
    }

    private void setProfileImage(){
        Log.d("Edit Profile Fragment","setting profile image");
        String imageUrl = "http://www.teknoustam.com/wp-content/uploads/2018/06/android.png";
        UniversalImageLoader.setIamge(imageUrl,profilePhoto,null,"");
    }

//    private  void initImageLoader(){
//        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getActivity());
//        ImageLoader.getInstance().init(universalImageLoader.getConfig());
//    }


}


