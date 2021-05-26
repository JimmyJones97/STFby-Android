package com.xzy.forestSystem.gogisapi.Others;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.xzy.forestSystem.gogisapi.Geodatabase.BaseDataObject;
import com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import com.xzy.forestSystem.gogisapi.MyControls.Input_Dialog;
import com.stczh.gzforestSystem.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class MediaManag_Dialog extends Dialog {
    public static ICallback _Callback = null;
    private ViewPager MyViewPager = null;
    private BaseDataObject _BaseObject = null;
    public Context _Context = null;
    private String _FileNamePre = "";
    private int _PhontoCount = 0;
    private String _PhotoInfo = "";
    private String _PhotoSaveFolder = "";
    private ICallback _returnCallback = null;
    private ImageView m_ImgLeft;
    private ImageView m_ImgRight;
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.MediaManag_Dialog.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String command, Object pObject) {
            Object[] tmpObjs;
            if (command.equals("输入参数") && (tmpObjs = (Object[]) pObject) != null && tmpObjs.length > 1) {
                String tmpReturnTag = String.valueOf(tmpObjs[0]);
                if (tmpReturnTag.equals("照片")) {
                    String tmpFileName = "";
                    if (tmpObjs[1] != null) {
                        tmpFileName = String.valueOf(String.valueOf(tmpObjs[1])) + "_";
                    }
                    Intent localIntent = new Intent(PubVar._PubCommand.m_Context, MediaActivity.class);
                    localIntent.putExtra("Type", 0);
                    localIntent.putExtra("SaveFolder", MediaManag_Dialog.this._PhotoSaveFolder);
                    Date tempTime = new Date();
                    localIntent.putExtra("Date", tempTime.getTime());
                    localIntent.putExtra("FileName", String.valueOf(tmpFileName) + Common.fileDateFormat.format(tempTime) + ".jpg");
                    PubVar._PubCommand.m_Context.startActivity(localIntent);
                } else if (tmpReturnTag.equals("语音")) {
                    String tmpFileName2 = String.valueOf(tmpObjs[1]);
                    AudioRecord_Dialog tempDialog = new AudioRecord_Dialog(MediaManag_Dialog.this.getContext());
                    tempDialog.SetCallback(MediaManag_Dialog._Callback);
                    tempDialog.setFileNamePreString(tmpFileName2);
                    tempDialog.show();
                } else if (tmpReturnTag.equals("视频")) {
                    String tmpFileName3 = "";
                    if (tmpObjs[1] != null) {
                        tmpFileName3 = String.valueOf(String.valueOf(tmpObjs[1])) + "_";
                    }
                    Intent localIntent2 = new Intent(PubVar._PubCommand.m_Context, MediaActivity.class);
                    localIntent2.putExtra("Type", 1);
                    localIntent2.putExtra("SaveFolder", MediaManag_Dialog.this._PhotoSaveFolder);
                    Date tempTime2 = new Date();
                    localIntent2.putExtra("Date", tempTime2.getTime());
                    localIntent2.putExtra("FileName", String.valueOf(tmpFileName3) + Common.fileDateFormat.format(tempTime2) + ".mp4");
                    PubVar._PubCommand.m_Context.startActivity(localIntent2);
                }
            }
        }
    };

    public MediaManag_Dialog(Context paramContext) {
        super(paramContext);
        setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Others.MediaManag_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                MediaManag_Dialog.this.UpdateDialogShowInfo();
            }
        });
        this._Context = paramContext;
        requestWindowFeature(1);
        HideSoftInputMode();
        setContentView(R.layout.mediamang_dialog);
        ViewClick localViewClick = new ViewClick();
        findViewById(R.id.buttonAddAudio).setOnClickListener(localViewClick);
        findViewById(R.id.buttonAddVideo).setOnClickListener(localViewClick);
        findViewById(R.id.buttonAddPhoto).setOnClickListener(localViewClick);
        findViewById(R.id.buttonDelete).setOnClickListener(localViewClick);
        findViewById(R.id.buttonOK).setOnClickListener(localViewClick);
        this.m_ImgLeft = (ImageView) findViewById(R.id.img_ViewLeft);
        this.m_ImgRight = (ImageView) findViewById(R.id.img_ViewRight);
        this.m_ImgLeft.setOnClickListener(localViewClick);
        this.m_ImgRight.setOnClickListener(localViewClick);
        this.MyViewPager = (ViewPager) findViewById(R.id.viewPager);
        this.MyViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.Others.MediaManag_Dialog.3
            @Override // android.support.p000v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override // android.support.p000v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @SuppressLint("WrongConstant")
            @Override // android.support.p000v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int arg0) {
                int arg02 = arg0 + 1;
                if (arg02 < MediaManag_Dialog.this._PhontoCount) {
                    MediaManag_Dialog.this.m_ImgRight.setVisibility(0);
                } else {
                    MediaManag_Dialog.this.m_ImgRight.setVisibility(8);
                }
                if (arg02 > 1) {
                    MediaManag_Dialog.this.m_ImgLeft.setVisibility(0);
                } else {
                    MediaManag_Dialog.this.m_ImgLeft.setVisibility(8);
                }
            }
        });
        _Callback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.MediaManag_Dialog.4
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                MediaManag_Dialog.this.DialogCallbackResult(paramString, pObject);
            }
        };
        this._PhotoSaveFolder = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/Photo";
    }

    public void setFileNamePreString(String fileNamePreString) {
        this._FileNamePre = fileNamePreString;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    @SuppressLint("WrongConstant")
    private void DoCommand(String paramString) {
        try {
            if (paramString.equals("完成")) {
                if (this._BaseObject != null) {
                    String[] tempStrs = this._PhotoInfo.split(",");
                    this._PhotoInfo = "";
                    for (String tempStr : tempStrs) {
                        if (!tempStr.trim().equals("")) {
                            this._PhotoInfo = String.valueOf(this._PhotoInfo) + tempStr + ",";
                        }
                    }
                    this._BaseObject.SetSYS_PHOTO(this._PhotoInfo);
                    if (this._BaseObject.SaveMediaInfo()) {
                        if (this._returnCallback != null) {
                            this._returnCallback.OnClick("多媒体返回", null);
                        }
                        dismiss();
                        return;
                    }
                    return;
                }
                if (this._returnCallback != null) {
                    this._returnCallback.OnClick("多媒体返回", this._PhotoInfo);
                }
                dismiss();
            } else if (paramString.contains("预览")) {
                String[] tempStrs2 = paramString.split(",");
                if (tempStrs2 != null && tempStrs2.length > 1) {
                    String tempFilePath = tempStrs2[1];
                    if (new File(tempFilePath).exists()) {
                        Intent tempIntent = Common.OpenFile(tempFilePath);
                        if (tempIntent != null) {
                            this._Context.startActivity(tempIntent);
                            return;
                        }
                        return;
                    }
                    Common.ShowDialog("文件:\r\n" + tempFilePath + "\r\n不存在.");
                }
            } else if (paramString.contains("文件信息")) {
                String[] tempStrs3 = paramString.split(",");
                if (tempStrs3 != null && tempStrs3.length > 1) {
                    String tempFilePath2 = tempStrs3[1];
                    MediaFileInfo_Dialog tmpMediaFileInfo_Dialog = new MediaFileInfo_Dialog();
                    tmpMediaFileInfo_Dialog.setTextFilePath(tempFilePath2);
                    tmpMediaFileInfo_Dialog.ShowDialog();
                }
            } else if (paramString.contains("共享")) {
                String[] tempStrs4 = paramString.split(",");
                if (tempStrs4 != null && tempStrs4.length > 1) {
                    String tempFilePath3 = tempStrs4[1];
                    File tmpFile = new File(tempFilePath3);
                    if (tmpFile.exists()) {
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(tmpFile));
                        intent.setType("*/*");
                        intent.putExtra("android.intent.extra.SUBJECT", "Share");
                        intent.setFlags(268435456);
                        PubVar.MainContext.startActivity(Intent.createChooser(intent, "分享 " + PubVar.AppName + " 中的多媒体文件"));
                        return;
                    }
                    Common.ShowDialog("文件:\r\n" + tempFilePath3 + "\r\n不存在.");
                }
            } else if (paramString.equals("语音")) {
                if (!PubVar.MediaInfo_Input_Bool) {
                    ICallback iCallback = this.pCallback;
                    Object[] objArr = new Object[2];
                    objArr[0] = "语音";
                    iCallback.OnClick("输入参数", objArr);
                    return;
                }
                Input_Dialog tempDialog = new Input_Dialog();
                tempDialog.setValues("文件名", "文件名:", "", "提示:输入该语音的文件名称.");
                tempDialog.SetCallback(this.pCallback);
                tempDialog.SetReturnTag("语音");
                tempDialog.SetHistoryHashTag("Tag_Media_Audio_Name");
                tempDialog.ShowDialog();
            } else if (paramString.equals("视频")) {
                if (!PubVar.MediaInfo_Input_Bool) {
                    ICallback iCallback2 = this.pCallback;
                    Object[] objArr2 = new Object[2];
                    objArr2[0] = "视频";
                    iCallback2.OnClick("输入参数", objArr2);
                    return;
                }
                Input_Dialog tempDialog2 = new Input_Dialog();
                tempDialog2.setValues("文件名", "文件名:", "", "提示:输入该视频的文件名称.");
                tempDialog2.SetCallback(this.pCallback);
                tempDialog2.SetReturnTag("视频");
                tempDialog2.SetHistoryHashTag("Tag_Media_Video_Name");
                tempDialog2.ShowDialog();
            } else if (paramString.equals("照片")) {
                if (!PubVar.MediaInfo_Input_Bool) {
                    ICallback iCallback3 = this.pCallback;
                    Object[] objArr3 = new Object[2];
                    objArr3[0] = "照片";
                    iCallback3.OnClick("输入参数", objArr3);
                    return;
                }
                Input_Dialog tempDialog3 = new Input_Dialog();
                tempDialog3.setValues("文件名", "文件名:", "", "提示:输入该照片的文件名称.");
                tempDialog3.SetCallback(this.pCallback);
                tempDialog3.SetReturnTag("照片");
                tempDialog3.SetHistoryHashTag("Tag_Media_Photo_Name");
                tempDialog3.ShowDialog();
            } else if (paramString.equals("删除")) {
                String[] tempStrs5 = this._PhotoInfo.split(",");
                if (tempStrs5 == null || tempStrs5.length <= 0) {
                    Common.ShowDialog("没有可删除的文件.");
                } else if (this.MyViewPager.getCurrentItem() < tempStrs5.length) {
                    Common.ShowYesNoDialog(this._Context, "是否确定删除该多媒体文件?\r\n", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.MediaManag_Dialog.5
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString2, Object pObject) {
                            try {
                                if (paramString2.equals("YES")) {
                                    String[] tempStrs6 = MediaManag_Dialog.this._PhotoInfo.split(",");
                                    String tempPhotoInfo = "";
                                    int tempInt = MediaManag_Dialog.this.MyViewPager.getCurrentItem();
                                    int count = tempStrs6.length;
                                    for (int i = 0; i < count; i++) {
                                        if (i != tempInt) {
                                            tempPhotoInfo = String.valueOf(tempPhotoInfo) + tempStrs6[i] + ",";
                                        } else {
                                            String tempFilePath01 = String.valueOf(MediaManag_Dialog.this._PhotoSaveFolder) + FileSelector_Dialog.sRoot + tempStrs6[i];
                                            File tempFile = new File(tempFilePath01);
                                            if (tempFile.exists()) {
                                                tempFile.delete();
                                            }
                                            File tempFile2 = new File(String.valueOf(tempFilePath01.substring(0, tempFilePath01.lastIndexOf(FileSelector_Dialog.sFolder))) + ".txt");
                                            if (tempFile2.exists()) {
                                                tempFile2.delete();
                                            }
                                        }
                                    }
                                    MediaManag_Dialog.this._PhotoInfo = tempPhotoInfo;
                                    MediaManag_Dialog.this.UpdateDialogShowInfo();
                                }
                            } catch (Exception ex) {
                                Common.Log("MediaManag_DoCommand", "错误:" + ex.toString() + "-->" + ex.getMessage());
                            }
                        }
                    });
                } else {
                    Common.ShowDialog("删除文件序号错误.");
                }
            } else if (paramString.equals("上一个文件")) {
                int tmpI = this.MyViewPager.getCurrentItem();
                if (tmpI > 0) {
                    this.MyViewPager.setCurrentItem(tmpI - 1, true);
                } else {
                    this.m_ImgLeft.setVisibility(8);
                }
            } else if (paramString.equals("下一个文件")) {
                int tmpI2 = this.MyViewPager.getCurrentItem();
                if (tmpI2 < this._PhontoCount - 1) {
                    this.MyViewPager.setCurrentItem(tmpI2 + 1, true);
                } else {
                    this.m_ImgRight.setVisibility(8);
                }
            }
        } catch (Exception e) {
        }
    }

    public void HideSoftInputMode() {
        getWindow().setSoftInputMode(3);
    }

    public void ReSetSize(float paramFloat1, float paramFloat2) {
        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        Display localDisplay = ((Activity) PubVar._PubCommand.m_Context).getWindowManager().getDefaultDisplay();
        localLayoutParams.x = 0;
        localLayoutParams.y = 0;
        if (PubVar.m_SCREEN_ORIENTATION == 0) {
            if (paramFloat2 != 0.0f) {
                localLayoutParams.height = (int) (((float) localDisplay.getHeight()) * paramFloat2);
            }
            if (paramFloat1 == 0.0f) {
            }
        }
        localLayoutParams.width = (int) (((float) localDisplay.getWidth()) * paramFloat1);
        getWindow().setAttributes(localLayoutParams);
    }

    public void SetCallback(ICallback tmpICallback) {
        this._returnCallback = tmpICallback;
    }

    public void SetCaption(String paramString) {
        ((TextView) findViewById(R.id.headerbar)).setText(" " + paramString);
    }

    public void SetBasePointObject(BaseDataObject baseDataObject) {
        this._BaseObject = baseDataObject;
        this._PhotoInfo = this._BaseObject.GetSYS_PHOTO();
    }

    public void SetPhotoInfo(String photoInfo) {
        this._PhotoInfo = photoInfo;
    }

    @SuppressLint("WrongConstant")
    public void UpdateDialogShowInfo() {
        String tempStr;
        initViewPager();
        if (this._BaseObject != null) {
            String tempStr2 = String.valueOf(this._BaseObject.GetLabelFieldName()) + "=" + this._BaseObject.GetSYS_LABEL();
            if (!tempStr2.equals("=")) {
                tempStr = String.valueOf(tempStr2) + ", ";
            } else if (this._BaseObject.GetSYS_ID() >= 0) {
                tempStr = "ID=" + this._BaseObject.GetSYS_ID() + ", ";
            } else {
                tempStr = "";
            }
            SetCaption(String.valueOf(tempStr) + "数量=" + this._PhontoCount);
            int tempInt = this.MyViewPager.getCurrentItem();
            if (tempInt < 0 || tempInt >= this._PhontoCount) {
                this.m_ImgRight.setVisibility(8);
            } else {
                this.m_ImgRight.setVisibility(0);
            }
            if (tempInt > 0) {
                this.m_ImgLeft.setVisibility(0);
            } else {
                this.m_ImgLeft.setVisibility(8);
            }
        }
        if (this._PhontoCount == 0) {
            this.m_ImgLeft.setVisibility(8);
            this.m_ImgRight.setVisibility(8);
        }
    }

    @SuppressLint("WrongConstant")
    private void initViewPager() {
        Bitmap tempBmp;
        this._PhontoCount = 0;
        String[] tempStrs = this._PhotoInfo.split(",");
        if (tempStrs != null && tempStrs.length > 0) {
            ArrayList<View> views = new ArrayList<>();
            for (String tempPath : tempStrs) {
                if (tempPath != null && !tempPath.equals("")) {
                    String tempFullPath = String.valueOf(this._PhotoSaveFolder) + FileSelector_Dialog.sRoot + tempPath;
                    if (new File(tempFullPath).exists()) {
                        View tempView = LayoutInflater.from(this._Context).inflate(R.layout.media_single_layout, (ViewGroup) null);
                        ImageView tempImageView = (ImageView) tempView.findViewById(R.id.imageView1);
                        String tempFileType = tempPath.substring(tempPath.lastIndexOf(FileSelector_Dialog.sFolder) + 1).toUpperCase();
                        String tempFileName = tempFullPath.substring(0, tempFullPath.lastIndexOf(FileSelector_Dialog.sFolder));
                        if (tempFileType.equals("JPG") || tempFileType.equals("PNG") || tempFileType.equals("BMP")) {
                            Bitmap tempBmp2 = Common.GetLocalBitmapThumbnail(tempFullPath, 0.0f);
                            if (tempBmp2 != null) {
                                tempImageView.setImageBitmap(tempBmp2);
                                tempImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            }
                        } else if (tempFileType.equals("ARM")) {
                            tempImageView.setImageResource(R.drawable.recorder_stop48);
                        } else if (tempFileType.equals("MP4") && (tempBmp = Common.GetVideoThumbnail(tempFullPath, 1)) != null) {
                            tempImageView.setImageBitmap(tempBmp);
                            tempImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            ImageView tempImageView2 = (ImageView) tempView.findViewById(R.id.imageView2);
                            tempImageView2.setVisibility(0);
                            tempImageView2.setTag("预览," + tempFullPath);
                            tempImageView2.setOnClickListener(new ViewClick());
                        }
                        tempImageView.setTag("预览," + tempFullPath);
                        tempImageView.setOnClickListener(new ViewClick());
                        File tempFile = new File(String.valueOf(tempFileName) + ".txt");
                        if (tempFile.exists()) {
                            Button tempBtn = (Button) tempView.findViewById(R.id.btn_MediaFileInfo);
                            tempBtn.setVisibility(0);
                            tempBtn.setTag("文件信息," + tempFile.getAbsolutePath());
                            tempBtn.setOnClickListener(new ViewClick());
                        }
                        Button tempBtn2 = (Button) tempView.findViewById(R.id.btn_MediaShare);
                        tempBtn2.setVisibility(0);
                        tempBtn2.setTag("共享文件," + tempFullPath);
                        tempBtn2.setOnClickListener(new ViewClick());
                        ((TextView) tempView.findViewById(R.id.textView_filePath)).setText(tempFullPath);
                        views.add(tempView);
                        this._PhontoCount++;
                    }
                }
            }
            MYViewPagerAdapter adapter = new MYViewPagerAdapter();
            adapter.setViews(views);
            this.MyViewPager.setAdapter(adapter);
        }
    }

    /* access modifiers changed from: protected */
    public void DialogCallbackResult(String paramString1, Object object) {
        Object[] tempObj2 = (Object[]) object;
        if (tempObj2 != null && tempObj2.length > 1) {
            String tempFilePath = tempObj2[0].toString();
            String tempFileName = tempFilePath.substring(tempFilePath.lastIndexOf(FileSelector_Dialog.sRoot) + 1);
            String tempFileName2 = tempFileName.substring(0, tempFileName.lastIndexOf(FileSelector_Dialog.sFolder));
            if (new File(tempFilePath).exists()) {
                Date tempDate = new Date(Long.parseLong(tempObj2[1].toString()));
                StringBuilder tempLocInfoSB = new StringBuilder();
                tempLocInfoSB.append("时间：" + Common.dateFormat.format(tempDate) + "\n");
                tempLocInfoSB.append("GPS经度：" + String.valueOf(PubVar._PubCommand.m_GpsLocation.longtitude) + "\n");
                tempLocInfoSB.append("GPS纬度：" + String.valueOf(PubVar._PubCommand.m_GpsLocation.latitude) + "\n");
                tempLocInfoSB.append("高程：" + String.valueOf(PubVar._PubCommand.m_GpsLocation.elevation) + "\n");
                tempLocInfoSB.append("精度：" + String.valueOf(PubVar._PubCommand.m_GpsLocation.accuracy) + "\n");
                tempLocInfoSB.append("方位角：" + String.valueOf(PubVar._PubCommand.m_GpsLocation.bearing) + "°\n");
                tempLocInfoSB.append("俯仰角：" + String.valueOf(PubVar._PubCommand.m_Compass.XAngle) + "°\n");
                tempLocInfoSB.append("横滚角：" + String.valueOf(PubVar._PubCommand.m_Compass.YAngle) + "°\n");
                tempLocInfoSB.append("水平角：" + String.valueOf(PubVar._PubCommand.m_Compass.ZAngle) + "°\n");
                Common.SaveTextFile(String.valueOf(tempFilePath.substring(0, tempFilePath.lastIndexOf(FileSelector_Dialog.sRoot))) + FileSelector_Dialog.sRoot + tempFileName2 + ".txt", tempLocInfoSB.toString());
                if (this._PhotoInfo.length() > 0) {
                    this._PhotoInfo = String.valueOf(this._PhotoInfo) + ",";
                }
                this._PhotoInfo = String.valueOf(this._PhotoInfo) + tempFileName;
                UpdateDialogShowInfo();
                return;
            }
            Common.ShowDialog("文件：\r\n" + tempFilePath + "\r\n不存在.");
        }
    }

    /* access modifiers changed from: package-private */
    public class MYViewPagerAdapter extends PagerAdapter {
        private ArrayList<View> views;

        MYViewPagerAdapter() {
        }

        public void setViews(ArrayList<View> views2) {
            this.views = views2;
        }

        @Override // android.support.p000v4.view.PagerAdapter
        public int getCount() {
            return this.views.size();
        }

        @Override // android.support.p000v4.view.PagerAdapter
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override // android.support.p000v4.view.PagerAdapter
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(this.views.get(position));
        }

        @Override // android.support.p000v4.view.PagerAdapter
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(this.views.get(position));
            return this.views.get(position);
        }
    }

    /* access modifiers changed from: package-private */
    public class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View paramView) {
            if (paramView.getTag() != null) {
                MediaManag_Dialog.this.DoCommand(paramView.getTag().toString());
            }
        }
    }
}
