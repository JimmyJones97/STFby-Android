package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VectorLayers_New_Dialog {
    private int _BKVectorLayersEditType;
    private XDialogTemplate _Dialog;
    private HashMap _EditHashMap;
    private String _LayersPath;
    private String m_AccessPassword;
    private ICallback m_Callback;
    private String m_CoorSystem;
    private String m_EncryptKey;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_TableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public VectorLayers_New_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_TableFactory = null;
        this.m_CoorSystem = "";
        this.m_EncryptKey = "";
        this.m_AccessPassword = "";
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_New_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("确定")) {
                    StringBuilder tempSB = new StringBuilder();
                    StringBuilder tempSB2 = new StringBuilder();
                    if (VectorLayers_New_Dialog.this.m_MyTableDataList.size() > 0) {
                        for (HashMap<String, Object> tempHash : VectorLayers_New_Dialog.this.m_MyTableDataList) {
                            if (Boolean.parseBoolean(tempHash.get("D1").toString())) {
                                if (tempSB.length() > 0) {
                                    tempSB.append(",");
                                }
                                tempSB.append(tempHash.get("D2").toString());
                                if (tempSB2.length() > 0) {
                                    tempSB2.append(",");
                                }
                                tempSB2.append(tempHash.get("D4").toString());
                            }
                        }
                    }
                    if (tempSB.length() == 0) {
                        Common.ShowDialog("请勾选矢量底图中需要加载的图层.");
                        return;
                    }
                    String tempBiasX = Common.GetEditTextValueOnID(VectorLayers_New_Dialog.this._Dialog, R.id.et_biasX);
                    String tempBiasY = Common.GetEditTextValueOnID(VectorLayers_New_Dialog.this._Dialog, R.id.et_biasY);
                    if (tempBiasX.equals("") || tempBiasY.equals("")) {
                        Common.ShowDialog("位移偏移值不能为空.");
                        return;
                    }
                    try {
                        Double.parseDouble(tempBiasX);
                        Double.parseDouble(tempBiasY);
                        final HashMap tempHash2 = new HashMap();
                        tempHash2.put("D1", false);
                        tempHash2.put("D2", VectorLayers_New_Dialog.this._LayersPath.substring(VectorLayers_New_Dialog.this._LayersPath.lastIndexOf(FileSelector_Dialog.sRoot) + 1));
                        tempHash2.put("D3", VectorLayers_New_Dialog.this._LayersPath);
                        tempHash2.put("D4", tempSB.toString());
                        tempHash2.put("D5", tempBiasX);
                        tempHash2.put("D6", tempBiasY);
                        tempHash2.put("D11", tempSB2.toString());
                        tempHash2.put("D12", "NEW");
                        tempHash2.put("D15", 0);
                        tempHash2.put("D16", 0);
                        if (VectorLayers_New_Dialog.this.m_EncryptKey.equals("")) {
                            tempHash2.put("IsEncrypt", "false");
                        } else {
                            tempHash2.put("IsEncrypt", "true");
                        }
                        tempHash2.put("Security", VectorLayers_New_Dialog.this.m_AccessPassword);
                        if (VectorLayers_New_Dialog.this._BKVectorLayersEditType == 0) {
                            if (!VectorLayers_New_Dialog.this.m_CoorSystem.endsWith(PubVar.m_Workspace.GetCoorSystem().GetName())) {
                                Common.ShowYesNoDialog(VectorLayers_New_Dialog.this._Dialog.getContext(), "本项目的坐标系为：" + PubVar.m_Workspace.GetCoorSystem().GetName() + "\r\n添加矢量底图的坐标系为:" + VectorLayers_New_Dialog.this.m_CoorSystem + "\r\n是否继续添加?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_New_Dialog.1.1
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String paramString2, Object pObject2) {
                                        if (paramString2.equals("YES")) {
                                            if (VectorLayers_New_Dialog.this.m_Callback != null) {
                                                VectorLayers_New_Dialog.this.m_Callback.OnClick("添加矢量底图", tempHash2);
                                            }
                                            VectorLayers_New_Dialog.this._Dialog.dismiss();
                                        }
                                    }
                                });
                                return;
                            }
                            if (VectorLayers_New_Dialog.this.m_Callback != null) {
                                VectorLayers_New_Dialog.this.m_Callback.OnClick("添加矢量底图", tempHash2);
                            }
                            VectorLayers_New_Dialog.this._Dialog.dismiss();
                        } else if (VectorLayers_New_Dialog.this._BKVectorLayersEditType == 1) {
                            if (VectorLayers_New_Dialog.this._EditHashMap != null) {
                                int tempTagInt = 0;
                                if (tempSB2.toString().equals(VectorLayers_New_Dialog.this._EditHashMap.get("D11").toString())) {
                                    tempTagInt = 1;
                                }
                                if (tempBiasX.equals(VectorLayers_New_Dialog.this._EditHashMap.get("D5").toString()) && tempBiasY.equals(VectorLayers_New_Dialog.this._EditHashMap.get("D6").toString())) {
                                    tempTagInt += 2;
                                }
                                if (tempTagInt > 0) {
                                    VectorLayers_New_Dialog.this._EditHashMap.put("D4", tempSB.toString());
                                    VectorLayers_New_Dialog.this._EditHashMap.put("D5", tempBiasX);
                                    VectorLayers_New_Dialog.this._EditHashMap.put("D6", tempBiasY);
                                    VectorLayers_New_Dialog.this._EditHashMap.put("D11", tempSB2.toString());
                                    if (tempTagInt == 1) {
                                        VectorLayers_New_Dialog.this._EditHashMap.put("D12", "EDIT1");
                                    } else if (tempTagInt == 2) {
                                        VectorLayers_New_Dialog.this._EditHashMap.put("D12", "EDIT2");
                                    } else if (tempTagInt == 3) {
                                        VectorLayers_New_Dialog.this._EditHashMap.put("D12", "EDIT3");
                                    }
                                    if (VectorLayers_New_Dialog.this.m_EncryptKey.equals("")) {
                                        VectorLayers_New_Dialog.this._EditHashMap.put("IsEncrypt", "false");
                                    } else {
                                        VectorLayers_New_Dialog.this._EditHashMap.put("IsEncrypt", "true");
                                    }
                                    VectorLayers_New_Dialog.this._EditHashMap.put("Security", VectorLayers_New_Dialog.this.m_AccessPassword);
                                    if (VectorLayers_New_Dialog.this.m_Callback != null) {
                                        VectorLayers_New_Dialog.this.m_Callback.OnClick("编辑矢量底图", VectorLayers_New_Dialog.this._EditHashMap);
                                    }
                                }
                                VectorLayers_New_Dialog.this._Dialog.dismiss();
                                return;
                            }
                            Common.ShowDialog("编辑矢量底图对象不存在.");
                        }
                    } catch (Exception e) {
                        Common.ShowDialog("请输入位移偏移值为数字.");
                    }
                }
            }
        };
        this._LayersPath = "";
        this._EditHashMap = null;
        this._BKVectorLayersEditType = 0;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.vectorlayers_new_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.m_TableFactory = new MyTableFactory();
        this.m_TableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.list_edtVectorLayers), "自定义", "选择,图层,类型,MinX,MinY,MaxX,MaxY", "checkbox,text,text,text,text,text,text", new int[]{50, 150, 50, 100, 100, 100, 100}, this.pCallback);
    }

    public void SetBKVectorLayersEditType(int type, Object object) {
        this.m_MyTableDataList = new ArrayList();
        if (type == 0) {
            this._BKVectorLayersEditType = 0;
            this._LayersPath = object.toString();
            this._LayersPath.substring(this._LayersPath.lastIndexOf(FileSelector_Dialog.sRoot) + 1);
            this._Dialog.SetCaption("添加矢量底图");
            SQLiteDBHelper tempASQLiteDatabase = new SQLiteDBHelper(this._LayersPath, this.m_EncryptKey);
            SQLiteReader tempReader = tempASQLiteDatabase.Query("Select Name,LayerID,Type,MinX,MinY,MaxX,MaxY From T_Layer Order By ID DESC");
            if (tempReader != null) {
                while (tempReader.Read()) {
                    HashMap tempHash = new HashMap();
                    tempHash.put("D1", true);
                    tempHash.put("D2", tempReader.GetString(0));
                    tempHash.put("D3", tempReader.GetString(2));
                    tempHash.put("D4", tempReader.GetString(1));
                    tempHash.put("D5", Double.valueOf(tempReader.GetDouble(3)));
                    tempHash.put("D6", Double.valueOf(tempReader.GetDouble(4)));
                    tempHash.put("D7", Double.valueOf(tempReader.GetDouble(5)));
                    tempHash.put("D8", Double.valueOf(tempReader.GetDouble(6)));
                    this.m_MyTableDataList.add(tempHash);
                }
                tempReader.Close();
            }
            StringBuilder tempSB = new StringBuilder();
            tempSB.append("文件路径: ");
            tempSB.append(this._LayersPath);
            SQLiteReader tempReader2 = tempASQLiteDatabase.Query("Select CoorType,CenterJX,FDType,DH From MapInfo limit 1");
            if (tempReader2 != null) {
                if (tempReader2.Read()) {
                    tempSB.append("\r\n坐标系: ");
                    this.m_CoorSystem = tempReader2.GetString(0);
                    tempSB.append(this.m_CoorSystem);
                    tempSB.append("\r\n中央经线: ");
                    tempSB.append(tempReader2.GetString(1));
                    tempSB.append("\r\n参数转换: ");
                    tempSB.append(tempReader2.GetString(2));
                    tempSB.append("\r\n分区带号: ");
                    tempSB.append(tempReader2.GetString(3));
                }
                tempReader2.Close();
            }
            tempASQLiteDatabase.Close();
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_vectorlayersInfo, tempSB.toString());
        } else if (type == 1) {
            this._BKVectorLayersEditType = 1;
            this._EditHashMap = (HashMap) object;
            if (this._EditHashMap.containsKey("Security")) {
                this.m_AccessPassword = String.valueOf(this._EditHashMap.get("Security"));
            }
            this._LayersPath = this._EditHashMap.get("D3").toString();
            this._EditHashMap.get("D2").toString();
            this._Dialog.SetCaption("编辑矢量底图");
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_biasX, this._EditHashMap.get("D5").toString());
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_biasY, this._EditHashMap.get("D6").toString());
            if (Common.CheckExistFile(this._LayersPath)) {
                String tempLayersID = this._EditHashMap.get("D11").toString();
                SQLiteDBHelper tempASQLiteDatabase2 = new SQLiteDBHelper(this._LayersPath, this.m_EncryptKey);
                SQLiteReader tempReader3 = tempASQLiteDatabase2.Query("Select Name,LayerID,Type,MinX,MinY,MaxX,MaxY From T_Layer");
                if (tempReader3 != null) {
                    while (tempReader3.Read()) {
                        HashMap tempHash2 = new HashMap();
                        String tempLayerID = tempReader3.GetString(1);
                        if (tempLayersID.contains(tempLayerID)) {
                            tempHash2.put("D1", true);
                        } else {
                            tempHash2.put("D1", false);
                        }
                        tempHash2.put("D2", tempReader3.GetString(0));
                        tempHash2.put("D3", tempReader3.GetString(2));
                        tempHash2.put("D4", tempLayerID);
                        tempHash2.put("D5", Double.valueOf(tempReader3.GetDouble(3)));
                        tempHash2.put("D6", Double.valueOf(tempReader3.GetDouble(4)));
                        tempHash2.put("D7", Double.valueOf(tempReader3.GetDouble(5)));
                        tempHash2.put("D8", Double.valueOf(tempReader3.GetDouble(6)));
                        this.m_MyTableDataList.add(tempHash2);
                    }
                    tempReader3.Close();
                }
                StringBuilder tempSB2 = new StringBuilder();
                tempSB2.append("文件路径: ");
                tempSB2.append(this._LayersPath);
                SQLiteReader tempReader22 = tempASQLiteDatabase2.Query("Select CoorType,CenterJX,FDType,DH From MapInfo limit 1");
                if (tempReader22 != null) {
                    if (tempReader22.Read()) {
                        tempSB2.append("\r\n坐标系: ");
                        tempSB2.append(tempReader22.GetString(0));
                        tempSB2.append("\r\n中央经线: ");
                        tempSB2.append(tempReader22.GetString(1));
                        tempSB2.append("\r\n参数转换: ");
                        tempSB2.append(tempReader22.GetString(2));
                        tempSB2.append("\r\n分区带号: ");
                        tempSB2.append(tempReader22.GetString(3));
                    }
                    tempReader22.Close();
                }
                tempASQLiteDatabase2.Close();
                Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_vectorlayersInfo, tempSB2.toString());
            } else {
                Common.ShowDialog("文件:\r\n" + this._LayersPath + "\r\n不存在.");
            }
        }
        this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2", "D3", "D5", "D6", "D7", "D8"}, this.pCallback);
    }

    public void SetEncryptKey(String value) {
        this.m_EncryptKey = value;
    }

    public void SetAccessPassword(String value) {
        this.m_AccessPassword = value;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_New_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
            }
        });
        this._Dialog.show();
    }
}
