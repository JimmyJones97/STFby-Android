package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.content.Context;
import android.view.View;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Server.DownloadFromServer_Dialog;
import  com.xzy.forestSystem.gogisapi.Server.LoginSetting_Dialog;
import  com.xzy.forestSystem.gogisapi.Server.UploadToServer_Dialog;
import com.stczh.gzforestSystem.R;

public class Toolbar_Cloud extends BaseToolbar {
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Cloud.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
        }
    };

    public Toolbar_Cloud(Context context, MapView mapView) {
        super(context, mapView);
        this.m_ToolbarName = "工具云中心";
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void LoadToolBar(View view, int mainLinearLayoutID) {
        super.LoadToolBar(view, mainLinearLayoutID);
        this.buttonIDs.put("账号", Integer.valueOf((int) R.id.buttonCloud_Login));
        this.buttonIDs.put("上传", Integer.valueOf((int) R.id.buttonCloud_Upload));
        this.buttonIDs.put("下载", Integer.valueOf((int) R.id.buttonCloud_Download));
        this.m_view.setOnTouchListener(this.touchListener);
        this.m_baseLayout.setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.buttonCloud_Login).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonCloud_Upload).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonCloud_Download).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonCloud_Exit).setOnClickListener(this.buttonClickListener);
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
            } else if (command.equals("账号")) {
                LoginSetting_Dialog tmpDialog = new LoginSetting_Dialog();
                tmpDialog.SetCallback(this.pCallback);
                tmpDialog.ShowDialog();
            } else if (command.equals("上传数据")) {
                UploadToServer_Dialog tmpDialog2 = new UploadToServer_Dialog();
                tmpDialog2.SetCallback(this.pCallback);
                tmpDialog2.ShowDialog();
            } else if (command.equals("下载数据")) {
                DownloadFromServer_Dialog tmpDialog3 = new DownloadFromServer_Dialog();
                tmpDialog3.SetCallback(this.pCallback);
                tmpDialog3.ShowDialog();
            }
        }
    }
}
