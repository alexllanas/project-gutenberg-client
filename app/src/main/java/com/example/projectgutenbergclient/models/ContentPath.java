package com.example.projectgutenbergclient.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//        foreignKeys = {
//        @ForeignKey(
//                entity = Book.class,
//                parentColumns = "book_id",
//                childColumns = "path_id", onDelete = ForeignKey.CASCADE)})
@Entity(tableName = "paths")
public class ContentPath implements Parcelable {

    /**
     * Class fields
     */
    @PrimaryKey
    @NonNull
    private long path_id;

    @ColumnInfo(name = "path")
    private String path;


    /**
     * Constructors
     */
    public ContentPath() {
    }

    @Ignore
    public ContentPath(long path_id, String path) {
        this.path_id = path_id;
        this.path = path;
    }

    @Ignore
    protected ContentPath(Parcel in) {
        path_id = in.readLong();
        path = in.readString();
    }


    /**
     * Getters/Setters
     */
    public long getPath_id() {
        return path_id;
    }

    public void setPath_id(long path_id) {
        this.path_id = path_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    /**
     * Parcelable Implementation
     */
    public static final Creator<ContentPath> CREATOR = new Creator<ContentPath>() {
        @Override
        public ContentPath createFromParcel(Parcel in) {
            return new ContentPath(in);
        }

        @Override
        public ContentPath[] newArray(int size) {
            return new ContentPath[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.path_id);
        dest.writeString(this.path);
    }
}
