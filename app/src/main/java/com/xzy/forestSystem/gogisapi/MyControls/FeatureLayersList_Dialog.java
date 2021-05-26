package  com.xzy.forestSystem.gogisapi.MyControls;

import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoGroupLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Display.IRender;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Dialog;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.XProject.Layer_New_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.Layer_Render_Dialog;
import com.stczh.gzforestSystem.R;

public class FeatureLayersList_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private String m_CurrentEditLyrID;
    private LinearLayout m_MainLinearLayout;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public FeatureLayersList_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_MainLinearLayout = null;
        this.m_CurrentEditLyrID = "";
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.FeatureLayersList_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.featurelayerlist_dialog);
        this._Dialog.Resize(0.9f, 0.96f);
        this._Dialog.SetCaption("选择编辑的图层:");
        this._Dialog.HideHeadBar();
        this._Dialog.SetHeightWrapContent();
        this._Dialog.setCanceledOnTouchOutside(true);
        this.m_MainLinearLayout = (LinearLayout) this._Dialog.findViewById(R.id.ll_layerList);
        this._Dialog.getMainView().setBackgroundResource(R.drawable.dialog_corner_bg);
    }

    public void setCurrentEditLyrID(String layerID) {
        this.m_CurrentEditLyrID = layerID;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void initialLayout() {
        GeoGroupLayer pGeoLayers = PubVar._Map.getGeoLayers();
        LinearLayout.LayoutParams layoutParamsRoot = new LinearLayout.LayoutParams(-1, -1);
        layoutParamsRoot.setMargins(10, 5, 0, 5);
        LinearLayout.LayoutParams tmpLineLP = new LinearLayout.LayoutParams(-1, -2);
        tmpLineLP.height = (int) (1.0f * PubVar.ScaledDensity);
        tmpLineLP.setMargins(0, 2, 0, 2);
        LinearLayout.LayoutParams tmpImgViewLayoutParams = new LinearLayout.LayoutParams(-2, -2);
        int tmpMargInt = (int) (PubVar.ScaledDensity * 5.0f);
        tmpImgViewLayoutParams.setMargins(tmpMargInt, tmpMargInt, tmpMargInt, tmpMargInt);
        int tmpTid = 0;
        int tmpCount = pGeoLayers.getList().size();
        for (GeoLayer tmpGeoLayer : pGeoLayers.getList()) {
            tmpTid++;
            if (tmpGeoLayer != null) {
                LinearLayout layoutRoot = new LinearLayout(this._Dialog.getContext());
                layoutRoot.setLayoutParams(layoutParamsRoot);
                layoutRoot.setOrientation(0);
                ImageView tmpImageView = new ImageView(this._Dialog.getContext());
                if (tmpGeoLayer.getVisible()) {
                    tmpImageView.setImageDrawable(this._Dialog.getContext().getResources().getDrawable(R.drawable.visible36));
                } else {
                    tmpImageView.setImageDrawable(this._Dialog.getContext().getResources().getDrawable(R.drawable.invisible36));
                }
                tmpImageView.setLayoutParams(tmpImgViewLayoutParams);
                tmpImageView.setBackgroundResource(R.drawable.mb_button_selector);
                tmpImageView.setClickable(true);
                tmpImageView.setTag(tmpGeoLayer.getLayerID());
                tmpImageView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.FeatureLayersList_Dialog.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        String tmpLyrID;
                        GeoLayer tmpGeoLayer2;
                        if (v.getTag() != null && (tmpGeoLayer2 = PubVar._Map.GetGeoLayerByID((tmpLyrID = v.getTag().toString()))) != null) {
                            boolean tmpVisible = !tmpGeoLayer2.getVisible();
                            tmpGeoLayer2.setVisible(tmpVisible);
                            FeatureLayer tmpFeatLyr = PubVar._PubCommand.m_ProjectDB.getFeatureLayerByID(tmpLyrID);
                            if (tmpFeatLyr != null) {
                                tmpFeatLyr.SetVisible(tmpVisible);
                                tmpFeatLyr.SaveLayerInfo();
                            }
                            PubVar._Map.RefreshFast();
                            if (v instanceof ImageView) {
                                ImageView tmpImageView2 = (ImageView) v;
                                if (tmpGeoLayer2.getVisible()) {
                                    tmpImageView2.setImageDrawable(FeatureLayersList_Dialog.this._Dialog.getContext().getResources().getDrawable(R.drawable.visible36));
                                } else {
                                    tmpImageView2.setImageDrawable(FeatureLayersList_Dialog.this._Dialog.getContext().getResources().getDrawable(R.drawable.invisible36));
                                }
                            }
                        }
                    }
                });
                layoutRoot.addView(tmpImageView);
                TextView tmpTextView = new TextView(this._Dialog.getContext());
                tmpTextView.setText(tmpGeoLayer.getLayerName());
                tmpTextView.setTextSize(20.0f);
                tmpTextView.setGravity(16);
                tmpTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                tmpTextView.setClickable(true);
                tmpTextView.setTag("图层:" + tmpGeoLayer.getLayerID());
                tmpTextView.setBackgroundResource(R.drawable.toolbar_btn_selector);
                tmpTextView.setLayoutParams(layoutParamsRoot);
                if (this.m_CurrentEditLyrID.equals(tmpGeoLayer.getLayerID())) {
                    tmpTextView.setTextColor(SupportMenu.CATEGORY_MASK);
                }
                tmpTextView.setOnClickListener(new ViewClick());
                tmpTextView.setOnLongClickListener(new ViewLongClick());
                layoutRoot.addView(tmpTextView);
                this.m_MainLinearLayout.addView(layoutRoot);
                if (tmpCount != tmpTid) {
                    View tmpView = new View(this._Dialog.getContext());
                    tmpView.setBackgroundColor(-2139062144);
                    tmpView.setLayoutParams(tmpLineLP);
                    this.m_MainLinearLayout.addView(tmpView);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        String[] tmpStrs;
        final String tmpLyrID;
        if (command.contains("图层") && (tmpStrs = command.split(":")) != null && tmpStrs.length > 1 && (tmpLyrID = tmpStrs[1]) != null && tmpLyrID.length() > 0) {
            if (tmpLyrID.equals(this.m_CurrentEditLyrID)) {
                Common.ShowToast("当前图层已处于编辑状态下.");
                return;
            }
            GeoLayer tmpGeoLayer = PubVar._Map.GetGeoLayerByID(tmpLyrID);
            if (tmpGeoLayer == null) {
                startEditLayer(tmpLyrID);
            } else if (!tmpGeoLayer.getVisible()) {
                Common.ShowYesNoDialog(this._Dialog.getContext(), "图层【" + tmpGeoLayer.getLayerName() + "】不可见.\r\n是否继续编辑该图层?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.FeatureLayersList_Dialog.3
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command2, Object pObject) {
                        if (command2.equals("YES")) {
                            FeatureLayersList_Dialog.this.startEditLayer(tmpLyrID);
                        }
                    }
                });
            } else {
                startEditLayer(tmpLyrID);
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand2(final String command, Object obj) {
        final GeoLayer tmpGeoLayer;
        FeatureLayer tmpFeatureLayer;
        GeoLayer tmpGeoLayer2;
        GeoLayer tmpGeoLayer3;
        FeatureLayer tmpFeatureLayer2;
        GeoLayer tmpGeoLayer4;
        Envelope tmpEnvelope;
        String tmpLyrID;
        final GeoLayer tmpGeoLayer5;
        if (command.contains("图层") && obj == null) {
            String[] tmpStrs = command.split(":");
            if (tmpStrs != null && tmpStrs.length > 1 && (tmpLyrID = tmpStrs[1]) != null && tmpLyrID.length() > 0 && (tmpGeoLayer5 = PubVar._Map.GetGeoLayerByID(tmpLyrID)) != null) {
                final String[] tmpArray2 = tmpLyrID.equals(this.m_CurrentEditLyrID) ? new String[]{"缩放至图层", "属性数据", "图层渲染", "字段定义"} : new String[]{"编辑图层", "缩放至图层", "属性数据", "图层渲染", "字段定义"};
                new AlertDialog.Builder(this._Dialog.getContext(), 3).setTitle("请选择操作:").setItems(tmpArray2, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.FeatureLayersList_Dialog.4
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (tmpArray2[arg1].equals("编辑图层")) {
                            FeatureLayersList_Dialog.this.DoCommand(command);
                        } else {
                            FeatureLayersList_Dialog.this.DoCommand2(tmpArray2[arg1], tmpGeoLayer5);
                        }
                    }
                }).show();
            }
        } else if (command.equals("缩放至图层")) {
            if (obj != null && (obj instanceof GeoLayer) && (tmpGeoLayer4 = (GeoLayer) obj) != null && (tmpEnvelope = tmpGeoLayer4.getExtend()) != null && !tmpEnvelope.IsEmpty()) {
                PubVar._Map.ZoomToExtend(tmpEnvelope);
                Common.ShowToast("已缩放至图层【" + tmpGeoLayer4.getLayerName() + "】的全图范围内.");
                this._Dialog.dismiss();
            }
        } else if (command.equals("属性数据")) {
            if (obj != null && (obj instanceof GeoLayer) && (tmpGeoLayer3 = (GeoLayer) obj) != null && (tmpFeatureLayer2 = PubVar._PubCommand.m_ProjectDB.getFeatureLayerByID(tmpGeoLayer3.getLayerID())) != null) {
                DataQuery_Dialog tempDialog = new DataQuery_Dialog();
                tempDialog.SetQueryLayer(tmpFeatureLayer2);
                tempDialog.ShowDialog();
            }
        } else if (command.equals("图层渲染")) {
            if (obj != null && (obj instanceof GeoLayer) && (tmpGeoLayer2 = (GeoLayer) obj) != null) {
                IRender tmpRender = tmpGeoLayer2.getRender();
                Layer_Render_Dialog tempDialog2 = new Layer_Render_Dialog();
                tempDialog2.SetGeoLayer(tmpGeoLayer2);
                tempDialog2.SetRender(tmpRender);
                tempDialog2.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.FeatureLayersList_Dialog.5
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command2, Object pObject) {
                        Object[] tempObjs;
                        String tmpLayerID;
                        GeoLayer tempGeoLayer;
                        if (command2.contains("渲染") && (tempObjs = (Object[]) pObject) != null && tempObjs.length > 1 && (tempGeoLayer = PubVar._Map.GetGeoLayerByName((tmpLayerID = tempObjs[0].toString()))) != null) {
                            IRender tempRender = (IRender) tempObjs[1];
                            if (tempRender != null) {
                                tempGeoLayer.setRender(tempRender);
                                tempGeoLayer.getRender().SaveRender();
                            }
                            if (tempGeoLayer.getRender().getIfLabel() && tempGeoLayer.getRender().needUpdateLableContent) {
                                tempGeoLayer.getDataset().UpdateLabelContent();
                                tempGeoLayer.getDataset().SaveGeoLabelContent();
                            }
                            if (tempGeoLayer.getRender().needUpdateSymbol) {
                                tempGeoLayer.getDataset().UpdateAllGeometrysSymbol();
                            }
                            FeatureLayer tmpLayer = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(tmpLayerID);
                            if (tmpLayer != null) {
                                if (tempRender != null) {
                                    tempRender.UpdateToLayer(tmpLayer);
                                }
                                tmpLayer.SaveLayerInfo();
                                tempGeoLayer.UpdateFromLayer(tmpLayer);
                            }
                            PubVar._Map.Refresh();
                        }
                    }
                });
                tempDialog2.ShowDialog();
            }
        } else if (command.equals("字段定义") && obj != null && (obj instanceof GeoLayer) && (tmpGeoLayer = (GeoLayer) obj) != null && (tmpFeatureLayer = PubVar._PubCommand.m_ProjectDB.getFeatureLayerByID(tmpGeoLayer.getLayerID())) != null) {
            Layer_New_Dialog tempLayerDialog = new Layer_New_Dialog();
            tempLayerDialog.SetHaveLayerList(null);
            tempLayerDialog.setAllowEditLayerName(false);
            tempLayerDialog.SetEditLayer(tmpFeatureLayer);
            tempLayerDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.FeatureLayersList_Dialog.6
                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                public void OnClick(String command2, Object pObject) {
                    GeoLayer tempGeoLayer;
                    FeatureLayer tmpLayer = (FeatureLayer) pObject;
                    if (!tmpLayer.GetLayerName().equals(tmpGeoLayer.getLayerName()) && (tempGeoLayer = PubVar._Map.GetGeoLayerByName(tmpLayer.GetLayerID())) != null) {
                        tempGeoLayer.SetLayerName(tmpLayer.GetLayerName());
                    }
                    tmpGeoLayer.UpdateFromLayer(tmpLayer);
                    FeatureLayer tmpLayer2 = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(tmpLayer.GetLayerID());
                    if (tmpLayer2 != null) {
                        tmpLayer.CopyTo(tmpLayer2);
                        tmpLayer2.SaveLayerInfo();
                    }
                }
            });
            tempLayerDialog.ShowDialog();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void startEditLayer(String layerID) {
        if (this.m_Callback != null) {
            this.m_Callback.OnClick("开始编辑图层", layerID);
            this._Dialog.dismiss();
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.FeatureLayersList_Dialog.7
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                FeatureLayersList_Dialog.this.initialLayout();
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: package-private */
    public class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                FeatureLayersList_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public class ViewLongClick implements View.OnLongClickListener {
        ViewLongClick() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            if (view.getTag() == null) {
                return false;
            }
            FeatureLayersList_Dialog.this.DoCommand2(view.getTag().toString(), null);
            return false;
        }
    }
}
