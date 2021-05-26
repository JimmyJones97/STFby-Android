package  com.xzy.forestSystem.gogisapi.Track;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class TrackManage {
    private String _DBFilePath;
    public SQLiteDBHelper _EDatabase;

    public TrackManage() {
        this._EDatabase = null;
        this._DBFilePath = "";
        this._EDatabase = new SQLiteDBHelper();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        this._DBFilePath = String.valueOf(PubVar.m_SystemPath) + "/Track/Track" + String.valueOf(calendar.get(1)) + ".dbx";
        if (!new File(this._DBFilePath).exists()) {
            CreateDataset(this._DBFilePath);
        } else {
            Open();
        }
    }

    public boolean Open() {
        this._EDatabase.setDatabaseName(this._DBFilePath);
        return true;
    }

    private void CreateDataset(String filepath) {
        this._EDatabase.CreateDatabase(filepath);
        for (int i = 1; i < 13; i++) {
            ExcuteSQL("CREATE TABLE IF NOT EXISTS T" + String.format("%02d", Integer.valueOf(i)) + " (SYS_ID Integer primary key not null UNIQUE,Time Long Default 0,TimeStr TEXT,JD double DEFAULT 0,WD double DEFAULT 0,GC double DEFAULT 0,SPEED double DEFAULT 0,Direction double DEFAULT 0,ACCURACY double DEFAULT 0,F1 Text,F2 TEXT,F3 TEXT,F4 TEXT,F5 TEXT)");
        }
    }

    public boolean ExcuteSQL(String paramString) {
        return this._EDatabase.ExecuteSQL(paramString);
    }

    public SQLiteDBHelper GetSQLiteDatabase() {
        return this._EDatabase;
    }
}
