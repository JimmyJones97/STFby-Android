package com.xzy.forestSystem.baidu.voicerecognition.android;

import android.annotation.SuppressLint;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressLint({"DefaultLocale"})
public class MD5Util {
    public static String toMd5(byte[] paramArrayOfByte, boolean paramBoolean) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.reset();
            localMessageDigest.update(paramArrayOfByte);
            return toHexString(localMessageDigest.digest(), "", paramBoolean);
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            throw new RuntimeException(localNoSuchAlgorithmException);
        }
    }

    public static String toHexString(byte[] paramArrayOfByte, String paramString, boolean paramBoolean) {
        StringBuilder localStringBuilder = new StringBuilder();
        for (byte b : paramArrayOfByte) {
            String str = Integer.toHexString(b & 255);
            if (paramBoolean) {
                str = str.toUpperCase();
            }
            if (str.length() == 1) {
                localStringBuilder.append("0");
            }
            localStringBuilder.append(str).append(paramString);
        }
        return localStringBuilder.toString();
    }
}
