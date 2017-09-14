package com.stainberg.koala.koalahttp;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class SecurityMD5 {
	
	public synchronized static String ToMD5(byte[] bytes) {
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(bytes);
			return toHexString(algorithm.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	synchronized static String ToMD5(String str) {
		if(TextUtils.isEmpty(str)) {
			return "";
		}
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(str.getBytes());
			String result = toHexString(algorithm.digest());
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private synchronized static String toHexString(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			int val = ((int)bytes[i]);
			if(val < 0) {
				val += 256;
			}
			if(val < 16) {
				hexString.append("0");  
            }
			hexString.append(Integer.toHexString(val));
		}
		return hexString.toString();
	}

}
