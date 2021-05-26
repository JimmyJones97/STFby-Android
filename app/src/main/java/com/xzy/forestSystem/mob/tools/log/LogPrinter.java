package  com.xzy.forestSystem.mob.tools.log;

import android.content.Context;
import android.text.TextUtils;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.util.HashMap;

public class LogPrinter {
    private HashMap<String, LogCollector> collectors = new HashMap<>();
    private String packageName = "";
    private String scope = "";

    public void setContext(Context context) {
        this.packageName = context.getPackageName();
        if (TextUtils.isEmpty(this.packageName)) {
            this.packageName = "";
        } else {
            this.scope = this.packageName;
        }
    }

    public void setCollector(String sdkTag, LogCollector collector) {
        this.collectors.put(sdkTag, collector);
    }

    public int println(String sdkTag, int priority, int level, String msg) {
        Thread t = Thread.currentThread();
        String message = processMessage(t, msg);
        String scope2 = getScope(t);
        LogCollector collector = this.collectors.get(sdkTag);
        if (collector == null) {
            return 0;
        }
        collector.log(sdkTag, priority, level, scope2, message);
        return 0;
    }

    public void nativeCrashLog(String sdkTag, String log) {
        LogCollector collector = this.collectors.get(sdkTag);
        if (collector != null) {
            collector.log(sdkTag, 6, 2, this.scope, log);
        }
    }

    private String processMessage(Thread t, String msg) {
        return String.format("%s %s", t.getName(), msg);
    }

    private String getScope(Thread t) {
        String fileName;
        StackTraceElement[] stackTrace = t.getStackTrace();
        if (stackTrace == null || stackTrace.length <= 0) {
            return this.scope;
        }
        StackTraceElement trace = stackTrace[stackTrace.length - 1];
        String fileName2 = trace.getFileName();
        if (fileName2 == null || fileName2.length() <= 0) {
            fileName = trace.getClassName();
        } else {
            fileName = this.scope + FileSelector_Dialog.sRoot + fileName2;
        }
        int lineNum = trace.getLineNumber();
        String source = String.valueOf(lineNum);
        if (lineNum < 0 && ((source = trace.getMethodName()) == null || source.length() <= 0)) {
            source = "Unknown Source";
        }
        return fileName + "(" + source + ")";
    }
}
