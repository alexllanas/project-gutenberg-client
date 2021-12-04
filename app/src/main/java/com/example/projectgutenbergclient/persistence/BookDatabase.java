package com.example.projectgutenbergclient.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.projectgutenbergclient.models.Book;
import com.example.projectgutenbergclient.models.ContentPath;

@Database(entities = {Book.class, ContentPath.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class BookDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "book_db";

    public abstract BookDao getBookDao();

    public abstract ContentPathDao getContentPathDao();

}
