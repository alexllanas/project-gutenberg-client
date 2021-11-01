package com.example.simplebookwormapp.persistence;

import androidx.room.TypeConverter;

import com.example.simplebookwormapp.models.Author;
import com.example.simplebookwormapp.models.Formats;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {

    @TypeConverter
    public static String fromStringArrayList(ArrayList<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ArrayList<String> toStringArrayList(String value) {
        Type formatsType = new TypeToken<ArrayList<String>>(){}.getType();
        return new Gson().fromJson(value, formatsType);
    }

    @TypeConverter
    public static String fromFormats(Formats formats) {
        Gson gson = new Gson();
        String json = gson.toJson(formats);
        return json;
    }

    @TypeConverter
    public static Formats toFormats(String value) {
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
    public static String[] toStringArray(String value) {
        Type stringArrayType = new TypeToken<String[]>(){}.getType();
        return new Gson().fromJson(value, stringArrayType);
    }
}
