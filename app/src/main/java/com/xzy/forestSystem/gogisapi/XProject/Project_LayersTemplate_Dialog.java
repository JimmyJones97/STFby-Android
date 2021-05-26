package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Config.UserConfig_LayerTemplate;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Project_LayersTemplate_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private String m_DefaultTemplateName;
    private ICallback pCallback;
    private ICallback pCallback2;

    public Project_LayersTemplate_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_DefaultTemplateName = "无";
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_LayersTemplate_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                String tempTemplateNameStr = Common.GetSpinnerValueOnID(Project_LayersTemplate_Dialog.this._Dialog, R.id.lt_name).split("【")[0];
                if (tempTemplateNameStr.contains("【")) {
                    tempTemplateNameStr = tempTemplateNameStr.split("【")[0];
                }
                if (paramString.equals("确定")) {
                    if (Common.GetCheckBoxValueOnID(Project_LayersTemplate_Dialog.this._Dialog, R.id.ck_SetDefaultTemp)) {
                        HashMap tmpHashMap = new HashMap();
                        tmpHashMap.put("F2", tempTemplateNameStr);
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_SysLayerTemplateName", (HashMap<String, String>) tmpHashMap);
                    }
                    Project_LayersTemplate_Dialog.this.m_Callback.OnClick("模板列表", tempTemplateNameStr);
                    Project_LayersTemplate_Dialog.this._Dialog.dismiss();
                } else if (!paramString.equals("删除")) {
                    Project_LayersTemplate_Dialog.this._Dialog.dismiss();
                } else if (tempTemplateNameStr.contains("无")) {
                    Common.ShowDialog("请选择有效的图层模板!");
                } else if (tempTemplateNameStr.contains("系统默认")) {
                    Common.ShowDialog("系统默认图层模板不能删除.");
                } else {
                    String finalTempTemplateNameStr = tempTemplateNameStr;
                    Common.ShowYesNoDialog(Project_LayersTemplate_Dialog.this._Dialog.getContext(), "是否删除以下图层模板?\r\n" + tempTemplateNameStr, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_LayersTemplate_Dialog.1.1
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString2, Object pObject2) {
                            if (paramString2.equals("YES")) {
                                PubVar._PubCommand.m_UserConfigDB.GetLayerTemplate().DeleteTemplateByName(finalTempTemplateNameStr);
                                Project_LayersTemplate_Dialog.this.SetDefaultTemplateName("无");
                                Project_LayersTemplate_Dialog.this.LoadTemplateListInfo();
                            }
                        }
                    });
                }
            }
        };
        this.pCallback2 = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_LayersTemplate_Dialog.2
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                String[] tempPath2;
                List<FeatureLayer> tmpList;
                if (paramString.equals("选择文件_项目模板配置文件") && (tempPath2 = pObject.toString().split(";")) != null && tempPath2.length > 1) {
                    String tmpFilePath = tempPath2[1];
                    String tmpLyrsString = Common.ReadTxtFileContent(tmpFilePath);
                    if (tmpLyrsString.length() > 0 && (tmpList = UserConfig_LayerTemplate.JSONObjectToLayerList(tmpLyrsString)) != null && tmpList.size() > 0) {
                        String tmpFileName = tmpFilePath.substring(tmpFilePath.lastIndexOf(FileSelector_Dialog.sRoot) + 1);
                        String tmpModName = tmpFileName.substring(0, tmpFileName.lastIndexOf(FileSelector_Dialog.sFolder));
                        if (!tmpModName.equals("")) {
                            HashMap tmpHashMap = new HashMap();
                            new ArrayList();
                            tmpHashMap.put("Name", tmpModName);
                            tmpHashMap.put("CreateTime", Common.GetSystemDate());
                            tmpHashMap.put("OverWrite", "true");
                            tmpHashMap.put("LayerList", tmpList);
                            PubVar._PubCommand.m_UserConfigDB.GetLayerTemplate().SaveLayerTemplate(tmpHashMap);
                            Project_LayersTemplate_Dialog.this.LoadTemplateListInfo();
                            Project_LayersTemplate_Dialog.this.LoadLayerListByTemplateName(tmpModName);
                            Common.SetValueToView(tmpModName, Project_LayersTemplate_Dialog.this._Dialog.findViewById(R.id.lt_name));
                        }
                    }
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.project_layerstemplate_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("图层模板");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.btn_layersTemplDelete).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_LayersTemplate_Dialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                Project_LayersTemplate_Dialog.this.pCallback.OnClick("删除", null);
            }
        });
        this._Dialog.findViewById(R.id.btn_LoadTemplConfig).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_LayersTemplate_Dialog.4
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                FileSelector_Dialog tempDialog = new FileSelector_Dialog(".txt;", false);
                Common.ShowToast("请选择项目模板配置文件.");
                tempDialog.SetCallback(Project_LayersTemplate_Dialog.this.pCallback2);
                tempDialog.SetTag("项目模板配置文件");
                tempDialog.ShowDialog();
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void LoadLayerListByTemplateName(String templateName) {
        new MyTableFactory();
        MyTableFactory tmpTableFactory = new MyTableFactory();
        tmpTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.prj_list), "自定义", "图层名称,图层类型", "text,text", new int[]{-50, -50}, this.pCallback);
        List localList = PubVar._PubCommand.m_UserConfigDB.GetLayerTemplate().ReadLayerTemplate(templateName);
        if (localList != null && localList.size() > 0) {
            ArrayList localArrayList = new ArrayList();
            for (Object object : localList) {
                FeatureLayer localv1_Layer = (FeatureLayer) object;
                HashMap tmpHashMap = new HashMap();
                tmpHashMap.put("D1", localv1_Layer.GetLayerName());
                tmpHashMap.put("D2", localv1_Layer.GetLayerTypeName());
                localArrayList.add(tmpHashMap);
            }
            tmpTableFactory.BindDataToListView(localArrayList);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void LoadTemplateListInfo() {
        List<String> localList = PubVar._PubCommand.m_UserConfigDB.GetLayerTemplate().ReadTemplateList("");
        localList.add(0, "无");
        Common.SetSpinnerListData(this._Dialog, "选择图层模板", localList, (int) R.id.lt_name, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_LayersTemplate_Dialog.5
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("OnItemSelected")) {
                    Project_LayersTemplate_Dialog.this.LoadLayerListByTemplateName(pObject.toString().split("【")[0]);
                }
            }
        });
        if (localList.size() > 0) {
            SetButtonEnable(true);
            for (String str : localList) {
                if (str.split("【")[0].equals(this.m_DefaultTemplateName)) {
                    Common.SetValueToView(str, this._Dialog.findViewById(R.id.lt_name));
                    return;
                }
            }
        }
    }

    private void SetButtonEnable(boolean paramBoolean) {
        this._Dialog.GetButton("1").setEnabled(paramBoolean);
    }

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public void SetDefaultTemplateName(String paramString) {
        this.m_DefaultTemplateName = paramString;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_LayersTemplate_Dialog.6
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Project_LayersTemplate_Dialog.this.LoadTemplateListInfo();
            }
        });
        this._Dialog.show();
    }
}
