package com.xzy.forestSystem.baidu.voicerecognition.android;

import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/* renamed from: com.baidu.voicerecognition.android.Config */
public class C0126Config {
    private static final String CONFIG_FILE_NAME = "bdspeech_asr.cfg";
    private static final String CONSTANT_VALUE_TRUE = "true";
    public static final String KEY_PRODUCTID_INPUT = "bdspeech.productid.input";
    public static final String KEY_PRODUCTID_SEARCH = "bdspeech.productid.search";
    public static final String KEY_PROTOCOL_INPUT = "bdspeech.protocol.input";
    public static final String KEY_SCO_CONTROLLER = "bdspeech.sco.controller";
    public static final String KEY_SERVER_URL = "bdspeech.server.url";
    private static final String LOG_TAG = "Config";
    private static final Properties mProperties = new Properties();

    static {
        InputStream configFile = C0126Config.class.getResourceAsStream(File.separator + CONFIG_FILE_NAME);
        if (configFile != null) {
            try {
                mProperties.load(configFile);
                if (Log.isLoggable(LOG_TAG, 3)) {
                    Log.d(LOG_TAG, mProperties.toString());
                }
                try {
                    configFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e2) {
                throw new IllegalArgumentException("bdspeech_asr.cfg load error");
            } catch (Throwable th) {
                try {
                    configFile.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
                throw th;
            }
        } else if (Log.isLoggable(LOG_TAG, 3)) {
            Log.d(LOG_TAG, "bdspeech_asr.cfg not exsit");
        }
    }

    public static boolean getBoolean(String name, boolean defValue) {
        String value = mProperties.getProperty(name);
        if (value != null) {
            return CONSTANT_VALUE_TRUE.equals(value);
        }
        return defValue;
    }

    public static int getInteger(String name, int defValue) {
        String value = mProperties.getProperty(name);
        if (value == null) {
            return defValue;
        }
        try {
            return Integer.valueOf(value).intValue();
        } catch (NumberFormatException e) {
            Log.w(LOG_TAG, String.format("sdCard config warn,param %1$s value %2$s isn't number", name, value));
            return defValue;
        }
    }

    public static String getString(String name, String defValue) {
        return mProperties.getProperty(name, defValue);
    }
}
