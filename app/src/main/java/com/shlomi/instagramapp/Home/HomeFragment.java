package com.shlomi.instagramapp.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shlomi.instagramapp.Models.Photo;
import com.shlomi.instagramapp.Profile.ViewPostActivity;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Utils.GridPhotoAdapter;
import com.shlomi.instagramapp.Utils.SignInActivity;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private DatabaseReference myRef;
    private ArrayList<Photo> photos = new ArrayList<>();
    private GridView gridView;
    private final int NUM_GRID_COLUMS = 3;
    private ArrayList<String> imgUrls = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        gridView = view.findViewById(R.id.homePhotoGrid);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), SignInActivity.class));
        }

        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("photos")
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()) {
                    Photo p = singleSnapShot.getValue(Photo.class);

                    if(!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(p.getUser_id())) {
                        photos.add(p);
                    }
                }

                int gridWidth = getResources().getDisplayMetrics().widthPixels;
                int imgWidth = gridWidth / NUM_GRID_COLUMS;
                gridView.setColumnWidth(imgWidth);

                GridPhotoAdapter adapter = new GridPhotoAdapter(getActivity(), R.layout.layaout_grid_imgview, "", photos);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(itemClickListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private ArrayList<Photo> getPhotos(){
        return this.photos;
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            final ArrayList<Photo> all_photos = getPhotos();
            final Photo photo = all_photos.get(position);

            Bundle bundle = new Bundle();
            bundle.putString("photo_id", photo.getPhoto_id());
            bundle.putString("user_id", photo.getUser_id());

            Intent intent  = new Intent(getActivity(), ViewPostActivity.class);
            intent.putExtra("photo_id", photo.getPhoto_id());
            intent.putExtra("user_id", photo.getUser_id());

            startActivity(intent);
        }
    };
}
