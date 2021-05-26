package com.xzy.forestSystem.baidu.speech;

import android.content.Context;
import com.xzy.forestSystem.baidu.speech.Results;
import com.xzy.forestSystem.baidu.speech.easr.EASRParams;
import com.xzy.forestSystem.baidu.speech.easr.EmbeddedASREngine;
import com.xzy.forestSystem.baidu.speech.easr.EASRParams;
import com.xzy.forestSystem.baidu.speech.easr.EmbeddedASREngine;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class OfflineDecoder extends AbsStreamDecoder {
    private static EmbeddedASREngine mEngine;
    private String lastDecodeJSONString = "";
    private boolean mDecodeFinished = false;
    private boolean mEngineStarted = false;
    private final int protocol;

    @Override // com.baidu.speech.AbsStreamDecoder, com.baidu.speech.AsrSession.Decoder
    public /* bridge */ /* synthetic */ Results.Result read() throws Exception {
        return super.read();
    }

    public OfflineDecoder(Context context, Map<String, Object> params) throws Exception {
        super(context, params);
        this.protocol = getIntOrThrow(params, "decoder-server.ptc");
        verifyParams(params);
    }

    /* access modifiers changed from: protected */
    @Override // com.baidu.speech.AbsStreamDecoder
    public void onCreate() {
        mEngine = EmbeddedASREngine.getInstance(this.context);
    }

    /* access modifiers changed from: protected */
    @Override // com.baidu.speech.AbsStreamDecoder
    public void onExecute(int id, InputStream[] ins) throws Exception {
        int x;
        this.logger.info("OfflineDecoder.onExecute " + id + ", " + ins[0]);
        if (!this.mEngineStarted) {
            HashMap params = new HashMap(this.mParams);
            int ret = mEngine.startRecognizing(generateEASRParams(params), (Integer) params.get("basic.appid"));
            if (ret != 0) {
                mEngine.addOneRecord(ret);
                throw new Exception(String.format("%s, reason: #%d, startRecognizing error.", AsrSession.ERROR_CLIENT, Integer.valueOf(ret)));
            }
            this.mEngineStarted = true;
        }
        try {
            if (!this.mDecodeFinished) {
                if (ins[0] != null) {
                    while (!isClosed()) {
                        int len = 0;
                        byte[] buf = new byte[1024];
                        while (len < buf.length && (x = ins[0].read(buf, len, buf.length - len)) >= 0) {
                            len += x;
                        }
                        short[] shortBuf = byteArrayToShortArray(buf, len);
                        if (shortBuf.length > 0) {
                            int ret2 = mEngine.newAudioData(shortBuf, 0, shortBuf.length, 0);
                            this.logger.info(String.format("newAudioData[bdeasrFront] short: %d eof: %b ret: %d", Integer.valueOf(shortBuf.length), false, Integer.valueOf(ret2)));
                            if (ret2 != 7) {
                                this.logger.severe("newAudioData[bdeasrFront] error = " + ret2);
                                mEngine.addOneRecord(ret2);
                                throw new Exception(String.format("%s, reason: #%d, newAudioData error.", AsrSession.ERROR_CLIENT, Integer.valueOf(ret2)));
                            }
                        }
                        int ret3 = mEngine.recognize();
                        this.logger.info("recognize[bdeasRec] ret = " + ret3);
                        if (needParseMoreResult(ret3)) {
                            if (len < buf.length) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                } else {
                    short[] buf2 = new short[512];
                    Arrays.fill(buf2, (short) 0);
                    int ret4 = mEngine.newAudioData(buf2, 0, buf2.length, 1);
                    this.logger.info(String.format("newAudioData[bdeasrFront] eof: %b ret: %d", true, Integer.valueOf(ret4)));
                    if (ret4 != 7) {
                        this.logger.severe("newAudioData[bdeasrFront] error = " + ret4);
                        mEngine.addOneRecord(ret4);
                        throw new Exception(String.format("%s, reason: #%d, newAudioData error.", AsrSession.ERROR_CLIENT, Integer.valueOf(ret4)));
                    }
                    int ret5 = mEngine.recognize();
                    this.logger.info("recognize[bdeasRec] ret = " + ret5);
                    boolean needParseMoreResult = needParseMoreResult(ret5);
                    if (needParseMoreResult) {
                        while (!isClosed() && needParseMoreResult) {
                            int ret6 = mEngine.recognize();
                            this.logger.info("recognize[bdeasRec] ret = " + ret6);
                            needParseMoreResult = needParseMoreResult(ret6);
                            Thread.sleep(20);
                        }
                    } else if (ins[0] != null) {
                        ins[0].close();
                        return;
                    } else {
                        return;
                    }
                }
                if (ins[0] != null) {
                    ins[0].close();
                }
            }
        } finally {
            if (ins[0] != null) {
                ins[0].close();
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.baidu.speech.AbsStreamDecoder
    public void onDestroy() throws Exception {
    }

    private boolean needParseMoreResult(int ret) throws Exception {
        int i = 2;
        if (ret != 7 && ret != 8) {
            this.logger.severe("bdeasrRec decode error: " + ret);
            mEngine.addOneRecord(ret);
            throw new Exception(String.format("%s, reason: #%d, recognize error.", AsrSession.ERROR_CLIENT, Integer.valueOf(ret)));
        } else if (ret == 7) {
            String json = mEngine.getJSONResult();
            if (json.equals(this.lastDecodeJSONString)) {
                return true;
            }
            this.lastDecodeJSONString = json;
            if (Parser.parseConfidence(json) < 1.0d) {
                throw new Exception(String.format("%s, reason: %d, no confidence.", AsrSession.ERROR_NO_MATCH, Integer.valueOf((int) EmbeddedASREngine.ERROR_EMBEDDED_NOT_CONFIDENCE)));
            }
            Results.Result result = new Parser().parseOffline(json, 0);
            if (result != null) {
                appendResult(result);
            }
            this.logger.info("bdeasrRec OK, partial result: " + json);
            return true;
        } else {
            String json2 = mEngine.getJSONResult();
            this.lastDecodeJSONString = json2;
            if (Parser.parseConfidence(json2) < 1.0d) {
                throw new Exception(String.format("%s, reason: %d, no confidence.", AsrSession.ERROR_NO_MATCH, Integer.valueOf((int) EmbeddedASREngine.ERROR_EMBEDDED_NOT_CONFIDENCE)));
            }
            Parser parser = new Parser();
            if (this.protocol != 101) {
                i = 1;
            }
            appendResult(parser.parseOffline(json2, i));
            this.logger.info("bdeasrRec get final result: " + json2);
            mEngine.addOneRecord(0);
            this.mDecodeFinished = true;
            return false;
        }
    }

    private EASRParams generateEASRParams(Map<String, Object> params) throws Exception {
        EASRParams easrParams = new EASRParams();
        easrParams.asrDataFilePath = (String) params.get("decoder-offline.asr-base-file-path");
        easrParams.lmResPath = (String) params.get("decoder-offline.lm-res-file-path");
        easrParams.licenseFilePath = (String) params.get("decoder-offline.license-file-path");
        easrParams.imePunctuationOn = ((Boolean) params.get("decoder-offline.enable-punctuation")).booleanValue() ? 1 : 0;
        Object propObj = params.get("decoder-offline.prop");
        if (propObj == null || "".equals(propObj)) {
            throw new Exception(String.format("%s, reason: #%d, prop not set", AsrSession.ERROR_CLIENT, Integer.valueOf((int) EmbeddedASREngine.ERROR_PROP_NOT_SET)));
        }
        easrParams.prop = ((Integer) propObj).intValue();
        Object slotData = params.get("decoder-offline.slot-data");
        if (slotData != null) {
            easrParams.slotData = new JSONObject((String) slotData);
        }
        return easrParams;
    }

    private static void verifyParams(Map<String, Object> params) throws Exception {
        int sampleRate = getIntOrThrow(params, "audio.sample");
        if (16000 != sampleRate) {
            throw new Exception(String.format("%s, reason: #%d, sample rate %d not supported", AsrSession.ERROR_CLIENT, Integer.valueOf((int) EmbeddedASREngine.ERROR_SAMPLE_RATE_NOT_SUPPORTED), Integer.valueOf(sampleRate)));
        }
        int prop = getIntOrThrow(params, "decoder-offline.prop");
        if (!isPropSupported(prop)) {
            throw new Exception(String.format("%s, reason: #%d, prop %d not supported", AsrSession.ERROR_CLIENT, Integer.valueOf((int) EmbeddedASREngine.ERROR_PROP_NOT_SUPPORTED), Integer.valueOf(prop)));
        }
        String language = (String) params.get("decoder-offline.language");
        if (!isLanguageSupported(language)) {
            throw new Exception(String.format("%s, reason: #%d, language %s not supported", AsrSession.ERROR_CLIENT, Integer.valueOf((int) EmbeddedASREngine.ERROR_LANGUAGE_NOT_SUPPORTED), language));
        }
    }

    private static int getIntOrThrow(Map<String, Object> params, String key) throws Exception {
        Object val = params.get(key);
        if (val != null) {
            return ((Integer) val).intValue();
        }
        throw new Exception(String.format("%s, reason: key %s not found", AsrSession.ERROR_CLIENT, key));
    }

    public static boolean check(Map<String, Object> params) {
        try {
            verifyParams(params);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isPropSupported(int prop) {
        switch (prop) {
            case EASRParams.PROP_MUSIC /* 10001 */:
            case EASRParams.PROP_APP /* 10003 */:
            case EASRParams.PROP_PHONE /* 10008 */:
            case EASRParams.PROP_MAP /* 10060 */:
            case EASRParams.PROP_INPUT /* 20000 */:
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

    private static boolean isLanguageSupported(String language) {
        return VoiceRecognitionService.LANGUAGE_CHINESE.equals(language);
    }

    private short[] byteArrayToShortArray(byte[] bytes, int len) {
        if (bytes == null || bytes.length < len || len == 0) {
            return new short[0];
        }
        short[] audioDataInShorts = new short[(len / 2)];
        ByteBuffer.wrap(bytes).order(ByteOrder.nativeOrder()).asShortBuffer().get(audioDataInShorts);
        return audioDataInShorts;
    }
}
