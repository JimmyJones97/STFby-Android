package  com.xzy.forestSystem.qihoo.util;

import android.content.DialogInterface;
import android.os.Build;

/* renamed from:  com.xzy.forestSystem.qihoo.util.ᵢˎ */
final class DialogInterface$OnClickListenerC0248 implements DialogInterface.OnClickListener {
    DialogInterface$OnClickListenerC0248(RunnableC0247 r1) {
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        Thread currentThread = Thread.currentThread();
        synchronized (currentThread) {
            if (Build.VERSION.SDK_INT >= 19) {
                currentThread.notify();
            }
        }
    }
}
