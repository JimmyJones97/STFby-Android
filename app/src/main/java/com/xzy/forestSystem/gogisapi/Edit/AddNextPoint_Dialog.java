package  com.xzy.forestSystem.gogisapi.Edit;

import android.widget.RadioGroup;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;

public class AddNextPoint_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private RadioGroup m_RadioGroup;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public AddNextPoint_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_RadioGroup = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Edit.AddNextPoint_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (!command.equals("确定")) {
                        return;
                    }
                    if (AddNextPoint_Dialog.this.m_RadioGroup.getCheckedRadioButtonId() == R.id.addNextPoint_radio0) {
                        double tmpX = Double.parseDouble(Common.GetEditTextValueOnID(AddNextPoint_Dialog.this._Dialog, R.id.et_xbias));
                        double tmpY = Double.parseDouble(Common.GetEditTextValueOnID(AddNextPoint_Dialog.this._Dialog, R.id.et_ybias));
                        if (tmpX == 0.0d && tmpY == 0.0d) {
                            Common.ShowDialog("偏移值不能都为0.");
                            return;
                        }
                        if (AddNextPoint_Dialog.this.m_Callback != null) {
                            AddNextPoint_Dialog.this.m_Callback.OnClick("输入下一点返回", new double[]{tmpX, tmpY});
                        }
                        AddNextPoint_Dialog.this._Dialog.dismiss();
                        return;
                    }
                    double tmpAngle = Math.toRadians(Double.parseDouble(Common.GetEditTextValueOnID(AddNextPoint_Dialog.this._Dialog, R.id.et_addPointAngle)));
                    double tmpD = Double.parseDouble(Common.GetEditTextValueOnID(AddNextPoint_Dialog.this._Dialog, R.id.et_addPointDistance));
                    if (tmpD == 0.0d) {
                        Common.ShowDialog("偏移距离不能为0.");
                        return;
                    }
                    double tmpX2 = tmpD * Math.cos(tmpAngle);
                    double tmpY2 = tmpD * Math.sin(tmpAngle);
                    if (AddNextPoint_Dialog.this.m_Callback != null) {
                        AddNextPoint_Dialog.this.m_Callback.OnClick("输入下一点返回", new double[]{tmpX2, tmpY2});
                    }
                    AddNextPoint_Dialog.this._Dialog.dismiss();
                } catch (Exception e) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.addnextpoint_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("增加下一点");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.m_RadioGroup = (RadioGroup) this._Dialog.findViewById(R.id.rg_addNextPoint);
        this.m_RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.Edit.AddNextPoint_Dialog.2
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (arg1 == R.id.addNextPoint_radio0) {
                    AddNextPoint_Dialog.this._Dialog.findViewById(R.id.ll_addNextPoint01).setVisibility(0);
                    AddNextPoint_Dialog.this._Dialog.findViewById(R.id.ll_addNextPoint02).setVisibility(8);
                    return;
                }
                AddNextPoint_Dialog.this._Dialog.findViewById(R.id.ll_addNextPoint01).setVisibility(8);
                AddNextPoint_Dialog.this._Dialog.findViewById(R.id.ll_addNextPoint02).setVisibility(0);
            }
        });
    }

    public void ShowDialog() {
        this._Dialog.show();
    }
}
