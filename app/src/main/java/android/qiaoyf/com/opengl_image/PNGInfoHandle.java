package android.qiaoyf.com.opengl_image;

import android.content.ContentResolver;
import android.content.res.AssetManager;
import android.net.Uri;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by qiaoyanfei on 16/11/16.
 */
public final class PNGInfoHandle {

    static {

        System.loadLibrary("opengl_image");
    }

    private PNGInfoHandle() {
    }

    private PNGInfoHandle(AssetManager assetManager,String fileName) {
        initPNGHandle(assetManager,fileName);
    }

    static PNGInfoHandle getPNGHandle(AssetManager assetManager,String fileName) {

        return new PNGInfoHandle(assetManager,fileName);
    }

    private static native void initPNGHandle(AssetManager assetManager,String fileName);

    private static native int getWidth();

    private static native int getHeight();

    private static native boolean isOpaque();

    private static native void glTexImage2D(int target, int level);

    private static native void free();

    public boolean isPngOpaque(){
        return isOpaque();
    }

    public int getPngWidth(){return getWidth();}

    public int getPngHeight(){return getHeight();}

    public void glPngTexImage2D(int target, int level){
        glTexImage2D(target,level);
    }

    public void recycle() {
        free();
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            recycle();
        } finally {
            super.finalize();
        }
    }




}
