package  com.xzy.forestSystem.gogisapi.GLMap;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* compiled from: GLMapControl */
class MyGLRenderer implements GLSurfaceView.Renderer {
    private int _Tag = 0;
    private Triangle mTriangle;
    private FloatBuffer myFB01;
    private FloatBuffer myFB02;

    MyGLRenderer() {
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        this.myFB01 = BufferUtil.floatToBuffer(new float[]{0.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f});
        this.myFB02 = BufferUtil.floatToBuffer(new float[]{1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, -1.0f, 0.0f});
        this.mTriangle = new Triangle(this.myFB01);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(16384);
        if (this._Tag == 0) {
            this.mTriangle.draw(this.myFB01);
            this._Tag = 1;
        } else {
            this.mTriangle.draw(this.myFB02);
            this._Tag = 0;
        }
        GLES20.glDisableVertexAttribArray(34962);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
