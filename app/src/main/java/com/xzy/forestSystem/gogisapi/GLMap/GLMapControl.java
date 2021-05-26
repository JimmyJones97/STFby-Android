package  com.xzy.forestSystem.gogisapi.GLMap;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GLMapControl extends GLSurfaceView {
    public GLMapControl(Context context) {
        super(context);
        try {
            setEGLContextClientVersion(2);
            setRenderer(new MyGLRenderer());
            setRenderMode(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
