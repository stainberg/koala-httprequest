package com.stainberg.koala.request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stainberg on 9/14/17.
 */

public class RequestConfig {

    private static RequestConfig config = null;
    Map<String, String> headers;
    Map<String, String> params;

    private RequestConfig() {
        headers = new HashMap<>();
        params = new HashMap<>();
    }

    static RequestConfig getConfig() {
        if(config == null) {
            synchronized (RequestConfig.class) {
                if(config == null) {
                    config = new RequestConfig();
                }
            }
        }
        return config;
    }

    public static void AddFixHeader(String key, String val) {
        getConfig().headers.put(key, val);
    }

    public static void AddFixParams(String key, String val) {
        getConfig().params.put(key, val);
    }

}
