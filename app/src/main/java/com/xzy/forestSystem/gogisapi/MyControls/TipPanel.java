package  com.xzy.forestSystem.gogisapi.MyControls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;
import java.util.UUID;

public class TipPanel extends PopupWindow {
    public boolean AllowShow;
    private int Height;
    private View.OnClickListener TipItemClickListener;
    private int Width;
    private ICallback _MainCallback;
    private String _TipMsg;
    private TextView _TipTextView;
    private String _UID;
    private View _conentView;
    private Context _context;

    public TipPanel(Context context) {
        this._MainCallback = null;
        this._TipMsg = "";
        this.Height = 0;
        this.Width = 0;
        this.AllowShow = true;
        this._TipTextView = null;
        this._UID = UUID.randomUUID().toString();
        this.TipItemClickListener = new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.TipPanel.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (view.getTag() != null && TipPanel.this._MainCallback != null) {
                    TipPanel.this._MainCallback.OnClick("关闭子菜单", TipPanel.this._UID);
                }
            }
        };
        this._UID = UUID.randomUUID().toString();
        this._context = context;
        OnCreate();
    }

    public void SetCallback(ICallback callback) {
        this._MainCallback = callback;
    }

    public void OnCreate() {
        this._conentView = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(R.layout.tippanel, (ViewGroup) null);
        setContentView(this._conentView);
        setWidth(-2);
        setHeight(-2);
        update();
        this._TipTextView = (TextView) this._conentView.findViewById(R.id.tv_TipPanel);
        this.Width = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.Height = View.MeasureSpec.makeMeasureSpec(0, 0);
        this._conentView.measure(this.Width, this.Height);
        this.Width = this._conentView.getMeasuredWidth();
        this.Height = this._conentView.getMeasuredHeight();
        setOnDismissListener(new PopupWindow.OnDismissListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.TipPanel.2
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                if (TipPanel.this._MainCallback != null) {
                    TipPanel.this._MainCallback.OnClick("关闭提示信息", null);
                }
            }
        });
    }

    public void setClickHide() {
        this._TipTextView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.TipPanel.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                TipPanel.this.hideTip();
            }
        });
    }

    public void hideTip() {
        this.AllowShow = false;
        dismiss();
    }

    public void showTip(View parent, int x, int y, String msg) {
        this._TipMsg = msg;
        if (isShowing()) {
            if (this._TipTextView != null) {
                if (this._TipMsg.equals("")) {
                    this._TipTextView.setVisibility(8);
                } else {
                    if (this._conentView.getVisibility() == 8) {
                        this._conentView.setVisibility(0);
                    }
                    this._TipTextView.setText(this._TipMsg);
                }
            }
            update(x, y, -1, -1);
            return;
        }
        showAtLocation(parent, 51, x, y);
        if (this._TipTextView == null) {
            return;
        }
        if (this._TipMsg.equals("")) {
            this._TipTextView.setVisibility(8);
            return;
        }
        if (this._conentView.getVisibility() == 8) {
            this._conentView.setVisibility(0);
        }
        this._TipTextView.setText(this._TipMsg);
    }
}
