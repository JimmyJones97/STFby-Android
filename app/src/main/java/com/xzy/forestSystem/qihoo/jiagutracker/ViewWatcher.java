package  com.xzy.forestSystem.qihoo.jiagutracker;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import  com.xzy.forestSystem.stub.StubApp;
import java.util.List;
import org.json.JSONObject;

@QVMProtect
public class ViewWatcher {
    private static ViewInfo lastFocusView = null;
    private static ViewWatcher sInstance = null;

    public static native ViewWatcher getInstance();

    public static native int getScreenHeight(Context context);

    public static native int getScreenWidth(Context context);

    private static native List<ViewInfo> rTraverse(View view, String str);

    public static native void recordFocusView(Activity activity);

    private static native List<ViewInfo> watch(ViewGroup viewGroup, String str);

    public static native JSONObject watchOver();

    private static native JSONObject watchViewTree(Activity activity);

    static {
        StubApp.interface11(3784);
    }
}
