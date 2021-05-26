package  com.xzy.forestSystem.gogisapi.GLMap;

import android.opengl.GLES20;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/* compiled from: GLMapControl */
class Triangle {
    static final int COORDS_PER_VERTEX = 3;
    private FloatBuffer _vertexBuffer = null;
    float[] color = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};
    private final String fragmentShaderCode = "precision mediump float;uniform vec4 vColor;void main() {  gl_FragColor = vColor;}";
    private int mColorHandle;
    private int mPositionHandle;
    private final int mProgram;
    float[] triangleCoords = {0.0f, 0.0f, 0.0f, -0.5f, -0.31100425f, 0.0f, 0.5f, -0.31100425f, 0.0f};
    private final FloatBuffer vertexBuffer1 = null;
    private final int vertexCount = (this.triangleCoords.length / 3);
    private final String vertexShaderCode = "attribute vec4 vPosition;void main() {  gl_Position = vPosition;}";
    private final int vertexStride = 12;

    public Triangle(FloatBuffer vertexBuffer2) {
        ByteBuffer.allocateDirect(this.triangleCoords.length * 4).order(ByteOrder.nativeOrder());
        this._vertexBuffer = vertexBuffer2;
        int vertexShader = MyGLRenderer.loadShader(35633, "attribute vec4 vPosition;void main() {  gl_Position = vPosition;}");
        int fragmentShader = MyGLRenderer.loadShader(35632, "precision mediump float;uniform vec4 vColor;void main() {  gl_FragColor = vColor;}");
        this.mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(this.mProgram, vertexShader);
        GLES20.glAttachShader(this.mProgram, fragmentShader);
        GLES20.glLinkProgram(this.mProgram);
    }

    public void draw(FloatBuffer myvertexBuffer) {
        GLES20.glUseProgram(this.mProgram);
        this.mPositionHandle = GLES20.glGetAttribLocation(this.mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(this.mPositionHandle);
        GLES20.glVertexAttribPointer(this.mPositionHandle, 3, 5126, false, 12, (Buffer) myvertexBuffer);
        this.mColorHandle = GLES20.glGetUniformLocation(this.mProgram, "vColor");
        GLES20.glUniform4fv(this.mColorHandle, 1, this.color, 0);
        GLES20.glDrawArrays(4, 0, this.vertexCount);
        GLES20.glDisableVertexAttribArray(this.mPositionHandle);
    }
}
