package com.shlomi.instagramapp.Utils;

import java.io.File;
import java.util.ArrayList;

public class FileSearch {
    public static ArrayList<String>getDirecotryPaths(String dir){
        ArrayList<String>pathArray = new ArrayList<>();
        File file  = new File(dir);
        File [] listFiles = file.listFiles();
        for(int i=0;i<listFiles.length;i++){
            if(listFiles[i].isDirectory()){
                pathArray.add(listFiles[i].getAbsolutePath());

            }

        }
        return pathArray;

    }
    public static ArrayList<String>getFilePath(String dir){
        ArrayList<String>pathArray = new ArrayList<>();
        File file  = new File(dir);
        File [] listFiles = file.listFiles();
        for(int i=0;i<listFiles.length;i++){
            if(listFiles[i].isFile()){
                pathArray.add(listFiles[i].getAbsolutePath());

            }

        }
        return pathArray;
    }



}
