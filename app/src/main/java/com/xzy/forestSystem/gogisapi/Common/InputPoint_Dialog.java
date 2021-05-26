package  com.xzy.forestSystem.gogisapi.Common;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;

public class InputPoint_Dialog {
    public int FormType;
    public boolean IsMeasureToolEnable;
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private ICallback pCallback;
    private Object returnTag;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public InputPoint_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.FormType = 0;
        this.IsMeasureToolEnable = true;
        this.returnTag = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Common.InputPoint_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("确定")) {
                    double tmpZ = 0.0d;
                    double tmpX = Double.parseDouble(Common.GetEditTextValueOnID(InputPoint_Dialog.this._Dialog, R.id.editTextPara01));
                    double tmpY = Double.parseDouble(Common.GetEditTextValueOnID(InputPoint_Dialog.this._Dialog, R.id.editTextPara02));
                    if (InputPoint_Dialog.this.FormType == 1) {
                        tmpZ = Double.parseDouble(Common.GetEditTextValueOnID(InputPoint_Dialog.this._Dialog, R.id.editTextPara03));
                    }
                    if (InputPoint_Dialog.this.m_Callback != null) {
                        InputPoint_Dialog.this.m_Callback.OnClick("输入节点坐标返回", new Object[]{InputPoint_Dialog.this.returnTag, Double.valueOf(tmpX), Double.valueOf(tmpY), Double.valueOf(tmpZ)});
                    }
                    InputPoint_Dialog.this._Dialog.dismiss();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.inputpoint_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("添加点");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
    }

    public void setTitle(String title) {
        this._Dialog.SetCaption(title);
    }

    public void SetReturnTag(Object ReturnTag) {
        this.returnTag = ReturnTag;
    }

    public void setDefaultValue(double x, double y) {
        setDefaultValue(x, y, 0.0d);
    }

    public void setDefaultValue(double x, double y, double z) {
        Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara01, String.valueOf(x));
        Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara02, String.valueOf(y));
        Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara03, String.valueOf(z));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        if (this.FormType != 0 && this.FormType == 1) {
            this._Dialog.findViewById(R.id.ll_zLayout).setVisibility(8);
        }
        if (this.IsMeasureToolEnable) {
            this._Dialog.findViewById(R.id.ll_Tools_Bottom).setVisibility(0);
            ((Button) this._Dialog.findViewById(R.id.btn_GetFromMeasure)).setOnClickListener(new ViewClick());
            return;
        }
        this._Dialog.findViewById(R.id.ll_Tools_Bottom).setVisibility(8);
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.InputPoint_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                InputPoint_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: package-private */
    public class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null && view.getTag().toString().equals("从测量获取")) {
                Coordinate tempCoord = PubVar._PubCommand.m_Measure.GetLastCoordinate();
                if (tempCoord == null) {
                    Common.ShowDialog("没有绘制测量的点或直线,请先利用【测量】工具测量点或直线.");
                    return;
                }
                Common.SetEditTextValueOnID(InputPoint_Dialog.this._Dialog, R.id.editTextPara01, String.valueOf(tempCoord.getX()));
                Common.SetEditTextValueOnID(InputPoint_Dialog.this._Dialog, R.id.editTextPara02, String.valueOf(tempCoord.getY()));
            }
        }
    }
}
