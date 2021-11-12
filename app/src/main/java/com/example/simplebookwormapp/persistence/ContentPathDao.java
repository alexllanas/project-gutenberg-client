package com.example.simplebookwormapp.persistence;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ContentPathDao {

    @Insert(onConflict = IGNORE)
    long[] insertContentPaths(String... paths);

    @Insert(onConflict = REPLACE)
    void insertContentPath(String path);

//    @Query("SELECT * FROM ")


}
