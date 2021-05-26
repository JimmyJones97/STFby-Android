package  com.xzy.forestSystem.gogisapi.MyControls;

import android.content.DialogInterface;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;

public class InputXY_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public InputXY_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.InputXY_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("确定")) {
                    String tempStr01 = Common.GetTextValueOnID(InputXY_Dialog.this._Dialog, (int) R.id.editTextPara01);
                    String tempStr02 = Common.GetTextValueOnID(InputXY_Dialog.this._Dialog, (int) R.id.editTextPara02);
                    if (tempStr01 == null || tempStr01.trim().equals("") || tempStr02 == null || tempStr02.trim().equals("")) {
                        Common.ShowDialog("坐标输入值不能为空.");
                        return;
                    }
                    double tempX = Double.parseDouble(tempStr01);
                    double tempY = Double.parseDouble(tempStr02);
                    if (InputXY_Dialog.this.m_Callback != null) {
                        InputXY_Dialog.this.m_Callback.OnClick("坐标输入返回", new double[]{tempX, tempY});
                    }
                    InputXY_Dialog.this._Dialog.dismiss();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.inputxy_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("坐标输入");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
    }

    public void SetXY(double[] xy) {
        Common.SetTextViewValueOnID(this._Dialog, (int) R.id.editTextPara01, String.valueOf(xy[0]));
        Common.SetTextViewValueOnID(this._Dialog, (int) R.id.editTextPara02, String.valueOf(xy[1]));
    }

    public void SetXY(String[] xy) {
        Common.SetTextViewValueOnID(this._Dialog, (int) R.id.editTextPara01, xy[0]);
        Common.SetTextViewValueOnID(this._Dialog, (int) R.id.editTextPara02, xy[1]);
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.InputXY_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
            }
        });
        this._Dialog.show();
    }
}
