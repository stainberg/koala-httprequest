package com.stainberg.koala.koalahttp;

import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * Created by Stainberg on 7/6/15.
 */
class KoalaGson {

    private static Gson gson = new Gson();

    synchronized static <T> T fromJson(Class<T> cls, String srcStr) {
        T result;
        if(TextUtils.isEmpty(srcStr)) {
            return null;
        }
        try {
            result = gson.fromJson(srcStr, cls);
        } catch (Exception e) {
        	e.printStackTrace();
            result = null;
        }
        return result;
    }

    synchronized static String toJson(Object object) {
        return gson.toJson(object);
    }
}
