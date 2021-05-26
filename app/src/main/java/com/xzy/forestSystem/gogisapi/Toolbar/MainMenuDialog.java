package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.HashValueObject;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Navigation.Navigation_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.FeatureLayers_Manage_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Manag_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_List_Dialog;
import com.stczh.gzforestSystem.R;

public class MainMenuDialog extends AlertDialog {
    public int Height = 0;
    private View.OnClickListener MenuItemClickListener = new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MainMenuDialog.2
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Object tempObj = view.getTag();
            if (tempObj == null) {
                MainMenuDialog.this.dismiss();
                return;
            }
            String tempTag = tempObj.toString();
            if (tempTag.equals("图层")) {
                int[] tmpXY = Common.GetViewSize(view);
                MenuItemDialog.ShowMenuItemWindow(PubVar.MainContext, R.layout.x_menu_layers, new int[]{R.id.btn_Menu_Layer_Item1, R.id.btn_Menu_Layer_Item2, R.id.btn_Menu_Layer_Item3}, view, (float) tmpXY[0], (float) tmpXY[1], MainMenuDialog.this.pCallback);
                return;
            }
            if (tempTag.equals("菜单_图层_采集图层")) {
                new FeatureLayers_Manage_Dialog().ShowDialog();
            } else if (tempTag.equals("菜单_图层_矢量背景图层")) {
                new VectorLayers_List_Dialog().ShowDialog();
            } else if (tempTag.equals("菜单_图层_栅格图层")) {
                new RasterLayers_Manag_Dialog().ShowDialog();
            } else if (tempTag.equals("选择")) {
                PubVar._PubCommand.ShowToolbar("选择工具栏");
            } else if (tempTag.equals("足迹")) {
                PubVar._PubCommand.ShowToolbar("足迹工具栏");
            } else if (tempTag.equals("导航")) {
                new Navigation_Dialog().ShowDialog();
            } else if (tempTag.equals("工具")) {
                PubVar._PubCommand.ShowToolbar("工具工具栏");
            } else if (tempTag.equals("保存")) {
                PubVar._PubCommand.ProcessCommand(tempTag);
            } else if (tempTag.equals("云中心")) {
                PubVar._PubCommand.ShowToolbar("工具云中心");
            } else if (tempTag.equals("项目")) {
                PubVar._PubCommand.ProcessCommand("项目_选择");
            } else {
                PubVar._PubCommand.ProcessCommand(tempTag);
            }
            MainMenuDialog.this.dismiss();
        }
    };
    public float ShowX = 0.0f;
    public float ShowY = 0.0f;
    public int Width = 0;
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MainMenuDialog.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
            if (paramString.equals("关闭菜单")) {
                MenuItemDialog.IsShowing = false;
                MainMenuDialog.this.dismiss();
            }
        }
    };

    /* renamed from: x */
    private float f543x = 0.0f;

    /* renamed from: y */
    private float f544y = 0.0f;

    protected MainMenuDialog(Context context) {
        super(context);
    }

    public MainMenuDialog(Context context, int theme) {
        super(context, theme);
    }

    /* access modifiers changed from: protected */
    @Override // android.app.AlertDialog, android.app.Dialog
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_mainmenu_dialog);
        findViewById(R.id.btn_MainMenu_Project).setOnClickListener(this.MenuItemClickListener);
        findViewById(R.id.btn_Menu_Layer_Item1).setOnClickListener(this.MenuItemClickListener);
        findViewById(R.id.btn_Menu_Layer_Item2).setOnClickListener(this.MenuItemClickListener);
        findViewById(R.id.btn_Menu_Layer_Item3).setOnClickListener(this.MenuItemClickListener);
        findViewById(R.id.btn_MainMenu_Select).setOnClickListener(this.MenuItemClickListener);
        findViewById(R.id.btn_MainMenu_Tools).setOnClickListener(this.MenuItemClickListener);
        findViewById(R.id.btn_MainMenu_Query).setOnClickListener(this.MenuItemClickListener);
        findViewById(R.id.btn_MainMenu_Track).setOnClickListener(this.MenuItemClickListener);
        findViewById(R.id.btn_MainMenu_Navigation).setOnClickListener(this.MenuItemClickListener);
        findViewById(R.id.buttonTools).setOnClickListener(this.MenuItemClickListener);
        findViewById(R.id.btn_MainMenu_Save).setOnClickListener(this.MenuItemClickListener);
        findViewById(R.id.btn_MainMenu_Cloud).setOnClickListener(this.MenuItemClickListener);
        LinearLayout tmpLinearLayout = (LinearLayout) findViewById(R.id.ll_MainMenuDialog);
        this.Width = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.Height = View.MeasureSpec.makeMeasureSpec(0, 0);
        tmpLinearLayout.measure(this.Width, this.Height);
        this.Width = tmpLinearLayout.getMeasuredWidth();
        this.Height = tmpLinearLayout.getMeasuredHeight();
        Window dialogWindow = getWindow();
        dialogWindow.setWindowAnimations(R.style.MainMenuDialogAnimR2LB2T);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        dialogWindow.setGravity(51);
        this.f543x = (this.ShowX - ((float) this.Width)) - (10.0f * PubVar.ScaledDensity);
        this.f544y = this.ShowY;
        params.x = (int) this.f543x;
        params.y = (int) this.f544y;
        dialogWindow.setAttributes(params);
        UpdateModuleMenus();
    }

    public static void ShowMenu(Context context, float x, float y) {
        HashValueObject tmpHashValueObject = PubVar._ComHashMap.GetValue("Project");
        if (tmpHashValueObject != null) {
            Common.ShowToast("当前项目【" + tmpHashValueObject.Value + "】");
        }
        MainMenuDialog tmpDialog = new MainMenuDialog(context, R.style.MainMenuDialog);
        tmpDialog.ShowX = x;
        tmpDialog.ShowY = y;
        MenuItemDialog.IsShowing = false;
        tmpDialog.setCanceledOnTouchOutside(true);
        tmpDialog.show();
    }

    public void UpdateModuleMenus() {
        if (PubVar.Module_SenLinDuCha) {
            findViewById(R.id.ll_Other_Module).setVisibility(0);
            findViewById(R.id.btn_Other_SENLINEDUCHA).setOnClickListener(this.MenuItemClickListener);
            return;
        }
        findViewById(R.id.ll_Other_Module).setVisibility(8);
    }
}
