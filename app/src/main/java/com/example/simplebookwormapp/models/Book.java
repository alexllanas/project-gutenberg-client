package com.example.simplebookwormapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

@Entity(tableName = "books")
public class Book implements Parcelable {

    /**
     * Class fields
     */

    @PrimaryKey
    @NonNull
    private long book_id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "authors")
    private ArrayList<String> authors;

    @ColumnInfo(name = "subjects")
    private String[] subjects;

    @ColumnInfo(name = "formats")
    private Formats formats;

    @ColumnInfo(name = "timestamp")
    private int timestamp;


    /**
     * Class constructors
     */

    public Book() {
    }

    public Book(Parcel in) {
    }

    /**
     * The list of authors is constructed from an array of Author objects from the api response.
     *
     * @param book_id
     * @param title
     * @param authors
     * @param subjects
     * @param formats
     * @param timestamp
     */
    public Book(long book_id, String title, Author[] authors, String[] subjects, Formats formats, int timestamp) {
        this.book_id = book_id;
        this.title = title;
        for (Author author : authors) {
            this.authors.add(author.getName());
        }
        this.subjects = subjects;
        this.formats = formats;
        this.timestamp = timestamp;
    }


    /**
     * Getters and setters
     */

    public long getBook_id() {
        return book_id;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public String[] getSubjects() {
        return subjects;
    }

    public void setSubjects(String[] subjects) {
        this.subjects = subjects;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Formats getFormats() {
        return formats;
    }

    public void setFormats(Formats formats) {
        this.formats = formats;
    }


    /**
     * Parcelable implementation
     */

    public static Creator<Book> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(book_id);
        parcel.writeString(title);
        parcel.writeList(authors);
        parcel.writeParcelable(formats, i);
    }
}
