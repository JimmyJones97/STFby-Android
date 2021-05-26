package  com.xzy.forestSystem.gogisapi.Encryption;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import  com.xzy.forestSystem.gogisapi.System.AuthorizeTools;
import java.io.File;

public class DataAuthority {
    private static String ENCRYPT_SYS_KEY = "";
    private int _DataSourceType = 0;
    private String _DatabsePath = null;
    private boolean _Enable = false;
    private boolean _EnableRead = false;
    private boolean _EnableWrite = false;
    private boolean _IsEncrypt = false;
    private boolean _NeedPwdAccess = true;
    private String _Password = "";

    public DataAuthority(int dataSourceType) {
        this._DataSourceType = dataSourceType;
        this._Enable = false;
        this._EnableRead = false;
        this._NeedPwdAccess = true;
        this._EnableWrite = false;
        this._IsEncrypt = false;
    }

    public void CloneFrom(DataAuthority dataAuthority) {
        this._DatabsePath = dataAuthority._DatabsePath;
        this._DataSourceType = dataAuthority._DataSourceType;
        this._Enable = dataAuthority._Enable;
        this._EnableRead = dataAuthority._EnableRead;
        this._EnableWrite = dataAuthority._EnableWrite;
        this._IsEncrypt = dataAuthority._IsEncrypt;
        this._NeedPwdAccess = dataAuthority._NeedPwdAccess;
        this._Password = dataAuthority._Password;
    }

    public boolean IsEnable() {
        return this._Enable;
    }

    public boolean IsEnableRead() {
        return this._EnableRead;
    }

    public boolean IsEnableWrite() {
        return this._EnableWrite;
    }

    public boolean IsEncrypt() {
        return this._IsEncrypt;
    }

    public void SetIsEncrypt(boolean isEncrypt) {
        this._IsEncrypt = isEncrypt;
    }

    public boolean NeedPwdAccess() {
        return this._NeedPwdAccess;
    }

    public String getDataKey() {
        if (this._DataSourceType == 0) {
            return this._Password;
        }
        if (this._DataSourceType != 1 || !this._IsEncrypt) {
            return "";
        }
        return getSysKey();
    }

    public String getPassword() {
        return this._Password;
    }

