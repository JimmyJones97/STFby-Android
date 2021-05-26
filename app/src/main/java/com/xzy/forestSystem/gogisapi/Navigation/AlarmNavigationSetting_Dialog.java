package  com.xzy.forestSystem.gogisapi.Navigation;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Config.UserParam;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.XProject.Layer_Select_Dialog;
import com.stczh.gzforestSystem.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlarmNavigationSetting_Dialog {
    private static DecimalFormat _DecimalFormat = new DecimalFormat("0.0");
    private XDialogTemplate _Dialog;
    private InputSpinner layersSpinner;
    private ICallback m_Callback;
    private Bitmap m_DeleteICON;
    private List<XLayer> m_XLayerList;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public AlarmNavigationSetting_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_DeleteICON = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.AlarmNavigationSetting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("确定")) {
                        String tmpLayer = String.valueOf(AlarmNavigationSetting_Dialog.this.layersSpinner.getEditTextView().getTag());
                        UserParam tempParam = PubVar._PubCommand.m_UserConfigDB.GetUserParam();
                        tempParam.SaveUserPara("Tag_System_AlarmNavig_Layer", tmpLayer);
                        String tmpDis = Common.GetEditTextValueOnID(AlarmNavigationSetting_Dialog.this._Dialog, R.id.et_Nav_AlarmDis);
                        tempParam.SaveUserPara("Tag_System_AlarmNavig_AlarmDistance", tmpDis);
                        String tmpGPSTime = Common.GetEditTextValueOnID(AlarmNavigationSetting_Dialog.this._Dialog, R.id.et_Nav_AlarmGPSTime);
                        tempParam.SaveUserPara("Tag_System_AlarmNavig_AlarmGPSTime", tmpGPSTime);
                        if (AlarmNavigationSetting_Dialog.this.m_Callback != null) {
                            AlarmNavigationSetting_Dialog.this.m_Callback.OnClick("提醒设置返回", new Object[]{tmpLayer, tmpDis, tmpGPSTime});
                        }
                        AlarmNavigationSetting_Dialog.this._Dialog.dismiss();
                    } else if (command.equals("选择图层")) {
                        String tmpLayerID = String.valueOf(object);
                        AlarmNavigationSetting_Dialog.this.layersSpinner.getEditTextView().setTag(tmpLayerID);
                        GeoLayer tmpLayer2 = PubVar._Map.GetGeoLayerByName(tmpLayerID);
                        if (tmpLayer2 != null) {
                            AlarmNavigationSetting_Dialog.this.layersSpinner.getEditTextView().setText(tmpLayer2.getLayerName());
                        }
                    }
                } catch (Exception e) {
                }
            }
        };
        this.layersSpinner = null;
        this.m_XLayerList = new ArrayList();
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.alarmnavigationsetting_dialog);
        this._Dialog.Resize(1.0f, PubVar.DialogHeightRatio);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("保护区提醒设置");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.HideKeybroad();
        this.layersSpinner = (InputSpinner) this._Dialog.findViewById(R.id.sp_myQueryLayersList);
        this.layersSpinner.setEditTextEnable(false);
        this.layersSpinner.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.AlarmNavigationSetting_Dialog.2
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                Layer_Select_Dialog tempDialog = new Layer_Select_Dialog();
                tempDialog.SetLayersList(AlarmNavigationSetting_Dialog.this.m_XLayerList);
                tempDialog.SetLayerSelectType(2);
                tempDialog.SetCallback(AlarmNavigationSetting_Dialog.this.pCallback);
                tempDialog.ShowDialog();
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        String tempStr;
        String tempStr2;
        String tempStr3;
        UserParam tempParam = PubVar._PubCommand.m_UserConfigDB.GetUserParam();
        HashMap<String, String> tempHashMap = tempParam.GetUserPara("Tag_System_AlarmNavig_Layer");
        if (!(tempHashMap == null || (tempStr3 = tempHashMap.get("F2")) == null)) {
            this.layersSpinner.getEditTextView().setTag(tempStr3);
            GeoLayer tmpLayer = PubVar._Map.GetGeoLayerByName(tempStr3);
            if (tmpLayer != null) {
                this.layersSpinner.getEditTextView().setText(tmpLayer.getLayerName());
            }
        }
        HashMap<String, String> tempHashMap2 = tempParam.GetUserPara("Tag_System_AlarmNavig_AlarmDistance");
        if (!(tempHashMap2 == null || (tempStr2 = tempHashMap2.get("F2")) == null)) {
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_Nav_AlarmDis, tempStr2);
        }
        HashMap<String, String> tempHashMap3 = tempParam.GetUserPara("Tag_System_AlarmNavig_AlarmGPSTime");
        if (tempHashMap3 != null && (tempStr = tempHashMap3.get("F2")) != null) {
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_Nav_AlarmGPSTime, tempStr);
        }
    }

    public void ShowDialog() {
        for (GeoLayer tmpGeoLayer : PubVar._Map.getGeoLayers().getList()) {
            if (tmpGeoLayer.getType() == EGeoLayerType.POLYGON) {
                this.m_XLayerList.add(tmpGeoLayer);
            }
        }
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.AlarmNavigationSetting_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                AlarmNavigationSetting_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
