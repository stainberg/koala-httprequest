package com.stainberg.koala.koalahttp;

import android.util.Log;

import com.stainberg.koala.request.BaseRequest;

import java.util.Map;

/**
 * Created by Stainberg on 9/14/17.
 */

public class Logger {

    private static Logger logger = null;
    private boolean isdebug;

    private Logger() {
        isdebug = false;
    }

    static Logger getLogger() {
        if(logger == null) {
            synchronized (Logger.class) {
                if(logger == null) {
                    logger = new Logger();
                }
            }
        }
        return logger;
    }

    public static void Debug(boolean debug) {
        getLogger().isdebug = debug;
    }

    void Println(String tag, String x) {
        if(getLogger().isdebug) {
            Log.d(tag, x);
        }
    }

    void Println(String tag, Object o) {
        if(getLogger().isdebug) {
            try {
                Log.d(tag, KoalaGson.toJson(o));
            } catch (Exception e) {
                Log.d(tag, "Println Error e = " + e.getMessage());
            }
        }
    }

    void PrintcUrl(BaseRequest o) {
        if(getLogger().isdebug) {
            Log.d("cURL String = ", BuildCurlString(o));
        }
    }

    private static String BuildCurlString(BaseRequest request) {
        String url = request.url;
        String wspace = " ";
        StringBuilder builder = new StringBuilder();
        builder.append("curl -X");
        builder.append(wspace);
        builder.append(request.method);
        builder.append(wspace);
        for (Map.Entry<String, String> entry: request.headers.entrySet()) {
            builder.append("-H");
            builder.append(wspace);
            builder.append("\"");
            builder.append(entry.getKey());
            builder.append(":");
            builder.append(entry.getValue());
            builder.append("\"");
            builder.append(wspace);
        }
        if(request.params.size() > 0) {
            if(request.method == KoalaRequestType.GET) {
                url += ("?" + KoalaHttpUtils.getNameValuePair(request.params));
            } else {
                builder.append("-d");
                builder.append(wspace);
                builder.append("'");
                if(request.obj != null) {
                    builder.append(KoalaGson.toJson(request.obj));
                } else {
                    int index = 1;
                    for (Map.Entry<String, String> entry : request.params.entrySet()) {
                        builder.append(entry.getKey());
                        builder.append("=");
                        builder.append(entry.getValue());
                        if (index < request.params.size()) {
                            builder.append("&");
                        }
                        index++;
                    }
                }
                builder.append("'");
                builder.append(wspace);
            }
        }
        builder.append("\"");
        builder.append(url);
        builder.append("\"");
        return builder.toString();
    }
}
