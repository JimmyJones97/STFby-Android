package com.xzy.forestSystem.baidu.speech.easr;

import com.xzy.forestSystem.baidu.voicerecognition.android.NoProGuard;
import com.xzy.forestSystem.baidu.voicerecognition.android.NoProGuard;

public class EASRParamObject implements NoProGuard {
    public float floatValue = -1.0f;
    public int intValue = -1;
    public String stringValue = "";

    public EASRParamObject(int intValue2) {
        this.intValue = intValue2;
    }

    public EASRParamObject(float floatValue2) {
        this.floatValue = floatValue2;
    }

    public EASRParamObject(String stringValue2) {
        this.stringValue = stringValue2;
    }
}
