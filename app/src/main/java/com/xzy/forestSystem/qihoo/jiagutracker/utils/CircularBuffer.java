package  com.xzy.forestSystem.qihoo.jiagutracker.utils;

import  com.xzy.forestSystem.qihoo.jiagutracker.QVMProtect;
import  com.xzy.forestSystem.stub.StubApp;
import java.util.LinkedList;

@QVMProtect
public class CircularBuffer<E> {
    private final LinkedList<E> buffer = new LinkedList<>();
    private final int mCapacity;

    static {
        StubApp.interface11(3791);
    }

    public native void addElement(E e);

    public native Object[] getData();

    public CircularBuffer(int i) {
        this.mCapacity = i;
    }
}
