package com.xzy.forestSystem.otherlibs.Waterfall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stczh.gzforestSystem.R;
import com.xzy.forestSystem.otherlibs.Imageloader.ImageLoader;

import java.util.ArrayList;

public class ImageGridAdapter extends BaseAdapter {
    private static final boolean DEBUG = true;
    private static final String TAG = "ImageGridAdapter";
    private ArrayList<String> mImageList;
    private LayoutInflater mLayoutInflater;
    private ImageLoader mLoader;

    public ImageGridAdapter(Context context, ArrayList<String> list) {
        this.mLoader = new ImageLoader(context);
        this.mLoader.setIsUseMediaStoreThumbnails(false);
        this.mImageList = list;
        this.mLoader.setRequiredSize(((WindowManager) context.getSystemService("window")).getDefaultDisplay().getWidth() / 3);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mImageList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int arg0) {
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int arg0) {
        return 0;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        String tmpFilePath = this.mImageList.get(position);
        if (convertView == null) {
            convertView = this.mLayoutInflater.inflate(R.layout.waterfall_item_image, (ViewGroup) null);
            ViewHolder holder = new ViewHolder();
            holder.imageView = (ScaleImageView) convertView.findViewById(R.id.Waterfall_ScaleImageView);
            holder.textView = (TextView) convertView.findViewById(R.id.Waterfall_TextView);
            holder.imageView.setOnClickListener(new View.OnClickListener() { // from class: com.stczh.otherlibs.Waterfall.ImageGridAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View arg0) {
//                    C0321Common.ShowDialog(String.valueOf(arg0.getTag()));
                }
            });
            convertView.setTag(holder);
        }
        ViewHolder holder2 = (ViewHolder) convertView.getTag();
        this.mLoader.DisplayImage(tmpFilePath, holder2.imageView);
        holder2.textView.setText(tmpFilePath);
        holder2.imageView.setTag(tmpFilePath);
        return convertView;
    }

    static class ViewHolder {
        ScaleImageView imageView;
        TextView textView;

        ViewHolder() {
        }
    }
}
