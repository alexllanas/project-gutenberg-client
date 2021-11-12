package com.example.simplebookwormapp.persistence;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.simplebookwormapp.models.ContentPath;

@Dao
public interface ContentPathDao {

    @Insert(onConflict = IGNORE)
    long[] insertContentPaths(ContentPath... contentPath);

    @Insert(onConflict = REPLACE)
    void insertContentPath(ContentPath contentPath);

    @Query("SELECT * FROM paths WHERE path_id = :book_id")
    LiveData<ContentPath> getPath(long book_id);


}
