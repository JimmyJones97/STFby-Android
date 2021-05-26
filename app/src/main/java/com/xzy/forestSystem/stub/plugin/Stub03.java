package  com.xzy.forestSystem.stub.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.HashMap;
import java.util.Map;

public class Stub03 extends BroadcastReceiver {
    private Map<String, BusiItem> delegates = new HashMap();

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            try {
                String stringExtra = intent.getStringExtra("source");
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
                    if (busiItem.getDelegateImpl() != null && busiItem.getDelegateClz() != null) {
                        ReflectionUtil.invoke(busiItem.getDelegateImpl(), ReflectionUtil.getMethod(busiItem.getDelegateClz(), "onReceive", Context.class, Intent.class), context, intent);
                    }
                }
            } catch (Throwable th) {
            }
        }
    }
}
