package com.xzy.forestSystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import  com.xzy.forestSystem.stub.StubApp;
import  com.xzy.forestSystem.gogisapi.Common.Constant;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AudioRecognition_Dialog extends Activity implements RecognitionListener {
    private static final int EVENT_ERROR = 11;
    private static final int REQUEST_UI = 1;
    public static final int STATUS_None = 0;
    public static final int STATUS_Ready = 3;
    public static final int STATUS_Recognition = 5;
    public static final int STATUS_Speaking = 4;
    public static final int STATUS_WaitingReady = 2;
    private static final String TAG = "Sdk2Api";
    private Button btn;
    private Button setting;
    private long speechEndTime = -1;
    private SpeechRecognizer speechRecognizer;
    private int status = 0;
    private TextView txtLog;
    private TextView txtResult;

    static {
        StubApp.interface11(1165);
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public native void onCreate(Bundle bundle);

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onDestroy() {
        this.speechRecognizer.destroy();
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            onResults(data.getExtras());
        }
    }

    public void bindParams(Intent intent) {
        PreferenceManager.getDefaultSharedPreferences(this);
        intent.putExtra(Constant.EXTRA_OFFLINE_ASR_BASE_FILE_PATH, "/sdcard/easr/s_1");
        intent.putExtra(Constant.EXTRA_LICENSE_FILE_PATH, "/sdcard/easr/license-tmp-20150530.txt");
        intent.putExtra(Constant.EXTRA_OFFLINE_SLOT_DATA, buildTestSlotData());
    }

    private String buildTestSlotData() {
        JSONObject slotData = new JSONObject();
        JSONArray name = new JSONArray().put("��ӿȪ").put("������");
        JSONArray song = new JSONArray().put("������").put("����ѩ");
        JSONArray artist = new JSONArray().put("�ܽ���").put("������");
        JSONArray app = new JSONArray().put("�ֻ��ٶ�").put("�ٶȵ�ͼ");
        JSONArray usercommand = new JSONArray().put("�ص�").put("����");
        try {
            slotData.put("name", name);
            slotData.put("song", song);
            slotData.put("artist", artist);
            slotData.put("app", app);
            slotData.put("usercommand", usercommand);
        } catch (JSONException e) {
        }
        return slotData.toString();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void start() {
        this.txtLog.setText("");
        print("����ˡ���ʼ��");
        Intent intent = new Intent();
        bindParams(intent);
        intent.setAction("com.baidu.action.RECOGNIZE_SPEECH");
        startActivityForResult(intent, 1);
        this.txtResult.setText("");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void stop() {
        this.speechRecognizer.stopListening();
        print("����ˡ�˵���ˡ�");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void cancel() {
        this.speechRecognizer.cancel();
        this.status = 0;
        print("����ˡ�ȡ����");
    }

    @Override // android.speech.RecognitionListener
    public void onReadyForSpeech(Bundle params) {
        this.status = 3;
        print("׼�����������Կ�ʼ˵��");
    }

    @Override // android.speech.RecognitionListener
    public void onBeginningOfSpeech() {
        this.status = 4;
        this.btn.setText("˵����");
        print("��⵽�û����Ѿ���ʼ˵��");
    }

    @Override // android.speech.RecognitionListener
    public void onRmsChanged(float rmsdB) {
    }

    @Override // android.speech.RecognitionListener
    public void onBufferReceived(byte[] buffer) {
    }

    @Override // android.speech.RecognitionListener
    public void onEndOfSpeech() {
        this.speechEndTime = System.currentTimeMillis();
        this.status = 5;
        print("��⵽�û����Ѿ�ֹͣ˵��");
        this.btn.setText("ʶ����");
    }

    @Override // android.speech.RecognitionListener
    public void onError(int error) {
        this.status = 0;
        StringBuilder sb = new StringBuilder();
        switch (error) {
            case 1:
                sb.append("���ӳ�ʱ");
                break;
            case 2:
                sb.append("��������");
                break;
            case 3:
                sb.append("��Ƶ����");
                break;
            case 4:
                sb.append("����˴���");
                break;
            case 5:
                sb.append("�����ͻ��˴���");
                break;
            case 6:
                sb.append("û����������");
                break;
            case 7:
                sb.append("û��ƥ���ʶ����");
                break;
            case 8:
                sb.append("����æ");
                break;
            case 9:
                sb.append("Ȩ�޲���");
                break;
        }
        sb.append(":" + error);
        print("ʶ��ʧ�ܣ�" + sb.toString());
        this.btn.setText("��ʼ");
    }

    @Override // android.speech.RecognitionListener
    public void onResults(Bundle results) {
        long end2finish = System.currentTimeMillis() - this.speechEndTime;
        this.status = 0;
        ArrayList<String> nbest = results.getStringArrayList("results_recognition");
        print("ʶ��ɹ���" + Arrays.toString(nbest.toArray(new String[nbest.size()])));
        String json_res = results.getString("origin_result");
        try {
            print("origin_result=\n" + new JSONObject(json_res).toString(4));
        } catch (Exception e) {
            print("origin_result=[warning: bad json]\n" + json_res);
        }
        this.btn.setText("��ʼ");
        String strEnd2Finish = "";
        if (end2finish < 60000) {
            strEnd2Finish = "(waited " + end2finish + "ms)";
        }
        this.txtResult.setText(String.valueOf(nbest.get(0)) + strEnd2Finish);
    }

    @Override // android.speech.RecognitionListener
    public void onPartialResults(Bundle partialResults) {
        ArrayList<String> nbest = partialResults.getStringArrayList("results_recognition");
        if (nbest.size() > 0) {
            print("~��ʱʶ������" + Arrays.toString(nbest.toArray(new String[0])));
            this.txtResult.setText(nbest.get(0));
        }
    }

    /* JADX DEBUG: TODO: convert one arg to string using `String.valueOf()`, args: [(wrap: java.lang.Object : 0x000b: INVOKE  (r3v2 java.lang.Object) = (r6v0 'params' android.os.Bundle A[D('params' android.os.Bundle)]), ("reason") type: VIRTUAL call: android.os.Bundle.get(java.lang.String):java.lang.Object)] */
    @Override // android.speech.RecognitionListener
    public void onEvent(int eventType, Bundle params) {
        switch (eventType) {
            case 11:
                print("EVENT_ERROR, " + new StringBuilder().append(params.get("reason")).toString());
                return;
            case 12:
                print("*�����л���" + (params.getInt("engine_type") == 0 ? "����" : "����"));
                return;
            default:
                return;
        }
    }

    private void print(String msg) {
        this.txtLog.append(String.valueOf(msg) + "\n");
        ((ScrollView) this.txtLog.getParent()).smoothScrollTo(0, 1000000);
        Log.d(TAG, "----" + msg);
    }
}
