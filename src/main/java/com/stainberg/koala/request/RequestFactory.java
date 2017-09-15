package com.stainberg.koala.request;

import com.stainberg.koala.koalahttp.KoalaRequestType;

/**
 * Created by Stainberg on 9/14/17.
 */

public class RequestFactory {

    public static BaseRequest NewRequest(String url, KoalaRequestType type) {
        return new CacheRequest(url, type);
    }

    public static BaseRequest NewRequest(String url) {
        return new CacheRequest(url);
    }

    private static class CacheRequest extends BaseRequest {

        CacheRequest(String url) {
            super(url);
        }

        CacheRequest(String url, KoalaRequestType method) {
            super(url, method);
        }
    }

}
