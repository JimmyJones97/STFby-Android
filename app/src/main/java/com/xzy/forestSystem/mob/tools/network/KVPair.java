package com.xzy.forestSystem.mob.tools.network;

public class KVPair<T> {
    public final String name;
    public final T value;

    public KVPair(String name2, T value2) {
        this.name = name2;
        this.value = value2;
    }

    public String toString() {
        return this.name + " = " + ((Object) this.value);
    }
}
