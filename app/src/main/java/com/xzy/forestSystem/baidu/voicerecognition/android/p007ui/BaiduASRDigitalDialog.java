package com.xzy.forestSystem.baidu.voicerecognition.android.p007ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.xzy.forestSystem.baidu.voicerecognition.android.NoProGuard;
import  com.xzy.forestSystem.stub.StubApp;
import com.xzy.forestSystem.baidu.voicerecognition.android.NoProGuard;

import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;

@SuppressLint({"Registered"})
/* renamed from: com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog */
public class BaiduASRDigitalDialog extends BaiduASRDialog implements NoProGuard {
    private static final int ENGINE_TYPE_OFFLINE = 1;
    private static final int ENGINE_TYPE_ONLINE = 0;
    private static final int ERROR_NETWORK_UNUSABLE = 589824;
    private static final String KEY_BTN_CANCEL = "btn.cancel";
    private static final String KEY_BTN_DONE = "btn.done";
    private static final String KEY_BTN_HELP = "btn.help";
    private static final String KEY_BTN_RETRY = "btn.retry";
    private static final String KEY_BTN_START = "btn.start";
    private static final String KEY_TIPS_COPYRIGHT = "tips.copyright";
    private static final String KEY_TIPS_ERROR_DECODER = "tips.error.decoder";
    private static final String KEY_TIPS_ERROR_INTERNAL = "tips.error.internal";
    private static final String KEY_TIPS_ERROR_NETWORK = "tips.error.network";
    private static final String KEY_TIPS_ERROR_NETWORK_UNUSABLE = "tips.error.network_unusable";
    private static final String KEY_TIPS_ERROR_SILENT = "tips.error.silent";
    private static final String KEY_TIPS_ERROR_SPEECH_TOO_LONG = "tips.error.speech_too_long";
    private static final String KEY_TIPS_ERROR_SPEECH_TOO_SHORT = "tips.error.speech_too_short";
    private static final String KEY_TIPS_HELP_TITLE = "tips.help.title";
    private static final String KEY_TIPS_PREFIX = "tips.suggestion.prefix";
    private static final String KEY_TIPS_STATE_INITIALIZING = "tips.state.initializing";
    private static final String KEY_TIPS_STATE_LISTENING = "tips.state.listening";
    private static final String KEY_TIPS_STATE_READY = "tips.state.ready";
    private static final String KEY_TIPS_STATE_RECOGNIZING = "tips.state.recognizing";
    private static final String KEY_TIPS_STATE_WAIT = "tips.state.wait";
    private static final String KEY_TIPS_WAITNET = "tips.wait.net";
    public static final String PARAM_DIALOG_THEME = "BaiduASRDigitalDialog_theme";
    public static final String PARAM_SHOW_HELP_ON_SILENT = "BaiduASRDigitalDialog_showHelp";
    public static final String PARAM_SHOW_TIP = "BaiduASRDigitalDialog_showTip";
    public static final String PARAM_SHOW_TIPS_ON_START = "BaiduASRDigitalDialog_showTips";
    public static final String PARAM_TIPS = "BaiduASRDigitalDialog_tips";
    private static final String TAG = "BSDigitalDialog";
    public static final int THEME_BLUE_DEEPBG = 33554433;
    public static final int THEME_BLUE_LIGHTBG = 16777217;
    public static final int THEME_GREEN_DEEPBG = 33554435;
    public static final int THEME_GREEN_LIGHTBG = 16777219;
    public static final int THEME_ORANGE_DEEPBG = 33554436;
    public static final int THEME_ORANGE_LIGHTBG = 16777220;
    public static final int THEME_RED_DEEPBG = 33554434;
    public static final int THEME_RED_LIGHTBG = 16777218;
    private static final String mUrl = "http://developer.baidu.com/static/community/servers/voice/sdk.html";
    private final int BAR_ONEND = 0;
    private final int BAR_ONFINISH = 1;
    private long SHOW_SUGGESTION_INTERVAL = 3000;
    Handler barHandler = new Handler() { // from class: com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog.4
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (BaiduASRDigitalDialog.this.delayTime >= 3000) {
                    if (BaiduASRDigitalDialog.this.mInputEdit.getVisibility() == 0) {
                        BaiduASRDigitalDialog.this.mInputEdit.setVisibility(4);
                    }
                    BaiduASRDigitalDialog.this.mTipsTextView.setVisibility(4);
                    if (BaiduASRDigitalDialog.this.mEngineType == 0) {
                        BaiduASRDigitalDialog.this.mWaitNetTextView.setText(BaiduASRDigitalDialog.this.getString(BaiduASRDigitalDialog.KEY_TIPS_WAITNET));
                        BaiduASRDigitalDialog.this.mWaitNetTextView.setVisibility(0);
                    }
                } else if (BaiduASRDigitalDialog.this.mInputEdit.getVisibility() == 0) {
                    BaiduASRDigitalDialog.this.mTipsTextView.setVisibility(4);
                    BaiduASRDigitalDialog.this.mWaitNetTextView.setVisibility(4);
                } else {
                    BaiduASRDigitalDialog.this.mTipsTextView.setVisibility(0);
                    BaiduASRDigitalDialog.this.mWaitNetTextView.setVisibility(4);
                }
                BaiduASRDigitalDialog.this.mMessage.what = 0;
                if (BaiduASRDigitalDialog.this.step <= 30) {
                    BaiduASRDigitalDialog.this.delayTime += 10;
                    BaiduASRDigitalDialog.this.step++;
                    BaiduASRDigitalDialog.this.barHandler.sendEmptyMessageDelayed(0, 10);
                } else if (BaiduASRDigitalDialog.this.step < 60) {
                    BaiduASRDigitalDialog.this.delayTime += 100;
                    BaiduASRDigitalDialog.this.step++;
                    BaiduASRDigitalDialog.this.barHandler.sendEmptyMessageDelayed(0, 100);
                } else if (BaiduASRDigitalDialog.this.delayTime >= 15000) {
                    BaiduASRDigitalDialog.this.cancleRecognition();
                    BaiduASRDigitalDialog.this.onFinish(2, BaiduASRDigitalDialog.ERROR_NETWORK_UNUSABLE);
                    BaiduASRDigitalDialog.this.step = 0;
                    BaiduASRDigitalDialog.this.delayTime = 0;
                    BaiduASRDigitalDialog.this.mSDKProgressBar.setVisibility(4);
                    BaiduASRDigitalDialog.this.barHandler.removeMessages(0);
                } else {
                    BaiduASRDigitalDialog.this.step = 60;
                    BaiduASRDigitalDialog.this.delayTime += 100;
                    BaiduASRDigitalDialog.this.barHandler.sendEmptyMessageDelayed(0, 100);
                }
                BaiduASRDigitalDialog.this.mSDKProgressBar.setProgress(BaiduASRDigitalDialog.this.step);
            } else if (msg.what == 1) {
                if (BaiduASRDigitalDialog.this.step <= 80) {
                    BaiduASRDigitalDialog.this.step += 3;
                    BaiduASRDigitalDialog.this.barHandler.sendEmptyMessageDelayed(1, 1);
                } else {
                    BaiduASRDigitalDialog.this.step = 0;
                    BaiduASRDigitalDialog.this.delayTime = 0;
                    BaiduASRDigitalDialog.this.mInputEdit.setVisibility(8);
                    BaiduASRDigitalDialog.this.mSDKProgressBar.setVisibility(4);
                    if (BaiduASRDigitalDialog.this.mErrorCode == 0) {
                        BaiduASRDigitalDialog.this.finish();
                    }
                    BaiduASRDigitalDialog.this.barHandler.removeMessages(1);
                }
                BaiduASRDigitalDialog.this.mSDKProgressBar.setProgress(BaiduASRDigitalDialog.this.step);
            }
        }
    };
    private int delayTime = 0;
    private Drawable mBg;
    private StateListDrawable mButtonBg = new StateListDrawable();
    private ColorStateList mButtonColor;
    private ColorStateList mButtonReverseColor;
    private ImageButton mCancelBtn;
    private TextView mCancelTextView;
    private View.OnClickListener mClickListener = new View.OnClickListener() { // from class: com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog.2
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if ("speak_complete".equals(v.getTag())) {
                String btntitle = BaiduASRDigitalDialog.this.mCompleteTextView.getText().toString();
                if (btntitle.equals(BaiduASRDigitalDialog.this.getString(BaiduASRDigitalDialog.KEY_BTN_START))) {
                    BaiduASRDigitalDialog.this.step = 0;
                    BaiduASRDigitalDialog.this.delayTime = 0;
                    BaiduASRDigitalDialog.this.mSDKProgressBar.setVisibility(4);
                    BaiduASRDigitalDialog.this.startRecognition();
                } else if (!btntitle.equals(BaiduASRDigitalDialog.this.getString(BaiduASRDigitalDialog.KEY_BTN_DONE))) {
                } else {
                    if (BaiduASRDigitalDialog.this.status == 4) {
                        BaiduASRDigitalDialog.this.speakFinish();
                        BaiduASRDigitalDialog.this.onEndOfSpeech();
                        return;
                    }
                    BaiduASRDigitalDialog.this.cancleRecognition();
                    BaiduASRDigitalDialog.this.mMyRecognitionListener.onError(7);
                }
            } else if ("cancel_text_btn".equals(v.getTag())) {
                if (BaiduASRDigitalDialog.this.mCancelTextView.getText().toString().equals(BaiduASRDigitalDialog.this.getString(BaiduASRDigitalDialog.KEY_BTN_HELP))) {
                    BaiduASRDigitalDialog.this.showSuggestions();
                } else {
                    BaiduASRDigitalDialog.this.finish();
                }
            } else if ("retry_text_btn".equals(v.getTag())) {
                BaiduASRDigitalDialog.this.step = 0;
                BaiduASRDigitalDialog.this.delayTime = 0;
                BaiduASRDigitalDialog.this.mInputEdit.setVisibility(8);
                BaiduASRDigitalDialog.this.mSDKProgressBar.setVisibility(4);
                BaiduASRDigitalDialog.this.startRecognition();
            } else if ("cancel_btn".equals(v.getTag())) {
                BaiduASRDigitalDialog.this.finish();
            } else if ("help_btn".equals(v.getTag())) {
                BaiduASRDigitalDialog.this.showSuggestions();
            } else if ("logo_1".equals(v.getTag()) || "logo_2".equals(v.getTag())) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(BaiduASRDigitalDialog.mUrl));
                intent.setFlags(268435456);
                try {
                    BaiduASRDigitalDialog.this.startActivity(intent);
                    BaiduASRDigitalDialog.this.finish();
                } catch (Exception e) {
                }
            }
        }
    };
    private TextView mCompleteTextView;
    private View mContentRoot = null;
    private int mCopyRightColor = 0;
    private volatile int mEngineType = 0;
    private int mErrorCode;
    private View mErrorLayout;
    private CharSequence mErrorRes = "";
    private int mErrorTipsColor = 0;
    private TextView mErrorTipsTextView;
    private Handler mHandler = new Handler();
    private ImageButton mHelpBtn;
    private StateListDrawable mHelpButtonBg = new StateListDrawable();
    private View mHelpView;
    private EditText mInputEdit;
    private ResourceBundle mLableRes;
    private StateListDrawable mLeftButtonBg = new StateListDrawable();
    private TextView mLogoText1;
    private TextView mLogoText2;
    private View mMainLayout;
    Message mMessage = Message.obtain();
    private String mPrefix;
    private Random mRandom = new Random();
    private View mRecognizingView;
    private TextView mRetryTextView;
    private StateListDrawable mRightButtonBg = new StateListDrawable();
    private SDKProgressBar mSDKProgressBar;
    private Runnable mShowSuggestionTip = new Runnable() { // from class: com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog.1
        @Override // java.lang.Runnable
        public void run() {
            BaiduASRDigitalDialog.this.showSuggestionTips();
        }
    };
    private int mStateTipsColor = 0;
    private TextView mSuggestionTips;
    private TextView mSuggestionTips2;
    private int mTheme = 0;
    private TipsAdapter mTipsAdapter;
    private TextView mTipsTextView;
    private TextView mTitle;
    private SDKAnimationView mVoiceWaveView;
    private TextView mWaitNetTextView;
    private int step = 0;

    static {
        StubApp.interface11(800);
    }

    /* access modifiers changed from: protected */
    @Override // com.baidu.voicerecognition.android.p007ui.BaiduASRDialog, android.app.Activity
    public native void onCreate(Bundle bundle);

    private void initView() {
        initResources(this.mTheme);
        this.mContentRoot = View.inflate(this, getResources().getIdentifier("bdspeech_digital_layout", "layout", getPackageName()), null);
        if (this.mContentRoot != null) {
            this.mContentRoot.findViewWithTag("bg_layout").setBackgroundDrawable(this.mBg);
            this.mTipsTextView = (TextView) this.mContentRoot.findViewWithTag("tips_text");
            this.mTipsTextView.setTextColor(this.mStateTipsColor);
            this.mWaitNetTextView = (TextView) this.mContentRoot.findViewWithTag("tips_wait_net");
            this.mWaitNetTextView.setVisibility(4);
            this.mWaitNetTextView.setTextColor(this.mStateTipsColor);
            this.mLogoText1 = (TextView) this.mContentRoot.findViewWithTag("logo_1");
            this.mLogoText2 = (TextView) this.mContentRoot.findViewWithTag("logo_2");
            this.mLogoText1.setOnClickListener(this.mClickListener);
            this.mLogoText2.setOnClickListener(this.mClickListener);
            this.mLogoText1.setTextColor(this.mCopyRightColor);
            this.mLogoText2.setTextColor(this.mCopyRightColor);
            this.mSuggestionTips = (TextView) this.mContentRoot.findViewWithTag("suggestion_tips");
            this.mSuggestionTips.setTextColor(this.mCopyRightColor);
            this.mSuggestionTips2 = (TextView) this.mContentRoot.findViewWithTag("suggestion_tips_2");
            this.mSuggestionTips2.setTextColor(this.mCopyRightColor);
            this.mSDKProgressBar = (SDKProgressBar) this.mContentRoot.findViewWithTag("progress");
            this.mSDKProgressBar.setVisibility(4);
            this.mSDKProgressBar.setTheme(this.mTheme);
            this.mCompleteTextView = (TextView) this.mContentRoot.findViewWithTag("speak_complete");
            this.mCompleteTextView.setOnClickListener(this.mClickListener);
            this.mCompleteTextView.setBackgroundDrawable(this.mButtonBg);
            this.mCompleteTextView.setTextColor(this.mButtonReverseColor);
            this.mCancelTextView = (TextView) this.mContentRoot.findViewWithTag("cancel_text_btn");
            this.mCancelTextView.setOnClickListener(this.mClickListener);
            this.mCancelTextView.setBackgroundDrawable(this.mLeftButtonBg);
            this.mCancelTextView.setTextColor(this.mButtonColor);
            this.mRetryTextView = (TextView) this.mContentRoot.findViewWithTag("retry_text_btn");
            this.mRetryTextView.setOnClickListener(this.mClickListener);
            this.mRetryTextView.setBackgroundDrawable(this.mRightButtonBg);
            this.mRetryTextView.setTextColor(this.mButtonReverseColor);
            this.mErrorTipsTextView = (TextView) this.mContentRoot.findViewWithTag("error_tips");
            this.mErrorTipsTextView.setTextColor(this.mErrorTipsColor);
            Drawable bgDrawable = getResources().getDrawable(getResources().getIdentifier("bdspeech_close_v2", "drawable", getPackageName()));
            this.mCancelBtn = (ImageButton) this.mContentRoot.findViewWithTag("cancel_btn");
            this.mCancelBtn.setOnClickListener(this.mClickListener);
            this.mCancelBtn.setImageDrawable(bgDrawable);
            this.mHelpBtn = (ImageButton) this.mContentRoot.findViewWithTag("help_btn");
            this.mHelpBtn.setOnClickListener(this.mClickListener);
            this.mHelpBtn.setImageDrawable(this.mHelpButtonBg);
            this.mErrorLayout = this.mContentRoot.findViewWithTag("error_reflect");
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mErrorLayout.getLayoutParams();
            this.mContentRoot.findViewWithTag("main_reflect").setId(2131492999);
            layoutParams.addRule(6, 2131492999);
            layoutParams.addRule(8, 2131492999);
            this.mVoiceWaveView = (SDKAnimationView) this.mContentRoot.findViewWithTag("voicewave_view");
            this.mVoiceWaveView.setThemeStyle(this.mTheme);
            this.mMainLayout = this.mContentRoot.findViewWithTag("main_reflect");
            this.mVoiceWaveView.setVisibility(4);
            this.mRecognizingView = this.mContentRoot.findViewWithTag("recognizing_reflect");
            this.mHelpView = this.mContentRoot.findViewWithTag("help_reflect");
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mHelpView.getLayoutParams();
            layoutParams2.addRule(6, 2131492999);
            layoutParams2.addRule(8, 2131492999);
            this.mTitle = (TextView) this.mContentRoot.findViewWithTag("help_title");
            this.mTitle.setTextColor(this.mStateTipsColor);
            this.mTipsAdapter = new TipsAdapter(this);
            this.mTipsAdapter.setNotifyOnChange(true);
            this.mTipsAdapter.setTextColor(this.mStateTipsColor);
            ((ListView) this.mContentRoot.findViewWithTag("suggestions_list")).setAdapter((ListAdapter) this.mTipsAdapter);
            this.mInputEdit = (EditText) this.mContentRoot.findViewWithTag("partial_text");
            this.mInputEdit.setTextColor(this.mStateTipsColor);
            requestWindowFeature(1);
            setContentView(new View(this));
            addContentView(this.mContentRoot, new FrameLayout.LayoutParams(-1, -1));
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        adjustThemeColor();
    }

    private void adjustThemeColor() {
        float hue = 0.0f;
        switch (this.mTheme) {
            case 16777217:
                hue = 0.0f;
                break;
            case 16777218:
                hue = 148.0f;
                break;
            case 16777219:
                hue = -108.0f;
                break;
            case 16777220:
                hue = -178.0f;
                break;
            case 33554433:
                hue = 0.0f;
                break;
            case 33554434:
                hue = 151.0f;
                break;
            case 33554435:
                hue = -109.0f;
                break;
            case 33554436:
                hue = -178.0f;
                break;
        }
        ColorMatrix cm = new ColorMatrix();
        ColorFilterGenerator.adjustColor(cm, 0.0f, 0.0f, 0.0f, hue);
        ColorFilter filter = new ColorMatrixColorFilter(cm);
        this.mBg.setColorFilter(filter);
        this.mButtonBg.setColorFilter(filter);
        this.mLeftButtonBg.setColorFilter(filter);
        this.mRightButtonBg.setColorFilter(filter);
        this.mSDKProgressBar.setHsvFilter(filter);
        this.mVoiceWaveView.setHsvFilter(filter);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void showSuggestions() {
        this.mErrorLayout.setVisibility(4);
        this.mMainLayout.setVisibility(0);
        this.mRecognizingView.setVisibility(4);
        this.mHelpView.setVisibility(0);
        this.mCompleteTextView.setText(getString(KEY_BTN_START));
        this.mCompleteTextView.setEnabled(true);
        this.mHelpBtn.setVisibility(4);
        this.mHandler.removeCallbacks(this.mShowSuggestionTip);
        cancleRecognition();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void showSuggestionTips() {
        this.mSuggestionTips.setText(this.mPrefix + ((String) this.mTipsAdapter.getItem(this.mRandom.nextInt(this.mTipsAdapter.getCount()))));
        this.mSuggestionTips.setVisibility(0);
        this.mLogoText1.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public void _onStart() {
        this.mTipsAdapter.clear();
        String[] temp = getParams().getStringArray(PARAM_TIPS);
        if (temp != null) {
            if (Build.VERSION.SDK_INT >= 11) {
                this.mTipsAdapter.addAll(temp);
            } else {
                for (String tip : temp) {
                    this.mTipsAdapter.add(tip);
                }
            }
        }
        boolean showTips = false;
        if (this.mTipsAdapter.getCount() > 0) {
            this.mHelpBtn.setVisibility(0);
            showTips = getParams().getBoolean(PARAM_SHOW_TIPS_ON_START, false);
        } else {
            this.mHelpBtn.setVisibility(4);
        }
        if (showTips) {
            showSuggestions();
        }
    }

    private void loadI18N() {
        try {
            this.mLableRes = ResourceBundle.getBundle("BaiduASRDigitalDialog");
            this.mLogoText1.setText(getString(KEY_TIPS_COPYRIGHT));
            this.mLogoText2.setText(getString(KEY_TIPS_COPYRIGHT));
            this.mRetryTextView.setText(getString(KEY_BTN_RETRY));
            this.mTitle.setText(getString(KEY_TIPS_HELP_TITLE));
            this.mPrefix = getString(KEY_TIPS_PREFIX);
        } catch (MissingResourceException e) {
            Log.w(TAG, "loadI18N error", e);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private String getString(String key) {
        if (this.mLableRes == null) {
            return null;
        }
        try {
            return this.mLableRes.getString(key);
        } catch (Exception e) {
            Log.w(TAG, "get internationalization error key:" + key, e);
            return null;
        }
    }

    /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x0270: APUT  (r5v0 'colors' int[] A[D('colors' int[])]), (0 ??[int, short, byte, char]), (-12105913 int) */
    /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x0280: APUT  (r6v0 'colors_reverse' int[] A[D('colors_reverse' int[])]), (0 ??[int, short, byte, char]), (-1 int) */
    private void initResources(int theme) {
        Integer bgName;
        Integer leftButtonNormalBgName;
        Integer leftButtonPressedBgName;
        Integer buttonRecognizingBgName;
        Integer buttonNormalBgName = Integer.valueOf(getResources().getIdentifier("bdspeech_btn_normal", "drawable", getPackageName()));
        Integer buttonPressedBgName = Integer.valueOf(getResources().getIdentifier("bdspeech_btn_pressed", "drawable", getPackageName()));
        Integer rightButtonNormalBgName = Integer.valueOf(getResources().getIdentifier("bdspeech_right_normal", "drawable", getPackageName()));
        Integer rightButtonPressedBgName = Integer.valueOf(getResources().getIdentifier("bdspeech_right_pressed", "drawable", getPackageName()));
        int[] colors = new int[3];
        int[] colors_reverse = new int[3];
        if (BaiduASRDialogTheme.isDeepStyle(theme)) {
            bgName = Integer.valueOf(getResources().getIdentifier("bdspeech_digital_deep_bg", "drawable", getPackageName()));
            leftButtonNormalBgName = Integer.valueOf(getResources().getIdentifier("bdspeech_left_deep_normal", "drawable", getPackageName()));
            leftButtonPressedBgName = Integer.valueOf(getResources().getIdentifier("bdspeech_left_deep_pressed", "drawable", getPackageName()));
            buttonRecognizingBgName = Integer.valueOf(getResources().getIdentifier("bdspeech_btn_recognizing_deep", "drawable", getPackageName()));
            colors[0] = -1;
            colors[1] = -11711155;
            colors[2] = -1;
            colors_reverse[0] = -1;
            colors_reverse[1] = -11711155;
            colors_reverse[2] = -1;
            this.mCopyRightColor = -10592672;
            this.mStateTipsColor = -3750202;
            this.mErrorTipsColor = -1579033;
            this.mHelpButtonBg.addState(new int[]{16842919}, getResources().getDrawable(getResources().getIdentifier("bdspeech_help_pressed_deep", "drawable", getPackageName())));
            this.mHelpButtonBg.addState(new int[0], getResources().getDrawable(getResources().getIdentifier("bdspeech_help_deep", "drawable", getPackageName())));
        } else {
            bgName = Integer.valueOf(getResources().getIdentifier("bdspeech_digital_bg", "drawable", getPackageName()));
            leftButtonNormalBgName = Integer.valueOf(getResources().getIdentifier("bdspeech_left_normal", "drawable", getPackageName()));
            leftButtonPressedBgName = Integer.valueOf(getResources().getIdentifier("bdspeech_left_pressed", "drawable", getPackageName()));
            buttonRecognizingBgName = Integer.valueOf(getResources().getIdentifier("bdspeech_btn_recognizing", "drawable", getPackageName()));
            colors[0] = -12105913;
            colors[1] = -1513240;
            colors[2] = -12105913;
            colors_reverse[0] = -1;
            colors_reverse[1] = -4276546;
            colors_reverse[2] = -1;
            this.mCopyRightColor = -2631721;
            this.mStateTipsColor = -9868951;
            this.mErrorTipsColor = -9803158;
            this.mHelpButtonBg.addState(new int[]{16842919}, getResources().getDrawable(getResources().getIdentifier("bdspeech_help_pressed_light", "drawable", getPackageName())));
            this.mHelpButtonBg.addState(new int[0], getResources().getDrawable(getResources().getIdentifier("bdspeech_help_light", "drawable", getPackageName())));
        }
        this.mBg = getResources().getDrawable(bgName.intValue());
        this.mButtonBg.addState(new int[]{16842919, 16842910}, getResources().getDrawable(buttonPressedBgName.intValue()));
        this.mButtonBg.addState(new int[]{-16842910}, getResources().getDrawable(buttonRecognizingBgName.intValue()));
        this.mButtonBg.addState(new int[0], getResources().getDrawable(buttonNormalBgName.intValue()));
        this.mLeftButtonBg.addState(new int[]{16842919}, getResources().getDrawable(leftButtonPressedBgName.intValue()));
        this.mLeftButtonBg.addState(new int[0], getResources().getDrawable(leftButtonNormalBgName.intValue()));
        this.mRightButtonBg.addState(new int[]{16842919}, getResources().getDrawable(rightButtonPressedBgName.intValue()));
        this.mRightButtonBg.addState(new int[0], getResources().getDrawable(rightButtonNormalBgName.intValue()));
        int[][] states = {new int[]{16842919, 16842910}, new int[]{-16842910}, new int[1]};
        this.mButtonColor = new ColorStateList(states, colors);
        this.mButtonReverseColor = new ColorStateList(states, colors_reverse);
    }

    private void stopRecognizingAnimation() {
        this.mVoiceWaveView.resetAnimation();
    }

    private void startRecognizingAnimation() {
        this.mVoiceWaveView.startRecognizingAnimation();
    }

    /* access modifiers changed from: protected */
    @Override // com.baidu.voicerecognition.android.p007ui.BaiduASRDialog
    public void onRecognitionStart() {
        this.barHandler.removeMessages(1);
        this.barHandler.removeMessages(0);
        this.step = 0;
        this.delayTime = 0;
        this.mInputEdit.setText("");
        this.mInputEdit.setVisibility(4);
        this.mVoiceWaveView.setVisibility(0);
        this.mVoiceWaveView.startInitializingAnimation();
        this.mTipsTextView.setText(getString(KEY_TIPS_STATE_WAIT));
        this.mErrorLayout.setVisibility(4);
        this.mMainLayout.setVisibility(0);
        this.mCompleteTextView.setText(getString(KEY_TIPS_STATE_INITIALIZING));
        this.mCompleteTextView.setEnabled(false);
        this.mTipsTextView.setVisibility(0);
        this.mSDKProgressBar.setVisibility(4);
        this.mWaitNetTextView.setVisibility(4);
        this.mRecognizingView.setVisibility(0);
        this.mHelpView.setVisibility(4);
        if (this.mTipsAdapter.getCount() > 0) {
            this.mHelpBtn.setVisibility(0);
        }
        this.mSuggestionTips.setVisibility(8);
        this.mLogoText1.setVisibility(0);
    }

    /* access modifiers changed from: protected */
    @Override // com.baidu.voicerecognition.android.p007ui.BaiduASRDialog
    public void onPrepared() {
        this.mVoiceWaveView.startPreparingAnimation();
        if (TextUtils.isEmpty(this.mPrompt)) {
            this.mTipsTextView.setText(getString(KEY_TIPS_STATE_READY));
        } else {
            this.mTipsTextView.setText(this.mPrompt);
        }
        this.mCompleteTextView.setText(getString(KEY_BTN_DONE));
        this.mCompleteTextView.setEnabled(true);
        this.mHandler.removeCallbacks(this.mShowSuggestionTip);
        if (getParams().getBoolean(PARAM_SHOW_TIP, true) && this.mTipsAdapter.getCount() > 0) {
            this.mHandler.postDelayed(this.mShowSuggestionTip, this.SHOW_SUGGESTION_INTERVAL);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.baidu.voicerecognition.android.p007ui.BaiduASRDialog
    public void onBeginningOfSpeech() {
        this.mTipsTextView.setText(getString(KEY_TIPS_STATE_LISTENING));
        this.mVoiceWaveView.startRecordingAnimation();
        this.mHandler.removeCallbacks(this.mShowSuggestionTip);
    }

    /* access modifiers changed from: protected */
    @Override // com.baidu.voicerecognition.android.p007ui.BaiduASRDialog
    public void onVolumeChanged(float volume) {
        this.mVoiceWaveView.setCurrentDBLevelMeter(volume);
    }

    /* access modifiers changed from: protected */
    @Override // com.baidu.voicerecognition.android.p007ui.BaiduASRDialog
    public void onEndOfSpeech() {
        this.mTipsTextView.setText(getString(KEY_TIPS_STATE_RECOGNIZING));
        this.mCompleteTextView.setText(getString(KEY_TIPS_STATE_RECOGNIZING));
        this.mSDKProgressBar.setVisibility(0);
        this.barHandler.sendEmptyMessage(0);
        this.mCompleteTextView.setEnabled(false);
        startRecognizingAnimation();
    }

    /* access modifiers changed from: protected */
    @Override // com.baidu.voicerecognition.android.p007ui.BaiduASRDialog
    public void onFinish(int errorType, int errorCode) {
        this.mErrorCode = errorType;
        this.barHandler.removeMessages(0);
        this.barHandler.sendEmptyMessage(1);
        this.mWaitNetTextView.setVisibility(4);
        stopRecognizingAnimation();
        if (errorType != 0) {
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, String.format("onError:errorType %1$d,errorCode %2$d ", Integer.valueOf(errorType), Integer.valueOf(errorCode)));
            }
            this.barHandler.removeMessages(1);
            boolean showHelpBtn = false;
            this.mSuggestionTips2.setVisibility(8);
            switch (errorType) {
                case 1:
                    SpannableString spanString = new SpannableString("网络超时，再试一次");
                    spanString.setSpan(new URLSpan("#") { // from class: com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog.3
                        @Override // android.text.style.URLSpan, android.text.style.ClickableSpan
                        public void onClick(View widget) {
                            BaiduASRDigitalDialog.this.startRecognition(true);
                        }
                    }, 5, 9, 33);
                    this.mErrorRes = spanString;
                    break;
                case 2:
                    if (errorCode != ERROR_NETWORK_UNUSABLE) {
                        this.mErrorRes = getString(KEY_TIPS_ERROR_NETWORK);
                        break;
                    } else {
                        this.mErrorRes = getString(KEY_TIPS_ERROR_NETWORK_UNUSABLE);
                        break;
                    }
                case 3:
                    this.mErrorRes = "启动录音失败";
                    if (this.mTipsAdapter.getCount() > 0) {
                        if (getParams().getBoolean(PARAM_SHOW_HELP_ON_SILENT, true)) {
                            showHelpBtn = true;
                        }
                        if (getParams().getBoolean(PARAM_SHOW_TIP, true)) {
                            this.mSuggestionTips2.setText(this.mPrefix + ((String) this.mTipsAdapter.getItem(this.mRandom.nextInt(this.mTipsAdapter.getCount()))));
                            this.mSuggestionTips2.setVisibility(0);
                            break;
                        }
                    }
                    break;
                case 4:
                    this.mErrorRes = getString(KEY_TIPS_ERROR_DECODER);
                    break;
                case 5:
                    this.mErrorRes = "客户端错误";
                    break;
                case 6:
                    this.mErrorRes = getString(KEY_TIPS_ERROR_SILENT);
                    break;
                case 7:
                    this.mErrorRes = "没有匹配的识别结果";
                    break;
                case 8:
                    this.mErrorRes = "引擎忙";
                    break;
                case 9:
                    this.mErrorRes = "权限不足，请检查设置";
                    break;
                default:
                    this.mErrorRes = getString(KEY_TIPS_ERROR_INTERNAL);
                    break;
            }
            this.mCancelTextView.setText(getString(showHelpBtn ? KEY_BTN_HELP : KEY_BTN_CANCEL));
            this.mWaitNetTextView.setVisibility(4);
            this.mErrorTipsTextView.setMovementMethod(LinkMovementMethod.getInstance());
            this.mErrorTipsTextView.setText(this.mErrorRes);
            this.mErrorLayout.setVisibility(0);
            this.mMainLayout.setVisibility(4);
            this.mHelpBtn.setVisibility(4);
            this.mHandler.removeCallbacks(this.mShowSuggestionTip);
        }
        this.mVoiceWaveView.setVisibility(4);
    }

    /* access modifiers changed from: protected */
    @Override // com.baidu.voicerecognition.android.p007ui.BaiduASRDialog
    public void onPartialResults(Bundle partialResults) {
        ArrayList<String> results;
        if (partialResults != null && (results = partialResults.getStringArrayList("results_recognition")) != null && results.size() > 0) {
            if (this.mInputEdit.getVisibility() != 0) {
                this.mInputEdit.setVisibility(0);
                this.mWaitNetTextView.setVisibility(4);
                this.mTipsTextView.setVisibility(4);
            }
            this.mInputEdit.setText(results.get(0));
            this.mInputEdit.setSelection(this.mInputEdit.getText().length());
            this.delayTime = 0;
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.baidu.voicerecognition.android.p007ui.BaiduASRDialog
    public void onEvent(int eventType, Bundle params) {
        switch (eventType) {
            case 12:
                int type = params.getInt("engine_type");
                this.mEngineType = type;
                showEngineType(type);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void showEngineType(int engineType) {
        switch (engineType) {
            case 0:
                this.mSuggestionTips.setVisibility(8);
                this.mLogoText1.setVisibility(0);
                return;
            case 1:
                this.mSuggestionTips.setText("当前正在使用离线识别引擎");
                this.mSuggestionTips.setVisibility(0);
                this.mLogoText1.setVisibility(8);
                return;
            default:
                return;
        }
    }
}
