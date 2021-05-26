package  com.xzy.forestSystem.gogisapi.Common;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Encryption.DatabaseHelper;
import java.io.File;

public class SQLiteDBHelper {
    private String _DatabaseName = "";
    private String _Password = "";
    private SQLiteDatabase _SQLiteDatabase = null;
    private DatabaseHelper dbHelper = null;

    public SQLiteDBHelper() {
    }

    public void SetPassword(String pwd) {
        this._Password = pwd;
    }

    public SQLiteDBHelper(String databasePath) {
        this._DatabaseName = databasePath;
        setDatabaseName(this._DatabaseName);
        this.dbHelper = new DatabaseHelper(PubVar.MainContext, this._DatabaseName, null, 1);
    }

    public SQLiteDBHelper(String databasePath, String pwd) {
        this._DatabaseName = databasePath;
        this._Password = pwd;
        this.dbHelper = new DatabaseHelper(PubVar.MainContext, this._DatabaseName, null, 1);
        setDatabaseName(this._DatabaseName);
    }

    public void CreateDatabase(String databasePath) {
        try {
            this._DatabaseName = databasePath;
            this._SQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(databasePath, (SQLiteDatabase.CursorFactory) null);
        } catch (Exception e) {
        }
    }

    public String GetDatabasePath() {
        return this._DatabaseName;
    }

    public SQLiteDatabase GetSQLiteDatabase() {
        return this._SQLiteDatabase;
    }

    public boolean Open() {
        try {
            if (this._SQLiteDatabase != null) {
                if (this._SQLiteDatabase.isOpen()) {
                    return true;
                }
                return false;
            } else if (this._DatabaseName.equals("")) {
                return false;
            } else {
                this._SQLiteDatabase = SQLiteDatabase.openDatabase(this._DatabaseName, null, 0);
                return true;
            }
        } catch (SQLiteException localSQLiteException) {
            if (localSQLiteException.getMessage().contains("Could not open the database in read/write mode") && this._DatabaseName.contains("extSdCard")) {
                Common.ShowDialog("打开数据库时错误!\r\n数据存储在:" + this._DatabaseName + "\r\n由于数据存储在外部存储器上涉及访问权限等问题,请将数据存放在外部存储器(SD卡)中\"Android\\Data\\com.xzy.forestSystem\"文件夹内.\r\n如果没有\"com.xzy.forestSystem\"文件夹,请手动新建该文件夹.");
            }
            if (new File(this._DatabaseName).setWritable(true)) {
                try {
                    this._SQLiteDatabase = SQLiteDatabase.openDatabase(this._DatabaseName, null, 0);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            } else {
                try {
                    this._SQLiteDatabase = SQLiteDatabase.openDatabase(this._DatabaseName, null, 1);
                    return true;
                } catch (Exception ex02) {
                    Common.Log("打开数据库错误", String.valueOf(this._DatabaseName) + ":" + ex02.getMessage());
                    return false;
                }
            }
        }
    }

    public void Close() {
        try {
            if (this._SQLiteDatabase != null && this._SQLiteDatabase.isOpen()) {
                this._SQLiteDatabase.close();
            }
        } catch (Exception e) {
        }
    }

    public boolean ExecuteSQL(String paramString) {
        try {
            if (!Open()) {
                return false;
            }
            this._SQLiteDatabase.execSQL(paramString);
            return true;
        } catch (SQLiteException localSQLiteException) {
            Common.Log("错误", "SQLiteDBHelp(ESQL):" + localSQLiteException.getMessage());
            return false;
        }
    }

    public boolean ExecuteSQL(String paramString, Object[] paramArrayOfObject) {
        try {
            if (!Open()) {
                return false;
            }
            this._SQLiteDatabase.execSQL(paramString, paramArrayOfObject);
            return true;
        } catch (SQLiteException e) {
            return false;
        }
    }

    public SQLiteReader Query(String paramString) {
        try {
            if (Open()) {
                return new SQLiteReader(this._SQLiteDatabase.rawQuery(paramString, null));
            }
            return null;
        } catch (Exception ex) {
            Common.Log("错误", "SQLiteDBHelp(Query):" + ex.getMessage());
            return null;
        }
    }

    public void setDatabaseName(String paramString) {
        try {
            this._DatabaseName = paramString;
            this._SQLiteDatabase = SQLiteDatabase.openDatabase(this._DatabaseName, null, 0);
        } catch (SQLiteException localSQLiteException) {
            Common.Log("错误", "setDatabaseName:" + localSQLiteException.getMessage());
        }
    }

    public boolean BeginTransaction() {
        if (!Open()) {
            return false;
        }
        this._SQLiteDatabase.beginTransaction();
        return true;
    }

    public boolean SetTransactionSuccessful() {
        if (!Open()) {
            return false;
        }
        this._SQLiteDatabase.setTransactionSuccessful();
        return true;
    }

    public boolean EndTransaction() {
        if (!Open()) {
            return false;
        }
        this._SQLiteDatabase.endTransaction();
        return true;
    }

    public boolean ExecuteSQLSimple(String paramString) {
        try {
            this._SQLiteDatabase.execSQL(paramString);
            return true;
        } catch (SQLiteException localSQLiteException) {
            Common.Log("错误", "SQLiteDBHelp(ESQLS):" + localSQLiteException.getMessage());
            return false;
        }
    }

    public boolean ReplaceWithOnConflict(String tableName, ContentValues contentValues) {
        try {
            this._SQLiteDatabase.insertWithOnConflict(tableName, null, contentValues, 5);
            return true;
        } catch (SQLiteException localSQLiteException) {
            Common.Log("错误", "SQLiteDBHelp(ESQLS):" + localSQLiteException.getMessage());
            return false;
        }
    }

    public boolean IsExistTable(String tableName) {
        boolean result = false;
        try {
            SQLiteReader localSQLiteDataReader = Query("SELECT COUNT(*) as count FROM sqlite_master WHERE type='table' and name= '" + tableName + "'");
            if (localSQLiteDataReader != null && localSQLiteDataReader.Read()) {
                if (Integer.parseInt(localSQLiteDataReader.GetString("count")) > 0) {
                    result = true;
                }
                localSQLiteDataReader.Close();
            }
        } catch (Exception e) {
        }
        return result;
    }

    public boolean CheckColumnExist(String tableName, String columnName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            Cursor cursor2 = this._SQLiteDatabase.rawQuery("SELECT * FROM " + tableName + " LIMIT 0", null);
            result = (cursor2 == null || cursor2.getColumnIndex(columnName) == -1) ? false : true;
            if (cursor2 != null && !cursor2.isClosed()) {
                cursor2.close();
            }
        } catch (Exception e) {
            if (0 != 0 && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (0 != 0 && !cursor.isClosed()) {
                cursor.close();
            }
            throw th;
        }
        return result;
    }
}
