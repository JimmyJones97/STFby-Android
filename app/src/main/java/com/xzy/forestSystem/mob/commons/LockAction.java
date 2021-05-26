package  com.xzy.forestSystem.mob.commons;

import  com.xzy.forestSystem.mob.tools.utils.FileLocker;

public interface LockAction {
    boolean run(FileLocker fileLocker);
}
