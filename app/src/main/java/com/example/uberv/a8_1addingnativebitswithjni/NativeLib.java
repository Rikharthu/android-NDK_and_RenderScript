package com.example.uberv.a8_1addingnativebitswithjni;

public class NativeLib {

    /**
     * Return the number of available cores on the device
     */
    public static native int getCpuCount();

    public static native String getCpuFamily();

    static {
        // libfeatures.so
        System.loadLibrary("features");
    }
}
