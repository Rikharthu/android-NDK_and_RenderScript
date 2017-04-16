//#include "NativeLib.h"
#include <jni.h>
#include <android/log.h>
#include <cpu-features.h>

static jint native_getCpuCount(JNIEnv *env, jclass clazz) {
    return android_getCpuCount();
}

static jstring native_getCpuFamily(JNIEnv *env, jclass clazz) {
    AndroidCpuFamily family = android_getCpuFamily();
    switch (family) {
        case ANDROID_CPU_FAMILY_ARM:
            // allocate a new Java string object (jstring)
            return (*env)->NewStringUTF(env, "ARM (32-bit)");
        case ANDROID_CPU_FAMILY_X86:
            return (*env)->NewStringUTF(env, "x86 (32-bit)");
        case ANDROID_CPU_FAMILY_MIPS:
            return (*env)->NewStringUTF(env, "MIPS (32-bit)");
        case ANDROID_CPU_FAMILY_ARM64:
            return (*env)->NewStringUTF(env, "ARM (64-bit)");
        case ANDROID_CPU_FAMILY_X86_64:
            return (*env)->NewStringUTF(env, "x86 (64-bit)");
        case ANDROID_CPU_FAMILY_MIPS64:
            return (*env)->NewStringUTF(env, "MIPS (64-bit)");
        case ANDROID_CPU_FAMILY_UNKNOWN:
        default:
            return (*env)->NewStringUTF(env, "Vaporware");
    }
}

// Construct a table mapping Java method signatures to native function pointers
static JNINativeMethod method_table[] = {
        // 1 -  method name of the method on the Java class as a string
        // 2 -  method signature of the method on the Java class (a string that
        //      defines the parameters and return types the Java method takes
        //      to uniquely identify an overloaded method version
        //      "()I" - no args method, returns integer, "(II)" void that takes 2 ints as parameters
        // 3 -  a function pointer to the native function we created to handle the invocation
        {"getCpuCount",  "()I",                  (void *) native_getCpuCount},
        {"getCpuFamily", "()Ljava/lang/String;", (void *) native_getCpuFamily}
};

// Use the OnLoad initializer to register the method table with the runtime
// Gets executed when the library is loaded
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    // get reference to the JNI environment
    if ((*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    } else {
        // find a reference to the Java class we created (using reflection)
        // and attach a method table to iut using RegisterNatives
        jclass clazz = (*env)->FindClass(env, "com/example/uberv/a8_1addingnativebitswithjni/NativeLib");
        if (clazz) {
            jint ret = (*env)->RegisterNatives(env, clazz, method_table,
                                               sizeof(method_table) / sizeof(method_table[0]));
            if (ret == 0) {
                return JNI_VERSION_1_6;
            }
        }
        return JNI_ERR;
    }
}


/*
JNIEXPORT jint JNICALL Java_com_example_uberv_a8_11addingnativebitswithjni_NativeLib_getCpuCount
(JNIEnv *env, jclass clazz)
{
    return android_getCpuCount();
}
JNIEXPORT jstring JNICALL Java_com_example_uberv_a8_11addingnativebitswithjni_NativeLib_getCpuFamily
(JNIEnv *env, jclass clazz)
{
    AndroidCpuFamily family = android_getCpuFamily();
    switch (family)
    {
        case ANDROID_CPU_FAMILY_ARM:
            // allocate a new Java string object (jstring)
            return (*env)->NewStringUTF(env, "ARM (32-bit)");
        case ANDROID_CPU_FAMILY_X86:
            return (*env)->NewStringUTF(env, "x86 (32-bit)");
        case ANDROID_CPU_FAMILY_MIPS:
            return (*env)->NewStringUTF(env, "MIPS (32-bit)");
        case ANDROID_CPU_FAMILY_ARM64:
            return (*env)->NewStringUTF(env, "ARM (64-bit)");
        case ANDROID_CPU_FAMILY_X86_64:
            return (*env)->NewStringUTF(env, "x86 (64-bit)");
        case ANDROID_CPU_FAMILY_MIPS64:
            return (*env)->NewStringUTF(env, "MIPS (64-bit)");
        case ANDROID_CPU_FAMILY_UNKNOWN:
        default:
            return (*env)->NewStringUTF(env, "Vaporware");
    }
}
 */