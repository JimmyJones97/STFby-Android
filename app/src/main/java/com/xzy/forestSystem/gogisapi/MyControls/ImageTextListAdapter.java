package  com.xzy.forestSystem.gogisapi.MyControls;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;
import java.util.HashMap;
import java.util.List;

public class ImageTextListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ICallback m_Callback = null;
    private List<HashMap<String, Object>> m_DataList = null;
    private int m_LayoutId = 0;
    private String[] m_ObjField;
    private int m_SelectItemIndex = -1;
    private int[] m_ViewId;
    private boolean showCheckbox = true;
    private float textSize = 20.0f;

    public ImageTextListAdapter(Context context, List<HashMap<String, Object>> data, boolean isShowCheckbox, int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt) {
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(context);
        }
        this.m_DataList = data;
        this.showCheckbox = isShowCheckbox;
        this.m_LayoutId = paramInt;
        this.m_ObjField = paramArrayOfString;
        this.m_ViewId = paramArrayOfInt;
    }

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public void SetSelectItemIndex(int paramInt) {
        this.m_SelectItemIndex = paramInt;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.m_DataList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int paramInt) {
        return this.m_DataList.get(paramInt);
    }

    @Override // android.widget.Adapter
    public long getItemId(int paramInt) {
        return 0;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean UpdateCheckBoxDataListValue(CheckBox paramCheckBox, String paramString) {
        String[] arrayOfString = paramString.split(",");
        int i = Integer.parseInt(arrayOfString[0]);
        int j = Integer.parseInt(arrayOfString[1]);
        HashMap tmpHashMap = (HashMap) getItem(i);
        boolean tempBool01 = paramCheckBox.isChecked();
        if (tempBool01 != Boolean.parseBoolean(tmpHashMap.get(this.m_ObjField[j]).toString())) {
            tmpHashMap.put(this.m_ObjField[j], Boolean.valueOf(tempBool01));
        }
        return true;
    }

    private void updateListViewItem(View paramView, int paramInt) {
        Bitmap tempBmp;
        HashMap tmpHashMap = (HashMap) getItem(paramInt);
        int tempICount = this.m_ViewId.length;
        for (int tempI = 0; tempI < tempICount; tempI++) {
            View localView = paramView.findViewById(this.m_ViewId[tempI]);
            String str = localView.getClass().getName();
            if (str.equals("android.widget.TextView")) {
                ((TextView) localView).setText(tmpHashMap.get(this.m_ObjField[tempI]).toString());
            } else if (str.equals("android.widget.LinearLayout")) {
                CheckBox localCheckBox = (CheckBox) localView.findViewById(R.id.imgtxt_itemcheckbox);
                localCheckBox.setTag(String.valueOf(paramInt) + "," + tempI);
                localCheckBox.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.ImageTextListAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View paramView2) {
                        CheckBox localCheckBox2 = (CheckBox) paramView2;
                        ImageTextListAdapter.this.UpdateCheckBoxDataListValue(localCheckBox2, localCheckBox2.getTag().toString());
                    }
                });
                localCheckBox.setChecked(Boolean.parseBoolean(tmpHashMap.get(this.m_ObjField[tempI]).toString()));
            } else if (str.equals("android.widget.ImageView")) {
                ImageView localImageView = (ImageView) localView;
                localImageView.setTag(Integer.valueOf(paramInt));
                if (!(tmpHashMap.get(this.m_ObjField[tempI]) == null || (tempBmp = (Bitmap) tmpHashMap.get(this.m_ObjField[tempI])) == null)) {
                    localImageView.setImageBitmap(tempBmp);
                }
                localImageView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.ImageTextListAdapter.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View paramView2) {
                        paramView2.getTag();
                    }
                });
            }
        }
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.mInflater.inflate(this.m_LayoutId, parent, false);
            HashMap hashMap = (HashMap) getItem(position);
            if (this.m_ViewId.length > 0) {
                int tempICount = this.m_ViewId.length;
                for (int tempI = 0; tempI < tempICount; tempI++) {
                    convertView.findViewById(this.m_ViewId[tempI]).setVisibility(0);
                }
            }
        }
        if (position == this.m_SelectItemIndex) {
            convertView.setSelected(true);
            convertView.setPressed(true);
            convertView.setBackgroundColor(-10185235);
        } else {
            convertView.setBackgroundColor(0);
        }
        updateListViewItem(convertView, position);
        return convertView;
    }
}
