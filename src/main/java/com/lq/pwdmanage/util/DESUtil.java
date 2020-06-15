/*
 * Copyright (c) 2012-2022 厦门市美亚柏科信息股份有限公司.
 */
package com.lq.pwdmanage.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * DES加密和解密工具,可以对字符串进行加密和解密操作
 *
 * @since linch, 2016-12-20, 创建文件
 */
public class DESUtil {

    /**
     * 字符串默认键值
     */
    public static String DEFAULT_KEY = "FwRH3gdg7Wry23456ovxx6789gf";

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuilder sb = new StringBuilder(iLen * 2);
        for (byte anArrB : arrB) {
            int intTmp = anArrB;
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     *
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 加密字节数组
     *
     * @param arrB   需加密的字节数组
     * @param keyStr 密钥
     * @return 加密后的字节数组
     * @throws Exception
     */
    public static byte[] encrypt(byte[] arrB, String keyStr) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("DES");
        Key key = getKey(keyStr.getBytes());
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密字符串
     *
     * @param strIn  需加密的字符串
     * @return 加密后的字符串
     */
    public static String encrypt(String strIn) {
        try {
            return byteArr2HexStr(encrypt(strIn.getBytes("utf-8"), DEFAULT_KEY));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 加密字符串
     *
     * @param strIn  需加密的字符串
     * @param keyStr 密钥
     * @return 加密后的字符串
     */
    public static String encrypt(String strIn, String keyStr) {
        try {
            return byteArr2HexStr(encrypt(strIn.getBytes("utf-8"), keyStr));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密字节数组
     *
     * @param arrB   需解密的字节数组
     * @param keyStr 密钥
     * @return 解密后的字节数组
     */
    public static byte[] decrypt(byte[] arrB, String keyStr) {
        try {
            Key key = getKey(keyStr.getBytes());
            Cipher decryptCipher = Cipher.getInstance("DES");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
            return decryptCipher.doFinal(arrB);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密字符串
     * @param strIn 需解密的字符串
     * @param keyStr 密钥
     * @return 解密后的字符串
     */
    public static String decrypt(String strIn, String keyStr) {
        try {
            return new String(decrypt(hexStr2ByteArr(strIn), keyStr), "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 解密字符串
     *
     * @param strIn  需解密的字符串
     * @return 解密后的字符串
     */
    public static String decrypt(String strIn) {
        try {
            return new String(decrypt(hexStr2ByteArr(strIn), DEFAULT_KEY), "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     *
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     * @throws Exception
     */
    private static Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];

        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }

        // 生成密钥
        return new SecretKeySpec(arrB, "DES");
    }

    public static String encryptDES(String encryptString, String encryptKey)
            throws Exception {
        if (encryptString.trim().equals("")) {
            return encryptString;
        }

        byte[] iv = encryptKey.getBytes();
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());

        String str = bytesToHexString(encryptedData);
        return str;
    }

    public static String decryptDES(String decryptString, String decryptKey) {

        byte[] iv = decryptKey.getBytes();
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");

        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte decryptedData[] = cipher.doFinal(hexToBytes(decryptString));
            return new String(decryptedData);
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }

    }

    /**
     * Convert byte[] to hex string.
     *
     * @param src
     *            byte[] data
     *
     * @return hex string
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] hexToBytes(String hexString) {
        char[] hex = hexString.toCharArray();
        // 轉rawData長度減半
        int length = hex.length / 2;
        byte[] rawData = new byte[length];
        for (int i = 0; i < length; i++) {
            // 先將hex資料轉10進位數值
            int high = Character.digit(hex[i * 2], 16);
            int low = Character.digit(hex[i * 2 + 1], 16);
            // 將第一個值的二進位值左平移4位,ex: 00001000 => 10000000 (8=>128)
            // 然後與第二個值的二進位值作聯集ex: 10000000 | 00001100 => 10001100 (137)
            int value = (high << 4) | low;
            // 與FFFFFFFF作補集
            if (value > 127) {
                value -= 256;
            }
            // 最後轉回byte就OK
            rawData[i] = (byte) value;
        }
        return rawData;
    }

    public static void main(String[] args) {
        System.out.println(DESUtil.encrypt("没有车哈哈"));
        System.out.println(DESUtil.decrypt("0708d928b13a0afec6ae484323cabc1e"));
    }

}