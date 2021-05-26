package  com.xzy.forestSystem.gogisapi.Track;

import android.content.DialogInterface;
import android.widget.EditText;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;

public class Track_Setting_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private EditText m_TrackParamESD;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public Track_Setting_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Track.Track_Setting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("确定")) {
                    try {
                        if (Common.GetSpinnerValueOnID(Track_Setting_Dialog.this._Dialog, R.id.sp_trackRecordType).equals("按时间间隔记录")) {
                            PubVar.Track_Record_Type = 0;
                        } else {
                            PubVar.Track_Record_Type = 1;
                        }
                        PubVar.Track_Record_Param = Double.parseDouble(Track_Setting_Dialog.this.m_TrackParamESD.getEditableText().toString());
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_Track_Record_Param", Double.valueOf(PubVar.Track_Record_Param));
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_Track_Record_Type", Integer.valueOf(PubVar.Track_Record_Type));
                    } catch (Exception e) {
                    }
                    if (Track_Setting_Dialog.this.m_Callback != null) {
                        Track_Setting_Dialog.this.m_Callback.OnClick("足迹记录参数设置返回", null);
                    }
                    PubVar._PubCommand.GetTrackLine().UpdateParas();
                    Track_Setting_Dialog.this._Dialog.dismiss();
                } else if (!command.equals("OnItemSelected")) {
                } else {
                    if (Common.GetSpinnerValueOnID(Track_Setting_Dialog.this._Dialog, R.id.sp_trackRecordType).equals("按时间间隔记录")) {
                        Common.SetTextViewValueOnID(Track_Setting_Dialog.this._Dialog, (int) R.id.tv_trackParamUnit, "秒");
                    } else {
                        Common.SetTextViewValueOnID(Track_Setting_Dialog.this._Dialog, (int) R.id.tv_trackParamUnit, "m");
                    }
                }
            }
        };
        this.m_TrackParamESD = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.setting_track_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("足迹记录设置");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        List<String> tempList = new ArrayList<>();
        tempList.add("按时间间隔记录");
        tempList.add("按距离间隔记录");
        Common.SetSpinnerListData(this._Dialog, "请选择足迹记录模式:", tempList, (int) R.id.sp_trackRecordType, this.pCallback);
        if (PubVar.Track_Record_Type < tempList.size()) {
            Common.SetValueToView(tempList.get(PubVar.Track_Record_Type), this._Dialog.findViewById(R.id.sp_trackRecordType));
        }
        if (PubVar.Track_Record_Type == 0) {
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_TrackRecordType, "按时间间隔记录");
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_trackParamUnit, "秒");
        } else {
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_TrackRecordType, "按距离间隔记录");
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_trackParamUnit, "m");
        }
        this.m_TrackParamESD = (EditText) this._Dialog.findViewById(R.id.sp_trackRecordParam);
        this.m_TrackParamESD.setText(String.valueOf(PubVar.Track_Record_Param));
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Track.Track_Setting_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Track_Setting_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
