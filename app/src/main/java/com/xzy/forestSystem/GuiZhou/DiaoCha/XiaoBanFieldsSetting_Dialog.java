package com.xzy.forestSystem.GuiZhou.DiaoCha;

import android.content.DialogInterface;
import androidx.core.view.ViewCompat;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XiaoBanFieldsSetting_Dialog {
    private XDialogTemplate _Dialog;
    private LinearLayout.LayoutParams layoutParamsRoot;
    private ICallback m_Callback;
    private HashMap<String, InputSpinner> m_InputSpinnerListHashMap;
    private LinearLayout m_MainLinearLayout;
    private ICallback pCallback;
    private LinearLayout.LayoutParams tmpImgViewLayoutParams;
    private LinearLayout.LayoutParams tmpSpLy;
    private LinearLayout.LayoutParams tmpTxtLP01;
    private LinearLayout.LayoutParams tmpTxtLP02;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public XiaoBanFieldsSetting_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_MainLinearLayout = null;
        this.m_InputSpinnerListHashMap = null;
        this.pCallback = new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanFieldsSetting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                GeoLayer tmpGeoLayer;
                try {
                    if (command.equals("选择调查图层返回")) {
                        String tmpLyrName = String.valueOf(object);
                        if (tmpLyrName.length() > 0 && (tmpGeoLayer = PubVar._Map.GetGeoLayerByLayerName(tmpLyrName)) != null) {
                            XiaoBanFieldsSetting_Dialog.this.setDuChaLayer(tmpGeoLayer, true);
                        }
                    } else if (command.equals("确定")) {
                        String tmpLyrName2 = Common.GetSpinnerValueOnID(XiaoBanFieldsSetting_Dialog.this._Dialog, R.id.sp_DuChaLayer);
                        if (tmpLyrName2.trim().length() == 0) {
                            Common.ShowDialog("小班图层不能为空.");
                            return;
                        }
                        StringBuilder tmpStringBuilder = new StringBuilder();
                        for (Map.Entry<String, InputSpinner> tmpEntry : XiaoBanFieldsSetting_Dialog.this.m_InputSpinnerListHashMap.entrySet()) {
                            String tmpV = tmpEntry.getValue().getText().trim();
                            if (tmpV.length() == 0) {
                                Common.ShowDialog("字段【" + tmpEntry.getKey() + "】对应的图层字段不能为空.");
                                return;
                            }
                            if (tmpStringBuilder.length() > 0) {
                                tmpStringBuilder.append(";");
                            }
                            tmpStringBuilder.append(tmpEntry.getKey());
                            tmpStringBuilder.append("=");
                            tmpStringBuilder.append(tmpV);
                        }
                        CommonSetting.SaveParaValue("小班字段对应", tmpStringBuilder.toString());
                        String tmpLyrID = "";
                        GeoLayer tmpGeoLayer2 = PubVar._Map.GetGeoLayerByLayerName(tmpLyrName2);
                        if (tmpGeoLayer2 != null) {
                            tmpLyrID = tmpGeoLayer2.getLayerID();
                        }
                        CommonSetting.SaveParaValue("小班图层", tmpLyrID);
                        CommonSetting.InitialSetting();
                        XiaoBanFieldsSetting_Dialog.this._Dialog.dismiss();
                    }
                } catch (Exception e) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.xiaobanfieldssetting_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("字段关联设置");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.m_MainLinearLayout = (LinearLayout) this._Dialog.findViewById(R.id.ll_Yangdi_DataList);
        refreshLayerList();
    }

    private void refreshLayerList() {
        try {
            List<String> tempArray = new ArrayList<>();
            for (GeoLayer tmpGeoLayer : PubVar._Map.getGeoLayers().getList()) {
                if (tmpGeoLayer.getType() == EGeoLayerType.POLYGON) {
                    tempArray.add(tmpGeoLayer.getLayerName());
                }
            }
            Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_DuChaLayer, "", "选择调查图层：", tempArray, "选择调查图层返回", this.pCallback);
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setDefaultLayer() {
        GeoLayer tmpGeoLayer;
        if (CommonSetting.m_XiaoBanLayersList.size() > 0 && (tmpGeoLayer = PubVar._Map.GetGeoLayerByID(CommonSetting.m_XiaoBanLayersList.get(0))) != null) {
            ((SpinnerList) this._Dialog.findViewById(R.id.sp_DuChaLayer)).SetTextJust(tmpGeoLayer.getLayerName());
            setDuChaLayer(tmpGeoLayer, false);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00c9  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00aa A[EDGE_INSN: B:43:0x00aa->B:21:0x00aa ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setDuChaLayer( com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer r19, boolean r20) {
        /*
            r18 = this;
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            if (r19 == 0) goto L_0x003b
             com.xzy.forestSystem.gogisapi.Common.PubCommand r14 = com.xzy.forestSystem.PubVar._PubCommand     // Catch:{ Exception -> 0x00bc }
             com.xzy.forestSystem.gogisapi.Geodatabase.ProjectDatabase r14 = r14.m_ProjectDB     // Catch:{ Exception -> 0x00bc }
            java.lang.String r15 = r19.getLayerID()     // Catch:{ Exception -> 0x00bc }
             com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer r4 = r14.getFeatureLayerByID(r15)     // Catch:{ Exception -> 0x00bc }
            if (r4 == 0) goto L_0x003b
            r0 = r18
            java.util.HashMap<java.lang.String, com.xzy.forestSystem.gogisapi.MyControls.InputSpinner> r14 = r0.m_InputSpinnerListHashMap     // Catch:{ Exception -> 0x00bc }
            if (r14 == 0) goto L_0x003b
            r0 = r18
            java.util.HashMap<java.lang.String, com.xzy.forestSystem.gogisapi.MyControls.InputSpinner> r14 = r0.m_InputSpinnerListHashMap     // Catch:{ Exception -> 0x00bc }
            int r14 = r14.size()     // Catch:{ Exception -> 0x00bc }
            if (r14 <= 0) goto L_0x003b
            java.util.List r8 = r4.GetFieldNamesList()     // Catch:{ Exception -> 0x00bc }
            r0 = r18
            java.util.HashMap<java.lang.String, com.xzy.forestSystem.gogisapi.MyControls.InputSpinner> r14 = r0.m_InputSpinnerListHashMap     // Catch:{ Exception -> 0x00bc }
            java.util.Set r14 = r14.entrySet()     // Catch:{ Exception -> 0x00bc }
            java.util.Iterator r6 = r14.iterator()     // Catch:{ Exception -> 0x00bc }
        L_0x0035:
            boolean r14 = r6.hasNext()     // Catch:{ Exception -> 0x00bc }
            if (r14 != 0) goto L_0x0075
        L_0x003b:
            if (r20 == 0) goto L_0x0074
            int r14 = r13.length()
            if (r14 <= 0) goto L_0x0074
            java.lang.String r11 = r13.toString()
            java.lang.String r9 = r19.getLayerID()
            r0 = r18
             com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate r14 = r0._Dialog
            android.content.Context r14 = r14.getContext()
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            java.lang.String r16 = "图层中不存在以下字段,是否自动新建这些字段?\r\n\r\n"
            r15.<init>(r16)
            java.lang.String r16 = r13.toString()
            java.lang.StringBuilder r15 = r15.append(r16)
            java.lang.String r15 = r15.toString()
            r16 = 1
            com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanFieldsSetting_Dialog$2 r17 = new com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanFieldsSetting_Dialog$2
            r0 = r17
            r1 = r18
            r0.<init>(r11, r9)
             com.xzy.forestSystem.gogisapi.Common.C0321Common.ShowYesNoDialogWithAlert(r14, r15, r16, r17)
        L_0x0074:
            return
        L_0x0075:
            java.lang.Object r3 = r6.next()
            java.util.Map$Entry r3 = (java.util.Map.Entry) r3
            java.lang.Object r10 = r3.getKey()
            java.lang.String r10 = (java.lang.String) r10
            java.lang.Object r5 = r3.getValue()
            com.xzy.forestSystem.gogisapi.MyControls.InputSpinner r5 = (com.xzy.forestSystem.gogisapi.MyControls.InputSpinner) r5
            r5.SetSelectItemList(r8)
            android.widget.EditText r14 = r5.getEditTextView()
            r15 = -65536(0xffffffffffff0000, float:NaN)
            r14.setTextColor(r15)
            if (r20 == 0) goto L_0x0035
            r2 = 0
            boolean r14 = r8.contains(r10)
            if (r14 == 0) goto L_0x00bf
            r5.setText(r10)
            android.widget.EditText r14 = r5.getEditTextView()
            r15 = -16776961(0xffffffffff0000ff, float:-1.7014636E38)
            r14.setTextColor(r15)
            r2 = 1
        L_0x00aa:
            if (r2 != 0) goto L_0x0035
            int r14 = r13.length()
            if (r14 <= 0) goto L_0x00b7
            java.lang.String r14 = "\r\n"
            r13.append(r14)
        L_0x00b7:
            r13.append(r10)
            goto L_0x0035
        L_0x00bc:
            r14 = move-exception
            goto L_0x003b
        L_0x00bf:
            java.util.Iterator r7 = r8.iterator()
        L_0x00c3:
            boolean r14 = r7.hasNext()
            if (r14 == 0) goto L_0x00aa
            java.lang.Object r12 = r7.next()
            java.lang.String r12 = (java.lang.String) r12
            boolean r14 = r10.contains(r12)
            if (r14 != 0) goto L_0x00db
            boolean r14 = r12.contains(r10)
            if (r14 == 0) goto L_0x00c3
        L_0x00db:
            r5.setText(r12)
            android.widget.EditText r14 = r5.getEditTextView()
            r15 = -16776961(0xffffffffff0000ff, float:-1.7014636E38)
            r14.setTextColor(r15)
            goto L_0x00aa
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanFieldsSetting_Dialog.setDuChaLayer( com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer, boolean):void");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        this.layoutParamsRoot = new LinearLayout.LayoutParams(-1, -2);
        this.layoutParamsRoot.setMargins(0, 0, 0, 0);
        this.tmpImgViewLayoutParams = new LinearLayout.LayoutParams(-2, -2);
        int tmpMargInt = (int) (PubVar.ScaledDensity * 5.0f);
        this.tmpImgViewLayoutParams.setMargins(tmpMargInt, tmpMargInt, tmpMargInt, tmpMargInt);
        this.tmpTxtLP01 = new LinearLayout.LayoutParams(-2, -1);
        this.tmpTxtLP01.width = (int) (150.0f * PubVar.ScaledDensity);
        this.tmpTxtLP01.setMargins(0, 0, 0, 0);
        this.tmpTxtLP02 = new LinearLayout.LayoutParams(-2, -2);
        this.tmpTxtLP02.weight = 1.0f;
        this.tmpTxtLP02.setMargins(0, 0, 0, 0);
        this.tmpSpLy = new LinearLayout.LayoutParams(-2, -1);
        this.tmpSpLy.weight = 1.0f;
        this.tmpSpLy.setMargins(0, 0, 0, 0);
        this.m_InputSpinnerListHashMap = new HashMap<>();
        HashMap<String, String> tmpHashMap = CommonSetting.m_XiaoBanLayerMustFieldsName;
        List<String> tmpList = CommonSetting.m_XiaoBanLayerMustFieldsNameList;
        if (tmpHashMap != null && tmpHashMap.size() > 0) {
            for (String tmpFNBame : tmpList) {
                addRow(tmpFNBame, tmpHashMap.get(tmpFNBame));
            }
        }
    }

    private void addRow(String fieldName, String relFileldName) {
        try {
            LinearLayout layoutRoot = new LinearLayout(this._Dialog.getContext());
            layoutRoot.setLayoutParams(this.layoutParamsRoot);
            layoutRoot.setOrientation(0);
            EditText[] editTextArr = new EditText[2];
            TextView tmpTextView = new TextView(this._Dialog.getContext());
            tmpTextView.setText(fieldName);
            tmpTextView.setTextSize(18.0f);
            tmpTextView.setGravity(17);
            tmpTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            tmpTextView.setLayoutParams(this.tmpTxtLP01);
            tmpTextView.setBackgroundResource(R.drawable.bg_withborder01);
            layoutRoot.addView(tmpTextView);
            InputSpinner tmpInputSpinner = new InputSpinner(this._Dialog.getContext());
            tmpInputSpinner.setLayoutParams(this.tmpTxtLP02);
            tmpInputSpinner.setText(relFileldName);
            tmpInputSpinner.setBackgroundResource(R.drawable.bg_withborder01);
            layoutRoot.addView(tmpInputSpinner);
            this.m_InputSpinnerListHashMap.put(fieldName, tmpInputSpinner);
            this.m_MainLinearLayout.addView(layoutRoot);
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanFieldsSetting_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                XiaoBanFieldsSetting_Dialog.this.refreshLayout();
                XiaoBanFieldsSetting_Dialog.this.setDefaultLayer();
            }
        });
        this._Dialog.show();
    }
}
