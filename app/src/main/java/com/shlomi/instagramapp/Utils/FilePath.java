package com.shlomi.instagramapp.Utils;

import android.os.Environment;

public class FilePath {
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();

    public String PICTURES = ROOT_DIR + "/DCIM/Pictures";
    public String CAMERA = ROOT_DIR + "/DCIM/camera";



}
