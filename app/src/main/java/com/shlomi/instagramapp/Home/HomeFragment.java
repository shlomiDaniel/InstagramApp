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
import com.shlomi.instagramapp.Cache.CacheModel;
import com.shlomi.instagramapp.Cache.PhotoEntity;
import com.shlomi.instagramapp.Models.Photo;
import com.shlomi.instagramapp.Post.ViewPostActivity;
import com.shlomi.instagramapp.Profile.ProfileActivity;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Utils.GridPhotoAdapter;
import com.shlomi.instagramapp.Utils.InetConnection;
import com.shlomi.instagramapp.Utils.SignInActivity;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private DatabaseReference myRef;
    private ArrayList<Photo> photos = new ArrayList<>();
    private GridView gridView;
    private final int NUM_GRID_COLUMS = 3;
    private ArrayList<String> imgUrls = new ArrayList<>();
    private CacheModel appCache;
    private InetConnection inet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        gridView = view.findViewById(R.id.homePhotoGrid);
        appCache = new CacheModel(this.getContext());
        inet = new InetConnection(1000);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), SignInActivity.class));
        }else{
            if(inet.isInternetAvailable()) {
                showPosts();
            }else{
                showPostsFromCache();
            }
        }

        return view;
    }

    private void showPostsFromCache(){
        //load from cache
        List<PhotoEntity> cachedPhotos = appCache.getDb().photos().getAll();

        for(PhotoEntity p : cachedPhotos){
            if(!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(p.getUser_id())) {
                Photo photo = new Photo(p.getCaption(), p.getDate(), p.getImage(), p.getPhoto_id(), p.getUser_id(), p.getTags());
                photos.add(photo);
            }
        }

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imgWidth = gridWidth / NUM_GRID_COLUMS;
        gridView.setColumnWidth(imgWidth);

        GridPhotoAdapter adapter = new GridPhotoAdapter(getActivity(), R.layout.layaout_grid_imgview, "", photos);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(itemClickListener);
    }

    private void showPosts(){
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("photos")
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()) {
                    Photo p = singleSnapShot.getValue(Photo.class);

                    if(!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(p.getUser_id())) {
                        photos.add(p);

                        // cache user photos
                        if(appCache.getDb().photos().getById(p.getPhoto_id()) == null){
                            PhotoEntity photo_entity = new PhotoEntity(p.getPhoto_id(), p.getCaption(), p.getDate_created(), p.getImage_path(), p.getUser_id(),p.getTags());
                            appCache.getDb().photos().insertAll(photo_entity);
                        }
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
