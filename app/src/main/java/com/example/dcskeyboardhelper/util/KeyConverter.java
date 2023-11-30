package com.example.dcskeyboardhelper.util;

import androidx.room.TypeConverter;

import com.example.dcskeyboardhelper.model.bean.Key;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class KeyConverter {
    @TypeConverter
    public String objectToKey(List<Key> keys){
        return GsonInstance.getInstance().getGson().toJson(keys);
    }

    @TypeConverter
    public List<Key> keyToObject(String json){
        Type listType = new TypeToken<List<Key>>(){}.getType();
        return GsonInstance.getInstance().getGson().fromJson(json, listType);
    }
}
