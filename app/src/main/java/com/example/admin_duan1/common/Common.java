package com.example.admin_duan1.common;

import com.example.admin_duan1.dto.Request;
import com.example.admin_duan1.dto.User;

import java.util.Calendar;
import java.util.Locale;

public class Common {
    public static User user;
    public static Request request;
    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final String CHAP_NHAN = "Chấp Thuận";
    public static final String HUY = "Hủy";

    //Lấy thời gian;
    public static String getTime(long time){
        Calendar calendar =     Calendar.getInstance(new Locale("vi","VN"));
        calendar.setTimeInMillis(time);
        StringBuilder date = new StringBuilder(android.text.format.DateFormat.format("dd-MM-yyyy HH:mm",calendar).toString());
        return date.toString();
    }
}
