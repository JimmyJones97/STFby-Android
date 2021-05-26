package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.PMTranslate;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;

public class PMTranslate_Setting_Dialog {
    private XDialogTemplate _Dialog;
    private boolean _NeedAddLastPara;
    private PMTranslate _PMTranslate;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public PMTranslate_Setting_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this._PMTranslate = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.PMTranslate_Setting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                double[] tempParas;
                try {
                    if (paramString.equals("确定")) {
                        Common.ShowYesNoDialog(PMTranslate_Setting_Dialog.this._Dialog.getContext(), "是否按如下参数进行平面转换?\r\nX平移(m):" + Common.GetEditTextValueOnID(PMTranslate_Setting_Dialog.this._Dialog, R.id.editTextPara01) + "\r\nY平移(m):" + Common.GetEditTextValueOnID(PMTranslate_Setting_Dialog.this._Dialog, R.id.editTextPara02), new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.PMTranslate_Setting_Dialog.1.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject2) {
                                if (paramString2.equals("YES")) {
                                    String tempPara01 = Common.GetEditTextValueOnID(PMTranslate_Setting_Dialog.this._Dialog, R.id.editTextPara01);
                                    String tempPara02 = Common.GetEditTextValueOnID(PMTranslate_Setting_Dialog.this._Dialog, R.id.editTextPara02);
                                    if (PMTranslate_Setting_Dialog.this._PMTranslate == null) {
                                        PMTranslate_Setting_Dialog.this._PMTranslate = new PMTranslate();
                                    }
                                    PMTranslate_Setting_Dialog.this._PMTranslate.SetPMCoorTransMethodName("三参转换");
                                    PMTranslate_Setting_Dialog.this._PMTranslate.SetTransToP31(tempPara01);
                                    PMTranslate_Setting_Dialog.this._PMTranslate.SetTransToP32(tempPara02);
                                    boolean tmpBool01 = Common.GetCheckBoxValueOnID(PMTranslate_Setting_Dialog.this._Dialog, R.id.ck_ConsiderTrans);
                                    if (PMTranslate_Setting_Dialog.this.m_Callback != null) {
                                        PMTranslate_Setting_Dialog.this.m_Callback.OnClick("平面参数转换返回", new Object[]{PMTranslate_Setting_Dialog.this._PMTranslate, Boolean.valueOf(tmpBool01)});
                                    }
                                    PubVar._PubCommand.m_ProjectDB.GetProjectManage().SaveProjectInfo();
                                    PMTranslate_Setting_Dialog.this._Dialog.dismiss();
                                }
                            }
                        });
                    } else if (paramString.equals("校准点") && (tempParas = (double[]) pObject) != null && tempParas.length > 1) {
                        double tempX = tempParas[0];
                        double tempY = tempParas[1];
                        if (PMTranslate_Setting_Dialog.this._NeedAddLastPara && PMTranslate_Setting_Dialog.this._PMTranslate != null) {
                            tempX += PMTranslate_Setting_Dialog.this._PMTranslate.GetTransToP31();
                            tempY += PMTranslate_Setting_Dialog.this._PMTranslate.GetTransToP32();
                        }
                        Common.SetEditTextValueOnID(PMTranslate_Setting_Dialog.this._Dialog, R.id.editTextPara01, String.valueOf(tempX));
                        Common.SetEditTextValueOnID(PMTranslate_Setting_Dialog.this._Dialog, R.id.editTextPara02, String.valueOf(tempY));
                    }
                } catch (Exception e) {
                }
            }
        };
        this._NeedAddLastPara = false;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.pmtranslate_setting_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("参数校正");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.buttonCheckBtn01).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.ck_ConsiderTrans).setVisibility(4);
        ((CheckBox) this._Dialog.findViewById(R.id.ck_checkResultSum)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.PMTranslate_Setting_Dialog.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                PMTranslate_Setting_Dialog.this._NeedAddLastPara = arg1;
            }
        });
    }

    public void SetTitle(String title) {
        this._Dialog.SetCaption(title);
    }

    public void SetPMTranslate(PMTranslate PMTranslate) {
        this._PMTranslate = PMTranslate;
    }

    public void SetNeedAddLastPara(boolean value) {
        this._NeedAddLastPara = value;
        this._Dialog.findViewById(R.id.ck_checkResultSum).setVisibility(8);
    }

    public void setConsideTranslateVisible(boolean visible, boolean isConsideTranslate) {
        if (visible) {
            this._Dialog.findViewById(R.id.ck_ConsiderTrans).setVisibility(0);
        } else {
            this._Dialog.findViewById(R.id.ck_ConsiderTrans).setVisibility(4);
        }
        Common.SetCheckValueOnID(this._Dialog, R.id.ck_ConsiderTrans, isConsideTranslate);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("单点校正")) {
            CheckControlPtnCal_Dialog tempDialog = new CheckControlPtnCal_Dialog();
            tempDialog.SetCallback(this.pCallback);
            tempDialog.ShowDialog();
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.PMTranslate_Setting_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                if (PMTranslate_Setting_Dialog.this._PMTranslate != null) {
                    Common.SetEditTextValueOnID(PMTranslate_Setting_Dialog.this._Dialog, R.id.editTextPara01, String.valueOf(PMTranslate_Setting_Dialog.this._PMTranslate.GetTransToP31()));
                    Common.SetEditTextValueOnID(PMTranslate_Setting_Dialog.this._Dialog, R.id.editTextPara02, String.valueOf(PMTranslate_Setting_Dialog.this._PMTranslate.GetTransToP32()));
                    Common.SetEditTextValueOnID(PMTranslate_Setting_Dialog.this._Dialog, R.id.editTextPara03, String.valueOf(PMTranslate_Setting_Dialog.this._PMTranslate.GetTransToP33()));
                }
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
                PMTranslate_Setting_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
