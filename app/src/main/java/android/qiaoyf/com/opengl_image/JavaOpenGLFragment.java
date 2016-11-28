package android.qiaoyf.com.opengl_image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by qiaoyanfei on 16/11/28.
 */
public class JavaOpenGLFragment extends BaseFragment {

    @Override
    protected void texImage2D() {
        InputStream in = null;
        try {
            in = getContext().getAssets().open("guide_image_4.png");
        } catch (Exception e) {
        }

        final Bitmap bitmap = BitmapFactory.decodeStream(in);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap, 0);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        initOpenGL();
        texImage2D();
    }

}
