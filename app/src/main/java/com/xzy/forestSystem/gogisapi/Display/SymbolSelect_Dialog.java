package  com.xzy.forestSystem.gogisapi.Display;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.CommonProcess;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SymbolSelect_Dialog {
    private XDialogTemplate _Dialog;
    private Object _SymbolTag;
    private ICallback m_Callback;
    private EGeoLayerType m_GeoLayerType;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private HashMap<String, Object> m_SelectItem;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public SymbolSelect_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this._SymbolTag = null;
        this.m_GeoLayerType = EGeoLayerType.NONE;
        this.m_MyTableDataList = null;
        this.m_SelectItem = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolSelect_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                String[] tempPath2;
                if (paramString.equals("返回")) {
                    return;
                }
                if (paramString.equals("列表选项")) {
                    SymbolSelect_Dialog.this.m_SelectItem = (HashMap) pObject;
                    SymbolSelect_Dialog.this.updateButtonAndImg();
                } else if (paramString.equals("确定")) {
                    if (SymbolSelect_Dialog.this.m_SelectItem != null) {
                        HashMap tmpHash = SymbolSelect_Dialog.this.m_SelectItem;
                        tmpHash.put("D0", SymbolSelect_Dialog.this._SymbolTag);
                        if (SymbolSelect_Dialog.this.m_Callback != null) {
                            SymbolSelect_Dialog.this.m_Callback.OnClick("符号库", tmpHash);
                        }
                        SymbolSelect_Dialog.this._Dialog.dismiss();
                        return;
                    }
                    Common.ShowDialog("没有选择任何符号对象.");
                } else if (paramString.equals("编辑符号")) {
                    SymbolSelect_Dialog.this.SetSelectedSymbol((HashMap) pObject);
                } else if (paramString.contains("选择文件") && (tempPath2 = pObject.toString().split(";")) != null && tempPath2.length > 1) {
                    int tempCount = tempPath2.length / 2;
                    boolean tempTag = false;
                    StringBuilder tempSB = new StringBuilder();
                    for (int tempI = 0; tempI < tempCount; tempI++) {
                        String tempfileName = tempPath2[tempI * 2];
                        int tmpI02 = tempfileName.lastIndexOf(FileSelector_Dialog.sFolder);
                        if (tmpI02 > 0) {
                            tempfileName = tempfileName.substring(0, tmpI02);
                        }
                        try {
                            String[] tmpOutMsg = new String[1];
                            if (PubVar._PubCommand.m_ConfigDB.GetSymbolManage().SaveSymbolInSystem(tempfileName, 1, CommonProcess.BitmapToBase64(BitmapFactory.decodeStream(new FileInputStream(tempPath2[(tempI * 2) + 1]))), tmpOutMsg)) {
                                tempTag = true;
                            } else {
                                tempSB.append(tmpOutMsg[0]);
                            }
                        } catch (Exception e) {
                        }
                    }
                    if (tempTag) {
                        SymbolSelect_Dialog.this.refreshSymbols();
                        return;
                    }
                    String tempStr01 = tempSB.toString();
                    if (!tempStr01.equals("")) {
                        Common.ShowToast(tempStr01);
                    }
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.symbolselect_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("符号库");
        this._Dialog.SetCallback(this.pCallback);
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        ((Button) this._Dialog.findViewById(R.id.btn_EditSymbol)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btnLoadSymbol)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_symbolDelete)).setOnClickListener(new ViewClick());
    }

    public void SetSymbolTag(Object symbolTag) {
        this._SymbolTag = symbolTag;
    }

    public void SetGeoLayerType(EGeoLayerType paramlkGeoLayerType) {
        this.m_GeoLayerType = paramlkGeoLayerType;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateButtonAndImg() {
        if (this.m_SelectItem != null) {
            HashMap tmpHashMap = this.m_SelectItem;
            this._Dialog.GetButton("1").setEnabled(true);
            this._Dialog.GetButton("2").setEnabled(true);
            ((ImageView) this._Dialog.findViewById(R.id.imageViewSymbol)).setImageBitmap((Bitmap) tmpHashMap.get("D2"));
            return;
        }
        this._Dialog.GetButton("1").setEnabled(false);
        this._Dialog.GetButton("2").setEnabled(false);
        ((ImageView) this._Dialog.findViewById(R.id.imageViewSymbol)).setImageBitmap(null);
    }

    public void SetSelectedSymbol(HashMap object) {
        this.m_SelectItem = object;
        updateButtonAndImg();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshSymbols() {
        if (this.m_GeoLayerType != EGeoLayerType.NONE) {
            if (this.m_GeoLayerType == EGeoLayerType.POINT) {
                ((Button) this._Dialog.findViewById(R.id.btnLoadSymbol)).setVisibility(0);
            } else {
                ((Button) this._Dialog.findViewById(R.id.btnLoadSymbol)).setVisibility(8);
            }
            MyTableFactory tmpTableFactory = new MyTableFactory();
            tmpTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.symbols_list), "自定义", "选择,符号名称,符号示例", "checkbox,text,image", new int[]{-15, -55, -30}, this.pCallback);
            this.m_MyTableDataList = new ArrayList();
            for (SymbolObject localSymbolObject : PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemSymbolFigure(new String[0], this.m_GeoLayerType)) {
                HashMap tmpHashMap = new HashMap();
                tmpHashMap.put("D0", false);
                tmpHashMap.put("D1", localSymbolObject.SymbolName);
                tmpHashMap.put("D2", localSymbolObject.SymbolFigure);
                tmpHashMap.put("D3", PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemSymbol(localSymbolObject.SymbolName, this.m_GeoLayerType));
                this.m_MyTableDataList.add(tmpHashMap);
            }
            tmpTableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D0", "D1", "D2"}, this.pCallback);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("编辑")) {
            SymbolEdit_Dialog tempDialog = new SymbolEdit_Dialog();
            tempDialog.SetSelectedSymbol(this.m_SelectItem);
            tempDialog.SetCallback(this.pCallback);
            tempDialog.ShowDialog();
        } else if (command.equals("导入符号")) {
            FileSelector_Dialog tempDialog2 = new FileSelector_Dialog(".png;", true);
            Common.ShowToast("请选择PNG文件.");
            tempDialog2.SetCallback(this.pCallback);
            tempDialog2.ShowDialog();
        } else if (command.equals("删除")) {
            final List<String> tempArray = new ArrayList<>();
            if (this.m_MyTableDataList != null) {
                for (HashMap tempHash : this.m_MyTableDataList) {
                    if (Boolean.valueOf(tempHash.get("D0").toString()).booleanValue()) {
                        tempArray.add(tempHash.get("D1").toString());
                    }
                }
            }
            if (tempArray.size() > 0) {
                Common.ShowYesNoDialog(this._Dialog.getContext(), "是否删除勾选的符号?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolSelect_Dialog.2
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        SymbolManage tmpSymbolManage = PubVar._PubCommand.m_ConfigDB.GetSymbolManage();
                        Iterator<String> tempIter02 = tempArray.iterator();
                        boolean tmpTag = false;
                        while (true) {
                            if (!tempIter02.hasNext()) {
                                break;
                            }
                            String[] tmpOutMsg = new String[1];
                            if (!tmpSymbolManage.DeleteSymbol(tempIter02.next(), SymbolSelect_Dialog.this.m_GeoLayerType, tmpOutMsg)) {
                                Common.ShowDialog(tmpOutMsg[0]);
                                break;
                            }
                            tmpTag = true;
                        }
                        if (tmpTag) {
                            SymbolSelect_Dialog.this.refreshSymbols();
                        }
                    }
                });
            } else {
                Common.ShowDialog("请在符号列表中勾选需要删除的符号!");
            }
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolSelect_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                SymbolSelect_Dialog.this.refreshSymbols();
            }
        });
        this._Dialog.show();
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null && view.getTag() != null) {
                SymbolSelect_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
