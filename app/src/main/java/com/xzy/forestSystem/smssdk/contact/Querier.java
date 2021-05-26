package com.xzy.forestSystem.smssdk.contact;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.tools.utils.DeviceHelper;

import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: cn.smssdk.contact.c */
public class Querier {

    /* renamed from: a */
    private ContentResolver f116a;

    public Querier(ContentResolver contentResolver) {
        this.f116a = contentResolver;
    }

    /* renamed from: a */
    public ArrayList<HashMap<String, Object>> m471a(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        ArrayList<HashMap<String, Object>> arrayList;
        Object blob;
        try {
            if (!DeviceHelper.getInstance(MobSDK.getContext()).checkPermission("android.permission.READ_CONTACTS")) {
                return null;
            }
        } catch (Throwable th) {
        }
        Cursor query = this.f116a.query(uri, strArr, str, strArr2, str2);
        if (query == null) {
            arrayList = null;
        } else if (query.getCount() == 0) {
            return null;
        } else {
            if (query.moveToFirst()) {
                ArrayList<HashMap<String, Object>> arrayList2 = new ArrayList<>();
                do {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    int columnCount = query.getColumnCount();
                    for (int i = 0; i < columnCount; i++) {
                        String columnName = query.getColumnName(i);
                        try {
                            blob = query.getString(i);
                        } catch (Throwable th2) {
                            blob = query.getBlob(i);
                        }
                        hashMap.put(columnName, blob);
                    }
                    arrayList2.add(hashMap);
                } while (query.moveToNext());
                arrayList = arrayList2;
            } else {
                arrayList = null;
            }
            query.close();
        }
        return arrayList;
    }
}
