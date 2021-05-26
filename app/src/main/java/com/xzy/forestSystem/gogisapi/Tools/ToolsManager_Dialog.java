package  com.xzy.forestSystem.gogisapi.Tools;

import android.os.Environment;
import android.view.View;
import com.xzy.forestSystem.GuiZhou.DiaoCha.CalXuJiSingle_Dialog2;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.GPSParaCalculate_Dialog;
import  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityManage_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDict_Manage_Dialog;
import  com.xzy.forestSystem.gogisapi.Others.Project_Shift_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.Date;

public class ToolsManager_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private int m_ToolboxType;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public ToolsManager_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_ToolboxType = 0;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.ToolsManager_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.toolsmanager_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("更多工具");
        this._Dialog.HideHeadBar();
        this._Dialog.setCanceledOnTouchOutside(true);
        this._Dialog.findViewById(R.id.button_toolbox_exit).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_save).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_checkupdate).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_basemapCache).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_parasMang).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_About).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_bluetoothSet).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_setting).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_export).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_CalXujiSingle).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_dataDict).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_calculator).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_measureHeight).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_measureDistance).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_captureScreen).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_metalDiscovery).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_pressure).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_GPSParaCal).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_Help).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_BakPrj).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_RasterConvert).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_toolbox_ExportPhoto).setOnClickListener(new ViewClick());
    }

    public void SetToolboxType(int type) {
        this.m_ToolboxType = type;
    }

    public void ShowDialog() {
        if (this.m_ToolboxType == 1) {
            this._Dialog.findViewById(R.id.tableRow_project_data).setVisibility(8);
        }
        this._Dialog.show();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("退出系统")) {
            PubVar._PubCommand.ProcessCommand("退出系统");
        } else if (command.equals("保存数据")) {
            PubVar._PubCommand.ProcessCommand("保存");
        } else if (command.equals("系统更新")) {
            PubVar._PubCommand.ProcessCommand("系统更新");
        } else if (command.equals("地图缓存")) {
            PubVar._PubCommand.ProcessCommand("地图缓存");
        } else if (command.equals("蓝牙设置")) {
            PubVar._PubCommand.ProcessCommand("蓝牙设置");
        } else if (command.equals("项目转移")) {
            new Project_Shift_Dialog().ShowDialog();
        } else if (command.equals("数据加密管理")) {
            new DataAuthorityManage_Dialog().ShowDialog();
        } else if (command.equals("数据字典")) {
            new DataDict_Manage_Dialog().ShowDialog();
        } else if (command.equals("计算器")) {
            new CalculateDialog().ShowDialog();
        } else if (command.equals("蓄积量计算")) {
            new CalXuJi_Dialog().ShowDialog();
        } else if (command.equals("照相测量高度")) {
            new MeasureHeightDialog().ShowDialog();
        } else if (command.equals("照相测量距离")) {
            new MeasureDistanceDialog().ShowDialog();
        } else if (command.equals("磁场感应")) {
            new MetalDetectDialog().ShowDialog();
        } else if (command.equals("气压监测")) {
            new PressureDetectDialog().ShowDialog();
        } else if (command.equals("系统截屏")) {
            try {
                String tmpFilePath = String.valueOf(PubVar.m_SystemPath) + "/系统截屏";
                if (!Common.ExistFolder(tmpFilePath)) {
                    tmpFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                }
                String tmpFilePath2 = String.valueOf(tmpFilePath) + "/截屏" + Common.fileDateFormat.format(new Date()) + ".png";
                if (Common.SaveImgFile(tmpFilePath2, PubVar._Map.getMaskBitmap())) {
                    Common.ShowDialog("截屏文件存放在:\r\n" + tmpFilePath2);
                } else {
                    Common.ShowDialog("截屏失败!");
                }
            } catch (Exception e) {
            }
        } else if (command.equals("GPS参数计算")) {
            new GPSParaCalculate_Dialog().ShowDialog();
        } else if (command.equals("单株蓄积量计算")) {
            new CalXuJiSingle_Dialog2().ShowDialog();
        } else if (command.equals("项目备份")) {
            BakPorject_Dialog tmpDialog = new BakPorject_Dialog();
            tmpDialog.setCurrentPrjNameString(PubVar._PubCommand.m_ProjectDB.GetProjectManage().getProjectName());
            tmpDialog.ShowDialog();
        } else if (command.equals("栅格数据转换")) {
            new RasterConvert_Dialog().ShowDialog();
        } else if (command.equals("导出图片")) {
            new ExportPhoto_Dialog().ShowDialog();
        } else {
            PubVar._PubCommand.ProcessCommand(command);
        }
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null && view.getTag() != null) {
                ToolsManager_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
