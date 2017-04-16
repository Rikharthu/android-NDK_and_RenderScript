package com.example.uberv.a8_1addingnativebitswithjni;

public class NativeWrapper {
    // In order for this to work, the application runtime must have a way of binding
    // the native method code to the invocations coming from Java. The JNI APIs provide
    // two distinct mechanisms for mapping native methods to Java:
    // a)   Define your native method names by using the default JNI specificaiton of class-
    //      name mangling. In the previous example, the native method name would be
    //      Java_com_example_uberv_a8_1addingnativebitswithjni_NativeWrapper_nativeMethod().
    //      Use javah command-line tool to generate those.
    // b)   Use an explicit method table to map Java methods to native method signatures. In
    //      this case, the method names in your native code can be anything you choose, but

    /*
    cd <project_directory>/src/main
    javah -jni -classpath <project_bin_directory> -d jni NativeWrapper

     */

    // Declare any methods with a C/C++ implementation as native
    public static native void nativeMethod();

    static {
        // Tell the runtime to load your shared library, in this case "libnative_wrapper.so"
        System.loadLibrary("native_wrapper");
    }
}
