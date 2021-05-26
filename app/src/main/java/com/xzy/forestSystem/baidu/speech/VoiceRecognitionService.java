package com.xzy.forestSystem.baidu.speech;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.speech.RecognitionService;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.Log;
import com.xzy.forestSystem.baidu.speech.Console;
import com.xzy.forestSystem.baidu.speech.MergedDecoder;
import com.xzy.forestSystem.baidu.speech.Results;
import  com.xzy.forestSystem.qihoo.jiagutracker.C0246Config;
import  com.xzy.forestSystem.stub.StubApp;
import  com.xzy.forestSystem.gogisapi.Common.Constant;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

public final class VoiceRecognitionService extends RecognitionService {
    public static final int EVENT_ENGINE_SWITCH = 12;
    private static final int EVENT_ERROR = 11;
    public static final String LANGUAGE_CANTONESE = "yue-Hans-CN";
    public static final String LANGUAGE_CHINESE = "cmn-Hans-CN";
    public static final String LANGUAGE_ENGLISH = "en-GB";
    public static final String LANGUAGE_SICHUAN = "sichuan-Hans-CN";
    public static final String NLU_DISABLE = "disable";
    public static final String NLU_ENABLE = "enable";
    private static final TreeMap<Object, Object> PARAMS_MAP = new TreeMap<>();
    public static final String SP_KEY_INTERNAL = "key_internal";
    public static final String SP_NAME_ASR = "baidu_speech_asr";
    public static final String TAG = "VoiceRecognitionService";
    private static final String VAD_INPUT = "input";
    private static final String VAD_SEARCH = "search";
    private static final String VAD_TOUCH = "touch";
    private static final String VERSION_NAME = "2.0.17.20150706";
    private static final Logger logger = Logger.getLogger(TAG);
    static HashMap<String, String> sKeyMapping = new HashMap<>();
    private Console console;
    private boolean internal;
    private final HashMap<RecognitionService.Callback, Task> mTasks = new HashMap<>();
    private Handler mainHandler;

