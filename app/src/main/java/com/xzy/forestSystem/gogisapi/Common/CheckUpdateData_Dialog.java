package  com.xzy.forestSystem.gogisapi.Common;

import com.xzy.forestSystem.PubVar;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class CheckUpdateData_Dialog {
    public void StartUpdate() {
        String line;
        String line2;
        File tmpFile = new File(String.valueOf(Common.GetSDCardPath()) + "/SysFile/updatedata.txt");
        if (tmpFile != null && tmpFile.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(tmpFile.getAbsolutePath());
                InputStreamReader inputStreamReader = null;
                try {
                    inputStreamReader = new InputStreamReader(inputStream, "gbk");
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                BufferedReader reader = new BufferedReader(inputStreamReader);
                try {
                    String tmpUpdateCode = reader.readLine().trim().substring(1);
                    boolean tmpIsEnd = false;
                    while (!tmpIsEnd) {
                        boolean tmpIsExist = false;
                        SQLiteReader tempSqlReader = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query("Select COUNT(*) From ConfigUpdate Where UpdateInfo ='" + tmpUpdateCode + "'");
                        if (tempSqlReader != null) {
                            if (tempSqlReader.Read() && tempSqlReader.GetInt32(0) > 0) {
                                tmpIsExist = true;
                            }
                            tempSqlReader.Close();
                        }
                        if (!tmpIsExist) {
                            PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().ExecuteSQL("Insert Into ConfigUpdate (UpdateInfo,UpdateTime) Values ('" + tmpUpdateCode + "','" + Common.dateFormat.format(new Date()) + "')");
                            while (true) {
                                line = reader.readLine();
                                if (line == null) {
                                    break;
                                }
                                String tmpType = line.trim().toLowerCase();
                                if (tmpType.indexOf("*") == 0) {
                                    tmpUpdateCode = line.substring(1);
                                    break;
                                } else if (!tmpType.equals("")) {
                                    line = reader.readLine();
                                    if (line.equals("")) {
                                        continue;
                                    } else if (line == null) {
                                        break;
                                    } else if (line.indexOf("*") == 0) {
                                        tmpUpdateCode = line.substring(1);
                                        break;
                                    } else {
                                        SQLiteDBHelper tmpSQLiteDBHelper = getSQLiteDBHelper(tmpType);
                                        if (tmpSQLiteDBHelper != null) {
                                            tmpSQLiteDBHelper.ExecuteSQL(line);
                                        }
                                    }
                                }
                            }
                            if (line == null) {
                                tmpIsEnd = true;
                            }
                        } else {
                            while (true) {
                                line2 = reader.readLine();
                                if (line2 != null) {
                                    if (line2.trim().toLowerCase().indexOf("*") == 0) {
                                        tmpUpdateCode = line2.substring(1);
                                        break;
                                    }
                                } else {
                                    break;
                                }
                            }
                            if (line2 == null) {
                                tmpIsEnd = true;
                            }
                        }
                    }
                } catch (IOException e) {
                }
                inputStream.close();
            } catch (Exception e2) {
            }
        }
    }

    private SQLiteDBHelper getSQLiteDBHelper(String type) {
        if (type.equals("sysconfig")) {
            return PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase();
        }
        if (type.equals("userconfig")) {
            return PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase();
        }
        if (type.equals("projectconfig")) {
            return PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase();
        }
        return null;
    }
}
