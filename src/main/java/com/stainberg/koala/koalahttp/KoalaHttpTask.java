package com.stainberg.koala.koalahttp;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by stainberg on 5/17/15.
 */
final class KoalaHttpTask {

    private Object tag;
    private Request request;

    KoalaHttpTask(final Request request) {
        tag = request.tag();
        this.request = request;
    }

    Response sync() throws IOException {
        return BaseHttpClient.getHttpObject().getOkHttpClient().newCall(request).execute();
    }

    public Object getTag() {
        return tag;
    }
}
