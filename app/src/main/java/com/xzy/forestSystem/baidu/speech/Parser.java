package com.xzy.forestSystem.baidu.speech;

import android.os.Bundle;
import com.xzy.forestSystem.baidu.speech.Results;
import com.xzy.forestSystem.baidu.speech.easr.stat.SynthesizeResultDb;
import com.xzy.forestSystem.baidu.speech.easr.stat.SynthesizeResultDb;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* access modifiers changed from: package-private */
public class Parser {
    private static final String EMBEDDED_RESULT_KEY_RAW_TEXT = "raw_text";
    private static final String EMBEDDED_RESULT_KEY_RESULTS = "results";
    private static final String EMBEDDED_RESULT_KEY_SCORE = "score";
    private static final String RESPONSE_KEY_CONTENT = "content";
    private static final String RESPONSE_KEY_ITEM = "item";
    private static final String RESPONSE_KEY_JSON_RES = "json_res";
    public static final int RESULT_TYPE_FINISH_CN = 2;
    public static final int RESULT_TYPE_FINISH_NBEST = 1;
    public static final int RESULT_TYPE_PARTIAL = 0;
    private static final String TAG = "Parser";
    private static final Logger logger = Logger.getLogger(TAG);

    Parser() {
    }

    public Results.Result parse(String content) throws Exception {
        Results.Result result;
        logger.info("===== parse(...) =====\n" + content);
        JSONObject t = new JSONObject(content);
        if (t.has("idxs")) {
            logger.info("parse with idxs");
            result = new Results.FinalResult(t);
        } else {
            result = parseNode(t);
        }
        logger.info("create one result instance:\n" + result);
        return result;
    }

    private Results.Result parseNode(JSONObject t) throws Exception {
        JSONObject json = t.getJSONObject(SynthesizeResultDb.KEY_RESULT);
        int err_no = json.getInt("err_no");
        int res_type = json.optInt("res_type");
        json.optInt("idx", 0);
        logger.info("parse with res_type=" + res_type);
        if (err_no != 0) {
            return new Results.FinalResult(t);
        }
        switch (res_type) {
            case 0:
                return new Results.StubResult(t);
            case 1:
                return new Results.RunningResult(t);
            case 2:
                return new Results.SentenceEndResult(t);
            case 3:
            case 5:
                return new Results.FinalResult(t);
            case 4:
            default:
                return null;
        }
    }

    public Results.Result parseOffline(String content, int type) throws Exception {
        switch (type) {
            case 0:
                Object rawText = new JSONObject(content).get(EMBEDDED_RESULT_KEY_RAW_TEXT);
                if (rawText == null || "".equals(rawText)) {
                    return null;
                }
                return new Results.RunningResult(new JSONObject(transferJSONFormatNbest(content)));
            case 1:
                return new Results.FinalResult(new JSONObject(transferJSONFormatNbest(content)));
            case 2:
                return new Results.FinalResult(new JSONObject(transferJSONFormatCN(content)));
            default:
                return null;
        }
    }

    public static double parseConfidence(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            if (json.has(EMBEDDED_RESULT_KEY_SCORE)) {
                double confidence = json.getDouble(EMBEDDED_RESULT_KEY_SCORE);
                logger.info("EmbeddedASR score: " + confidence);
                return confidence;
            } else if (!json.has(EMBEDDED_RESULT_KEY_RESULTS)) {
                return 0.0d;
            } else {
                double confidence2 = json.getJSONArray(EMBEDDED_RESULT_KEY_RESULTS).getJSONObject(0).getDouble(EMBEDDED_RESULT_KEY_SCORE);
                logger.info("EmbeddedASR score: " + confidence2);
                return confidence2;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return 0.0d;
        }
    }

    private static String transferJSONFormatNbest(String result) throws Exception {
        JSONObject serverLikeJSON = new JSONObject();
        JSONObject offlineJSON = new JSONObject(result);
        JSONObject contentJSON = new JSONObject();
        contentJSON.put(RESPONSE_KEY_ITEM, new JSONArray().put(offlineJSON.getString(EMBEDDED_RESULT_KEY_RAW_TEXT)));
        contentJSON.put(RESPONSE_KEY_JSON_RES, new JSONObject(result).toString());
        serverLikeJSON.put(RESPONSE_KEY_CONTENT, contentJSON);
        return serverLikeJSON.toString();
    }

    private static String transferJSONFormatCN(String result) throws Exception {
        JSONObject serverLikeJSON = new JSONObject();
        JSONObject offlineJSON = new JSONObject(result);
        JSONArray idxs = new JSONArray();
        JSONArray content = new JSONArray();
        content.put(new JSONArray().put(new JSONObject().put(offlineJSON.getString(EMBEDDED_RESULT_KEY_RAW_TEXT), 1.0d)));
        idxs.put(new JSONObject().put(RESPONSE_KEY_CONTENT, content));
        serverLikeJSON.put("idxs", idxs);
        return serverLikeJSON.toString();
    }

    public static Bundle convertToBundler(Results.Result r) {
        Bundle b = new Bundle();
        for (Map.Entry<String, Object> e : r.toBundle().entrySet()) {
            if (e.getValue() instanceof String) {
                b.putString(e.getKey(), (String) e.getValue());
            } else if (e.getValue() instanceof Integer) {
                b.putInt(e.getKey(), ((Integer) e.getValue()).intValue());
            } else if (e.getValue() instanceof Double) {
                b.putDouble(e.getKey(), ((Double) e.getValue()).doubleValue());
            } else if (e.getValue() instanceof Long) {
                b.putLong(e.getKey(), ((Long) e.getValue()).longValue());
            } else if (new ArrayList().getClass().equals(e.getValue().getClass())) {
                b.putStringArrayList(e.getKey(), (ArrayList) e.getValue());
            } else if (new ArrayList().getClass().equals(e.getValue().getClass())) {
                b.putIntegerArrayList(e.getKey(), (ArrayList) e.getValue());
            }
        }
        return b;
    }
}
