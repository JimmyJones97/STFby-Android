package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import android.widget.EditText;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_UTM;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;

public class CoordSystem_CenterLine {
    public String CurrentCoordName;
    private XDialogTemplate _Dialog;
    private ICallback _pCallback;
    private ICallback m_Callback;
    private double m_CenterLine;
    private SpinnerList m_DaiHSpinner;
    private SpinnerList m_DegreeSpinner;

    public CoordSystem_CenterLine() {
        this._Dialog = null;
        this.CurrentCoordName = "";
        this._pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CoordSystem_CenterLine.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                float tempF03;
                final String centerJXStr = ((EditText) CoordSystem_CenterLine.this._Dialog.findViewById(R.id.et_centerjx)).getText().toString();
                if (paramString.equals("确定")) {
                    if (centerJXStr.equals("")) {
                        Common.ShowDialog(CoordSystem_CenterLine.this._Dialog.getContext(), "请填写正确的中央经线！");
                        return;
                    }
                    final String tmpFenDaiStr = Common.GetSpinnerValueOnID(CoordSystem_CenterLine.this._Dialog, R.id.sp_fd).replace("度", "");
                    final String tmpDaiHao = Common.GetSpinnerValueOnID(CoordSystem_CenterLine.this._Dialog, R.id.sp_dh);
                    float tempF01 = Float.parseFloat(tmpFenDaiStr);
                    int tempF02 = Integer.parseInt(tmpDaiHao);
                    if (CoordSystem_CenterLine.this.CurrentCoordName.contains("UTM")) {
                        tempF03 = (float) Coordinate_UTM.GetUTMCenterline((double) tempF02);
                    } else {
                        tempF03 = AbstractC0383CoordinateSystem.GetCenterJX(tempF02, tempF01);
                    }
                    if (tempF03 != Float.parseFloat(centerJXStr)) {
                        Common.ShowYesNoDialog(CoordSystem_CenterLine.this._Dialog.getContext(), "按【" + tmpFenDaiStr + "】分带,带号【" + tmpDaiHao + "】\r\n\r\n计算中央经线为:" + tempF03 + "\r\n输入中央经线为:" + centerJXStr + "\r\n\r\n不符合,是否继续?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CoordSystem_CenterLine.1.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject2) {
                                if (paramString2.equals("YES")) {
                                    if (CoordSystem_CenterLine.this.m_Callback != null) {
                                        CoordSystem_CenterLine.this.m_Callback.OnClick("中央经线选择返回", new String[]{centerJXStr, tmpFenDaiStr, tmpDaiHao});
                                    }
                                    CoordSystem_CenterLine.this._Dialog.dismiss();
                                }
                            }
                        });
                        return;
                    }
                    if (CoordSystem_CenterLine.this.m_Callback != null) {
                        CoordSystem_CenterLine.this.m_Callback.OnClick("中央经线选择返回", new String[]{centerJXStr, tmpFenDaiStr, tmpDaiHao});
                    }
                    CoordSystem_CenterLine.this._Dialog.dismiss();
                } else if (paramString.equals("OnItemSelected")) {
                    CoordSystem_CenterLine.this.updateCenterLine();
                    CoordSystem_CenterLine.this.calCenterJX(Common.GetSpinnerValueOnID(CoordSystem_CenterLine.this._Dialog, R.id.sp_dh));
                } else {
                    CoordSystem_CenterLine.this._Dialog.dismiss();
                }
            }
        };
        this.m_Callback = null;
        this.m_CenterLine = 120.0d;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.project_centerjx_dialog);
        this._Dialog.SetCaption("中央经线");
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this._pCallback);
        this.m_DegreeSpinner = (SpinnerList) this._Dialog.findViewById(R.id.sp_fd);
        this.m_DaiHSpinner = (SpinnerList) this._Dialog.findViewById(R.id.sp_dh);
        List<String> tmpDrgList = new ArrayList<>();
        tmpDrgList.add("3度");
        tmpDrgList.add("6度");
        tmpDrgList.add("1.5度");
        Common.SetSpinnerListData(this._Dialog, "选择分带类型", tmpDrgList, (int) R.id.sp_fd, this._pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateCenterLine() {
        float tempLongWidth = 3.0f;
        String tempStr = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_fd);
        if (tempStr.equals("6度")) {
            tempLongWidth = 6.0f;
        } else if (tempStr.equals("1.5度")) {
            tempLongWidth = 1.5f;
        }
        int tempEnd = (int) (Math.floor(360.0d / ((double) tempLongWidth)) + 1.0d);
        List<String> tempArray = new ArrayList<>();
        for (int i = 1; i < tempEnd; i++) {
            tempArray.add(String.valueOf(i));
        }
        Common.SetSpinnerListData(this._Dialog, "选择带号", tempArray, (int) R.id.sp_dh, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CoordSystem_CenterLine.2
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                CoordSystem_CenterLine.this.calCenterJX(pObject.toString());
            }
        });
    }

    public void SetCenterLine(double centerLine) {
        int tmpDaiHao;
        this.m_CenterLine = centerLine;
        Common.SetTextViewValueOnID(this._Dialog, (int) R.id.et_centerjx, String.valueOf(centerLine));
        String tmpStr = this.m_DegreeSpinner.getText().toString();
        if (tmpStr != null && !tmpStr.equals("")) {
            float tmpFenDai = Float.parseFloat(tmpStr.replace("度", ""));
            if (this.CurrentCoordName.contains("UTM")) {
                tmpDaiHao = Coordinate_UTM.GetDH(centerLine);
            } else {
                tmpDaiHao = AbstractC0383CoordinateSystem.GetDH(centerLine, tmpFenDai);
            }
            this.m_DaiHSpinner.SetTextJust(String.valueOf(tmpDaiHao));
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void calCenterJX(String daiHao) {
        String str;
        float tempFenDu = Float.parseFloat(Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_fd).replace("度", ""));
        if (this.CurrentCoordName.contains("UTM")) {
            str = String.valueOf(Coordinate_UTM.GetUTMCenterline(Double.parseDouble(daiHao)));
        } else {
            str = String.valueOf(AbstractC0383CoordinateSystem.GetCenterJX(Integer.parseInt(daiHao), tempFenDu));
        }
        Common.SetTextViewValueOnID(this._Dialog, (int) R.id.et_centerjx, str);
    }

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CoordSystem_CenterLine.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                List<String> tmpDrgList = new ArrayList<>();
                if (CoordSystem_CenterLine.this.CurrentCoordName.contains("UTM")) {
                    tmpDrgList.add("6度");
                } else {
                    tmpDrgList.add("3度");
                    tmpDrgList.add("6度");
                    tmpDrgList.add("1.5度");
                }
                Common.SetSpinnerListData(CoordSystem_CenterLine.this._Dialog, "选择分带类型", tmpDrgList, (int) R.id.sp_fd, CoordSystem_CenterLine.this._pCallback);
            }
        });
        this._Dialog.show();
    }
}
