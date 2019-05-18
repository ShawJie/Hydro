package com.sfan.hydro.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestUtil {

    private static MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("md5");
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("Failed to initial MessageDigest", e);
        }
    }

    public static String getEncryptionCharset(String originStr){
        return toMD5Charset(originStr);
    }

    private static String toMD5Charset(String originStr){
        digest.update(originStr.getBytes());
        String str = new BigInteger(1, digest.digest()).toString(16);
        digest.reset();
        return str;
    }
}
