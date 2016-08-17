package com.uuzz.android.util;

import com.uuzz.android.util.log.UUZZLog;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class CodeHelper {
	/**
	 * 日志对象
	 */
	private static UUZZLog logger = new UUZZLog("CodeHelper");

	/**
	 * 描 述：Base64解码，屏蔽空格和\r\n<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/12/29 注释 <br/>
	 */
	public static byte[] Base64Decode(String oriData) {
		return Base64.decode(oriData.replace(" ", ""), Base64.NO_WRAP);
	}

	/**
	 * 描 述：Base64解码，屏蔽空格和\r\n<br/>
	 * 这里的oriData必须是UTF-8编码的，也就是str.getBytes("UTF8")!!!!!!!!!!!!!!!!!!!!!!!!<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/12/29 注释 <br/>
	 */
	public static String Base64Encode(byte[] oriData) {
		return Base64.encodeToString(oriData, Base64.NO_WRAP).replace(" ", "");
	}

	/**
	 * 描 述：DES解密<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/12/29 注释 <br/>
	 */
	public static byte[] DesDecrypt(byte[] byteMi, byte[] byteKey)
			throws InvalidKeyException, InvalidKeySpecException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		return CodeHelper.Des(byteMi, byteKey, Cipher.DECRYPT_MODE);
	}

	/**
	 * 描 述：DES加密<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/12/29 注释 <br/>
	 */
	public static byte[] DesEncrypt(byte[] byteMi, byte[] byteKey)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {

		return CodeHelper.Des(byteMi, byteKey, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 描 述：DES加解密<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/12/29 注释 <br/>
	 */
	private static byte[] Des(byte[] byteData, byte[] byteKey, int opmode)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		try {
			SecretKeySpec desKey = new SecretKeySpec(byteKey, "DES");

			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(opmode, desKey);

			return cipher.doFinal(byteData);
		} catch (Exception e) {
			logger.w("AES Encrypt or Decrypt failed!", e);
			return null;
		}
	}

	/**
	 * 描 述：AES解密<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/12/29 注释 <br/>
	 */
	public static byte[] AesDecrypt(byte[] byteMi, byte[] byteKey)
			throws Exception {
		return CodeHelper.Aes(byteMi, byteKey, Cipher.DECRYPT_MODE);
	}

	/**
	 * 描 述：AES加密<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/12/29 注释 <br/>
	 */
	public static byte[] AesEncrypt(byte[] byteMi, byte[] byteKey)
			throws Exception {
		return CodeHelper.Aes(byteMi, byteKey, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 描 述：AES加密解密<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/12/29 注释 <br/>
	 */
	private static byte[] Aes(byte[] byteData, byte[] byteKey, int opmode)
			throws Exception {
		try {
			SecretKeySpec aesKey = new SecretKeySpec(byteKey, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(opmode, aesKey);

			return cipher.doFinal(byteData);
		} catch (Exception e) {
			logger.w("AES Encrypt or Decrypt failed!", e);
			return null;
		}
	}

	/**
	 * 描 述：RSA解密<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/12/29 注释 <br/>
	 */
	public static byte[] RsaDecrypt(byte[] byteMi, Key pKey) throws Exception {
		return CodeHelper.Rsa(byteMi, pKey, Cipher.DECRYPT_MODE);
	}

	/**
	 * 描 述：RSA解密<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/12/29 注释 <br/>
	 */
	public static byte[] RsaEncrypt(byte[] byteMi, Key pKey) throws Exception {
		return CodeHelper.Rsa(byteMi, pKey, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 描 述：RSA加密解密<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/12/29 注释 <br/>
	 */
	private static byte[] Rsa(byte[] byteData, Key pKey, int opmode)
			throws Exception {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(opmode, pKey);

			return cipher.doFinal(byteData);
		} catch (Exception e) {
			logger.w("RSA Encrypt or Decrypt failed!", e);
			return null;
		}
	}
}
