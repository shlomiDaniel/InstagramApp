package com.shlomi.instagramapp.Cache;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {UserEntity.class, PhotoEntity.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserEntityDao users();
    public abstract PhotoEntityDao photos();
}