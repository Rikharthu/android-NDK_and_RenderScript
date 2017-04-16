cmake_minimum_required(VERSION 3.4.1)

# We want to include the NativeLib.c file in the shared library named features
add_library(
            features
            SHARED
            src/main/jni/NativeLib.c )

include_directories(${ANDROID_NDK}/sources/android/cpufeatures)

# Build a static version of the cpufeatures library
add_library(
            cpufeatures
            STATIC
            ${ANDROID_NDK}/sources/android/cpufeatures/cpu-features.c )

# Locate an NDK library and store its path as a variable
find_library(log-lib log )

# Finally link all these libraries together to form the final .so file
target_link_libraries(features cpufeatures ${log-lib} )

# Compile our single activity.c file into a module named native-activity
# which matches the name we supplied to the manifest earlier
add_library(native-activity SHARED src/main/jni/activity.c )
include_directories(${ANDROID_NDK}/sources/android/native_app_glue )
add_library(native_app_glue
            STATIC
            ${ANDROID_NDK}/sources/android/native_app_glue/android_native_app_glue.c )

find_library(android android )
find_library(egl-lib EGL )
find_library(gles-lib GLESv1_CM )
target_link_libraries(native-activity native_app_glue ${log-lib} ${android} ${egl-lib} ${gles-lib} )