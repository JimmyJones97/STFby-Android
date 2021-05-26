package  com.xzy.forestSystem.gogisapi.Tools;

import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;

public class CalXuJiDialog {
    private XDialogTemplate _Dialog;
    private EditText etDMJ;
    private EditText etJSG;
    private EditText etJXJ;
    private EditText etMXJ;
    private EditText etMZS;
    private EditText etSMD;
    private XuJiHelper helper;
    private int jsg;
    private int jxj;
    private View.OnKeyListener keyListener;
    private ICallback m_Callback;
    private ICallback pCallback;
    private Spinner spnSZZ;
    private int szz;
    private final String[] szzArray;
    private TextView tvRemark;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public CalXuJiDialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.szzArray = new String[]{"马尾松组", "杉木组", "阔叶组"};
        this.szz = -1;
        this.jsg = 0;
        this.jxj = 0;
        this.helper = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalXuJiDialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("确定")) {
                    String tmpStr = CalXuJiDialog.this.etMXJ.getText().toString();
                    if (tmpStr.equals("")) {
                        Common.ShowDialog("没有任何计算结果.");
                    } else if (tmpStr.equals("计算错误.")) {
                        Common.ShowDialog("计算结果错,不能返回.");
                    } else {
                        if (CalXuJiDialog.this.m_Callback != null) {
                            CalXuJiDialog.this.m_Callback.OnClick("蓄积量计算返回", tmpStr);
                        }
                        CalXuJiDialog.this._Dialog.dismiss();
                    }
                }
            }
        };
        this.keyListener = new View.OnKeyListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalXuJiDialog.2
            @Override // android.view.View.OnKeyListener
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    if (CalXuJiDialog.this.etSMD.hasFocus()) {
                        CalXuJiDialog.this.computerBySMD(null);
                        return true;
                    } else if (CalXuJiDialog.this.etMXJ.hasFocus()) {
                        CalXuJiDialog.this.computerByMXJ(null);
                        return true;
                    } else if (CalXuJiDialog.this.etDMJ.hasFocus()) {
                        CalXuJiDialog.this.computerByDMJ(null);
                        return true;
                    }
                }
                return false;
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.xuji_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("蓄积量计算");
        this.helper = new XuJiHelper(this._Dialog.getContext());
        ((Button) this._Dialog.findViewById(R.id.btn_clear)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalXuJiDialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                CalXuJiDialog.this.etJSG.setText("");
                CalXuJiDialog.this.etJXJ.setText("");
                CalXuJiDialog.this.etSMD.setText("");
                CalXuJiDialog.this.etMZS.setText("");
                CalXuJiDialog.this.etMXJ.setText("");
                CalXuJiDialog.this.etDMJ.setText("");
            }
        });
        ((Button) this._Dialog.findViewById(R.id.btn_calSMD)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalXuJiDialog.4
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                CalXuJiDialog.this.computerBySMD(arg0);
            }
        });
        ((Button) this._Dialog.findViewById(R.id.btn_calMMXJ)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalXuJiDialog.5
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                CalXuJiDialog.this.computerByMXJ(arg0);
            }
        });
        ((Button) this._Dialog.findViewById(R.id.btn_calDMJ)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalXuJiDialog.6
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                CalXuJiDialog.this.computerByDMJ(arg0);
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void initWidget() {
        this.spnSZZ = (Spinner) this._Dialog.findViewById(R.id.spnSZZ);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this._Dialog.getContext(), (int) R.layout.spinner_textview, this.szzArray);
        adapter.setDropDownViewResource(17367049);
        this.spnSZZ.setAdapter((SpinnerAdapter) adapter);
        this.spnSZZ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalXuJiDialog.7
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                CalXuJiDialog.this.szz = position;
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.spnSZZ.setVisibility(0);
        this.etJSG = (EditText) this._Dialog.findViewById(R.id.etJSG);
        this.etJSG.setSelectAllOnFocus(true);
        this.etJXJ = (EditText) this._Dialog.findViewById(R.id.etJXJ);
        this.etJXJ.setSelectAllOnFocus(true);
        this.etSMD = (EditText) this._Dialog.findViewById(R.id.etSMD);
        this.etSMD.setOnKeyListener(this.keyListener);
        this.etSMD.setSelectAllOnFocus(true);
        this.etMZS = (EditText) this._Dialog.findViewById(R.id.etMZS);
        this.etMZS.setEnabled(false);
        this.etMXJ = (EditText) this._Dialog.findViewById(R.id.etMXJ);
        this.etMXJ.setOnKeyListener(this.keyListener);
        this.etMXJ.setSelectAllOnFocus(true);
        this.etDMJ = (EditText) this._Dialog.findViewById(R.id.etDMJ);
        this.etDMJ.setOnKeyListener(this.keyListener);
        this.etDMJ.setSelectAllOnFocus(true);
    }

    private boolean getFieldData() {
        clearEditTextFocus();
        this.szz = this.spnSZZ.getSelectedItemPosition();
        this.jsg = (int) this.helper.getFieldValue(this.etJSG, "平均高");
        if (this.jsg == 0) {
            return false;
        }
        if (this.jsg < 4 || this.jsg > 18) {
            this.helper.showToast(this.etJSG, "平均树高范围为4-18米，已超越计算限制");
            return false;
        }
        this.jxj = (int) this.helper.getFieldValue(this.etJXJ, "平均胸径");
        if (this.jxj == 0) {
            return false;
        }
        String res = RefData.checkPJXJ(this.jsg, this.jxj);
        if (res == null) {
            return true;
        }
        this.helper.showToast(this.etJXJ, res);
        return false;
    }

    public void computerBySMD(View view) {
        if (getFieldData()) {
            setFieldValue(this.etSMD, "疏密度", 0);
        }
    }

    public void computerByMXJ(View view) {
        if (getFieldData()) {
            setFieldValue(this.etMXJ, "每亩蓄积", 1);
        }
    }

    public void computerByDMJ(View view) {
        if (getFieldData()) {
            setFieldValue(this.etDMJ, "断面积", 2);
        }
    }

    private void setFieldValue(EditText editText, String itemName, int t) {
        try {
            double f = this.helper.getFieldValue(editText, itemName);
            if (f != 0.0d) {
                if (t == 0 && f < 1.0d) {
                    f *= 10.0d;
                }
                String[] result = FastComputation.getInstance().execute((double) this.jsg, (double) this.jxj, f, this.szz, t);
                double value = Double.parseDouble(result[0]);
                if (value == 0.0d) {
                    this.helper.showToast(null, "计算错误，请检查数据");
                    return;
                }
                this.etSMD.setText(String.valueOf(value));
                this.etMZS.setText(result[1]);
                this.etMXJ.setText(String.valueOf(Double.parseDouble(result[2])));
                this.etDMJ.setText(String.valueOf(Double.parseDouble(result[3])));
                this.helper.closeSoftKeyBoard();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearEditTextFocus() {
        this.etJSG.clearFocus();
        this.etJXJ.clearFocus();
        this.etSMD.clearFocus();
        this.etMZS.clearFocus();
        this.etMXJ.clearFocus();
        this.etDMJ.clearFocus();
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalXuJiDialog.8
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                CalXuJiDialog.this.initWidget();
                if (CalXuJiDialog.this.m_Callback != null) {
                    CalXuJiDialog.this._Dialog.SetHeadButtons("1,2130837858,确定,确定", CalXuJiDialog.this.pCallback);
                }
            }
        });
        this._Dialog.show();
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                view.getTag().toString();
            }
        }
    }
}
