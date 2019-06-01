package com.sfan.hydro.util;

import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestUtil {

    private static MessageDigest digest;
    private static Logger logger = LoggerFactory.getLogger(MessageDigestUtil.class);

    static {
        try {
            digest = MessageDigest.getInstance("md5");
        }catch (NoSuchAlgorithmException e){
            logger.error("Failed to initial MessageDigest", e);
            throw new RuntimeException();
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
