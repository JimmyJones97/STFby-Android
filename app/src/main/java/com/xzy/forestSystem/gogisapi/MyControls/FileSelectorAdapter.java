package  com.xzy.forestSystem.gogisapi.MyControls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSelectorAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private ICallback m_Callback = null;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public final class MyComponet {
        public CheckBox checkBox;
        public ImageView imageView;
        public TextView textView;

        public MyComponet() {
        }
    }

    public FileSelectorAdapter(Context context2, List<Map<String, Object>> data2) {
        this.context = context2;
        this.data = data2;
        this.layoutInflater = LayoutInflater.from(context2);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.data.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int arg0) {
        return this.data.get(arg0);
    }

    @Override // android.widget.Adapter
    public long getItemId(int arg0) {
        return (long) arg0;
    }

    @SuppressLint({"NewApi"})
    private void updateListViewItem(View paramView, int paramInt) {
        HashMap tmpHashMap = (HashMap) getItem(paramInt);
        CheckBox localCheckBox = (CheckBox) paramView.findViewById(R.id.visiblecheckBox);
        if (Boolean.parseBoolean(tmpHashMap.get("visible").toString())) {
            localCheckBox.setVisibility(0);
            localCheckBox.setTag(Integer.valueOf(paramInt));
            localCheckBox.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.FileSelectorAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View paramView2) {
                    CheckBox localCheckBox2 = (CheckBox) paramView2;
                    FileSelectorAdapter.this.UpdateCheckBoxDataListValue(localCheckBox2, localCheckBox2.getTag().toString());
                    if (FileSelectorAdapter.this.m_Callback != null) {
                        FileSelectorAdapter.this.m_Callback.OnClick("文件选择勾选返回", localCheckBox2.getTag().toString());
                    }
                }
            });
            localCheckBox.setChecked(Boolean.parseBoolean(tmpHashMap.get("isselected").toString()));
        } else {
            localCheckBox.setVisibility(8);
        }
        TextView tempTxtView = (TextView) paramView.findViewById(R.id.infoText);
        tempTxtView.setText(tmpHashMap.get("name").toString());
        tempTxtView.setTag(String.valueOf(paramInt) + ",文件名");
        ImageView tempImgView01 = (ImageView) paramView.findViewById(R.id.image);
        tempImgView01.setTag(String.valueOf(paramInt) + ",图片");
        tempImgView01.setBackgroundResource(Integer.parseInt(tmpHashMap.get("image").toString()));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean UpdateCheckBoxDataListValue(CheckBox paramCheckBox, String paramString) {
        HashMap tmpHashMap = (HashMap) getItem(Integer.parseInt(paramString));
        boolean tempBool01 = paramCheckBox.isChecked();
        if (tempBool01 == Boolean.parseBoolean(tmpHashMap.get("isselected").toString())) {
            return true;
        }
        tmpHashMap.put("isselected", Boolean.valueOf(tempBool01));
        return true;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            try {
                convertView = this.layoutInflater.inflate(R.layout.fileselector_adpater, (ViewGroup) null);
            } catch (Exception ex) {
                Common.Log("getView", "错误:" + ex.toString() + "-->" + ex.getMessage());
            }
        }
        convertView.setBackgroundColor(0);
        updateListViewItem(convertView, position);
        return convertView;
    }
}
