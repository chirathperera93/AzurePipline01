package com.ayubo.life.ayubolife.prochat.appointment;

public class Lame {
    private static final String LAME_LIB = "lame";

    public static native int closeEncoder();

    public static native int encode(short[] sArr, short[] sArr2, int i, byte[] bArr, int i2);

    public static native int flushEncoder(byte[] bArr, int i);

    public static native int initEncoder(int i, int i2, int i3, int i4, float f, int i5);

    static {
        System.loadLibrary(LAME_LIB);
    }
}
