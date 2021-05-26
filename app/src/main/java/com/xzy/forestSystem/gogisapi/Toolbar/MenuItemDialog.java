package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.XProject.FeatureLayers_Manage_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Manag_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_List_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.UUID;

public class MenuItemDialog extends PopupWindow {
    public static boolean IsShowing = false;
    public int Height;
    private View.OnClickListener MenuItemClickListener;
    public int Width;
    private int _LayoutID;
    private ICallback _MainCallback;
    private int[] _MenuItemIDs;
    private boolean _NeedCloseParent;
    private String _UID;
    private View _conentView;
    private Context _context;
    private ICallback pCallback;

    public MenuItemDialog(Context context, int layoutID, int[] menuItemIDs) {
        this._LayoutID = 0;
        this._MainCallback = null;
        this._MenuItemIDs = null;
        this.Height = 0;
        this.Width = 0;
        this._UID = UUID.randomUUID().toString();
        this._NeedCloseParent = true;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MenuItemDialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("关闭菜单")) {
                    if (MenuItemDialog.this._MainCallback != null) {
                        MenuItemDialog.this._MainCallback.OnClick("关闭子菜单", MenuItemDialog.this._UID);
                    }
                    MenuItemDialog.this.dismiss();
                } else if (paramString.contains("菜单")) {
                    MenuItemDialog.ProcessCommand(paramString);
                }
            }
        };
        this.MenuItemClickListener = new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MenuItemDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Object tempObj = view.getTag();
                MenuItemDialog.IsShowing = false;
                if (tempObj == null) {
                    MenuItemDialog.this.dismiss();
                    return;
                }
                MenuItemDialog.this.pCallback.OnClick(String.valueOf(tempObj), null);
                MenuItemDialog.this.dismiss();
            }
        };
        this._UID = UUID.randomUUID().toString();
        this._context = context;
        this._LayoutID = layoutID;
        this._MenuItemIDs = menuItemIDs;
        OnCreate();
    }

    public void SetCallback(ICallback callback) {
        this._MainCallback = callback;
    }

    public void SetNeedCloseParent(boolean value) {
        this._NeedCloseParent = value;
    }

    public void SetMenuItemIDs(int[] values) {
        this._MenuItemIDs = values;
    }

    public void OnCreate() {
        this._conentView = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(this._LayoutID, (ViewGroup) null);
        setContentView(this._conentView);
        setWidth(-2);
        setHeight(-2);
        setOutsideTouchable(false);
        setAnimationStyle(R.style.MainMenuDialogAnimR2L);
        update();
        if (this._MenuItemIDs != null && this._MenuItemIDs.length > 0) {
            for (int tmpID : this._MenuItemIDs) {
                this._conentView.findViewById(tmpID).setOnClickListener(this.MenuItemClickListener);
            }
        }
        this.Width = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.Height = View.MeasureSpec.makeMeasureSpec(0, 0);
        this._conentView.measure(this.Width, this.Height);
        this.Width = this._conentView.getMeasuredWidth();
        this.Height = this._conentView.getMeasuredHeight();
        setOnDismissListener(new PopupWindow.OnDismissListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MenuItemDialog.3
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                MenuItemDialog.IsShowing = false;
                if (MenuItemDialog.this._NeedCloseParent && MenuItemDialog.this._MainCallback != null) {
                    MenuItemDialog.this._MainCallback.OnClick("关闭菜单", null);
                }
            }
        });
    }

    public void showMenu(View parent, int x, int y) {
        if (!isShowing()) {
            showAtLocation(parent, 51, x, y);
            IsShowing = true;
            return;
        }
        dismiss();
    }

    public static void ProcessCommand(String command) {
        if (command.equals("菜单_图层_采集图层")) {
            new FeatureLayers_Manage_Dialog().ShowDialog();
        } else if (command.equals("菜单_图层_矢量背景图层")) {
            new VectorLayers_List_Dialog().ShowDialog();
        } else if (command.equals("菜单_图层_栅格图层")) {
            new RasterLayers_Manag_Dialog().ShowDialog();
        }
    }

    public static void ShowMenuItemWindow(Context context, int layoutID, int[] menuItemIDs, View parentView, float x, float y, ICallback pCallback2) {
        if (!IsShowing) {
            MenuItemDialog tmpWin = new MenuItemDialog(context, layoutID, menuItemIDs);
            tmpWin.SetCallback(pCallback2);
            tmpWin.showMenu(parentView, (int) (((float) (-tmpWin.Width)) - (3.0f * PubVar.ScaledDensity)), 0);
        }
    }
}
