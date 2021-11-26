package com.example.projectgutenbergclient.persistence;

import androidx.room.TypeConverter;

import com.example.projectgutenbergclient.models.Author;
import com.example.projectgutenbergclient.models.ContentPath;
import com.example.projectgutenbergclient.models.Formats;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converters {

    @TypeConverter
    public static String fromAuthorList(List<Author> authors) {
        Gson gson = new Gson();
        String json = gson.toJson(authors);
        return json;    }

    @TypeConverter
    public static List<Author> toAuthorList(String authors) {
        Type entityType = new TypeToken<List<Author>>() {
        }.getType();
        return new Gson().fromJson(authors, entityType);

    }

    @TypeConverter
    public static String fromStringArrayList(ArrayList<String> list) {
        return getJson(list);
    }

    @TypeConverter
    public static ArrayList<String> toStringArrayList(String json) {
        Type entityType = new TypeToken<ArrayList<String>>() {
        }.getType();
        return new Gson().fromJson(json, entityType);    }

    @TypeConverter
    public static String fromFormats(Formats formats) {
        return getJson(formats);
    }

    @TypeConverter
    public static Formats toFormats(String json) {
        Type entityType = new TypeToken<Formats>() {
        }.getType();
        return new Gson().fromJson(json, entityType);    }

    @TypeConverter
    public static String fromStringArray(String[] stringArray) {
        return getJson(stringArray);
    }

    @TypeConverter
    public static String[] toStringArray(String json) {
        Type entityType = new TypeToken<String[]>() {
        }.getType();
        return new Gson().fromJson(json, entityType);
    }

    /**
     * ContentPath converters
     */
    public static String fromContentPath(ContentPath contentPath) {
        return getJson(contentPath);
    }

    public static ContentPath toContentPath(String json) {
        Type entityType = new TypeToken<ContentPath>() {
        }.getType();
        return new Gson().fromJson(json, entityType);
    }


    /**
     * Get a JSON representation of an entity object as a String.
     *
     * @param entityObject
     * @param <EntityObject>
     * @return
     */
    public static <EntityObject> String getJson(EntityObject entityObject) {
        Gson gson = new Gson();
        String json = gson.toJson(entityObject);
        return json;
    }
}
