package com.stainberg.koala.koalahttp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

/**
 * Created by stainberg on 6/3/15.
 */
public class OkHttpUtils {
    private OkHttpClient client;
    private OkHttpClient downloadc;
    private static OkHttpUtils instance;

    private OkHttpUtils() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(NetworkConfig.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(NetworkConfig.HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(NetworkConfig.HTTP_WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
        List<Protocol> protocols = new ArrayList<>();
            protocols.add(Protocol.HTTP_1_1);
        builder.protocols(protocols);
        client = builder.build();

        OkHttpClient.Builder downloadb = new OkHttpClient.Builder();
        downloadb.connectTimeout(NetworkConfig.DOWNLOAD_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        downloadb.readTimeout(NetworkConfig.DOWNLOAD_READ_TIMEOUT, TimeUnit.MILLISECONDS);
        downloadb.writeTimeout(NetworkConfig.DOWNLOAD_WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
        downloadc = downloadb.build();
    }

    public static OkHttpUtils getInstance() {
        if(instance == null) {
            synchronized (OkHttpUtils.class) {
                if(instance == null) {
                    instance = new OkHttpUtils();
                }
            }
        }
        return instance;
    }

    OkHttpClient getHttpClient() {
        return client;
    }

    public OkHttpClient getDownloadClient() {
        return downloadc;
    }
}
