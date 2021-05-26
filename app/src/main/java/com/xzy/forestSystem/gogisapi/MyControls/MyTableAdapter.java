package  com.xzy.forestSystem.gogisapi.MyControls;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.core.view.ViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyTableAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private String m_AllowClickColsStr = "";
    private ICallback m_Callback = null;
    private boolean m_CellHeigthWrap = false;
    private boolean m_ClickItemReturn = false;
    private int m_ColumnCount = 0;
    private Context m_Context = null;
    private List<HashMap<String, Object>> m_DataList = null;
    private int m_ItemHeight = ((int) (50.0f * PubVar.ScaledDensity));
    private int m_LayoutId = 0;
    private String[] m_ObjField;
    private MyTableHeader m_ReportHeader = null;
    private int m_SelectItemIndex = -1;
    private List<View> m_ViewList = new ArrayList();

    public MyTableAdapter(Context paramContext, List<HashMap<String, Object>> paramList, int paramInt, String[] fieldIDNameArray, MyTableHeader myTableHeader) {
        this.m_Context = paramContext;
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(paramContext);
        }
        this.m_DataList = paramList;
        this.m_LayoutId = paramInt;
        this.m_ObjField = fieldIDNameArray;
        if (fieldIDNameArray != null) {
            this.m_ColumnCount = fieldIDNameArray.length;
        }
        this.m_ReportHeader = myTableHeader;
    }

    public void SetItemHeight(int height) {
        this.m_ItemHeight = height;
    }

    public void SetClickItemReturn(boolean value) {
        this.m_ClickItemReturn = value;
    }

    public void SetClickItemReturnColumns(String columns) {
        this.m_AllowClickColsStr = columns;
        this.m_ClickItemReturn = true;
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

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public void SetSelectItemIndex(int paramInt) {
        this.m_SelectItemIndex = paramInt;
        if (this.m_Callback != null) {
            this.m_Callback.OnClick("单击选择行", (HashMap) getItem(paramInt));
        }
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

    private void updateListViewItem(View paramView, int rowIndex) {
        Bitmap tempBmp;
        HashMap tmpHashMap = (HashMap) getItem(rowIndex);
        int tmpDefaultColor = ViewCompat.MEASURED_STATE_MASK;
        if (tmpHashMap.containsKey("Text_Color")) {
            try {
                tmpDefaultColor = Integer.parseInt(String.valueOf(tmpHashMap.get("Text_Color")));
            } catch (Exception e) {
            }
        }
        int tempICount = this.m_ViewList.size();
        for (int tempI = 0; tempI < tempICount; tempI++) {
            View localView = this.m_ViewList.get(tempI);
            String str = localView.getClass().getName();
            if (str.equals("android.widget.TextView")) {
                Object tempObj01 = tmpHashMap.get(this.m_ObjField[tempI]);
                if (tempObj01 != null) {
                    ((TextView) localView).setText(tempObj01.toString());
                } else {
                    ((TextView) localView).setText("");
                }
                ((TextView) localView).setTextColor(tmpDefaultColor);
                localView.setTag(String.valueOf(rowIndex) + "," + tempI + "," + tempObj01.toString());
                if (this.m_ClickItemReturn && this.m_AllowClickColsStr.contains(String.valueOf(String.valueOf(tempI)) + ",")) {
                    localView.setBackgroundResource(R.drawable.tablecell_border);
                    localView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.MyTableAdapter.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View paramView2) {
                            if (MyTableAdapter.this.m_Callback != null && paramView2.getTag() != null) {
                                MyTableAdapter.this.m_Callback.OnClick("单击单元格", paramView2.getTag().toString());
                            }
                        }
                    });
                }
            } else if (str.equals("android.widget.CheckBox")) {
                CheckBox localCheckBox = (CheckBox) localView;
                localCheckBox.setTag(String.valueOf(rowIndex) + "," + tempI);
                localCheckBox.setTextColor(tmpDefaultColor);
                localCheckBox.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.MyTableAdapter.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View paramView2) {
                        CheckBox localCheckBox2 = (CheckBox) paramView2;
                        MyTableAdapter.this.UpdateCheckBoxDataListValue(localCheckBox2, localCheckBox2.getTag().toString());
                    }
                });
                localCheckBox.setChecked(Boolean.parseBoolean(tmpHashMap.get(this.m_ObjField[tempI]).toString()));
            } else if (str.equals("android.widget.ImageView")) {
                ImageView localImageView = (ImageView) localView;
                localImageView.setTag(String.valueOf(rowIndex) + "," + tempI + ",null");
                if (!(tmpHashMap.get(this.m_ObjField[tempI]) == null || (tempBmp = (Bitmap) tmpHashMap.get(this.m_ObjField[tempI])) == null)) {
                    localImageView.setImageBitmap(tempBmp);
                }
                if (this.m_ClickItemReturn && this.m_AllowClickColsStr.contains(String.valueOf(String.valueOf(tempI)) + ",")) {
                    localImageView.setBackgroundResource(R.drawable.tablecell_border);
                    localImageView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.MyTableAdapter.3
                        @Override // android.view.View.OnClickListener
                        public void onClick(View paramView2) {
                            if (paramView2.getTag() != null && MyTableAdapter.this.m_Callback != null) {
                                MyTableAdapter.this.m_Callback.OnClick("单击单元格", paramView2.getTag().toString());
                            }
                        }
                    });
                }
            }
        }
    }

    @Override // android.widget.Adapter
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        try {
            paramView = (LinearLayout) this.mInflater.inflate(this.m_LayoutId, (ViewGroup) null);
            if (paramView != null) {
                LinearLayout tmpLayout = (LinearLayout) paramView;
                if (this.m_CellHeigthWrap) {
                    LinearLayout.LayoutParams tmpLyParams = (LinearLayout.LayoutParams) tmpLayout.getLayoutParams();
                    if (tmpLyParams == null) {
                        tmpLyParams = new LinearLayout.LayoutParams(-1, -2);
                    }
                    tmpLyParams.height = -2;
                } else if (this.m_ItemHeight > 0) {
                    LinearLayout.LayoutParams tmpLyParams2 = (LinearLayout.LayoutParams) tmpLayout.getLayoutParams();
                    if (tmpLyParams2 == null) {
                        tmpLyParams2 = new LinearLayout.LayoutParams(-1, -2);
                    }
                    tmpLyParams2.height = this.m_ItemHeight;
                }
                this.m_ViewList = new ArrayList();
                for (int i = 0; i < this.m_ColumnCount; i++) {
                    View tmpView = null;
                    MyTableHeaderInfo tmpHeaderInfo = this.m_ReportHeader.m_HeaderInfoList.get(i);
                    if (tmpHeaderInfo.ItemType == EMyTableColumnType.TEXT) {
                        tmpView = new TextView(this.m_Context);
                        ((TextView) tmpView).setTextSize(16.0f);
                        ((TextView) tmpView).setTextColor(ViewCompat.MEASURED_STATE_MASK);
                        ((TextView) tmpView).setEllipsize(TextUtils.TruncateAt.END);
                        ((TextView) tmpView).setGravity(19);
                    } else if (tmpHeaderInfo.ItemType == EMyTableColumnType.IMAGE) {
                        tmpView = new ImageView(this.m_Context);
                        ((ImageView) tmpView).setScaleType(ImageView.ScaleType.CENTER);
                    } else if (tmpHeaderInfo.ItemType == EMyTableColumnType.CHECKBOX) {
                        tmpView = new CheckBox(this.m_Context);
                        ((CheckBox) tmpView).setGravity(17);
                        tmpView.setFocusable(false);
                    }
                    if (tmpView != null) {
                        tmpView.setBackgroundResource(R.drawable.mytablecell_bg);
                        tmpView.setVisibility(0);
                        LinearLayout.LayoutParams tmpParams = (LinearLayout.LayoutParams) tmpView.getLayoutParams();
                        if (tmpParams == null) {
                            tmpParams = new LinearLayout.LayoutParams(-2, -1);
                        }
                        tmpParams.width = this.m_ReportHeader.m_HeaderWidthList.get(i).intValue();
                        if (i == 0) {
                            if (this.m_ItemHeight > 0) {
                                tmpParams.height = this.m_ItemHeight;
                            } else if (tmpHeaderInfo.ItemType == EMyTableColumnType.TEXT) {
                                tmpParams.height = (int) (1.5d * ((double) PubVar.TextHeight20));
                            }
                        }
                        tmpView.setLayoutParams(tmpParams);
                        tmpLayout.addView(tmpView);
                        this.m_ViewList.add(tmpView);
                    }
                }
            }
            HashMap tmpHashMap = (HashMap) getItem(paramInt);
            if (tmpHashMap.containsKey("Bg_Color")) {
                paramView.setBackgroundColor(Integer.parseInt(String.valueOf(tmpHashMap.get("Bg_Color"))));
            } else if (paramInt == this.m_SelectItemIndex) {
                paramView.setSelected(true);
                paramView.setPressed(true);
                paramView.setBackgroundResource(R.color.table_selected_color);
            } else {
                paramView.setBackgroundColor(0);
            }
            updateListViewItem(paramView, paramInt);
        } catch (Exception e) {
        }
        return paramView;
    }

    public void SetCellHeigthWrap(boolean value) {
        this.m_CellHeigthWrap = value;
    }

    public int getSelectItemIndex() {
        return this.m_SelectItemIndex;
    }
}
