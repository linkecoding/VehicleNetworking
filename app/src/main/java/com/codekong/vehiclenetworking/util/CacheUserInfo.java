package com.codekong.vehiclenetworking.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.codekong.vehiclenetworking.bean.User;
import com.codekong.vehiclenetworking.config.Config;


public class CacheUserInfo {

    public static User getUser(Context context){
        String username = context.getSharedPreferences(Config.APP_ID, Context.MODE_PRIVATE).getString("username", null);
        String password = context.getSharedPreferences(Config.APP_ID, Context.MODE_PRIVATE).getString("password", null);
        String telnum = context.getSharedPreferences(Config.APP_ID, Context.MODE_PRIVATE).getString("telnum", null);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setTelnum(telnum);
        return user;
    }

    public static boolean isSaved(Context context){
        return context.getSharedPreferences(Config.APP_ID, Context.MODE_PRIVATE).getBoolean("savepassword", false);
    }

    public static void cacheData(Context context, String key, String value){
        SharedPreferences.Editor e = context.getSharedPreferences(Config.APP_ID, Context.MODE_PRIVATE).edit();
        e.putString(key, value);
        e.commit();
    }

    public static void cacheData(Context context, String key, boolean value){
        SharedPreferences.Editor e = context.getSharedPreferences(Config.APP_ID, Context.MODE_PRIVATE).edit();
        e.putBoolean(key, value);
        e.commit();
    }

}
