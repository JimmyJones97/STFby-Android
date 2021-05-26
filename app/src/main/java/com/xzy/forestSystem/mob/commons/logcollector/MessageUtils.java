package  com.xzy.forestSystem.mob.commons.logcollector;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Base64;
import  com.xzy.forestSystem.mob.tools.MobLog;
import java.util.ArrayList;
import java.util.HashMap;

/* renamed from:  com.xzy.forestSystem.mob.commons.logcollector.f */
public class MessageUtils {
    /* renamed from: a */
    public static synchronized long m74a(long j, String str, int i, String str2) throws Throwable {
        long a;
        synchronized (MessageUtils.class) {
            if (TextUtils.isEmpty(str)) {
                a = -1;
            } else {
                DBProvider a2 = DBProvider.m106a();
                ContentValues contentValues = new ContentValues();
                contentValues.put("exception_time", Long.valueOf(j));
                contentValues.put("exception_msg", str.toString());
                contentValues.put("exception_level", Integer.valueOf(i));
                contentValues.put("exception_md5", str2);
                a = a2.m104a("table_exception", contentValues);
            }
        }
        return a;
    }

    /* renamed from: a */
    public static synchronized long m72a(ArrayList<String> arrayList) throws Throwable {
        long j;
        synchronized (MessageUtils.class) {
            if (arrayList == null) {
                j = 0;
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < arrayList.size(); i++) {
                    sb.append("'");
                    sb.append(arrayList.get(i));
                    sb.append("'");
                    sb.append(",");
                }
                int a = DBProvider.m106a().m103a("table_exception", "exception_md5 in ( " + sb.toString().substring(0, sb.length() - 1) + " )", null);
                MobLog.getInstance().m64i("delete COUNT == %s", Integer.valueOf(a));
                j = (long) a;
            }
        }
        return j;
    }

    /* renamed from: a */
    private static synchronized ArrayList<MessageModel> m73a(String str, String[] strArr) throws Throwable {
        ArrayList<MessageModel> arrayList;
        synchronized (MessageUtils.class) {
            arrayList = new ArrayList<>();
            MessageModel eVar = new MessageModel();
            DBProvider a = DBProvider.m106a();
            String str2 = " select exception_md5, exception_level, exception_time, exception_msg, sum(exception_counts) from table_exception group by exception_md5 having max(_id)";
            if (!TextUtils.isEmpty(str) && strArr != null && strArr.length > 0) {
                str2 = " select exception_md5, exception_level, exception_time, exception_msg, sum(exception_counts) from table_exception where " + str + " group by exception_md5 having max(_id)";
            }
            Cursor a2 = a.m102a(str2, strArr);
            while (true) {
                if (a2 == null || !a2.moveToNext()) {
                    break;
                }
                eVar.f378b.add(a2.getString(0));
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("type", Integer.valueOf(a2.getInt(1)));
                hashMap.put("errat", Long.valueOf(a2.getLong(2)));
                hashMap.put("msg", Base64.encodeToString(a2.getString(3).getBytes(), 2));
                hashMap.put("times", Integer.valueOf(a2.getInt(4)));
                eVar.f377a.add(hashMap);
                if (eVar.f378b.size() == 50) {
                    arrayList.add(eVar);
                    eVar = new MessageModel();
                    break;
                }
            }
            a2.close();
            if (eVar.f378b.size() != 0) {
                arrayList.add(eVar);
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0009, code lost:
        if (r4.length <= 0) goto L_0x000b;
     */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized java.util.ArrayList< com.xzy.forestSystem.mob.commons.logcollector.MessageModel> m71a(java.lang.String[] r4) throws java.lang.Throwable {
        /*
            r1 = 0
            java.lang.Class< com.xzy.forestSystem.mob.commons.logcollector.f> r2 =  com.xzy.forestSystem.mob.commons.logcollector.MessageUtils.class
            monitor-enter(r2)
            java.lang.String r0 = "exception_level = ?"
            if (r4 == 0) goto L_0x000b
            int r3 = r4.length     // Catch:{ all -> 0x0025 }
            if (r3 > 0) goto L_0x000d
        L_0x000b:
            r0 = r1
            r4 = r1
        L_0x000d:
             com.xzy.forestSystem.mob.commons.logcollector.b r1 =  com.xzy.forestSystem.mob.commons.logcollector.DBProvider.m106a()     // Catch:{ all -> 0x0025 }
            java.lang.String r3 = "table_exception"
            int r1 = r1.m105a(r3)     // Catch:{ all -> 0x0025 }
            if (r1 <= 0) goto L_0x001f
            java.util.ArrayList r0 = m73a(r0, r4)     // Catch:{ all -> 0x0025 }
        L_0x001d:
            monitor-exit(r2)
            return r0
        L_0x001f:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            goto L_0x001d
        L_0x0025:
            r0 = move-exception
            monitor-exit(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.commons.logcollector.MessageUtils.m71a(java.lang.String[]):java.util.ArrayList");
    }
}
