package com.shlomi.instagramapp.Cache;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserEntityDao {
    @Query("SELECT * FROM userentity")
    UserEntity getUser();

    @Query("SELECT * FROM userentity WHERE id IN (:userIds)")
    List<UserEntity> getByIds(String[] userIds);

    @Query("DELETE FROM userentity")
    void deleteAll();

    @Insert
    void insertAll(UserEntity... users);

    @Delete
    void delete(UserEntity user);
}