    static {
        StubApp.interface11(757);
        Object[] data = {"cmn-Hans-CNtouch", 1536, "cmn-Hans-CNsearch", 1536, "cmn-Hans-CNinput", 521, "yue-Hans-CNtouch", 1636, "yue-Hans-CNsearch", 1636, "yue-Hans-CNinput", 1637, "en-GBtouch", 1736, "en-GBsearch", 1736, "en-GBinput", 1737, "sichuan-Hans-CNtouch", 1836, "sichuan-Hans-CNsearch", 1836, "sichuan-Hans-CNinput", 1837, "disabletouch", 1, "disablesearch", 1, "disableinput"/*, Integer.valueOf((int) ogrConstants.wkbLinearRing)*/, "enabletouch", 305, "enablesearch", 305, "enableinput", 305};
        for (int i = 0; i < data.length; i += 2) {
            PARAMS_MAP.put(data[i], data[i + 1]);
        }
        sKeyMapping.put(Constant.EXTRA_KEY, "decoder-server.key");
        sKeyMapping.put(Constant.EXTRA_SECRET, "decoder-server.secret");
        sKeyMapping.put("app", "decoder-server.app");
        sKeyMapping.put("auth", "decoder-server.auth");
        sKeyMapping.put(Constant.EXTRA_INFILE, "audio.file");
        sKeyMapping.put(Constant.EXTRA_OUTFILE, "audio.outfile");
        sKeyMapping.put(Constant.EXTRA_SAMPLE, "audio.sample");
        sKeyMapping.put(Constant.EXTRA_OFFLINE_ASR_BASE_FILE_PATH, "decoder-offline.asr-base-file-path");
        sKeyMapping.put(Constant.EXTRA_OFFLINE_LM_RES_FILE_PATH, "decoder-offline.lm-res-file-path");
        sKeyMapping.put(Constant.EXTRA_LICENSE_FILE_PATH, "decoder-offline.license-file-path");
        sKeyMapping.put("language", "decoder-offline.language");
        sKeyMapping.put(Constant.EXTRA_OFFLINE_SLOT_DATA, "decoder-offline.slot-data");
        sKeyMapping.put("preferred", "decoder-merge.preferred");
        try {
            String[] logs = {"MFE_JNI", TAG, "asr", "decoder", "Console", "HttpCallable", "MfeVadInputStream", "MicrophoneInputStream", "Parser", "TokenCallable", "EmbeddedASREngine"};
            for (String name : logs) {
                Logger.getLogger(name).setLevel(Level.WARNING);
            }
            if (Log.isLoggable("baidu_speech", 3)) {
                File f = new File(Environment.getExternalStorageDirectory(), "baidu_speech.log");
                if (f.exists()) {
                    f.renameTo(new File(f + FileSelector_Dialog.sFolder + System.currentTimeMillis()));
                }
                java.util.logging.Handler handler = new FileHandler(f.toString());
                handler.setFormatter(new SimpleFormatter());
                handler.setLevel(Level.ALL);
                Logger.getAnonymousLogger().addHandler(handler);
                Logger.getAnonymousLogger().info("\n\n\n==== " + new Date(System.currentTimeMillis()) + " pid=" + Process.myPid() + "\n========");
                for (String name2 : logs) {
                    Logger.getLogger(name2).setLevel(Level.ALL);
                    Logger.getLogger(name2).addHandler(handler);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        logger.info(String.format("onCreate(), hashcode=%s", Integer.valueOf(hashCode())));
        getSharedPreferences(SP_NAME_ASR, 0);
        try {
            Class.forName("com.baidu.android.voicedemo.SettingMore");
            this.internal = true;
        } catch (Exception e) {
        }
        logger.info("internal=" + this.internal);
        try {
            if (getPackageManager().getServiceInfo(new ComponentName(getPackageName(), getClass().getName()), 128).exported) {
                throw new AndroidRuntimeException(getClass().getName() + ", 'android:exported' should be false, please modify AndroidManifest.xml");
            }
        } catch (PackageManager.NameNotFoundException e2) {
            e2.printStackTrace();
        }
        if (this.console != null) {
            throw new AndroidRuntimeException("voice service has been inited");
        }
        this.console = new Console(StubApp.getOrigApplicationContext(getApplicationContext()));
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private String parseArgs(Intent intent) throws Exception {
        String secretKey;
        StringBuilder args = new StringBuilder();
        @SuppressLint("WrongConstant") ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), 128);
        String appid = appInfo.metaData == null ? null : "" + appInfo.metaData.get("com.baidu.speech.APP_ID");
        String apiKey = appInfo.metaData == null ? null : appInfo.metaData.getString("com.baidu.speech.API_KEY");
        if (appInfo.metaData == null) {
            secretKey = null;
        } else {
            secretKey = appInfo.metaData.getString("com.baidu.speech.SECRET_KEY");
        }
        if (!TextUtils.isEmpty(appid)) {
            args.append(" --basic.appid " + appid + " ");
        }
        if (!TextUtils.isEmpty(apiKey)) {
            args.append(" --decoder-server.key " + apiKey + " ");
        }
        if (!TextUtils.isEmpty(secretKey)) {
            args.append(" --decoder-server.secret " + secretKey + " ");
        }
        if (Process.myUid() != Binder.getCallingUid()) {
            throw new Exception("does not allow non-current program calls");
        }
        String language = intent.hasExtra("language") ? intent.getStringExtra("language") : LANGUAGE_CHINESE;
        String vad = intent.hasExtra(Constant.EXTRA_VAD) ? intent.getStringExtra(Constant.EXTRA_VAD) : "search";
        String nlu = intent.hasExtra(Constant.EXTRA_NLU) ? intent.getStringExtra(Constant.EXTRA_NLU) : NLU_DISABLE;
        if (vad != null && vad.length() > 0) {
            args.append(" --decoder-server.vad " + vad);
        }
        Object pdt = PARAMS_MAP.get(language + vad);
        if (pdt != null) {
            args.append(" --decoder-server.pdt " + pdt);
        }
        Object ptc = PARAMS_MAP.get(nlu + vad);
        if (ptc != null) {
            args.append(" --decoder-server.ptc " + ptc);
        }
        JSONArray list = new JSONArray();
        Object tmp = intent.getExtras().get(Constant.EXTRA_PROP);
        if (tmp instanceof Integer) {
            list.put(tmp);
        }
        if (tmp instanceof int[]) {
            for (int v : (int[]) tmp) {
                list.put(v);
            }
        }
        if (tmp instanceof String) {
            for (String s1 : ((String) tmp).split(",")) {
                list.put(Integer.valueOf(Integer.parseInt(s1)));
            }
        }
        if (list.length() > 0) {
            JSONObject propJson = new JSONObject();
            propJson.put("prop_list", list);
            args.append(" --decoder-server.prop_list " + propJson);
            args.append(" --decoder-offline.prop " + list.get(0));
        }
        for (String k : intent.getExtras().keySet()) {
            Object v2 = intent.getExtras().get(k);
            if (v2 != null && !C0246Config.EMPTY_STRING.equals("" + v2) && !k.equals("args")) {
                String key = sKeyMapping.get(k);
                if (key == null) {
                    key = k.contains(FileSelector_Dialog.sFolder) ? k : "basic." + k;
                }
                String val = v2 + "";
                if (!"".equals(val)) {
                    args.append(" --" + key + " " + val + " ");
                }
            }
        }
        if (this.internal) {
            String str = intent.getStringExtra("args");
            logger.info("internal task, use args=" + ((Object) args));
            if (str != null) {
                args.append(" " + str);
            }
        }
        return args.toString();
    }

    /* access modifiers changed from: protected */
    @Override // android.speech.RecognitionService
    public void onStartListening(Intent recognizerIntent, RecognitionService.Callback listener) {
        for (Task task : this.mTasks.values()) {
            if (!task.isCanceled()) {
                task.cancel();
            }
        }
        logger.info(String.format("--onStartListening(intent, listener=%s) \t%s", Integer.toHexString(listener.hashCode()), Integer.toHexString(hashCode())));
        Task task2 = new Task(recognizerIntent, listener);
        new Thread(task2, "console-reader").start();
        this.mTasks.put(listener, task2);
    }

    /* access modifiers changed from: protected */
    @Override // android.speech.RecognitionService
    public void onCancel(RecognitionService.Callback listener) {
        logger.info(String.format("--onCancel(listener=%s) \t%s", Integer.toHexString(listener.hashCode()), Integer.toHexString(hashCode())));
        Task t = this.mTasks.get(listener);
        if (t != null) {
            t.cancel();
        }
    }

    @Override // android.speech.RecognitionService, android.app.Service
    public void onDestroy() {
        super.onDestroy();
        logger.info(String.format("--onDestroy() \t%s", Integer.toHexString(hashCode())));
        for (Task task : this.mTasks.values()) {
            if (task != null) {
                task.cancel();
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.speech.RecognitionService
    public void onStopListening(RecognitionService.Callback listener) {
        logger.info(String.format("--onStopListening(listener=%s) \t%s", Integer.toHexString(listener.hashCode()), Integer.toHexString(hashCode())));
        Task t = this.mTasks.get(listener);
        if (t != null) {
            t.stop();
        }
    }

    /* access modifiers changed from: package-private */
    public class Task implements Runnable {
        private volatile boolean canceled;
        private Intent intent;
        private RecognitionService.Callback listener;
        private Console.Session session;

        Task(Intent intent2, RecognitionService.Callback listener2) {
            this.intent = intent2;
            this.listener = listener2;
        }

        public void stop() {
            if (this.session != null) {
                this.session.cancel(false);
            }
        }

        public void cancel() {
            this.canceled = true;
            if (this.session != null) {
                this.session.cancel(true);
            }
        }

        public boolean isCanceled() {
            return this.canceled;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.lang.Runnable
        public void run() {
            Object val;
            Throwable t;
            try {
                val = _run();
            } catch (Throwable e) {
                e.printStackTrace();
                val = e;
            }
            try {
                if (val instanceof Results.FinalResult) {
                    Bundle b = Parser.convertToBundler((Results.FinalResult) val);
                    VoiceRecognitionService.logger.info(String.format("--|--call listener.results", new Object[0]));
                    this.listener.results(b);
                } else {
                    if (val instanceof Throwable) {
                        t = (Throwable) val;
                    } else {
                        t = new Exception("#7, No recognition result matched. non expected results: " + val);
                    }
                    if (!this.canceled) {
                        Matcher matcher = Pattern.compile("^#(\\d+)[\t]*,.+").matcher(t.getMessage() + "");
                        if (matcher.find()) {
                            this.listener.error(Integer.parseInt(matcher.group(1)));
                        } else {
                            this.listener.error(7);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("reason", t.getMessage());
                        callbackOnEvent(this.listener, 11, bundle);
                    }
                }
            } catch (RemoteException e2) {
                e2.printStackTrace();
            } finally {
                VoiceRecognitionService.this.mTasks.remove(this.listener);
            }
        }

        private Object _run() throws Exception {
            Console.Msg msg;
            String args = VoiceRecognitionService.this.parseArgs(this.intent);
            this.session = VoiceRecognitionService.this.console.enter(args == null ? "" : "" + args);
            boolean exit = false;
            Object obj = null;
            while (!exit) {
                while (true) {
                    msg = this.session.msg();
                    if (msg != null) {
                        break;
                    }
                    Thread.sleep(3);
                }
                String key = msg.getKey();
                Object val = msg.getValue();
                if (Console.TYPE_ASR_MSG_READY.equals(key)) {
                    this.listener.readyForSpeech(new Bundle());
                    VoiceRecognitionService.logger.info(String.format("--|--ready, hashcode=%s", Integer.valueOf(this.listener.hashCode())));
                } else if (Console.TYPE_ASR_MSG_BEGIN.equals(key)) {
                    VoiceRecognitionService.logger.info(String.format("--|--begin, hashcode=%s", Integer.valueOf(this.listener.hashCode())));
                    this.listener.beginningOfSpeech();
                } else if (Console.TYPE_ASR_MSG_AUDIO.equals(key)) {
                    this.listener.bufferReceived((byte[]) msg.getValue());
                } else if (Console.TYPE_ASR_MSG_VOLUME.equals(key)) {
                    this.listener.rmsChanged(((Float) msg.getValue()).floatValue());
                } else if (Console.TYPE_ASR_MSG_END.equals(key)) {
                    VoiceRecognitionService.logger.info(String.format("--|--end, hashcode=%s", Integer.valueOf(this.listener.hashCode())));
                    this.listener.endOfSpeech();
                } else if (Console.TYPE_ASR_MSG_PARTIAL.equals(key)) {
                    if (val instanceof Results.RunningResult) {
                        this.listener.partialResults(Parser.convertToBundler((Results.RunningResult) val));
                    }
                } else if (Console.TYPE_ASR_MSG_FINISH.equals(key)) {
                    VoiceRecognitionService.logger.info(String.format("--|--finish, %s, hashcode=%s", val, Integer.valueOf(this.listener.hashCode())));
                    obj = val;
                } else if ("exit".equals(key)) {
                    exit = true;
                } else if (Console.TYPE_ASR_MSG_ENGINE_TYPE.equals(key) && (val instanceof MergedDecoder.MessageResult)) {
                    int type = ((MergedDecoder.MessageResult) val).getEngineType();
                    Bundle bundle = new Bundle();
                    bundle.putInt("engine_type", type);
                    callbackOnEvent(this.listener, 12, bundle);
                }
            }
            return obj;
        }

        private final void callbackOnEvent(RecognitionService.Callback listener2, int eventType, Bundle bundle) {
            try {
                Field f = listener2.getClass().getDeclaredField("mListener");
                f.setAccessible(true);
                Class.forName("android.speech.IRecognitionListener").getMethod("onEvent", Integer.TYPE, Bundle.class).invoke(f.get(listener2), Integer.valueOf(eventType), bundle);
            } catch (Exception e) {
                e.printStackTrace();
                VoiceRecognitionService.logger.log(Level.WARNING, "", (Throwable) e);
            }
        }
    }

    public static String getSdkVersion() {
        return VERSION_NAME;
    }
}
