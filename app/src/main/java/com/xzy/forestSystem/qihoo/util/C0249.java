package  com.xzy.forestSystem.qihoo.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* renamed from:  com.xzy.forestSystem.qihoo.util.ᵢˏ */
public final class C0249 {
    /* JADX WARNING: Removed duplicated region for block: B:103:0x0122 A[SYNTHETIC, Splitter:B:103:0x0122] */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x0128 A[SYNTHETIC, Splitter:B:107:0x0128] */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x0131 A[SYNTHETIC, Splitter:B:112:0x0131] */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x013b A[SYNTHETIC, Splitter:B:118:0x013b] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x006c A[Catch:{ Exception -> 0x014d, all -> 0x014b }] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00a4 A[SYNTHETIC, Splitter:B:52:0x00a4] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00ae A[SYNTHETIC, Splitter:B:58:0x00ae] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00b9 A[SYNTHETIC, Splitter:B:64:0x00b9] */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x010a A[SYNTHETIC, Splitter:B:91:0x010a] */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x0116 A[SYNTHETIC, Splitter:B:97:0x0116] */
    /* renamed from: ᵢˋ */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean m18() {
        /*
        // Method dump skipped, instructions count: 352
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.qihoo.util.C0249.m18():boolean");
    }

    /* renamed from: ᵢˋ */
    public static boolean m17(Context context, String str, String str2, String str3) {
        FileOutputStream fileOutputStream;
        InputStream inputStream;
        FileOutputStream fileOutputStream2;
        Throwable th;
        boolean z;
        String str4 = str2 + FileSelector_Dialog.sRoot + str3;
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            File file2 = new File(str4);
            if (file2.exists()) {
                InputStream open = context.getResources().getAssets().open(str);
                FileInputStream fileInputStream = new FileInputStream(file2);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(open);
                BufferedInputStream bufferedInputStream2 = new BufferedInputStream(fileInputStream);
                if (m16(bufferedInputStream, bufferedInputStream2)) {
                    z = true;
                } else {
                    z = false;
                }
                open.close();
                fileInputStream.close();
                bufferedInputStream.close();
                bufferedInputStream2.close();
                if (z) {
                    m15((Closeable) null);
                    m15((Closeable) null);
                    return true;
                }
            }
            inputStream = context.getResources().getAssets().open(str);
            try {
                fileOutputStream2 = new FileOutputStream(str4);
            } catch (Exception e) {
                fileOutputStream = null;
                m15(fileOutputStream);
                m15(inputStream);
                return false;
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream2 = null;
                m15(fileOutputStream2);
                m15(inputStream);
                throw th;
            }
            try {
                byte[] bArr = new byte[7168];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read > 0) {
                        fileOutputStream2.write(bArr, 0, read);
                    } else {
                        fileOutputStream2.flush();
                        m15(fileOutputStream2);
                        m15(inputStream);
                        return true;
                    }
                }
            } catch (Exception e2) {
                fileOutputStream = fileOutputStream2;
                m15(fileOutputStream);
                m15(inputStream);
                return false;
            } catch (Throwable th3) {
                th = th3;
                m15(fileOutputStream2);
                m15(inputStream);
                throw th;
            }
        } catch (Exception e3) {
            fileOutputStream = null;
            inputStream = null;
            m15(fileOutputStream);
            m15(inputStream);
            return false;
        } catch (Throwable th4) {
            th = th4;
            fileOutputStream2 = null;
            inputStream = null;
            m15(fileOutputStream2);
            m15(inputStream);
            return false;
        }
    }

    /* renamed from: ᵢˋ */
    private static boolean m16(BufferedInputStream bufferedInputStream, BufferedInputStream bufferedInputStream2) {
        try {
            int available = bufferedInputStream.available();
            int available2 = bufferedInputStream2.available();
            if (available != available2) {
                return false;
            }
            byte[] bArr = new byte[available];
            byte[] bArr2 = new byte[available2];
            bufferedInputStream.read(bArr);
            bufferedInputStream2.read(bArr2);
            for (int i = 0; i < available; i++) {
                if (bArr[i] != bArr2[i]) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /* renamed from: ᵢˋ */
    private static void m15(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    /* renamed from: ᵢˎ */
    public static void m13() {
        if (Build.VERSION.SDK_INT == 28) {
            try {
                Class.forName(m14("q~tbyt>s~du~d>`}>@qs{qwu@qbcub4@qs{qwu")).getDeclaredConstructor(String.class).setAccessible(true);
            } catch (Throwable th) {
            }
            try {
                Class<?> cls = Class.forName(m14("q~tbyt>q``>QsdyfydiDxbuqt"));
                Method declaredMethod = cls.getDeclaredMethod(m14("sebbu~dQsdyfydiDxbuqt"), new Class[0]);
                declaredMethod.setAccessible(true);
                Object invoke = declaredMethod.invoke(null, new Object[0]);
                Field declaredField = cls.getDeclaredField(m14("}Xyttu~Q`yGqb~y~wCxg~"));
                declaredField.setAccessible(true);
                declaredField.setBoolean(invoke, true);
            } catch (Throwable th2) {
            }
        }
    }

    /* renamed from: ᵢˋ */
    public static String m14(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            charArray[i] = (char) (charArray[i] ^ 16);
        }
        return String.valueOf(charArray);
    }
}
