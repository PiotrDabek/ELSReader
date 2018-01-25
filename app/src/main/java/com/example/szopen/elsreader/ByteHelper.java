package com.example.szopen.elsreader;

import android.support.v4.content.res.TypedArrayUtils;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ByteHelper {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String byteArrayToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String intToHexString(int hex) {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(hex));

        while (sb.length() < 4) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }


}
