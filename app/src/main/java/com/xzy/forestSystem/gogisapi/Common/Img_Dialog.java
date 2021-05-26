package  com.xzy.forestSystem.gogisapi.Common;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.util.Date;

public class Img_Dialog {
    private XDialogTemplate _Dialog;
    private Bitmap m_Bitmap;
    private ICallback m_Callback;
    private ImageView m_ImageView;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public Img_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_Bitmap = null;
        this.m_ImageView = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Common.Img_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("点击图片") && Img_Dialog.this.m_Bitmap != null) {
                    String tmpPath = String.valueOf(Common.GetAPPPath()) + "/Others/QR_" + Common.fileDateFormat.format(new Date());
                    if (Common.SaveImgFile(tmpPath, Img_Dialog.this.m_Bitmap)) {
                        Uri uri = Uri.fromFile(new File(tmpPath));
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("image/*");
                        intent.putExtra("android.intent.extra.STREAM", uri);
                        intent.setFlags(268435456);
                        PubVar.MainContext.startActivity(Intent.createChooser(intent, "分享图片"));
                    }
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.img_dialog);
        this._Dialog.Resize(0.96f, 0.96f);
        this._Dialog.SetCaption("图片");
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetHeadButtons("1,2130837935,分享,点击图片", this.pCallback);
        this.m_ImageView = (ImageView) this._Dialog.findViewById(R.id.img_ShowImg);
    }

    public void SetTitle(String title) {
        this._Dialog.SetCaption(title);
    }

    public void SetImage(Bitmap bitmap) {
        this.m_Bitmap = bitmap;
        if (this.m_Bitmap != null) {
            this.m_ImageView.setImageBitmap(this.m_Bitmap);
            this.m_ImageView.setOnClickListener(new ViewClick());
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.Img_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
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
                Img_Dialog.this.pCallback.OnClick(view.getTag().toString(), null);
            }
        }
    }
}
