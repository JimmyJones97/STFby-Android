package com.xzy.forestSystem.baidu.voicerecognition.android;

public class LJNI {
    public native int lwDetect();

    public native int lwExit();

    public native int lwGetCallbackData(byte[] bArr, int i);

    public native int lwGetVADVersion();

    public native int lwInit();

    public native int lwSendData(short[] sArr, int i);

    public native int lwSetParam(int i, int i2);
}
