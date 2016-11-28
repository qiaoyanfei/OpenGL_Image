//
// Created by qiaoyanfei on 16/11/21.
//

#ifndef OPENGL_IMAGE_PARSEPNGFILE_H
#define OPENGL_IMAGE_PARSEPNGFILE_H

#include "common.h"
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>

class ParsePngFile{

public:
    ParsePngFile(AAssetManager *assetManager,const char *filePath);
    bool parse();

    void glPngTexImage2D(int target, int level);
    int getWidth();
    int getHeight();
    bool hasAlpha();

    ~ParsePngFile();

private:
    char * pngData;
    bool _hasAlpha;
    int _width, _height;
    std::string pngDataString;

};

#endif //OPENGL_IMAGE_PARSEPNGFILE_H
