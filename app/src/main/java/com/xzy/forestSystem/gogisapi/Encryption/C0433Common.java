package  com.xzy.forestSystem.gogisapi.Encryption;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;

/* renamed from:  com.xzy.forestSystem.gogisapi.Encryption.Common */
public class C0433Common {
    public static boolean RemovePassword(Context context, String filepath, String password) {
        try {
            File tmpCacheFile = new File(String.valueOf(filepath) + "2");
            if (tmpCacheFile.exists()) {
                tmpCacheFile.delete();
            }
            SQLiteDatabase tmpDB = new DatabaseHelper(context, filepath, null, 1).getWritableDatabase();
            tmpDB.execSQL("attach DATABASE '" + filepath + "2' as decryptdb key '';");
            tmpDB.execSQL("SELECT sqlcipher_export('decryptdb')");
            tmpDB.execSQL("DETACH DATABASE decryptdb");
            tmpDB.close();
            File tmpOrigeFile = new File(filepath);
            tmpOrigeFile.delete();
            tmpCacheFile.renameTo(tmpOrigeFile);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean CreatePassword(Context context, String filepath, String password) {
        try {
            File tmpCacheFile = new File(String.valueOf(filepath) + "2");
            if (tmpCacheFile.exists()) {
                tmpCacheFile.delete();
            }
            SQLiteDatabase tmpDB = new DatabaseHelper(context, filepath, null, 1).getWritableDatabase();
            tmpDB.execSQL("attach DATABASE '" + filepath + "2' as decryptdb key '" + password + "';");
            tmpDB.execSQL("SELECT sqlcipher_export('decryptdb')");
            tmpDB.execSQL("DETACH DATABASE decryptdb");
            tmpDB.close();
            File tmpOrigeFile = new File(filepath);
            tmpOrigeFile.delete();
            tmpCacheFile.renameTo(tmpOrigeFile);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean ModifyPassword(Context context, String filepath, String password, String newPassowrd) {
        try {
            if (!new File(filepath).exists()) {
                return false;
            }
            DatabaseHelper tmpDBHelper = new DatabaseHelper(context, filepath, null, 1);
            tmpDBHelper.getWritableDatabase().close();
            tmpDBHelper.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
