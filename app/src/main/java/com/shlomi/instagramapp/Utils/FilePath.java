package com.shlomi.instagramapp.Utils;

import android.os.Environment;

public class FilePath {
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public final String PICTURES = ROOT_DIR + "/Pictures";
    public final String CAMERA = ROOT_DIR + "/DCIM/Camera";
    public final String DOWNLOADS = ROOT_DIR + "/Download";
    public final String FIRE_BASE_IMAGE_STORAGE = "photos/users";
    // public String CAMERA = "/storage/emulated/0/DCIM/Camera/";
}
