package com.bintil.encryption;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@SuppressWarnings("all")
public class AESUtils {

    private static final String key = "@#^$&!*()hnsbtx2020";

    private static Cipher init(int cipherType) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes());
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(cipherType, secretKeySpec);
        return cipher;
    }

    /**
     * 加密
     *
     * @param content 待加密内容
     ******/
    public static String encrypt(String content) throws Exception {
        Cipher cipher = init(Cipher.ENCRYPT_MODE);
        byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
        byte[] byteResult = cipher.doFinal(byteContent);
        StringBuilder sb = new StringBuilder();
        for (byte b : byteResult) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 解密
     *
     * @param content 待解密内容
     */
    public String decrypt(String content) throws Exception {
        if (content.length() < 1) {
            return null;
        }
        byte[] byteResult = new byte[content.length() / 2];
        for (int i = 0; i < content.length() / 2; i++) {
            int high = Integer.parseInt(content.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(content.substring(i * 2 + 1, i * 2 + 2), 16);
            byteResult[i] = (byte) (high * 16 + low);
        }
        Cipher cipher = init(Cipher.DECRYPT_MODE);
        byte[] result = cipher.doFinal(byteResult);
        //不加utf-8，中文时会乱码
        return new String(result, StandardCharsets.UTF_8);
    }
}
