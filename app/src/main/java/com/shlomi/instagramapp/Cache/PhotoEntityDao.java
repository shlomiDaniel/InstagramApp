package com.shlomi.instagramapp.Cache;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PhotoEntityDao {
    @Query("SELECT * FROM photoentity")
    List<PhotoEntity> getAll();

    @Query("SELECT * FROM photoentity WHERE photo_id IN (:photosIds)")
    List<PhotoEntity> getByIds(String[] photosIds);

    @Query("SELECT * FROM photoentity WHERE photo_id = (:photoId)")
    PhotoEntity getById(String photoId);

    @Query("DELETE FROM photoentity")
    void deleteAll();

    @Insert
    void insertAll(PhotoEntity... users);

    @Delete
    void delete(PhotoEntity user);
}
