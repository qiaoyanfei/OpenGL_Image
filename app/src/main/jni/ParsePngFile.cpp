//
// Created by qiaoyanfei on 16/11/21.
//

#include "ParsePngFile.h"

#include <GLES2/gl2.h>
#include <png.h>

#include <istream>
#include <fstream>
#include <sstream>
#include <vector>

ParsePngFile::ParsePngFile(AAssetManager *assetManager,const char *filePath) {
    std::vector<char> buffer;
    AAsset * asset = AAssetManager_open(assetManager,filePath, AASSET_MODE_UNKNOWN);
    if (asset) {
        int size = AAsset_getLength(asset);
        buffer.resize(size);
        AAsset_read(asset, &buffer[0], size);

        AAsset_close(asset);

        for (int i = 0; i < buffer.size(); i++) {
            pngDataString += buffer[i];
        }

        std::vector<char>().swap(buffer);

    }


}

void readFileCallback(png_structp png_ptr, png_bytep destination, png_size_t bytesToRead)
{
    png_voidp io_ptr = png_get_io_ptr( png_ptr );

    if( io_ptr == 0 )
    {
        return;
    }

    png_voidp a = png_get_io_ptr(png_ptr);
    //Cast the pointer to std::istream* and read 'bytesToRead' bytes into 'destination'
    ((std::istream*)a)->read((char*)destination, bytesToRead);
}

bool ParsePngFile::parse() {

    std::istringstream pngDataStream(pngDataString);

    const int PNG_SIG_BYTES = 8;
    char pngSignature[PNG_SIG_BYTES];
    pngDataStream.read(pngSignature, PNG_SIG_BYTES * sizeof(char));

    if(!png_check_sig( (png_bytep)pngSignature, PNG_SIG_BYTES) )
    {
        return false;
    }

    /**
     * Create and initialize the png_struct
     * with the desired error handler
     * functions.  If you want to use the
     * default stderr and longjump method,
     * you can supply NULL for the last
     * three parameters.  We also supply the
     * the compiler header file version, so
     * that we know if the application
     * was compiled with a compatible version
     * of the library.  REQUIRED
     */
    png_structp png_ptr = png_create_read_struct(PNG_LIBPNG_VER_STRING, NULL, NULL, NULL);
    if (png_ptr == NULL)
    {
        return false;
    }

    /**
     * Allocate/initialize the memory
     * for image information.  REQUIRED. */
    png_infop info_ptr = png_create_info_struct(png_ptr);
    if (info_ptr == NULL)
    {
        png_destroy_read_struct(&png_ptr, NULL, NULL);
        return false;
    }

    /**
     * Set error handling if you are
     * using the setjmp/longjmp method
     * (this is the normal method of
     * doing things with libpng).
     * REQUIRED unless you  set up
     * your own error handlers in
     * the png_create_read_struct()
     * earlier.
     */
    if (setjmp(png_jmpbuf(png_ptr))) {
        /* Free all of the memory associated
         * with the png_ptr and info_ptr */
        png_destroy_read_struct(&png_ptr, &info_ptr, NULL);
        return false;
    }


    /**
     * Set custom input stream READER / handler
     */
    png_set_read_fn( png_ptr, (void*)&pngDataStream, readFileCallback );

    /* If we have already
     * read some of the signature */
    png_set_sig_bytes( png_ptr, 8 );

    png_read_info( png_ptr, info_ptr);

    int  _depth;
    int color_type, interlace_type;

    png_get_IHDR( png_ptr, info_ptr, (png_uint_32*)&_width, (png_uint_32*)&_height, &_depth, &color_type, &interlace_type, NULL, NULL );

    switch(color_type)
    {
        case PNG_COLOR_TYPE_RGB:
            _hasAlpha = false;
            break;
        case PNG_COLOR_TYPE_RGBA:
            _hasAlpha = true;
            break;
        default:
            return false;
    }

    png_size_t cols = png_get_rowbytes(png_ptr, info_ptr);

    png_bytepp row_pp = new png_bytep[_height];
    pngData= new char[ cols * _height ];

    for( int i = 0; i < _height; ++i )
    {
        // note that png is ordered top to
        // bottom, but OpenGL expect it bottom to top
        // so the order or swapped
        row_pp[i] = (png_bytep)&((char *)pngData)[ i * cols ];
    }

    png_read_image( png_ptr, row_pp );
    png_read_end( png_ptr, info_ptr );

    png_destroy_read_struct( &png_ptr, &info_ptr, 0 );

    delete [] row_pp;

    return true;

}


void ParsePngFile::glPngTexImage2D(int target, int level) {

   if(pngData != NULL){
       glTexImage2D(target, level, _hasAlpha ? GL_RGBA : GL_RGB, _width, _height, 0, _hasAlpha ? GL_RGBA : GL_RGB, GL_UNSIGNED_BYTE, pngData);
   }

}
int ParsePngFile::getWidth() { return _width; }

int ParsePngFile::getHeight() { return _height; }

bool ParsePngFile::hasAlpha() { return _hasAlpha; }

ParsePngFile::~ParsePngFile() {
    if(pngData != NULL){
        delete [] pngData;
        pngData = NULL;
    }

}