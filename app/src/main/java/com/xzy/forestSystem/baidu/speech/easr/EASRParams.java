package com.xzy.forestSystem.baidu.speech.easr;

import  com.xzy.forestSystem.gogisapi.Common.Constant;
import org.json.JSONObject;

public class EASRParams {
    public static final int PROP_APP = 10003;
    public static final int PROP_ASSISTANT = 10058;
    public static final int PROP_CAR = 10053;
    public static final int PROP_CATERING = 10054;
    public static final int PROP_COMMAND = 100021;
    public static final int PROP_CONTACTS = 100014;
    public static final int PROP_DELIMITER = 100000;
    public static final int PROP_FINANCE = 10055;
    public static final int PROP_GAME = 10056;
    public static final int PROP_HEALTH = 10007;
    public static final int PROP_INPUT = 20000;
    public static final int PROP_MAP = 10060;
    public static final int PROP_MEDICAL = 10052;
    public static final int PROP_MUSIC = 10001;
    public static final int PROP_PHONE = 10008;
    public static final int PROP_PLAYER = 100019;
    public static final int PROP_RADIO = 100020;
    public static final int PROP_RECIPES = 10057;
    public static final int PROP_SEARCH = 10005;
    public static final int PROP_SETTING = 100016;
    public static final int PROP_SHOPPING = 10006;
    public static final int PROP_SONG = 10009;
    public static final int PROP_TV = 100018;
    public static final int PROP_VIDEO = 10002;
    public static final int PROP_WEB = 10004;
    public String asrDataFilePath = null;
    int expectRecogNum = 1;
    public int imePunctuationOn = 1;
    public String licenseFilePath = null;
    public String lmResPath = null;
    float maxSpeechPauseSec = 1.6f;
    float maxSpeechSec = 15.0f;
    int needRecog = 1;
    int needVAD = 1;
    int needWakeUp = 0;
    public int prop = PROP_INPUT;
    int recogFastMode = 0;
    int sampleRate = Constant.SAMPLE_16K;
    public JSONObject slotData = null;
    int supportLongSpeech = 0;
    int useSSE4 = 0;
    int useVADEndCut = 1;
    int wakeUpFastMode = 1;
    String wakeUpWords = null;

    public void validate() {
        if (this.asrDataFilePath == null) {
            this.asrDataFilePath = "";
        }
        if (this.lmResPath == null) {
            this.lmResPath = "";
        }
        if (this.licenseFilePath == null) {
            this.licenseFilePath = "";
        }
        if (this.wakeUpWords == null) {
            this.wakeUpWords = "";
        }
    }
}
