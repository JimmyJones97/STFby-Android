package  com.xzy.forestSystem.gogisapi.GLMap;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* compiled from: GLMapControl */
class MyRenderer implements GLSurfaceView.Renderer {
    private final int mBytesPerFloat;
    private float[] mColorArray = {1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f};
    private FloatBuffer mColorBuffer;
    private float[] mTriangleArray = {0.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f};
    private FloatBuffer mTriangleBuffer;
    float[] pentacle;
    FloatBuffer pentacleBuffer;
    int[] rectColor;
    IntBuffer rectColorBuffer;
    float[] rectData;
    float[] rectData2;
    FloatBuffer rectDataBuffer;
    FloatBuffer rectDataBuffer2;
    int[] triangleColor;
    IntBuffer triangleColorBuffer;
    float[] triangleData = {0.1f, 0.6f, 0.0f, -0.3f, 0.0f, 0.0f, 0.3f, 0.1f, 0.0f};
    FloatBuffer triangleDataBuffer;

    public MyRenderer() {
        int[] iArr = new int[12];
        iArr[0] = 65535;
        iArr[5] = 65535;
        iArr[10] = 65535;
        this.triangleColor = iArr;
        this.rectData = new float[]{0.4f, 0.4f, 0.0f, 0.4f, -0.4f, 0.0f, -0.4f, 0.4f, 0.0f, -0.4f, -0.4f, 0.0f};
        int[] iArr2 = new int[16];
        iArr2[1] = 65535;
        iArr2[6] = 65535;
        iArr2[8] = 65535;
        iArr2[12] = 65535;
        iArr2[13] = 65535;
        this.rectColor = iArr2;
        this.rectData2 = new float[]{-0.4f, 0.4f, 0.0f, 0.4f, 0.4f, 0.0f, 0.4f, -0.4f, 0.0f, -0.4f, -0.4f, 0.0f};
        this.pentacle = new float[]{0.4f, 0.4f, 0.0f, -0.2f, 0.3f, 0.0f, 0.5f, 0.0f, 0.0f, -0.4f, 0.0f, 0.0f, -0.1f, -0.3f, 0.0f};
        this.mBytesPerFloat = 4;
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(16384);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }
}
