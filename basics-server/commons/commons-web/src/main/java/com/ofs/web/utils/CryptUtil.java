package com.ofs.web.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.zip.CRC32;

/**
 * 加密工具类<br>
 * 使用 PBEWithMD5AndDES 算法
 *
 * @author gaoly
 */
@Slf4j
public class CryptUtil {
    private static final int ITERATION_COUNT = 64;
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String LONG_HEX = "^[0-9a-fA-F]{1,16}$";
    private static final char HIDE_CHAR = '\u25cf';
    private static final String ALGORITHM = "PBEWithMD5AndDES";

    /**
     * 加密
     *
     * @param data
     * @param key
     * @param salt
     * @return
     */
    public static String encryptString(String data, String key, String salt) {
        if (StringUtils.isEmpty(data)) {
            return StringUtils.EMPTY;
        }
        return Base64.getEncoder().encodeToString(encryptByte(data.getBytes(CHARSET), key, salt));
    }

    /**
     * 加密
     *
     * @param data
     * @param key
     * @param salt
     * @return
     */
    public static byte[] encryptByte(byte[] data, String key, String salt) {
        if (data == null || data.length == 0) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key), getPBEParameterSpec(salt));
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException | InvalidKeySpecException | IllegalBlockSizeException
                | BadPaddingException e) {
            log.error(e.toString(), e);
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
    }

    /**
     * 解密
     *
     * @param data
     * @param key
     * @param salt
     * @return
     */
    public static String decryptString(String data, String key, String salt) {
        if (StringUtils.isEmpty(data)) {
            return StringUtils.EMPTY;
        }
        return new String(decryptByte(Base64.getDecoder().decode(data), key, salt), CHARSET);
    }

    /**
     * 解密
     *
     * @param data
     * @param key
     * @param salt
     * @return
     */
    public static byte[] decryptByte(byte[] data, String key, String salt) {
        if (data == null || data.length == 0) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getKey(key), getPBEParameterSpec(salt));
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException | InvalidKeySpecException | IllegalBlockSizeException
                | BadPaddingException e) {
            log.error(e.toString(), e);
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
    }

    /**
     * 解密,并将解密后的字符使用“●”隐藏掉,返回一个长度与原始字符串相同,单所有内容为“●”字符串
     *
     * @param data
     * @param key
     * @param salt
     * @return
     */
    public static String decryptToHideChar(String data, String key, String salt) {
        String pass = decryptString(data, key, salt);
        return StringUtils.leftPad(StringUtils.EMPTY, pass.length(), HIDE_CHAR);
    }

    private static Key getKey(String key) throws InvalidKeySpecException, NoSuchAlgorithmException {
        char[] keyArray = key == null ? new char[0] : key.toCharArray();
        PBEKeySpec pbeKeySpec = new PBEKeySpec(keyArray);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        return factory.generateSecret(pbeKeySpec);
    }

    private static PBEParameterSpec getPBEParameterSpec(String salt) {
        long saltLong = 0;
        if (StringUtils.isEmpty(salt)) {
            saltLong = 0;
        } else if (salt.matches(LONG_HEX)) {
            saltLong = Long.parseUnsignedLong(salt, 16);
        } else {
            CRC32 crc32 = new CRC32();
            crc32.update(salt.getBytes(StandardCharsets.UTF_8));
            saltLong = crc32.getValue();
        }
        ByteBuffer byteBuff = ByteBuffer.allocate(8);
        byteBuff.putLong(saltLong);
        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(byteBuff.array(), ITERATION_COUNT);
        return pbeParameterSpec;
    }
}
