package  com.xzy.forestSystem.mob.commons.iosbridge;

import  com.xzy.forestSystem.mob.tools.proguard.PublicMemberKeeper;

public class UDPServer implements PublicMemberKeeper {
    public static synchronized void start() {
        synchronized (UDPServer.class) {
        }
    }
}
