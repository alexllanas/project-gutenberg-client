package com.example.simplebookwormapp.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.simplebookwormapp.models.Book;
import com.example.simplebookwormapp.models.ContentPath;

@Database(entities = {Book.class, ContentPath.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class BookDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "book_db";

    private static BookDatabase INSTANCE;

    public static BookDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    BookDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return INSTANCE;
    }

    public abstract BookDao getBookDao();

    public abstract  ContentPathDao getContentPathDao();

}
