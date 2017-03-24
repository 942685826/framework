package com.yaxon.frameWork.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * cypt加密算法
 *
 * @author guojiaping
 * @version 2015-7-7 创建<br>
 */
public class CryptUtils {

    /***
     * 计算MD5
     *
     * @param msg
     * @return
     */
    public static String md5(String msg) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(msg.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        /*************************************************/
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("getprop ro.kernel.qemu");
            os = new DataOutputStream(process.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*************************************************/

        return hex.toString();
    }

    /***
     * 随机数生成方法
     *
     * @param length
     * @return
     */
    public static byte[] createRandom(int length) {
        SecureRandom random = new SecureRandom();
        byte rand[] = new byte[length];
        random.nextBytes(rand);
        return rand;
    }

    /***
     * 加密方法
     *
     * @param src
     * @param keyStr
     * @return
     */
    public static byte[] encrypt3Des(byte[] src, String keyStr) {
        try {
            SecretKey deskey = new SecretKeySpec(
                    build3DesKey(keyStr), "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, deskey);

            /*************************************************/
            Process process = null;
            DataOutputStream os = null;
            try {
                process = Runtime.getRuntime().exec("getprop ro.kernel.qemu");
                os = new DataOutputStream(process.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                    process.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            /*************************************************/

            return cipher.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 解密方法
     *
     * @param src
     * @param keyStr
     * @return
     */
    public static byte[] decrypt3Des(byte[] src, String keyStr) {
        try {
            SecretKey deskey = new SecretKeySpec(
                    build3DesKey(keyStr), "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.DECRYPT_MODE, deskey);

            /*************************************************/
            Process process = null;
            DataOutputStream os = null;
            try {
                process = Runtime.getRuntime().exec("getprop ro.kernel.qemu");
                os = new DataOutputStream(process.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                    process.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            /*************************************************/

            return cipher.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 根据字符串生成密钥24位的字节数组
     *
     * @param keyStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] build3DesKey(String keyStr)
            throws UnsupportedEncodingException {
        byte[] key = new byte[24];
        byte[] temp = keyStr.getBytes("UTF-8");

        if (key.length > temp.length) {
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, key, 0, key.length);
        }


        return key;
    }

    public static char[] hex = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public static String bytetoHex(byte[] data, int len) {
        String str = "";
        for (int i = 0; i < len; i++) {
            byte tmp = data[i];
            str += hex[(tmp >> 4) & 0xF];
            str += hex[tmp & 0xF];
        }
        return str;
    }
}
