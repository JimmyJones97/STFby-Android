package com.xzy.forestSystem.baidu.voicerecognition.android.p007ui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/* renamed from: com.baidu.voicerecognition.android.ui.TipsAdapter */
class TipsAdapter extends ArrayAdapter<String> {
    private String ITEM_FORMAT;
    private int mTextColor;

    private TipsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.ITEM_FORMAT = "%1$d.\"%2$s\"";
    }

    public TipsAdapter(Context context) {
        this(context, 0);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        TextView text;
        if (convertView == null) {
            view = View.inflate(getContext(), getContext().getResources().getIdentifier("bdspeech_suggestion_item", "layout", getContext().getPackageName()), null);
        } else {
            view = convertView;
        }
        try {
            if (view instanceof TextView) {
                text = (TextView) view;
            } else {
                text = (TextView) view.findViewWithTag("textView");
            }
            text.setTextColor(this.mTextColor);
            text.setText(String.format(this.ITEM_FORMAT, Integer.valueOf(position + 1), getItem(position)));
            return view;
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException("ArrayAdapter requires the resource ID to be a TextView", e);
        }
    }

    public void setTextColor(int color) {
        this.mTextColor = color;
    }
}
