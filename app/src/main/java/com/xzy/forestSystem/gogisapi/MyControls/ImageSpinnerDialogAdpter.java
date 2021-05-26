package  com.xzy.forestSystem.gogisapi.MyControls;

import android.content.Context;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import  com.xzy.forestSystem.gogisapi.Display.SymbolObject;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;

public class ImageSpinnerDialogAdpter extends BaseAdapter {
    private Context m_Context = null;
    private List<SymbolObject> m_DataList = new ArrayList();
    private ImageView m_ImageView = null;
    private boolean m_ShowText = false;
    private TextView m_TextView = null;
    private View m_View = null;

    public ImageSpinnerDialogAdpter(Context paramContext) {
        this.m_Context = paramContext;
    }

    public void SetIsShowText(boolean ShowText) {
        this.m_ShowText = ShowText;
        this.m_View = null;
        GetView();
    }

    public View GetView() {
        if (this.m_View == null) {
            LinearLayout localLinearLayout = new LinearLayout(this.m_Context);
            localLinearLayout.setOrientation(0);
            localLinearLayout.setGravity(17);
            this.m_ImageView = new ImageView(this.m_Context);
            this.m_ImageView.setBackgroundDrawable(this.m_Context.getResources().getDrawable(R.drawable.imageview_flat));
            localLinearLayout.addView(this.m_ImageView);
            if (this.m_ShowText) {
                this.m_ImageView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2, 1.0f));
                this.m_TextView = new TextView(this.m_Context);
                this.m_TextView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2, 1.0f));
                this.m_TextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.m_TextView.setGravity(17);
                localLinearLayout.addView(this.m_TextView);
            } else {
                this.m_ImageView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1, 1.0f));
            }
            this.m_View = localLinearLayout;
        }
        return this.m_View;
    }

    public void SetDataList(List<SymbolObject> paramList) {
        this.m_DataList = paramList;
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

    @Override // android.widget.Adapter
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        GetView();
        if (this.m_DataList.get(paramInt) == null) {
            this.m_ImageView.setImageBitmap(null);
            if (this.m_ShowText && this.m_TextView != null) {
                this.m_TextView.setText("");
            }
        } else {
            SymbolObject localSymbolObject = this.m_DataList.get(paramInt);
            this.m_ImageView.setImageBitmap(localSymbolObject.SymbolFigure);
            if (this.m_ShowText && this.m_TextView != null) {
                this.m_TextView.setText(localSymbolObject.SymbolName);
            }
        }
        return GetView();
    }
}
