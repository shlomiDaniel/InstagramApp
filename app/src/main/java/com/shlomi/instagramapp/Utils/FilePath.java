package com.shlomi.instagramapp.Utils;

import android.os.Environment;

public class FilePath {
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public String PICTURES = ROOT_DIR + "/Pictures";
    public String CAMERA = ROOT_DIR + "/DCIM/Camera";
    public String DOWNLOADS = ROOT_DIR + "/Download";
    public String FIRE_BASE_IMAGE_STORAGE = "photos/users";
   // public String CAMERA = "/storage/emulated/0/DCIM/Camera/";



}
