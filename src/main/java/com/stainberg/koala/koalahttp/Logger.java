package com.stainberg.koala.koalahttp;

import android.util.Log;

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
}
