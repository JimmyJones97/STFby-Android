package  com.xzy.forestSystem.gogisapi.Tools;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.SeekBar;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;

public class GraphicViewSetting_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private SeekBar m_SeekBar_B;
    private SeekBar m_SeekBar_G;
    private SeekBar m_SeekBar_R;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public GraphicViewSetting_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.GraphicViewSetting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("确定")) {
                    GraphicViewSetting_Dialog.this._Dialog.dismiss();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.graphicviewsetting_dialog);
        this._Dialog.Resize(0.7f, 0.96f);
        this._Dialog.SetCaption("");
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.btn_GraphicViewSetting_Cancel).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.GraphicViewSetting_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                PubVar._MapView.clearColorFilter();
                GraphicViewSetting_Dialog.this._Dialog.dismiss();
            }
        });
        this.m_SeekBar_R = (SeekBar) this._Dialog.findViewById(R.id.seekBarValue_R);
        this.m_SeekBar_R.setMax(255);
        this.m_SeekBar_R.setProgress(255);
        this.m_SeekBar_R.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.GraphicViewSetting_Dialog.3
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                if (arg2) {
                    GraphicViewSetting_Dialog.this.ProcessView();
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar arg0) {
            }
        });
        this.m_SeekBar_G = (SeekBar) this._Dialog.findViewById(R.id.seekBarValue_G);
        this.m_SeekBar_G.setMax(255);
        this.m_SeekBar_G.setProgress(255);
        this.m_SeekBar_G.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.GraphicViewSetting_Dialog.4
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                if (arg2) {
                    GraphicViewSetting_Dialog.this.ProcessView();
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar arg0) {
            }
        });
        this.m_SeekBar_B = (SeekBar) this._Dialog.findViewById(R.id.seekBarValue_B);
        this.m_SeekBar_B.setMax(255);
        this.m_SeekBar_B.setProgress(255);
        this.m_SeekBar_B.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.GraphicViewSetting_Dialog.5
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                if (arg2) {
                    GraphicViewSetting_Dialog.this.ProcessView();
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar arg0) {
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void ProcessView() {
        PubVar._MapView.setColorFilter(Color.rgb(this.m_SeekBar_R.getProgress(), this.m_SeekBar_G.getProgress(), this.m_SeekBar_B.getProgress()), PorterDuff.Mode.DARKEN);
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.GraphicViewSetting_Dialog.6
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
            }
        });
        this._Dialog.show();
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                view.getTag().toString();
            }
        }
    }
}
