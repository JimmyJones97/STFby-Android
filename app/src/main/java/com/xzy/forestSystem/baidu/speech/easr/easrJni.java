package com.xzy.forestSystem.baidu.speech.easr;

import java.util.ArrayList;

public class easrJni {
    public static final int BDEASR_AUTH_DOWNLOAD_ERROR = 5;
    public static final int BDEASR_AUTH_ERROR = 6;
    public static final int BDEASR_AUTH_OK = 4;
    public static final int BDEASR_EXPECT_RECOG_NUM = 14;
    public static final int BDEASR_IME_PUNCTUATION_ON = 15;
    public static final int BDEASR_INITIAL_ERROR = 1;
    public static final int BDEASR_INITIAL_OK = 0;
    public static final int BDEASR_LM_RES_PATH = 6;
    public static final int BDEASR_LM_SLOT_NAME = 7;
    public static final int BDEASR_MAX_SPEECH_PAUSE_SEC = 4;
    public static final int BDEASR_MAX_SPEECH_SEC = 3;
    public static final String BDEASR_NAVI_NGRAM_SLOT_NAME = "$navi_ngram_LM_LOOP_CORE";
    public static final int BDEASR_NEED_RECOGNITION = 11;
    public static final int BDEASR_NEED_VAD = 9;
    public static final int BDEASR_NEED_WAKEUP = 10;
    public static final String BDEASR_NGRAM_SLOT_NAME = "$ngram_LM_LOOP_CORE";
    public static final int BDEASR_RECOG_ERROR = 10;
    public static final int BDEASR_RECOG_FAST_MODE = 0;
    public static final int BDEASR_RECOG_FIND_FINAL_RESULT = 8;
    public static final int BDEASR_RECOG_OK = 7;
    public static final int BDEASR_RECOG_SOLONG_SPEECH = 9;
    public static final int BDEASR_SAMPLE_RATE = 2;
    public static final int BDEASR_SETPARAM_ERROR = 3;
    public static final int BDEASR_SETPARAM_OK = 2;
    public static final String BDEASR_SLOT_NAME_APP = "app";
    public static final String BDEASR_SLOT_NAME_ARTIST = "artist";
    public static final String BDEASR_SLOT_NAME_NAME = "name";
    public static final String BDEASR_SLOT_NAME_SONG = "song";
    public static final String BDEASR_SLOT_NAME_USER_COMMAND = "usercommand";
    public static final int BDEASR_SUPPORT_LONGSPEECH = 13;
    public static final int BDEASR_TREEID_APP = 13;
    public static final int BDEASR_TREEID_CONTACTS = 14;
    public static final int BDEASR_TREEID_INPUT = 9;
    public static final int BDEASR_TREEID_MAP = 7;
    public static final int BDEASR_TREEID_MESSAGE = 11;
    public static final int BDEASR_TREEID_MUSIC = 12;
    public static final int BDEASR_TREEID_PHONE = 10;
    public static final int BDEASR_TREEID_PLAYER_INSTRUCTION = 19;
    public static final int BDEASR_TREEID_RADIO = 20;
    public static final int BDEASR_TREEID_REFUSAL = 8;
    public static final int BDEASR_TREEID_SETTING = 16;
    public static final int BDEASR_TREEID_TV_INSTRUCTION = 18;
    public static final int BDEASR_TREEID_USER_COMMAND = 21;
    public static final int BDEASR_USE_SSE4 = 12;
    public static final int BDEASR_USE_VADEND_CUT = 5;
    public static final int BDEASR_VAD_SENTENCE_END = 13;
    public static final int BDEASR_WAKEUP_ERROR = 12;
    public static final int BDEASR_WAKEUP_FAST_MODE = 1;
    public static final int BDEASR_WAKEUP_OK = 11;
    public static final int BDEASR_WAKEUP_WORDS = 8;
    public static final ArrayList<String> slots = new ArrayList<>();

    public static native int bdeasrBuildSlot(String str, String str2);

    public static native void bdeasrExit();

    public static native int bdeasrFeedAudioData(short[] sArr, int i, int i2);

    public static native int bdeasrFront(short[] sArr, int i, int i2, int i3);

    public static native String bdeasrGetJSONResult();

    public static native int bdeasrGetVolume();

    public static native int bdeasrInitial(String str, String str2);

    public static native int bdeasrRec();

    public static native int bdeasrSetParam(int i, EASRParamObject eASRParamObject);

    public static native int bdeasrSetSlot(String str, String str2);

    public static native int bdeasrStartRecognition(int[] iArr, int i);

    public static native int bdeasrStartWakeUp();

    public static native int bdeasrStopRecognition();

    public static native int bdeasrStopWakeUp();

    static {
        slots.add("name");
        slots.add("song");
        slots.add("artist");
        slots.add("app");
        slots.add("usercommand");
        try {
            System.loadLibrary("bdEASRAndroid");
        } catch (Exception e) {
        }
    }
}
