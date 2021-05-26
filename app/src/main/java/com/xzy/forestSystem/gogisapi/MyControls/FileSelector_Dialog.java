package  com.xzy.forestSystem.gogisapi.MyControls;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSelector_Dialog {
    private static Map<String, Integer> imagemap = null;
    public static final String sEmpty = "";
    public static final String sFolder = ".";
    private static final String sOnErrorMsg = "没有进入的权限!";
    public static final String sParent = "..";
    public static final String sRoot = "/";
    public static String tag = "OpenFileDialog";
    LinearLayout LinearLayoutSelector = null;
    private String SelectFilePath = null;
    private String SelectPath = null;
    private XDialogTemplate _Dialog = null;
    private String _Tag = null;
    private View.OnClickListener clickBtnListener = new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog.3
        @Override // android.view.View.OnClickListener
        public void onClick(View arg0) {
            if (arg0.getTag() != null && (arg0.getTag() instanceof String)) {
                FileSelector_Dialog.this.SelectPath = (String) arg0.getTag();
                FileSelector_Dialog.this.refreshListByPath(FileSelector_Dialog.this.SelectPath);
            }
        }
    };
    int fileResID = 0;
    List<Map<String, Object>> fileslistArray = new ArrayList();
    String filter = "";
    int folderResID = 0;
    LinearLayout linearLayoutbottom = null;
    private Context mContext = null;
    private ICallback m_Callback = null;
    private FileSelectorAdapter m_FileSelectorAdapter = null;
    private AdapterView.OnItemClickListener myItemClickListener = new AdapterView.OnItemClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog.2
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            if (FileSelector_Dialog.this.fileslistArray != null && FileSelector_Dialog.this.fileslistArray.size() > 0) {
                String pt = (String) FileSelector_Dialog.this.fileslistArray.get(arg2).get("path");
                String fn = (String) FileSelector_Dialog.this.fileslistArray.get(arg2).get("name");
                if (fn.equals(FileSelector_Dialog.sRoot) || fn.equals(FileSelector_Dialog.sParent)) {
                    String ppt = new File(pt).getParent();
                    if (ppt != null) {
                        FileSelector_Dialog.this.SelectPath = ppt;
                    } else {
                        FileSelector_Dialog.this.SelectPath = Common.GetAPPPath();
                    }
                } else {
                    File fl = new File(pt);
                    if (fl.isFile()) {
                        FileSelector_Dialog.this.SelectFilePath = pt;
                        return;
                    } else if (fl.isDirectory()) {
                        FileSelector_Dialog.this.SelectPath = pt;
                    }
                }
                FileSelector_Dialog.this.refreshListByPath(FileSelector_Dialog.this.SelectPath);
            }
        }
    };
    private ListView myListView;
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
            if (paramString.equals("确定")) {
                if (FileSelector_Dialog.this.fileslistArray == null || FileSelector_Dialog.this.fileslistArray.size() <= 0) {
                    Common.ShowDialog("没有选择任何文件对象.");
                    return;
                }
                String tempMsg = "";
                int tmpCount = 0;
                for (Map<String, Object> tempMap : FileSelector_Dialog.this.fileslistArray) {
                    if (tempMap != null && Boolean.parseBoolean(tempMap.get("isselected").toString())) {
                        tmpCount++;
                        tempMsg = tmpCount > 1 ? String.valueOf(tempMsg) + ";" + tempMap.get("name").toString() + ";" + tempMap.get("path").toString() : String.valueOf(tempMap.get("name").toString()) + ";" + tempMap.get("path").toString();
                    }
                }
                if (FileSelector_Dialog.this.selectMultiFile || tmpCount == 1) {
                    if (FileSelector_Dialog.this.m_Callback != null) {
                        if (FileSelector_Dialog.this._Tag != null) {
                            FileSelector_Dialog.this.m_Callback.OnClick("选择文件_" + FileSelector_Dialog.this._Tag, tempMsg);
                        } else {
                            FileSelector_Dialog.this.m_Callback.OnClick("选择文件", tempMsg);
                        }
                    }
                    FileSelector_Dialog.this._Dialog.dismiss();
                } else if (tmpCount == 0) {
                    Common.ShowDialog("没有选择任何文件.");
                } else {
                    Common.ShowDialog("共选择了多个文件对象:\r\n" + tempMsg);
                }
            } else if (paramString.equals("文件选择勾选返回") && FileSelector_Dialog.this.fileslistArray.size() > 0) {
                int tempTid = 0;
                for (Map<String, Object> tempMap2 : FileSelector_Dialog.this.fileslistArray) {
                    Object tempObj = tempMap2.get("isFile");
                    if (tempObj != null && Boolean.valueOf(tempObj.toString()).booleanValue() && Boolean.valueOf(tempMap2.get("isselected").toString()).booleanValue()) {
                        tempTid++;
                    }
                }
                Common.SetTextViewValueOnID(FileSelector_Dialog.this._Dialog, (int) R.id.tv_selectFileCount, "数量:" + tempTid);
            }
        }
    };
    int rootResID = 0;
    private boolean selectMultiFile = false;
    private List<String> selectedFiles = null;
    Drawable viewRight = null;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public FileSelector_Dialog(String filter2, boolean selectMultiFile2) {
        this.filter = filter2;
        this.selectMultiFile = selectMultiFile2;
        this.SelectPath = Common.GetAPPPath();
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this.mContext = this._Dialog.getContext();
        this._Dialog.SetLayoutView(R.layout.fileselector_dialog);
        this._Dialog.Resize(1.0f, PubVar.DialogHeightRatio);
        this._Dialog.SetCaption("选择文件");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        imagemap = new HashMap();
        this.selectedFiles = new ArrayList();
        this.rootResID = R.drawable.home0148;
        this.folderResID = R.drawable.folder1148;
        this.fileResID = R.drawable.filetype_1048;
        imagemap.put(sRoot, Integer.valueOf((int) R.drawable.home0148));
        imagemap.put(sParent, Integer.valueOf((int) R.drawable.gotolast));
        imagemap.put(sFolder, Integer.valueOf((int) R.drawable.folder1148));
        imagemap.put("folder", Integer.valueOf((int) R.drawable.folder1148));
        imagemap.put("file", Integer.valueOf((int) R.drawable.filetype_1048));
        imagemap.put("shp", Integer.valueOf((int) R.drawable.file_shp));
        imagemap.put("imx", Integer.valueOf((int) R.drawable.file_imx));
        imagemap.put("vmx", Integer.valueOf((int) R.drawable.file_vmx));
        imagemap.put("bmp", Integer.valueOf((int) R.drawable.filetype_bmp02_48));
        imagemap.put("gif", Integer.valueOf((int) R.drawable.filetype_gif02_48));
        imagemap.put("jpg", Integer.valueOf((int) R.drawable.filetype_jpg02_48));
        imagemap.put("png", Integer.valueOf((int) R.drawable.filetype_png02_48));
        imagemap.put("tif", Integer.valueOf((int) R.drawable.filetype_tif02_48));
        imagemap.put("txt", Integer.valueOf((int) R.drawable.filetype_txt02_48));
        imagemap.put("apk", Integer.valueOf((int) R.drawable.android148));
        imagemap.put("csv", Integer.valueOf((int) R.drawable.file_excel));
        imagemap.put("vxd", Integer.valueOf((int) R.drawable.file_vxd));
        imagemap.put("rxd", Integer.valueOf((int) R.drawable.file_rxd));
        imagemap.put("dwg", Integer.valueOf((int) R.drawable.filetype_dwg_48));
        imagemap.put("dxf", Integer.valueOf((int) R.drawable.filetype_dxf_48));
        imagemap.put("xls", Integer.valueOf((int) R.drawable.file_excel));
        this.myListView = (ListView) this._Dialog.findViewById(R.id.listView1);
        this.myListView.setOnItemClickListener(this.myItemClickListener);
        this.LinearLayoutSelector = (LinearLayout) this._Dialog.findViewById(R.id.LinearLayoutSelector);
        this.viewRight = this._Dialog.getContext().getResources().getDrawable(R.drawable.view_right2);
        this.linearLayoutbottom = (LinearLayout) this._Dialog.findViewById(R.id.linearLayoutbottom);
        if (!this.selectMultiFile) {
            this._Dialog.findViewById(R.id.buttonSelectAll).setVisibility(8);
            this._Dialog.findViewById(R.id.buttonSelectDe).setVisibility(8);
        } else {
            this._Dialog.findViewById(R.id.buttonSelectAll).setOnClickListener(new ViewClick());
            this._Dialog.findViewById(R.id.buttonSelectDe).setOnClickListener(new ViewClick());
        }
        this.m_FileSelectorAdapter = new FileSelectorAdapter(this.mContext, this.fileslistArray);
        this.m_FileSelectorAdapter.SetCallback(this.pCallback);
        this.myListView.setAdapter((ListAdapter) this.m_FileSelectorAdapter);
        this._Dialog.findViewById(R.id.buttonSelectFromQQ).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectFromWeiXin).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectFromTIM).setOnClickListener(new ViewClick());
    }

    public void SetTitle(String title) {
        this._Dialog.SetCaption(title);
    }

    public void SetTag(String tag2) {
        this._Tag = tag2;
    }

    private int getImageId(String s) {
        if (imagemap == null) {
            return 0;
        }
        if (imagemap.containsKey(s)) {
            return imagemap.get(s).intValue();
        }
        if (imagemap.containsKey("")) {
            return imagemap.get("").intValue();
        }
        return 0;
    }

    private String getSuffix(String filename) {
        int dix = filename.lastIndexOf(46);
        if (dix < 0) {
            return "";
        }
        return filename.substring(dix + 1);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshListByPath(String path) {
        File[] files;
        refreshNaviBtn(path);
        try {
            files = new File(path).listFiles();
        } catch (Exception e) {
            files = null;
        }
        if (files == null) {
            Common.ShowToast(sOnErrorMsg);
            return;
        }
        if (this.fileslistArray != null) {
            this.fileslistArray.clear();
        } else {
            this.fileslistArray = new ArrayList();
        }
        ArrayList<Map<String, Object>> lfolders = new ArrayList<>();
        ArrayList<Map<String, Object>> lfiles = new ArrayList<>();
        if (!path.equals(sRoot)) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", sRoot);
            map.put("path", sRoot);
            map.put("visible", false);
            map.put("isselected", false);
            map.put("image", Integer.valueOf(this.rootResID));
            this.fileslistArray.add(map);
            Map<String, Object> map2 = new HashMap<>();
            map2.put("name", sParent);
            map2.put("path", path);
            map2.put("visible", false);
            map2.put("isselected", false);
            map2.put("image", Integer.valueOf(this.folderResID));
            this.fileslistArray.add(map2);
        }
        for (File file : files) {
            if (file.isDirectory()) {
                Map<String, Object> map3 = new HashMap<>();
                map3.put("name", file.getName());
                map3.put("isFile", false);
                map3.put("path", file.getPath());
                map3.put("isselected", false);
                map3.put("image", Integer.valueOf(this.folderResID));
                map3.put("visible", false);
                lfolders.add(map3);
            } else if (file.isFile()) {
                int i = this.fileResID;
                String sf = getSuffix(file.getName()).toLowerCase();
                if (this.filter == null || this.filter.length() == 0 || (sf.length() > 0 && this.filter.indexOf(sFolder + sf + ";") >= 0)) {
                    Map<String, Object> map4 = new HashMap<>();
                    map4.put("name", file.getName());
                    map4.put("isFile", true);
                    map4.put("path", file.getPath());
                    int tempImgRes = getImageId(sf);
                    if (tempImgRes <= 0) {
                        tempImgRes = this.fileResID;
                    }
                    map4.put("image", Integer.valueOf(tempImgRes));
                    map4.put("visible", true);
                    map4.put("isselected", false);
                    lfiles.add(map4);
                }
            }
        }
        this.fileslistArray.addAll(lfolders);
        this.fileslistArray.addAll(lfiles);
        this.m_FileSelectorAdapter.notifyDataSetChanged();
    }

    private void refreshNaviBtn(String path) {
        this.LinearLayoutSelector.removeAllViews();
        WindowManager.LayoutParams naviBtnLayout = new WindowManager.LayoutParams();
        naviBtnLayout.width = -2;
        naviBtnLayout.height = -1;
        naviBtnLayout.gravity = 17;
        TextView tempBtn = new TextView(this.mContext);
        tempBtn.setText("我的设备");
        tempBtn.setTag(sRoot);
        tempBtn.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, this.viewRight, (Drawable) null);
        tempBtn.setLayoutParams(naviBtnLayout);
        tempBtn.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        tempBtn.setGravity(17);
        tempBtn.setClickable(true);
        tempBtn.setOnClickListener(this.clickBtnListener);
        this.LinearLayoutSelector.addView(tempBtn);
        if (!path.equals(sRoot)) {
            String[] tempPaths = path.split(sRoot);
            String tempTagPath = "";
            if (tempPaths != null && tempPaths.length > 0) {
                for (String tempPath : tempPaths) {
                    tempTagPath = String.valueOf(tempTagPath) + tempPath + sRoot;
                    if (!tempPath.equals("")) {
                        TextView tempBtn2 = new TextView(this.mContext);
                        tempBtn2.setText(tempPath);
                        tempBtn2.setBackgroundResource(R.drawable.buttonstyle_nonborder);
                        tempBtn2.setTag(tempTagPath);
                        tempBtn2.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, this.viewRight, (Drawable) null);
                        tempBtn2.setLayoutParams(naviBtnLayout);
                        tempBtn2.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                        tempBtn2.setGravity(17);
                        tempBtn2.setClickable(true);
                        tempBtn2.setOnClickListener(this.clickBtnListener);
                        this.LinearLayoutSelector.addView(tempBtn2);
                    }
                }
            }
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog.4
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                FileSelector_Dialog.this.refreshListByPath(FileSelector_Dialog.this.SelectPath);
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("全选")) {
            if (this.fileslistArray != null && this.fileslistArray.size() > 0) {
                int tempTid = 0;
                for (Map<String, Object> tempMap : this.fileslistArray) {
                    Object tempObj = tempMap.get("isFile");
                    if (tempObj != null && Boolean.valueOf(tempObj.toString()).booleanValue()) {
                        tempMap.put("isselected", true);
                        tempTid++;
                    }
                }
                this.m_FileSelectorAdapter.notifyDataSetChanged();
                Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_selectFileCount, "数量:" + tempTid);
            }
        } else if (command.equals("反选")) {
            int tempTid2 = 0;
            for (Map<String, Object> tempMap2 : this.fileslistArray) {
                Object tempObj2 = tempMap2.get("isFile");
                if (tempObj2 != null && Boolean.valueOf(tempObj2.toString()).booleanValue()) {
                    boolean tempBool2 = !Boolean.valueOf(tempMap2.get("isselected").toString()).booleanValue();
                    tempMap2.put("isselected", Boolean.valueOf(tempBool2));
                    if (tempBool2) {
                        tempTid2++;
                    }
                }
            }
            this.m_FileSelectorAdapter.notifyDataSetChanged();
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_selectFileCount, "数量:" + tempTid2);
        } else if (command.equals("QQ文件夹")) {
            File tmpFile = new File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/tencent/QQfile_recv");
            if (tmpFile.exists()) {
                refreshListByPath(tmpFile.getAbsolutePath());
                Common.ShowToast("打开QQ文件夹.");
                return;
            }
            Common.ShowDialog("请检查以下文件夹是否存在?\r\n" + tmpFile.getAbsolutePath());
        } else if (command.equals("WeiXin文件夹")) {
            File tmpFile2 = new File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/tencent/MicroMsg/Download");
            if (tmpFile2.exists()) {
                refreshListByPath(tmpFile2.getAbsolutePath());
                Common.ShowToast("打开微信文件夹.");
                return;
            }
            Common.ShowDialog("请检查以下文件夹是否存在?\r\n" + tmpFile2.getAbsolutePath());
        } else if (command.equals("TIM文件夹")) {
            File tmpFile3 = new File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/tencent/Timfile_recv");
            if (tmpFile3.exists()) {
                refreshListByPath(tmpFile3.getAbsolutePath());
                Common.ShowToast("打开TIM文件夹.");
                return;
            }
            Common.ShowDialog("请检查以下文件夹是否存在?\r\n" + tmpFile3.getAbsolutePath());
        }
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                FileSelector_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
