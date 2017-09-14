package com.stainberg.koala.koalahttp;

interface KoalaOnUploadListener {
	void onUploadCompletion(String msg);
	void onUploadError(String code, String msg);
	void onProgress(int progress, int size);
}
