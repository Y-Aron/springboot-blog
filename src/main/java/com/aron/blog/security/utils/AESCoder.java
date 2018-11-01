package com.aron.blog.security.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author: Y-Aron
 * @create: 2018-10-29 23:08
 **/
@Slf4j
public class AESCoder {

    private static final String ALGORITHMS = "AES/ECB/PKCS5Padding";
    private static final int BYTE_SIZE = 128;
    private static final String KEY_ALGORITHM = "AES";

    /**
     * AES加密为base 64 code
     * @param content 待加密的内容
     * @param key 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String encrypt(String content, String key) {
        try {
            return base64Encode(aesEncryptToBytes(content, key));
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 将base 64 code AES解密
     * @param encryptStr 待解密的base 64 code
     * @param key 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String decrypt(String encryptStr, String key){
        try {
            return StringUtils.isEmpty(encryptStr) ? null :
                    aesDecryptByBytes(base64Decode(encryptStr), key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
        kgen.init(BYTE_SIZE);
        Cipher cipher = Cipher.getInstance(ALGORITHMS);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), KEY_ALGORITHM));
        return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }

    private static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
        kgen.init(BYTE_SIZE);
        Cipher cipher = Cipher.getInstance(ALGORITHMS);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), KEY_ALGORITHM));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    private static byte[] base64Decode(String base64Code) throws Exception {
        return StringUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
    }
}
