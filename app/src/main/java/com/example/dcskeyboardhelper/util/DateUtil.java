package com.example.dcskeyboardhelper.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    //long转为日期
    public static String longToDate(long lo){
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return sd.format(date);
    }
}
