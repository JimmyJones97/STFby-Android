package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import com.xzy.forestSystem.gogisapi.Common.Common;
import com.stczh.gzforestSystem.R;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseToolbar {
    static final long BUTTON_CHANGLE_DIR_TIME = 200;
    static final float BUTTON_CLICK_BIAS = (PubVar.ScaledDensity * 3.0f);
    static final float BUTTON_CLOSE_SPEED = (PubVar.ScaledDensity / 2.0f);
    static final int BUTTON_DEFAULT = 2130837962;
    static final int BUTTON_DISABLE = 2130837959;
    static final int BUTTON_SELECTED = 2130837961;
    private int StartX = 0;
    private int StartY = 0;
    private boolean _IsMoveToobar = false;
    private long _LastMouseDownTime = 0;
    private int _OrigHeight = 50;
    private int _OrigWidth = 100;
    private int _xDelta;
    private int _yDelta;
    View.OnClickListener buttonClickListener = new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar.2
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            BaseToolbar.this.DoCommand(view);
        }
    };
    HashMap<String, Integer> buttonIDs = new HashMap<>();
    boolean isHorizontal = true;
    private boolean isShow = false;
    boolean m_AllowdChangeOri = true;
    boolean m_IsAllowedClose = true;
    MapView m_MapView = null;
    String m_ToolbarName = "";
    LinearLayout m_baseLayout = null;
    Context m_context = null;
    View m_view = null;
    private int myX = 0;
    private int myY = 0;
    View.OnTouchListener touchListener = new View.OnTouchListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar.1
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent event) {
            return false;
        }
    };

    public BaseToolbar(Context context, MapView mapView) {
        this.m_context = context;
        this.m_MapView = mapView;
    }

    public void LoadToolBar(View view) {
        LoadToolBar(view, -1);
    }

    public void LoadToolBar(View view, int mainLinearLayoutID) {
        try {
            this.m_view = view;
            if (mainLinearLayoutID != -1) {
                this.m_baseLayout = (LinearLayout) view.findViewById(mainLinearLayoutID);
            } else {
                this.m_baseLayout = (LinearLayout) view;
            }
            this.m_view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            this._OrigHeight = this.m_view.getMeasuredHeight();
            this._OrigWidth = this.m_view.getMeasuredWidth();
            if (!this.m_ToolbarName.equals("")) {
                HashMap<String, String> hashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara(this.m_ToolbarName);
                if (hashMap == null) {
                    int tempY = 80 * (PubVar._PubCommand.m_Toolbars.size() + 1);
                    UpdateToolbarPosition(30, tempY);
                    HashMap<String, String> hashMap2 = new HashMap<>();
                    hashMap2.put("F2", String.valueOf(30) + ";" + tempY + ";true;true");
                    PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara(this.m_ToolbarName, hashMap2);
                    this.isShow = true;
                    return;
                }
                String[] tempStrs = hashMap.get("F2").split(";");
                if (tempStrs != null && tempStrs.length > 2) {
                    UpdateToolbarPosition(Integer.parseInt(tempStrs[0]), Integer.parseInt(tempStrs[1]));
                    if (!tempStrs[2].equals("true")) {
                        this.m_view.setVisibility(8);
                        this.isShow = false;
                    } else {
                        this.isShow = true;
                    }
                    if (tempStrs.length <= 3) {
                        return;
                    }
                    if (!tempStrs[3].equals("true")) {
                        this.isHorizontal = false;
                    } else {
                        this.isHorizontal = true;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public boolean getButtonIsSelected(String button) {
        View tempView;
        if (button == null || !this.buttonIDs.containsKey(button) || (tempView = this.m_view.findViewById(this.buttonIDs.get(button).intValue())) == null) {
            return false;
        }
        return tempView.isSelected();
    }

    public void SetButtonSelectedStatus(String button, boolean isSeleted) {
        View tempView;
        if (button == null) {
            for (Map.Entry<String, Integer> entry : this.buttonIDs.entrySet()) {
                View tempView2 = this.m_view.findViewById(entry.getValue().intValue());
                if (tempView2 != null) {
                    tempView2.setBackgroundResource(R.drawable.toolbar_btn_selector);
                    tempView2.setSelected(false);
                }
            }
        } else if (this.buttonIDs.containsKey(button) && (tempView = this.m_view.findViewById(this.buttonIDs.get(button).intValue())) != null) {
            if (isSeleted) {
                SetButtonSelectedStatus(null, false);
                tempView.setBackgroundResource(R.drawable.toolbar_btn_selected);
                tempView.setSelected(true);
                return;
            }
            tempView.setBackgroundResource(R.drawable.toolbar_btn_selector);
            tempView.setSelected(false);
        }
    }

    public void SetAllButtonSelectedStatusExcept(String exceptButton, boolean isSeleted) {
        try {
            if (this instanceof Toolbar_ZoomScale) {
                SetButtonSelectedStatus(exceptButton, isSeleted);
                return;
            }
            for (Map.Entry<String, Integer> entry : this.buttonIDs.entrySet()) {
                View tempView = this.m_view.findViewById(entry.getValue().intValue());
                if (tempView != null && (exceptButton == null || !entry.getKey().equals(exceptButton))) {
                    if (isSeleted) {
                        tempView.setBackgroundResource(R.drawable.toolbar_btn_selected);
                        tempView.setSelected(true);
                    } else {
                        tempView.setBackgroundResource(R.drawable.toolbar_btn_selector);
                        tempView.setSelected(false);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void SetButtonsEnable(String exceptButtons, boolean enable) {
        String[] tmpButtons;
        View tempView;
        for (Map.Entry<String, Integer> entry : this.buttonIDs.entrySet()) {
            View tempView2 = this.m_view.findViewById(entry.getValue().intValue());
            if (tempView2 != null) {
                tempView2.setEnabled(enable);
                if (!enable) {
                    tempView2.setBackgroundResource(R.drawable.toolbar_btn_disable);
                } else if (tempView2.isSelected()) {
                    tempView2.setBackgroundResource(R.drawable.toolbar_btn_selected);
                } else {
                    tempView2.setBackgroundResource(R.drawable.toolbar_btn_selector);
                }
            }
        }
        if (!(exceptButtons == null || (tmpButtons = exceptButtons.split(";")) == null || tmpButtons.length <= 0)) {
            for (String button : tmpButtons) {
                if (this.buttonIDs.containsKey(button) && (tempView = this.m_view.findViewById(this.buttonIDs.get(button).intValue())) != null) {
                    tempView.setEnabled(!enable);
                    if (enable) {
                        tempView.setBackgroundResource(R.drawable.toolbar_btn_disable);
                    } else if (tempView.isSelected()) {
                        tempView.setBackgroundResource(R.drawable.toolbar_btn_selected);
                    } else {
                        tempView.setBackgroundResource(R.drawable.toolbar_btn_selector);
                    }
                }
            }
        }
    }

    public void DoCommand(View view) {
    }

    public void DoCommand(String command) {
    }

    public void UpdateToolbarPosition() {
        UpdateToolbarPosition(this.myX, this.myY);
    }

    public void UpdateToolbarPosition(int x, int y) {
        try {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.m_view.getLayoutParams();
            if (!PubVar.Toolbar_AutoResize) {
                layoutParams.leftMargin = x;
                layoutParams.topMargin = y;
                layoutParams.rightMargin = -1000;
                layoutParams.bottomMargin = -1000;
                layoutParams.width = -2;
                layoutParams.height = -2;
            } else if (this.isHorizontal) {
                layoutParams.rightMargin = 0;
                layoutParams.bottomMargin = 0;
                int tmpW = PubVar.ScreenWidth - x;
                if (x < 0) {
                    x = 0;
                    tmpW = PubVar.ScreenWidth + 0;
                }
                if (y < 0) {
                    y = 0;
                }
                if (tmpW > this._OrigWidth) {
                    tmpW = this._OrigWidth;
                }
                layoutParams.width = tmpW;
                layoutParams.height = -2;
                layoutParams.leftMargin = x;
                layoutParams.topMargin = y;
            } else {
                layoutParams.rightMargin = 0;
                layoutParams.bottomMargin = 0;
                int tmpH = PubVar.ScreenHeight - y;
                if (tmpH > this._OrigHeight) {
                    tmpH = this._OrigHeight;
                }
                layoutParams.height = tmpH;
                layoutParams.width = -2;
                layoutParams.leftMargin = x;
                layoutParams.topMargin = y;
            }
            this.myX = x;
            this.myY = y;
            this.m_view.setLayoutParams(layoutParams);
        } catch (Exception e) {
        }
    }

    public void SaveConfigDB() {
        String tempStr;
        String tempStr2;
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.m_view.getLayoutParams();
            String tempStr3 = String.valueOf(layoutParams.leftMargin) + ";" + layoutParams.topMargin;
            if (this.m_view.getVisibility() == 0) {
                tempStr = String.valueOf(tempStr3) + ";true";
            } else {
                tempStr = String.valueOf(tempStr3) + ";false";
            }
            if (this.isHorizontal) {
                tempStr2 = String.valueOf(tempStr) + ";true";
            } else {
                tempStr2 = String.valueOf(tempStr) + ";false";
            }
            hashMap.put("F2", tempStr2);
            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara(this.m_ToolbarName, hashMap);
        } catch (Exception e) {
        }
    }

    public void SetOrientation(boolean _isHorizontal) {
        this.isHorizontal = _isHorizontal;
        ChangeOrientation();
    }

    public void ChangeOrientation() {
    }

    public void Show() {
        Animation translateAnimation;
        try {
            ChangeOrientation();
            if (PubVar.ShowToolbarAnim) {
                Animation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
                int tempY = PubVar.ScreenHeight - this.myY;
                if (tempY > this.myY) {
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-this.myY), 0.0f);
                } else {
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) tempY, 0.0f);
                }
                AnimationSet animSet = new AnimationSet(true);
                animSet.addAnimation(translateAnimation);
                animSet.addAnimation(alphaAnimation);
                animSet.setDuration(500);
                this.m_view.startAnimation(animSet);
            }
            if (this.m_view.getVisibility() != 0) {
                SetButtonSelectedStatus(null, false);
                this.m_view.setVisibility(0);
            }
            this.isShow = true;
        } catch (Exception e) {
        }
    }

    public void Hide() {
        Animation translateAnimation;
        try {
            if (PubVar.ShowToolbarAnim && this.m_view.getVisibility() != 8) {
                Animation alphaAnimation = new AlphaAnimation(1.0f, 0.1f);
                int tempY = PubVar.ScreenHeight - this.myY;
                if (tempY > this.myY) {
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-this.myY));
                } else {
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) tempY);
                }
                AnimationSet animSet = new AnimationSet(true);
                animSet.addAnimation(translateAnimation);
                animSet.addAnimation(alphaAnimation);
                animSet.setDuration(500);
                this.m_view.startAnimation(animSet);
            }
            ((LinearLayout) PubVar._PubCommand.m_MainLayout.findViewById(R.id.ll_toolbarGroup)).removeView(this.m_view);
            PubVar._PubCommand.RemoveToolbar(this.m_ToolbarName);
            this.isShow = false;
            PubVar._PubCommand.ProcessCommand("自由缩放");
        } catch (Exception ex) {
            Common.Log("关闭工具栏", ex.getLocalizedMessage());
        }
    }

    public void Enable() {
        this.m_view.setEnabled(true);
    }

    public void Disable() {
        this.m_view.setEnabled(false);
    }

    public boolean IsVisiable() {
        return this.isShow;
    }

    public String getToolbarName() {
        return this.m_ToolbarName;
    }
}
