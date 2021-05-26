package com.xzy.forestSystem.baidu.speech;

import android.content.Context;
import com.xzy.forestSystem.baidu.speech.Results;
import com.xzy.forestSystem.baidu.speech.easr.EASRParams;
import com.xzy.forestSystem.baidu.speech.easr.EmbeddedASREngine;
import com.xzy.forestSystem.baidu.voicerecognition.android.Utility;
import com.xzy.forestSystem.baidu.speech.easr.EASRParams;
import com.xzy.forestSystem.baidu.speech.easr.EmbeddedASREngine;
import com.xzy.forestSystem.baidu.voicerecognition.android.Utility;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.json.JSONObject;

public final class MergedDecoder extends AbsStreamDecoder {
    private static final int ADD_NOTICE_FREQ = 20;
    private static final int DECODER_ID_OFFLINE = 1;
    private static final int DECODER_ID_ONLINE = 0;
    private static int mDecodeCount = 0;
    private final HashMap<Integer, InputStream> mCachedStreams = new HashMap<>();
    private AbsStreamDecoder[] mDecoders;
    private Results.Result mLockedResult;
    private final Object mSwitchLock = new byte[0];
    private final String mTempLicenseNotice;
    private final int preferredDecoder;
    private final int secondaryDecoder;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public MergedDecoder(Context context, Map<String, Object> params) throws Exception {
        super(context, params);
        int i = 1;
        Object setDecoder = params.get("decoder-merge.preferred");
        if (setDecoder != null) {
            this.preferredDecoder = ((Integer) setDecoder).intValue();
        } else {
            this.preferredDecoder = isPropPreferOffline(getIntOrThrow(params, "decoder-offline.prop", EASRParams.PROP_SEARCH)) ? 1 : 0;
        }
        this.secondaryDecoder = this.preferredDecoder != 0 ? 0 : i;
        String licenseFilePath = (String) params.get("decoder-offline.license-file-path");
        Integer appid = (Integer) params.get("basic.appid");
        if (mDecodeCount % 20 != 0 || !EmbeddedASREngine.getInstance(context).isTrialLicense(licenseFilePath, appid)) {
            this.mTempLicenseNotice = "";
        } else {
            this.mTempLicenseNotice = EmbeddedASREngine.getInstance(context).getTrialPrefix();
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.baidu.speech.AbsStreamDecoder
    public void onCreate() throws Exception {
        this.mDecoders = new AbsStreamDecoder[]{null, null};
        if (this.preferredDecoder == 0) {
            try {
                if (!Utility.isNetworkConnected(this.context)) {
                    throw new Exception(String.format("%s. network unavailable.", AsrSession.ERROR_NETWORK));
                }
                createOnlineDecoderIfNeeded();
            } catch (Exception e) {
                this.logger.info("MulThreadDecoder onCreate Exception: " + e);
                synchronized (this.mSwitchLock) {
                    if (this.mDecoders[0] != null) {
                        this.mDecoders[0].close();
                        this.mDecoders[0] = null;
                    }
                    if (OfflineDecoder.check(this.mParams)) {
                        createOfflineDecoderIfNeeded();
                    } else {
                        e.printStackTrace();
                        throw new Exception(getMessage(e) + "(cannot switch to offline: params not supported.)", e);
                    }
                }
            }
        } else {
            try {
                createOfflineDecoderIfNeeded();
            } catch (Exception e2) {
                this.logger.info("OfflineDecoder onCreate Exception: " + e2);
                synchronized (this.mSwitchLock) {
                    if (this.mDecoders[1] != null) {
                        this.mDecoders[1].close();
                        this.mDecoders[1] = null;
                    }
                    if (Utility.isNetworkConnected(this.context)) {
                        createOnlineDecoderIfNeeded();
                    } else {
                        e2.printStackTrace();
                        throw new Exception(getMessage(e2) + "(cannot switch to online: network unavailable.)", e2);
                    }
                }
            }
        }
    }

    private synchronized void createOnlineDecoderIfNeeded() throws Exception {
        appendResult(new MessageResult(new JSONObject().put("engine_type", 0), 0));
        if (this.mDecoders[0] == null) {
            MulThreadDecoder proxyOnlineDecoder = new MulThreadDecoder(this.mParams);
            this.mDecoders[0] = proxyOnlineDecoder;
            proxyOnlineDecoder.onCreate();
        }
    }

    private synchronized void createOfflineDecoderIfNeeded() throws Exception {
        appendResult(new MessageResult(new JSONObject().put("engine_type", 1), 1));
        if (this.mDecoders[1] == null) {
            OfflineDecoder proxyOfflineDecoder = new OfflineDecoder(this.context, this.mParams);
            this.mDecoders[1] = proxyOfflineDecoder;
            proxyOfflineDecoder.onCreate();
        }
    }

    public final class MessageResult extends Results.Result {
        private final int engineType;

        protected MessageResult(JSONObject response, int type) throws Exception {
            super(response);
            this.engineType = type;
        }

        public int getEngineType() {
            return this.engineType;
        }
    }

    @Override // com.baidu.speech.AbsStreamDecoder, com.baidu.speech.AsrSession.Decoder
    public Results.Result read() throws Exception {
        Results.Result myResult = super.read();
        if (myResult != null) {
            return myResult;
        }
        synchronized (this.mSwitchLock) {
            if (this.mDecoders[this.preferredDecoder] != null) {
                Results.Result result = null;
                try {
                    result = this.mDecoders[this.preferredDecoder].read();
                } catch (Exception e) {
                    this.logger.info(String.format("decoder %d Exception %s ignored, initial may failed", Integer.valueOf(this.preferredDecoder), e));
                }
                if (result instanceof Results.SentenceEndResult) {
                    this.logger.info(String.format("sentence result: %s, tid: %d", result, Integer.valueOf(result.getId())));
                    if (result.getId() <= 0 || (this.mLockedResult != null && result.getId() <= this.mLockedResult.getId())) {
                        this.logger.info("tid not increase, SentenceEndResult ignored");
                    } else {
                        this.mLockedResult = new Results.SentenceEndResult(new JSONObject(result.strResponse));
                        this.mLockedResult.setId(result.getId());
                    }
                }
                if (result instanceof Results.FinalResult) {
                    mDecodeCount++;
                }
                if (result != null) {
                    result.addPrefix(this.mTempLicenseNotice);
                }
                return result;
            }
        }
        if (this.mDecoders[this.secondaryDecoder] == null) {
            return null;
        }
        Results.Result result2 = this.mDecoders[this.secondaryDecoder].read();
        if (result2 != null) {
            if (this.mLockedResult != null) {
                Object tmp = this.mLockedResult.toBundle().get("results_recognition");
                if ((tmp instanceof ArrayList) && ((ArrayList) tmp).size() > 0 && (((ArrayList) tmp).get(0) instanceof String)) {
                    result2.addPrefix((String) ((ArrayList) tmp).get(0));
                }
            }
            result2.addPrefix(this.mTempLicenseNotice);
        }
        if (!(result2 instanceof Results.FinalResult)) {
            return result2;
        }
        mDecodeCount++;
        return result2;
    }

    /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x0072: APUT  (r7v1 java.lang.Object[]), (0 ??[int, short, byte, char]), (r4v19 java.lang.String) */
    /* access modifiers changed from: protected */
    @Override // com.baidu.speech.AbsStreamDecoder
    public void onExecute(int id, InputStream[] ins) throws Exception {
        this.logger.info("MergedDecoder.onExecute " + id + ", " + ins);
        if (this.mDecoders[this.preferredDecoder] != null) {
            clearCachedStreams();
            this.mCachedStreams.put(Integer.valueOf(id), ins[this.secondaryDecoder]);
            try {
                this.mDecoders[this.preferredDecoder].onExecute(id, new InputStream[]{ins[this.preferredDecoder]});
            } catch (Exception e) {
                synchronized (this.mSwitchLock) {
                    this.mDecoders[this.preferredDecoder].close();
                    this.mDecoders[this.preferredDecoder] = null;
                    Logger logger = this.logger;
                    Object[] objArr = new Object[2];
                    objArr[0] = this.preferredDecoder == 0 ? "Online" : "Offline";
                    objArr[1] = e;
                    logger.info(String.format("%s asr Exception: %s", objArr));
                    if (this.preferredDecoder == 0) {
                        if (OfflineDecoder.check(this.mParams)) {
                            createOfflineDecoderIfNeeded();
                        }
                    } else if (Utility.isNetworkConnected(this.context)) {
                        createOnlineDecoderIfNeeded();
                    }
                    if (this.mDecoders[this.secondaryDecoder] == null) {
                        e.printStackTrace();
                        String errorMessage = getMessage(e);
                        throw new Exception(this.preferredDecoder == 0 ? errorMessage + "(cannot switch to offline: params not supported.)" : errorMessage + "(cannot switch to online: network unavailable.)", e);
                    } else if (this.mCachedStreams.size() > 0) {
                        for (int i = clearCachedStreams(); i <= id; i++) {
                            this.mDecoders[this.secondaryDecoder].onExecute(i, new InputStream[]{this.mCachedStreams.remove(Integer.valueOf(i))});
                        }
                    }
                }
            }
        } else {
            if (ins[this.preferredDecoder] != null) {
                ins[this.preferredDecoder].close();
            }
            if (this.mDecoders[this.secondaryDecoder] != null) {
                this.mDecoders[this.secondaryDecoder].onExecute(id, new InputStream[]{ins[this.secondaryDecoder]});
            }
        }
    }

    private int clearCachedStreams() throws Exception {
        int i = this.mLockedResult == null ? 0 : this.mLockedResult.getId();
        for (int j = 0; j < i; j++) {
            InputStream in = this.mCachedStreams.remove(Integer.valueOf(j));
            if (in != null) {
                in.close();
            }
        }
        return i;
    }

    /* JADX INFO: Multiple debug info for r2v2 java.util.Iterator<java.io.InputStream>: [D('i$' int), D('i$' java.util.Iterator)] */
    /* access modifiers changed from: protected */
    @Override // com.baidu.speech.AbsStreamDecoder
    public void onDestroy() throws Exception {
        AbsStreamDecoder[] arr$ = this.mDecoders;
        for (AbsStreamDecoder decoder : arr$) {
            if (decoder != null) {
                decoder.close();
            }
        }
        for (InputStream in : this.mCachedStreams.values()) {
            if (in != null) {
                in.close();
            }
        }
        this.mCachedStreams.clear();
    }

    private static int getIntOrThrow(Map<String, Object> params, String key, int defaultValue) throws Exception {
        Object val = params.get(key);
        if (val == null) {
            val = Integer.valueOf(defaultValue);
        }
        return ((Integer) val).intValue();
    }

    private static boolean isPropPreferOffline(int prop) {
        switch (prop) {
            case EASRParams.PROP_MUSIC /* 10001 */:
            case EASRParams.PROP_APP /* 10003 */:
            case EASRParams.PROP_PHONE /* 10008 */:
            case EASRParams.PROP_CONTACTS /* 100014 */:
            case EASRParams.PROP_SETTING /* 100016 */:
            case EASRParams.PROP_TV /* 100018 */:
            case EASRParams.PROP_PLAYER /* 100019 */:
            case EASRParams.PROP_RADIO /* 100020 */:
            case EASRParams.PROP_COMMAND /* 100021 */:
                return true;
            default:
                return false;
        }
    }

    private String getMessage(Exception e) {
        Pattern p = Pattern.compile("^#(\\d+)[\t]*,.+");
        for (Throwable x = e; x != null; x = x.getCause()) {
            if (p.matcher(x.getMessage() + "").find()) {
                return x.getMessage();
            }
        }
        return AsrSession.ERROR_NO_MATCH;
    }
}
