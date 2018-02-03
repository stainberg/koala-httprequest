package com.stainberg.koala.koalahttp;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.stainberg.koala.request.BaseRequest;
import com.stainberg.koala.request.RequestConfig;

import okhttp3.Response;

/**
 * Created by Stainberg on 8/29/16.
 */
public class RequestLogic {

    private String id;
    BaseRequest request;
    KoalaTaskListener listener;
    KoalaInterrupt interrupt;
    private int code = 0;
    private String resultString = "";

    private RequestLogic(Builder builder) {
        id = builder.id;
        request = builder.request;
        listener = builder.listener;
        interrupt = builder.interrupt;

    }

    @SuppressWarnings("unchecked")
    public <T> void execute() {
        ExecuteHelper.getLogicHelper().execute(new LogicTask(id, RequestLogic.this) {
            @Override
            protected void execute() {
                if(interrupt != null) {
                    if(interrupt.OnInterrupt()) {
                        if (listener != null) {
                            code = NetworkConstants.REQUEST_INTERRUPT;
                            listener.onResponseResult(null, request, code, resultString);
                        }
                        return;
                    }
                }
                Class<T> cls = KoalaHttpUtils.parserClass(listener);
                T object = generalRequestSync(cls);
                if (listener != null) {
                    listener.onResponseResult(object, request, code, resultString);
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
        return generalRequestSync(cls);
    }

    private <T> T generalRequestSync(Class<T> cls) {
        T result;
        String responseString;
        try {
            Logger.getLogger().PrintcUrl(request);
            Logger.getLogger().Println("Request id = " + id, request);
            Response response = KoalaHttpLoader.getInstance().syncTask(request);
            if (response != null && response.body() != null) {
                code = response.code();
                responseString = response.body().string();
                resultString = responseString;
                Logger.getLogger().Println("Response String id = " + id, responseString);
                response.close();
                if (response.isSuccessful()) {
                    if (cls != null && !TextUtils.isEmpty(responseString)) {
                        result = KoalaGson.fromJson(cls, responseString);
                        if (result != null) {
                            return result;
                        }
                        code = NetworkConstants.PARSER_ERROR;
                        RequestConfig.NotifyListener(code, responseString, request);
                    }
                } else {
                    RequestConfig.NotifyListener(code, responseString, request);
                }
            } else {
                code = NetworkConstants.NO_RESPONSE;
                RequestConfig.NotifyListener(code, "NO_RESPONSE", request);
            }
        } catch (Exception e) {
            code = NetworkConstants.NETWORK_ERROR;
            RequestConfig.NotifyListener(code, "NETWORK_ERROR", request);
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
