package com.stainberg.koala.koalahttp;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.stainberg.koala.request.BaseRequest;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by stainberg on 5/17/15.
 */
final class KoalaHttpLoader {

    private static KoalaHttpLoader loader = null;

    private KoalaHttpLoader() {

    }

    static KoalaHttpLoader getInstance() {
        if(loader == null) {
            synchronized (KoalaHttpLoader.class) {
                if(loader == null) {
                    loader = new KoalaHttpLoader();
                }
            }
        }
        return loader;
    }

    Response syncTask(@NonNull BaseRequest req) throws IOException {
        KoalaHttpTask task;
        task = parse(req.getUrl(), req);
        if (task != null) {
            return task.sync();
        }
        return null;
    }

    private KoalaHttpTask parse(String url, BaseRequest requestEntity) {
        Map<String, String> params = requestEntity.getParams();
        Map<String, String> headers = requestEntity.getHeaders();
        List<BaseRequest.Request> extras = requestEntity.getExtras();
        Request.Builder builder = new Request.Builder();
        if(requestEntity.getMaxStale() == 0) {
            builder.addHeader("Cache-Control", "no-cache");
        } else {
            builder.addHeader("Cache-Control", "max-stale=" + requestEntity.getMaxStale());
        }
        if(headers.size() > 0) {
            Set<Map.Entry<String, String>> set = headers.entrySet();
            Iterator<Map.Entry<String, String>> iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> maEntry = iterator.next();
                builder.addHeader(maEntry.getKey(), maEntry.getValue());
            }
        }
        if (requestEntity.getMethod() == KoalaRequestType.GET) {
            if(params.size() > 0) {
                url += ("?" + KoalaHttpUtils.getNameValuePair(params));
            }
        } else if (requestEntity.getMethod() == KoalaRequestType.POST) {
            if(params.size() == 0 && extras.size() == 0) {
                builder.post(new FormBody.Builder().build());
            } else {
                MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
                bodyBuilder.setType(MultipartBody.FORM);
                for (Map.Entry<String, String> maEntry : params.entrySet()) {
                    if (maEntry.getKey() != null && maEntry.getValue() != null) {
                        bodyBuilder.addFormDataPart(maEntry.getKey(), maEntry.getValue());
                    }
                }
                for (BaseRequest.Request request : extras) {
                    bodyBuilder.addFormDataPart(request.getKey(), request.getFilename(), request.getBody());
                }
                builder.post(bodyBuilder.build());
            }
        } else if (requestEntity.getMethod() == KoalaRequestType.PATCH) {
            if(params.size() == 0 && extras.size() == 0) {
                builder.patch(new FormBody.Builder().build());
            } else {
                MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
                bodyBuilder.setType(MultipartBody.FORM);
                for (Map.Entry<String, String> maEntry : params.entrySet()) {
                    if (maEntry.getKey() != null && maEntry.getValue() != null) {
                        bodyBuilder.addFormDataPart(maEntry.getKey(), maEntry.getValue());
                    }
                }
                for (BaseRequest.Request request : extras) {
                    bodyBuilder.addFormDataPart(request.getKey(), request.getFilename(), request.getBody());
                }
                builder.patch(bodyBuilder.build());
            }
        } else if(requestEntity.getMethod() == KoalaRequestType.DELETE) {
            if(params.size() == 0 && extras.size() == 0) {
                builder.delete();
            } else {
                MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
                bodyBuilder.setType(MultipartBody.FORM);
                for (Map.Entry<String, String> maEntry : params.entrySet()) {
                    if (maEntry.getKey() != null && maEntry.getValue() != null) {
                        bodyBuilder.addFormDataPart(maEntry.getKey(), maEntry.getValue());
                    }
                }
                for (BaseRequest.Request request : extras) {
                    bodyBuilder.addFormDataPart(request.getKey(), request.getFilename(), request.getBody());
                }
                builder.delete(bodyBuilder.build());
            }
        }
        if(TextUtils.isEmpty(requestEntity.getTag())) {
            builder.tag(SecurityMD5.ToMD5(url));
        } else {
            builder.tag(requestEntity.getTag());
        }
        builder.url(url);
        Request request = builder.build();
        return new KoalaHttpTask(request);
    }


}
