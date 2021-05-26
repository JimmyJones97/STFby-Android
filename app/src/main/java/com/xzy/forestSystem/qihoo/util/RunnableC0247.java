package  com.xzy.forestSystem.qihoo.util;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Looper;
import org.gdal.ogr.ogrConstants;

/* renamed from:  com.xzy.forestSystem.qihoo.util.ᵢˋ */
final class RunnableC0247 implements Runnable {

    /* renamed from: ᵢˋ */
    private /* synthetic */ Context f436;

    /* renamed from: ᵢˎ */
    private /* synthetic */ String f437;

    RunnableC0247(Context context, String str) {
        this.f436 = context;
        this.f437 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Looper.prepare();
        AlertDialog create = new AlertDialog.Builder(this.f436).setMessage(this.f437).setCancelable(false).setPositiveButton("确定", new DialogInterface$OnClickListenerC0248(this)).create();
        create.getWindow().setType(ogrConstants.wkbMultiLineStringM);
        create.show();
        Looper.loop();
    }
}
