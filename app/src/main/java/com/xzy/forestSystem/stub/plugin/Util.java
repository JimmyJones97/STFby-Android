package  com.xzy.forestSystem.stub.plugin;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

public class Util {
    public static void setThemeWithSdkVersion(Activity activity) {
        if (Build.VERSION.SDK_INT >= 14) {
            activity.setTheme(16973941);
        } else {
            activity.setTheme(16973835);
        }
        WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
        attributes.alpha = 0.0f;
        activity.getWindow().setAttributes(attributes);
    }
}
