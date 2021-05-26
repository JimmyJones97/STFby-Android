package com.xzy.forestSystem.baidu.voicerecognition.android;

import com.xzy.forestSystem.baidu.voicerecognition.android.LibFactory;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* compiled from: LibFactory */
/* access modifiers changed from: package-private */
public class NO_JNI implements LibFactory.JNI {
    private boolean isFirst = true;
    private boolean isNull = false;
    private int length = 0;
    private int sampleRate = 8000;
    private byte[] temp;

    NO_JNI() {
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int Init() {
        this.isFirst = true;
        this.isNull = false;
        this.length = 0;
        return 0;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int Exit() {
        return 0;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int SendData(short[] pDataBuf, int iLen) {
        if (pDataBuf == null || iLen == 0) {
            this.isNull = true;
        } else {
            if (iLen > pDataBuf.length) {
                iLen = pDataBuf.length;
            }
            this.length = iLen;
            ByteBuffer buffer = ByteBuffer.allocate(pDataBuf.length * 2);
            buffer.clear();
            buffer.order(ByteOrder.nativeOrder());
            for (int i = 0; i < this.length; i++) {
                buffer.putShort(i * 2, pDataBuf[i]);
            }
            this.temp = buffer.array();
        }
        return 0;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int GetCallbackData(byte[] pDataBuf, int iLen) {
        int len;
        if (this.isNull) {
            return 0;
        }
        if (this.isFirst) {
            if (this.sampleRate == 16000) {
                pDataBuf[0] = 5;
            } else if (this.sampleRate == 8000) {
                pDataBuf[0] = 1;
            }
            pDataBuf[1] = 0;
            pDataBuf[2] = 0;
            pDataBuf[3] = 0;
            for (int i = 0; i < this.temp.length; i++) {
                pDataBuf[i + 4] = this.temp[i];
            }
            len = (this.length * 2) + 4;
            this.isFirst = false;
        } else {
            for (int i2 = 0; i2 < this.temp.length; i2++) {
                pDataBuf[i2] = this.temp[i2];
            }
            len = this.length * 2;
        }
        return len;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int Detect() {
        if (this.isNull) {
            return 2;
        }
        return 1;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int SetParam(int type, int val) {
        if (type != 13) {
            return LibFactory.PARAMRANGE_ERR;
        }
        this.sampleRate = val;
        return 0;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int GetParam(int type) {
        return 0;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int EnableNoiseDetection(boolean val) {
        return 0;
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public void SetLogLevel(int iLevel) {
    }

    @Override // com.baidu.voicerecognition.android.LibFactory.JNI
    public int GetVADVersion() {
        return 0;
    }
}
