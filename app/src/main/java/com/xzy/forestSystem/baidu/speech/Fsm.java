package com.xzy.forestSystem.baidu.speech;

import java.util.HashMap;
import java.util.logging.Logger;

class Fsm {
    private final Logger logger;
    private final HashMap<String, String[]> map;
    private final String name;
    private String oldStatus;

    public Fsm(String name2, HashMap<String, String[]> map2) {
        this.name = name2;
        this.map = map2;
        this.logger = Logger.getLogger(name2);
    }

    public final void check(String status) {
        check(this.oldStatus, status);
        this.oldStatus = status;
    }

    public final void check(String oldStatus2, String realStatus) {
        this.logger.finer(String.format("%s -> %s, @0x%s", oldStatus2, realStatus, Integer.toHexString(hashCode())));
        String[] statuss = this.map.get(oldStatus2);
        boolean ok = false;
        StringBuilder sb = new StringBuilder();
        if (statuss != null) {
            for (String s : statuss) {
                ok |= s.equals(realStatus);
                sb.append("'" + s + "'");
                sb.append(", ");
            }
        }
        if (!ok) {
            throw new IllegalStateException(String.format("old status: %s, expect %s real status is '%s', fail.", oldStatus2, sb, realStatus));
        }
    }
}
