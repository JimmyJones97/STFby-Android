package com.xzy.forestSystem.baidu.voicerecognition.android;

import android.util.Log;
import com.xzy.forestSystem.baidu.voicerecognition.android.LibFactory;
import java.util.logging.Logger;

/* compiled from: LibFactory */
/* access modifiers changed from: package-private */
public class MFE_JNI implements LibFactory.JNI {
    private static final String TAG = "MFE_JNI";
    private static final Logger logger = Logger.getLogger(TAG);
    private MJNI mEngine = new MJNI();

    public MFE_JNI() {
        logger.fine("calling new MJNI()");
        logger.fine("called  new MJNI()");
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int Init() {
        logger.fine("calling mfeInit()");
        int r = this.mEngine.mfeInit();
        logger.fine("called  mfeInit() : " + r);
        return r;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int Exit() {
        logger.fine("calling mfeExit()");
        logger.fine("called  mfeExit() : " + this.mEngine.mfeExit());
        return 0;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int SendData(short[] pDataBuf, int iLen) {
        logger.fine("calling mfeSendData()");
        int r = this.mEngine.mfeSendData(pDataBuf, iLen);
        logger.fine("called  mfeSendData() : " + r);
        return r;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int GetCallbackData(byte[] pDataBuf, int iLen) {
        logger.fine("calling mfeGetCallbackData()");
        int r = this.mEngine.mfeGetCallbackData(pDataBuf, iLen);
        logger.fine("called  mfeGetCallbackData() : " + r);
        return r;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int Detect() {
        logger.fine("calling mfeDetect()");
        int r = this.mEngine.mfeDetect();
        logger.fine("called  mfeDetect() : " + r);
        return r;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int SetParam(int type, int val) {
        logger.fine("calling mfeSetParam()");
        int r = this.mEngine.mfeSetParam(type, val);
        logger.fine("called  mfeSetParam() : " + r);
        return r;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int GetParam(int type) {
        logger.fine("calling mfeGetParam()");
        int r = this.mEngine.mfeGetParam(type);
        logger.fine("called  mfeGetParam()" + r);
        return r;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int EnableNoiseDetection(boolean val) {
        logger.fine("calling mfeEnableNoiseDetection(" + val + ")");
        int r = this.mEngine.mfeEnableNoiseDetection(val);
        logger.fine("called  mfeEnableNoiseDetection(" + val + ")" + r);
        return r;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public void SetLogLevel(int iLevel) {
        logger.fine("calling mfeSetLogLevel(" + iLevel + ")");
        this.mEngine.mfeSetLogLevel(iLevel);
        logger.fine("called  mfeSetLogLevel(" + iLevel + ")");
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int GetVADVersion() {
        Log.d("MFE", "The method is invalid in MFE LIB");
        return 0;
    }
}
