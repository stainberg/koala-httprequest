package com.stainberg.koala.koalahttp;


import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;

import com.stainberg.koala.request.BaseRequest;

import java.lang.reflect.Field;

/**
 * Created by stainberg on 5/17/15.
 */
public abstract class KoalaTaskListener<T, K extends BaseRequest> {

    private MODE mode;

    public KoalaTaskListener() {
        mode = MODE.MAIN;
    }

    public KoalaTaskListener(MODE mode) {
        this.mode = mode;
    }

    protected abstract void onResponse(T result, K request, int code, String resultString);

    protected void onFinish() {

    }

    private Object getOuterObject(Object object) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if(field.getName().contains("this$")) {
                field.setAccessible(true);
                Object result = field.get(object);
                if(field.getName().equals("this$0")) {
                        return result;
                } else {
                    return getOuterObject(result);
                }
            }
        }
        return object;
    }

    void onResponseResult(final T result, final K request, final int code, final String resultString) {
        switch (mode) {
            case MAIN:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Object outer = getOuterObject(KoalaTaskListener.this);
                            if(outer instanceof Fragment) {
                                if(!((Fragment) outer).isAdded()) {
                                    return;
                                }
                            }
                            if(outer instanceof android.app.Fragment) {
                                if(!((android.app.Fragment) outer).isAdded()) {
                                    return;
                                }
                            }
                            if(outer instanceof Activity) {
                                if(((Activity) outer).isDestroyed()) {
                                    return;
                                }
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        KoalaTaskListener.this.onFinish();
                        KoalaTaskListener.this.onResponse(result, request, code, resultString);
                    }
                });
                break;
            case BACKGROUND:
                ExecuteHelper.getLogicHelper().executeOnBackground(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Object outer = getOuterObject(KoalaTaskListener.this);
                            if(outer instanceof Fragment) {
                                if(!((Fragment) outer).isAdded()) {
                                    return;
                                }
                            }
                            if(outer instanceof android.app.Fragment) {
                                if(!((android.app.Fragment) outer).isAdded()) {
                                    return;
                                }
                            }
                            if(outer instanceof Activity) {
                                if(((Activity) outer).isDestroyed()) {
                                    return;
                                }
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        KoalaTaskListener.this.onFinish();
                        KoalaTaskListener.this.onResponse(result, request, code, resultString);
                    }
                });
                break;
            case SYNC:
                KoalaTaskListener.this.onFinish();
                KoalaTaskListener.this.onResponse(result, request, code, resultString);
                break;
        }
    }

    private Handler handler = new Handler(Looper.getMainLooper());

    public enum MODE {
        MAIN,
        SYNC,
        BACKGROUND
    }
}
