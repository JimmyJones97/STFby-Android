package  com.xzy.forestSystem.gogisapi.Others;

import android.content.DialogInterface;
import android.view.View;
import android.widget.ListAdapter;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import com.xzy.forestSystem.otherlibs.Waterfall.ImageGridAdapter;
import com.xzy.forestSystem.otherlibs.Waterfall.MultiColumnListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaManage_Dialog {
    private XDialogTemplate _Dialog;
    private ImageGridAdapter adapter;
    private ArrayList<String> imageUrls;
    private MultiColumnListView mAdapterView;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public MediaManage_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.MediaManage_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
            }
        };
        this.mAdapterView = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.mediamanage_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("多媒体管理");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
    }

    public void queryMediaImages() {
        List<String> tmpLsit = getImagePathFromFolder(Common.GetAPPPath());
        if (tmpLsit.size() > 0) {
            for (String tmpString : tmpLsit) {
                this.imageUrls.add(tmpString);
            }
            this.adapter.notifyDataSetChanged();
        }
    }

    public static List<String> getImagePathFromFolder(String path) {
        List<String> imagePathList = new ArrayList<>();
        File[] files = new File(path).listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    List<String> tmpList2 = getImagePathFromFolder(file.getAbsolutePath());
                    if (tmpList2.size() > 0) {
                        imagePathList.addAll(tmpList2);
                    }
                } else if (checkIsImageFile(file.getPath())) {
                    imagePathList.add(file.getPath());
                }
            }
        }
        return imagePathList;
    }

    public static boolean checkIsImageFile(String fName) {
        String FileEnd = fName.substring(fName.lastIndexOf(FileSelector_Dialog.sFolder) + 1, fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif") || FileEnd.equals("jpeg") || FileEnd.equals("bmp")) {
            return true;
        }
        return false;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Others.MediaManage_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                MediaManage_Dialog.this.mAdapterView = (MultiColumnListView) MediaManage_Dialog.this._Dialog.findViewById(R.id.list);
                MediaManage_Dialog.this.imageUrls = new ArrayList();
                MediaManage_Dialog.this.adapter = new ImageGridAdapter(MediaManage_Dialog.this._Dialog.getContext(), MediaManage_Dialog.this.imageUrls);
                MediaManage_Dialog.this.mAdapterView.setAdapter((ListAdapter) MediaManage_Dialog.this.adapter);
                MediaManage_Dialog.this.queryMediaImages();
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
                view.getTag().toString();
            }
        }
    }
}
