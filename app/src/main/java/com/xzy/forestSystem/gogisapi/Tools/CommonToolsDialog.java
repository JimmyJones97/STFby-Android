package  com.xzy.forestSystem.gogisapi.Tools;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xzy.forestSystem.baidu.speech.VoiceRecognitionService;
import com.xzy.forestSystem.GuiZhou.DiaoCha.CalXuJiSingle_Dialog2;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.Constant;
import  com.xzy.forestSystem.gogisapi.Common.ECopyPasteType;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.GPS.AddGPSPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Setting.FormSizeSetting_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class CommonToolsDialog extends DialogFragment implements RecognitionListener {
    private static final int EVENT_ERROR = 11;
    private static final int REQUEST_UI = 1;
    public static final int STATUS_None = 0;
    public static final int STATUS_Ready = 3;
    public static final int STATUS_Recognition = 5;
    public static final int STATUS_Speaking = 4;
    public static final int STATUS_WaitingReady = 2;
    private View.OnClickListener TipItemClickListener = new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CommonToolsDialog.2
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            try {
                Object tempObj = view.getTag();
                if (tempObj != null) {
                    String tmpCommand = String.valueOf(tempObj);
                    if (tmpCommand.equals("关闭")) {
                        CommonToolsDialog.this._conentView.setVisibility(8);
                    } else if (tmpCommand.equals("语音识别")) {
                        CommonToolsDialog.this._CurrentToolsIndex = 1;
                        if (CommonToolsDialog.this.speechRecognizer == null) {
                            CommonToolsDialog.this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(CommonToolsDialog.this._context, new ComponentName(CommonToolsDialog.this._context, VoiceRecognitionService.class));
                            CommonToolsDialog.this.speechRecognizer.setRecognitionListener(CommonToolsDialog.this);
                        }
                        if (PreferenceManager.getDefaultSharedPreferences(CommonToolsDialog.this._context).getBoolean("api", false)) {
                            switch (CommonToolsDialog.this.status) {
                                case 0:
                                    CommonToolsDialog.this.startVoiceSpeech();
                                    CommonToolsDialog.this.status = 2;
                                    return;
                                case 1:
                                default:
                                    return;
                                case 2:
                                    CommonToolsDialog.this.cancelVoiceSpeech();
                                    CommonToolsDialog.this.status = 0;
                                    return;
                                case 3:
                                    CommonToolsDialog.this.cancelVoiceSpeech();
                                    CommonToolsDialog.this.status = 0;
                                    return;
                                case 4:
                                    CommonToolsDialog.this.stopVoiceSpeech();
                                    CommonToolsDialog.this.status = 5;
                                    return;
                                case 5:
                                    CommonToolsDialog.this.cancelVoiceSpeech();
                                    CommonToolsDialog.this.status = 0;
                                    return;
                            }
                        } else {
                            CommonToolsDialog.this.startVoiceSpeech();
                        }
                    } else if (tmpCommand.equals("计算器")) {
                        CommonToolsDialog.this._CurrentToolsIndex = 2;
                        CalculateDialog tmpDialog = new CalculateDialog();
                        tmpDialog.SetCallback(CommonToolsDialog.this.pCallback);
                        tmpDialog.ShowDialog();
                    } else if (tmpCommand.equals("测树高")) {
                        CommonToolsDialog.this._CurrentToolsIndex = 3;
                        MeasureHeightDialog tmpDialog2 = new MeasureHeightDialog();
                        tmpDialog2.SetCallback(CommonToolsDialog.this.pCallback);
                        tmpDialog2.ShowDialog();
                    } else if (tmpCommand.equals("摄像测距")) {
                        CommonToolsDialog.this._CurrentToolsIndex = 4;
                        MeasureDistanceDialog tmpDialog3 = new MeasureDistanceDialog();
                        tmpDialog3.SetCallback(CommonToolsDialog.this.pCallback);
                        tmpDialog3.ShowDialog();
                    } else if (tmpCommand.equals("蓄积量计算")) {
                        CommonToolsDialog.this._CurrentToolsIndex = 5;
                        CalXuJiSingle_Dialog2 tmpDialog4 = new CalXuJiSingle_Dialog2();
                        tmpDialog4.SetCallback(CommonToolsDialog.this.pCallback);
                        tmpDialog4.ShowDialog();
                    } else if (tmpCommand.equals("复制属性")) {
                        CommonToolsDialog.this._CurrentToolsIndex = 6;
                        if (CommonToolsDialog.this._MainCallback != null) {
                            CommonToolsDialog.this._MainCallback.OnClick("复制属性工具", null);
                        }
                        CommonToolsDialog.this.dismiss();
                    } else if (tmpCommand.equals("粘贴属性")) {
                        CommonToolsDialog.this._CurrentToolsIndex = 7;
                        if (CommonToolsDialog.this._MainCallback != null) {
                            CommonToolsDialog.this._MainCallback.OnClick("粘贴属性工具", null);
                        }
                        CommonToolsDialog.this.dismiss();
                    } else if (tmpCommand.equals("设置")) {
                        FormSizeSetting_Dialog tmpDialog5 = new FormSizeSetting_Dialog();
                        tmpDialog5.Set_FormType(0);
                        tmpDialog5.SetCallback(CommonToolsDialog.this.pCallback);
                        tmpDialog5.ShowDialog();
                    } else if (tmpCommand.equals("添加GPS采样点")) {
                        AddGPSPoint_Dialog tempDialog = new AddGPSPoint_Dialog();
                        tempDialog.SetCallback(CommonToolsDialog.this.pCallback);
                        tempDialog.ShowDialog();
                    } else {
                        Common.ShowDialog("对不起,该功能正在完善中!感谢您的关注!");
                    }
                }
            } catch (Exception e) {
            }
        }
    };
    private int _CurrentToolsIndex = -1;
    private boolean _IsInitial = false;
    private ICallback _MainCallback = null;
    private List<String> _OutMsgList = new ArrayList();
    private View _ParentView = null;
    private TextView _TipTextView = null;
    private View _conentView;
    private Context _context;
    private Object _relateTag = null;
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CommonToolsDialog.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String command, Object object) {
            if (command.equals("确定")) {
                return;
            }
            if (command.equals("语音识别返回") || command.equals("计算器返回") || command.equals("测量高度返回") || command.equals("测量距离返回") || command.equals("蓄积量计算返回")) {
                if (CommonToolsDialog.this._MainCallback != null) {
                    CommonToolsDialog.this._MainCallback.OnClick("常用工具结果返回", new Object[]{CommonToolsDialog.this._relateTag, object});
                }
                CommonToolsDialog.this.dismiss();
            } else if (command.equals("添加GPS采样点返回")) {
                Coordinate tempCoord = (Coordinate) object;
                if (tempCoord != null) {
                    String tmpString = String.valueOf(String.valueOf((long) tempCoord.getX())) + "," + String.valueOf((long) tempCoord.getY());
                    if (CommonToolsDialog.this._MainCallback != null) {
                        CommonToolsDialog.this._MainCallback.OnClick("常用工具结果返回", new Object[]{CommonToolsDialog.this._relateTag, tmpString});
                    }
                }
                CommonToolsDialog.this.dismiss();
            }
        }
    };
    private long speechEndTime = -1;
    private SpeechRecognizer speechRecognizer;
    private int status = 0;

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.commontools_dialog, container, false);
        getDialog().requestWindowFeature(1);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this._context = getDialog().getContext();
        loadTools(view);
        return view;
    }

    private void loadTools(View mainView) {
        try {
            LinearLayout tmpMainLayout = (LinearLayout) mainView.findViewById(R.id.ll_toolsLayout);
            Button tmpTool01 = new Button(this._context);
            Drawable tmpBmp01 = getResources().getDrawable(R.drawable.gps348);
            tmpBmp01.setBounds(0, 0, tmpBmp01.getMinimumWidth(), tmpBmp01.getMinimumHeight());
            tmpTool01.setCompoundDrawables(null, tmpBmp01, null, null);
            tmpTool01.setBackgroundResource(R.drawable.toolbar_btn_selector);
            tmpTool01.setText("GPS");
            tmpTool01.setTag("添加GPS采样点");
            tmpTool01.setOnClickListener(this.TipItemClickListener);
            tmpMainLayout.addView(tmpTool01);
            LinearLayout.LayoutParams tmplayoutParams01 = (LinearLayout.LayoutParams) tmpTool01.getLayoutParams();
            tmplayoutParams01.setMargins(10, 10, 10, 10);
            tmpTool01.setLayoutParams(tmplayoutParams01);
            Button tmpTool012 = new Button(this._context);
            Drawable tmpBmp012 = getResources().getDrawable(R.drawable.copy64);
            tmpBmp012.setBounds(0, 0, tmpBmp012.getMinimumWidth(), tmpBmp012.getMinimumHeight());
            tmpTool012.setCompoundDrawables(null, tmpBmp012, null, null);
            tmpTool012.setBackgroundResource(R.drawable.toolbar_btn_selector);
            tmpTool012.setText("复制属性");
            tmpTool012.setTag("复制属性");
            tmpTool012.setOnClickListener(this.TipItemClickListener);
            tmpMainLayout.addView(tmpTool012);
            LinearLayout.LayoutParams tmplayoutParams012 = (LinearLayout.LayoutParams) tmpTool012.getLayoutParams();
            tmplayoutParams012.setMargins(10, 10, 10, 10);
            tmpTool012.setLayoutParams(tmplayoutParams012);
            if (PubVar.SystemCopyObject != null && PubVar.SystemCopyObject.getECopyPasteType() == ECopyPasteType.Attribute) {
                Button tmpTool013 = new Button(this._context);
                Drawable tmpBmp013 = getResources().getDrawable(R.drawable.paste64);
                tmpBmp013.setBounds(0, 0, tmpBmp013.getMinimumWidth(), tmpBmp013.getMinimumHeight());
                tmpTool013.setCompoundDrawables(null, tmpBmp013, null, null);
                tmpTool013.setBackgroundResource(R.drawable.toolbar_btn_selector);
                tmpTool013.setText("粘贴属性");
                tmpTool013.setTag("粘贴属性");
                tmpTool013.setOnClickListener(this.TipItemClickListener);
                tmpMainLayout.addView(tmpTool013);
                LinearLayout.LayoutParams tmplayoutParams013 = (LinearLayout.LayoutParams) tmpTool013.getLayoutParams();
                tmplayoutParams013.setMargins(10, 10, 10, 10);
                tmpTool013.setLayoutParams(tmplayoutParams013);
            }
            Button tmpTool014 = new Button(this._context);
            Drawable tmpBmp014 = getResources().getDrawable(R.drawable.voice0164);
            tmpBmp014.setBounds(0, 0, tmpBmp014.getMinimumWidth(), tmpBmp014.getMinimumHeight());
            tmpTool014.setCompoundDrawables(null, tmpBmp014, null, null);
            tmpTool014.setBackgroundResource(R.drawable.toolbar_btn_selector);
            tmpTool014.setText("语音识别");
            tmpTool014.setTag("语音识别");
            tmpTool014.setOnClickListener(this.TipItemClickListener);
            tmpMainLayout.addView(tmpTool014);
            LinearLayout.LayoutParams tmplayoutParams014 = (LinearLayout.LayoutParams) tmpTool014.getLayoutParams();
            tmplayoutParams014.setMargins(10, 10, 10, 10);
            tmpTool014.setLayoutParams(tmplayoutParams014);
            Button tmpTool015 = new Button(this._context);
            Drawable tmpBmp015 = getResources().getDrawable(R.drawable.calculator1464);
            tmpBmp015.setBounds(0, 0, tmpBmp015.getMinimumWidth(), tmpBmp015.getMinimumHeight());
            tmpTool015.setCompoundDrawables(null, tmpBmp015, null, null);
            tmpTool015.setBackgroundResource(R.drawable.toolbar_btn_selector);
            tmpTool015.setText("计算器");
            tmpTool015.setTag("计算器");
            tmpTool015.setOnClickListener(this.TipItemClickListener);
            tmpMainLayout.addView(tmpTool015);
            LinearLayout.LayoutParams tmplayoutParams015 = (LinearLayout.LayoutParams) tmpTool015.getLayoutParams();
            tmplayoutParams015.setMargins(10, 10, 10, 10);
            tmpTool015.setLayoutParams(tmplayoutParams015);
            Button tmpTool = new Button(this._context);
            Drawable tmpBmp = getResources().getDrawable(R.drawable.tree0864);
            tmpBmp.setBounds(0, 0, tmpBmp.getMinimumWidth(), tmpBmp.getMinimumHeight());
            tmpTool.setCompoundDrawables(null, tmpBmp, null, null);
            tmpTool.setBackgroundResource(R.drawable.toolbar_btn_selector);
            tmpTool.setText("测树高");
            tmpTool.setTag("测树高");
            tmpTool.setOnClickListener(this.TipItemClickListener);
            tmpMainLayout.addView(tmpTool);
            LinearLayout.LayoutParams tmplayoutParams016 = (LinearLayout.LayoutParams) tmpTool.getLayoutParams();
            tmplayoutParams016.setMargins(10, 10, 10, 10);
            tmpTool.setLayoutParams(tmplayoutParams016);
            Button tmpTool2 = new Button(this._context);
            Drawable tmpBmp2 = getResources().getDrawable(R.drawable.caldistance0164);
            tmpBmp2.setBounds(0, 0, tmpBmp2.getMinimumWidth(), tmpBmp2.getMinimumHeight());
            tmpTool2.setCompoundDrawables(null, tmpBmp2, null, null);
            tmpTool2.setBackgroundResource(R.drawable.toolbar_btn_selector);
            tmpTool2.setText("摄像测距");
            tmpTool2.setTag("摄像测距");
            tmpTool2.setOnClickListener(this.TipItemClickListener);
            tmpMainLayout.addView(tmpTool2);
            LinearLayout.LayoutParams tmplayoutParams017 = (LinearLayout.LayoutParams) tmpTool2.getLayoutParams();
            tmplayoutParams017.setMargins(10, 10, 10, 10);
            tmpTool2.setLayoutParams(tmplayoutParams017);
            Button tmpTool3 = new Button(this._context);
            Drawable tmpBmp3 = getResources().getDrawable(R.drawable.tree0264);
            tmpBmp3.setBounds(0, 0, tmpBmp3.getMinimumWidth(), tmpBmp3.getMinimumHeight());
            tmpTool3.setCompoundDrawables(null, tmpBmp3, null, null);
            tmpTool3.setBackgroundResource(R.drawable.toolbar_btn_selector);
            tmpTool3.setText("蓄积量计算");
            tmpTool3.setTag("蓄积量计算");
            tmpTool3.setOnClickListener(this.TipItemClickListener);
            tmpMainLayout.addView(tmpTool3);
            LinearLayout.LayoutParams tmplayoutParams018 = (LinearLayout.LayoutParams) tmpTool3.getLayoutParams();
            tmplayoutParams018.setMargins(10, 10, 10, 10);
            tmpTool3.setLayoutParams(tmplayoutParams018);
        } catch (Exception e) {
        }
    }

    public void SetCallback(ICallback callback) {
        this._MainCallback = callback;
    }

    public void SetRelateTag(Object object) {
        this._relateTag = object;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void startVoiceSpeech() {
        try {
            Intent intent = new Intent();
            bindVoiceSpeechParams(intent);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this._context);
            String args = sp.getString("args", "");
            if (args != null) {
                intent.putExtra("args", args);
            }
            if (sp.getBoolean("api", false)) {
                this.speechEndTime = -1;
                this.speechRecognizer.startListening(intent);
                return;
            }
            intent.setAction("com.baidu.action.RECOGNIZE_SPEECH");
            startActivityForResult(intent, 1);
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void stopVoiceSpeech() {
        this.speechRecognizer.stopListening();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void cancelVoiceSpeech() {
        this.speechRecognizer.cancel();
        this.status = 0;
    }

    public void bindVoiceSpeechParams(Intent intent) {
        intent.putExtra(Constant.EXTRA_OFFLINE_ASR_BASE_FILE_PATH, "/sdcard/easr/s_1");
        intent.putExtra(Constant.EXTRA_LICENSE_FILE_PATH, "/sdcard/easr/license-tmp-20150530.txt");
        intent.putExtra(Constant.EXTRA_OFFLINE_SLOT_DATA, buildTestSlotData());
    }

    private String buildTestSlotData() {
        return new JSONObject().toString();
    }

    @Override // android.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            onResults(data.getExtras());
        }
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        if (this.speechRecognizer != null) {
            this.speechRecognizer.destroy();
        }
        if (this._MainCallback != null) {
            this._MainCallback.OnClick("常用快捷工具返回", null);
        }
        super.onDestroy();
    }

    @Override // android.speech.RecognitionListener
    public void onBeginningOfSpeech() {
        this.status = 4;
    }

    @Override // android.speech.RecognitionListener
    public void onBufferReceived(byte[] arg0) {
    }

    @Override // android.speech.RecognitionListener
    public void onEndOfSpeech() {
        this.speechEndTime = System.currentTimeMillis();
        this.status = 5;
    }

    @Override // android.speech.RecognitionListener
    public void onError(int error) {
        this.status = 0;
        StringBuilder sb = new StringBuilder();
        switch (error) {
            case 1:
                sb.append("连接超时");
                break;
            case 2:
                sb.append("网络问题");
                break;
            case 3:
                sb.append("音频问题");
                break;
            case 4:
                sb.append("服务端错误");
                break;
            case 5:
                sb.append("其它客户端错误");
                break;
            case 6:
                sb.append("没有语音输入");
                break;
            case 7:
                sb.append("没有匹配的识别结果");
                break;
            case 8:
                sb.append("引擎忙");
                break;
            case 9:
                sb.append("权限不足");
                break;
        }
        sb.append(":" + error);
        Common.ShowToast("识别失败：" + sb.toString());
    }

    /* JADX DEBUG: TODO: convert one arg to string using `String.valueOf()`, args: [(wrap: java.lang.Object : 0x000b: INVOKE  (r3v2 java.lang.Object) = (r6v0 'params' android.os.Bundle A[D('params' android.os.Bundle)]), ("reason") type: VIRTUAL call: android.os.Bundle.get(java.lang.String):java.lang.Object)] */
    @Override // android.speech.RecognitionListener
    public void onEvent(int eventType, Bundle params) {
        switch (eventType) {
            case 11:
                Common.ShowToast("识别错误:" + new StringBuilder().append(params.get("reason")).toString());
                return;
            case 12:
                Common.ShowToast("*引擎切换至" + (params.getInt("engine_type") == 0 ? "在线" : "离线"));
                return;
            default:
                return;
        }
    }

    @Override // android.speech.RecognitionListener
    public void onPartialResults(Bundle partialResults) {
    }

    @Override // android.speech.RecognitionListener
    public void onReadyForSpeech(Bundle arg0) {
        this.status = 3;
    }

    @Override // android.speech.RecognitionListener
    public void onResults(Bundle results) {
        this.status = 0;
        try {
            this.pCallback.OnClick("语音识别返回", results.getStringArrayList("results_recognition").get(0));
        } catch (Exception e) {
        }
    }

    @Override // android.speech.RecognitionListener
    public void onRmsChanged(float arg0) {
    }
}
