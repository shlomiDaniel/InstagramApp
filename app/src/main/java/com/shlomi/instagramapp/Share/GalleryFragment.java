package com.shlomi.instagramapp.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.shlomi.instagramapp.Post.UploadPostActivity;
import com.shlomi.instagramapp.Profile.accountSettingsActivity;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Utils.FilePath;
import com.shlomi.instagramapp.Utils.FileSearch;
import com.shlomi.instagramapp.Utils.GridImageAdapter;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    private static final String TAG = "Home Fragment";
    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar progressBar;
    private Spinner spinner;
    private ArrayList<String> dir;
    private static final int NUM_GRID_COL = 3;
    private String selectedImage;
    private String mAppend = "file:/";

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryImage = (ImageView) view.findViewById(R.id.galleryImageView);
        gridView = (GridView) view.findViewById(R.id.gridView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        spinner = (Spinner) view.findViewById(R.id.spinner_diractory);
        progressBar.setVisibility(View.GONE);

        dir = new ArrayList<>();
        ImageView shareClose = (ImageView) view.findViewById(R.id.ivCloseShare);
        shareClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        TextView nextScreen = (TextView) view.findViewById(R.id.tvNext);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRoootTask()) {
                    Intent intent = new Intent(getActivity(), UploadPostActivity.class);
                    intent.putExtra(getString(R.string.selected_img), selectedImage);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), accountSettingsActivity.class);
                    intent.putExtra(getString(R.string.selected_img), selectedImage);
                    intent.putExtra("return_to_fragment", getString(R.string.edit_profile_fragment));
                    startActivity(intent);
                }
            }
        });

        init();
        return view;
    }

    private void init() {
        //check for other folder Inside "/storage/emulated/0/pictures"
        FilePath filePaths = new FilePath();
        if (FileSearch.getDirecotryPaths(filePaths.PICTURES) != null) {
            dir = FileSearch.getDirecotryPaths(filePaths.PICTURES);
        }
        ArrayList<String> dirNames = new ArrayList<>();
        for (int i = 0; i < dirNames.size(); i++) {
            int indx = dir.get(i).lastIndexOf("/");
            String str = dir.get(i).substring(indx);
            dirNames.add(str);
        }
        dir.add(filePaths.DOWNLOADS);
        dir.add(filePaths.CAMERA);
        dir.add(filePaths.PICTURES);

        ArrayList<String> list = new ArrayList<>();
        list.add("DOWNLOADS");
        list.add("Camera");
        list.add("PICTURES");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupGreedView(dir.get(position));
                Toast.makeText(getContext(), "Please Enter Email.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setupGreedView(String selectedDir) {
        final ArrayList<String> imgUrl = FileSearch.getFilePath(selectedDir);
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imgWidth = gridWidth / NUM_GRID_COL;

        gridView.setColumnWidth(imgWidth);
        final GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layaout_grid_imgview, mAppend, imgUrl);

        gridView.setAdapter(adapter);
        setImage(imgUrl.get(0), galleryImage, mAppend);
        selectedImage = imgUrl.get(0);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setImage(imgUrl.get(position), galleryImage, mAppend);
                selectedImage = imgUrl.get(position);
            }
        });
    }

    private void setImage(String imgUrl, ImageView imageView, String append) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(append + imgUrl, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean isRoootTask() {
        if (((ShareActivity) getActivity()).getTask() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