    public boolean Initial(String filepath, String inputPwd, BasicValue outMsg) {
        this._DatabsePath = filepath;
        this._Password = inputPwd;
        this._IsEncrypt = false;
        if (new File(this._DatabsePath).exists()) {
            if (this._DataSourceType == 0) {
                try {
                    SQLiteDatabase tmpDB = new DatabaseHelper(PubVar.MainContext, this._DatabsePath, null, 1).getReadableDatabase();
                    if (tmpDB != null) {
                        this._Enable = true;
                        if (this._Password.equals("")) {
                            this._IsEncrypt = false;
                        } else {
                            this._IsEncrypt = true;
                        }
                        this._EnableRead = true;
                        this._NeedPwdAccess = false;
                        this._EnableWrite = true;
                        tmpDB.close();
                        return true;
                    }
                } catch (Exception e) {
                    this._IsEncrypt = true;
                    outMsg.setValue("密码不正确.");
                    return false;
                }
            } else if (this._DataSourceType == 1) {
                try {
                    SQLiteDatabase tmpDB2 = new DatabaseHelper(PubVar.MainContext, this._DatabsePath, null, 1).getReadableDatabase();
                    if (tmpDB2 != null) {
                        this._Enable = true;
                        tmpDB2.close();
                        this._IsEncrypt = false;
                        this._EnableRead = true;
                        this._NeedPwdAccess = false;
                        this._EnableWrite = true;
                        return true;
                    }
                } catch (Exception e2) {
                    this._IsEncrypt = true;
                }
                if (!this._Enable) {
                    getSysKey();
                    try {
                        SQLiteDatabase tmpDB3 = new DatabaseHelper(PubVar.MainContext, this._DatabsePath, null, 1).getWritableDatabase();
                        if (tmpDB3 != null) {
                            this._IsEncrypt = true;
                            Cursor tmpCursor = tmpDB3.rawQuery("select count(*) from sqlite_master where type='table' and name='Authority'", null);
                            if (tmpCursor.moveToNext()) {
                                int tmpInt = tmpCursor.getInt(0);
                                tmpCursor.close();
                                if (tmpInt > 0) {
                                    if (getAutorityValue(tmpDB3, "NeedPwdAccess").equals("true")) {
                                        this._NeedPwdAccess = true;
                                    } else {
                                        this._NeedPwdAccess = false;
                                    }
                                    if (!this._NeedPwdAccess) {
                                        String tmpDeviceID = PubVar.m_AuthorizeTools.getUserAndroidID();
                                        if (getAutorityValue(tmpDB3, "AllowRead").contains(tmpDeviceID)) {
                                            this._EnableRead = true;
                                        }
                                        if (getAutorityValue(tmpDB3, "AllowWrite").contains(tmpDeviceID)) {
                                            this._EnableWrite = true;
                                        }
                                        if (this._EnableRead) {
                                            this._Enable = true;
                                            tmpDB3.close();
                                            return true;
                                        }
                                        outMsg.setValue("本设备未授权使用该数据.");
                                        tmpDB3.close();
                                        return false;
                                    } else if (!getAutorityValue(tmpDB3, "AccessPassword").equals(inputPwd)) {
                                        outMsg.setValue("输入密码不正确.");
                                        tmpDB3.close();
                                        return false;
                                    } else {
                                        this._Enable = true;
                                        this._EnableRead = true;
                                        this._EnableWrite = true;
                                        return true;
                                    }
                                } else {
                                    tmpDB3.execSQL("Create Table If Not Exists Authority (ID integer primary key AutoIncrement,ParaName varchar(255) NOT NULL Unique Default '', ParaValue TEXT Default '')");
                                    String tmpDeviceID2 = PubVar.m_AuthorizeTools.getUserAndroidID();
                                    tmpDB3.execSQL("Replace Into Authority (ParaName, ParaValue) Values ('AllowRead','" + tmpDeviceID2 + "');");
                                    tmpDB3.execSQL("Replace Into Authority (ParaName, ParaValue) Values ('AllowWrite','" + tmpDeviceID2 + "');");
                                    this._Enable = true;
                                    this._EnableRead = true;
                                    this._EnableWrite = true;
                                    this._NeedPwdAccess = false;
                                    tmpDB3.close();
                                    return true;
                                }
                            } else {
                                tmpDB3.execSQL("Create Table If Not Exists Authority (ID integer primary key AutoIncrement,ParaName varchar(255) NOT NULL Unique Default '', ParaValue TEXT Default '')");
                                String tmpDeviceID3 = PubVar.m_AuthorizeTools.getUserAndroidID();
                                tmpDB3.execSQL("Replace Into Authority (ParaName, ParaValue) Values ('AllowRead','" + tmpDeviceID3 + "');");
                                tmpDB3.execSQL("Replace Into Authority (ParaName, ParaValue) Values ('AllowWrite','" + tmpDeviceID3 + "');");
                                this._Enable = true;
                                this._EnableRead = true;
                                this._EnableWrite = true;
                                this._NeedPwdAccess = false;
                                tmpDB3.close();
                                return true;
                            }
                        }
                    } catch (Exception ex) {
                        outMsg.setValue("打开数据文件错误:" + ex.getMessage());
                        return false;
                    }
                } else {
                    this._NeedPwdAccess = false;
                    return true;
                }
            }
        }
        return false;
    }

    private String getAutorityValue(SQLiteDatabase db, String paraName) {
        try {
            Cursor tmpCursor = db.rawQuery("select ParaValue from Authority Where ParaName='" + paraName + "'", null);
            if (!tmpCursor.moveToNext()) {
                return "";
            }
            String tmpStr = String.valueOf(tmpCursor.getString(0));
            tmpCursor.close();
            return tmpStr;
        } catch (Exception e) {
            return "";
        }
    }

    private String getSysKey() {
        if (ENCRYPT_SYS_KEY.equals("")) {
            try {
                ENCRYPT_SYS_KEY = AuthorizeTools.encrypt("XZYMILLIONTEC2015WHUZYX", "AroundTheWorld");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (ENCRYPT_SYS_KEY.equals("")) {
            ENCRYPT_SYS_KEY = "XZYZeyunXiaoWHU2015UHW";
        }
        return ENCRYPT_SYS_KEY;
    }
}
