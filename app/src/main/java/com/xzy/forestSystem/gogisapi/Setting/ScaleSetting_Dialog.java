package  com.xzy.forestSystem.gogisapi.Setting;

import android.content.DialogInterface;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;

public class ScaleSetting_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public ScaleSetting_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Setting.ScaleSetting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("确定")) {
                        if (((RadioButton) ScaleSetting_Dialog.this._Dialog.findViewById(R.id.radioButton1)).isChecked()) {
                            PubVar.ScaleBar_ShowType = 0;
                        } else {
                            PubVar.ScaleBar_ShowType = 1;
                        }
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_Map_ScaleBar_ShowType", Integer.valueOf(PubVar.ScaleBar_ShowType));
                        boolean tempBool = true;
                        String tempStr = Common.GetEditTextValueOnID(ScaleSetting_Dialog.this._Dialog, R.id.et_inputScaleValue);
                        if (tempStr != null && !tempStr.trim().equals("")) {
                            PubVar._Map.setActualScale(Integer.parseInt(tempStr));
                            PubVar._Map.Refresh();
                            tempBool = false;
                        }
                        if (ScaleSetting_Dialog.this.m_Callback != null) {
                            ScaleSetting_Dialog.this.m_Callback.OnClick("设置比例尺返回", null);
                        }
                        if (tempBool) {
                            PubVar._PubCommand.m_ScaleBar.RefreshScalBar(PubVar._Map);
                            PubVar._PubCommand.m_ScaleBar.Refresh();
                        }
                        ScaleSetting_Dialog.this._Dialog.dismiss();
                    } else if (command.equals("隐藏")) {
                        PubVar.ScaleBar_Visible = false;
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_ScaleBar_Visible", Boolean.valueOf(PubVar.ScaleBar_Visible));
                        PubVar.m_Callback.OnClick("更新比例尺显示", null);
                        ScaleSetting_Dialog.this._Dialog.dismiss();
                    } else if (command.equals("OnItemSelected") && object != null && !object.toString().equals("")) {
                        String tmpStr = String.valueOf(object);
                        if (tmpStr.contains(":")) {
                            tmpStr = tmpStr.substring(tmpStr.lastIndexOf(":") + 1);
                        }
                        Common.SetEditTextValueOnID(ScaleSetting_Dialog.this._Dialog, R.id.et_inputScaleValue, String.valueOf(Integer.parseInt(tmpStr)));
                        ScaleSetting_Dialog.this.pCallback.OnClick("确定", null);
                    }
                } catch (Exception e) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.scalebarsetting_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("比例尺设置");
        this._Dialog.SetHeightWrapContent();
        this._Dialog.setCanceledOnTouchOutside(true);
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.btn_scale_hide).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Setting.ScaleSetting_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                ScaleSetting_Dialog.this.pCallback.OnClick("隐藏", null);
            }
        });
        List<String> tmpArray = new ArrayList<>();
        tmpArray.add("");
        tmpArray.add("1:1");
        tmpArray.add("1:100");
        tmpArray.add("1:500");
        tmpArray.add("1:1000");
        tmpArray.add("1:2000");
        tmpArray.add("1:5000");
        tmpArray.add("1:10000");
        tmpArray.add("1:20000");
        tmpArray.add("1:50000");
        tmpArray.add("1:100000");
        tmpArray.add("1:200000");
        tmpArray.add("1:500000");
        tmpArray.add("1:1000000");
        tmpArray.add("1:2000000");
        tmpArray.add("1:5000000");
        tmpArray.add("1:10000000");
        tmpArray.add("1:20000000");
        tmpArray.add("1:50000000");
        Common.SetSpinnerListData(this._Dialog, "选择快速比例尺", tmpArray, (int) R.id.sp_selectScale, this.pCallback);
        ((SeekBar) this._Dialog.findViewById(R.id.sb_scaleValue)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.Setting.ScaleSetting_Dialog.3
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                String tmpStr = Common.GetEditTextValueOnID(ScaleSetting_Dialog.this._Dialog, R.id.et_inputScaleValue);
                int tmpInt = arg1;
                if (tmpStr != null && !tmpStr.equals("")) {
                    if (tmpInt >= 100) {
                        int tmpInt02 = Integer.parseInt(tmpStr);
                        tmpInt = (((tmpInt - 50) * (tmpInt02 / 10)) / 10) + tmpInt02;
                    } else {
                        tmpInt = arg1;
                    }
                }
                Common.SetEditTextValueOnID(ScaleSetting_Dialog.this._Dialog, R.id.et_inputScaleValue, String.valueOf(tmpInt));
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
    private void refreshLayout() {
        if (PubVar.ScaleBar_ShowType == 0) {
            ((RadioButton) this._Dialog.findViewById(R.id.radioButton1)).setChecked(true);
        } else {
            ((RadioButton) this._Dialog.findViewById(R.id.radioButton2)).setChecked(true);
        }
        int tempScale = PubVar._Map.getActualScale();
        if (tempScale < 1) {
            tempScale = 1;
        }
        ((TextView) this._Dialog.findViewById(R.id.tv_currentScaleRatio)).setText("当前比例尺:" + String.valueOf(tempScale));
        Common.SetEditTextValueOnID(this._Dialog, R.id.et_inputScaleValue, String.valueOf(tempScale));
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Setting.ScaleSetting_Dialog.4
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                ScaleSetting_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
