package com.xzy.forestSystem.baidu.speech;

import android.content.Context;
import com.xzy.forestSystem.baidu.voicerecognition.android.Device;
import com.xzy.forestSystem.baidu.voicerecognition.android.Util;
import com.xzy.forestSystem.baidu.voicerecognition.android.Utility;
import  com.xzy.forestSystem.gogisapi.Common.Constant;
import com.xzy.forestSystem.baidu.voicerecognition.android.Device;
import com.xzy.forestSystem.baidu.voicerecognition.android.Util;
import com.xzy.forestSystem.baidu.voicerecognition.android.Utility;

import java.io.File;

class Policy {
    Policy() {
    }

    public static int sample(Context context) {
        if (Utility.is2G(context)) {
            return 8000;
        }
        return Constant.SAMPLE_16K;
    }

    public static int taskTimeout() {
        return 30000;
    }

    public static String uiRetryFile(Context context) {
        return new File(context.getCacheDir(), "bd_asr_ui_repeat.pcm").toString();
    }

    public static String app(Context context) {
        return context.getPackageName();
    }

    public static String pfm(Context context) {
        return Util.pfm(context);
    }

    public static String uid(Context context) {
        return Device.getDeviceID(context);
    }

    public static String ver(Context context) {
        return VoiceRecognitionService.getSdkVersion();
    }
}
