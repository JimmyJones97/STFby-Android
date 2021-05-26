package  com.xzy.forestSystem.gogisapi.Display;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleSymbol_Select_Dialog {
    private XDialogTemplate _Dialog;
    private HashMap _selectObject;
    private int _symbolType;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public SimpleSymbol_Select_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_MyTableDataList = null;
        this._selectObject = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Display.SimpleSymbol_Select_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("单击选择行")) {
                    SimpleSymbol_Select_Dialog.this._selectObject = (HashMap) pObject;
                } else if (!paramString.equals("确定")) {
                } else {
                    if (SimpleSymbol_Select_Dialog.this._selectObject != null) {
                        if (SimpleSymbol_Select_Dialog.this.m_Callback != null) {
                            SimpleSymbol_Select_Dialog.this.m_Callback.OnClick("简单符号样式选择", SimpleSymbol_Select_Dialog.this._selectObject);
                        }
                        SimpleSymbol_Select_Dialog.this._Dialog.dismiss();
                        return;
                    }
                    Common.ShowDialog("没有选择符号样式.");
                }
            }
        };
        this._symbolType = 0;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.x_symbol_style_select_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("基础符号库");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
    }

    public void setSymbolType(int symbolType) {
        this._symbolType = symbolType;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshList() {
        this.m_MyTableDataList = new ArrayList();
        if (this._symbolType > 0) {
            MyTableFactory tmpTableFactory = new MyTableFactory();
            tmpTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.symbolstyles_list), "自定义", "符号样式", "image", new int[]{-100}, this.pCallback);
            if (this._symbolType == 1) {
                this._Dialog.SetCaption("点符号");
                HashMap tmpHashMap = new HashMap();
                tmpHashMap.put("D1", BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.point_symbol_circle48));
                tmpHashMap.put("D2", 0);
                this.m_MyTableDataList.add(tmpHashMap);
                HashMap tmpHashMap2 = new HashMap();
                tmpHashMap2.put("D1", BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.point_symbol_rect48));
                tmpHashMap2.put("D2", 1);
                this.m_MyTableDataList.add(tmpHashMap2);
                HashMap tmpHashMap3 = new HashMap();
                tmpHashMap3.put("D1", BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.point_symbol_tri48));
                tmpHashMap3.put("D2", 2);
                this.m_MyTableDataList.add(tmpHashMap3);
            } else if (this._symbolType == 3) {
                this._Dialog.SetCaption("线符号");
                HashMap tmpHashMap4 = new HashMap();
                tmpHashMap4.put("D1", BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.linstyle01));
                tmpHashMap4.put("D2", 0);
                this.m_MyTableDataList.add(tmpHashMap4);
                HashMap tmpHashMap5 = new HashMap();
                tmpHashMap5.put("D1", BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.linestyle02));
                tmpHashMap5.put("D2", 1);
                this.m_MyTableDataList.add(tmpHashMap5);
                HashMap tmpHashMap6 = new HashMap();
                tmpHashMap6.put("D1", BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.linstyle03));
                tmpHashMap6.put("D2", 2);
                this.m_MyTableDataList.add(tmpHashMap6);
            }
            tmpTableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1"}, this.pCallback);
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Display.SimpleSymbol_Select_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                SimpleSymbol_Select_Dialog.this.refreshList();
            }
        });
        this._Dialog.show();
    }
}
