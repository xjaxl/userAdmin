package com.xjaxl.user.common.utils;

import java.security.MessageDigest;


public class TokenGenerator {

    // 生成Token
    public static String generateValue() {
        return generateValue(UUIDUtils.randomUUIDWithoutHyphen());
    }

    private static final char[] hexCode = "0123456789abcdef".toCharArray();

    private static String toHexString(byte[] data) {
        if(data == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(data.length*2);
        for ( byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    private static String generateValue(String param) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes());
            byte[] messageDigest = algorithm.digest();
            return toHexString(messageDigest);
        } catch (Exception e) {
            throw new RuntimeException("生成Token失败");
        }
    }
}
