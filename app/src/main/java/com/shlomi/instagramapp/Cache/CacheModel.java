package com.shlomi.instagramapp.Cache;

import android.arch.persistence.room.Room;
import android.content.Context;

public class CacheModel {
    private AppDatabase db;

    public CacheModel(Context context){
        this.db = Room.databaseBuilder(context, AppDatabase.class, "instagram-db").allowMainThreadQueries().build();
    }

    public AppDatabase getDb() {
        return db;
    }
}
