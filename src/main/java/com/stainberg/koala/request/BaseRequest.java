package com.stainberg.koala.request;


import com.stainberg.koala.koalahttp.KoalaRequestType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

public abstract class BaseRequest implements Serializable {

    public Object obj;
    public Map<String, String> params;
    public Map<String, String> headers;
    public List<Attachment> extras;
    public String url;
    public KoalaRequestType method;

    public BaseRequest(String url) {
        this(url, KoalaRequestType.GET);
    }

    public BaseRequest(String url, KoalaRequestType method) {
        this.url = url;
        this.method = method;
        params = new HashMap<>();
        headers = new HashMap<>();
        extras = new ArrayList<>();
        if(RequestConfig.getConfig().headers.size() > 0) {
            headers.putAll(RequestConfig.getConfig().headers);
        }
        if(RequestConfig.getConfig().params.size() > 0) {
            params.putAll(RequestConfig.getConfig().params);
        }
        if(!this.url.startsWith("http")) {
            this.url = "http://" + this.url;
        }
    }

    public BaseRequest AddParam(String k, String v) {
        params.put(k, v);
        return this;
    }

    public BaseRequest AddHead(String k, String v) {
        headers.put(k, v);
        return this;
    }

    public BaseRequest AddObject(Object o) {
        obj = o;
        return this;
    }

    public BaseRequest AddAttachment(String k, String filename, RequestBody body) {
        Attachment attachment = new Attachment();
        attachment.key = k;
        attachment.filename = filename;
        attachment.body = body;
        extras.add(attachment);
        return this;
    }

    public class Attachment {
        public String key;
        public String filename;
        public RequestBody body;
    }
}
