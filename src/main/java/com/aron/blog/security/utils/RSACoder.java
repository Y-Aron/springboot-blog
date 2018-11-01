package com.aron.blog.security.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.MapUtils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Y-Aron
 * @create: 2018-10-30 00:10
 **/
public class RSACoder {

    private static final String KEY_ALGORITHM = "RSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 初始化密钥
     * @return
     * @throws Exception
     */
    public static Map<String, String> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return  new HashMap<String, String>(2){{
            put(PUBLIC_KEY, encryptBASE64(keyPair.getPublic().getEncoded()));   // 公钥
            put(PRIVATE_KEY, encryptBASE64(keyPair.getPrivate().getEncoded())); // 私钥
        }};
    }

    /**
     * 取得私钥
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, String> keyMap){
        if (MapUtils.isEmpty(keyMap)) {
            return null;
        }
        return keyMap.get(PRIVATE_KEY);
    }

    /**
     * 取得公钥
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, String> keyMap){
        if (MapUtils.isEmpty(keyMap)) {
            return null;
        }
        return keyMap.get(PUBLIC_KEY);
    }


    /**
     * 用私钥对信息生成数字签名
     * @param data    加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = decryptBASE64(privateKey);
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     * @param data   加密数据
     * @param publicKey 公钥
     * @param sign   数字签名
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) {
        try {
            // 解密由base64编码的公钥
            byte[] keyBytes = decryptBASE64(publicKey);
            // 构造X509EncodedKeySpec对象
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            // KEY_ALGORITHM 指定的加密算法
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            // 取公钥匙对象
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(pubKey);
            signature.update(data);
            // 验证签名是否正常
            return signature.verify(decryptBASE64(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String data, String publicKey){
        try {
            // 对公钥解密
            byte[] keyBytes = Base64.decodeBase64(publicKey);
            // 取得公钥
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key key = keyFactory.generatePublic(x509KeySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥解密
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String privateKey){
        try {
            return new String(decrypt(decryptBASE64(data), privateKey));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] decrypt(byte[] data, String privateKey) throws Exception{
        // 对密钥解密
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key key = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    private static String encryptBASE64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    private static byte[] decryptBASE64(String key) {
        return Base64.decodeBase64(key);
    }
}
