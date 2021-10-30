package com.example.simplebookwormapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Author implements Parcelable {
    private String name;
    private String birth_year;
    private String death_year;

    protected Author(Parcel in) {
        name = in.readString();
        birth_year = in.readString();
        death_year = in.readString();
    }

    public static final Creator<Author> CREATOR = new Creator<Author>() {
        @Override
        public Author createFromParcel(Parcel in) {
            return new Author(in);
        }

        @Override
        public Author[] newArray(int size) {
            return new Author[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(birth_year);
        parcel.writeString(death_year);
    }
}
