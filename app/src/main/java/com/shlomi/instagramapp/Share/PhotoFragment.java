package com.shlomi.instagramapp.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shlomi.instagramapp.Profile.accountSettingsActivity;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Utils.Permissions;

public class PhotoFragment extends Fragment {
    private static final String TAG = "Home Fragment";
    private static final int photo_fragment_Num = 1;
    private static final int galley_fragment_num = 2;
    private static final int CAMERA_REQUEST_CODE = 5;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        Button openCameraBut = (Button) view.findViewById(R.id.btLaunchCamera);
        openCameraBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((ShareActivity) getActivity()).getCurrentTabNum() == photo_fragment_Num) {
                    if (((ShareActivity) getActivity()).checkPermissions(Permissions.CAMERA_PERMISSIONS[0])) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                    } else {

                        Intent intent = new Intent(getActivity(), ShareActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getContext().startActivity(intent);

                    }

                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            Bitmap bitmap;
            bitmap = (Bitmap) data.getExtras().get("data");
            if (isRoootTask()) {
                try {
                    Intent intent = new Intent(getActivity(), UploadPostActivity.class);
                    intent.putExtra(getString(R.string.selected_bitmap), bitmap);

                    startActivity(intent);
                } catch (NullPointerException e) {
                    Log.d("", "null pointer exeption");
                }

            } else {
                try {
                    Intent intent = new Intent(getActivity(), accountSettingsActivity.class);
                    intent.putExtra(getString(R.string.selected_bitmap), bitmap);
                    intent.putExtra("return_to_fragment", getString(R.string.edit_profile_fragment));

                    startActivity(intent);
                    getActivity().finish();
                } catch (NullPointerException e) {
                    Log.d("", "null pointer exeption");
                }
            }
        }
    }

    private boolean isRoootTask() {
        if (((ShareActivity) getActivity()).getTask() == 0) {
            return true;
        } else {
            return false;
        }
    }

}
