package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.BaseAdapter;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LayerModule_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_TableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public LayerModule_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.LayerModule_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                String[] tmpStrs;
                try {
                    if (paramString.equals("确定")) {
                        List<String> tmpList = new ArrayList<>();
                        if (LayerModule_Dialog.this.m_MyTableDataList != null) {
                            for (HashMap<String, Object> temphash : LayerModule_Dialog.this.m_MyTableDataList) {
                                if (Boolean.parseBoolean(temphash.get("D1").toString())) {
                                    tmpList.add(String.valueOf(temphash.get("D2")));
                                }
                            }
                        }
                        if (tmpList.size() <= 0) {
                            Common.ShowDialog("没有选择任何图层模板.");
                        } else if (tmpList.size() == 1) {
                            if (LayerModule_Dialog.this.m_Callback != null) {
                                LayerModule_Dialog.this.m_Callback.OnClick("图层模板选择返回", tmpList.get(0).toString());
                            }
                            LayerModule_Dialog.this._Dialog.dismiss();
                        } else {
                            Common.ShowDialog("共选择了" + String.valueOf(tmpList.size()) + "个图层模板.\r\n请勾选一个图层模板即可.");
                        }
                    } else if (paramString.equals("单击单元格")) {
                        String tmpStr = String.valueOf(pObject);
                        if (!(tmpStr == null || tmpStr.equals("") || (tmpStrs = tmpStr.split(",")) == null || tmpStrs.length <= 1 || Integer.parseInt(tmpStrs[1]) != 4)) {
                            LayerModule_Dialog.this.pCallback.OnClick("删除图层模板", null);
                        }
                    } else if (paramString.equals("删除图层模板")) {
                        final ArrayList localArrayList = new ArrayList();
                        for (HashMap tmpHashMap : LayerModule_Dialog.this.m_MyTableDataList) {
                            if (Boolean.parseBoolean(tmpHashMap.get("D1").toString())) {
                                localArrayList.add(tmpHashMap.get("D2").toString());
                            }
                        }
                        if (localArrayList.size() > 0) {
                            Common.ShowYesNoDialog(LayerModule_Dialog.this._Dialog.getContext(), "是否确定要删除以下图层模板？\r\n" + Common.CombineStrings("\r\n", localArrayList), new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.LayerModule_Dialog.1.1
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String paramString2, Object pObject2) {
                                    if (paramString2.equals("YES")) {
                                        PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().ExecuteSQL("Delete From Layer_Template Where LayerName in ('" + Common.CombineStrings("','", localArrayList) + "')");
                                        LayerModule_Dialog.this.readLayerModules();
                                        LayerModule_Dialog.this.refreshList();
                                    }
                                }
                            });
                        } else {
                            Common.ShowDialog(LayerModule_Dialog.this._Dialog.getContext(), "请勾选需要删除的项目！");
                        }
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_MyTableDataList = null;
        this.m_TableFactory = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.layermodule_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("图层模板");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayersList() {
        this.m_MyTableDataList = new ArrayList();
        this.m_TableFactory = new MyTableFactory();
        this.m_TableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.layerModules_list), "自定义", "选择,图层模板名称,创建时间,类型,操作", "checkbox,text,text,text,image", new int[]{50, 150, 150, 50, 50}, this.pCallback);
        readLayerModules();
        this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2", "D3", "D4", "D5"}, this.pCallback);
        this.m_TableFactory.SetClickItemReturnColumns("1,4,");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshList() {
        try {
            ((BaseAdapter) this.m_TableFactory.getListView_Scroll().getAdapter()).notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void readLayerModules() {
        try {
            this.m_MyTableDataList.clear();
            Bitmap tmpDeleteBmp = BitmapFactory.decodeResource(PubVar._PubCommand.m_Context.getResources(), R.drawable.deleteobject);
            SQLiteReader tmpReader = PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().Query("Select LayerName,CreateTime,Type From Layer_Template");
            if (tmpReader != null) {
                while (tmpReader.Read()) {
                    HashMap tmpHashMap = new HashMap();
                    tmpHashMap.put("D1", false);
                    tmpHashMap.put("D2", tmpReader.GetString("LayerName"));
                    tmpHashMap.put("D3", tmpReader.GetString("CreateTime"));
                    tmpHashMap.put("D4", tmpReader.GetString("Type"));
                    tmpHashMap.put("D5", tmpDeleteBmp);
                    this.m_MyTableDataList.add(tmpHashMap);
                }
                tmpReader.Close();
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.LayerModule_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                LayerModule_Dialog.this.refreshLayersList();
            }
        });
        this._Dialog.show();
    }
}
