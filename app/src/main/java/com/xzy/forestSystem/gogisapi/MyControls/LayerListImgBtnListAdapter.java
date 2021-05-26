package  com.xzy.forestSystem.gogisapi.MyControls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.SpannableString;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LayerListImgBtnListAdapter extends BaseAdapter {
    private View.OnClickListener ImgBtnClickListener = new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.LayerListImgBtnListAdapter.2
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String[] tempStrs;
            if (!(view.getTag() == null || LayerListImgBtnListAdapter.this.m_Callback == null || (tempStrs = view.getTag().toString().split(",")) == null || tempStrs.length <= 1)) {
                int tempIndex = Integer.parseInt(tempStrs[0]);
                if (LayerListImgBtnListAdapter.this.m_Callback != null) {
                    String tempValue = "图层列表," + tempStrs[1] + "," + tempIndex;
                    if (tempStrs.length > 2) {
                        for (int i = 2; i < tempStrs.length; i++) {
                            tempValue = String.valueOf(tempValue) + "," + tempStrs[i];
                        }
                    }
                    LayerListImgBtnListAdapter.this.m_Callback.OnClick(tempValue, null);
                }
            }
        }
    };
    private boolean _NeedCheckBoxCheckReturn = false;
    private Context context;
    private boolean hasAdjustTextWidth = false;
    private int layerListType = 0;
    private LayoutInflater mInflater;
    private ICallback m_Callback = null;
    private List<HashMap<String, Object>> m_DataList = null;
    private boolean m_IsNormalText = true;
    private boolean m_IsTransparentAnim = false;
    private int m_LayoutId = 0;
    private int m_SelectItemIndex = -1;
    private List<Integer> m_SelectItemList = new ArrayList();
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.LayerListImgBtnListAdapter.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
            if (paramString.contains("对话框返回")) {
                String[] tempStr = paramString.split("-");
                if (pObject != null && tempStr != null && tempStr.length > 3) {
                    int tempIndex = Integer.parseInt(tempStr[2]);
                    int tempInt = Integer.parseInt(tempStr[3]);
                    HashMap tmpHashMap = (HashMap) LayerListImgBtnListAdapter.this.getItem(tempIndex);
                    if (Integer.parseInt(tmpHashMap.get("D3").toString()) != tempInt) {
                        tmpHashMap.put("D3", Integer.valueOf(tempInt));
                        if (LayerListImgBtnListAdapter.this.m_IsTransparentAnim) {
                            ((NumberCircleProgressBar) ((View) pObject).findViewById(R.id.progressBar_transp)).SetProgressAnim(tempInt);
                        } else {
                            ((NumberCircleProgressBar) ((View) pObject).findViewById(R.id.progressBar_transp)).setProgress(tempInt);
                        }
                    }
                }
            }
        }
    };

    public void AddSelectIndex(int index) {
        if (!this.m_SelectItemList.contains(Integer.valueOf(index))) {
            this.m_SelectItemList.add(Integer.valueOf(index));
        }
    }

    public void RemoveSelectIndex(int index) {
        if (!this.m_SelectItemList.contains(Integer.valueOf(index))) {
            this.m_SelectItemList.remove(index);
        }
    }

    public void AddOrRemoveSelectIndex(int index) {
        int i = this.m_SelectItemList.indexOf(Integer.valueOf(index));
        if (i >= 0) {
            this.m_SelectItemList.remove(i);
        } else {
            this.m_SelectItemList.add(Integer.valueOf(index));
        }
    }

    public void ClearSelectIndex() {
        this.m_SelectItemList = new ArrayList();
        this.m_SelectItemIndex = -1;
    }

    public List<Integer> GetSelectList() {
        return this.m_SelectItemList;
    }

    public void setIsNormalText(boolean value) {
        this.m_IsNormalText = value;
    }

    public LayerListImgBtnListAdapter(Context context2, int layerListType2, List<HashMap<String, Object>> data, int paramInt) {
        this.context = context2;
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(context2);
        }
        this.layerListType = layerListType2;
        this.m_DataList = data;
        this.m_LayoutId = paramInt;
    }

    public void setIsTransparentAnim(boolean isAnim) {
        this.m_IsTransparentAnim = isAnim;
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

    public void SetNeedCheckBoxCheckReturn(boolean value) {
        this._NeedCheckBoxCheckReturn = value;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean UpdateCheckBoxDataListValue(CheckBox paramCheckBox, String paramString) {
        HashMap tmpHashMap = (HashMap) getItem(Integer.parseInt(paramString));
        boolean tempBool01 = paramCheckBox.isChecked();
        if (tempBool01 != Boolean.parseBoolean(tmpHashMap.get("D1").toString())) {
            tmpHashMap.put("D1", Boolean.valueOf(tempBool01));
        }
        if (!this._NeedCheckBoxCheckReturn || this.m_Callback == null) {
            return true;
        }
        this.m_Callback.OnClick("图层列表,是否选择," + paramString + "," + tmpHashMap.get("LayerID").toString() + "," + tempBool01, null);
        return true;
    }

    @SuppressLint({"NewApi"})
    private void updateListViewItem(View paramView, final int paramInt) {
        Bitmap tempBmp;
        Bitmap tempBmp2;
        HashMap tmpHashMap = (HashMap) getItem(paramInt);
        String tempLyrID = tmpHashMap.get("LayerID").toString();
        Object tempTag = tmpHashMap.get("TAG");
        if (tempTag == null) {
            tempTag = "";
        }
        CheckBox localCheckBox = (CheckBox) paramView.findViewById(R.id.imgtxt_itemcheckbox);
        localCheckBox.setTag(Integer.valueOf(paramInt));
        localCheckBox.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.LayerListImgBtnListAdapter.3
            @Override // android.view.View.OnClickListener
            public void onClick(View paramView2) {
                CheckBox localCheckBox2 = (CheckBox) paramView2;
                LayerListImgBtnListAdapter.this.UpdateCheckBoxDataListValue(localCheckBox2, localCheckBox2.getTag().toString());
            }
        });
        localCheckBox.setChecked(Boolean.parseBoolean(tmpHashMap.get("D1").toString()));
        TextView tempTxtView = (TextView) paramView.findViewById(R.id.imgtxt_itemtext1);
        if (this.m_IsNormalText) {
            tempTxtView.setText(tmpHashMap.get("D2").toString());
        } else {
            tempTxtView.setText((SpannableString) tmpHashMap.get("D2"));
        }
        tempTxtView.setTag(String.valueOf(paramInt) + ",图层," + tempLyrID + "," + tempTag);
        tempTxtView.setOnClickListener(this.ImgBtnClickListener);
        if (this.layerListType == 0) {
            paramView.findViewById(R.id.progressBar_transp).setVisibility(8);
            ImageView tempImgView01 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage1);
            tempImgView01.setTag(String.valueOf(paramInt) + ",符号," + tempLyrID + "," + tempTag);
            if (!(tmpHashMap.get("D4") == null || (tempBmp2 = (Bitmap) tmpHashMap.get("D4")) == null)) {
                tempImgView01.setImageBitmap(tempBmp2);
            }
            tempImgView01.setOnClickListener(this.ImgBtnClickListener);
            ImageView tempImgView02 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage2);
            tempImgView02.setTag(String.valueOf(paramInt) + ",渲染," + tempLyrID + "," + tempTag);
            tempImgView02.setOnClickListener(this.ImgBtnClickListener);
            ImageView tempImgView03 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage3);
            tempImgView03.setTag(String.valueOf(paramInt) + ",属性," + tempLyrID + "," + tempTag);
            tempImgView03.setOnClickListener(this.ImgBtnClickListener);
            ImageView tempImgView04 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage4);
            tempImgView04.setTag(String.valueOf(paramInt) + ",向上," + tempLyrID + "," + tempTag);
            tempImgView04.setOnClickListener(this.ImgBtnClickListener);
            ImageView tempImgView05 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage5);
            tempImgView05.setTag(String.valueOf(paramInt) + ",向下," + tempLyrID + "," + tempTag);
            tempImgView05.setOnClickListener(this.ImgBtnClickListener);
            ImageView tempImgView06 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage6);
            tempImgView06.setTag(String.valueOf(paramInt) + ",删除," + tempLyrID + "," + tempTag);
            tempImgView06.setOnClickListener(this.ImgBtnClickListener);
            ImageView tempImgView07 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage7);
            tempImgView07.setTag(String.valueOf(paramInt) + ",重建索引," + tempLyrID + "," + tempTag);
            tempImgView07.setOnClickListener(this.ImgBtnClickListener);
        } else if (this.layerListType == 1) {
            paramView.findViewById(R.id.imgtxt_itemimage1).setVisibility(8);
            paramView.findViewById(R.id.imgtxt_itemimage2).setVisibility(8);
            paramView.findViewById(R.id.imgtxt_itemimage3).setVisibility(8);
            paramView.findViewById(R.id.imgtxt_itemimage7).setVisibility(8);
            NumberCircleProgressBar tempProgressBar = (NumberCircleProgressBar) paramView.findViewById(R.id.progressBar_transp);
            tempProgressBar.setProgress(Integer.parseInt(tmpHashMap.get("D3").toString()));
            tempProgressBar.setTag(paramView);
            tempProgressBar.setClickable(true);
            tempProgressBar.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.LayerListImgBtnListAdapter.4
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int tempInt = ((NumberCircleProgressBar) view).getProgress();
                    SeekbarDialog.Builder builder = new SeekbarDialog.Builder(LayerListImgBtnListAdapter.this.context);
                    builder.setMessage("请输入图层的透明度:");
                    builder.setTitle("输入参数");
                    builder.SetDefaultValue(tempInt);
                    builder.SetSeekbarMaxValue(255);
                    builder.SetIndex(paramInt);
                    builder.SetTag(view.getTag());
                    builder.SetCallback(LayerListImgBtnListAdapter.this.pCallback);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.LayerListImgBtnListAdapter.4.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface paramDialogInterface, int paramInt2) {
                            paramDialogInterface.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });
            ImageView tempImgView042 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage4);
            tempImgView042.setTag(String.valueOf(paramInt) + ",向上," + tempTag);
            tempImgView042.setOnClickListener(this.ImgBtnClickListener);
            ImageView tempImgView052 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage5);
            tempImgView052.setTag(String.valueOf(paramInt) + ",向下," + tempTag);
            tempImgView052.setOnClickListener(this.ImgBtnClickListener);
            ImageView tempImgView062 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage6);
            tempImgView062.setTag(String.valueOf(paramInt) + ",删除," + tempTag);
            tempImgView062.setOnClickListener(this.ImgBtnClickListener);
        } else if (this.layerListType == 2) {
            ImageView tempImgView012 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage2);
            tempImgView012.setTag(String.valueOf(paramInt) + ",渲染," + tempLyrID + "," + tempTag);
            tempImgView012.setOnClickListener(this.ImgBtnClickListener);
            ImageView tempImgView001 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage1);
            tempImgView001.setTag(String.valueOf(paramInt) + ",符号," + tempLyrID + "," + tempTag);
            if (!(tmpHashMap.get("D20") == null || (tempBmp = (Bitmap) tmpHashMap.get("D20")) == null)) {
                tempImgView001.setImageBitmap(tempBmp);
            }
            NumberCircleProgressBar tempProgressBar2 = (NumberCircleProgressBar) paramView.findViewById(R.id.progressBar_transp);
            tempProgressBar2.setProgress(Integer.parseInt(tmpHashMap.get("D3").toString()));
            tempProgressBar2.setTag(paramView);
            tempProgressBar2.setClickable(true);
            tempProgressBar2.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.LayerListImgBtnListAdapter.5
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int tempInt = ((NumberCircleProgressBar) view).getProgress();
                    SeekbarDialog.Builder builder = new SeekbarDialog.Builder(LayerListImgBtnListAdapter.this.context);
                    builder.setMessage("请输入图层的透明度:");
                    builder.setTitle("输入参数");
                    builder.SetDefaultValue(tempInt);
                    builder.SetSeekbarMaxValue(255);
                    builder.SetIndex(paramInt);
                    builder.SetTag(view.getTag());
                    builder.SetCallback(LayerListImgBtnListAdapter.this.pCallback);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.LayerListImgBtnListAdapter.5.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface paramDialogInterface, int paramInt2) {
                            paramDialogInterface.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });
            ImageView tempImgView032 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage3);
            tempImgView032.setTag(String.valueOf(paramInt) + ",属性," + tempLyrID + "," + tempTag);
            tempImgView032.setOnClickListener(this.ImgBtnClickListener);
            ImageView tempImgView043 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage4);
            tempImgView043.setTag(String.valueOf(paramInt) + ",向上," + tempTag);
            tempImgView043.setOnClickListener(this.ImgBtnClickListener);
            ImageView tempImgView053 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage5);
            tempImgView053.setTag(String.valueOf(paramInt) + ",向下," + tempTag);
            tempImgView053.setOnClickListener(this.ImgBtnClickListener);
            ImageView tempImgView063 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage6);
            tempImgView063.setTag(String.valueOf(paramInt) + ",删除," + tempTag);
            tempImgView063.setOnClickListener(this.ImgBtnClickListener);
            ImageView tempImgView072 = (ImageView) paramView.findViewById(R.id.imgtxt_itemimage7);
            tempImgView072.setTag(String.valueOf(paramInt) + ",重建索引," + tempLyrID + "," + tempTag);
            tempImgView072.setOnClickListener(this.ImgBtnClickListener);
        }
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            try {
                convertView = this.mInflater.inflate(this.m_LayoutId, parent, false);
            } catch (Exception ex) {
                Common.Log("getView", "错误:" + ex.toString() + "-->" + ex.getMessage());
            }
        }
        HashMap tmpHashMap = (HashMap) getItem(position);
        if (tmpHashMap.containsKey("Bg_Color")) {
            convertView.setBackgroundColor(Integer.parseInt(String.valueOf(tmpHashMap.get("Bg_Color"))));
        } else {
            if (position == this.m_SelectItemIndex) {
                convertView.setSelected(true);
                convertView.setPressed(true);
                convertView.setBackgroundColor(-10185235);
            } else {
                convertView.setBackgroundColor(0);
            }
            if (this.m_SelectItemList.size() <= 0 || !this.m_SelectItemList.contains(Integer.valueOf(position))) {
                convertView.setBackgroundColor(0);
            } else {
                convertView.setSelected(true);
                convertView.setPressed(true);
                convertView.setBackgroundColor(-10185235);
            }
        }
        updateListViewItem(convertView, position);
        return convertView;
    }
}
