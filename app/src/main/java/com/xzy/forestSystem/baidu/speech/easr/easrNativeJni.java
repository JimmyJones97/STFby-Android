package com.xzy.forestSystem.baidu.speech.easr;

import android.content.Context;
import android.util.Log;
import java.io.File;

public class easrNativeJni {
    public static final int VERIFY_LICENSE_EXPIRED = -5;
    public static final int VERIFY_LICENSE_FAIL_1 = -1;
    public static final int VERIFY_LICENSE_FAIL_7 = -7;
    public static final int VERIFY_LICENSE_FAIL_CUID = -4;
    public static final int VERIFY_LICENSE_FAIL_PACKAGE_NAME = -2;
    public static final int VERIFY_LICENSE_FAIL_SIGNATURE = -3;
    public static final int VERIFY_LICENSE_WILL_EXPIRED = -6;
    public static final int VERIFY_OK = 0;
    public static final int VERIFY_TEST_LICENSE_EXPIRED = -10;
    public static final int VERIFY_TEST_LICENSE_OK_PREFIX = 1000;

    public static native int BuildNet(int i, String str);

    public static native int BuildSlot(String str, int i);

    public static native String CalPostProcessTransf(String str);

    public static native boolean CheckMD5(String str);

    public static native int Decode(int i, short[] sArr, int i2, byte[][] bArr, int i3, boolean z);

    public static native boolean ExistFile(String str);

    public static native int Free();

    public static native int GetDate(String str);

    public static native String GetImmeSentence(int i);

    public static native int GetLicense(Context context, String str, String str2, String str3, String str4);

    public static native int GetPyED(String str, String str2);

    public static native int GetStateED(String str, String str2, int i);

    public static native int GetTestAuthorize();

    public static native int GetVadEd(int i);

    public static native int GetVadSt(int i);

    public static native int GetVersion();

    public static native int Initial(String str, String str2, String str3, String str4, boolean z);

    public static native int InitialDecoder(int i, int i2, int i3, double d);

    public static native int InitialVAD(int i, float f, float f2);

    public static native int LoadRes(String str, String str2, String str3, String str4);

    public static native int ReadLM(String str, String str2, boolean z);

    public static native int ReadSlot(String str);

    public static native int ReadSlotLink(String str);

    public static native int ResetDecoder(int i);

    public static native int ResetVAD(int i);

    public static native int SetCurrNetTreeID(int i, int i2, int i3);

    public static native int SetLogLevel(int i);

    public static native int SetSampleRateMode(int i);

    public static native int SetVADEndCut(int i, boolean z);

    public static native int VADDetect(int i, short[] sArr, int i2, boolean z);

    public static native int VerifyLicense(Context context, String str, String str2, byte[] bArr, int i, byte[] bArr2, String str3);

    public static native int WakeUpBuildNet(String str, String str2);

    public static native int WakeUpDecode(short[] sArr, int i, byte[][] bArr, int i2, boolean z);

    public static native int WakeUpFree();

    public static native int WakeUpInitial(String str, String str2, String str3, String str4, int i);

    static {
        try {
            System.loadLibrary("bdEASRAndroid");
        } catch (UnsatisfiedLinkError e) {
        }
    }

    public static int Load(String resFile, String fLMSlotName, String cLMSlotName, String slotBuffer, boolean fast_mode) {
        if (!new File(resFile).exists()) {
            return -1;
        }
        String f1 = resFile + ":f_1";
        String f3 = resFile + ":f_3";
        String f4 = resFile + ":f_4";
        String f5 = resFile + ":f_5";
        String f6 = resFile + ":f_6";
        String fa = resFile + ":f_a";
        String fb = resFile + ":f_b";
        String fc = resFile + ":f_c";
        String ca = resFile + ":c_a";
        String cb = resFile + ":c_b";
        String cc = resFile + ":c_c";
        if (!ExistFile(f3) || !ExistFile(f4) || !ExistFile(f5) || !ExistFile(f6)) {
            Log.e("EASR", "Load: no initial file");
            return -1;
        } else if (Initial(f5, f6, f3, f4, fast_mode) < 0) {
            return -1;
        } else {
            if (slotBuffer.length() > 0 && BuildSlot(slotBuffer, slotBuffer.length()) < 0) {
                return -1;
            }
            if (ExistFile(fa) && fLMSlotName.length() > 0 && ReadLM(fa, fLMSlotName, false) < 0) {
                return -1;
            }
            if (ExistFile(fb) && ReadSlot(fb) < 0) {
                return -1;
            }
            if (ExistFile(fc) && ReadSlotLink(fc) < 0) {
                return -1;
            }
            if (ExistFile(ca) && ExistFile(cb) && ExistFile(cc) && cLMSlotName.length() > 0 && LoadRes(ca, cLMSlotName, cb, cc) < 0) {
                return -1;
            }
            if (!ExistFile(f1)) {
                Log.e("EASR", "Load: no gram file");
                return -1;
            } else if (BuildNet(-1, f1) < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
