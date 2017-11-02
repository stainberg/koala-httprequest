package com.stainberg.koala.koalahttp;

/**
 * Created by stainberg on 6/3/15.
 */
class NetworkConfig {
    static final int HTTP_CONNECT_TIMEOUT = 1000 * 10;
    static final int HTTP_READ_TIMEOUT = 1500 * 10;
    static final int HTTP_WRITE_TIMEOUT = 1500 * 10;

    static final int DOWNLOAD_CONNECT_TIMEOUT = 1000 * 10;
    static final int DOWNLOAD_READ_TIMEOUT = 1000 * 120;
    static final int DOWNLOAD_WRITE_TIMEOUT = 1000 * 60;
}
