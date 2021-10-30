package com.example.simplebookwormapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Author implements Parcelable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(String birth_year) {
        this.birth_year = birth_year;
    }

    public String getDeath_year() {
        return death_year;
    }

    public void setDeath_year(String death_year) {
        this.death_year = death_year;
    }

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
