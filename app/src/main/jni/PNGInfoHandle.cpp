//
// Created by qiaoyanfei on 16/11/16.
//


#include "android_qiaoyf_com_opengl_image_PNGInfoHandle.h"
#include "ParsePngFile.h"

ParsePngFile* parsePngTool = NULL;

JNIEXPORT void JNICALL Java_android_qiaoyf_com_opengl_1image_PNGInfoHandle_initPNGHandle
(JNIEnv *env, jclass cls, jobject assetMgrJava, jstring fileName)
{
   if(parsePngTool == NULL){
    AAssetManager *assetManager=AAssetManager_fromJava(env, assetMgrJava);
    const char *name = env->GetStringUTFChars(fileName, 0);
    parsePngTool = new ParsePngFile(assetManager, name);
    env->ReleaseStringUTFChars(fileName, name);
   }

   parsePngTool->parse();

}


JNIEXPORT jint JNICALL Java_android_qiaoyf_com_opengl_1image_PNGInfoHandle_getWidth
        (JNIEnv *env, jclass cls){

    if(parsePngTool != NULL){
        return parsePngTool->getWidth();
    }
    return 0;

}


JNIEXPORT jint JNICALL Java_android_qiaoyf_com_opengl_1image_PNGInfoHandle_getHeight
        (JNIEnv *env, jclass cls){

    if(parsePngTool != NULL){
        return parsePngTool->getHeight();
    }
    return 0;
}


JNIEXPORT jboolean JNICALL Java_android_qiaoyf_com_opengl_1image_PNGInfoHandle_isOpaque
        (JNIEnv *env, jclass cls){
    if(parsePngTool != NULL){
        return parsePngTool->hasAlpha();
    }
    return false;
}


JNIEXPORT void JNICALL Java_android_qiaoyf_com_opengl_1image_PNGInfoHandle_glTexImage2D
(JNIEnv *env, jclass cls, jint target , jint level){

    if(parsePngTool != NULL){
       parsePngTool->glPngTexImage2D(target, level);

     }

}


JNIEXPORT void JNICALL Java_android_qiaoyf_com_opengl_1image_PNGInfoHandle_free
(JNIEnv *env, jclass cls){
    if(parsePngTool != NULL){
        delete parsePngTool;
        parsePngTool = NULL;
    }

}





