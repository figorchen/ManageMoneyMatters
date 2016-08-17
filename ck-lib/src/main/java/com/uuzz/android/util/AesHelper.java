package com.uuzz.android.util;

import com.uuzz.android.util.log.UUZZLog;

import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesHelper {

	/**
	 * 日志对象
	 */
	private static UUZZLog logger = new UUZZLog("AesHelper");

	// 加密算法
	public static String Encrypt(String sSrc, String sKey) {
		if (sKey == null) {
			logger.w("Key为空null");
			return null;
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			logger.w("Key长度不是16位");
			return null;
		}
		try {
			byte[] raw = sKey.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// "算法/模式/补码方式"
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
			return URLEncoder.encode(Base64.encodeToString(encrypted, Base64.NO_WRAP), "utf-8");
		} catch (Exception e) {
			logger.w("AES Encrypt is failed!", e);
			return null;
		}
	}

	// 加密算法
	public static String EncryptWithoutEncode(String sSrc, String sKey) {
		if (sKey == null) {
			logger.w("Key为空null");
			return null;
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			logger.w("Key长度不是16位");
			return null;
		}
		try {
			byte[] raw = sKey.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// "算法/模式/补码方式"
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
			return Base64.encodeToString(encrypted, Base64.NO_WRAP);
		} catch (Exception e) {
			logger.w("AES Encrypt is failed!", e);
			return null;
		}
	}

	// 解密
	public static String Decrypt(String sSrc, String sKey) {
		// 判断Key是否正确
		if (sKey == null) {
			System.out.print("Key为空null");
			return null;
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			System.out.print("Key长度不是16位");
			return null;
		}
		try {
			byte[] raw = sKey.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = Base64.decode(sSrc, Base64.NO_WRAP);// 先用base64解密

			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, "utf-8");
			return originalString;
		} catch (Exception e) {
			logger.w("AES Decrypt is failed!", e);
			return null;
		}
	}

//	public static void main(String[] args) throws Exception {
//		/*
//		 * 此处使用AES-128-ECB加密模式，key需要为16位。
//		 */
//		String cKey = "425887A717C43508";
//		// 需要加密的字串
//		String cSrc = "www.gowhere.so";
//		System.out.println(cSrc);
//		// 加密
//		String enString = AesHelper.Encrypt(cSrc, cKey);
//		System.out.println("加密后的字串是：" + enString);
//
//		// 解密
//		String DeString = AesHelper.Decrypt(enString, cKey);
//		System.out.println("解密后的字串是：" + DeString);
//	}
}
