package com.xzy.forestSystem.mob.tools.utils;

import java.io.FileOutputStream;
import java.nio.channels.FileLock;

public class FileLocker {
    private FileOutputStream fos;
    private FileLock lock;

    public synchronized void setLockFile(String path) {
        try {
            this.fos = new FileOutputStream(path);
        } catch (Throwable th) {
        }
        this.fos = null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x001a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean lock(boolean r4) {
        /*
            r3 = this;
            r1 = 0
            monitor-enter(r3)
            java.io.FileOutputStream r2 = r3.fos     // Catch:{ all -> 0x0037 }
            if (r2 != 0) goto L_0x0008
        L_0x0006:
            monitor-exit(r3)
            return r1
        L_0x0008:
            if (r4 == 0) goto L_0x001c
            java.io.FileOutputStream r2 = r3.fos     // Catch:{ Throwable -> 0x0029 }
            java.nio.channels.FileChannel r2 = r2.getChannel()     // Catch:{ Throwable -> 0x0029 }
            java.nio.channels.FileLock r2 = r2.lock()     // Catch:{ Throwable -> 0x0029 }
            r3.lock = r2     // Catch:{ Throwable -> 0x0029 }
        L_0x0016:
            java.nio.channels.FileLock r2 = r3.lock
            if (r2 == 0) goto L_0x0006
            r1 = 1
            goto L_0x0006
        L_0x001c:
            java.io.FileOutputStream r2 = r3.fos
            java.nio.channels.FileChannel r2 = r2.getChannel()
            java.nio.channels.FileLock r2 = r2.tryLock()
            r3.lock = r2
            goto L_0x0016
        L_0x0029:
            r0 = move-exception
            java.nio.channels.FileLock r2 = r3.lock
            if (r2 == 0) goto L_0x0016
            java.nio.channels.FileLock r2 = r3.lock     // Catch:{ Throwable -> 0x003a }
            r2.release()     // Catch:{ Throwable -> 0x003a }
        L_0x0033:
            r2 = 0
            r3.lock = r2
            goto L_0x0016
        L_0x0037:
            r1 = move-exception
            monitor-exit(r3)
            throw r1
        L_0x003a:
            r2 = move-exception
            goto L_0x0033
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.utils.FileLocker.lock(boolean):boolean");
    }

    public synchronized void lock(Runnable onLock, boolean block) {
        if (lock(block) && onLock != null) {
            onLock.run();
        }
    }

    public synchronized void unlock() {
        if (this.lock != null) {
            try {
                this.lock.release();
                this.lock = null;
            } catch (Throwable th) {
            }
        }
    }

    public synchronized void release() {
        if (this.fos != null) {
            unlock();
            try {
                this.fos.close();
                this.fos = null;
            } catch (Throwable th) {
            }
        }
    }
}
