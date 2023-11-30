package com.example.dcskeyboardhelper.util;

import androidx.room.TypeConverter;

import com.example.dcskeyboardhelper.model.bean.Action;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class KeyConverter {
    @TypeConverter
    public String objectToKey(List<Action> actions){
        return GsonInstance.getInstance().getGson().toJson(actions);
    }

    @TypeConverter
    public List<Action> keyToObject(String json){
        Type listType = new TypeToken<List<Action>>(){}.getType();
        return GsonInstance.getInstance().getGson().fromJson(json, listType);
    }
}
