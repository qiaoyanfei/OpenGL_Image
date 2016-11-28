package android.qiaoyf.com.opengl_image;

import android.opengl.GLES20;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by qiaoyanfei on 16/11/28.
 */
public class NativeOpenGLFragment extends BaseFragment {

    @Override
    protected void texImage2D() {
        PNGInfoHandle.getPNGHandle(getContext().getAssets(), "guide_image_4.png").
                glPngTexImage2D(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        initOpenGL();
        texImage2D();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        PNGInfoHandle.getPNGHandle(getContext().getAssets(), "guide_image_4.png").recycle();

    }
}
