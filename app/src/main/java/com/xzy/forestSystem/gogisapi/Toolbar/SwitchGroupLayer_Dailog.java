package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageButton;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.Iterator;
import java.util.List;

public class SwitchGroupLayer_Dailog {
    private XDialogTemplate _Dialog;
    private ImageButton imageView01;
    private ImageButton imageView02;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public SwitchGroupLayer_Dailog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.imageView01 = null;
        this.imageView02 = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.SwitchGroupLayer_Dailog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.x_switchgrouplayer_dialog);
        this._Dialog.SetCaption("图层快速切换");
        this._Dialog.SetWidthHeightWrapContent();
        this._Dialog.HideHeadBar();
        this._Dialog.setCanceledOnTouchOutside(true);
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.imageView01 = (ImageButton) this._Dialog.findViewById(R.id.img_streetMap);
        this.imageView01.setOnClickListener(new ViewClick());
        this.imageView02 = (ImageButton) this._Dialog.findViewById(R.id.img_sateMap);
        this.imageView02.setOnClickListener(new ViewClick());
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        try {
            PubVar._Map.getBKVectorLayersVisibleList();
            PubVar._Map.getRasterLayersVisibleList();
            boolean tempBool = false;
            if (PubVar._Map.getVectorGeoLayers().size() > 0) {
                this.imageView01.setEnabled(true);
                Iterator<GeoLayer> tempIter = PubVar._Map.getVectorGeoLayers().getList().iterator();
                while (true) {
                    if (tempIter.hasNext()) {
                        if (tempIter.next().getVisible()) {
                            tempBool = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (tempBool) {
                    this.imageView01.setTag("隐藏矢量背景图层");
                    this.imageView01.setBackgroundResource(R.color.grid_selected_color);
                }
            } else {
                this.imageView01.setEnabled(false);
                this.imageView01.setBackgroundResource(R.color.myRedColor);
            }
            if (PubVar._Map.getRasterLayers().size() > 0) {
                boolean tempBool2 = false;
                this.imageView02.setEnabled(true);
                Iterator<XLayer> tempIter02 = PubVar._Map.getRasterLayers().iterator();
                while (true) {
                    if (tempIter02.hasNext()) {
                        if (tempIter02.next().getVisible()) {
                            tempBool2 = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (tempBool2) {
                    this.imageView02.setTag("隐藏栅格图层");
                    this.imageView02.setBackgroundResource(R.color.grid_selected_color);
                    return;
                }
                return;
            }
            this.imageView02.setEnabled(false);
            this.imageView02.setBackgroundResource(R.color.myRedColor);
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("显示矢量背景图层")) {
                this.imageView01.setTag("隐藏矢量背景图层");
                this.imageView01.setBackgroundResource(R.color.grid_selected_color);
                boolean tmpNeedRefresh = false;
                List<Boolean> tempBoolList = PubVar._Map.getBKVectorLayersVisibleList();
                Iterator<GeoLayer> tempIter = PubVar._Map.getVectorGeoLayers().getList().iterator();
                int tid = 0;
                int tmpCount = tempBoolList.size();
                while (tempIter.hasNext()) {
                    if (tid < tmpCount) {
                        tempIter.next().setVisible(tempBoolList.get(tid).booleanValue());
                    } else {
                        tempIter.next().setVisible(true);
                    }
                    tmpNeedRefresh = true;
                    tid++;
                }
                if (tmpNeedRefresh) {
                    PubVar._Map.Refresh();
                }
            } else if (command.equals("隐藏矢量背景图层")) {
                this.imageView01.setTag("显示矢量背景图层");
                this.imageView01.setBackgroundColor(-921103);
                boolean tmpNeedRefresh2 = false;
                for (GeoLayer geoLayer : PubVar._Map.getVectorGeoLayers().getList()) {
                    geoLayer.setVisible(false);
                    tmpNeedRefresh2 = true;
                }
                if (tmpNeedRefresh2) {
                    PubVar._Map.Refresh();
                }
            } else if (command.equals("显示栅格图层")) {
                this.imageView02.setTag("隐藏栅格图层");
                this.imageView02.setBackgroundResource(R.color.grid_selected_color);
                boolean tmpNeedRefresh3 = false;
                Iterator<XLayer> tempIter2 = PubVar._Map.getRasterLayers().iterator();
                List<Boolean> tempBoolList2 = PubVar._Map.getRasterLayersVisibleList();
                int tid2 = 0;
                int tmpCount2 = tempBoolList2.size();
                while (tempIter2.hasNext()) {
                    if (tid2 < tmpCount2) {
                        tempIter2.next().setVisible(tempBoolList2.get(tid2).booleanValue());
                    } else {
                        tempIter2.next().setVisible(true);
                    }
                    tmpNeedRefresh3 = true;
                    tid2++;
                }
                if (tmpNeedRefresh3) {
                    PubVar._Map.Refresh();
                }
            } else if (command.equals("隐藏栅格图层")) {
                this.imageView02.setTag("显示栅格图层");
                this.imageView02.setBackgroundColor(-921103);
                boolean tmpNeedRefresh4 = false;
                for (XLayer xLayer : PubVar._Map.getRasterLayers()) {
                    xLayer.setVisible(false);
                    tmpNeedRefresh4 = true;
                }
                if (tmpNeedRefresh4) {
                    PubVar._Map.Refresh();
                }
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.SwitchGroupLayer_Dailog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                SwitchGroupLayer_Dailog.this.refreshLayout();
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
                SwitchGroupLayer_Dailog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
