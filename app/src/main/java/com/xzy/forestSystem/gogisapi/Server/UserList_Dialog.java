package  com.xzy.forestSystem.gogisapi.Server;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserList_Dialog {
    private XDialogTemplate _Dialog;
    private String m_AllUsersInfo;
    private boolean m_AllowMultiSelect;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private HashMap<String, Object> m_SelectItem;
    private String m_SelectUsers;
    private MyTableFactory m_TableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public UserList_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_TableFactory = null;
        this.m_MyTableDataList = null;
        this.m_SelectItem = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Server.UserList_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                HashMap tmpHash;
                try {
                    if (paramString.equals("列表选项")) {
                        UserList_Dialog.this.m_SelectItem = (HashMap) pObject;
                    } else if (paramString.equals("单击选择行")) {
                        if (!(UserList_Dialog.this.m_AllowMultiSelect || (tmpHash = (HashMap) pObject) == null)) {
                            UserList_Dialog.this.m_SelectItem = tmpHash;
                            UserList_Dialog.this.pCallback.OnClick("确定", null);
                        }
                    } else if (paramString.equals("确定")) {
                        StringBuilder tempSB = new StringBuilder();
                        for (HashMap<String, Object> temphash : UserList_Dialog.this.m_MyTableDataList) {
                            if (Boolean.parseBoolean(temphash.get("D1").toString())) {
                                if (tempSB.length() > 0) {
                                    tempSB.append(",");
                                }
                                tempSB.append(temphash.get("D2").toString());
                            }
                        }
                        if (UserList_Dialog.this.m_Callback != null) {
                            UserList_Dialog.this.m_Callback.OnClick("选择用户返回", tempSB.toString());
                        }
                        UserList_Dialog.this._Dialog.dismiss();
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_AllUsersInfo = "";
        this.m_SelectUsers = "";
        this.m_AllowMultiSelect = false;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.userlist_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("选择用户");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        ((Button) this._Dialog.findViewById(R.id.buttonSelectAll)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Server.UserList_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (UserList_Dialog.this.m_MyTableDataList != null && UserList_Dialog.this.m_MyTableDataList.size() > 0) {
                    for (HashMap<String, Object> tempHash : UserList_Dialog.this.m_MyTableDataList) {
                        tempHash.put("D1", true);
                    }
                    UserList_Dialog.this.m_TableFactory.notifyDataSetInvalidated();
                }
            }
        });
        ((Button) this._Dialog.findViewById(R.id.buttonSelectDe)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Server.UserList_Dialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (UserList_Dialog.this.m_MyTableDataList != null && UserList_Dialog.this.m_MyTableDataList.size() > 0) {
                    for (HashMap<String, Object> tempHash : UserList_Dialog.this.m_MyTableDataList) {
                        tempHash.put("D1", Boolean.valueOf(!Boolean.parseBoolean(tempHash.get("D1").toString())));
                    }
                    UserList_Dialog.this.m_TableFactory.notifyDataSetInvalidated();
                }
            }
        });
    }

    public void SetAllUsersInfo(String AllUsersInfo) {
        this.m_AllUsersInfo = AllUsersInfo;
    }

    public void SetSelectUsers(String selectUsers) {
        this.m_SelectUsers = selectUsers;
        if (!this.m_SelectUsers.endsWith(",")) {
            this.m_SelectUsers = String.valueOf(this.m_SelectUsers) + ",";
        }
    }

    public void SetAllowMultiSelect(boolean value) {
        this.m_AllowMultiSelect = value;
        if (value) {
            this._Dialog.findViewById(R.id.linearLayoutbottom).setVisibility(0);
        } else {
            this._Dialog.findViewById(R.id.linearLayoutbottom).setVisibility(8);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        try {
            if (!(this.m_AllUsersInfo == null || this.m_AllUsersInfo.equals(""))) {
                this.m_TableFactory = new MyTableFactory();
                this.m_TableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.list_users), "自定义", "选择,用户名,姓名,单位", "checkbox,text,text,text", new int[]{-15, -30, -25, -30}, this.pCallback);
                this.m_MyTableDataList = new ArrayList();
                String[] tmpStrs = this.m_AllUsersInfo.split(";");
                if (tmpStrs != null && tmpStrs.length > 0) {
                    for (String tmpStr01 : tmpStrs) {
                        String[] tmpStrs02 = tmpStr01.split(",");
                        if (tmpStrs02 != null && tmpStrs02.length > 2) {
                            HashMap tmpHashMap = new HashMap();
                            String tmpUser = tmpStrs02[0];
                            if (this.m_SelectUsers.contains(String.valueOf(tmpUser) + ",")) {
                                tmpHashMap.put("D1", true);
                            } else {
                                tmpHashMap.put("D1", false);
                            }
                            tmpHashMap.put("D2", tmpUser);
                            tmpHashMap.put("D3", tmpStrs02[1]);
                            tmpHashMap.put("D4", tmpStrs02[2]);
                            this.m_MyTableDataList.add(tmpHashMap);
                        }
                    }
                }
                this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2", "D3", "D4"}, this.pCallback);
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Server.UserList_Dialog.4
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                UserList_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
