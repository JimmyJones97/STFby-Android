package  com.xzy.forestSystem.gogisapi.Edit;

import android.content.DialogInterface;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.ECoordinateSystemType;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;

public class AddPoint_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public AddPoint_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Edit.AddPoint_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                Coordinate tempCoord;
                if (command.equals("确定")) {
                    String tempStr01 = Common.GetTextValueOnID(AddPoint_Dialog.this._Dialog, (int) R.id.editTextPara01);
                    String tempStr02 = Common.GetTextValueOnID(AddPoint_Dialog.this._Dialog, (int) R.id.editTextPara02);
                    if (tempStr01 == null || tempStr01.trim().equals("") || tempStr02 == null || tempStr02.trim().equals("")) {
                        Common.ShowDialog("坐标输入值不能为空.");
                        return;
                    }
                    String tempType = Common.GetSpinnerValueOnID(AddPoint_Dialog.this._Dialog, R.id.sp_coordInputFormat);
                    double tempX = Double.parseDouble(tempStr01);
                    double tempY = Double.parseDouble(tempStr02);
                    boolean tmpConvert = Common.GetCheckBoxValueOnID(AddPoint_Dialog.this._Dialog, R.id.ck_consideConvert);
                    AbstractC0383CoordinateSystem pCoordSystem = PubVar.m_Workspace.GetCoorSystem();
                    if (!tempType.contains("WGS-1984")) {
                        tempCoord = new Coordinate(tempX, tempY);
                        if (tmpConvert) {
                            tempCoord = pCoordSystem.ConvertXYtoBLWithTranslate(tempCoord, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                        } else {
                            Coordinate tempCoord2 = pCoordSystem.ConvertXYToBL(tempCoord);
                            tempCoord.setGeoX(tempCoord2.getX());
                            tempCoord.setGeoY(tempCoord2.getY());
                        }
                    } else if (pCoordSystem.GetCoordinateSystemType() == ECoordinateSystemType.enWGS1984) {
                        tempCoord = pCoordSystem.ConvertBLToXY(new Coordinate(tempX, tempY));
                        tempCoord.setGeoX(tempX);
                        tempCoord.setGeoY(tempY);
                    } else {
                        Coordinate tempCoord22 = new Coordinate(tempX, tempY);
                        if (tmpConvert) {
                            tempCoord = pCoordSystem.ConvertBLtoXYWithTranslate(tempCoord22, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                        } else {
                            tempCoord = pCoordSystem.ConvertBLToXY(tempCoord22);
                            tempCoord.setGeoX(tempX);
                            tempCoord.setGeoY(tempY);
                        }
                    }
                    if (!(tempCoord == null || AddPoint_Dialog.this.m_Callback == null)) {
                        AddPoint_Dialog.this.m_Callback.OnClick("坐标输入返回", tempCoord);
                    }
                    AddPoint_Dialog.this._Dialog.dismiss();
                } else if (!command.equals("OnItemSelected")) {
                } else {
                    if (object.toString().contains("当前项目")) {
                        Common.SetTextViewValueOnID(AddPoint_Dialog.this._Dialog, (int) R.id.tv_xlabel, "X坐标：");
                        Common.SetTextViewValueOnID(AddPoint_Dialog.this._Dialog, (int) R.id.tv_ylabel, "Y坐标：");
                        return;
                    }
                    Common.SetTextViewValueOnID(AddPoint_Dialog.this._Dialog, (int) R.id.tv_xlabel, "经度：");
                    Common.SetTextViewValueOnID(AddPoint_Dialog.this._Dialog, (int) R.id.tv_ylabel, "纬度：");
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.addpoint_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("坐标输入");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshFormatTypes() {
        List<String> tempArray = new ArrayList<>();
        AbstractC0383CoordinateSystem pCoordSystem = PubVar.m_Workspace.GetCoorSystem();
        if (pCoordSystem.GetCoordinateSystemType() == ECoordinateSystemType.enWGS1984) {
            tempArray.add("【WGS-1984】DDD.DDDDDDDD");
            this._Dialog.findViewById(R.id.ck_consideConvert).setVisibility(8);
        } else {
            tempArray.add("【当前项目】" + pCoordSystem.GetName());
            tempArray.add("【WGS-1984】DDD.DDDDDDDD");
        }
        Common.SetSpinnerListData(this._Dialog, "选择坐标输入格式", tempArray, (int) R.id.sp_coordInputFormat, this.pCallback);
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Edit.AddPoint_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                AddPoint_Dialog.this.refreshFormatTypes();
            }
        });
        this._Dialog.show();
    }
}
