package com.xzy.forestSystem.baidu.voicerecognition.android;

import android.annotation.SuppressLint;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AESUtil {
    private static final String ALGORITHM_NAME = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    @SuppressLint({"TrulyRandom"})
    public static byte[] encrypt(String paramString1, String paramString2, byte[] paramArrayOfByte) throws Exception {
        SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramString2.getBytes(), ALGORITHM_NAME);
        Cipher localCipher = Cipher.getInstance(TRANSFORMATION);
        localCipher.init(1, localSecretKeySpec, new IvParameterSpec(paramString1.getBytes()));
        return localCipher.doFinal(paramArrayOfByte);
    }

    public static byte[] decrypt(String paramString1, String paramString2, byte[] paramArrayOfByte) throws Exception {
        SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramString2.getBytes(), ALGORITHM_NAME);
        Cipher localCipher = Cipher.getInstance(TRANSFORMATION);
        localCipher.init(2, localSecretKeySpec, new IvParameterSpec(paramString1.getBytes()));
        return localCipher.doFinal(paramArrayOfByte);
    }
}
