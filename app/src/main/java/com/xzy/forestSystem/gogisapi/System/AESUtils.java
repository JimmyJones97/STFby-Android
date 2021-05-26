package  com.xzy.forestSystem.gogisapi.System;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.IntDef;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    private static final int KEY_SIZE = 32;
    private static final String SHA1_PRNG = "SHA1PRNG";

    @IntDef({1, 2})
    @interface AESType {
    }

    @SuppressLint({"DeletedProvider", "GetInstance"})
    public static String des(String content, String password, @AESType int type) {
        SecretKeySpec secretKeySpec;
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(password)) {
            return null;
        }
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                secretKeySpec = deriveKeyInsecurely(password);
            } else {
                secretKeySpec = fixSmallVersion(password);
            }
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(type, secretKeySpec);
            if (type == 1) {
                return parseByte2HexStr(cipher.doFinal(content.getBytes("utf-8")));
            }
            return new String(cipher.doFinal(parseHexStr2Byte(content)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint({"DeletedProvider"})
    private static SecretKeySpec fixSmallVersion(String password) throws NoSuchAlgorithmException, NoSuchProviderException {
        SecureRandom secureRandom;
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        if (Build.VERSION.SDK_INT >= 23) {
            secureRandom = SecureRandom.getInstance(SHA1_PRNG, new CryptoProvider());
        } else {
            secureRandom = SecureRandom.getInstance(SHA1_PRNG, "Crypto");
        }
        secureRandom.setSeed(password.getBytes());
        generator.init(128, secureRandom);
        return new SecretKeySpec(generator.generateKey().getEncoded(), "AES");
    }

    @SuppressLint({"NewApi"})
    private static SecretKeySpec deriveKeyInsecurely(String password) {
        return new SecretKeySpec(InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(password.getBytes(StandardCharsets.US_ASCII), 32), "AES");
    }

    private static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 255);
            if (hex.length() == 1) {
                hex = String.valueOf('0') + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[(hexStr.length() / 2)];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, (i * 2) + 1), 16);
            result[i] = (byte) ((high * 16) + Integer.parseInt(hexStr.substring((i * 2) + 1, (i * 2) + 2), 16));
        }
        return result;
    }

    /* access modifiers changed from: private */
    public static final class CryptoProvider extends Provider {
        CryptoProvider() {
            super("Crypto", 1.0d, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
            put("SecureRandom.SHA1PRNG", "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }
}
