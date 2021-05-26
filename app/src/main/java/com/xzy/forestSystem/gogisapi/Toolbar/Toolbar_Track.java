package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Track.TrackLine;
import  com.xzy.forestSystem.gogisapi.Track.TrackPoint;
import  com.xzy.forestSystem.gogisapi.Track.TrackQuery_Dailog;
import com.stczh.gzforestSystem.R;
import java.util.List;

public class Toolbar_Track extends BaseToolbar {
    private Drawable _GPSRecordBg01 = null;
    private Drawable _GPSRecordBg02 = null;
    private Button m_GPSRecordBtn = null;
    private CheckBox m_TrackVisible = null;
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Track.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String command, Object object) {
            try {
                if (command.equals("足迹查询结果返回")) {
                    List<TrackPoint> tempList = (List) object;
                    if (tempList == null || tempList.size() <= 0) {
                        Common.ShowToast("没有获取到任何足迹数据.");
                        return;
                    }
                    TrackLine tmpTrack = PubVar._PubCommand.GetTrackLine();
                    if (tmpTrack != null) {
                        tmpTrack.SetIsQueryTrack(true);
                        tmpTrack.Clear();
                        tmpTrack.AddTrackPoints(tempList);
                        if (!PubVar._PubCommand.GetTrackLine().getEnabelDraw()) {
                            Toolbar_Track.this.m_TrackVisible.setChecked(true);
                        }
                        tmpTrack.gotoCoordByIndex(tempList.size() - 1);
                        Common.ShowToast("查询足迹结果显示在地图中.");
                    }
                }
            } catch (Exception e) {
            }
        }
    };

    public Toolbar_Track(Context context, MapView mapView) {
        super(context, mapView);
        this.m_ToolbarName = "足迹工具栏";
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void LoadToolBar(View view, int mainLinearLayoutID) {
        super.LoadToolBar(view, mainLinearLayoutID);
        this.buttonIDs.put("开始", Integer.valueOf((int) R.id.bt_TrackStart));
        this.buttonIDs.put("上一点", Integer.valueOf((int) R.id.bt_TrackLast));
        this.buttonIDs.put("下一点", Integer.valueOf((int) R.id.bt_TrackNext));
        this.buttonIDs.put("查询", Integer.valueOf((int) R.id.bt_TrackQuery));
        this.buttonIDs.put("清空", Integer.valueOf((int) R.id.bt_Track_Clear));
        this.m_view.setOnTouchListener(this.touchListener);
        this.m_baseLayout.setOnTouchListener(this.touchListener);
        this.m_TrackVisible = (CheckBox) this.m_view.findViewById(R.id.ck_TrackVisible);
        this.m_TrackVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Track.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                Toolbar_Track.this.DoCommand("足迹可见性");
            }
        });
        this.m_view.findViewById(R.id.tv_trackLine).setOnTouchListener(this.touchListener);
        this.m_GPSRecordBtn = (Button) this.m_view.findViewById(R.id.bt_TrackStart);
        this.m_GPSRecordBtn.setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_TrackLast).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_TrackNext).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_TrackQuery).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_Track_Clear).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_Track_Exit).setOnClickListener(this.buttonClickListener);
        this._GPSRecordBg01 = PubVar._PubCommand.m_Context.getResources().getDrawable(R.drawable.play48);
        this._GPSRecordBg01.setBounds(0, 0, this._GPSRecordBg01.getMinimumWidth(), this._GPSRecordBg01.getMinimumHeight());
        this._GPSRecordBg02 = PubVar._PubCommand.m_Context.getResources().getDrawable(R.drawable.stop6048);
        this._GPSRecordBg02.setBounds(0, 0, this._GPSRecordBg02.getMinimumWidth(), this._GPSRecordBg02.getMinimumHeight());
        ((TextView) this.m_view.findViewById(R.id.tv_trackLine)).setOnLongClickListener(new View.OnLongClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Track.3
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View arg0) {
                Common.ShowYesNoDialog(Toolbar_Track.this.m_context, "是否关闭该工具栏?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Track.3.1
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        if (paramString.equals("YES")) {
                            Toolbar_Track.this.Hide();
                        }
                    }
                });
                return false;
            }
        });
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Show() {
        super.Show();
        initialLayout();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Hide() {
        super.Hide();
    }

    private void initialLayout() {
        TrackLine tmpTrack = PubVar._PubCommand.GetTrackLine();
        if (tmpTrack != null) {
            this.m_TrackVisible.setChecked(true);
            if (tmpTrack.getIsRecord()) {
                this.m_GPSRecordBtn.setText("停止");
                this.m_GPSRecordBtn.setTag("停止记录足迹");
                this.m_GPSRecordBtn.setCompoundDrawables(null, this._GPSRecordBg02, null, null);
            }
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void DoCommand(String command) {
        try {
            TrackLine tmpTrackLine = PubVar._PubCommand.GetTrackLine();
            if (command.equals("足迹可见性")) {
                tmpTrackLine.SetEnabelDraw(this.m_TrackVisible.isChecked());
            } else if (command.equals("清空足迹")) {
                tmpTrackLine.Clear();
                tmpTrackLine.SetIsQueryTrack(false);
                PubVar._MapView._GraphicLayer.RemoveGeometrysByType("TrackLine");
                Common.ShowToast("清空地图中足迹显示.");
            } else if (command.equals("开始记录足迹")) {
                tmpTrackLine.SetIsRecordByGPS(true);
                this.m_GPSRecordBtn.setText("停止");
                this.m_GPSRecordBtn.setTag("停止记录足迹");
                this.m_GPSRecordBtn.setCompoundDrawables(null, this._GPSRecordBg02, null, null);
            } else if (command.equals("停止记录足迹")) {
                tmpTrackLine.SetIsRecordByGPS(false);
                this.m_GPSRecordBtn.setText("开始");
                this.m_GPSRecordBtn.setTag("开始记录足迹");
                this.m_GPSRecordBtn.setCompoundDrawables(null, this._GPSRecordBg01, null, null);
            } else if (command.equals("上一个足迹点")) {
                if (!tmpTrackLine.getIsRecord() || tmpTrackLine.getIsQueryTrack()) {
                    if (!tmpTrackLine.getEnabelDraw()) {
                        this.m_TrackVisible.setChecked(true);
                    }
                    tmpTrackLine.gotoLastCoord();
                    return;
                }
                Common.ShowDialog("正在记录足迹,不能返还上一个足迹点.");
            } else if (command.equals("下一个足迹点")) {
                if (!tmpTrackLine.getIsRecord() || tmpTrackLine.getIsQueryTrack()) {
                    if (!tmpTrackLine.getEnabelDraw()) {
                        this.m_TrackVisible.setChecked(true);
                    }
                    tmpTrackLine.gotoNextCoord();
                    return;
                }
                Common.ShowDialog("正在记录足迹,不能跳至下一个足迹点.");
            } else if (command.equals("查询足迹")) {
                TrackQuery_Dailog tempDialog = new TrackQuery_Dailog();
                tempDialog.SetCallback(this.pCallback);
                tempDialog.ShowDialog();
            }
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void DoCommand(View view) {
        Object tempObj = view.getTag();
        if (tempObj != null) {
            String command = tempObj.toString();
            if (command.equals("关闭工具")) {
                Hide();
                SaveConfigDB();
                return;
            }
            DoCommand(command);
        }
    }
}
