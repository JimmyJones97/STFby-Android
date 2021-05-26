package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import com.stczh.gzforestSystem.R;

public class Toolbar_ZoomScale extends BaseToolbar {
    static final int BUTTON_DEFAULT = 2130837641;
    static final int BUTTON_SELECTED = 2130837640;
    ImageView imgBtnToolGPSLock;
    ImageView imgBtnToolGlobal;
    ImageView imgBtnToolMore;
    ImageView imgBtnToolPan;
    ImageView imgBtnToolZoomIn;
    ImageView imgBtnToolZoomOut;
    ImageView imgBtnToolZoomToLayer;
    boolean isShowToolMore = false;
    private View.OnClickListener myViewClick = new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_ZoomScale.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                Toolbar_ZoomScale.this.DoCommand(view.getTag().toString());
            }
        }
    };

    public Toolbar_ZoomScale(Context context, MapView mapView) {
        super(context, mapView);
        this.m_ToolbarName = "地图缩放工具栏";
        this.m_AllowdChangeOri = false;
    }

    public void LoadToolbar(View paramView) {
        this.m_view = paramView;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void DoCommand(String command) {
        if (command.equals("解锁GPS")) {
            this.imgBtnToolGPSLock.setImageResource(R.drawable.m_zoom_gps_unlock);
            this.imgBtnToolGPSLock.setTag("锁定GPS");
            PubVar.AutoPan = false;
        } else if (command.equals("锁定GPS")) {
            this.imgBtnToolGPSLock.setImageResource(R.drawable.m_zoom_gps_lock);
            this.imgBtnToolGPSLock.setTag("解锁GPS");
            PubVar.AutoPan = true;
        } else {
            PubVar._PubCommand.ProcessCommand(command);
        }
    }
}
