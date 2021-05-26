package com.xzy.forestSystem.baidu.speech.easr.stat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;

public class SynthesizeResultDb {
    private static final String DATABASE_NAME = "ttsdata";
    private static final int DATABASE_VERSION = 1;
    public static final String KEY_CMD_ID = "cmd_id";
    public static final String KEY_CMD_TYPE = "cmd_type";
    public static final String KEY_ERROR_CODE = "code";
    public static final String KEY_RESULT = "result";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TIME = "time";
    private static final String TABLE_RESULT = "result";
    private static final String TABLE_RESULT_CREATE_SQL = "create table result (_id integer primary key autoincrement, time text, code integer, cmd_type integer, cmd_id integer, result text);";
    private static final String TAG = "SynthesizeResultDb";
    private static SynthesizeResultDb mSynthesizeResultDb;
    private volatile boolean isDatabaseClosed = false;
    private Context mContext;
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    private SynthesizeResultDb(Context context) {
        this.mContext = context;
    }

    public static SynthesizeResultDb getInstance(Context context) {
        if (mSynthesizeResultDb == null) {
            synchronized (SynthesizeResultDb.class) {
                if (mSynthesizeResultDb == null) {
                    mSynthesizeResultDb = new SynthesizeResultDb(context);
                }
            }
        }
        return mSynthesizeResultDb;
    }

    public synchronized void open() {
        if (this.mDbHelper == null) {
            this.mDbHelper = new DatabaseHelper(this.mContext);
            this.mDb = this.mDbHelper.getWritableDatabase();
        }
    }

    public void addSynthesizeResult(long time, int errorCode, int cmdType, int cmdId, String result) {
        ContentValues values = new ContentValues();
        values.put(KEY_TIME, Long.valueOf(time));
        values.put(KEY_ERROR_CODE, Integer.valueOf(errorCode));
        values.put(KEY_CMD_TYPE, Integer.valueOf(cmdType));
        values.put(KEY_CMD_ID, Integer.valueOf(cmdId));
        values.put("result", result);
        this.mDb.insert("result", null, values);
    }

    public Cursor querySynthesizeResult() {
        if (this.mDb != null) {
            return this.mDb.query("result", new String[]{KEY_ROWID, KEY_TIME, KEY_ERROR_CODE, KEY_CMD_TYPE, KEY_CMD_ID, "result"}, null, null, null, null, null);
        }
        return null;
    }

    public void deleteSynthesizeResult(List<Integer> ids) {
        if (!(ids == null || ids.size() == 0)) {
            String rowIds = "";
            for (int i = 0; i < ids.size(); i++) {
                rowIds = rowIds + ids.get(i) + ",";
            }
            if (rowIds.length() > 0) {
                this.mDb.delete("result", "_id in (" + rowIds.substring(0, rowIds.length() - 1) + ")", null);
            }
        }
    }

    public synchronized void close() {
        if (this.mContext != null) {
            this.mDbHelper.close();
            this.mDbHelper = null;
            this.mContext = null;
            this.isDatabaseClosed = true;
        }
    }

    public boolean isDatabaseClosed() {
        return this.isDatabaseClosed;
    }

    public static void releaseInstance() {
        if (mSynthesizeResultDb != null) {
            synchronized (SynthesizeResultDb.class) {
                if (mSynthesizeResultDb != null) {
                    mSynthesizeResultDb = null;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, SynthesizeResultDb.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SynthesizeResultDb.TABLE_RESULT_CREATE_SQL);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS result");
            onCreate(db);
        }
    }
}
