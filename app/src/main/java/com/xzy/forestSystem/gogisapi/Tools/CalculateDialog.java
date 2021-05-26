package  com.xzy.forestSystem.gogisapi.Tools;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.xzy.forestSystem.gogisapi.MyControls.ExpressEditText;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculateDialog {
    private static List<Map<String, String>> calFunctionsArray = null;
    private XDialogTemplate _Dialog;
    private ExpressEditText expressText;
    private View indepLayout;
    private ICallback m_Callback;

    /* renamed from: mi */
    private MenuInflater f545mi;
    private ICallback pCallback;
    private EditText resultTxt;
    private View tableLayout;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public CalculateDialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalculateDialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("确定")) {
                    String tmpStr = CalculateDialog.this.resultTxt.getText().toString();
                    if (tmpStr.equals("")) {
                        Common.ShowDialog("没有任何计算结果.");
                    } else if (tmpStr.equals("计算错误.")) {
                        Common.ShowDialog("计算结果错,不能返回.");
                    } else {
                        if (CalculateDialog.this.m_Callback != null) {
                            CalculateDialog.this.m_Callback.OnClick("计算器返回", tmpStr);
                        }
                        CalculateDialog.this._Dialog.dismiss();
                    }
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.calculator_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("计算器");
        this._Dialog.SetHeightWrapContent();
        this.resultTxt = (EditText) this._Dialog.findViewById(R.id.et_calResult);
        this.f545mi = new MenuInflater(this._Dialog.getContext());
        initButtons();
        this.tableLayout = (TableLayout) this._Dialog.findViewById(R.id.innerkeyboard);
        this.indepLayout = this._Dialog.findViewById(R.id.indepLayout);
    }

    private void initButtons() {
        this.expressText = (ExpressEditText) this._Dialog.findViewById(R.id.expressEditText);
        Map<Integer, String> appendButtons = new HashMap<>();
        appendButtons.put(Integer.valueOf((int) R.id.add), "+");
        appendButtons.put(Integer.valueOf((int) R.id.sub), "-");
        appendButtons.put(Integer.valueOf((int) R.id.multiply), "×");
        appendButtons.put(Integer.valueOf((int) R.id.divide), "÷");
        appendButtons.put(Integer.valueOf((int) R.id.dot), FileSelector_Dialog.sFolder);
        appendButtons.put(Integer.valueOf((int) R.id.lparenthesis), "(");
        appendButtons.put(Integer.valueOf((int) R.id.rparenthesis), ")");
        appendButtons.put(Integer.valueOf((int) R.id.remainder), "%");
        appendButtons.put(Integer.valueOf((int) R.id.comma), ",");
//        appendButtons.put(Integer.valueOf((int) R.id.f564pi), "π");
//        appendButtons.put(Integer.valueOf((int) R.id.f562e), "e");
        appendButtons.put(Integer.valueOf((int) R.id.sqrt), "sqrt(");
        ((Button) this._Dialog.findViewById(R.id.backspace)).setOnClickListener(new CommonButtonClickListener(this.expressText, this.resultTxt, ButtonType.BACKSPACE, new String[0]));
        ((Button) this._Dialog.findViewById(R.id.clear)).setOnClickListener(new CommonButtonClickListener(this.expressText, this.resultTxt, ButtonType.CLEAR, new String[0]));
        ((Button) this._Dialog.findViewById(R.id.equals)).setOnClickListener(new CommonButtonClickListener(this.expressText, this.resultTxt, ButtonType.EQUALS, new String[0]));
        ((Button) this._Dialog.findViewById(R.id.more)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalculateDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                List<Map<String, String>> tmpList = CalculateDialog.initfunctions();
                List<String> tmpList2 = new ArrayList<>();
                for (Map<String, String> tmpMap : tmpList) {
                    tmpList2.add(String.valueOf(String.valueOf(tmpMap.get("FuncName"))) + "\r\n" + String.valueOf(tmpMap.get("FuncExpress")));
                }
                new AlertDialog.Builder(CalculateDialog.this._Dialog.getContext(), 3).setTitle("选择计算符号或函数:").setSingleChoiceItems((String[]) tmpList2.toArray(new String[tmpList2.size()]), -1, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalculateDialog.2.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface arg02, int arg1) {
                        if (arg1 >= 0) {
                            try {
                                List<Map<String, String>> tmpList3 = CalculateDialog.initfunctions();
                                if (arg1 < tmpList3.size()) {
                                    Map<String, String> tmpMap2 = tmpList3.get(arg1);
                                    CalculateDialog.this.expressText.getEditableText().append((CharSequence) (String.valueOf(tmpMap2.get("FuncName")) + ("true".equalsIgnoreCase(tmpMap2.get("NeedLParenthesis")) ? "(" : "")));
                                }
                            } catch (Exception e) {
                            }
                        }
                        arg02.dismiss();
                    }
                }).show();
            }
        });
        ((Button) this._Dialog.findViewById(R.id.indepCalButton)).setOnClickListener(new CommonButtonClickListener(this.expressText, this.resultTxt, ButtonType.EQUALS, new String[0]));
        ((Button) this._Dialog.findViewById(R.id.indepClearButton)).setOnClickListener(new CommonButtonClickListener(this.expressText, this.resultTxt, ButtonType.CLEAR, new String[0]));
        for (Map.Entry<Integer, String> entry : appendButtons.entrySet()) {
            ((Button) this._Dialog.findViewById(entry.getKey().intValue())).setOnClickListener(new CommonButtonClickListener(this.expressText, this.resultTxt, ButtonType.SYNTHETIC_COMMON_APPEND, entry.getValue()));
        }
        try {
            initNumButtons();
        } catch (Exception e) {
        }
    }

    /* JADX DEBUG: TODO: convert one arg to string using `String.valueOf()`, args: [(r2v1 'i' int A[D('i' int)])] */
    private void initNumButtons() throws Exception {
        for (int i = 0; i < 10; i++) {
            ((Button) this._Dialog.findViewById(R.id.class.getDeclaredField("num" + i).getInt(null))).setOnClickListener(new CommonButtonClickListener(this.expressText, this.resultTxt, ButtonType.SYNTHETIC_COMMON_APPEND, new StringBuilder().append(i).toString()));
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalculateDialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                if (CalculateDialog.this.m_Callback != null) {
                    CalculateDialog.this._Dialog.SetHeadButtons("1,2130837858,确定,确定", CalculateDialog.this.pCallback);
                }
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: private */
    public static List<Map<String, String>> initfunctions() {
        if (calFunctionsArray == null) {
            calFunctionsArray = new ArrayList();
            String[][] funcArray = {new String[]{"&", "按位与,示例:1&3", "false"}, new String[]{"|", "按位或,示例:1|3", "false"}, new String[]{"~", "按位非,示例:~1", "false"}, new String[]{"^", "按位异或,示例:2^3", "false"}, new String[]{"sin", "正弦,示例:sin(30)", "true"}, new String[]{"asin", "反正弦,示例:asin(0.5)", "true"}, new String[]{"cos", "余弦,示例:cos(60)", "true"}, new String[]{"acos", "反余弦,示例:acos(0.5)", "true"}, new String[]{"tan", "正切,示例:tan(45)", "true"}, new String[]{"atan", "反正切,示例:atan(0.5)", "true"}, new String[]{"abs", "绝对值,示例:abs(-25)", "true"}, new String[]{"exp", "e的n次幂,示例:exp(3.5)", "true"}, new String[]{"ln", "对数,底数为e,示例:ln(9.8)", "true"}, new String[]{"log", "对数,底数为10,示例:log(100)", "true"}, new String[]{"pow", "pow(a,b)表示a的b次幂,示例:pow(4,5)", "true"}};
            for (String[] func : funcArray) {
                if (func != null && func.length == 3) {
                    Map<String, String> map = new HashMap<>();
                    map.put("FuncName", func[0]);
                    map.put("FuncExpress", func[1]);
                    map.put("NeedLParenthesis", func[2]);
                    calFunctionsArray.add(map);
                }
            }
        }
        return calFunctionsArray;
    }
}
