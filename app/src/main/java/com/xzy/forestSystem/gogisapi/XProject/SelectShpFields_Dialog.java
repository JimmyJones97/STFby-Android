package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Shapefile;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectShpFields_Dialog {
    public String ShpFilePath;
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private List<LayerField> m_LayerFields;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_TableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public SelectShpFields_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.ShpFilePath = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_TableFactory = null;
        this.m_LayerFields = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.SelectShpFields_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                Object tmpObj;
                try {
                    if (!command.equals("确定")) {
                        return;
                    }
                    if (SelectShpFields_Dialog.this.m_LayerFields == null || SelectShpFields_Dialog.this.m_LayerFields.size() == 0) {
                        Common.ShowDialog("选择的Shp文件无法获取属性字段信息.");
                        SelectShpFields_Dialog.this._Dialog.dismiss();
                        return;
                    }
                    List<LayerField> tmpFields = new ArrayList<>();
                    for (HashMap<String, Object> tmpHash : SelectShpFields_Dialog.this.m_MyTableDataList) {
                        if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1"))) && (tmpObj = tmpHash.get("D6")) != null && (tmpObj instanceof LayerField)) {
                            tmpFields.add((LayerField) tmpObj);
                        }
                    }
                    if (tmpFields.size() == 0) {
                        Common.ShowDialog("请勾选需要加载的字段.");
                        return;
                    }
                    if (SelectShpFields_Dialog.this.m_Callback != null) {
                        SelectShpFields_Dialog.this.m_Callback.OnClick("选择SHP模板返回", tmpFields);
                    }
                    SelectShpFields_Dialog.this._Dialog.dismiss();
                } catch (Exception e) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.selectshpfields_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("加载Shp字段");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.m_TableFactory = new MyTableFactory();
        this.m_TableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.list_fields), "自定义", "选择,名称,类型,长度,数据字典", "checkbox,text,text,text,text", new int[]{-15, -35, -15, -15, -20}, this.pCallback);
        ((Button) this._Dialog.findViewById(R.id.buttonSelectAll)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.SelectShpFields_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (SelectShpFields_Dialog.this.m_MyTableDataList != null && SelectShpFields_Dialog.this.m_MyTableDataList.size() > 0) {
                    for (HashMap<String, Object> tempHash : SelectShpFields_Dialog.this.m_MyTableDataList) {
                        tempHash.put("D1", true);
                    }
                    SelectShpFields_Dialog.this.m_TableFactory.notifyDataSetInvalidated();
                }
            }
        });
        ((Button) this._Dialog.findViewById(R.id.buttonSelectDe)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.SelectShpFields_Dialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (SelectShpFields_Dialog.this.m_MyTableDataList != null && SelectShpFields_Dialog.this.m_MyTableDataList.size() > 0) {
                    for (HashMap<String, Object> tempHash : SelectShpFields_Dialog.this.m_MyTableDataList) {
                        tempHash.put("D1", Boolean.valueOf(!Boolean.parseBoolean(tempHash.get("D1").toString())));
                    }
                    SelectShpFields_Dialog.this.m_TableFactory.notifyDataSetInvalidated();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshList() {
        try {
            if (this.ShpFilePath != null && new File(this.ShpFilePath).exists()) {
                this.m_MyTableDataList.clear();
                String tempDBFPath = String.valueOf(this.ShpFilePath.substring(0, this.ShpFilePath.lastIndexOf(FileSelector_Dialog.sFolder))) + ".dbf";
                if (new File(tempDBFPath).exists()) {
                    Shapefile tmpShp = new Shapefile();
                    if (tmpShp.readDBF(tempDBFPath, false, null)) {
                        this.m_LayerFields = tmpShp.getFieldsList();
                        if (this.m_LayerFields != null && this.m_LayerFields.size() > 0) {
                            for (LayerField tmpField : this.m_LayerFields) {
                                HashMap tmpHashMap = new HashMap();
                                tmpHashMap.put("D1", false);
                                tmpHashMap.put("D2", tmpField.GetFieldName());
                                tmpHashMap.put("D3", tmpField.GetFieldTypeName());
                                tmpHashMap.put("D4", Integer.valueOf(tmpField.GetFieldSize()));
                                tmpHashMap.put("D5", tmpField.GetFieldEnumCode());
                                tmpHashMap.put("D6", tmpField);
                                this.m_MyTableDataList.add(tmpHashMap);
                            }
                        }
                    }
                }
                this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2", "D3", "D4", "D5"}, this.pCallback);
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.SelectShpFields_Dialog.4
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                SelectShpFields_Dialog.this.refreshList();
            }
        });
        this._Dialog.show();
    }
}
