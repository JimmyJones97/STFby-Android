package  com.xzy.forestSystem.qihoo.util;

import android.content.Context;
import android.os.Build;

public class QHDialog {
    public static void showDialog(Context context, String str) {
        Thread thread = new Thread(new RunnableC0247(context, str));
        synchronized (thread) {
            try {
                thread.start();
                if (Build.VERSION.SDK_INT >= 19) {
                    thread.wait();
                } else {
                    Thread.sleep(3000);
                }
            } catch (InterruptedException e) {
            }
        }
    }
}
