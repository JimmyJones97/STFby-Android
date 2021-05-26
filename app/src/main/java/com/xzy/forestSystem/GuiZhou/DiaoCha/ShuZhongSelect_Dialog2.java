package com.xzy.forestSystem.GuiZhou.DiaoCha;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShuZhongSelect_Dialog2 {
    private XDialogTemplate _Dialog;
    private LinearLayout.LayoutParams layoutParamsMatchWrap;
    private LinearLayout.LayoutParams layoutParamsRoot;
    private AutoCompleteTextView m_AutoCompleteTextView;
    private ICallback m_Callback;
    private boolean m_ClickReturn;
    Drawable m_DefaultDrawable;
    LinearLayout m_MainLinearLayout;
    Drawable m_SelectDrawable;
    private String m_SelectName;
    private ICallback pCallback;
    private LinearLayout.LayoutParams tmpButtonPL;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public ShuZhongSelect_Dialog2() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_SelectName = "";
        this.m_MainLinearLayout = null;
        this.m_ClickReturn = true;
        this.m_AutoCompleteTextView = null;
        this.pCallback = new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.ShuZhongSelect_Dialog2.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("确定")) {
                    ShuZhongSelect_Dialog2.this.m_SelectName = ShuZhongSelect_Dialog2.this.m_AutoCompleteTextView.getText().toString();
                    if (ShuZhongSelect_Dialog2.this.m_Callback != null) {
                        ShuZhongSelect_Dialog2.this.m_Callback.OnClick("选择树种返回", ShuZhongSelect_Dialog2.this.m_SelectName);
                    }
                    ShuZhongSelect_Dialog2.this._Dialog.dismiss();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.shuzhongselect_dialog2);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("选择树种");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.m_MainLinearLayout = (LinearLayout) this._Dialog.findViewById(R.id.ll_ShuZhong_DataList);
        this.m_AutoCompleteTextView = (AutoCompleteTextView) this._Dialog.findViewById(R.id.atinput_shuzhong);
        this.m_DefaultDrawable = this._Dialog.getContext().getResources().getDrawable(R.drawable.shuzhongbuttondefault);
        this.m_SelectDrawable = this._Dialog.getContext().getResources().getDrawable(R.drawable.shuzhongbuttonselector);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void initialButtons() {
        this.layoutParamsRoot = new LinearLayout.LayoutParams(-1, -2);
        this.layoutParamsRoot.setMargins(0, (int) (5.0f * PubVar.ScaledDensity), 0, 0);
        this.layoutParamsMatchWrap = new LinearLayout.LayoutParams(-1, -2);
        this.layoutParamsMatchWrap.setMargins(0, 0, 0, 0);
        this.tmpButtonPL = new LinearLayout.LayoutParams(-2, -2);
        this.tmpButtonPL.weight = 1.0f;
        this.tmpButtonPL.setMargins((int) (PubVar.ScaledDensity * 3.0f), (int) (PubVar.ScaledDensity * 3.0f), (int) (PubVar.ScaledDensity * 3.0f), (int) (PubVar.ScaledDensity * 3.0f));
        HashMap<String, List<String>> tmphasHashMap = CommonSetting.m_ShuZhongZuHashMap;
        List<String> tmpList = CommonSetting.GetShuZhongZuList();
        if (!(tmphasHashMap == null || tmphasHashMap.size() <= 0 || tmpList == null)) {
            List<String> tmpShuZhongList = new ArrayList<>();
            for (String tmpShuZhongZuString : tmpList) {
                if (tmphasHashMap.containsKey(tmpShuZhongZuString)) {
                    List<String> tmpShuZList = tmphasHashMap.get(tmpShuZhongZuString);
                    addShuZhongZu(tmpShuZhongZuString, tmpShuZList);
                    tmpShuZhongList.addAll(tmpShuZList);
                }
            }
            if (tmpShuZhongList.size() > 0) {
                this.m_AutoCompleteTextView.setAdapter(new ArrayAdapter<>(this._Dialog.getContext(), 17367050, (String[]) tmpShuZhongList.toArray(new String[tmpShuZhongList.size()])));
            }
        }
    }

    private void addShuZhongZu(String shuZhongZu, List<String> shuZhongList) {
        if (shuZhongList.size() > 0) {
            LinearLayout layoutRoot = new LinearLayout(this._Dialog.getContext());
            layoutRoot.setLayoutParams(this.layoutParamsRoot);
            layoutRoot.setOrientation(1);
            layoutRoot.setBackgroundResource(R.drawable.dialog_headbar_bg);
            TextView tmpTextView = new TextView(this._Dialog.getContext());
            tmpTextView.setText(shuZhongZu);
            tmpTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            tmpTextView.setLayoutParams(this.layoutParamsMatchWrap);
            tmpTextView.setBackgroundResource(R.drawable.dialog_headtext_corner_bg);
            layoutRoot.addView(tmpTextView);
            int tmpTid = -1;
            LinearLayout tmpLinearLayout001 = null;
            for (String tmpShuZhong : shuZhongList) {
                tmpTid++;
                if (tmpTid == 0) {
                    tmpLinearLayout001 = new LinearLayout(this._Dialog.getContext());
                    tmpLinearLayout001.setLayoutParams(this.layoutParamsMatchWrap);
                    tmpLinearLayout001.setOrientation(0);
                }
                if (tmpTid > 2) {
                    tmpTid = -1;
                    if (tmpLinearLayout001 != null) {
                        layoutRoot.addView(tmpLinearLayout001);
                    }
                    tmpLinearLayout001 = new LinearLayout(this._Dialog.getContext());
                    tmpLinearLayout001.setLayoutParams(this.layoutParamsMatchWrap);
                    tmpLinearLayout001.setOrientation(0);
                }
                if (tmpLinearLayout001 != null) {
                    Button tmpBtnButton = new Button(this._Dialog.getContext());
                    tmpBtnButton.setLayoutParams(this.tmpButtonPL);
                    tmpBtnButton.setText(tmpShuZhong);
                    tmpBtnButton.setTag(tmpShuZhong);
                    tmpBtnButton.setOnClickListener(new ViewClick());
                    if (this.m_SelectName.equals(tmpShuZhong)) {
                        tmpBtnButton.setBackgroundResource(R.drawable.shuzhongbuttonselector);
                    } else {
                        tmpBtnButton.setBackgroundResource(R.drawable.shuzhongbuttondefault);
                    }
                    tmpLinearLayout001.addView(tmpBtnButton);
                }
            }
            if (tmpLinearLayout001 != null) {
                int tmpChCount = tmpLinearLayout001.getChildCount();
                if (tmpChCount > 0 && tmpChCount < 3) {
                    int tmpTid02 = 3 - tmpChCount;
                    while (tmpTid02 > 0) {
                        tmpTid02--;
                        Button tmpBtnButton2 = new Button(this._Dialog.getContext());
                        tmpBtnButton2.setLayoutParams(this.tmpButtonPL);
                        tmpBtnButton2.setText("  ");
                        tmpBtnButton2.setVisibility(4);
                        tmpBtnButton2.setBackgroundResource(R.drawable.shuzhongbuttondefault);
                        tmpLinearLayout001.addView(tmpBtnButton2);
                    }
                }
                layoutRoot.addView(tmpLinearLayout001);
            }
            this.m_MainLinearLayout.addView(layoutRoot);
        }
    }

    public void SetSelectName(String shuZhong) {
        this.m_SelectName = shuZhong;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        this.m_SelectName = command;
        this.m_AutoCompleteTextView.setText(command);
        if (this.m_ClickReturn) {
            this.pCallback.OnClick("确定", null);
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.ShuZhongSelect_Dialog2.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                ShuZhongSelect_Dialog2.this.initialButtons();
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: package-private */
    public class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                ShuZhongSelect_Dialog2.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
