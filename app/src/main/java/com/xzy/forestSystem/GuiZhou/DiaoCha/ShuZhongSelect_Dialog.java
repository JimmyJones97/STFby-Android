package com.xzy.forestSystem.GuiZhou.DiaoCha;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;

public class ShuZhongSelect_Dialog {
    private XDialogTemplate _Dialog;
    private List<List<Integer>> m_BtnIDLists;
    private ICallback m_Callback;
    private boolean m_ClickReturn;
    private String m_SelectName;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public ShuZhongSelect_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_BtnIDLists = null;
        this.m_SelectName = "";
        this.m_ClickReturn = true;
        this.pCallback = new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.ShuZhongSelect_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("确定")) {
                    if (ShuZhongSelect_Dialog.this.m_Callback != null) {
                        ShuZhongSelect_Dialog.this.m_Callback.OnClick("选择树种返回", ShuZhongSelect_Dialog.this.m_SelectName);
                    }
                    ShuZhongSelect_Dialog.this._Dialog.dismiss();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.shuzhongselect_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("选择树种");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.m_BtnIDLists = new ArrayList();
        List<Integer> tmpList01 = new ArrayList<>();
        tmpList01.add(Integer.valueOf((int) R.id.btn_ShuZhong01_01));
        tmpList01.add(Integer.valueOf((int) R.id.btn_ShuZhong01_02));
        this.m_BtnIDLists.add(tmpList01);
        List<Integer> tmpList012 = new ArrayList<>();
        tmpList012.add(Integer.valueOf((int) R.id.btn_ShuZhong02_01));
        tmpList012.add(Integer.valueOf((int) R.id.btn_ShuZhong02_02));
        tmpList012.add(Integer.valueOf((int) R.id.btn_ShuZhong02_03));
        this.m_BtnIDLists.add(tmpList012);
        List<Integer> tmpList013 = new ArrayList<>();
        tmpList013.add(Integer.valueOf((int) R.id.btn_ShuZhong03_01));
        tmpList013.add(Integer.valueOf((int) R.id.btn_ShuZhong03_02));
        this.m_BtnIDLists.add(tmpList013);
        List<Integer> tmpList014 = new ArrayList<>();
        tmpList014.add(Integer.valueOf((int) R.id.btn_ShuZhong04_01));
        tmpList014.add(Integer.valueOf((int) R.id.btn_ShuZhong04_02));
        this.m_BtnIDLists.add(tmpList014);
        List<Integer> tmpList015 = new ArrayList<>();
        tmpList015.add(Integer.valueOf((int) R.id.btn_ShuZhong05_01));
        tmpList015.add(Integer.valueOf((int) R.id.btn_ShuZhong05_02));
        tmpList015.add(Integer.valueOf((int) R.id.btn_ShuZhong05_03));
        tmpList015.add(Integer.valueOf((int) R.id.btn_ShuZhong05_04));
        tmpList015.add(Integer.valueOf((int) R.id.btn_ShuZhong05_05));
        tmpList015.add(Integer.valueOf((int) R.id.btn_ShuZhong05_06));
        this.m_BtnIDLists.add(tmpList015);
        List<Integer> tmpList016 = new ArrayList<>();
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_01));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_02));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_03));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_04));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_05));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_06));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_07));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_08));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_09));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_10));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_11));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_12));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_13));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_14));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_15));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_16));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_17));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_18));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_19));
        tmpList016.add(Integer.valueOf((int) R.id.btn_ShuZhong06_20));
        this.m_BtnIDLists.add(tmpList016);
        List<Integer> tmpList017 = new ArrayList<>();
        tmpList017.add(Integer.valueOf((int) R.id.btn_ShuZhong07_01));
        tmpList017.add(Integer.valueOf((int) R.id.btn_ShuZhong07_02));
        tmpList017.add(Integer.valueOf((int) R.id.btn_ShuZhong07_03));
        tmpList017.add(Integer.valueOf((int) R.id.btn_ShuZhong07_04));
        tmpList017.add(Integer.valueOf((int) R.id.btn_ShuZhong07_05));
        tmpList017.add(Integer.valueOf((int) R.id.btn_ShuZhong07_06));
        tmpList017.add(Integer.valueOf((int) R.id.btn_ShuZhong07_07));
        tmpList017.add(Integer.valueOf((int) R.id.btn_ShuZhong07_08));
        this.m_BtnIDLists.add(tmpList017);
        for (List<Integer> tmpList : this.m_BtnIDLists) {
            for (Integer tmpInteger : tmpList) {
                View tmpView = this._Dialog.findViewById(tmpInteger.intValue());
                if (tmpView.getTag() != null) {
                    tmpView.setOnClickListener(new ViewClick());
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void switchButtonStyle(String exceptName) {
        Drawable tmpDefaultDrawable = this._Dialog.getContext().getResources().getDrawable(R.drawable.shuzhongbuttondefault);
        Drawable tmpSelectDrawable = this._Dialog.getContext().getResources().getDrawable(R.drawable.shuzhongbuttonselector);
        for (List<Integer> tmpList : this.m_BtnIDLists) {
            for (Integer tmpInteger : tmpList) {
                View tmpView = this._Dialog.findViewById(tmpInteger.intValue());
                if (tmpView.getTag() == null || !String.valueOf(tmpView.getTag()).equals(exceptName)) {
                    tmpView.setBackgroundDrawable(tmpDefaultDrawable);
                } else {
                    tmpView.setBackgroundDrawable(tmpSelectDrawable);
                }
            }
        }
    }

    public void SetSelectName(String shuZhong) {
        this.m_SelectName = shuZhong;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        this.m_SelectName = command;
        if (this.m_ClickReturn) {
            this.pCallback.OnClick("确定", null);
        } else {
            switchButtonStyle(this.m_SelectName);
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.ShuZhongSelect_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                ShuZhongSelect_Dialog.this.switchButtonStyle(ShuZhongSelect_Dialog.this.m_SelectName);
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
                ShuZhongSelect_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
