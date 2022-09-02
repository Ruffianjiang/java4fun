package com.dawn.html2md.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具
 *
 * @author Administrator
 */
public class MD5Util {
    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static MessageDigest messagedigest = null;

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
//             logger.error("MD5FileUtil messagedigest初始化失败", e);
        }
    }

    /**
     * 获取文件的MD5值
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(File file) throws IOException {
        return getFileMD5String(file, 0, -1);
    }

    /**
     * 获取文件的MD5值
     *
     * @param file
     * @param start
     * @param length
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(File file, long start, long length)
            throws IOException {
        FileInputStream fis = new FileInputStream(file);
        FileChannel ch = fis.getChannel();

        long remain = file.length() - start;

        if (length < 0 || length > remain) {
            length = remain;
        }

        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY,
                start, length);

        messagedigest.update(byteBuffer);
        return bufferToHex(messagedigest.digest());
    }

    /**
     * 获取字符串的MD5加密的结果
     *
     * @param s
     * @return
     */
    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

}