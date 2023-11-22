package com.example.dcskeyboardhelper.util;

import androidx.room.TypeConverter;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class IntegerConverter {
    @TypeConverter
    public String objectToInteger(List<Integer> integers){
        return GsonInstance.getInstance().getGson().toJson(integers);
    }

    @TypeConverter
    public List<Integer> integerToObject(String json){
        Type listType = new TypeToken<List<Integer>>(){}.getType();
        return GsonInstance.getInstance().getGson().fromJson(json, listType);
    }
}
