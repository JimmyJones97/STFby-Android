package  com.xzy.forestSystem.gogisapi.MyControls;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Input_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private AutoCompleteTextView m_InputEditTxt;
    private Object m_ReturnTag;
    private Object m_TagValue;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public Input_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_ReturnTag = null;
        this.m_InputEditTxt = null;
        this.m_TagValue = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.Input_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("确定")) {
                    String tempValue = Input_Dialog.this.m_InputEditTxt.getText().toString();
                    try {
                        if (Input_Dialog.this.m_InputEditTxt.getTag() != null) {
                            String[] tmpTagsStrings = (String[]) Input_Dialog.this.m_InputEditTxt.getTag();
                            String tmpString01 = tmpTagsStrings[1];
                            if (tmpString01 == null) {
                                tmpString01 = "";
                            }
                            String tmpName = Input_Dialog.this.m_InputEditTxt.getText().toString().trim();
                            if (tmpName.length() > 0 && !tmpString01.contains(String.valueOf(tmpName) + "|")) {
                                if (tmpString01.length() > 0) {
                                    tmpName = String.valueOf(tmpName) + "|";
                                }
                                tmpString01 = String.valueOf(tmpName) + tmpString01;
                            }
                            HashMap<String, String> tempHash01 = new HashMap<>();
                            tempHash01.put("F2", tmpString01);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara(tmpTagsStrings[0], tempHash01);
                        }
                    } catch (Exception e) {
                    }
                    if (Input_Dialog.this.m_Callback != null) {
                        Input_Dialog.this.m_Callback.OnClick("输入参数", new Object[]{Input_Dialog.this.m_ReturnTag, tempValue, Input_Dialog.this.m_TagValue});
                    }
                    Input_Dialog.this._Dialog.dismiss();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.input_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("参数输入");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.m_InputEditTxt = (AutoCompleteTextView) this._Dialog.findViewById(R.id.ed_inputpara);
    }

    public void SetReturnTag(Object object) {
        this.m_ReturnTag = object;
    }

    public void SetTagValue(Object obj) {
        this.m_TagValue = obj;
    }

    public void SetHistoryHashTag(String tag) {
        String tempStr;
        String[] arr;
        String[] tmpTagStrings = {tag, ""};
        try {
            HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara(tag);
            if (!(tempHashMap == null || (tempStr = tempHashMap.get("F2")) == null || tempStr.equals("") || (arr = tempStr.split("\\|")) == null || arr.length <= 0)) {
                StringBuilder tmpStringBuilder = new StringBuilder();
                List<String> tmpList = new ArrayList<>();
                int tmpTid = 0;
                for (String tmpStr01String : arr) {
                    String tmpStr02String = tmpStr01String.trim();
                    if (tmpStr02String.length() > 0 && !tmpStringBuilder.toString().contains(tmpStr02String)) {
                        tmpList.add(tmpStr02String);
                        if (tmpStringBuilder.length() > 0) {
                            tmpStringBuilder.append("|");
                        }
                        tmpStringBuilder.append(tmpStr02String);
                        tmpTid++;
                        if (tmpTid > 10) {
                            break;
                        }
                    }
                }
                tmpList.add("清空历史记录");
                tmpTagStrings[1] = tmpStringBuilder.toString();
                this.m_InputEditTxt.setAdapter(new ArrayAdapter<>(this._Dialog.getContext(), (int) R.layout.autocompletetextview_layout, (String[]) tmpList.toArray(new String[tmpList.size()])));
                if (tmpList.size() > 1) {
                    this.m_InputEditTxt.setText(tmpList.get(0));
                }
            }
        } catch (Exception e) {
        }
        this.m_InputEditTxt.setTag(tmpTagStrings);
    }

    public void setValues(String title, String paraName, String paraValue, String tipInfo) {
        this._Dialog.SetCaption(title);
        ((TextView) this._Dialog.findViewById(R.id.tv_paraName)).setText(paraName);
        this.m_InputEditTxt.setText(paraValue);
        if (tipInfo == null || tipInfo.equals("")) {
            ((TextView) this._Dialog.findViewById(R.id.tv_tipInfo)).setVisibility(8);
            return;
        }
        ((TextView) this._Dialog.findViewById(R.id.tv_tipInfo)).setVisibility(0);
        ((TextView) this._Dialog.findViewById(R.id.tv_tipInfo)).setText(tipInfo);
    }

    public void setValues(String title, String paraName, String paraValue) {
        setValues(title, paraName, paraValue, null);
    }

    public void setInputType(int type) {
        this.m_InputEditTxt.setInputType(type);
    }

    public void ShowDialog() {
        this._Dialog.show();
    }
}
