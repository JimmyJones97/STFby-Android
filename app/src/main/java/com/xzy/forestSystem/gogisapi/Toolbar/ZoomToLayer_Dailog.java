package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.ELayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XRasterFileLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.XProject.Layer_Select_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;

public class ZoomToLayer_Dailog {
    private XDialogTemplate _Dialog;
    private InputSpinner layersSpinner;
    private ICallback m_Callback;
    private List<XLayer> m_LayerList;
    private XLayer m_SelectLayer;
    private TextView m_TextView;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public ZoomToLayer_Dailog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_LayerList = null;
        this.m_SelectLayer = null;
        this.m_TextView = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.ZoomToLayer_Dailog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("选择图层")) {
                        String tmpLayerID = object.toString();
                        for (XLayer tmpLayer : ZoomToLayer_Dailog.this.m_LayerList) {
                            if (tmpLayer.getLayerType() == ELayerType.FEATURE || tmpLayer.getLayerType() == ELayerType.VECTOR) {
                                if (((GeoLayer) tmpLayer).getLayerID().equals(tmpLayerID)) {
                                    ZoomToLayer_Dailog.this.m_SelectLayer = tmpLayer;
                                    ZoomToLayer_Dailog.this.layersSpinner.getEditTextView().setText(ZoomToLayer_Dailog.this.m_SelectLayer.getLayerName());
                                    ZoomToLayer_Dailog.this.updateLayerInfo();
                                    return;
                                }
                            } else if (tmpLayer.getLayerType() == ELayerType.RASTERFILE) {
                                if (((XRasterFileLayer) tmpLayer).getFilePath().equals(tmpLayerID)) {
                                    ZoomToLayer_Dailog.this.m_SelectLayer = tmpLayer;
                                    ZoomToLayer_Dailog.this.layersSpinner.getEditTextView().setText(ZoomToLayer_Dailog.this.m_SelectLayer.getLayerName());
                                    ZoomToLayer_Dailog.this.updateLayerInfo();
                                    return;
                                }
                            } else if (tmpLayer.getLayerType() == ELayerType.ONLINEMAP && tmpLayer.getName().equals(tmpLayerID)) {
                                ZoomToLayer_Dailog.this.m_SelectLayer = tmpLayer;
                                ZoomToLayer_Dailog.this.layersSpinner.getEditTextView().setText(ZoomToLayer_Dailog.this.m_SelectLayer.getName());
                                ZoomToLayer_Dailog.this.updateLayerInfo();
                                return;
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        };
        this.layersSpinner = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.zoomtolayer_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("图层缩放");
        this._Dialog.setCanceledOnTouchOutside(true);
        this._Dialog.HideKeybroad();
        this.layersSpinner = (InputSpinner) this._Dialog.findViewById(R.id.sp_myQueryLayersList2);
        this.m_TextView = (TextView) this._Dialog.findViewById(R.id.tv_zoomToLayerInfo);
        this._Dialog.findViewById(R.id.bt_ZoomToLayerBT).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.bt_ZoomToLayerMove).setOnClickListener(new ViewClick());
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayers() {
        if (this.m_LayerList != null && this.m_LayerList.size() > 0) {
            this.m_SelectLayer = this.m_LayerList.get(0);
            updateLayerInfo();
        }
        this.layersSpinner.setEditTextEnable(false);
        this.layersSpinner.AddTextClick();
        if (this.m_SelectLayer != null) {
            this.layersSpinner.getEditTextView().setText(this.m_SelectLayer.getLayerName());
        }
        this.layersSpinner.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.ZoomToLayer_Dailog.2
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                Layer_Select_Dialog tempDialog = new Layer_Select_Dialog();
                tempDialog.SetLayersList(ZoomToLayer_Dailog.this.m_LayerList);
                tempDialog.SetLayerSelectType(3);
                tempDialog.SetCallback(ZoomToLayer_Dailog.this.pCallback);
                tempDialog.ShowDialog();
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateLayerInfo() {
        if (this.m_SelectLayer != null) {
            StringBuilder tempSB = new StringBuilder();
            Envelope tempExtend = getLayerExtend();
            if (tempExtend != null) {
                tempSB.append("图层范围:\r\nXMin:");
                tempSB.append(tempExtend.getMinX());
                tempSB.append("\r\nYMin:");
                tempSB.append(tempExtend.getMinY());
                tempSB.append("\r\nXMax:");
                tempSB.append(tempExtend.getMaxX());
                tempSB.append("\r\nYMax:");
                tempSB.append(tempExtend.getMaxY());
            }
            this.m_TextView.setText(tempSB.toString());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("缩放至图层")) {
            if (this.m_SelectLayer != null) {
                Envelope tempExtend = getLayerExtend();
                if (tempExtend != null) {
                    PubVar._Map.ZoomToExtend(tempExtend.Scale(2.0d));
                    Common.ShowToast("已经缩放至图层【" + this.m_SelectLayer.getLayerName() + "】全图范围.");
                    this._Dialog.dismiss();
                    return;
                }
                return;
            }
            Common.ShowDialog("没有选择任何图层对象.");
        } else if (!command.equals("平移至图层")) {
        } else {
            if (this.m_SelectLayer != null) {
                Envelope tempExtend2 = getLayerExtend();
                if (tempExtend2 != null) {
                    PubVar._Map.ZoomToCenter(tempExtend2.getCenter());
                    Common.ShowToast("已经平移至图层【" + this.m_SelectLayer.getLayerName() + "】范围中心.");
                    this._Dialog.dismiss();
                    return;
                }
                return;
            }
            Common.ShowDialog("没有选择任何图层对象.");
        }
    }

    private Envelope getLayerExtend() {
        if (this.m_SelectLayer.getLayerType() == ELayerType.FEATURE || this.m_SelectLayer.getLayerType() == ELayerType.VECTOR) {
            DataSet pDataset = ((GeoLayer) this.m_SelectLayer).getDataset();
            if (pDataset != null) {
                return pDataset.GetExtendFromDB();
            }
            return null;
        } else if (this.m_SelectLayer.getLayerType() == ELayerType.RASTERFILE) {
            return ((XRasterFileLayer) this.m_SelectLayer).getExtend();
        } else {
            if (this.m_SelectLayer.getLayerType() == ELayerType.ONLINEMAP) {
                return this.m_SelectLayer.getExtend();
            }
            return null;
        }
    }

    public void ShowDialog() {
        this.m_LayerList = new ArrayList();
        this.m_LayerList.addAll(PubVar._Map.getGeoLayers().getList());
        this.m_LayerList.addAll(PubVar._Map.getVectorGeoLayers().getList());
        this.m_LayerList.addAll(PubVar._Map.getRasterLayers());
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.ZoomToLayer_Dailog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                ZoomToLayer_Dailog.this.refreshLayers();
            }
        });
        this._Dialog.show();
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                ZoomToLayer_Dailog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
