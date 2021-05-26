package  com.xzy.forestSystem.mob.commons.logcollector;

import android.content.ContentValues;
import android.database.Cursor;
import  com.xzy.forestSystem.mob.tools.MobLog;

/* renamed from:  com.xzy.forestSystem.mob.commons.logcollector.b */
public class DBProvider {

    /* renamed from: b */
    private static DBProvider f366b = null;

    /* renamed from: a */
    private DBHelp f367a = new DBHelp();

    private DBProvider() {
    }

    /* renamed from: a */
    public static synchronized DBProvider m106a() {
        DBProvider bVar;
        synchronized (DBProvider.class) {
            if (f366b == null) {
                f366b = new DBProvider();
            }
            bVar = f366b;
        }
        return bVar;
    }

    /* renamed from: a */
    public long m104a(String str, ContentValues contentValues) {
        try {
            return this.f367a.getWritableDatabase().replace(str, null, contentValues);
        } catch (Exception e) {
            MobLog.getInstance().m56w(e, "when insert database occur error table:%s,", str);
            return -1;
        }
    }

    /* renamed from: a */
    public int m103a(String str, String str2, String[] strArr) {
        Exception e;
        int i;
        try {
            i = this.f367a.getWritableDatabase().delete(str, str2, strArr);
            try {
                MobLog.getInstance().m70d("Deleted %d rows from table: %s", Integer.valueOf(i), str);
            } catch (Exception e2) {
                e = e2;
                MobLog.getInstance().m56w(e, "when delete database occur error table:%s,", str);
                return i;
            }
        } catch (Exception e3) {
            e = e3;
            i = 0;
            MobLog.getInstance().m56w(e, "when delete database occur error table:%s,", str);
            return i;
        }
        return i;
    }

    /* renamed from: a */
    public int m105a(String str) {
        Cursor cursor = null;
        int i = 0;
        try {
            cursor = this.f367a.getWritableDatabase().rawQuery("select count(*) from " + str, null);
            if (cursor.moveToNext()) {
                i = cursor.getInt(0);
            }
        } catch (Exception e) {
            MobLog.getInstance().m57w(e);
        } finally {
            cursor.close();
        }
        return i;
    }

    /* renamed from: a */
    public Cursor m102a(String str, String[] strArr) {
        try {
            return this.f367a.getWritableDatabase().rawQuery(str, strArr);
        } catch (Exception e) {
            MobLog.getInstance().m57w(e);
            return null;
        }
    }
}
