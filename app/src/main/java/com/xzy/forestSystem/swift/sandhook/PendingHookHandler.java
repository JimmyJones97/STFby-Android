package  com.xzy.forestSystem.swift.sandhook;

import com.xzy.forestSystem.swift.sandhook.wrapper.HookErrorException;
import com.xzy.forestSystem.swift.sandhook.wrapper.HookWrapper;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class PendingHookHandler {
    private static boolean canUsePendingHook = SandHook.initForPendingHook();
    private static Map<Class, Vector<HookWrapper.HookEntity>> pendingHooks = new ConcurrentHashMap();

    static {
        if (SandHookConfig.delayHook) {
        }
    }

    public static synchronized void addPendingHook(HookWrapper.HookEntity hookEntity) {
        synchronized (PendingHookHandler.class) {
            Vector<HookWrapper.HookEntity> vector = pendingHooks.get(hookEntity.target.getDeclaringClass());
            if (vector == null) {
                vector = new Vector<>();
                pendingHooks.put(hookEntity.target.getDeclaringClass(), vector);
            }
            vector.add(hookEntity);
        }
    }

    public static boolean canWork() {
        return canUsePendingHook && SandHook.canGetObject() && !SandHookConfig.DEBUG;
    }

    public static void onClassInit(long j) {
        Class cls;
        Vector<HookWrapper.HookEntity> vector;
        if (j != 0 && (cls = (Class) SandHook.getObject(j)) != null && (vector = pendingHooks.get(cls)) != null) {
            Iterator<HookWrapper.HookEntity> it = vector.iterator();
            while (it.hasNext()) {
                HookWrapper.HookEntity next = it.next();
                HookLog.m5w("do pending hook for method: " + next.target.toString());
                try {
                    next.initClass = false;
                    SandHook.hook(next);
                } catch (HookErrorException e) {
                    HookLog.m8e("Pending Hook Error!", e);
                }
            }
            pendingHooks.remove(cls);
        }
    }
}
