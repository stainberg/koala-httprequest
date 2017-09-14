package com.stainberg.koala.koalahttp;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class KoalaUploadRunnable implements Runnable {

	private String BOUNDARY = java.util.UUID.randomUUID().toString();
	private String Url = null;
	private KoalaOnUploadListener mListener = null;
	private File uploadFile;
	private boolean isRunning = false;

	public KoalaUploadRunnable(KoalaOnUploadListener l, String url, File file) {
		mListener = l;
		Url = url;
		this.uploadFile = file;
	}

	public void interrupt() {
		isRunning = false;
	}

	@Override
	public void run() {
		if(uploadFile == null || TextUtils.isEmpty(Url)) {
			if(mListener != null) {
				mListener.onUploadError("-1", "file is null or url is null");
			}
			return;
		}
		if(uploadFile.isFile()) {
			FileInputStream fStream;
			try {
				fStream = new FileInputStream(uploadFile);
				int total = fStream.available();
				if(total <= 0) {
					if(mListener != null) {
						mListener.onUploadError("-3", "file size <= 0");
					}
					return;
				}
			} catch (FileNotFoundException e) {
				if(mListener != null) {
					mListener.onUploadError("-1", "FileNotFoundException");
				}
				return;
			} catch (IOException e) {
				if(mListener != null) {
					mListener.onUploadError("-2", "get file size IOException");
				}
				return;
			}
			upload();
		} else {
			if(mListener != null) {
				mListener.onUploadError("-4", "path is not file");
			}
		}

	}
	
	private void upload() {
		isRunning = true;
		HttpURLConnection conn;
        DataOutputStream dataOutputStream;
        URL url;
		try {
			url = new URL(Url);
			conn = (HttpURLConnection) url.openConnection();
	        conn.setConnectTimeout(60000);
			conn.setReadTimeout(120000);
	        conn.setDoOutput(true);
	        conn.setUseCaches(false);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Connection", "keep-alive");
	        conn.setRequestProperty("Content-Type", "application/octet-stream" + ";boundary=" + BOUNDARY);
	        conn.connect();
	        dataOutputStream = new DataOutputStream(conn.getOutputStream());
	        addFileContent(dataOutputStream);
			dataOutputStream.flush();
	        int res = conn.getResponseCode();
	        InputStream in = conn.getInputStream();
	        InputStreamReader isReader = new InputStreamReader(in);
	        BufferedReader bufReader = new BufferedReader(isReader);
	        String line;
	        while ((line = bufReader.readLine()) == null) {
	        	line = "";
	        }
	        if(res == NetworkConstants.SC_OK) {
				if(mListener != null) {
					mListener.onUploadCompletion(line);
				}
	        } else {
				if(mListener != null) {
					mListener.onUploadError(String.valueOf(res), line);
				}
	        }
	        dataOutputStream.close();
			in.close();
	        conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			if(mListener != null) {
				mListener.onUploadError("-5", "HttpURLConnection IOException");
			}
		}
	}
	
	private void addFileContent(DataOutputStream output) throws IOException {
		int bufferSize = 8192;
		int index = 0;
		FileInputStream fStream = new FileInputStream(uploadFile);
		int total = fStream.available();
		byte[] buffer = new byte[bufferSize];
		int length;
		while ((length = fStream.read(buffer)) != -1) {
			if (isRunning) {
				output.write(buffer, 0, length);
				if (length >= bufferSize) {
					index++;
					if(mListener != null) {
						mListener.onProgress(bufferSize * index, total);
					}
				} else {
					if(mListener != null) {
						mListener.onProgress(bufferSize * index + length, total);
					}
				}
			}
		}
		fStream.close();
	}
}
