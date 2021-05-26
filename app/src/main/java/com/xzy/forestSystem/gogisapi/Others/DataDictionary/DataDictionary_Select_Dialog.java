package  com.xzy.forestSystem.gogisapi.Others.DataDictionary;

import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.MyControls.ListValues_Dialog;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;

public class DataDictionary_Select_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private SpinnerList m_Spinner01;
    private SpinnerList m_Spinner02;
    private SpinnerList m_Spinner03;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public DataDictionary_Select_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDictionary_Select_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                Object[] tmpObjs;
                if (command.equals("确定")) {
                    String tempStr = "";
                    String tempMainType = Common.GetViewValue(DataDictionary_Select_Dialog.this.m_Spinner01);
                    String tempMainType2 = Common.GetViewValue(DataDictionary_Select_Dialog.this.m_Spinner02);
                    String tempMainType3 = Common.GetViewValue(DataDictionary_Select_Dialog.this.m_Spinner03);
                    if (!(tempMainType == null || tempMainType.equals("") || tempMainType2 == null || tempMainType2.equals("") || tempMainType3 == null || tempMainType3.equals("") || (tmpObjs = DataDictionary_Select_Dialog.QueryDictionary(tempMainType, tempMainType2, tempMainType3)) == null)) {
                        tempStr = String.valueOf(tmpObjs[2]);
                    }
                    if (DataDictionary_Select_Dialog.this.m_Callback != null) {
                        DataDictionary_Select_Dialog.this.m_Callback.OnClick("数据字典选择返回", tempStr);
                    }
                    DataDictionary_Select_Dialog.this._Dialog.dismiss();
                } else if (command.equals("重置")) {
                    Common.SetValueToView("", DataDictionary_Select_Dialog.this.m_Spinner01);
                    Common.SetValueToView("", DataDictionary_Select_Dialog.this.m_Spinner02);
                    Common.SetValueToView("", DataDictionary_Select_Dialog.this.m_Spinner03);
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.datadictionary_select_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("数据字典");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.btn_dictReset).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDictionary_Select_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                DataDictionary_Select_Dialog.this.pCallback.OnClick("重置", null);
            }
        });
        this.m_Spinner01 = (SpinnerList) this._Dialog.findViewById(R.id.sp_dd_dictType);
        this.m_Spinner02 = (SpinnerList) this._Dialog.findViewById(R.id.sp_dd_mainClass);
        this.m_Spinner03 = (SpinnerList) this._Dialog.findViewById(R.id.sp_dd_secondClass);
        this.m_Spinner01.SetSelectReturnTag("SpinnerCallback");
        this.m_Spinner02.SetSelectReturnTag("SpinnerCallback");
        this.m_Spinner03.SetSelectReturnTag("SpinnerCallback");
        this.m_Spinner01.SetClickCallback(true);
        this.m_Spinner02.SetClickCallback(true);
        this.m_Spinner03.SetClickCallback(true);
        this.m_Spinner01.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDictionary_Select_Dialog.3
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("SpinnerCallback")) {
                    List<String> tempList = new ArrayList<>();
                    SQLiteReader tempReader = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query("Select Distinct ZDType From T_DataDictionary");
                    if (tempReader != null) {
                        while (tempReader.Read()) {
                            String tempStr = tempReader.GetString(0);
                            if (tempStr != null && !tempStr.equals("")) {
                                tempList.add(tempStr);
                            }
                        }
                        tempReader.Close();
                    }
                    if (tempList.size() > 0) {
                        ListValues_Dialog tempDialog = new ListValues_Dialog();
                        tempDialog.ClickItemReturn = true;
                        tempDialog.SetItems(tempList, 1, "字典类别");
                        tempDialog.SetText("数据字典");
                        tempDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDictionary_Select_Dialog.3.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject2) {
                                String tmpStr;
                                if (paramString2.equals("值选择返回") && (tmpStr = String.valueOf(pObject2)) != null) {
                                    DataDictionary_Select_Dialog.this.m_Spinner01.SetTextJust(tmpStr);
                                }
                            }
                        });
                        tempDialog.ShowDialog();
                        return;
                    }
                    Common.ShowDialog("系统中没有数据字典数据.");
                }
            }
        });
        this.m_Spinner02.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDictionary_Select_Dialog.4
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("SpinnerCallback")) {
                    String tempMainType = Common.GetViewValue(DataDictionary_Select_Dialog.this.m_Spinner01);
                    if (tempMainType == null || tempMainType.equals("")) {
                        Common.ShowDialog("请先选择字典类别.");
                        return;
                    }
                    List<String> tempList = new ArrayList<>();
                    SQLiteReader tempReader = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query("Select Distinct ZDSub From T_DataDictionary Where ZDType='" + tempMainType + "'");
                    if (tempReader != null) {
                        while (tempReader.Read()) {
                            String tempStr = tempReader.GetString(0);
                            if (tempStr != null && !tempStr.equals("")) {
                                tempList.add(tempStr);
                            }
                        }
                        tempReader.Close();
                    }
                    if (tempList.size() > 0) {
                        ListValues_Dialog tempDialog = new ListValues_Dialog();
                        tempDialog.ClickItemReturn = true;
                        tempDialog.SetItems(tempList, 1, "条目大类");
                        tempDialog.SetText("条目大类");
                        tempDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDictionary_Select_Dialog.4.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject2) {
                                String tmpStr;
                                if (paramString2.equals("值选择返回") && (tmpStr = String.valueOf(pObject2)) != null) {
                                    DataDictionary_Select_Dialog.this.m_Spinner02.SetTextJust(tmpStr);
                                }
                            }
                        });
                        tempDialog.ShowDialog();
                        return;
                    }
                    Common.ShowDialog("系统中没有字典类别【" + tempMainType + "】对应的条目大类数据.");
                }
            }
        });
        this.m_Spinner03.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDictionary_Select_Dialog.5
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("SpinnerCallback")) {
                    final String tempMainType = Common.GetViewValue(DataDictionary_Select_Dialog.this.m_Spinner01);
                    if (tempMainType == null || tempMainType.equals("")) {
                        Common.ShowDialog("请先选择字典类别.");
                        return;
                    }
                    final String tempSubType = Common.GetViewValue(DataDictionary_Select_Dialog.this.m_Spinner02);
                    if (tempSubType == null || tempSubType.equals("")) {
                        Common.ShowDialog("请先选择条目大类.");
                        return;
                    }
                    List<String> tempList = new ArrayList<>();
                    SQLiteReader tempReader = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query("Select Distinct ZDName,ZDList,ZDBM From T_DataDictionary Where ZDType='" + tempMainType + "' And ZDSub='" + tempSubType + "'");
                    if (tempReader != null) {
                        while (tempReader.Read()) {
                            String tempStr = tempReader.GetString(0);
                            if (tempStr != null && !tempStr.equals("")) {
                                tempList.add(tempStr);
                            }
                        }
                        tempReader.Close();
                    }
                    if (tempList.size() > 0) {
                        ListValues_Dialog tempDialog = new ListValues_Dialog();
                        tempDialog.ClickItemReturn = true;
                        tempDialog.SetItems(tempList, 1, "条目细类");
                        tempDialog.SetText("条目细类");
                        tempDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDictionary_Select_Dialog.5.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject2) {
                                String tmpStr;
                                if (paramString2.equals("值选择返回") && (tmpStr = String.valueOf(pObject2)) != null) {
                                    DataDictionary_Select_Dialog.this.m_Spinner03.SetTextJust(tmpStr);
                                    Object[] tmpObjs = DataDictionary_Select_Dialog.QueryDictionary(tempMainType, tempSubType, tmpStr);
                                    if (tmpObjs != null) {
                                        Common.SetTextViewValueOnID(DataDictionary_Select_Dialog.this._Dialog, (int) R.id.tv_dictValueList, String.valueOf(tmpObjs[1]));
                                    }
                                }
                            }
                        });
                        tempDialog.ShowDialog();
                        return;
                    }
                    Common.ShowDialog("系统中字典类别【" + tempMainType + "】-条目大类【" + tempSubType + "】下没有条目细类数据.");
                }
            }
        });
    }

    public static Object[] QueryDictionary(String typeName, String subName, String dictName) {
        Object[] result = null;
        SQLiteReader tempReader = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query("Select Distinct ZDList,ZDBM From T_DataDictionary Where ZDType='" + typeName + "' And ZDSub='" + subName + "' And ZDName='" + dictName + "'");
        if (tempReader != null) {
            if (tempReader.Read()) {
                result = new Object[]{dictName, tempReader.GetString(0), tempReader.GetString(1)};
            }
            tempReader.Close();
        }
        return result;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDictionary_Select_Dialog.6
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
            }
        });
        this._Dialog.show();
    }
}
