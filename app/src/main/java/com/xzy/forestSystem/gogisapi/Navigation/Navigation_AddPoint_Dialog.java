package  com.xzy.forestSystem.gogisapi.Navigation;

import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Navigation_AddPoint_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_ListViewDataItemList;
    private MyTableFactory m_MyTableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public Navigation_AddPoint_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.Navigation_AddPoint_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                String[] tempStrs;
                try {
                    if (command.equals("确定")) {
                        List<NavigatePoint> tempList = new ArrayList<>();
                        if (Navigation_AddPoint_Dialog.this.m_ListViewDataItemList.size() > 0) {
                            for (HashMap<String, Object> temphash : Navigation_AddPoint_Dialog.this.m_ListViewDataItemList) {
                                NavigatePoint tmpPtn = (NavigatePoint) temphash.get("D5");
                                if (tmpPtn != null) {
                                    tempList.add(tmpPtn.Clone());
                                }
                            }
                        }
                        if (Navigation_AddPoint_Dialog.this.m_Callback != null) {
                            Navigation_AddPoint_Dialog.this.m_Callback.OnClick("添加导航目标返回", tempList);
                        }
                        Navigation_AddPoint_Dialog.this._Dialog.dismiss();
                    } else if (command.equals("选择文件")) {
                        boolean tmpNeedRefresh = false;
                        String[] tempPath2 = object.toString().split(";");
                        if (tempPath2 != null && tempPath2.length > 1) {
                            try {
                                FileInputStream inputStream = new FileInputStream(tempPath2[1]);
                                InputStreamReader inputStreamReader = null;
                                try {
                                    inputStreamReader = new InputStreamReader(inputStream, "gbk");
                                } catch (UnsupportedEncodingException e1) {
                                    e1.printStackTrace();
                                }
                                BufferedReader reader = new BufferedReader(inputStreamReader);
                                int tid = 0;
                                while (true) {
                                    try {
                                        String line = reader.readLine();
                                        if (line == null) {
                                            break;
                                        } else if (!line.equals("") && (tempStrs = line.split(",")) != null && tempStrs.length > 1) {
                                            double tmpX = Double.parseDouble(tempStrs[0]);
                                            double tmpY = Double.parseDouble(tempStrs[1]);
                                            tid++;
                                            NavigatePoint tmpPtn2 = new NavigatePoint();
                                            tmpPtn2.SetName("文件中点" + tid);
                                            Coordinate tmpCoord = new Coordinate(tmpX, tmpY);
                                            tmpPtn2.SetCoordinate(tmpCoord);
                                            HashMap tempHashMap = new HashMap();
                                            tempHashMap.put("D1", false);
                                            tempHashMap.put("D2", tmpPtn2._Name);
                                            tempHashMap.put("D3", Double.valueOf(tmpCoord.getX()));
                                            tempHashMap.put("D4", Double.valueOf(tmpCoord.getY()));
                                            tempHashMap.put("D5", tmpPtn2);
                                            Navigation_AddPoint_Dialog.this.m_ListViewDataItemList.add(tempHashMap);
                                            tmpNeedRefresh = true;
                                        }
                                    } catch (IOException e) {
                                    }
                                }
                                inputStream.close();
                            } catch (Exception e2) {
                            }
                            if (tmpNeedRefresh) {
                                Navigation_AddPoint_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (Exception e3) {
                }
            }
        };
        this.m_ListViewDataItemList = null;
        this.m_MyTableFactory = new MyTableFactory();
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.navigation_addpoint_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("添加导航目标");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.btn_NavAddPtn_Add).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_NavAddPtn_FromMeasure).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_NavAddPtn_FromFile).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_NavAddPtn_Delete).setOnClickListener(new ViewClick());
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        this.m_ListViewDataItemList = new ArrayList();
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.navPtns_list), "自定义", "选择,点名称,X坐标,Y坐标", "checkbox,text,text,text", new int[]{-15, -30, -28, -27}, this.pCallback);
        this.m_MyTableFactory.BindDataToListView(this.m_ListViewDataItemList, new String[]{"D1", "D2", "D3", "D4"}, this.pCallback);
        if (PubVar._PubCommand.m_Measure.GetLastCoordinate() == null) {
            this._Dialog.findViewById(R.id.btn_NavAddPtn_FromMeasure).setEnabled(false);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("添加导航点")) {
                String tempStr01 = Common.GetEditTextValueOnID(this._Dialog, R.id.et_NavAddPtn_Name);
                String tempStr02 = Common.GetEditTextValueOnID(this._Dialog, R.id.et_NavAddPtn_X);
                String tempStr03 = Common.GetEditTextValueOnID(this._Dialog, R.id.et_NavAddPtn_Y);
                if (tempStr01.equals("") || tempStr02.equals("") || tempStr03.equals("")) {
                    Common.ShowDialog("导航点名称及坐标值不能为空.");
                    return;
                }
                double tmpX = Double.parseDouble(tempStr02);
                double tmpY = Double.parseDouble(tempStr03);
                Coordinate tmpCoord = new Coordinate(tmpX, tmpY);
                if (tmpX >= -180.0d && tmpX <= 180.0d && tmpY >= -90.0d && tmpY <= 90.0d) {
                    tmpCoord = PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(tmpX, tmpY, 0.0d, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                }
                NavigatePoint tmpPtn = new NavigatePoint();
                tmpPtn.SetName(tempStr01);
                tmpPtn.SetCoordinate(tmpCoord);
                HashMap tempHashMap = new HashMap();
                tempHashMap.put("D1", false);
                tempHashMap.put("D2", tmpPtn._Name);
                tempHashMap.put("D3", Double.valueOf(tmpCoord.getX()));
                tempHashMap.put("D4", Double.valueOf(tmpCoord.getY()));
                tempHashMap.put("D5", tmpPtn);
                this.m_ListViewDataItemList.add(tempHashMap);
                this.m_MyTableFactory.notifyDataSetChanged();
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_NavAddPtn_Name, "");
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_NavAddPtn_X, "");
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_NavAddPtn_Y, "");
            } else if (command.equals("测量获取")) {
                Coordinate tempCoord = PubVar._PubCommand.m_Measure.GetLastCoordinate();
                if (tempCoord == null) {
                    Common.ShowDialog("没有使用测量工具,请先利用【测量】工具测量点或直线.");
                    return;
                }
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_NavAddPtn_Name, "测量点");
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_NavAddPtn_X, String.valueOf(tempCoord.getX()));
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_NavAddPtn_Y, String.valueOf(tempCoord.getY()));
            } else if (command.equals("导入文件")) {
                FileSelector_Dialog tempDialog = new FileSelector_Dialog(".txt;.csv;", false);
                Common.ShowToast("请选择坐标文件(txt或csv文件).");
                tempDialog.SetCallback(this.pCallback);
                tempDialog.ShowDialog();
            } else if (command.equals("删除")) {
                final List tmpDeleteList = new ArrayList();
                if (this.m_ListViewDataItemList.size() > 0) {
                    for (HashMap<String, Object> temphash : this.m_ListViewDataItemList) {
                        if (Boolean.parseBoolean(String.valueOf(temphash.get("D1")))) {
                            tmpDeleteList.add(temphash);
                        }
                    }
                }
                if (tmpDeleteList.size() > 0) {
                    Common.ShowYesNoDialog(this._Dialog.getContext(), "是否删除选择的导航点?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.Navigation_AddPoint_Dialog.2
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (paramString.equals("YES")) {
                                int count = tmpDeleteList.size();
                                for (int i = 0; i < count; i++) {
                                    Navigation_AddPoint_Dialog.this.m_ListViewDataItemList.remove(tmpDeleteList.get(i));
                                }
                                Navigation_AddPoint_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                            }
                        }
                    });
                } else {
                    Common.ShowDialog("没有选择任何对象.");
                }
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.Navigation_AddPoint_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Navigation_AddPoint_Dialog.this.refreshLayout();
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
                Navigation_AddPoint_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
