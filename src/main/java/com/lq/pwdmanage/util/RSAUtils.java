package com.lq.pwdmanage.util;

import javax.crypto.Cipher;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 非对称加密公钥
     */
    //private static final String PUBLIC_KEY_FILE = "/rsaKey/public.key";

    /**
     * 非对称加密私钥
     */
    //private static final String PRIVATE_KEY_FILE = "/rsaKey/private.key";

    /**
     * 非对称加密公钥
     */
    private static final String PUBLIC_KEY_PAIR = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDVtjK92+hXigZovFixjtAdQhfCR4Vn3nKdHW66t/y/0Afi7UeRTkapE53O6LvAAs6t3szaMbILyR83DGeFPoWOttDXmrJFoaej/ZvESqWU7lnkXlMHPp0ROq9dgdFHZ2DKH7fC8xg4pQ2FzGd3JYZ5yFem4V3PZ5GAxmsOpVFyGwIDAQAB";

    /**
     * 非对称加密私钥
     */
    private static final String PRIVATE_KEY_PAIR = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANW2Mr3b6FeKBmi8WLGO0B1CF8JHhWfecp0dbrq3/L/QB+LtR5FORqkTnc7ou8ACzq3ezNoxsgvJHzcMZ4U+hY620NeaskWhp6P9m8RKpZTuWeReUwc+nRE6r12B0UdnYMoft8LzGDilDYXMZ3clhnnIV6bhXc9nkYDGaw6lUXIbAgMBAAECgYBsEg/EzNDIe8bT7fLYD0UUlpiXKT3AEdBZvUmCaIoKoXNrgNwBEdlPpNjXif9vkLvdVt62tAy6QwFjAAXVbpFwdlZVNwtnqDnlnucKB9+4FDeEcuivXtNXu62813VKaZ68W/mAZI9Eo0Qnkd5ZRgzRFqN5oaLlSGZ2p/G1FToHGQJBAOuGd/RtLBxRGGFEE/k+eslN1b+9G8Ib8lP7Xge8Hg/GIytqh1Guht3UeiNscyCG6eaUt1n/5rgS0s4m4iTOFL8CQQDoSkbT2QsAYt4CoE7VpjpJgpvSQxBf/MtkA+Og1/H9FiEsuRiTcRXYjPdv3Z1pNVzToP3nQW3W4mdBg/54x62lAkBak9hGFEZGjZmGrpvP2wt1p41ws9WdDQpg6eb7qokVH+oepFVuQGR1o2Vlgiu0cq45kzAAWL3uRK71wVZvOSIZAkEArPpa4fkXYzM8hhIggqLldsZHCP2d3Qzf3Nu0j5sffcsT6zzu2yT0Eq1uuSP7y1PMi1+wk5w6kjFImRYU0pZHbQJBAMRCta0pntYnFoWco06ugvn2zF3S0XDaAgWz4an5UyaSS4JXKqQY/isExWbSmuDIot6Nhm0Tv3QnkbvufKFdCq8=";

    /**
     * 生成密钥对(公钥和私钥)
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 获取私钥
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(String data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data.getBytes());
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(String data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data.getBytes());
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    //========================= 加密 =============================

    /**
     * 公钥加密
     *
     * @param data 源数据
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data) throws Exception {
        //读取文件
        //String publicKey = readFileToString(PUBLIC_KEY_FILE);
        return encryptByPublicKey(data, PUBLIC_KEY_PAIR);
    }

    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, String publicKey) throws Exception {
        if (data == null) {
            return "";
        }

        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);

        byte[] bytes = data.getBytes();
        int inputLen = bytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(bytes, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(bytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * 私钥加密
     *
     * @param data 源数据
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data) throws Exception {
        //读取文件
        //String privateKey = readFileToString(PRIVATE_KEY_FILE);
        return encryptByPrivateKey(data, PRIVATE_KEY_PAIR);
    }

    /**
     * 私钥加密
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, String privateKey) throws Exception {
        if (data == null) {
            return "";
        }

        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);

        byte[] bytes = data.getBytes();
        int inputLen = bytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(bytes, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(bytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    //========================= 解密 =============================

    /**
     * 私钥解密
     *
     * @param encryptedData 已加密数据
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String encryptedData) throws Exception {
        //读取文件
        //String privateKey = readFileToString(PRIVATE_KEY_FILE);
        return decryptByPrivateKey(encryptedData, PRIVATE_KEY_PAIR);
    }

    /**
     * 私钥解密
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String encryptedData, String privateKey) throws Exception {
        if (encryptedData == null) {
            return "";
        }

        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);

        byte[] bytes = Base64.getDecoder().decode(encryptedData);
        int inputLen = bytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(bytes, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(bytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData);
    }

    /**
     * 公钥解密
     *
     * @param encryptedData 已加密数据
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String encryptedData) throws Exception {
        //读取文件
        //String publicKey = readFileToString(PUBLIC_KEY_FILE);
        return decryptByPublicKey(encryptedData, PUBLIC_KEY_PAIR);
    }

    /**
     * 公钥解密
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String encryptedData, String publicKey) throws Exception {
        if (encryptedData == null) {
            return "";
        }

        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);

        byte[] bytes = Base64.getDecoder().decode(encryptedData);
        int inputLen = bytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(bytes, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(bytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData);
    }

    /**
     * 读取文件转为字符串
     *
     * @param path
     * @return
     */
    public static String readFileToString(String path) {
        StringBuilder sb = new StringBuilder();

        InputStream in = RSAUtils.class.getResourceAsStream(path);
        BufferedInputStream is = new BufferedInputStream(in);
        try {

            int len = -1;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                sb.append(new String(buff, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString().replaceAll("\r|\n", "").trim();
    }

}
