package  com.xzy.forestSystem.mob.commons;

import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.utils.FileLocker;
import  com.xzy.forestSystem.mob.tools.utils.ResHelper;
import java.io.File;

/* renamed from:  com.xzy.forestSystem.mob.commons.d */
public class Locks {
    /* renamed from: a */
    public static final File m140a(String str) {
        File file = new File(ResHelper.getCacheRoot(MobSDK.getContext()), str);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    /* renamed from: a */
    public static final void m141a(File file, LockAction lockAction) {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileLocker fileLocker = new FileLocker();
            fileLocker.setLockFile(file.getAbsolutePath());
            if (fileLocker.lock(true) && !lockAction.run(fileLocker)) {
                fileLocker.release();
            }
        } catch (Throwable th) {
            MobLog.getInstance().m57w(th);
        }
    }
}
