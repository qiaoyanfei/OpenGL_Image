include $(CLEAR_VARS)


PNG_SOURCE_PATH := ./libpng
LOCAL_CFLAGS :=

LOCAL_MODULE    := libpng
LOCAL_SRC_FILES :=\
	$(PNG_SOURCE_PATH)/png.c \
	$(PNG_SOURCE_PATH)/pngerror.c \
	$(PNG_SOURCE_PATH)/pngget.c \
	$(PNG_SOURCE_PATH)/pngmem.c \
	$(PNG_SOURCE_PATH)/pngpread.c \
	$(PNG_SOURCE_PATH)/pngread.c \
	$(PNG_SOURCE_PATH)/pngrio.c \
	$(PNG_SOURCE_PATH)/pngrtran.c \
	$(PNG_SOURCE_PATH)/pngrutil.c \
	$(PNG_SOURCE_PATH)/pngset.c \
	$(PNG_SOURCE_PATH)/pngtest.c \
	$(PNG_SOURCE_PATH)/pngtrans.c \
	$(PNG_SOURCE_PATH)/pngwio.c \
	$(PNG_SOURCE_PATH)/pngwrite.c \
	$(PNG_SOURCE_PATH)/pngwtran.c \
	$(PNG_SOURCE_PATH)/pngwutil.c

#LOCAL_SHARED_LIBRARIES := -lz
LOCAL_EXPORT_LDLIBS := -lz
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/libpng/.

#include $(BUILD_SHARED_LIBRARY)
include $(BUILD_STATIC_LIBRARY)
