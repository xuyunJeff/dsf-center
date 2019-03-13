package com.invech.platform.dsfcenterdata.utils;




import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Slf4j
public class AESUtil {

    private final static int OUT_TIME = 1;

    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码 ,时间
     * @return
     */
    public static String encrypt(String content, String password) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        kgen.init(128, random);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器
        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
        byte[] byteContent = content.getBytes("utf-8");
        byte [] result = cipher.doFinal(byteContent);
        return new BASE64Encoder().encode(result);
    }


    /**
     * AES加密
     * @param
     * @return
     * @throws Exception
     */
    public static String AESEncrypt




    (String value,String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] raw = key.getBytes("UTF-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
        String base64 = new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码
        return URLEncoder.encode(base64, "UTF-8");//URL加密
    }

    /**
     * AES加密 不进行URLEncoder
     * @param
     * @return
     * @throws Exception
     */
    public static String AESUNURLEncrypt(String value,String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] raw = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码
    }


    /**
     * ky AES 解密
     * @param value
     * @param key
     * @param isDecodeURL
     * @return
     * @throws Exception
     */
    public static String kyAESDecrypt(String value,String key,boolean isDecodeURL) throws Exception {
        try {
            byte[] raw = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            if(isDecodeURL)	value = URLDecoder.decode(value, "UTF-8");
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(value);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, StandardCharsets.UTF_8);
            return originalString;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
