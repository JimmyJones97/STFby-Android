package  com.xzy.forestSystem.gogisapi.MyControls;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Setting.FormSizeSetting_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessagePanel_Dialog extends PopupWindow {
    static final long BUTTON_CHANGLE_DIR_TIME = 200;
    static final float BUTTON_CLICK_BIAS = (PubVar.ScaledDensity * 3.0f);
    static final float BUTTON_CLOSE_SPEED = (PubVar.ScaledDensity / 2.0f);
    private int StartX = 0;
    private int StartY = 0;
    private View.OnClickListener TipItemClickListener = new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.MessagePanel_Dialog.3
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Object tempObj = view.getTag();
            if (tempObj != null) {
                String tmpCommand = String.valueOf(tempObj);
                if (tmpCommand.equals("关闭")) {
                    PubVar.MessageDialog = null;
                    PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_MessagePanel_X", Integer.valueOf(MessagePanel_Dialog.this._LastX));
                    PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_MessagePanel_Y", Integer.valueOf(MessagePanel_Dialog.this._LastY));
                    MessagePanel_Dialog.this.dismiss();
                } else if (tmpCommand.equals("清空")) {
                    if (MessagePanel_Dialog.this._TipTextView != null) {
                        MessagePanel_Dialog.this._TipTextView.setText("");
                    }
                    MessagePanel_Dialog.this._OutMsgList.clear();
                } else if (tmpCommand.equals("设置")) {
                    FormSizeSetting_Dialog tmpDialog = new FormSizeSetting_Dialog();
                    tmpDialog.Set_FormType(0);
                    tmpDialog.SetCallback(MessagePanel_Dialog.this.pCallback);
                    tmpDialog.ShowDialog();
                }
            }
        }
    };
    private boolean _IsInitial = false;
    private boolean _IsMoveToobar = false;
    private long _LastMouseDownTime = 0;
    private int _LastX = ((int) (20.0f * PubVar.ScaledDensity));
    private int _LastY = ((int) (60.0f * PubVar.ScaledDensity));
    private ICallback _MainCallback = null;
    private List<String> _OutMsgList = new ArrayList();
    private View _ParentView = null;
    private TextView _TipTextView = null;
    private View _conentView;
    private Context _context;
    private int _xDelta;
    private int _yDelta;
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.MessagePanel_Dialog.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String command, Object object) {
            if (command.equals("窗体设置返回")) {
                MessagePanel_Dialog.this.UpdatePosition();
            }
        }
    };
    View.OnTouchListener touchListener = new View.OnTouchListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.MessagePanel_Dialog.2
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent event) {
            int X = (int) event.getRawX();
            int Y = (int) event.getRawY();
            switch (event.getAction()) {
                case 0:
                    MessagePanel_Dialog.this._IsMoveToobar = false;
                    MessagePanel_Dialog.this.StartX = X;
                    MessagePanel_Dialog.this.StartY = Y;
                    MessagePanel_Dialog.this._LastMouseDownTime = event.getDownTime();
                    return true;
                case 1:
                    MessagePanel_Dialog.this._IsMoveToobar = false;
                    MessagePanel_Dialog.this._xDelta = X - MessagePanel_Dialog.this.StartX;
                    MessagePanel_Dialog.this._yDelta = Y - MessagePanel_Dialog.this.StartY;
                    MessagePanel_Dialog.this._LastX += MessagePanel_Dialog.this._xDelta;
                    MessagePanel_Dialog.this._LastY -= MessagePanel_Dialog.this._yDelta;
                    if (MessagePanel_Dialog.this._LastX < 0) {
                        MessagePanel_Dialog.this._LastX = 0;
                    }
                    if (MessagePanel_Dialog.this._LastY < 0) {
                        MessagePanel_Dialog.this._LastY = 0;
                    }
                    MessagePanel_Dialog.this.UpdatePosition(MessagePanel_Dialog.this._LastX, MessagePanel_Dialog.this._LastY);
                    return true;
                case 2:
                    if (MessagePanel_Dialog.this._IsMoveToobar || (((float) Math.abs(X - MessagePanel_Dialog.this.StartX)) > MessagePanel_Dialog.BUTTON_CLICK_BIAS && ((float) Math.abs(Y - MessagePanel_Dialog.this.StartY)) > MessagePanel_Dialog.BUTTON_CLICK_BIAS)) {
                        MessagePanel_Dialog.this._IsMoveToobar = true;
                        MessagePanel_Dialog.this._xDelta = X - MessagePanel_Dialog.this.StartX;
                        MessagePanel_Dialog.this._yDelta = Y - MessagePanel_Dialog.this.StartY;
                        MessagePanel_Dialog.this.StartX = X;
                        MessagePanel_Dialog.this.StartY = Y;
                        MessagePanel_Dialog.this._LastX += MessagePanel_Dialog.this._xDelta;
                        MessagePanel_Dialog.this._LastY -= MessagePanel_Dialog.this._yDelta;
                        if (MessagePanel_Dialog.this._LastX < 0) {
                            MessagePanel_Dialog.this._LastX = 0;
                        }
                        if (MessagePanel_Dialog.this._LastY < 0) {
                            MessagePanel_Dialog.this._LastY = 0;
                        }
                        MessagePanel_Dialog.this.UpdatePosition(MessagePanel_Dialog.this._LastX, MessagePanel_Dialog.this._LastY);
                        break;
                    }
                case 6:
                    MessagePanel_Dialog.this._IsMoveToobar = false;
                    break;
            }
            return false;
        }
    };

    public MessagePanel_Dialog(Context context, View parent) {
        this._context = context;
        OnCreate(parent);
    }

    public void SetCallback(ICallback callback) {
        this._MainCallback = callback;
    }

    public void OnCreate(View parent) {
        String tempStr;
        String tempStr2;
        this._ParentView = parent;
        this._conentView = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(R.layout.messagepanel_dialog, (ViewGroup) null);
        setWidth(PubVar.MessagePanelWidth);
        setHeight(PubVar.MessagePanelHeight);
        setContentView(this._conentView);
        update();
        this._TipTextView = (TextView) this._conentView.findViewById(R.id.tv_Message);
        this._conentView.findViewById(R.id.btn_MessageDialogClose).setOnClickListener(this.TipItemClickListener);
        this._conentView.findViewById(R.id.btn_MessageDialogClear).setOnClickListener(this.TipItemClickListener);
        this._conentView.setOnTouchListener(this.touchListener);
        HashMap<String, String> tmpHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_MessagePanel_X");
        if (!(tmpHashMap == null || (tempStr2 = tmpHashMap.get("F2")) == null || tempStr2.equals(""))) {
            this._LastX = Integer.parseInt(tempStr2);
        }
        HashMap<String, String> tmpHashMap2 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_MessagePanel_Y");
        if (!(tmpHashMap2 == null || (tempStr = tmpHashMap2.get("F2")) == null || tempStr.equals(""))) {
            this._LastY = Integer.parseInt(tempStr);
        }
        if (this._LastX < 0 || this._LastX >= PubVar.ScreenWidth) {
            this._LastX = (int) (20.0f * PubVar.ScaledDensity);
        }
        if (this._LastY < 0 || this._LastY >= PubVar.ScreenHeight) {
            this._LastY = (int) (60.0f * PubVar.ScaledDensity);
        }
    }

    public void hideTip() {
        this._conentView.setVisibility(8);
    }

    public void showMsg(String msg) {
        if (!this._IsInitial) {
            UpdatePosition();
        }
        if (this._conentView.getVisibility() == 8) {
            this._conentView.setVisibility(0);
        }
        this._OutMsgList.add(0, msg);
        if (this._OutMsgList.size() > PubVar.MessageMaxCount) {
            this._OutMsgList.remove(this._OutMsgList.size() - 1);
        }
        String tmpTotalMsg = Common.CombineStrings("\r\n", this._OutMsgList);
        if (PubVar.MessageMaxCount > 1) {
            SpannableStringBuilder builder = new SpannableStringBuilder(tmpTotalMsg);
            ForegroundColorSpan redSpan = new ForegroundColorSpan(-1);
            ForegroundColorSpan whiteSpan = new ForegroundColorSpan(-256);
            int tmpIndex = this._OutMsgList.get(0).length();
            builder.setSpan(redSpan, 0, tmpIndex, 33);
            builder.setSpan(whiteSpan, tmpIndex, tmpTotalMsg.length(), 18);
            this._TipTextView.setText(builder);
            return;
        }
        this._TipTextView.setText(tmpTotalMsg);
    }

    public void UpdatePosition() {
        UpdatePosition(this._LastX, this._LastY);
    }

    public void UpdatePosition(int x, int y) {
        this._LastX = x;
        this._LastY = y;
        if (!this._IsInitial) {
            showAtLocation(this._ParentView, 83, this._LastX, this._LastY);
            this._IsInitial = true;
            update(this._LastX, this._LastY, PubVar.MessagePanelWidth, PubVar.MessagePanelHeight);
            this._IsInitial = true;
            return;
        }
        update(this._LastX, this._LastY, PubVar.MessagePanelWidth, PubVar.MessagePanelHeight);
    }
}
