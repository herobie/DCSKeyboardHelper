package com.example.dcskeyboardhelper.util;

import androidx.room.TypeConverter;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StringConverter {
    @TypeConverter
    public String objectToString(List<String> strings){
        return GsonInstance.getInstance().getGson().toJson(strings);
    }

    @TypeConverter
    public List<String> stringToObject(String json){
        Type listType = new TypeToken<List<String>>(){}.getType();
        return GsonInstance.getInstance().getGson().fromJson(json, listType);
    }
}
