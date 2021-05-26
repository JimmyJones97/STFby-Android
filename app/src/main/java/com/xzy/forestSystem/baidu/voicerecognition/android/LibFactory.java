package com.xzy.forestSystem.baidu.voicerecognition.android;

public class LibFactory {
    public static final int ERROR_UNKNOWN = -100;
    public static final int FORMAT_ADPCM_8K = 2;
    public static final int FORMAT_AMR_16K = 7;
    public static final int FORMAT_BV32_16K = 4;
    public static final int FORMAT_BV32_8K = 0;
    public static final int FORMAT_FEA_16K_2_3_30 = 20;
    public static final int FORMAT_OPUS_16K = 68;
    public static final int FORMAT_OPUS_8K = 64;
    public static final int FORMAT_PCM_16K = 5;
    public static final int FORMAT_PCM_8K = 1;
    public static final int MEMALLOC_ERR = -107;
    public static final int PARAMRANGE_ERR = -109;
    public static final int PARAM_BITRATE_NB = 17;
    public static final int PARAM_BITRATE_OPUS_16K = 20;
    public static final int PARAM_BITRATE_OPUS_8K = 19;
    public static final int PARAM_BITRATE_WB = 18;
    public static final int PARAM_CODE_FORMAT = 14;
    public static final int PARAM_ENERGY_THRESHOLD_EP = 7;
    public static final int PARAM_ENERGY_THRESHOLD_SP = 6;
    public static final int PARAM_MAX_SP_DURATION = 2;
    public static final int PARAM_MAX_SP_PAUSE = 3;
    public static final int PARAM_MAX_WAIT_DURATION = 1;
    public static final int PARAM_MIN_SP_DURATION = 4;
    public static final int PARAM_MODE_CMB = 21;
    public static final int PARAM_NEED_COMPRESS = 12;
    public static final int PARAM_NEED_VAD = 11;
    public static final int PARAM_OFFSET = 8;
    public static final int PARAM_SAMPLE_RATE = 13;
    public static final int PARAM_SLEEP_TIMEOUT = 5;
    public static final int PARAM_SPEECHIN_THRESHOLD_BIAS = 15;
    public static final int PARAM_SPEECHOUT_THRESHOLD_BIAS = 16;
    public static final int PARAM_SPEECH_END = 9;
    public static final int PARAM_SPEECH_MODE = 10;
    public static final int POINTER_ERR = -103;
    public static final int SEND_TOOMORE_DATA_ONCE = -118;
    public static final int STATE_ERR = -102;
    public static final int SUCCESS = 0;
    public static final int VAD_INIT_ERROR = -120;

    public interface JNI {
        int Detect();

        int EnableNoiseDetection(boolean z);

        int Exit();

        int GetCallbackData(byte[] bArr, int i);

        int GetParam(int i);

        int GetVADVersion();

        int Init();

        int SendData(short[] sArr, int i);

        void SetLogLevel(int i);

        int SetParam(int i, int i2);
    }

    public static JNI create(int type) {
        if (type == 1) {
            return new MFE_JNI();
        }
        if (type == 3) {
            return new NO_JNI();
        }
        return null;
    }
}
