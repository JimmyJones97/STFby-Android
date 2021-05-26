package com.xzy.forestSystem.baidu.speech.easr.stat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.http.AndroidHttpClient;
import android.text.TextUtils;
import com.xzy.forestSystem.baidu.speech.easr.Utility;
import com.xzy.forestSystem.baidu.speech.easr.easrNativeJni;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StatHelper {
    private static final int EACH_TIME_UPLOAD_RESULT_COUNT = 500;
    private static final String KEY_APP_ID = "appid";
    private static final String KEY_APP_NAME = "app_name";
    private static final String KEY_APP_SIGNATURE = "app_signature";
    private static final String KEY_CUID = "wise_cuid";
    private static final String KEY_NET_TYPE = "net_type";
    private static final String KEY_OS = "os";
    private static final String KEY_PLATFORM = "platform";
    private static final String KEY_RECOGNITION_RESULT = "recog_results";
    private static final String KEY_RECOGNITION_RESULT_CMD_ID = "cmd_id";
    private static final String KEY_RECOGNITION_RESULT_CMD_TYPE = "cmd_type";
    private static final String KEY_RECOGNITION_RESULT_ERROR_CODE = "error_code";
    private static final String KEY_RECOGNITION_RESULT_TIME = "time";
    private static final String KEY_RECOGNITION_RESULT_VOICE_TO_TEXT = "voice_to_text_result";
    private static final String KEY_RESPONSE_ERROR_CODE = "errno";
    private static final String KEY_RESPONSE_UPLOAD_DATA = "data";
    private static final String KEY_RESPONSE_UPLOAD_PERIOD = "secs";
    private static final String KEY_SCREEN = "screen";
    private static final String KEY_SDK_NAME = "sdk_name";
    private static final String KEY_SDK_VERSION = "sdk_version";
    private static final String TAG = "StatHelper";
    private static final Logger logger = Logger.getLogger(TAG);

    public static String getStatHeader(Context context, String appID) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put(KEY_CUID, Utility.getCUID(context));
            jsonObj.put(KEY_SDK_VERSION, Utility.getSdkVersion());
            jsonObj.put(KEY_APP_NAME, Utility.getPackageName(context));
            jsonObj.put(KEY_PLATFORM, Utility.getPlatform(context));
            jsonObj.put(KEY_OS, Utility.getOS());
            jsonObj.put(KEY_NET_TYPE, Utility.getNetType(context));
            if (!TextUtils.isEmpty(appID)) {
                jsonObj.put(KEY_APP_ID, appID);
            }
            jsonObj.put(KEY_SCREEN, Utility.getScreen(context));
            jsonObj.put(KEY_APP_SIGNATURE, Utility.getSignatureMD5(context));
        } catch (JSONException je) {
            logger.severe("getStatHeader:" + je);
        }
        logger.fine("getStatHeader: " + jsonObj.toString());
        return Utility.encryptBASE64(jsonObj.toString());
    }

    public static synchronized void uploadStatDatas(Context context, String appID) {
        synchronized (StatHelper.class) {
            if (Utility.isNetworkConnected(context)) {
                long lastTime = Utility.getLastUploadStatTime(context);
                int lastPeriod = Utility.getLastDownloadStatPeriod(context);
                long period = 86400000;
                if (lastPeriod > 0) {
                    period = (long) lastPeriod;
                }
                logger.fine("lastTime " + lastTime + ", curTime " + System.currentTimeMillis() + ", lastPeriod " + lastPeriod);
                long currentMills = System.currentTimeMillis();
                Date lastDate = new Date(lastTime);
                Date currentDate = new Date(currentMills);
                if (currentMills - lastTime < period) {
                    logger.fine("lastTime " + lastTime + ", curTime " + System.currentTimeMillis());
                    logger.fine("lastDate " + lastDate + "\ncurDate " + currentDate);
                } else if (doUploadStatData(context, appID)) {
                    getStatPeriod(context, appID);
                }
            }
        }
    }

    private static boolean doUploadStatData(Context context, String appID) {
        Cursor cursor;
        int count;
        boolean updated = false;
        SynthesizeResultDb mStatDb = SynthesizeResultDb.getInstance(context);
        synchronized (mStatDb) {
            if (mStatDb.isDatabaseClosed()) {
                return false;
            }
            cursor = mStatDb.querySynthesizeResult();
        }
        if (cursor != null) {
            try {
                if (cursor.getCount() >= 1) {
                    logger.fine("cursor.getCount: " + cursor.getCount());
                    if (cursor.getCount() % EACH_TIME_UPLOAD_RESULT_COUNT == 0) {
                        count = cursor.getCount() / EACH_TIME_UPLOAD_RESULT_COUNT;
                    } else {
                        count = (cursor.getCount() / EACH_TIME_UPLOAD_RESULT_COUNT) + 1;
                    }
                    for (int i = 0; i < count; i++) {
                        JSONObject jsonObj = new JSONObject();
                        List<Integer> ids = getRecogResult(jsonObj, cursor, i * EACH_TIME_UPLOAD_RESULT_COUNT, EACH_TIME_UPLOAD_RESULT_COUNT);
                        logger.fine("jsonObj all: " + jsonObj.toString());
                        byte[] data = Utility.encryptGZIP(jsonObj.toString());
                        if (data.length >= 2) {
                            data[0] = 117;
                            data[1] = 123;
                        }
                        String postContent = Utility.encryptBASE64(data);
                        logger.fine(" postContent:" + postContent);
                        if (updateDataByHttp(context, appID, postContent)) {
                            synchronized (mStatDb) {
                                if (!mStatDb.isDatabaseClosed()) {
                                    mStatDb.deleteSynthesizeResult(ids);
                                    Utility.setLastUploadStatTime(context, System.currentTimeMillis());
                                    updated = true;
                                }
                            }
                        }
                    }
                }
                cursor.close();
            } catch (SQLiteException e) {
                return false;
            }
        }
        return updated;
    }

    private static boolean getStatPeriod(Context context, String appId) {
        boolean ret = false;
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance("");
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 8000);
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), 8000);
        String url = buildPeriodUrl(context, appId);
        logger.fine("url:" + url);
        try {
            HttpResponse response = httpClient.execute(new HttpGet(url));
            logger.fine("response.getStatusLine().getStatusCode():" + response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                String entity = EntityUtils.toString(response.getEntity(), "utf-8");
                logger.fine("entity:" + entity);
                if (!TextUtils.isEmpty(entity)) {
                    try {
                        JSONObject dataJson = new JSONObject(entity).optJSONObject(KEY_RESPONSE_UPLOAD_DATA);
                        if (dataJson != null) {
                            int period = dataJson.optInt(KEY_RESPONSE_UPLOAD_PERIOD);
                            logger.fine("period:" + period);
                            int period2 = period * easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX;
                            if (period2 > 0) {
                                int oldPeriod = Utility.getLastDownloadStatPeriod(context);
                                logger.fine("oldPeriod:" + oldPeriod);
                                if (period2 != oldPeriod) {
                                    Utility.setLastDownloadStatPeriod(context, period2);
                                }
                            }
                        }
                    } catch (JSONException je) {
                        logger.severe("parse:" + je.toString());
                    } catch (Exception e) {
                        logger.severe("parse:" + e.toString());
                    }
                }
                ret = true;
            }
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        } finally {
            httpClient.close();
        }
        return ret;
    }

    private static List<Integer> getRecogResult(JSONObject jsonObj, Cursor cursor, int offset, int size) {
        if (cursor == null) {
            return null;
        }
        List<Integer> idList = new ArrayList<>();
        JSONArray resultArray = new JSONArray();
        int count = 0;
        boolean isStoped = cursor.moveToPosition(offset);
        while (isStoped) {
            int id = cursor.getInt(cursor.getColumnIndex(SynthesizeResultDb.KEY_ROWID));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int errorCode = cursor.getInt(cursor.getColumnIndex(SynthesizeResultDb.KEY_ERROR_CODE));
            int cmdType = cursor.getInt(cursor.getColumnIndex("cmd_type"));
            int cmdId = cursor.getInt(cursor.getColumnIndex("cmd_id"));
            String recogResult = cursor.getString(cursor.getColumnIndex(SynthesizeResultDb.KEY_RESULT));
            idList.add(Integer.valueOf(id));
            JSONObject json = new JSONObject();
            try {
                json.put("time", Long.parseLong(time));
                json.put(KEY_RECOGNITION_RESULT_ERROR_CODE, errorCode);
                if (errorCode == 0) {
                    json.put("cmd_type", cmdType);
                    json.put("cmd_id", cmdId);
                    json.put(KEY_RECOGNITION_RESULT_VOICE_TO_TEXT, recogResult);
                }
                resultArray.put(json);
            } catch (JSONException je) {
                logger.severe("je:" + je);
            } catch (NumberFormatException e) {
                logger.severe("e " + e);
            }
            count++;
            if (count >= size) {
                break;
            }
            isStoped = cursor.moveToNext();
        }
        try {
            jsonObj.put(KEY_RECOGNITION_RESULT, resultArray);
            return idList;
        } catch (JSONException je2) {
            logger.severe("je:" + je2);
            return idList;
        }
    }

    private static boolean updateDataByHttp(Context context, String appId, String postContent) {
        boolean ret = false;
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance("");
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 8000);
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), 8000);
        String url = buildStatUrl(context, appId);
        logger.fine("url:" + url);
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httppost.setEntity(getPostData(postContent));
        try {
            HttpResponse response = httpClient.execute(httppost);
            logger.fine("response.getStatusLine().getStatusCode():" + response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                String entity = EntityUtils.toString(response.getEntity(), "utf-8");
                logger.fine("entity:" + entity);
                if (!TextUtils.isEmpty(entity)) {
                    try {
                        if ("0".equals(new JSONObject(entity).optString(KEY_RESPONSE_ERROR_CODE))) {
                            ret = true;
                        }
                    } catch (JSONException je) {
                        logger.severe("parse:" + je.toString());
                    } catch (Exception e) {
                        logger.severe("parse:" + e.toString());
                    }
                }
            }
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        } finally {
            httpClient.close();
        }
        return ret;
    }

    private static String buildStatUrl(Context context, String appId) {
        List<NameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair(KEY_CUID, Utility.getCUID(context)));
        params.add(new BasicNameValuePair(KEY_SDK_VERSION, Utility.getSdkVersion()));
        params.add(new BasicNameValuePair(KEY_APP_NAME, Utility.getAppName(context)));
        params.add(new BasicNameValuePair(KEY_PLATFORM, Utility.getPlatform(context)));
        params.add(new BasicNameValuePair(KEY_OS, Utility.getOS()));
        params.add(new BasicNameValuePair(KEY_NET_TYPE, Utility.getNetType(context) + ""));
        params.add(new BasicNameValuePair(KEY_APP_ID, appId));
        params.add(new BasicNameValuePair(KEY_SCREEN, Utility.getScreen(context)));
        params.add(new BasicNameValuePair(KEY_SDK_NAME, Utility.getSdkName()));
        params.add(new BasicNameValuePair(KEY_APP_SIGNATURE, Utility.getSignatureMD5(context)));
        return "http://yuyin.baidu.com/voice?osname=voiceopen&action=usereventflow&" + URLEncodedUtils.format(params, "utf-8");
    }

    private static String buildPeriodUrl(Context context, String appId) {
        List<NameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair(KEY_APP_ID, appId));
        return "http://uil.cbs.baidu.com/voiceLog/getconfig?" + URLEncodedUtils.format(params, "utf-8");
    }

    private static UrlEncodedFormEntity getPostData(String postContent) {
        UnsupportedEncodingException e;
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("records", postContent));
        UrlEncodedFormEntity byteentity = null;
        try {
            UrlEncodedFormEntity byteentity2 = new UrlEncodedFormEntity(list, "utf-8");
            byteentity2.setContentType("application/x-www-form-urlencoded");
            return byteentity2;
        } catch (UnsupportedEncodingException e3) {
            e = e3;
            e.printStackTrace();
            return byteentity;
        }
    }
}
