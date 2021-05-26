package com.xzy.forestSystem.baidu.voicerecognition.android;

import android.os.Environment;

final class DebugControl {
    static final boolean DEBUG = true;
    static final String DEBUG_EXPORT_DIRECTORY = (Environment.getExternalStorageDirectory() + "/BDVRDebug");
    static final String DEBUG_EXPORT_DIRECTORY_BEFORE_COMPRESS = "SrcLib";
    public static final String DEBUG_EXPORT_DIRECTORY_LIB = "CompressLib";
    static final int DEFAULT_TIME_OUT_IN_MS = 86400000;
    static final int DEFAULT_USER_DATA_FILE_SIZE = 4096;
    static final boolean EXPORT_RECORDE_VOICE = false;
    static final boolean EXPORT_RECORDE_VOICE_SEGMENT = false;
    static final boolean EXPORT_UPLOADDATA_TO_SDCARD = false;
    static final boolean IS_PRINT_ERROR_SN = false;
    static final boolean IS_SUPPORT_NETWORK_PACKAGE_DEBUG = false;
    static final boolean IS_SUPPORT_PERFORMANCE = false;
    static final boolean IS_USER_DATA_PARAMETER_CHANGABLE = false;
    static final String RECORDE_VOICE_FILENAME = "OriginalVoice.pcm";
    static final String RECORD_ERROR_SN_FINLENAME = "ErrorSn.csv";
    static final String RECORD_VOICE_SAMPLERATE_FILENAME = "pcmsamplerate.txt";
    static int sConnectTimeout = -1;
    static int sMaxUserDataSize = 4096;
    static int sNetworkPackSize = -1;
    static String sUserDataServerUrl = "http://10.65.45.215:8002/echo.fcgi";
    static int sUserDataTimeOutInMilliSeconds = DEFAULT_TIME_OUT_IN_MS;

    private DebugControl() {
    }
}
