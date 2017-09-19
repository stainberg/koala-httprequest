package com.stainberg.koala.koalahttp;

import com.stainberg.koala.request.BaseRequest;

/**
 * Created by Stainberg on 9/19/17.
 */

public interface OnHttpCodeMsgListener {

    void handler(int code, String result, BaseRequest request);

}
