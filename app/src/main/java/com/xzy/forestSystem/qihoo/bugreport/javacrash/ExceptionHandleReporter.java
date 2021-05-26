package  com.xzy.forestSystem.qihoo.bugreport.javacrash;

import android.content.Context;
import android.content.SharedPreferences;
import  com.xzy.forestSystem.qihoo.jiagu.C0241b;
import  com.xzy.forestSystem.qihoo.jiagu.C0242c;
import  com.xzy.forestSystem.qihoo.jiagu.C0244e;
import  com.xzy.forestSystem.qihoo.jiagutracker.C0246Config;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class ExceptionHandleReporter implements Thread.UncaughtExceptionHandler {

    /* renamed from: a */
    private static ExceptionHandleReporter f394a;

    /* renamed from: b */
    private final Thread.UncaughtExceptionHandler f395b = Thread.getDefaultUncaughtExceptionHandler();

    /* renamed from: c */
    private final Context context;

    /* renamed from: d */
//    private final CrashReportDataFactory f397d = new CrashReportDataFactory(context, new GregorianCalendar());

    /* renamed from: e */
    private final Thread.UncaughtExceptionHandler f398e;

    private ExceptionHandleReporter(Context context, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.context = context;
        this.f398e = uncaughtExceptionHandler;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /* renamed from: a */
    public static synchronized ExceptionHandleReporter m45a(Context context, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        ExceptionHandleReporter exceptionHandleReporter;
        synchronized (ExceptionHandleReporter.class) {
            if (f394a == null) {
                f394a = new ExceptionHandleReporter(context, uncaughtExceptionHandler);
            }
            exceptionHandleReporter = f394a;
        }
        return exceptionHandleReporter;
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        uncaughtException(thread, th, false, 0);
    }

    public void uncaughtException(Thread thread, Throwable th, boolean z) {
        uncaughtException(thread, th, z, 2);
    }

    public void uncaughtException(Thread thread, Throwable th, boolean z, int i) {
        boolean z2 = false;
//        try {
//            String[] strArr = C0241b.f427a;
//            Context context = this.f396c;
//            int length = strArr.length;
//            int i2 = 0;
//            while (true) {
//                if (i2 >= length) {
//                    z2 = true;
//                    break;
//                } else if (!C0242c.m36a(context, strArr[i2])) {
//                    break;
//                } else {
//                    i2++;
//                }
//            }
//            if (!z2) {
////                m43a(thread, th, z);
//                return;
//            }
//            CrashReportDataFactory crashReportDataFactory = this.f397d;
////            EnumMap enumMap = new EnumMap(ReportField.class);
////            enumMap.put((EnumMap) ReportField.t, (ReportField) "1");
////            enumMap.put((EnumMap) ReportField.cpv, (ReportField) "3");
////            enumMap.put((EnumMap) ReportField.st, (ReportField) CrashReportDataFactory.m49a(crashReportDataFactory.f393a));
////            enumMap.put((EnumMap) ReportField.ct, (ReportField) CrashReportDataFactory.m49a(new GregorianCalendar()));
////            String name = th.getClass().getName();
////            enumMap.put((EnumMap) ReportField.et, (ReportField) name);
////            String message = th.getMessage();
////            enumMap.put((EnumMap) ReportField.ec, (ReportField) message);
////            enumMap.put((EnumMap) ReportField.jc, (ReportField) String.valueOf(i));
////            ArrayList<String> a = CrashReportDataFactory.m50a(th);
////            String a2 = crashReportDataFactory.m52a(name, message, a);
////            if (a2 != null) {
////                enumMap.put((EnumMap) ReportField.me, (ReportField) a2);
////            }
////            StringWriter stringWriter = new StringWriter();
////            PrintWriter printWriter = new PrintWriter(stringWriter);
////            th.printStackTrace(printWriter);
////            String obj = stringWriter.toString();
////            printWriter.close();
////            String a3 = CrashReportDataFactory.m53a(obj);
////            if (a3 == null) {
////                enumMap.put((EnumMap) ReportField.crd, (ReportField) C0246Config.EMPTY_STRING);
////            } else {
////                enumMap.put((EnumMap) ReportField.crd, (ReportField) a3);
////            }
////            String a4 = CrashReportDataFactory.m51a(obj, a);
////            if (a4 != null) {
////                enumMap.put((EnumMap) ReportField.em, (ReportField) a4);
////            }
////            String c = CrashReportDataFactory.m46c();
////            if (c != null) {
////                enumMap.put((EnumMap) ReportField.ep, (ReportField) c);
////            }
////            HashMap<String, String> a5 = CrashReportDataFactory.m54a();
////            if (a5.get("ed") != null) {
////                enumMap.put((EnumMap) ReportField.ed, (ReportField) a5.get("ed"));
////            }
////            if (a5.get("epd") != null) {
////                enumMap.put((EnumMap) ReportField.epd, (ReportField) a5.get("epd"));
////            }
////            JSONObject a6 = C0242c.m35a(enumMap, crashReportDataFactory.m47b());
////            if (a6 == null) {
////                m43a(thread, th, z);
////                return;
////            }
////            a6.toString();
////            synchronized (ExceptionHandleReporter.class) {
////                if (m42a(a6, th)) {
////                    m43a(thread, th, z);
////                    return;
////                }
////                C0244e eVar = new C0244e(this, z, a6);
////                eVar.start();
////                eVar.join(4000);
////                m43a(thread, th, z);
////            }
////        } catch (Throwable th2) {
////            m43a(thread, th, z);
////        }
//        }
    }

    /* renamed from: a */
    private synchronized boolean m42a(JSONObject jSONObject, Throwable th) {
        boolean z;
        long j;
        if (!jSONObject.has(ReportField.me.name()) || !jSONObject.has(ReportField.ct.name())) {
            z = true;
        } else {
            SharedPreferences sharedPreferences = context.getSharedPreferences("qihoo_jiagu_crash_report", 0);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            String string = sharedPreferences.getString("last_report_me", "");
            String string2 = sharedPreferences.getString("last_report_time", "0000/00/00 00:00:00");
            String string3 = sharedPreferences.getString("last_exception_info", "");
            String num = Integer.toString(th.hashCode());
            try {
                String string4 = jSONObject.getString(ReportField.me.name());
                String string5 = jSONObject.getString(ReportField.ct.name());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(C0246Config.DATE_TIME_FORMAT_STRING, Locale.ENGLISH);
                Date parse = simpleDateFormat.parse(string5);
                Date parse2 = simpleDateFormat.parse(string2);
                if (parse.getTime() - parse2.getTime() < 0) {
                    edit.putString("last_report_time", jSONObject.getString(ReportField.ct.name()));
                    edit.commit();
                    z = true;
                } else {
                    if (string.equals(string4) || num.equals(string3)) {
                        j = (parse.getTime() - parse2.getTime()) - 600000;
                    } else {
                        j = (parse.getTime() - parse2.getTime()) - 120000;
                    }
                    if (j < 0) {
                        z = true;
                    } else {
                        edit.putString("last_report_me", jSONObject.getString(ReportField.me.name()));
                        edit.putString("last_report_time", jSONObject.getString(ReportField.ct.name()));
                        edit.putString("last_exception_info", num);
                        edit.commit();
                        z = false;
                    }
                }
            } catch (JSONException e) {
                z = true;
            } catch (ParseException e2) {
                z = true;
            }
        }
        return z;
    }

    /* renamed from: a */
    private void m43a(Thread thread, Throwable th, boolean z) {
        if (!z) {
            try {
                if (this.f395b != null) {
                    this.f395b.uncaughtException(thread, th);
                } else {
                    m41b(thread, th, z);
                }
            } catch (Throwable th2) {
                m41b(thread, th, z);
            }
        }
    }

    /* renamed from: b */
    private void m41b(Thread thread, Throwable th, boolean z) {
        if (!z) {
            try {
                if (this.f398e != null) {
                    this.f398e.uncaughtException(thread, th);
                }
            } catch (Throwable th2) {
            }
        }
    }
}
