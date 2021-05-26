package com.xzy.forestSystem.baidu.speech.easr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.xzy.forestSystem.baidu.speech.easr.stat.SynthesizeResultDb;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import com.xzy.forestSystem.baidu.speech.easr.stat.SynthesizeResultDb;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmbeddedASREngine {
    public static final int ASR_DATA_FILE_NOT_EXISTS = 7051;
    public static final int AUTH_FAILED_1 = 8001;
    public static final int AUTH_FAILED_7 = 8007;
    public static final int AUTH_FAILED_CUID = 8004;
    public static final int AUTH_FAILED_LICENSE_EXPIRED = 8005;
    public static final int AUTH_FAILED_PACKAGE_NAME = 8002;
    public static final int AUTH_FAILED_SIGNATURE = 8003;
    public static final int AUTH_FAILED_UNKNOWN = 8000;
    public static final int AUTH_PASSED = 0;
    public static final int AUTH_SERVER_ERR_CODE_BASE = 9000;
    private static final String DEFAULT_LICENCE_FILE_NAME = "baidu_asr_licence.dat";
    private static String DEFAULT_LICENCE_FILE_PATH = null;
    private static final int EMBEDDED_ENGINE_ERROR_TYPE = 0;
    public static final int ERROR_EMBEDDED = 7000;
    public static final int ERROR_EMBEDDED_NOT_CONFIDENCE = 7998;
    public static final int ERROR_LANGUAGE_NOT_SUPPORTED = 7060;
    public static final int ERROR_PROP_NOT_SET = 7056;
    public static final int ERROR_PROP_NOT_SUPPORTED = 7058;
    public static final int ERROR_SAMPLE_RATE_NOT_SUPPORTED = 7057;
    public static final int ERROR_SLOT_DATA_LIMIT_EXCEEDS = 7055;
    public static final int ERROR_SLOT_DATA_NOT_VALID = 7059;
    private static final String EXPIRED_PREFIX = "[百度语音试用服务已经到期，请及时更新授权]";
    public static final int LM_RES_DATA_FILE_NOT_EXISTS = 7052;
    public static final int PARAM_ERROR_PREFIX = 7000;
    public static final int PARAM_ERROR_TYPE = 100;
    private static final String TAG = "EmbeddedASREngine";
    private static final String TRIAL_PREFIX = "[百度语音试用服务%d天后到期]";
    private static final Logger logger = Logger.getLogger(TAG);
    private static Context mContext;
    private static final String mDebugAudioFilePath = (Environment.getExternalStorageDirectory() + "/easr/debug.pcm");
    private static final String mDsDebugAudioFilePath = (Environment.getExternalStorageDirectory() + "/easr/ds_debug.pcm");
    private static String mInitedDataFilePath = null;
    private static String mInitedLmResFilePath = null;
    private static boolean mIsInited = false;
    private static boolean mIsLicenseDownloaded = false;
    private static boolean mIsTrialLicense = true;
    private static SynthesizeResultDb mStatDb;
    private static String mTrialPrefix = "";
    private static final boolean mWriteAudioFile = false;
    private static EmbeddedASREngine sASREngineInstance;

    @SuppressLint("WrongConstant")
    private EmbeddedASREngine() {
        if (Log.isLoggable("baidu_speech", 3)) {
            com.xzy.forestSystem.baidu.speech.easr.easrNativeJni.SetLogLevel(5);
        } else {
            com.xzy.forestSystem.baidu.speech.easr.easrNativeJni.SetLogLevel(0);
        }
    }

    public static synchronized EmbeddedASREngine getInstance(Context context) {
        EmbeddedASREngine embeddedASREngine;
        synchronized (EmbeddedASREngine.class) {
            if (sASREngineInstance == null) {
                sASREngineInstance = new EmbeddedASREngine();
            }
            EmbeddedASREngine embeddedASREngine2 = sASREngineInstance;
            if (mContext != context) {
                EmbeddedASREngine embeddedASREngine3 = sASREngineInstance;
                mContext = context;
                try {
                    DEFAULT_LICENCE_FILE_PATH = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.dataDir + FileSelector_Dialog.sRoot + DEFAULT_LICENCE_FILE_NAME;
                    logger.info("default license path: " + DEFAULT_LICENCE_FILE_PATH);
                } catch (PackageManager.NameNotFoundException e) {
                    logger.info("Error package name not found " + e);
                }
                EmbeddedASREngine embeddedASREngine4 = sASREngineInstance;
                EmbeddedASREngine embeddedASREngine5 = sASREngineInstance;
                mStatDb = SynthesizeResultDb.getInstance(mContext);
                EmbeddedASREngine embeddedASREngine6 = sASREngineInstance;
                mStatDb.open();
            }
            embeddedASREngine = sASREngineInstance;
        }
        return embeddedASREngine;
    }

    private synchronized int initEngine(boolean forceReInit, EASRParams params, Integer appid) {
        int initError;
        int setParamRet = setParams(params);
        if (setParamRet != 2) {
            initError = setParamRet;
        } else {
            if (!mIsInited) {
                initError = initEngine(params, appid);
                if (initError == 0) {
                    mIsInited = true;
                }
            } else if (forceReInit || !params.asrDataFilePath.equals(mInitedDataFilePath) || !params.lmResPath.equals(mInitedLmResFilePath)) {
                logger.info("ReInit Embedded ASR Engine from " + mInitedDataFilePath + " to " + params.asrDataFilePath);
                logger.info("ReInit Embedded ASR Engine from " + mInitedLmResFilePath + " to " + params.lmResPath);
                if (mIsInited) {
                    easrJni.bdeasrExit();
                    mIsInited = false;
                }
                initError = initEngine(params, appid);
                if (initError == 0) {
                    mIsInited = true;
                }
            }
            initError = 0;
        }
        return initError;
    }

    private int initEngine(EASRParams params, Integer appid) {
        int ret = auth(mContext, appid, params.licenseFilePath);
        if (ret != 0) {
            logger.severe("auth failed! code = " + ret);
            return convertAuthErrorToSDKError(ret);
        }
        logger.info(params.asrDataFilePath);
        if (!new File(params.asrDataFilePath).exists()) {
            return ASR_DATA_FILE_NOT_EXISTS;
        }
        long preInit = System.currentTimeMillis();
        Iterator i$ = easrJni.slots.iterator();
        while (i$.hasNext()) {
            String slot = (String) i$.next();
            int ret2 = easrJni.bdeasrSetSlot("$" + slot + "_CORE", "测试\n");
            if (ret2 != 2) {
                logger.severe(String.format("bdeasrSetSlot $%s_CORE failed! ret = %d", slot, Integer.valueOf(ret2)));
                return generateEmbeddedErrorNumber(0, 3);
            }
        }
        int ret3 = easrJni.bdeasrInitial("", params.asrDataFilePath);
        mInitedDataFilePath = params.asrDataFilePath;
        mInitedLmResFilePath = params.lmResPath;
        logger.info("Init time is: " + (System.currentTimeMillis() - preInit) + "ms");
        if (ret3 == 0) {
            return 0;
        }
        logger.severe("bdEASREngineInit failed! ret = " + ret3);
        return generateEmbeddedErrorNumber(0, 1);
    }

    private int convertAuthErrorToSDKError(int authErrorCode) {
        switch (authErrorCode) {
            case com.xzy.forestSystem.baidu.speech.easr.easrNativeJni.VERIFY_LICENSE_FAIL_7 /* -7 */:
                return AUTH_FAILED_7;
            case com.xzy.forestSystem.baidu.speech.easr.easrNativeJni.VERIFY_LICENSE_WILL_EXPIRED /* -6 */:
            default:
                return authErrorCode;
            case com.xzy.forestSystem.baidu.speech.easr.easrNativeJni.VERIFY_LICENSE_EXPIRED /* -5 */:
                return AUTH_FAILED_LICENSE_EXPIRED;
            case com.xzy.forestSystem.baidu.speech.easr.easrNativeJni.VERIFY_LICENSE_FAIL_CUID /* -4 */:
                return AUTH_FAILED_CUID;
            case com.xzy.forestSystem.baidu.speech.easr.easrNativeJni.VERIFY_LICENSE_FAIL_SIGNATURE /* -3 */:
                return AUTH_FAILED_SIGNATURE;
            case -2:
                return AUTH_FAILED_PACKAGE_NAME;
            case -1:
                return AUTH_FAILED_1;
        }
    }

    /* access modifiers changed from: package-private */
    public int GetLicense2Way(Context context, Integer appid, String cuid, String stat, String license_file) {
        int r1 = com.xzy.forestSystem.baidu.speech.easr.easrNativeJni.GetLicense(context, appid == null ? "" : appid.toString(), cuid, stat, license_file);
        logger.info("GetLicense getLicenseRet " + r1 + ", licensePath: " + license_file + ", appid: " + appid);
        logger.info("cuid: " + cuid + ", stat: " + stat);
        if (r1 < 0) {
            int r2 = com.xzy.forestSystem.baidu.speech.easr.easrNativeJni.GetLicense(context, "", cuid, stat, license_file);
            logger.info("GetLicense getLicenseRet " + r2 + ", licensePath: " + license_file + ", appid: ");
            logger.info("cuid: " + cuid + ", stat: " + stat);
            if (r2 >= 0) {
                return r2;
            }
        }
        return r1;
    }

    /* access modifiers changed from: package-private */
    public int VerifyLicense2Way(Context context, Integer appid, String cuid, byte[] license, int len, byte[] appID, String logDir) {
        int r1 = com.xzy.forestSystem.baidu.speech.easr.easrNativeJni.VerifyLicense(mContext, appid == null ? "" : appid.toString(), cuid, license, len, appID, logDir);
        if (r1 >= 0) {
            return r1;
        }
        return easrNativeJni.VerifyLicense(mContext, "", cuid, license, len, appID, logDir);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000e, code lost:
        if (new java.io.File(com.baidu.speech.easr.EmbeddedASREngine.DEFAULT_LICENCE_FILE_PATH).exists() != false) goto L_0x0010;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized int auth(final android.content.Context r29, final java.lang.Integer r30, final java.lang.String r31) {
        /*
        // Method dump skipped, instructions count: 648
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.speech.easr.EmbeddedASREngine.auth(android.content.Context, java.lang.Integer, java.lang.String):int");
    }

    public boolean isTrialLicense(String licensePath, Integer appid) {
        auth(mContext, appid, licensePath);
        return mIsTrialLicense;
    }

    public String getTrialPrefix() {
        return mTrialPrefix;
    }

    private int generateEmbeddedErrorNumber(int type, int index) {
        return (type * 100) + 7000 + index;
    }

    private int setParams(EASRParams params) {
        if (!new File(params.asrDataFilePath).exists()) {
            return ASR_DATA_FILE_NOT_EXISTS;
        }
        if ((params.prop == 20000 || params.prop == 10060) && !new File(params.lmResPath).exists()) {
            return LM_RES_DATA_FILE_NOT_EXISTS;
        }
        int ret = easrJni.bdeasrSetParam(0, new EASRParamObject(params.recogFastMode));
        if (ret != 2) {
            logger.severe("bdeasrSetParam error! ret: " + ret);
            return generateEmbeddedErrorNumber(ret, 0);
        }
        int ret2 = easrJni.bdeasrSetParam(1, new EASRParamObject(params.wakeUpFastMode));
        if (ret2 != 2) {
            logger.severe("bdeasrSetParam error! ret: " + ret2);
            return generateEmbeddedErrorNumber(ret2, 1);
        }
        int ret3 = easrJni.bdeasrSetParam(2, new EASRParamObject(params.sampleRate));
        if (ret3 != 2) {
            logger.severe("bdeasrSetParam error! ret: " + ret3);
            return generateEmbeddedErrorNumber(ret3, 2);
        }
        int ret4 = easrJni.bdeasrSetParam(3, new EASRParamObject(params.maxSpeechSec));
        if (ret4 != 2) {
            logger.severe("bdeasrSetParam error! ret: " + ret4);
            return generateEmbeddedErrorNumber(ret4, 3);
        }
        int ret5 = easrJni.bdeasrSetParam(4, new EASRParamObject(params.maxSpeechPauseSec));
        if (ret5 != 2) {
            logger.severe("bdeasrSetParam error! ret: " + ret5);
            return generateEmbeddedErrorNumber(ret5, 4);
        }
        int ret6 = easrJni.bdeasrSetParam(5, new EASRParamObject(params.useVADEndCut));
        if (ret6 != 2) {
            logger.severe("bdeasrSetParam error! ret: " + ret6);
            return generateEmbeddedErrorNumber(ret6, 5);
        }
        String lmSlotName = "";
        if (params.prop == 20000) {
            lmSlotName = easrJni.BDEASR_NGRAM_SLOT_NAME;
        } else if (params.prop == 10060) {
            lmSlotName = easrJni.BDEASR_NAVI_NGRAM_SLOT_NAME;
        }
        if (!"".equals(lmSlotName)) {
            int ret7 = easrJni.bdeasrSetParam(6, new EASRParamObject(params.lmResPath));
            if (ret7 != 2) {
                logger.severe("bdeasrSetParam error! ret: " + ret7);
                return generateEmbeddedErrorNumber(ret7, 6);
            }
        } else {
            int ret8 = easrJni.bdeasrSetParam(6, new EASRParamObject(""));
            if (ret8 != 2) {
                logger.severe("bdeasrSetParam error! ret: " + ret8);
                return generateEmbeddedErrorNumber(ret8, 6);
            }
        }
        int ret9 = easrJni.bdeasrSetParam(7, new EASRParamObject(lmSlotName));
        if (ret9 != 2) {
            logger.severe("bdeasrSetParam error! ret: " + ret9);
            return generateEmbeddedErrorNumber(ret9, 7);
        }
        int ret10 = easrJni.bdeasrSetParam(8, new EASRParamObject(params.wakeUpWords));
        if (ret10 != 2) {
            logger.severe("bdeasrSetParam error! ret: " + ret10);
            return generateEmbeddedErrorNumber(ret10, 8);
        }
        int ret11 = easrJni.bdeasrSetParam(9, new EASRParamObject(params.needVAD));
        if (ret11 != 2) {
            logger.severe("bdeasrSetParam error! ret: " + ret11);
            return generateEmbeddedErrorNumber(ret11, 9);
        }
        int ret12 = easrJni.bdeasrSetParam(10, new EASRParamObject(params.needWakeUp));
        if (ret12 != 2) {
            logger.severe("bdeasrSetParam error! ret: " + ret12);
            return generateEmbeddedErrorNumber(ret12, 10);
        }
        int ret13 = easrJni.bdeasrSetParam(11, new EASRParamObject(params.needRecog));
        if (ret13 != 2) {
            logger.severe("bdeasrSetParam error! ret: " + ret13);
            return generateEmbeddedErrorNumber(ret13, 11);
        }
        int ret14 = easrJni.bdeasrSetParam(12, new EASRParamObject(params.useSSE4));
        if (ret14 != 2) {
            logger.severe("bdeasrSetParam error! ret: " + ret14);
            return generateEmbeddedErrorNumber(ret14, 12);
        }
        int ret15 = easrJni.bdeasrSetParam(13, new EASRParamObject(params.supportLongSpeech));
        if (ret15 != 2) {
            logger.severe("bdeasrSetParam error! ret: " + ret15);
            return generateEmbeddedErrorNumber(ret15, 13);
        }
        int ret16 = easrJni.bdeasrSetParam(14, new EASRParamObject(params.expectRecogNum));
        if (ret16 != 2) {
            logger.severe("bdeasrSetParam error! ret: " + ret16);
            return generateEmbeddedErrorNumber(ret16, 14);
        }
        int ret17 = easrJni.bdeasrSetParam(15, new EASRParamObject(params.imePunctuationOn));
        if (ret17 == 2) {
            return ret17;
        }
        logger.severe("bdeasrSetParam error! ret: " + ret17);
        return generateEmbeddedErrorNumber(ret17, 15);
    }

    public int startRecognizing(EASRParams params, Integer appid) {
        int[] treeIDArray;
        params.validate();
        int initRet = initEngine(false, params, appid);
        if (initRet != 0) {
            return initRet;
        }
        switch (params.prop) {
            case EASRParams.PROP_MUSIC /* 10001 */:
                treeIDArray = new int[]{8, 12};
                break;
            case EASRParams.PROP_APP /* 10003 */:
                treeIDArray = new int[]{8, 13};
                break;
            case EASRParams.PROP_PHONE /* 10008 */:
                treeIDArray = new int[]{8, 10, 11};
                break;
            case EASRParams.PROP_MAP /* 10060 */:
                treeIDArray = new int[]{7};
                break;
            case EASRParams.PROP_CONTACTS /* 100014 */:
                treeIDArray = new int[]{8, 14};
                break;
            case EASRParams.PROP_SETTING /* 100016 */:
                treeIDArray = new int[]{8, 16};
                break;
            case EASRParams.PROP_TV /* 100018 */:
                treeIDArray = new int[]{8, 18};
                break;
            case EASRParams.PROP_PLAYER /* 100019 */:
                treeIDArray = new int[]{8, 19};
                break;
            case EASRParams.PROP_RADIO /* 100020 */:
                treeIDArray = new int[]{8, 20};
                break;
            case EASRParams.PROP_COMMAND /* 100021 */:
                treeIDArray = new int[]{8, 21};
                break;
            default:
                treeIDArray = new int[]{9};
                break;
        }
        try {
            int buildRet = buildSlot(treeIDArray, params.slotData);
            if (buildRet != 0) {
                return buildRet;
            }
            StringBuilder treeIDs = new StringBuilder();
            for (int id : treeIDArray) {
                treeIDs.append(id);
                treeIDs.append(" ");
            }
            logger.info("treeIDs: " + treeIDs.toString());
            int startRet = easrJni.bdeasrStartRecognition(treeIDArray, treeIDArray.length);
            if (startRet != 7) {
                logger.severe("bdeasrStartRecognition error: " + startRet);
                return generateEmbeddedErrorNumber(0, startRet);
            }
            logger.info("bdeasrStartRecognition OK");
            return 0;
        } catch (JSONException e) {
            e.printStackTrace();
            return ERROR_SLOT_DATA_NOT_VALID;
        }
    }

    private int buildSlot(int[] treeID, JSONObject slotData) throws JSONException {
        if (slotData == null) {
            return 0;
        }
        for (int i : treeID) {
            switch (i) {
                case 10:
                case 11:
                case 14:
                    int ret = buildSlot("name", slotData);
                    if (ret == 0) {
                        break;
                    } else {
                        return ret;
                    }
                case 12:
                    int ret2 = buildSlot("song", slotData);
                    if (ret2 != 0) {
                        return ret2;
                    }
                    int ret3 = buildSlot("artist", slotData);
                    if (ret3 == 0) {
                        break;
                    } else {
                        return ret3;
                    }
                case 13:
                    int ret4 = buildSlot("app", slotData);
                    if (ret4 == 0) {
                        break;
                    } else {
                        return ret4;
                    }
                case 21:
                    if (!slotData.has("usercommand")) {
                        continue;
                    } else if (slotData.getJSONArray("usercommand").length() > 10) {
                        return ERROR_SLOT_DATA_LIMIT_EXCEEDS;
                    } else {
                        int ret5 = buildSlot("usercommand", slotData);
                        if (ret5 == 0) {
                            break;
                        } else {
                            return ret5;
                        }
                    }
            }
        }
        return 0;
    }

    private int buildSlot(String slotName, JSONObject slotData) throws JSONException {
        int ret;
        if (!slotData.has("name") || (ret = easrJni.bdeasrBuildSlot("$" + slotName + "_CORE", getSlotString(slotData.getJSONArray(slotName)))) == 2) {
            return 0;
        }
        return ret;
    }

    private String getSlotString(JSONArray list) throws JSONException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.length(); i++) {
            sb.append(list.getString(i));
            sb.append("\n");
        }
        return sb.toString();
    }

    public int startWakeup() {
        return 0;
    }

    public int newAudioData(short[] audioBuffer, int offset, int length, int isEnd) {
        logger.info("bdeasrFront: " + length);
        return easrJni.bdeasrFront(audioBuffer, offset, length, isEnd);
    }

    public int newAudioData(short[] audioBuffer, int offset, int length) {
        logger.info("bdeasrFeedAudioData: " + length);
        return easrJni.bdeasrFeedAudioData(audioBuffer, offset, length);
    }

    public int recognize() {
        return easrJni.bdeasrRec();
    }

    public String getJSONResult() {
        return easrJni.bdeasrGetJSONResult();
    }

    public int stopRecognizing() {
        return easrJni.bdeasrStopRecognition();
    }

    public void stopWakeup() {
    }

    public static void releaseRecognizer() {
        if (sASREngineInstance != null) {
            synchronized (sASREngineInstance) {
                if (mIsInited) {
                    easrJni.bdeasrExit();
                    mIsInited = false;
                }
                EmbeddedASREngine embeddedASREngine = sASREngineInstance;
                synchronized (mStatDb) {
                    EmbeddedASREngine embeddedASREngine2 = sASREngineInstance;
                    if (!mStatDb.isDatabaseClosed()) {
                        EmbeddedASREngine embeddedASREngine3 = sASREngineInstance;
                        mStatDb.close();
                        SynthesizeResultDb.releaseInstance();
                    }
                }
                EmbeddedASREngine embeddedASREngine4 = sASREngineInstance;
                mContext = null;
                sASREngineInstance = null;
            }
        }
    }

    public void addOneRecord(int errorCode) {
        new AddPVResultsToDB(errorCode).start();
    }

    /* access modifiers changed from: private */
    public class AddPVResultsToDB extends Thread {
        private int mErrorCode;

        public AddPVResultsToDB(int errorCode) {
            this.mErrorCode = errorCode;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            synchronized (EmbeddedASREngine.mStatDb) {
                if (!EmbeddedASREngine.mStatDb.isDatabaseClosed()) {
                    EmbeddedASREngine.mStatDb.addSynthesizeResult(System.currentTimeMillis(), this.mErrorCode, 0, 0, "");
                }
            }
        }
    }

    private static byte[] shortArrayToByteArray(short[] shortArray) {
        int length = shortArray.length;
        ByteBuffer buffer = ByteBuffer.allocate(shortArray.length * 2);
        buffer.clear();
        buffer.order(ByteOrder.nativeOrder());
        for (int i = 0; i < length; i++) {
            buffer.putShort(i * 2, shortArray[i]);
        }
        return buffer.array();
    }
}
