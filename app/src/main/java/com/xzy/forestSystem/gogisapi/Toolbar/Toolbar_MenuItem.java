package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.XProject.FeatureLayers_Manage_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Manag_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_List_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.UUID;

public class Toolbar_MenuItem extends PopupWindow {
    public static boolean IsShowing = false;
    public int Height;
    private View.OnClickListener MenuItemClickListener;
    public int Width;
    private int[] _ItemImgsRes;
    private String[] _ItemNames;
    private Object[] _ItemTags;
    private ICallback _MainCallback;
    private LinearLayout _MainLayout;
    private boolean _NeedCloseParent;
    private String _UID;
    private View _conentView;
    private Context _context;
    private ICallback pCallback;

    public Toolbar_MenuItem(Context context, String[] ItemNames, int[] ItemImgsRes, Object[] ItemTags) {
        this._MainCallback = null;
        this.Height = 0;
        this.Width = 0;
        this._UID = UUID.randomUUID().toString();
        this._MainLayout = null;
        this._ItemImgsRes = null;
        this._ItemNames = null;
        this._ItemTags = null;
        this._NeedCloseParent = true;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_MenuItem.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
            }
        };
        this.MenuItemClickListener = new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_MenuItem.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Object tempObj = view.getTag();
                Toolbar_MenuItem.IsShowing = false;
                if (tempObj == null) {
                    Toolbar_MenuItem.this.dismiss();
                    return;
                }
                if (Toolbar_MenuItem.this._MainCallback != null) {
                    Toolbar_MenuItem.this._MainCallback.OnClick("子菜单点击", String.valueOf(tempObj));
                }
                Toolbar_MenuItem.this.dismiss();
            }
        };
        this._UID = UUID.randomUUID().toString();
        this._context = context;
        this._ItemNames = ItemNames;
        this._ItemImgsRes = ItemImgsRes;
        this._ItemTags = ItemTags;
        OnCreate();
    }

    public void SetCallback(ICallback callback) {
        this._MainCallback = callback;
    }

    public void SetNeedCloseParent(boolean value) {
        this._NeedCloseParent = value;
    }

    public void OnCreate() {
        this._conentView = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(R.layout.toolbar_menuitem, (ViewGroup) null);
        setContentView(this._conentView);
        setWidth(-2);
        setHeight(-2);
        setOutsideTouchable(true);
        setAnimationStyle(R.style.MenuItemAnimB2T);
        this._MainLayout = (LinearLayout) this._conentView.findViewById(R.id.ll_Toolbar_MenuItem);
        initialItems();
        update();
        this.Width = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.Height = View.MeasureSpec.makeMeasureSpec(0, 0);
        this._conentView.measure(this.Width, this.Height);
        this.Width = this._conentView.getMeasuredWidth();
        this.Height = this._conentView.getMeasuredHeight();
        setOnDismissListener(new PopupWindow.OnDismissListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_MenuItem.3
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
            }
        });
    }

    private void initialItems() {
        if (!(this._ItemImgsRes == null || this._ItemNames == null || this._ItemTags == null)) {
            int count = this._ItemNames.length;
            int tmpMargin = (int) (PubVar.ScaledDensity * 5.0f);
            Resources res = this._context.getResources();
            for (int i = 0; i < count; i++) {
                Button tmpBtn01 = new Button(this._context);
                tmpBtn01.setText(this._ItemNames[i]);
                tmpBtn01.setTag(this._ItemTags[i]);
                tmpBtn01.setBackgroundResource(R.drawable.toolbar_btn_selector);
                Drawable tmpImg = res.getDrawable(this._ItemImgsRes[i]);
                tmpImg.setBounds(0, 0, tmpImg.getMinimumWidth(), tmpImg.getMinimumHeight());
                tmpBtn01.setCompoundDrawables(tmpImg, null, null, null);
                tmpBtn01.setOnClickListener(this.MenuItemClickListener);
                this._MainLayout.addView(tmpBtn01);
                LinearLayout.LayoutParams tmpLayput = (LinearLayout.LayoutParams) tmpBtn01.getLayoutParams();
                if (i == 0 || i == count - 1) {
                    tmpLayput.setMargins(tmpMargin, tmpMargin, tmpMargin, tmpMargin);
                } else {
                    tmpLayput.setMargins(tmpMargin, 0, tmpMargin, 0);
                }
            }
        }
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

    public static void ShowMenuItemWindow(Context context, View parentView, String[] menuItemNames, int[] menuItemImgs, Object[] menuItemTags, float x, float y, ICallback pCallback2) {
        Toolbar_MenuItem tmpWin = new Toolbar_MenuItem(context, menuItemNames, menuItemImgs, menuItemTags);
        tmpWin.SetCallback(pCallback2);
        tmpWin.showMenu(parentView, (int) (((float) (-tmpWin.Width)) - (3.0f * PubVar.ScaledDensity)), ((int) y) - tmpWin.Height);
    }
}
