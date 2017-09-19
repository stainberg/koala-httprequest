package com.stainberg.koala.request;

import android.util.SparseArray;

import com.stainberg.koala.koalahttp.OnHttpCodeMsgListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stainberg on 9/14/17.
 */

public class RequestConfig {

    private static RequestConfig config = null;
    private SparseArray<OnHttpCodeMsgListener> listenerSparseArray;
    Map<String, String> headers;
    Map<String, String> params;

    private RequestConfig() {
        headers = new HashMap<>();
        params = new HashMap<>();
        listenerSparseArray = new SparseArray<>();
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

    public static void SubscribeHttpCodeHandler(int code, OnHttpCodeMsgListener listener) {
        getConfig().listenerSparseArray.put(code, listener);
    }

    public static void NotifyListener(int code, String result, BaseRequest request) {
        OnHttpCodeMsgListener l = getConfig().listenerSparseArray.get(code);
        if(l != null) {
            l.handler(code, result, request);
        }
    }

}
