package  com.xzy.forestSystem.gogisapi.GLMap;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* compiled from: GLMapControl */
class TestRenderer implements GLSurfaceView.Renderer {
    private static final int BYTES_PER_FLOAT = 4;
    private final int COLOR_DATA_SIZE = 4;
    private final int COLOR_OFFSET = 3;
    private final int POSITION_DATA_SIZE = 3;
    private final int POSITION_OFFSET = 0;
    private final int STRIDE = 28;
    private int mColorHandle;
    private float[] mMVPMatrix = new float[16];
    private int mMVPMatrixHandle;
    private float[] mModelMatrix = new float[16];
    private int mPositionHandle;
    private float[] mProjectionMatrix = new float[16];
    Triangle mTriangle = null;
    private final FloatBuffer mTriangle1Vertices;
    private float[] mViewMatrix = new float[16];

    public TestRenderer() {
        float[] triangle1VerticesData = {-0.5f, -0.25f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.5f, -0.25f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.559017f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f};
        this.mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTriangle1Vertices.put(triangle1VerticesData).position(0);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(16640);
        Matrix.setIdentityM(this.mModelMatrix, 0);
        this.mTriangle.draw(null);
    }

    private void drawTriandle(FloatBuffer triangleBuffer) {
        triangleBuffer.position(0);
        GLES20.glVertexAttribPointer(this.mPositionHandle, 3, 5126, false, 28, (Buffer) triangleBuffer);
        GLES20.glEnableVertexAttribArray(this.mPositionHandle);
        triangleBuffer.position(3);
        GLES20.glVertexAttribPointer(this.mColorHandle, 4, 5126, false, 28, (Buffer) triangleBuffer);
        GLES20.glEnableVertexAttribArray(this.mColorHandle);
        Matrix.multiplyMM(this.mMVPMatrix, 0, this.mViewMatrix, 0, this.mModelMatrix, 0);
        Matrix.multiplyMM(this.mMVPMatrix, 0, this.mProjectionMatrix, 0, this.mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(this.mMVPMatrixHandle, 1, false, this.mMVPMatrix, 0);
        GLES20.glDrawArrays(4, 0, 3);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = ((float) width) / ((float) height);
        Matrix.frustumM(this.mProjectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 1.0f, 10.0f);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Matrix.setLookAtM(this.mViewMatrix, 0, 0.0f, 0.0f, 1.5f, 0.0f, 0.0f, -5.0f, 0.0f, 1.0f, 0.0f);
        int vertexShaderHandle = GLES20.glCreateShader(35633);
        if (vertexShaderHandle != 0) {
            GLES20.glShaderSource(vertexShaderHandle, "uniform mat4 u_MVPMatrix;      \nattribute vec4 a_Position;     \nattribute vec4 a_Color;        \nvarying vec4 v_Color;          \nvoid main()                    \n{                              \n   v_Color = a_Color;          \n   gl_Position = u_MVPMatrix   \n               * a_Position;   \n}                              \n");
            GLES20.glCompileShader(vertexShaderHandle);
            int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, 35713, compileStatus, 0);
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }
        }
        if (vertexShaderHandle == 0) {
            throw new RuntimeException("failed to creating vertex shader");
        }
        int fragmentShaderHandle = GLES20.glCreateShader(35632);
        if (fragmentShaderHandle != 0) {
            GLES20.glShaderSource(fragmentShaderHandle, "precision mediump float;       \nvarying vec4 v_Color;          \nvoid main()                    \n{                              \n   gl_FragColor = v_Color;     \n}                              \n");
            GLES20.glCompileShader(fragmentShaderHandle);
            int[] compileStatus2 = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, 35713, compileStatus2, 0);
            if (compileStatus2[0] == 0) {
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
            }
        }
        if (fragmentShaderHandle == 0) {
            throw new RuntimeException("failed to create fragment shader");
        }
        int programHandle = GLES20.glCreateProgram();
        if (programHandle != 0) {
            GLES20.glAttachShader(programHandle, vertexShaderHandle);
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
            GLES20.glLinkProgram(programHandle);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, 35714, linkStatus, 0);
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }
        if (programHandle == 0) {
            throw new RuntimeException("failed to create program");
        }
        this.mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        this.mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        this.mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
        GLES20.glUseProgram(programHandle);
    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
