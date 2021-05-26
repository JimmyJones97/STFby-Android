package com.xzy.forestSystem.baidu.voicerecognition.android.p007ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.AndroidRuntimeException;
import android.util.Log;
import com.xzy.forestSystem.baidu.speech.VoiceRecognitionService;
import  com.xzy.forestSystem.gogisapi.Common.Constant;
import java.io.File;

/* renamed from: com.baidu.voicerecognition.android.ui.BaiduASRDialog */
public abstract class BaiduASRDialog extends Activity {
    protected static final int ERROR_NONE = 0;
    public static final String PARAM_LANGUAGE = "language";
    public static final String PARAM_PORMPT_TEXT = "prompt_text";
    public static final int STATUS_None = 0;
    public static final int STATUS_Ready = 3;
    public static final int STATUS_Recognition = 5;
    public static final int STATUS_Speaking = 4;
    public static final int STATUS_WaitingReady = 2;
    private static final String TAG = "BaiduASRDialog";
    private long VOLUME_INTERVAL = 140;
    private Handler mHandler = new Handler();
    private volatile boolean mIsRunning = false;
    protected MyRecognitionListener mMyRecognitionListener;
    private Bundle mParams = new Bundle();
    protected String mPrompt;
    private String mRetryFile;
    SpeechRecognizer speechRecognizer;
    protected int status = 0;

    /* access modifiers changed from: protected */
    public abstract void onBeginningOfSpeech();

    /* access modifiers changed from: protected */
    public abstract void onEndOfSpeech();

    /* access modifiers changed from: protected */
    public abstract void onEvent(int i, Bundle bundle);

    /* access modifiers changed from: protected */
    public abstract void onFinish(int i, int i2);

    /* access modifiers changed from: protected */
    public abstract void onPartialResults(Bundle bundle);

    /* access modifiers changed from: protected */
    public abstract void onPrepared();

    /* access modifiers changed from: protected */
    public abstract void onRecognitionStart();

    /* access modifiers changed from: protected */
    public abstract void onVolumeChanged(float f);

    /* renamed from: com.baidu.voicerecognition.android.ui.BaiduASRDialog$MyRecognitionListener */
    protected class MyRecognitionListener implements RecognitionListener {
        protected MyRecognitionListener() {
        }

        @Override // android.speech.RecognitionListener
        public void onReadyForSpeech(Bundle params) {
            BaiduASRDialog.this.onPrepared();
            BaiduASRDialog.this.status = 3;
        }

        @Override // android.speech.RecognitionListener
        public void onBeginningOfSpeech() {
            BaiduASRDialog.this.status = 4;
            BaiduASRDialog.this.onBeginningOfSpeech();
        }

        @Override // android.speech.RecognitionListener
        public void onRmsChanged(float rmsdB) {
            BaiduASRDialog.this.onVolumeChanged(rmsdB);
        }

        @Override // android.speech.RecognitionListener
        public void onBufferReceived(byte[] buffer) {
        }

        @Override // android.speech.RecognitionListener
        public void onEndOfSpeech() {
            BaiduASRDialog.this.status = 5;
            BaiduASRDialog.this.onEndOfSpeech();
        }

        @Override // android.speech.RecognitionListener
        public void onError(int error) {
            BaiduASRDialog.this.status = 0;
            BaiduASRDialog.this.mIsRunning = false;
            BaiduASRDialog.this.onFinish(error, error);
        }

        @Override // android.speech.RecognitionListener
        public void onResults(Bundle results) {
            BaiduASRDialog.this.status = 0;
            BaiduASRDialog.this.mIsRunning = false;
            BaiduASRDialog.this.onPartialResults(results);
            BaiduASRDialog.this.onFinish(0, 0);
            Intent intentResult = new Intent();
            intentResult.putExtras(results);
            BaiduASRDialog.this.setResult(-1, intentResult);
            BaiduASRDialog.this.finish();
        }

        @Override // android.speech.RecognitionListener
        public void onPartialResults(Bundle partialResults) {
            BaiduASRDialog.this.onPartialResults(partialResults);
        }

        @Override // android.speech.RecognitionListener
        public void onEvent(int eventType, Bundle params) {
            BaiduASRDialog.this.onEvent(eventType, params);
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            boolean exported = getPackageManager().getActivityInfo(new ComponentName(getPackageName(), getClass().getName()), 128).exported;
            if (exported) {
                throw new AndroidRuntimeException(getClass().getName() + ", 'android:exported' should be false, please modify AndroidManifest.xml");
            }
            Log.d("export", "exported:" + exported);
            this.mMyRecognitionListener = new MyRecognitionListener();
            this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this, new ComponentName(this, VoiceRecognitionService.class));
            this.speechRecognizer.setRecognitionListener(this.mMyRecognitionListener);
            Intent intent = getIntent();
            if (intent != null) {
                this.mParams.putAll(intent.getExtras());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void speakFinish() {
        this.speechRecognizer.stopListening();
    }

    /* access modifiers changed from: protected */
    public void startRecognition() {
        startRecognition(false);
    }

    /* access modifiers changed from: protected */
    public void startRecognition(boolean useBackAudio) {
        this.mPrompt = this.mParams.getString(PARAM_PORMPT_TEXT);
        this.mIsRunning = true;
        onRecognitionStart();
        Intent intent = getIntent();
        this.mRetryFile = new File(getCacheDir(), "bd_asr_ui_retry_file.pcm").toString();
        if (useBackAudio) {
            intent.putExtra(Constant.EXTRA_INFILE, this.mRetryFile);
            intent.putExtra("ui.retry-file", "");
        } else {
            intent.removeExtra(Constant.EXTRA_INFILE);
            intent.putExtra("ui.retry-file", this.mRetryFile);
        }
        this.speechRecognizer.startListening(getIntent());
    }

    /* access modifiers changed from: protected */
    public void cancleRecognition() {
        this.speechRecognizer.cancel();
        this.status = 0;
    }

    public Bundle getParams() {
        return this.mParams;
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onPause() {
        super.onPause();
        this.speechRecognizer.cancel();
        if (!isFinishing()) {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.speechRecognizer.destroy();
    }
}
