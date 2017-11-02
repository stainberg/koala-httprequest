package com.stainberg.koala.koalahttp;

import okhttp3.OkHttpClient;

class BaseHttpClient {
	private static BaseHttpClient mHttpClient;
	private OkHttpClient mOkHttpClient;
	private OkHttpClient mDownloadClient;

	static BaseHttpClient getHttpObject() {
		if(mHttpClient == null) {
			synchronized (BaseHttpClient.class) {
				if(mHttpClient == null) {
					mHttpClient = new BaseHttpClient();
				}
			}
		}
		return mHttpClient;
	}
	
	private BaseHttpClient() {
		mOkHttpClient = OkHttpUtils.getInstance().getHttpClient();
		mDownloadClient = OkHttpUtils.getInstance().getDownloadClient();
	}
	
	OkHttpClient getOkHttpClient() {
		return mOkHttpClient;
	}

	public OkHttpClient getDownloadClient() {
		return mDownloadClient;
	}
}
