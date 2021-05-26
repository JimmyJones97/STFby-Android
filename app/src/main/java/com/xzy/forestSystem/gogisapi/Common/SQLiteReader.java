package  com.xzy.forestSystem.gogisapi.Common;

import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class SQLiteReader {
    private Cursor _Cursor = null;

    public SQLiteReader(Cursor paramCursor) {
        this._Cursor = paramCursor;
    }

    public void Close() {
        this._Cursor.close();
    }

    public byte[] GetBlob(int paramInt) {
        return this._Cursor.getBlob(paramInt);
    }

    public byte[] GetBlob(String paramString) {
        return this._Cursor.getBlob(this._Cursor.getColumnIndex(paramString));
    }

    public int GetFieldCount() {
        return this._Cursor.getColumnCount();
    }

    public String[] GetFieldNameList() {
        return this._Cursor.getColumnNames();
    }

    public int GetInt32(int paramInt) {
        return this._Cursor.getInt(paramInt);
    }

    public int GetInt32(String paramString) {
        return this._Cursor.getInt(this._Cursor.getColumnIndex(paramString));
    }

    public long GetLong(int paramInt) {
        return this._Cursor.getLong(paramInt);
    }

    public double GetDouble(int paramInt) {
        return this._Cursor.getDouble(paramInt);
    }

    public double GetDouble(String paramString) {
        return this._Cursor.getDouble(this._Cursor.getColumnIndex(paramString));
    }

    public String GetString(int paramInt) {
        return this._Cursor.getString(paramInt);
    }

    public String GetString(String paramString) {
        return this._Cursor.getString(this._Cursor.getColumnIndex(paramString));
    }

    public List<String> GetValues(int startIndex, int endIndex) {
        List<String> result = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
            result.add(this._Cursor.getString(i));
        }
        return result;
    }

    public boolean Read() {
        return this._Cursor.moveToNext();
    }

    public int GetFieldIndex(String fieldName) {
        int tid = 0;
        String[] tempStrs = this._Cursor.getColumnNames();
        if (tempStrs != null && tempStrs.length > 0) {
            for (String tempStr : tempStrs) {
                if (fieldName.equals(tempStr)) {
                    return tid;
                }
                tid++;
            }
        }
        return -1;
    }
}
