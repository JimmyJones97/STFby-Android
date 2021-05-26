package  com.xzy.forestSystem.gogisapi.Others.DataDictionary;

import android.content.DialogInterface;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.HashMap;

public class DataDict_AddModify_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private HashMap<String, Object> m_DataObject;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public DataDict_AddModify_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_DataObject = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDict_AddModify_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (!command.equals("确定")) {
                        return;
                    }
                    if (DataDict_AddModify_Dialog.this.m_DataObject != null) {
                        String tmpZD01 = Common.GetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name);
                        String tmpZD02 = Common.GetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name2);
                        String tmpZD03 = Common.GetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name3);
                        String tmpZD04 = Common.GetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name4);
                        String tmpZDNameCode = Common.GetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name5);
                        if (tmpZD01.equals("") || tmpZD01.equals("") || tmpZD01.equals("") || tmpZD01.equals("")) {
                            Common.ShowDialog("字典条目内容不能为空.");
                            return;
                        }
                        PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().ExecuteSQL("Update T_DataDictionary Set ZDType='" + tmpZD01 + "',ZDSub='" + tmpZD02 + "',ZDName='" + tmpZD03 + "',ZDList='" + tmpZD04 + "',ZDNameCode='" + tmpZDNameCode + "' Where ZDBM='" + Common.GetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name0) + "'");
                        if (DataDict_AddModify_Dialog.this.m_Callback != null) {
                            DataDict_AddModify_Dialog.this.m_Callback.OnClick("字典编辑返回", null);
                        }
                        DataDict_AddModify_Dialog.this._Dialog.dismiss();
                        return;
                    }
                    String tmpZDBM = Common.GetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name0);
                    String tmpZD012 = Common.GetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name);
                    String tmpZD022 = Common.GetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name2);
                    String tmpZD032 = Common.GetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name3);
                    String tmpZD042 = Common.GetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name4);
                    String tmpZDNameCode2 = Common.GetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name5);
                    if (tmpZDBM.equals("") || tmpZD012.equals("") || tmpZD012.equals("") || tmpZD012.equals("") || tmpZD012.equals("")) {
                        Common.ShowDialog("字典条目内容不能为空.");
                        return;
                    }
                    boolean tmpBool = false;
                    SQLiteReader localSQLiteDataReader = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query("Select Count(*) from T_DataDictionary Where ZDBM='" + tmpZDBM + "'");
                    if (localSQLiteDataReader != null) {
                        if (localSQLiteDataReader.Read() && localSQLiteDataReader.GetInt32(0) > 0) {
                            tmpBool = true;
                        }
                        localSQLiteDataReader.Close();
                    }
                    if (tmpBool) {
                        Common.ShowDialog("字典条目编码【" + tmpZDBM + "】已经存在,请输入其他编码.");
                        return;
                    }
                    PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().ExecuteSQL("Insert Into T_DataDictionary (ZDType,ZDSub,ZDName,ZDList,ZDBM,ZDNameCode) Values ('" + tmpZD012 + "','" + tmpZD022 + "','" + tmpZD032 + "','" + tmpZD042 + "','" + tmpZDBM + "','" + tmpZDNameCode2 + "')");
                    if (DataDict_AddModify_Dialog.this.m_Callback != null) {
                        DataDict_AddModify_Dialog.this.m_Callback.OnClick("字典条目添加返回", null);
                    }
                    DataDict_AddModify_Dialog.this._Dialog.dismiss();
                } catch (Exception e) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.datadict_addmodify_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("字典条目");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
    }

    public void setDataObject(HashMap<String, Object> hash) {
        this.m_DataObject = hash;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDict_AddModify_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                if (DataDict_AddModify_Dialog.this.m_DataObject != null) {
                    DataDict_AddModify_Dialog.this._Dialog.SetCaption("字典条目编辑");
                    Common.SetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name, String.valueOf(DataDict_AddModify_Dialog.this.m_DataObject.get("D2")));
                    Common.SetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name2, String.valueOf(DataDict_AddModify_Dialog.this.m_DataObject.get("D3")));
                    Common.SetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name3, String.valueOf(DataDict_AddModify_Dialog.this.m_DataObject.get("D4")));
                    Common.SetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name5, String.valueOf(DataDict_AddModify_Dialog.this.m_DataObject.get("D5")));
                    Common.SetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name4, String.valueOf(DataDict_AddModify_Dialog.this.m_DataObject.get("D6")));
                    Common.SetEditTextValueOnID(DataDict_AddModify_Dialog.this._Dialog, R.id.et_dataDictAdd_Name0, String.valueOf(DataDict_AddModify_Dialog.this.m_DataObject.get("D7")));
                    DataDict_AddModify_Dialog.this._Dialog.findViewById(R.id.et_dataDictAdd_Name0).setEnabled(false);
                    return;
                }
                DataDict_AddModify_Dialog.this._Dialog.SetCaption("添加字典条目");
            }
        });
        this._Dialog.show();
    }
}
