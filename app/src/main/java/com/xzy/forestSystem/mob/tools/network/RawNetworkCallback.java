package  com.xzy.forestSystem.mob.tools.network;

import java.io.InputStream;

public interface RawNetworkCallback {
    void onResponse(InputStream inputStream) throws Throwable;
}
