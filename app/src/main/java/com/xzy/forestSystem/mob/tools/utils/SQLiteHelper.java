package com.xzy.forestSystem.mob.tools.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SQLiteHelper {
    public static SingleTableDB getDatabase(Context context, String name) {
        return getDatabase(context.getDatabasePath(name).getPath(), name);
    }

    public static SingleTableDB getDatabase(String path, String name) {
        return new SingleTableDB(path, name);
    }

    public static long insert(SingleTableDB db, ContentValues values) throws Throwable {
        db.open();
        return db.f384db.replace(db.getName(), null, values);
    }

    public static int delete(SingleTableDB db, String selection, String[] selectionArgs) throws Throwable {
        db.open();
        return db.f384db.delete(db.getName(), selection, selectionArgs);
    }

    public static int update(SingleTableDB db, ContentValues values, String selection, String[] selectionArgs) throws Throwable {
        db.open();
        return db.f384db.update(db.getName(), values, selection, selectionArgs);
    }

    public static Cursor query(SingleTableDB db, String[] columns, String selection, String[] selectionArgs, String sortOrder) throws Throwable {
        db.open();
        return db.f384db.query(db.getName(), columns, selection, selectionArgs, null, null, sortOrder);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002a, code lost:
        throw r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0022, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0023, code lost:
        r3.f384db.endTransaction();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void execSQL( com.xzy.forestSystem.mob.tools.utils.SQLiteHelper.SingleTableDB r3, java.lang.String r4) throws java.lang.Throwable {
        /*
             com.xzy.forestSystem.mob.tools.utils.SQLiteHelper.SingleTableDB.access$100(r3)
            android.database.sqlite.SQLiteDatabase r1 =  com.xzy.forestSystem.mob.tools.utils.SQLiteHelper.SingleTableDB.access$300(r3)
            r1.beginTransaction()
            android.database.sqlite.SQLiteDatabase r1 =  com.xzy.forestSystem.mob.tools.utils.SQLiteHelper.SingleTableDB.access$300(r3)     // Catch:{ Throwable -> 0x0020 }
            r1.execSQL(r4)     // Catch:{ Throwable -> 0x0020 }
            android.database.sqlite.SQLiteDatabase r1 =  com.xzy.forestSystem.mob.tools.utils.SQLiteHelper.SingleTableDB.access$300(r3)     // Catch:{ Throwable -> 0x0020 }
            r1.setTransactionSuccessful()     // Catch:{ Throwable -> 0x0020 }
            android.database.sqlite.SQLiteDatabase r1 =  com.xzy.forestSystem.mob.tools.utils.SQLiteHelper.SingleTableDB.access$300(r3)
            r1.endTransaction()
            return
        L_0x0020:
            r0 = move-exception
            throw r0     // Catch:{ all -> 0x0022 }
        L_0x0022:
            r1 = move-exception
            android.database.sqlite.SQLiteDatabase r2 =  com.xzy.forestSystem.mob.tools.utils.SQLiteHelper.SingleTableDB.access$300(r3)
            r2.endTransaction()
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.utils.SQLiteHelper.execSQL( com.xzy.forestSystem.mob.tools.utils.SQLiteHelper$SingleTableDB, java.lang.String):void");
    }

    public static Cursor rawQuery(SingleTableDB db, String script, String[] selectionArgs) throws Throwable {
        db.open();
        return db.f384db.rawQuery(script, selectionArgs);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0036, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0037, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003a, code lost:
        throw r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getSize( com.xzy.forestSystem.mob.tools.utils.SQLiteHelper.SingleTableDB r6) throws java.lang.Throwable {
        /*
             com.xzy.forestSystem.mob.tools.utils.SQLiteHelper.SingleTableDB.access$100(r6)
            r1 = 0
            r0 = 0
            android.database.sqlite.SQLiteDatabase r3 =  com.xzy.forestSystem.mob.tools.utils.SQLiteHelper.SingleTableDB.access$300(r6)     // Catch:{ Throwable -> 0x0034 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0034 }
            r4.<init>()     // Catch:{ Throwable -> 0x0034 }
            java.lang.String r5 = "select count(*) from "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Throwable -> 0x0034 }
            java.lang.String r5 =  com.xzy.forestSystem.mob.tools.utils.SQLiteHelper.SingleTableDB.access$200(r6)     // Catch:{ Throwable -> 0x0034 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Throwable -> 0x0034 }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x0034 }
            r5 = 0
            android.database.Cursor r0 = r3.rawQuery(r4, r5)     // Catch:{ Throwable -> 0x0034 }
            boolean r3 = r0.moveToNext()     // Catch:{ Throwable -> 0x0034 }
            if (r3 == 0) goto L_0x0030
            r3 = 0
            int r1 = r0.getInt(r3)     // Catch:{ Throwable -> 0x0034 }
        L_0x0030:
            r0.close()
            return r1
        L_0x0034:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x0036 }
        L_0x0036:
            r3 = move-exception
            r0.close()
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.utils.SQLiteHelper.getSize( com.xzy.forestSystem.mob.tools.utils.SQLiteHelper$SingleTableDB):int");
    }

    public static void close(SingleTableDB db) {
        db.close();
    }

    public static class SingleTableDB {

        /* renamed from: db */
        private SQLiteDatabase f384db;
        private HashMap<String, Boolean> fieldLimits;
        private LinkedHashMap<String, String> fieldTypes;
        private String name;
        private String path;
        private String primary;
        private boolean primaryAutoincrement;

        private SingleTableDB(String path2, String name2) {
            this.path = path2;
            this.name = name2;
            this.fieldTypes = new LinkedHashMap<>();
            this.fieldLimits = new HashMap<>();
        }

        public void addField(String fieldName, String fieldType, boolean notNull) {
            if (this.f384db == null) {
                this.fieldTypes.put(fieldName, fieldType);
                this.fieldLimits.put(fieldName, Boolean.valueOf(notNull));
            }
        }

        public void setPrimary(String fieldName, boolean autoincrement) {
            this.primary = fieldName;
            this.primaryAutoincrement = autoincrement;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void open() {
            if (this.f384db == null) {
                this.f384db = SQLiteDatabase.openOrCreateDatabase(new File(this.path), (SQLiteDatabase.CursorFactory) null);
                Cursor cursor = this.f384db.rawQuery("select count(*) from sqlite_master where type ='table' and name ='" + this.name + "' ", null);
                boolean shouldCreate = true;
                if (cursor != null) {
                    if (cursor.moveToNext() && cursor.getInt(0) > 0) {
                        shouldCreate = false;
                    }
                    cursor.close();
                }
                if (shouldCreate) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("create table  ").append(this.name).append("(");
                    for (Map.Entry<String, String> ent : this.fieldTypes.entrySet()) {
                        String field = ent.getKey();
                        String type = ent.getValue();
                        boolean notNull = this.fieldLimits.get(field).booleanValue();
                        boolean primary2 = field.equals(this.primary);
                        boolean autoincrement = primary2 ? this.primaryAutoincrement : false;
                        sb.append(field).append(" ").append(type);
                        sb.append(notNull ? " not null" : "");
                        sb.append(primary2 ? " primary key" : "");
                        sb.append(autoincrement ? " autoincrement," : ",");
                    }
                    sb.replace(sb.length() - 1, sb.length(), ");");
                    this.f384db.execSQL(sb.toString());
                }
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void close() {
            if (this.f384db != null) {
                this.f384db.close();
                this.f384db = null;
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private String getName() {
            return this.name;
        }
    }
}
