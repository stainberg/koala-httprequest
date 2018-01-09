package com.stainberg.koala.koalahttp;


import com.stainberg.koala.request.BaseRequest;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * Created by Stainberg on 7/9/15.
 */
class KoalaHttpUtils {

    static String getNameValuePair(Map<String, String> params) {
        StringBuilder StrArgs = null;
        Set<Map.Entry<String, String>> set = params.entrySet();
        for (Map.Entry<String, String> maEntry : set) {
            try {
                if (StrArgs == null) {
                    StrArgs = new StringBuilder(maEntry.getKey() + "=" + URLEncoder.encode(maEntry.getValue(), "UTF-8"));
                } else {
                    StrArgs.append(StrArgs = new StringBuilder("&" + maEntry.getKey() + "=" + URLEncoder.encode(maEntry.getValue(), "UTF-8")));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return StrArgs != null ? StrArgs.toString() : "";
    }

    static <T, K extends BaseRequest> Class parserClass(KoalaTaskListener<T, K> taskListener) {
        if(taskListener == null) {
            return null;
        }
        Type type = taskListener.getClass().getGenericSuperclass();
        if (type != null && type instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) type).getActualTypeArguments();
            Class cls = (Class) p[0];
            if(cls != null) {
                return cls;
            }
        }
        return null;
    }
}
