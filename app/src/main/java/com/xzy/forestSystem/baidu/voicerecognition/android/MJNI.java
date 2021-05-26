package com.xzy.forestSystem.baidu.voicerecognition.android;

public class MJNI {
    public native void bvEncoderExit();

    public native int bvEncoderInit();

    public native int mfeDetect();

    public native int mfeEnableNoiseDetection(boolean z);

    public native int mfeExit();

    public native int mfeGetCallbackData(byte[] bArr, int i);

    public native int mfeGetParam(int i);

    public native int mfeInit();

    public native int mfeSendData(short[] sArr, int i);

    public native void mfeSetLogLevel(int i);

    public native int mfeSetParam(int i, int i2);

    public native int pcm2bv(byte[] bArr, int i, byte[] bArr2, int i2, int i3, boolean z);
}
