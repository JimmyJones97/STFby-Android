package  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha;

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

public class FieldSetting_Dialog {
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

    public FieldSetting_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_MainLinearLayout = null;
        this.m_InputSpinnerListHashMap = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.FieldSetting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                GeoLayer tmpGeoLayer;
                try {
                    if (command.equals("选择督察图层返回")) {
                        String tmpLyrName = String.valueOf(object);
                        if (tmpLyrName.length() > 0 && (tmpGeoLayer = PubVar._Map.GetGeoLayerByLayerName(tmpLyrName)) != null) {
                            FieldSetting_Dialog.this.setDuChaLayer(tmpGeoLayer, true);
                        }
                    } else if (command.equals("确定")) {
                        StringBuilder tmpStringBuilder = new StringBuilder();
                        for (Map.Entry<String, InputSpinner> tmpEntry : FieldSetting_Dialog.this.m_InputSpinnerListHashMap.entrySet()) {
                            String tmpV = tmpEntry.getValue().getText().trim();
                            if (tmpV.length() == 0) {
                                Common.ShowDialog("检查字段【" + tmpEntry.getKey() + "】对应的图层字段不能为空.");
                                return;
                            }
                            if (tmpStringBuilder.length() > 0) {
                                tmpStringBuilder.append(";");
                            }
                            tmpStringBuilder.append(tmpEntry.getKey());
                            tmpStringBuilder.append("=");
                            tmpStringBuilder.append(tmpV);
                        }
                        CommonSetting.SaveParaValue("检查字段对应", tmpStringBuilder.toString());
                        CommonSetting.LoadDuChaLayerMustFields();
                        FieldSetting_Dialog.this._Dialog.dismiss();
                    }
                } catch (Exception e) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.fieldsetting_dialog);
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
            Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_DuChaLayer, "", "选择督察图层：", tempArray, "选择督察图层返回", this.pCallback);
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setDefaultLayer() {
        GeoLayer tmpGeoLayer;
        if (CommonSetting.m_LayersList.size() > 0 && (tmpGeoLayer = PubVar._Map.GetGeoLayerByID(CommonSetting.m_LayersList.get(0))) != null) {
            ((SpinnerList) this._Dialog.findViewById(R.id.sp_DuChaLayer)).SetTextJust(tmpGeoLayer.getLayerName());
            setDuChaLayer(tmpGeoLayer, false);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0083, code lost:
        r2.setText(r7);
        r2.getEditTextView().setTextColor(-16776961);
     */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0071 A[Catch:{ Exception -> 0x0065 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x002a A[EDGE_INSN: B:35:0x002a->B:32:0x002a ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setDuChaLayer( com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer r11, boolean r12) {
        /*
            r10 = this;
            if (r11 == 0) goto L_0x0030
             com.xzy.forestSystem.gogisapi.Common.PubCommand r8 = com.xzy.forestSystem.PubVar._PubCommand     // Catch:{ Exception -> 0x0065 }
             com.xzy.forestSystem.gogisapi.Geodatabase.ProjectDatabase r8 = r8.m_ProjectDB     // Catch:{ Exception -> 0x0065 }
            java.lang.String r9 = r11.getLayerID()     // Catch:{ Exception -> 0x0065 }
             com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer r1 = r8.getFeatureLayerByID(r9)     // Catch:{ Exception -> 0x0065 }
            if (r1 == 0) goto L_0x0030
            java.util.HashMap<java.lang.String, com.xzy.forestSystem.gogisapi.MyControls.InputSpinner> r8 = r10.m_InputSpinnerListHashMap     // Catch:{ Exception -> 0x0065 }
            if (r8 == 0) goto L_0x0030
            java.util.HashMap<java.lang.String, com.xzy.forestSystem.gogisapi.MyControls.InputSpinner> r8 = r10.m_InputSpinnerListHashMap     // Catch:{ Exception -> 0x0065 }
            int r8 = r8.size()     // Catch:{ Exception -> 0x0065 }
            if (r8 <= 0) goto L_0x0030
            java.util.List r5 = r1.GetFieldNamesList()     // Catch:{ Exception -> 0x0065 }
            java.util.HashMap<java.lang.String, com.xzy.forestSystem.gogisapi.MyControls.InputSpinner> r8 = r10.m_InputSpinnerListHashMap     // Catch:{ Exception -> 0x0065 }
            java.util.Set r8 = r8.entrySet()     // Catch:{ Exception -> 0x0065 }
            java.util.Iterator r3 = r8.iterator()     // Catch:{ Exception -> 0x0065 }
        L_0x002a:
            boolean r8 = r3.hasNext()     // Catch:{ Exception -> 0x0065 }
            if (r8 != 0) goto L_0x0031
        L_0x0030:
            return
        L_0x0031:
            java.lang.Object r0 = r3.next()     // Catch:{ Exception -> 0x0065 }
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0     // Catch:{ Exception -> 0x0065 }
            java.lang.Object r6 = r0.getKey()     // Catch:{ Exception -> 0x0065 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ Exception -> 0x0065 }
            java.lang.Object r2 = r0.getValue()     // Catch:{ Exception -> 0x0065 }
            com.xzy.forestSystem.gogisapi.MyControls.InputSpinner r2 = (com.xzy.forestSystem.gogisapi.MyControls.InputSpinner) r2     // Catch:{ Exception -> 0x0065 }
            r2.SetSelectItemList(r5)     // Catch:{ Exception -> 0x0065 }
            android.widget.EditText r8 = r2.getEditTextView()     // Catch:{ Exception -> 0x0065 }
            r9 = -65536(0xffffffffffff0000, float:NaN)
            r8.setTextColor(r9)     // Catch:{ Exception -> 0x0065 }
            if (r12 == 0) goto L_0x002a
            boolean r8 = r5.contains(r6)     // Catch:{ Exception -> 0x0065 }
            if (r8 == 0) goto L_0x0067
            r2.setText(r6)     // Catch:{ Exception -> 0x0065 }
            android.widget.EditText r8 = r2.getEditTextView()     // Catch:{ Exception -> 0x0065 }
            r9 = -16776961(0xffffffffff0000ff, float:-1.7014636E38)
            r8.setTextColor(r9)     // Catch:{ Exception -> 0x0065 }
            goto L_0x002a
        L_0x0065:
            r8 = move-exception
            goto L_0x0030
        L_0x0067:
            java.util.Iterator r4 = r5.iterator()     // Catch:{ Exception -> 0x0065 }
        L_0x006b:
            boolean r8 = r4.hasNext()     // Catch:{ Exception -> 0x0065 }
            if (r8 == 0) goto L_0x002a
            java.lang.Object r7 = r4.next()     // Catch:{ Exception -> 0x0065 }
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ Exception -> 0x0065 }
            boolean r8 = r6.contains(r7)     // Catch:{ Exception -> 0x0065 }
            if (r8 != 0) goto L_0x0083
            boolean r8 = r7.contains(r6)     // Catch:{ Exception -> 0x0065 }
            if (r8 == 0) goto L_0x006b
        L_0x0083:
            r2.setText(r7)     // Catch:{ Exception -> 0x0065 }
            android.widget.EditText r8 = r2.getEditTextView()     // Catch:{ Exception -> 0x0065 }
            r9 = -16776961(0xffffffffff0000ff, float:-1.7014636E38)
            r8.setTextColor(r9)     // Catch:{ Exception -> 0x0065 }
            goto L_0x002a
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.FieldSetting_Dialog.setDuChaLayer( com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer, boolean):void");
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
        HashMap<String, String> tmpHashMap = CommonSetting.m_DuChaLayerMustFieldsName;
        if (tmpHashMap != null && tmpHashMap.size() > 0) {
            for (Map.Entry<String, String> tmpEntry : tmpHashMap.entrySet()) {
                addRow(tmpEntry);
            }
        }
    }

    private void addRow(Map.Entry<String, String> entry) {
        try {
            LinearLayout layoutRoot = new LinearLayout(this._Dialog.getContext());
            layoutRoot.setLayoutParams(this.layoutParamsRoot);
            layoutRoot.setOrientation(0);
            EditText[] editTextArr = new EditText[2];
            TextView tmpTextView = new TextView(this._Dialog.getContext());
            tmpTextView.setText(entry.getKey());
            tmpTextView.setTextSize(18.0f);
            tmpTextView.setGravity(17);
            tmpTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            tmpTextView.setLayoutParams(this.tmpTxtLP01);
            tmpTextView.setBackgroundResource(R.drawable.bg_withborder01);
            layoutRoot.addView(tmpTextView);
            InputSpinner tmpInputSpinner = new InputSpinner(this._Dialog.getContext());
            tmpInputSpinner.setLayoutParams(this.tmpTxtLP02);
            tmpInputSpinner.setText(entry.getValue());
            tmpInputSpinner.setBackgroundResource(R.drawable.bg_withborder01);
            layoutRoot.addView(tmpInputSpinner);
            this.m_InputSpinnerListHashMap.put(entry.getKey(), tmpInputSpinner);
            this.m_MainLinearLayout.addView(layoutRoot);
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.FieldSetting_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                FieldSetting_Dialog.this.refreshLayout();
                FieldSetting_Dialog.this.setDefaultLayer();
            }
        });
        this._Dialog.show();
    }
}
