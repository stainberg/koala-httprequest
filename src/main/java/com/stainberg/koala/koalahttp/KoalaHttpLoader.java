package com.stainberg.koala.koalahttp;

import android.support.annotation.NonNull;

import com.stainberg.koala.request.BaseRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
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
        task = parse(req.url, req);
        if (task != null) {
            return task.sync();
        }
        return null;
    }

    private KoalaHttpTask parse(String url, BaseRequest requestEntity) {
        Map<String, String> params = requestEntity.params;
        Map<String, String> headers = requestEntity.headers;
        List<BaseRequest.Attachment> extras = requestEntity.extras;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("Cache-Control", "no-cache");
        if(headers.size() > 0) {
            Set<Map.Entry<String, String>> set = headers.entrySet();
            for (Map.Entry<String, String> maEntry : set) {
                builder.addHeader(maEntry.getKey(), maEntry.getValue());
            }
        }
        if (requestEntity.method == KoalaRequestType.GET) {
            if(params.size() > 0) {
                url += ("?" + KoalaHttpUtils.getNameValuePair(params));
            }
        } else if (requestEntity.method == KoalaRequestType.POST) {
            if(params.size() == 0) {
                builder.post(new FormBody.Builder().build());
            } else {
                RequestBody body;
                if(extras.size() == 0) {
                    FormBody.Builder bodyBuilder = new FormBody.Builder();
                    for (Map.Entry<String, String> maEntry : params.entrySet()) {
                        if (maEntry.getKey() != null && maEntry.getValue() != null) {
                            bodyBuilder.add(maEntry.getKey(), maEntry.getValue());
                        }
                    }
                    body = bodyBuilder.build();
                } else {
                    MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
                    bodyBuilder.setType(MultipartBody.FORM);
                    for (Map.Entry<String, String> maEntry : params.entrySet()) {
                        if (maEntry.getKey() != null && maEntry.getValue() != null) {
                            bodyBuilder.addFormDataPart(maEntry.getKey(), maEntry.getValue());
                        }
                    }
                    for (BaseRequest.Attachment request : extras) {
                        bodyBuilder.addFormDataPart(request.key, request.filename, request.body);
                    }
                    body = bodyBuilder.build();
                }
                builder.post(body);
            }
        } else if (requestEntity.method == KoalaRequestType.PATCH) {
            if(params.size() == 0) {
                builder.patch(new FormBody.Builder().build());
            } else {
                RequestBody body;
                if(extras.size() == 0) {
                    FormBody.Builder bodyBuilder = new FormBody.Builder();
                    for (Map.Entry<String, String> maEntry : params.entrySet()) {
                        if (maEntry.getKey() != null && maEntry.getValue() != null) {
                            bodyBuilder.add(maEntry.getKey(), maEntry.getValue());
                        }
                    }
                    body = bodyBuilder.build();
                } else {
                    MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
                    bodyBuilder.setType(MultipartBody.FORM);
                    for (Map.Entry<String, String> maEntry : params.entrySet()) {
                        if (maEntry.getKey() != null && maEntry.getValue() != null) {
                            bodyBuilder.addFormDataPart(maEntry.getKey(), maEntry.getValue());
                        }
                    }
                    for (BaseRequest.Attachment request : extras) {
                        bodyBuilder.addFormDataPart(request.key, request.filename, request.body);
                    }
                    body = bodyBuilder.build();
                }
                builder.patch(body);
            }
        } else if(requestEntity.method == KoalaRequestType.DELETE) {
            if(params.size() == 0) {
                builder.delete();
            } else {
                RequestBody body;
                if(extras.size() == 0) {
                    FormBody.Builder bodyBuilder = new FormBody.Builder();
                    for (Map.Entry<String, String> maEntry : params.entrySet()) {
                        if (maEntry.getKey() != null && maEntry.getValue() != null) {
                            bodyBuilder.add(maEntry.getKey(), maEntry.getValue());
                        }
                    }
                    body = bodyBuilder.build();
                } else {
                    MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
                    bodyBuilder.setType(MultipartBody.FORM);
                    for (Map.Entry<String, String> maEntry : params.entrySet()) {
                        if (maEntry.getKey() != null && maEntry.getValue() != null) {
                            bodyBuilder.addFormDataPart(maEntry.getKey(), maEntry.getValue());
                        }
                    }
                    for (BaseRequest.Attachment request : extras) {
                        bodyBuilder.addFormDataPart(request.key, request.filename, request.body);
                    }
                    body = bodyBuilder.build();
                }
                builder.delete(body);
            }
        }
        builder.tag(SecurityMD5.ToMD5(url));
        builder.url(url);
        Request request = builder.build();
        return new KoalaHttpTask(request);
    }


}
