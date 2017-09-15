package com.stainberg.koala.koalahttp;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.stainberg.koala.request.BaseRequest;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by Stainberg on 8/29/16.
 */
public class RequestLogic {

    private String id;
    private BaseRequest request;
    private KoalaTaskListener listener;
    private KoalaInterrupt interrupt;
    private int code = 0;

    private RequestLogic(Builder builder) {
        id = builder.id;
        request = builder.request;
        listener = builder.listener;
        interrupt = builder.interrupt;

    }

    @SuppressWarnings("unchecked")
    public <T> void execute() {
        ExecuteHelper.getLogicHelper().execute(new LogicTask(id) {
            @Override
            protected void execute() {
                if(interrupt != null) {
                    if(interrupt.OnInterrupt()) {
                        if (listener != null) {
                            code = NetworkConstants.REQUEST_INTERRUPT;
                            listener.onResponseResult(null, request, code);
                        }
                        return;
                    }
                }
                Class<T> cls = KoalaHttpUtils.parserClass(listener);
                T object = generalRequestSync(cls, request);
                if (listener != null) {
                    listener.onResponseResult(object, request, code);
                }
            }
        });
    }

    public void get() {
        request.method = KoalaRequestType.GET;
        execute();
    }

    public void post() {
        request.method = KoalaRequestType.POST;
        execute();
    }

    public void patch() {
        request.method = KoalaRequestType.PATCH;
        execute();
    }

    public void delete() {
        request.method = KoalaRequestType.DELETE;
        execute();
    }

    public <T> T executeSync(Class<T> cls) {
        return generalRequestSync(cls, request);
    }

    private <T> T generalRequestSync(Class<T> cls, BaseRequest request) {
        T result;
        String responseString;
        try {
            Logger.getLogger().Println("Request id = " + id, KoalaGson.toJson(request));
            Response response = KoalaHttpLoader.getInstance().syncTask(request);
            Logger.getLogger().Println("Response id = " + id, KoalaGson.toJson(response));
            if (response != null) {
                code = response.code();
                responseString = response.body().string();
                Logger.getLogger().Println("Response String id = " + id, responseString);
                response.close();
                if (response.isSuccessful()) {
                    if (cls != null && !TextUtils.isEmpty(responseString)) {
                        result = KoalaGson.fromJson(cls, responseString);
                        if (result != null) {
                            return result;
                        }
                        code = NetworkConstants.PARSER_ERROR;
                    }
                }
            } else {
                code = NetworkConstants.NO_RESPONSE;
            }
        } catch (IOException e) {
            code = NetworkConstants.NETWORK_ERROR;
        }
        return null;
    }

    public static class Builder {
        private String id;
        private BaseRequest request;
        private KoalaTaskListener listener;
        private KoalaInterrupt interrupt;

        public Builder() {

        }

        public Builder setTaskId(String i) {
            id = i;
            return this;
        }

        public Builder setRequest(BaseRequest r) {
            request = r;
            return this;
        }

        public Builder setListener(@NonNull KoalaTaskListener l) {
            listener = l;
            return this;
        }

        public Builder setInterrupt(KoalaInterrupt i) {
            interrupt = i;
            return this;
        }

        public RequestLogic build() {
            if(TextUtils.isEmpty(id)) {
                id = SecurityMD5.ToMD5(request.url);
            }
            return new RequestLogic(this);
        }
    }

}
