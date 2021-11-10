package com.example.simplebookwormapp.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

@Entity(tableName = "books")
public class Book implements Parcelable {

    /**
     * Class fields
     */

    @SerializedName("id")
    @PrimaryKey
    @NonNull
    private long book_id;

    @ColumnInfo(name = "title")
    private String title;

    @SerializedName("authors")
    @ColumnInfo(name = "authors")
    private List<Author> authors;

    @ColumnInfo(name = "subjects")
    private ArrayList<String> subjects;

    @ColumnInfo(name = "formats")
    private Formats formats;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

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
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Book(long book_id, String title, List<Author> authors, ArrayList<String> subjects, Formats formats) {
        this.book_id = book_id;
        this.title = title;
        this.authors = authors;
        this.subjects = subjects;
        this.formats = formats;
        this.timestamp = new Date().getTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Author> reorderedAuthorNames(List<Author> authors) {
        ArrayList<Author> reordered = (ArrayList<Author>) authors;

        reordered.forEach(author -> {
            Timber.d(author.getName());
            String[] firstLast = author.getName().split(",");
            if (firstLast.length == 2) {
                author.setName(firstLast[1] + " " + firstLast[0]);
            }
        });
        return reordered;
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

    public List<Author> getAuthors() {
        return authors;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setAuthors(List<Author> authors) {
        this.authors = reorderedAuthorNames(authors);
    }

    public ArrayList<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<String> subjects) {
        this.subjects = subjects;
    }

    public long getTimestamp() {
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
        parcel.writeLong(timestamp);
    }
}
