package com.xzy.forestSystem.smssdk.net;

import android.text.TextUtils;
import android.util.Base64;

import com.xzy.forestSystem.gogisapi.Geodatabase.StringEncodings;
import  com.xzy.forestSystem.mob.tools.utils.Data;
import  com.xzy.forestSystem.mob.tools.utils.Hashon;
import  com.xzy.forestSystem.mob.tools.utils.MobRSA;
import com.xzy.forestSystem.smssdk.utils.SMSLog;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/* renamed from: cn.smssdk.net.c */
public class Crypto {

    /* renamed from: a */
    private static final String f161a = Crypto.class.getSimpleName();

    /* renamed from: b */
    private static Hashon f162b = new Hashon();

    /* renamed from: c */
    private static String f163c = "a1a4b49646080c6c9b381222c5649644fb7a1e2663e493f5f61f7692bb9618ef0396b708e3896730ebe840889b4b11269c363bc445339d7591cede60d079fc27";

    /* renamed from: d */
    private static String f164d = "25e7a9ca2cfe7f099873eab6c14123bd1f9303b82e3332cd5704e6d65452f1608420e9c49e56140b0ffb828de005558979147e0cb08791a9982684e5b587c4676681c756677bcced0877c725d49f5da8c7b834519e4ddc89ae00f5478b3b87a88949601a5a732f4257d0dbbc90f74051b6ce368cb731df5b070931c7f8a570cf";

    /* renamed from: e */
    private static int f165e = 1024;

    /* renamed from: a */
    public static void m412a(String str, String str2, int i) {
        f163c = str;
        f164d = str2;
        f165e = i;
    }

    /* renamed from: a */
    public static byte[] m411a(String str, byte[] bArr) {
        try {
            return Data.AES128Encode(bArr, str);
        } catch (Throwable th) {
            SMSLog.getInstance().m69d(th);
            return null;
        }
    }

    /* renamed from: a */
    public static byte[] m407a(byte[] bArr, byte[] bArr2) {
        try {
            return Data.AES128Decode(bArr2, bArr);
        } catch (Throwable th) {
            SMSLog.getInstance().m69d(th);
            return null;
        }
    }

    /* renamed from: a */
    public static byte[] m410a(HashMap<String, Object> hashMap, boolean z, byte[] bArr) throws Throwable {
        if (bArr == null || bArr.length <= 0) {
            throw new Throwable("[encode]aesKey can not be null");
        }
        byte[] bytes = f162b.fromHashMap(hashMap).getBytes(StringEncodings.UTF8);
        if (z) {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f161a, "encodeData", "zip: true. Gzip data before encode.");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(bytes);
            gZIPOutputStream.close();
            bytes = byteArrayOutputStream.toByteArray();
        }
        return Data.AES128Encode(bArr, bytes);
    }

    /* renamed from: a */
    public static String m408a(byte[] bArr, boolean z, byte[] bArr2) throws Throwable {
        if (bArr2 == null || bArr2.length <= 0) {
            throw new Throwable("[decode]aesKey can not be null");
        }
        byte[] AES128Decode = Data.AES128Decode(bArr2, bArr);
        if (z) {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f161a, "decodeData", "zip: true. Un-Gzip data.");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(AES128Decode);
            GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            byte[] bArr3 = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                int read = gZIPInputStream.read(bArr3, 0, bArr3.length);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr3, 0, read);
            }
            AES128Decode = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            gZIPInputStream.close();
            byteArrayInputStream.close();
        }
        String str = new String(AES128Decode, StringEncodings.UTF8);
        if (!TextUtils.isEmpty(str)) {
            return str.trim();
        }
        throw new Throwable("[decode]Response is empty");
    }

    /* renamed from: a */
    public static String m414a(String str, Object obj) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        return Base64.encodeToString(Data.AES128Encode(Data.rawMD5(str.getBytes()), Base64.encodeToString(byteArrayOutputStream.toByteArray(), 2).getBytes()), 2);
    }

    /* renamed from: a */
    public static Object m413a(String str, String str2) throws Throwable {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(Base64.decode(new String(Data.AES128Decode(Data.rawMD5(str.getBytes()), Base64.decode(str2, 2)), StringEncodings.UTF8).trim(), 2)));
        String str3 = (String) objectInputStream.readObject();
        objectInputStream.close();
        return str3;
    }

    /* renamed from: a */
    public static String m415a(String str) throws Throwable {
        return Data.byteToHex(Data.AES128Encode(Data.rawMD5("sms.mob.com.sdk.2.0.0".getBytes()), str.getBytes()));
    }

    /* renamed from: b */
    public static String m406b(String str) throws Throwable {
        byte[] AES128Decode = Data.AES128Decode(Data.rawMD5("sms.mob.com.sdk.2.0.0".getBytes()), m404c(str));
        if (AES128Decode == null) {
            return null;
        }
        return new String(AES128Decode, StringEncodings.UTF8).trim();
    }

    /* renamed from: c */
    public static byte[] m404c(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length % 2 == 1) {
            return null;
        }
        int i = length / 2;
        byte[] bArr = new byte[i];
        for (int i2 = 0; i2 < i; i2++) {
            try {
                bArr[i2] = (byte) Integer.parseInt(str.substring(i2 * 2, (i2 * 2) + 2), 16);
            } catch (Throwable th) {
                return null;
            }
        }
        return bArr;
    }

    /* renamed from: a */
    public static byte[] m409a(byte[] bArr) throws Throwable {
        return new MobRSA(f165e).encode(bArr, new BigInteger(f163c, 16), new BigInteger(f164d, 16));
    }

    /* renamed from: b */
    public static byte[] m405b(byte[] bArr) throws Throwable {
        return new MobRSA(f165e).decode(bArr, new BigInteger(f163c, 16), new BigInteger(f164d, 16));
    }

    /* renamed from: a */
    public static byte[] m416a() throws Throwable {
        Random random = new Random();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeLong(random.nextLong());
        dataOutputStream.writeLong(random.nextLong());
        dataOutputStream.flush();
        dataOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
