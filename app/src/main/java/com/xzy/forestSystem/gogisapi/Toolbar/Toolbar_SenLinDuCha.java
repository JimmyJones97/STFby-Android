package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.content.Context;
import android.view.View;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.Setting_Dialog;
import  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.TubanExport_Dialog;
import  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiQuery_Dialog;
import  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDi_Dialog;
import com.stczh.gzforestSystem.R;

public class Toolbar_SenLinDuCha extends BaseToolbar {
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_SenLinDuCha.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
        }
    };

    public Toolbar_SenLinDuCha(Context context, MapView mapView) {
        super(context, mapView);
        this.m_ToolbarName = "森林督查工具栏";
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void LoadToolBar(View view, int mainLinearLayoutID) {
        super.LoadToolBar(view, mainLinearLayoutID);
        this.buttonIDs.put("设置", Integer.valueOf((int) R.id.buttonSLDC_Setting));
        this.buttonIDs.put("样地调查", Integer.valueOf((int) R.id.buttonSLDC_YDDC));
        this.buttonIDs.put("样地查询", Integer.valueOf((int) R.id.buttonSLDC_YDCX));
        this.buttonIDs.put("图斑导出", Integer.valueOf((int) R.id.buttonSLDC_TBExport));
        this.m_view.setOnTouchListener(this.touchListener);
        this.m_baseLayout.setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.buttonSLDC_Setting).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonSLDC_YDDC).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonSLDC_YDCX).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonSLDC_TBExport).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonSLDC_Exit).setOnClickListener(this.buttonClickListener);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Show() {
        super.Show();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Hide() {
        super.Hide();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void DoCommand(View view) {
        Object tempObj = view.getTag();
        if (tempObj != null) {
            String command = tempObj.toString();
            if (command.equals("关闭工具")) {
                Hide();
                SaveConfigDB();
            } else if (command.equals("设置")) {
                Setting_Dialog tmpDialog = new Setting_Dialog();
                tmpDialog.SetCallback(this.pCallback);
                tmpDialog.ShowDialog();
            } else if (command.equals("样地调查")) {
                YangDi_Dialog tmpDialog2 = new YangDi_Dialog();
                tmpDialog2.SetCallback(this.pCallback);
                tmpDialog2.ShowDialog();
            } else if (command.equals("样地查询")) {
                YangDiQuery_Dialog tmpDialog3 = new YangDiQuery_Dialog();
                tmpDialog3.SetCallback(this.pCallback);
                tmpDialog3.ShowDialog();
            } else if (command.equals("图斑导出")) {
                new TubanExport_Dialog().ShowDialog();
            }
        }
    }
}
