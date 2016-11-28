#cflags:= -Wpedantic -std=c11

APP_CPPFLAGS +=-std=c++11

LOCAL_PATH := $(call my-dir)

include libpng/Android.mk

include $(CLEAR_VARS)

LOCAL_MODULE := opengl_image
LOCAL_CFLAGS    := -Werror
LOCAL_LDLIBS	:= -llog -lGLESv2 -landroid

LOCAL_C_INCLUDES := libpng


LOCAL_STATIC_LIBRARIES := libpng

LOCAL_SRC_FILES := PNGInfoHandle.cpp \
                   ParsePngFile.cpp

include $(BUILD_SHARED_LIBRARY)
