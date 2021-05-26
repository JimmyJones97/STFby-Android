package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.TipPanel;
import com.stczh.gzforestSystem.R;

public class Toolbar_Measure extends BaseToolbar {
    private TipPanel _TipPanel = null;

    public Toolbar_Measure(Context context, MapView mapView) {
        super(context, mapView);
        this.m_ToolbarName = "测量工具栏";
        this._TipPanel = new TipPanel(context);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void LoadToolBar(View view, int mainLinearLayoutID) {
        super.LoadToolBar(view, mainLinearLayoutID);
        this.buttonIDs.put("测量长度与面积", Integer.valueOf((int) R.id.buttonMeasureLenArea));
        this.buttonIDs.put("测量点", Integer.valueOf((int) R.id.buttonMeasureCoord));
        this.buttonIDs.put("测量自由绘制", Integer.valueOf((int) R.id.buttonMeasureFree));
        this.buttonIDs.put("添加GPS点", Integer.valueOf((int) R.id.buttonMeasureAddGPS));
        this.buttonIDs.put("测量取消上一点", Integer.valueOf((int) R.id.buttonMeasureUndo));
        this.buttonIDs.put("测量重做", Integer.valueOf((int) R.id.buttonMeasureRedo));
        this.buttonIDs.put("清空", Integer.valueOf((int) R.id.buttonMeasureClear));
        this.buttonIDs.put("面积", Integer.valueOf((int) R.id.buttonMeasureArea));
        this.m_view.setOnTouchListener(this.touchListener);
        this.m_baseLayout.setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.buttonMeasureCoord).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonMeasureLenArea).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonMeasureFree).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonMeasureAddGPS).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonMeasureUndo).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonMeasureRedo).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonMeasureClear).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonExit).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonMeasureArea).setOnClickListener(this.buttonClickListener);
        ((Button) this.m_view.findViewById(R.id.buttonMeasureLenArea)).setOnLongClickListener(new View.OnLongClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Measure.1
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View arg0) {
                Common.ShowYesNoDialog(Toolbar_Measure.this.m_context, "是否关闭该工具栏?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Measure.1.1
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        if (paramString.equals("YES")) {
                            Toolbar_Measure.this.Hide();
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
        if (this._TipPanel != null) {
            PubVar._PubCommand.m_Measure.SetBindTipPanel(this._TipPanel);
        }
        SetButtonSelectedStatus("测量长度与面积", true);
        PubVar._PubCommand.ProcessCommand("测量_长度与面积");
        PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("测量工具栏;图层工具栏");
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Hide() {
        if (this._TipPanel != null) {
            this._TipPanel.dismiss();
        }
        super.Hide();
        PubVar._PubCommand.ProcessCommand("测量_清空");
        PubVar._PubCommand.ProcessCommand("自由缩放");
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void DoCommand(View view) {
        Object tempObj = view.getTag();
        if (tempObj != null) {
            String command = tempObj.toString();
            if (command.equals("关闭工具")) {
                Hide();
                SaveConfigDB();
                if (this._TipPanel != null) {
                    this._TipPanel.dismiss();
                }
            } else if (command.equals("测量点")) {
                if (view.isSelected()) {
                    SetButtonSelectedStatus("测量点", false);
                    PubVar._PubCommand.ProcessCommand("测量_点坐标");
                } else {
                    SetButtonSelectedStatus("测量点", true);
                    PubVar._PubCommand.ProcessCommand("测量_点坐标");
                }
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("测量工具栏;图层工具栏");
            } else if (command.equals("测量长度与面积")) {
                if (view.isSelected()) {
                    SetButtonSelectedStatus("测量长度与面积", false);
                    PubVar._PubCommand.ProcessCommand("测量_长度与面积");
                } else {
                    SetButtonSelectedStatus("测量长度与面积", true);
                    PubVar._PubCommand.ProcessCommand("测量_长度与面积");
                }
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("测量工具栏;图层工具栏");
            } else if (command.equals("测量自由绘制")) {
                if (view.isSelected()) {
                    SetButtonSelectedStatus("测量自由绘制", false);
                    PubVar._PubCommand.ProcessCommand("测量_自由绘制");
                } else {
                    SetButtonSelectedStatus("测量自由绘制", true);
                    PubVar._PubCommand.ProcessCommand("测量_自由绘制");
                }
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("测量工具栏;图层工具栏");
            } else if (command.equals("添加GPS点")) {
                if (PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                    PubVar._PubCommand.m_Measure.AddPoint(PubVar._PubCommand.m_GpsLocation.getGPSCoordinate());
                    return;
                }
                Common.ShowToast("未进行GPS定位.");
            } else if (command.equals("取消")) {
                PubVar._PubCommand.ProcessCommand("测量_取消上一点");
            } else if (command.equals("重做")) {
                PubVar._PubCommand.ProcessCommand("测量_重做上一点");
            } else if (command.equals("清空")) {
                PubVar._PubCommand.ProcessCommand("测量_清空");
                if (this._TipPanel != null) {
                    this._TipPanel.hideTip();
                }
            } else if (command.equals("测量面积")) {
                if (view.isSelected()) {
                    SetButtonSelectedStatus("面积", false);
                    PubVar._PubCommand.ProcessCommand("测量_测量面积");
                } else {
                    SetButtonSelectedStatus("面积", true);
                    PubVar._PubCommand.ProcessCommand("测量_测量面积");
                }
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("测量工具栏;图层工具栏");
            }
        }
    }
}
