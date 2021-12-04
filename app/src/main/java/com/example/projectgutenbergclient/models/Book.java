package com.example.projectgutenbergclient.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "books")
public class Book implements Parcelable {

    /**
     * Class fields
     */
    @SerializedName("id")
    @PrimaryKey
    @NonNull
    private long id;

    @ColumnInfo(name = "title")
    @NonNull
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

    @Ignore
    public Book(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.authors = book.getAuthors();
        this.subjects = book.getSubjects();
        this.formats = book.getFormats();
        this.timestamp = book.getTimestamp();
    }

    @Ignore
    public Book(Parcel in) {
        id = in.readLong();
        title = in.readString();
        authors = new ArrayList<>();
        in.readList(authors, Book.class.getClassLoader());
        subjects = new ArrayList<>();
        in.readStringList(subjects);
        formats = in.readParcelable(Book.class.getClassLoader());
        timestamp = in.readLong();
    }

    /**
     * The list of authors is constructed from an array of Author objects from the api response.
     *
     * @param id
     * @param title
     * @param authors
     * @param subjects
     * @param formats
     * @param timestamp
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Ignore
    public Book(long id, String title, List<Author> authors, ArrayList<String> subjects, Formats formats) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.subjects = subjects;
        this.formats = formats;
        this.timestamp = new Date().getTime();
    }

    public Book(long id, String title, List<Author> authors, ArrayList<String> subjects, Formats formats, long timestamp) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.subjects = subjects;
        this.formats = formats;
        this.timestamp = timestamp;
    }

    private void resizeImageUrl(Formats format) {
        String imageUrl = format.getImage_jpeg();
        if (imageUrl != null) {
            if (imageUrl.contains("small")) {
                imageUrl = imageUrl.replace("small", "medium");
            }
        }
        if (imageUrl == null || TextUtils.isEmpty(imageUrl)) {
//            resizedImageUrl = "android.resource://com.example.projectgutenbergclient/drawable/white_background.png";
            imageUrl = "https://via.placeholder.com/150";
        }
        format.setImage_jpeg(imageUrl);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Author> reorderedAuthorNames(List<Author> authors) {
        ArrayList<Author> reordered = (ArrayList<Author>) authors;

        reordered.forEach(author -> {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Formats getFormats() {
        return formats;
    }

    public void setFormats(Formats format) {
        resizeImageUrl(format);
        changeFilePrefixes(format);

        this.formats = format;
    }

    private void changeFilePrefixes(Formats format) {
        String ascii = format.getText_plain_ascii();
        String utf8 = format.getText_plain_utf_8();
        if (ascii != null && ascii.endsWith("zip")) {
            format.setText_plain_ascii(ascii.replace("zip", "txt"));
        }
        if (utf8 != null && utf8.endsWith("zip")) {
            format.setText_plain_utf_8(utf8.replace("zip", "txt"));
        }
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
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeList(authors);
        parcel.writeParcelable(formats, i);
        parcel.writeLong(timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && timestamp == book.timestamp && Objects.equals(title, book.title) && Objects.equals(authors, book.authors) && Objects.equals(subjects, book.subjects) && Objects.equals(formats, book.formats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, authors, subjects, formats, timestamp);
    }
}
