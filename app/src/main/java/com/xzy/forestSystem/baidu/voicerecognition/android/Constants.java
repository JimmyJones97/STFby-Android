package com.xzy.forestSystem.baidu.voicerecognition.android;

final class Constants {
    static final String DEVICE_ID_KEY = "device_id";
    static final int FILE_READ_FINISH = -2147483548;
    static final String GB2312 = "gb2312";
    static final int PRODUCT_ID_HAO123 = 256;
    static final int PRODUCT_ID_MULTIPLE = 512;
    static final int PRODUCT_ID_OPEN_SDK_INPUT = C0126Config.getInteger(C0126Config.KEY_PRODUCTID_INPUT, 1537);
    static final int PRODUCT_ID_OPEN_SDK_INPUT_EN_GB = 1737;
    static final int PRODUCT_ID_OPEN_SDK_INPUT_SICHUAN_HANS_CN = 1837;
    static final int PRODUCT_ID_OPEN_SDK_INPUT_YUE_HANS_CN = 1637;
    static final int PRODUCT_ID_OPEN_SDK_SEARCH = C0126Config.getInteger(C0126Config.KEY_PRODUCTID_SEARCH, 1536);
    static final int PRODUCT_ID_OPEN_SDK_SEARCH_EN_GB = 1736;
    static final int PRODUCT_ID_OPEN_SDK_SEARCH_SICHUAN_HANS_CN = 1836;
    static final int PRODUCT_ID_OPEN_SDK_SEARCH_YUE_HANS_CN = 1636;
    static final int PRODUCT_ID_QUNAER = 256;
    static final int PRODUCT_ID_QUNAER_TEXT = 256;
    static final int PRODUCT_ID_SINGLE = 1;
    static final int PROTOCOL_VERSION_AUDIO_DA = 302;
    static int PROTOCOL_VERSION_INPUT = 0;
    static final int PROTOCOL_VERSION_INPUT_NBEST = 2;
    static final int PROTOCOL_VERSION_MULTIPLE = 101;
    static final int PROTOCOL_VERSION_NLU = 303;
    static final int PROTOCOL_VERSION_NLU_TEXT = 304;
    static final int PROTOCOL_VERSION_SINGLE = 1;
    static final int PROTOCOL_VERSION_WISE = 300;
    static final int PROTOCOL_VERSION_WISE_NLU = 305;
    static final int PROTOCOL_VERSION_WISE_TEXT = 301;
    static final String THREADNAME_POSTDATA = "PostDataThread";
    static final String THREADNAME_SHEDULE = "PostDataSheduleThread";
    static final String THREADNAME_STATISTIC_POST = "PostStatisticThread";
    static final String THREADNAME_VOICERECORD = "VoiceRecordThread";
    private static final String TIME_STAMP = "timestamp";

    static final String TIME_STAME_MD5 = Util.toMd5(TIME_STAMP.getBytes(), false);
    static final String UTF8 = "utf-8";
    static final String VERSION_NAME = "1.6.1.0-11793+";
    public static int debugMask = 0;
    public static int logLevel = 3;
    public static int productID = PRODUCT_ID_OPEN_SDK_SEARCH;
    public static String sInputUrl;
    public static String sSearchUrl;
    static String sUserDataServerUrl = "http://123.125.65.52:8002/echo.fcgi";

    private Constants() {
//        static {
            PROTOCOL_VERSION_INPUT = 101;
            sSearchUrl = "http://vop.baidu.com/echo.fcgi";
            sInputUrl = "http://vop.baidu.com/echo.fcgi";
            PROTOCOL_VERSION_INPUT = C0126Config.getInteger(C0126Config.KEY_PROTOCOL_INPUT, PROTOCOL_VERSION_INPUT);
            if (PROTOCOL_VERSION_INPUT == 2 || PROTOCOL_VERSION_INPUT == 101) {
                sSearchUrl = C0126Config.getString(C0126Config.KEY_SERVER_URL, sSearchUrl);
                sInputUrl = C0126Config.getString(C0126Config.KEY_SERVER_URL, sInputUrl);
                return;
            }
            throw new IllegalArgumentException("PROTOCOL_VERSION_INPUT must be 2 or 101");
//        }
    }

}
