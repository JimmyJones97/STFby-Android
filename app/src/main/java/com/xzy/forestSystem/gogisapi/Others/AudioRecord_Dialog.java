package  com.xzy.forestSystem.gogisapi.Others;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;

public class AudioRecord_Dialog extends Dialog {
    private Context _Context = null;
    private String _FileNamePre = "File";
    private String _SaveFolder = "";
    private ICallback _returnCallback = null;
    private File audioFile = null;
    private Button buttonRecord;
    private ImageView imageView;
    private TextView infoTextView;
    private MediaRecorder myMediaRecorder;
    private Runnable myTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Others.AudioRecord_Dialog.1
        @Override // java.lang.Runnable
        public void run() {
            if (AudioRecord_Dialog.this.tempImageIndex == 0) {
                AudioRecord_Dialog.this.imageView.setImageResource(R.drawable.recorder_start48);
                AudioRecord_Dialog.this.tempImageIndex = 1;
            } else {
                AudioRecord_Dialog.this.imageView.setImageResource(R.drawable.recorder_stop48);
                AudioRecord_Dialog.this.tempImageIndex = 0;
            }
            int tempTime = AudioRecord_Dialog.this.recordTime;
            int tempS = tempTime % 60;
            int tempTime2 = tempTime / 60;
            DecimalFormat df1 = new DecimalFormat("00");
            AudioRecord_Dialog.this.timeTextView.setText(String.valueOf(df1.format((long) ((tempTime2 / 60) % 60))) + ":" + df1.format((long) (tempTime2 % 60)) + ":" + df1.format((long) tempS));
            AudioRecord_Dialog.this.myhandler.postDelayed(AudioRecord_Dialog.this.myTask, 1000);
            AudioRecord_Dialog.this.recordTime++;
        }
    };
    private Handler myhandler = new Handler();
    int recordTime = 0;
    int tempImageIndex = 0;
    private TextView timeTextView;
    private long tokenDate = 0;

    public AudioRecord_Dialog(Context paramContext) {
        super(paramContext);
        this._Context = paramContext;
        requestWindowFeature(1);
        getWindow().setSoftInputMode(3);
        setCancelable(false);
        setContentView(R.layout.x_audiorecord_dialog);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = -1;
        layoutParams.height = -2;
        window.setAttributes(layoutParams);
        this.infoTextView = (TextView) findViewById(R.id.textViewInfo);
        this.timeTextView = (TextView) findViewById(R.id.textViewRecordTime);
        this.imageView = (ImageView) findViewById(R.id.imageViewRecord);
        this._SaveFolder = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/Photo";
        ((Button) findViewById(R.id.buttonExit)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Others.AudioRecord_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (AudioRecord_Dialog.this.recordTime > 0) {
                    Common.ShowYesNoDialog(AudioRecord_Dialog.this._Context, "正在录音,是否取消录音退出?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.AudioRecord_Dialog.2.1
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (paramString.equals("YES")) {
                                AudioRecord_Dialog.this.stopRecorder();
                                if (AudioRecord_Dialog.this.audioFile != null) {
                                    AudioRecord_Dialog.this.audioFile.delete();
                                }
                                AudioRecord_Dialog.this.dismiss();
                            }
                        }
                    });
                } else {
                    AudioRecord_Dialog.this.dismiss();
                }
            }
        });
        this.buttonRecord = (Button) findViewById(R.id.buttonOK);
        this.buttonRecord.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Others.AudioRecord_Dialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (AudioRecord_Dialog.this.buttonRecord.getTag().toString().equals("开始录音")) {
                    AudioRecord_Dialog.this.buttonRecord.setText("停止录音");
                    AudioRecord_Dialog.this.buttonRecord.setTag("停止录音");
                    AudioRecord_Dialog.this.infoTextView.setText("正在录音...");
                    AudioRecord_Dialog.this.startRecorder();
                    return;
                }
                AudioRecord_Dialog.this.buttonRecord.setTag("开始录音");
                AudioRecord_Dialog.this.stopRecorder();
                if (!(AudioRecord_Dialog.this.audioFile == null || AudioRecord_Dialog.this._returnCallback == null)) {
                    AudioRecord_Dialog.this._returnCallback.OnClick("Image", new Object[]{AudioRecord_Dialog.this.audioFile.getAbsolutePath(), Long.valueOf(AudioRecord_Dialog.this.tokenDate)});
                }
                AudioRecord_Dialog.this.dismiss();
            }
        });
    }

    public void SetCallback(ICallback tmpICallback) {
        this._returnCallback = tmpICallback;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void startRecorder() {
        this.tempImageIndex = 0;
        this.recordTime = 0;
        this.tokenDate = new Date().getTime();
        try {
            this.audioFile = new File(String.valueOf(this._SaveFolder) + FileSelector_Dialog.sRoot + this._FileNamePre + Common.fileDateFormat.format(new Date()) + ".arm");
            this.myMediaRecorder = new MediaRecorder();
            this.myMediaRecorder.setAudioSource(1);
            this.myMediaRecorder.setOutputFormat(0);
            this.myMediaRecorder.setAudioEncoder(0);
            this.myMediaRecorder.setOutputFile(this.audioFile.getAbsolutePath());
            this.myMediaRecorder.prepare();
            this.myMediaRecorder.start();
            this.myhandler.post(this.myTask);
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void stopRecorder() {
        if (this.myMediaRecorder != null) {
            this.myMediaRecorder.stop();
            this.myMediaRecorder.release();
            this.myMediaRecorder = null;
        }
    }

    /* access modifiers changed from: protected */
    @Override // java.lang.Object
    public void finalize() {
        try {
            stopRecorder();
            super.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void setFileNamePreString(String fileNamePreString) {
        this._FileNamePre = fileNamePreString;
    }
}
