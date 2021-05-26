package  com.xzy.forestSystem.stub.plugin;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import java.util.HashMap;
import java.util.Map;

public class Stub02 extends Service {
    private Map<String, BusiItem> delegates = new HashMap();

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        MyLog.log("Stub02", "onDestroy", "enter");
        try {
            for (BusiItem busiItem : this.delegates.values()) {
                ReflectionUtil.invoke(busiItem.getDelegateImpl(), ReflectionUtil.getMethod(busiItem.getDelegateClz(), "onDestroy", new Class[0]), new Object[0]);
            }
        } catch (Throwable th) {
        }
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        MyLog.log("Stub02", "onStartCommand", "enter");
        try {
            ReflectionUtil.invokeStatic(ReflectionUtil.getMethod(Class.forName(" com.xzy.forestSystem.stub.stub02.ScreenReceiver"), "registerScreenActionReceiver", Context.class), this);
        } catch (Throwable th) {
        }
        if (intent != null) {
            try {
                String stringExtra = intent.getStringExtra("source");
                MyLog.log("Stub02", "onStartCommand", "source= " + stringExtra);
                BusiItem busiItem = new BusiItem();
                if (stringExtra != null) {
                    if (!this.delegates.containsKey(stringExtra)) {
                        try {
                            busiItem.setDelegateClz(Class.forName(stringExtra));
                        } catch (ClassNotFoundException e) {
                        }
                        if (busiItem.getDelegateClz() != null) {
                            try {
                                busiItem.setDelegateImpl(busiItem.getDelegateClz().newInstance());
                            } catch (IllegalAccessException | InstantiationException e2) {
                            }
                        }
                    } else {
                        busiItem = this.delegates.get(stringExtra);
                    }
                    if (!(busiItem.getDelegateImpl() == null || busiItem.getDelegateClz() == null)) {
                        MyLog.log("Stub02", "onStartCommand", "call impl onStartCommand");
                        ReflectionUtil.invoke(busiItem.getDelegateImpl(), ReflectionUtil.getMethod(busiItem.getDelegateClz(), "onStartCommand", Intent.class, Integer.TYPE, Integer.TYPE, Service.class), intent, Integer.valueOf(i), Integer.valueOf(i2), this);
                    }
                }
            } catch (Throwable th2) {
            }
        }
        return super.onStartCommand(intent, i, i2);
    }
}
