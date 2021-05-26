package  com.xzy.forestSystem.gogisapi.MyControls;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MyTableFactory {
    private RelativeLayout m_HeaderView_Scroll = null;
    private ListView m_ListView_Scroll = null;
    private MyTableHeader m_ReportHeader = null;

    public void BindDataToListView(List<HashMap<String, Object>> paramList) {
        BindDataToListView(paramList, null);
    }

    public ListView getListView_Scroll() {
        return this.m_ListView_Scroll;
    }

    public void BindDataToListView(List<HashMap<String, Object>> paramList, ICallback tmpICallback) {
        List localList = this.m_ReportHeader.m_HeaderInfoList;
        if (localList != null) {
            int count = localList.size();
            String[] arrayOfString = new String[localList.size()];
            for (int i = 0; i < count; i++) {
                arrayOfString[i] = "D" + (i + 1);
            }
            BindDataToListView(paramList, arrayOfString, tmpICallback, -2);
        }
    }

    public void BindDataToListView(List<HashMap<String, Object>> paramList, String[] fieldIDNameArray, ICallback tmpICallback) {
        BindDataToListView(paramList, fieldIDNameArray, tmpICallback, -2);
    }

    public void BindDataToListView(List<HashMap<String, Object>> paramList, String[] fieldIDNameArray, ICallback tmpICallback, int cellHeight) {
        if (this.m_ReportHeader.m_HeaderInfoList != null) {
            MyTableAdapter tempAdapter = new MyTableAdapter(this.m_ListView_Scroll.getContext(), paramList, R.layout.x_mytable_row, fieldIDNameArray, this.m_ReportHeader);
            tempAdapter.SetCallback(tmpICallback);
            tempAdapter.SetItemHeight(cellHeight);
            this.m_ListView_Scroll.setAdapter((ListAdapter) tempAdapter);
            this.m_ListView_Scroll.setDivider(null);
        }
    }

    public void SetHeaderListView(View paramView, String paramString) {
        SetHeaderListView(paramView, paramString, null, null, null, null);
    }

    public void SetHeaderListView(View paramView, String paramString, ICallback tmpICallback) {
        SetHeaderListView(paramView, paramString, null, null, null, tmpICallback);
    }

    public void SetHeaderListView(View paramView, String paramString, String columnsName, String columnsType, int[] columnsWidth, final ICallback tmpICallback) {
        this.m_HeaderView_Scroll = (RelativeLayout) paramView.findViewById(R.id.rt_header_scroll);
        this.m_HeaderView_Scroll.removeAllViewsInLayout();
        this.m_ListView_Scroll = (ListView) paramView.findViewById(R.id.rt_listview_scroll);
        this.m_ListView_Scroll.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> paramAdapterView, View paramView2, int paramInt, long paramLong) {
                MyTableAdapter localv1_HeaderListViewAdpter = (MyTableAdapter) ((ListView) paramAdapterView).getAdapter();
                localv1_HeaderListViewAdpter.SetSelectItemIndex(paramInt);
                localv1_HeaderListViewAdpter.notifyDataSetInvalidated();
                if (tmpICallback != null) {
                    tmpICallback.OnClick("列表选项", localv1_HeaderListViewAdpter.getItem(paramInt));
                }
            }
        });
        this.m_ListView_Scroll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory.2
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> paramAdapterView, View paramView2, int paramInt, long paramLong) {
                MyTableAdapter localv1_HeaderListViewAdpter = (MyTableAdapter) ((ListView) paramAdapterView).getAdapter();
                localv1_HeaderListViewAdpter.SetSelectItemIndex(paramInt);
                localv1_HeaderListViewAdpter.notifyDataSetInvalidated();
                if (tmpICallback != null) {
                    tmpICallback.OnClick("列表选项", localv1_HeaderListViewAdpter.getItem(paramInt));
                }
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        CreateHeaders(this.m_HeaderView_Scroll, paramString, columnsName, columnsType, columnsWidth);
    }

    public void SetHeaderVisible(boolean visible) {
        if (this.m_HeaderView_Scroll == null) {
            return;
        }
        if (visible) {
            this.m_HeaderView_Scroll.setVisibility(0);
        } else {
            this.m_HeaderView_Scroll.setVisibility(8);
        }
    }

    private List<Integer> ConverDipToPix(View paramView, Integer[] paramArrayOfInt) {
        ArrayList localArrayList = new ArrayList();
        if (paramArrayOfInt.length > 0) {
            for (Integer num : paramArrayOfInt) {
                int k = num.intValue();
                if (k < 0) {
                    localArrayList.add(Integer.valueOf(k));
                } else {
                    localArrayList.add(Integer.valueOf((int) TypedValue.applyDimension(1, (float) k, paramView.getResources().getDisplayMetrics())));
                }
            }
        }
        return localArrayList;
    }

    public void CreateHeaders(RelativeLayout paramRelativeLayout, String paramString, String columnsName, String columnsType, int[] columnsWidth) {
        String[] tempColumNames;
        try {
            this.m_ReportHeader = new MyTableHeader();
            ArrayList localArrayList = new ArrayList();
            List<Integer> localList = null;
            if (paramString.equals("自定义") && (tempColumNames = columnsName.split(",")) != null && tempColumNames.length > 0) {
                int count = tempColumNames.length;
                String[] tempColumTypes = columnsType.split(",");
                if (tempColumTypes != null && columnsWidth.length == count && tempColumTypes.length == count) {
                    List<Integer> tempList = new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        String tempColumnType = tempColumTypes[i];
                        if (tempColumnType.equals("checkbox")) {
                            localArrayList.add(new MyTableHeaderInfo(tempColumNames[i], 1, 1, i + 1, i + 1, false, EMyTableColumnType.CHECKBOX));
                        } else if (tempColumnType.equals("image")) {
                            localArrayList.add(new MyTableHeaderInfo(tempColumNames[i], 1, 1, i + 1, i + 1, false, EMyTableColumnType.IMAGE));
                        } else {
                            localArrayList.add(new MyTableHeaderInfo(tempColumNames[i], 1, 1, i + 1, i + 1, false, EMyTableColumnType.TEXT));
                        }
                        tempList.add(Integer.valueOf(columnsWidth[i]));
                    }
                    localList = ConverDipToPix(paramRelativeLayout, (Integer[]) tempList.toArray(new Integer[0]));
                }
            }
            localList.iterator();
            int i4 = (int) TypedValue.applyDimension(1, 40.0f, paramRelativeLayout.getResources().getDisplayMetrics());
            int tempTotalWidth = 0;
            for (Integer num : localList) {
                tempTotalWidth += num.intValue();
            }
            int k = ((LinearLayout) paramRelativeLayout.getParent()).getLayoutParams().width;
            if (tempTotalWidth > 0) {
                k = tempTotalWidth;
            } else if (k <= 0) {
                k = PubVar.ScreenWidth - (i4 / 2);
            }
            for (int i2 = 0; i2 < localList.size(); i2++) {
                int i22 = localList.get(i2).intValue();
                if (i22 < 0) {
                    try {
                        localList.set(i2, Integer.valueOf((int) ((((double) Math.abs(i22)) / 100.0d) * ((double) k))));
                    } catch (Exception e) {
                    }
                }
            }
            View localView = ((LinearLayout) paramRelativeLayout.getParent()).getChildAt(1);
            ViewGroup.LayoutParams localLayoutParams = localView.getLayoutParams();
            localLayoutParams.width = k;
            localView.setLayoutParams(localLayoutParams);
            this.m_ReportHeader.SetHeaderInfoList(localArrayList);
            this.m_ReportHeader.SetHeaderWidthList(localList);
            Iterator localIterator3 = localArrayList.iterator();
            ViewGroup.LayoutParams localLayoutParams2 = paramRelativeLayout.getLayoutParams();
            localLayoutParams2.width = k;
            paramRelativeLayout.setLayoutParams(localLayoutParams2);
            int tempStartX = 0;
            int tempI = 0;
            while (localIterator3.hasNext()) {
                MyTableHeaderInfo localReportHeaderInfo = (MyTableHeaderInfo) localIterator3.next();
                int i3 = localReportHeaderInfo.StartCol;
                int i5 = localReportHeaderInfo.EndCol;
                TextView localTextView = new TextView(paramRelativeLayout.getContext());
                localTextView.setText(localReportHeaderInfo.Text);
                localTextView.setBackgroundResource(R.drawable.mytable_header_bg);
                localTextView.setGravity(17);
                localTextView.setTextAppearance(paramRelativeLayout.getContext(), 16842816);
                localTextView.setTextColor(-1);
                RelativeLayout.LayoutParams localLayoutParams1 = new RelativeLayout.LayoutParams(0, 0);
                localLayoutParams1.leftMargin = tempStartX;
                localLayoutParams1.topMargin = (localReportHeaderInfo.StartRow - 1) * i4;
                localLayoutParams1.width = localList.get(tempI).intValue();
                localLayoutParams1.height = ((localReportHeaderInfo.EndRow - localReportHeaderInfo.StartRow) + 1) * i4;
                paramRelativeLayout.addView(localTextView, localLayoutParams1);
                tempStartX += localList.get(tempI).intValue();
                tempI++;
            }
        } catch (Exception ex) {
            Log.v("CreateReport", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    public void SetSelectItemIndex(int paramInt, ICallback tmpICallback) {
        if (this.m_ListView_Scroll != null) {
            MyTableAdapter localv1_HeaderListViewAdpter = (MyTableAdapter) this.m_ListView_Scroll.getAdapter();
            localv1_HeaderListViewAdpter.SetSelectItemIndex(paramInt);
            localv1_HeaderListViewAdpter.notifyDataSetInvalidated();
            if (tmpICallback != null) {
                tmpICallback.OnClick("列表选项", localv1_HeaderListViewAdpter.getItem(paramInt));
            }
        }
    }

    public void notifyDataSetChanged() {
        if (this.m_ListView_Scroll != null) {
            ((MyTableAdapter) this.m_ListView_Scroll.getAdapter()).notifyDataSetChanged();
        }
    }

    public void notifyDataSetInvalidated() {
        if (this.m_ListView_Scroll != null) {
            ((MyTableAdapter) this.m_ListView_Scroll.getAdapter()).notifyDataSetInvalidated();
        }
    }

    public void setClickItemReturn1(boolean needReturn) {
        if (this.m_ListView_Scroll != null) {
            ((MyTableAdapter) this.m_ListView_Scroll.getAdapter()).SetClickItemReturn(needReturn);
        }
    }

    public void SetClickItemReturnColumns(String columns) {
        if (this.m_ListView_Scroll != null) {
            ((MyTableAdapter) this.m_ListView_Scroll.getAdapter()).SetClickItemReturnColumns(columns);
        }
    }

    public int getSelectItemIndex() {
        if (this.m_ListView_Scroll == null) {
            return -1;
        }
        return ((MyTableAdapter) this.m_ListView_Scroll.getAdapter()).getSelectItemIndex();
    }
}
