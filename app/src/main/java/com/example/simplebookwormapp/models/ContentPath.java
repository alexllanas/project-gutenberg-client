package com.example.simplebookwormapp.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.security.PrivateKey;

@Entity(tableName = "paths", foreignKeys = {
        @ForeignKey(
                entity = Book.class,
                parentColumns = "book_id",
                childColumns = "book_id", onDelete = ForeignKey.CASCADE)})
public class ContentPath {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long path_id;

    @ColumnInfo(name = "path")
    private String path;

    @ColumnInfo(name = "book_id")
    private long book_id;
}
