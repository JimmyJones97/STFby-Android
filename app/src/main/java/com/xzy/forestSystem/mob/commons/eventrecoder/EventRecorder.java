package  com.xzy.forestSystem.mob.commons.eventrecoder;

import android.text.TextUtils;
import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.commons.LockAction;
import  com.xzy.forestSystem.mob.commons.Locks;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.proguard.PublicMemberKeeper;
import  com.xzy.forestSystem.mob.tools.utils.FileLocker;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public final class EventRecorder implements PublicMemberKeeper {

    /* renamed from: a */
    private static File f358a;

    /* renamed from: b */
    private static FileOutputStream f359b;

    public static final synchronized void prepare() {
        synchronized (EventRecorder.class) {
            m122a(new LockAction() { // from class:  com.xzy.forestSystem.mob.commons.eventrecoder.EventRecorder.1
                @Override //  com.xzy.forestSystem.mob.commons.LockAction
                public boolean run(FileLocker fileLocker) {
                    try {
                        File unused = EventRecorder.f358a = new File(MobSDK.getContext().getFilesDir(), ".mrecord");
                        if (!EventRecorder.f358a.exists()) {
                            EventRecorder.f358a.createNewFile();
                        }
                        FileOutputStream unused2 = EventRecorder.f359b = new FileOutputStream(EventRecorder.f358a, true);
                        return false;
                    } catch (Throwable th) {
                        MobLog.getInstance().m57w(th);
                        return false;
                    }
                }
            });
        }
    }

    public static final synchronized void addBegin(String str, String str2) {
        synchronized (EventRecorder.class) {
            m119a(str + " " + str2 + " 0\n");
        }
    }

    /* renamed from: a */
    private static final void m122a(LockAction lockAction) {
        Locks.m141a(new File(MobSDK.getContext().getFilesDir(), "comm/locks/.mrlock"), lockAction);
    }

    public static final synchronized void addEnd(String str, String str2) {
        synchronized (EventRecorder.class) {
            m119a(str + " " + str2 + " 1\n");
        }
    }

    /* renamed from: a */
    private static final void m119a(final String str) {
        m122a(new LockAction() { // from class:  com.xzy.forestSystem.mob.commons.eventrecoder.EventRecorder.2
            @Override //  com.xzy.forestSystem.mob.commons.LockAction
            public boolean run(FileLocker fileLocker) {
                try {
                    EventRecorder.f359b.write(str.getBytes("utf-8"));
                    EventRecorder.f359b.flush();
                    return false;
                } catch (Throwable th) {
                    MobLog.getInstance().m57w(th);
                    return false;
                }
            }
        });
    }

    public static final synchronized String checkRecord(final String str) {
        String str2;
        synchronized (EventRecorder.class) {
            final LinkedList linkedList = new LinkedList();
            m122a(new LockAction() { // from class:  com.xzy.forestSystem.mob.commons.eventrecoder.EventRecorder.3
                @Override //  com.xzy.forestSystem.mob.commons.LockAction
                public boolean run(FileLocker fileLocker) {
                    int indexOf;
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(EventRecorder.f358a), "utf-8"));
                        for (String readLine = bufferedReader.readLine(); !TextUtils.isEmpty(readLine); readLine = bufferedReader.readLine()) {
                            String[] split = readLine.split(" ");
                            if (str.equals(split[0])) {
                                if ("0".equals(split[2])) {
                                    linkedList.add(split[1]);
                                } else if ("1".equals(split[2]) && (indexOf = linkedList.indexOf(split[1])) != -1) {
                                    linkedList.remove(indexOf);
                                }
                            }
                        }
                        bufferedReader.close();
                    } catch (Throwable th) {
                        MobLog.getInstance().m69d(th);
                    }
                    return false;
                }
            });
            if (linkedList.size() > 0) {
                str2 = (String) linkedList.get(0);
            } else {
                str2 = null;
            }
        }
        return str2;
    }

    public static final synchronized void clear() {
        synchronized (EventRecorder.class) {
            m122a(new LockAction() { // from class:  com.xzy.forestSystem.mob.commons.eventrecoder.EventRecorder.4
                @Override //  com.xzy.forestSystem.mob.commons.LockAction
                public boolean run(FileLocker fileLocker) {
                    try {
                        EventRecorder.f359b.close();
                        EventRecorder.f358a.delete();
                        File unused = EventRecorder.f358a = new File(MobSDK.getContext().getFilesDir(), ".mrecord");
                        EventRecorder.f358a.createNewFile();
                        FileOutputStream unused2 = EventRecorder.f359b = new FileOutputStream(EventRecorder.f358a, true);
                        return false;
                    } catch (Throwable th) {
                        MobLog.getInstance().m57w(th);
                        return false;
                    }
                }
            });
        }
    }
}
