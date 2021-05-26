package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.HashMap;

public class Project_Select_Preview_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private String m_DataPreviewImageName;
    private HashMap<String, Object> m_PrjInfo;
    private ICallback pCallback;

    public Project_Select_Preview_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_DataPreviewImageName = null;
        this.m_PrjInfo = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Preview_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("打开项目")) {
                    if (Project_Select_Preview_Dialog.this.m_Callback != null) {
                        Project_Select_Preview_Dialog.this.m_Callback.OnClick("打开项目", Project_Select_Preview_Dialog.this.m_PrjInfo.get("D2"));
                    }
                    Project_Select_Preview_Dialog.this._Dialog.dismiss();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.project_select_datapreview);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("数据预览图");
        this._Dialog.SetHeadButtons("1,2130837859,打开,打开项目", this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void LoadDataPreviewInfo() {
        ImageView localImageView = (ImageView) this._Dialog.findViewById(R.id.iv_datapreview);
        localImageView.setImageBitmap(BitmapFactory.decodeFile(this.m_DataPreviewImageName));
        localImageView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Preview_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View paramView) {
                Project_Select_Preview_Dialog.this._Dialog.dismiss();
            }
        });
    }

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public void SetDataPreviewInfo(HashMap<String, Object> paramHashMap, String paramString) {
        this.m_PrjInfo = paramHashMap;
        this.m_DataPreviewImageName = paramString;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Preview_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Project_Select_Preview_Dialog.this.LoadDataPreviewInfo();
            }
        });
        this._Dialog.show();
    }
}
