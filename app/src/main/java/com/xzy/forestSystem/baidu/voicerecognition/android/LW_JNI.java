package com.xzy.forestSystem.baidu.voicerecognition.android;

import android.util.Log;
import com.xzy.forestSystem.baidu.voicerecognition.android.LibFactory;

/* compiled from: LibFactory */
class LW_JNI implements LibFactory.JNI {
    private LJNI mEngine = new LJNI();

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int Init() {
        return this.mEngine.lwInit();
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int Exit() {
        return this.mEngine.lwExit();
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int SendData(short[] pDataBuf, int iLen) {
        return this.mEngine.lwSendData(pDataBuf, iLen);
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int GetCallbackData(byte[] pDataBuf, int iLen) {
        return this.mEngine.lwGetCallbackData(pDataBuf, iLen);
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int Detect() {
        return this.mEngine.lwDetect();
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int SetParam(int type, int val) {
        return this.mEngine.lwSetParam(type, val);
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int GetParam(int type) {
        Log.d("LW", "The method is invalid in MFE LIB");
        return 0;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int EnableNoiseDetection(boolean val) {
        Log.d("LW", "The method is invalid in MFE LIB");
        return 0;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public void SetLogLevel(int iLevel) {
        Log.d("LW", "The method is invalid in MFE LIB");
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int GetVADVersion() {
        return this.mEngine.lwGetVADVersion();
    }
}
