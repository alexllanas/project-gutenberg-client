package com.example.simplebookwormapp.persistence;

import androidx.room.TypeConverter;

import com.example.simplebookwormapp.models.Author;
import com.example.simplebookwormapp.models.Formats;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Converters {

    @TypeConverter
    public static String fromAuthorArray(Author[] authors) {
        Gson gson = new Gson();
        String json = gson.toJson(authors);
        return json;
    }

    @TypeConverter
    public static Author[] fromAuthorArrayString(String value) {
        Type authorType = new TypeToken<Author[]>(){}.getType();
        return new Gson().fromJson(value, authorType);
    }

    @TypeConverter
    public static String fromFormats(Formats formats) {
        Gson gson = new Gson();
        String json = gson.toJson(formats);
        return json;
    }

    @TypeConverter
    public static Formats fromFormatsString(String value) {
        Type formatsType = new TypeToken<Formats>(){}.getType();
        return new Gson().fromJson(value, formatsType);
    }


    @TypeConverter
    public static String fromStringArray(String[] stringArray) {
        Gson gson = new Gson();
        String json = gson.toJson(stringArray);
        return json;
    }

    @TypeConverter
    public static String[] fromString(String value) {
        Type stringArrayType = new TypeToken<String[]>(){}.getType();
        return new Gson().fromJson(value, stringArrayType);
    }
}
