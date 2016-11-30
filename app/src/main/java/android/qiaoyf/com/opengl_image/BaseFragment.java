package android.qiaoyf.com.opengl_image;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;


public abstract class BaseFragment extends Fragment implements GLSurfaceView.Renderer {


    private static final String VERTEX_SHADER_CODE =
            "attribute vec4 position;" +
                    "attribute vec4 inputTextureCoordinate;" +
                    "varying vec2 textureCoordinate;" +
                    "void main()" +
                    "{" +
                    "    gl_Position = position;" +
                    "    textureCoordinate = vec2(inputTextureCoordinate.s, inputTextureCoordinate.t);" +
                    "}";

    private static final String FRAGMENT_SHADER_CODE =
            "varying mediump vec2 textureCoordinate;" +
                    "uniform sampler2D textureSampler;" +
                    "void main() { " +
                    "    gl_FragColor = texture2D(textureSampler, textureCoordinate);" +
                    "}";

    private static final float VERTICES[] = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f,
    };

    private static final float TEXTURE[] = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
    };

    private GLSurfaceView mGLSurfaceView;

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = ((MainActivity) getContext()).getRefWatcher();
        refWatcher.watch(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGLSurfaceView = (GLSurfaceView) inflater.inflate(R.layout.opengl, container, false);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(this);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        return mGLSurfaceView;
    }


    protected void initOpenGL() {
        final int[] texNames = {0};
        glGenTextures(1, texNames, 0);
        glBindTexture(GL_TEXTURE_2D, texNames[0]);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        final int vertexShader = loadShader(GL_VERTEX_SHADER, VERTEX_SHADER_CODE);
        final int pixelShader = loadShader(GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE);
        final int program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, pixelShader);
        glLinkProgram(program);
        glDeleteShader(pixelShader);
        glDeleteShader(vertexShader);
        final int positionLocation = glGetAttribLocation(program, "position");
        final int textureSampler = glGetUniformLocation(program, "textureSampler");
        final int coordinateLocation = glGetAttribLocation(program, "inputTextureCoordinate");
        glUseProgram(program);

        Buffer textureBuffer = createFloatBuffer(TEXTURE);
        Buffer verticesBuffer = createFloatBuffer(VERTICES);
        glVertexAttribPointer(coordinateLocation, 2, GL_FLOAT, false, 0, textureBuffer);
        glEnableVertexAttribArray(coordinateLocation);
        glUniform1i(textureSampler, 0);
        glVertexAttribPointer(positionLocation, 2, GL_FLOAT, false, 0, verticesBuffer);
        glEnableVertexAttribArray(positionLocation);

    }

    private int loadShader(int shaderType, String source) {
        final int shader = glCreateShader(shaderType);
        glShaderSource(shader, source);
        glCompileShader(shader);
        return shader;
    }

    private Buffer createFloatBuffer(float[] floats) {
        return ByteBuffer
                .allocateDirect(floats.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(floats)
                .rewind();
    }

    protected abstract void texImage2D();

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {


    }

    @Override
    public void onDrawFrame(GL10 gl) {

        // Draw background color
        glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

        glDisable(GL_BLEND);
    }

}
