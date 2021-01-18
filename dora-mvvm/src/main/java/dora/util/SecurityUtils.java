package dora.util;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    // <editor-folder desc="DES加密">

    public static String encryptDES(byte[] key, String encryptString) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
        return Math.bs2H(encryptedData);
    }

    public static String decryptDES(byte[] key, String decryptString) throws Exception {
        if (TextUtils.isNotEmpty(decryptString)) {
            byte[] byteMi = Math.H2bs(decryptString, "");
            IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, zeroIv);
            byte decryptedData[] = cipher.doFinal(byteMi);
            return new String(decryptedData);
        }
        return "";
    }

    // </editor-folder>

    // <editor-folder desc="RSA加密">

    /**
     *  生成RSA公钥和私钥。
     *
     * @param keySize 如1024
     * @return
     */
    public static Map<String, String> generateRSAKeyPair(int keySize) {
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[RSA]");
        }
        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeToString(publicKey.getEncoded(), Base64.NO_WRAP);
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeToString(privateKey.getEncoded(), Base64.NO_WRAP);
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);
        return keyPairMap;
    }

    /**
     * 使用公钥加密。
     *
     * @param rsa_public
     * @param content
     * @return
     */
    public static String encryptByPublic(String rsa_public, String content) {
        try {
            RSAPublicKey publicKey = getPublicKey(rsa_public);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, content.getBytes("UTF-8"),
                    publicKey.getModulus().bitLength()), Base64.NO_WRAP);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 使用私钥解密。
     *
     * @param rsa_private
     * @param content
     * @return
     */
    public static String decryptByPrimary(String rsa_private, String content) {
        try {
            RSAPrivateKey privateKey = getPrivateKey(rsa_private);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decode(content, Base64.NO_WRAP), privateKey.getModulus().bitLength()),
                    "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 使用私钥加密。
     *
     * @param rsa_private
     * @param content
     * @return
     */
    public static String encryptByPrimary(String rsa_private, String content) {
        try {
            RSAPrivateKey privateKey = getPrivateKey(rsa_private);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, content.getBytes("UTF-8"), privateKey.getModulus().bitLength()),
                    Base64.NO_WRAP);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 使用公钥解密。
     *
     * @param rsa_public
     * @param content
     * @return
     */
    public static String decryptByPublic(String rsa_public, String content) {
        try {
            RSAPublicKey publicKey = getPublicKey(rsa_public);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decode(content, Base64.NO_WRAP),
                    publicKey.getModulus().bitLength()), "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }

    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decode(publicKey, Base64.NO_WRAP));
        return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
    }

    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey, Base64.NO_WRAP));
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultDatas;
    }

    // </editor-folder>
}
