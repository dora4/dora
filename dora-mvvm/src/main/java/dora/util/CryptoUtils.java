package dora.util;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Cryptography-related tools. AES encryption is more secure than DES encryption, but it is slower
 * than DES. RSA is used for the highest level of data encryption security.
 * ECB (Electronic Code Book) Mode: Electronic Code Book mode.
 * CBC (Cipher Block Chaining) Mode: Cipher Block Chaining mode.
 * CFB (Cipher Feedback Mode) Mode: Cipher Feedback mode.
 * OFB (Output Feedback) Mode: Output Feedback mode.
 * CTR (Counter) Mode: Counter mode (less common).
 * Among them, ECB, CBC, and CTR are block cipher modes, while CFB and OFB are stream cipher modes.
 * 简体中文：密码学相关工具。AES加密方式比DES加密更安全，但是速度比不上DES。RSA用于最高安全级别的数据加密。
 * ECB(Electronic Code Book) 电子密码本模式
 * CBC(Cipher Block Chaining) 加密块链模式
 * CFB(Cipher FeedBack Mode) 加密反馈模式
 * OFB(Output FeedBack) 输出反馈模式
 * CTR(Counter) 计数器模式（不常见）
 * 其中，ECB、CBC、CTR 为块加密模式，CFB、OFB 为流加密模式。
 */
public final class CryptoUtils {

    public static final String AES = "AES";
    public static final String DES = "DES";
    public static final String RSA = "RSA";
    public static final String AES_ECB_PKCS5 = "AES/ECB/PKCS5Padding";
    public static final String AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";
    public static final String AES_CTR = "AES/CTR/NoPadding";
    public static final String AES_GCM = "AES/GCM/NoPadding";
    public static final String AES_CFB_PKCS5 = "AES/CFB/PKCS5Padding";
    public static final String AES_OFB_PKCS5 = "AES/OFB/PKCS5Padding";
    public static final String AES_XTS = "AES/XTS/NoPadding";
    public static final String DES_ECB_PKCS5 = "DES/ECB/PKCS5Padding";
    public static final String DES_CBC_PKCS5 = "DES/CBC/PKCS5Padding";
    public static final String DES_CTR = "DES/CTR/NoPadding";
    public static final String DES_GCM = "DES/GCM/NoPadding";
    public static final String DES_CFB_PKCS5 = "DES/CFB/PKCS5Padding";
    public static final String DES_OFB_PKCS5 = "DES/OFB/PKCS5Padding";
    public static final String DES_XTS = "DES/XTS/NoPadding";

    private CryptoUtils() {
    }

    // <editor-folder desc="Base64 Encoding and Decoding">

    /**
     * Decode the Base64 string into a byte array.
     * 简体中文：将Base64字符串解码成字节数组。
     */
    public static byte[] base64Decode(String data) {
        return Base64.decode(data, Base64.NO_WRAP);
    }

    /**
     * Convert the byte array to Base64 encoding.
     * 简体中文：将字节数组转换成Base64编码。
     */
    public static String base64Encode(byte[] data) {
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

    // </editor-folder>

    /**
     * Acquiring a key using a password.
     * 简体中文：使用密码获取密钥。
     *
     * @param secretKey Key string
     * @param algorithm "AES" or "DES"
     */
    public static SecretKeySpec getSecretKey(String secretKey, String algorithm) {
        secretKey = makeKey(secretKey, 32, "0");
        return new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), algorithm);
    }

    /**
     * If the AES key is shorter than the length of {@code length}, padding will be applied to the
     * key to ensure key security.
     * 简体中文：如果AES的密钥小于 {@code length} 的长度，就对秘钥进行补位，保证秘钥安全。
     *
     * @param secretKey The secret cryptographic key used in encryption and decryption algorithms
     * @param length    The required length of the key
     * @param text      Default padding text
     * @return secret key
     */
    private static String makeKey(String secretKey, int length, String text) {
        int strLen = secretKey.length();
        if (strLen < length) {
            StringBuilder builder = new StringBuilder();
            builder.append(secretKey);
            for (int i = 0; i < length - strLen; i++) {
                builder.append(text);
            }
            secretKey = builder.toString();
        }
        return secretKey;
    }

    // <editor-folder desc="AES加解密">

    /**
     * AES加密。
     *
     * @param secretKey 密码
     * @param transformation 加密模式
     * @param iv 偏移量
     * @param data 要加密的数据
     */
    public static String encryptAES(String secretKey, String transformation, IvParameterSpec iv, String data) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(transformation);
            // 初始化为加密密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(secretKey, AES), iv);
            byte[] encryptByte = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            // 将加密以后的数据进行 Base64 编码
            return base64Encode(encryptByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES加密，使用默认的方式。
     *
     * @param secretKey 密码
     * @param data 要加密的数据
     */
    public static String encryptAES(String secretKey, String data) {
        // 不设置偏移量
        IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
        return encryptAES(secretKey, AES_ECB_PKCS5, zeroIv, data);
    }

    /**
     * AES解密。
     *
     * @param secretKey 密码
     * @param transformation 加密模式
     * @param iv 偏移量
     * @param base64Data 要解密的base64数据
     */
    public static String decryptAES(String secretKey, String transformation, IvParameterSpec iv, String base64Data) {
        try {
            byte[] data = base64Decode(base64Data);
            Cipher cipher = Cipher.getInstance(transformation);
            // 设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(secretKey, AES), iv);
            // 执行解密操作
            byte[] result = cipher.doFinal(data);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密，使用默认的方式。
     *
     * @param secretKey 密码
     * @param base64Data 要解密的base64数据
     */
    public static String decryptAES(String secretKey, String base64Data) {
        // 不设置偏移量
        IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
        return decryptAES(secretKey, AES_ECB_PKCS5, zeroIv, base64Data);
    }

    /**
     * 对文件进行AES加密。
     *
     * @param srcFile 待加密文件
     * @param dir        加密后的文件存储路径
     * @param dstName 加密后的文件名称
     * @param secretKey  密钥
     * @param transformation 加密模式
     * @return 加密后的文件
     */
    public static File encryptFileAES(File srcFile, String dir, String dstName, String secretKey, String transformation) {
        try {
            // 创建加密后的文件
            File encryptFile = new File(dir, dstName);
            // 根据文件创建输出流
            FileOutputStream outputStream = new FileOutputStream(encryptFile);
            // 初始化 Cipher
            Cipher cipher = initFileAESCipher(secretKey, transformation, Cipher.ENCRYPT_MODE);
            // 以加密流写入文件
            CipherInputStream cipherInputStream = new CipherInputStream(
                    new FileInputStream(srcFile), cipher);
            // 创建缓存字节数组
            byte[] buffer = new byte[1024 * 2];
            // 读取
            int len;
            // 读取加密并写入文件
            while ((len = cipherInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
            // 关闭加密输入流
            cipherInputStream.close();
            IoUtils.close(outputStream);
            return encryptFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES加密文件，默认方式。
     *
     * @param srcFile 待加密文件
     * @param dir        加密后的文件存储路径
     * @param dstName 加密后的文件名称
     * @param secretKey  密钥
     */
    public static File encryptFileAES(File srcFile, String dir, String dstName, String secretKey) {
        return encryptFileAES(srcFile, dir, dstName, secretKey, AES_CFB_PKCS5);
    }

    /**
     * AES解密文件。
     *
     * @param srcFile 源加密文件
     * @param dir        解密后的文件存储路径
     * @param dstName 解密后的文件名称
     * @param secretKey  密钥
     * @param transformation 加密模式
     */
    public static File decryptFileAES(File srcFile, String dir, String dstName, String secretKey, String transformation) {
        try {
            // 创建解密文件
            File decryptFile = new File(dir, dstName);
            // 初始化Cipher
            Cipher cipher = initFileAESCipher(secretKey, transformation, Cipher.DECRYPT_MODE);
            // 根据源文件创建输入流
            FileInputStream inputStream = new FileInputStream(srcFile);
            // 获取解密输出流
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    new FileOutputStream(decryptFile), cipher);
            // 创建缓冲字节数组
            byte[] buffer = new byte[1024 * 2];
            int len;
            // 读取解密并写入
            while ((len = inputStream.read(buffer)) >= 0) {
                cipherOutputStream.write(buffer, 0, len);
                cipherOutputStream.flush();
            }
            // 关闭流
            IoUtils.close(cipherOutputStream, inputStream);
            return decryptFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密文件，默认方式。
     *
     * @param srcFile 源加密文件
     * @param dir        解密后的文件存储路径
     * @param dstName 解密后的文件名称
     * @param secretKey  密钥
     */
    public static File decryptFileAES(File srcFile, String dir, String dstName, String secretKey) {
        return decryptFileAES(srcFile, dir, dstName, secretKey, AES_CFB_PKCS5);
    }

    /**
     * 初始化AES Cipher。
     *
     * @param secretKey  密钥
     * @param transformation 加密模式
     * @param cipherMode 加密模式
     * @return 密钥
     */
    private static Cipher initFileAESCipher(String secretKey, String transformation, int cipherMode) {
        try {
            // 创建密钥规格
            SecretKeySpec secretKeySpec = getSecretKey(secretKey, AES);
            // 获取密钥
            Cipher cipher = Cipher.getInstance(transformation);
            // 初始化
            cipher.init(cipherMode, secretKeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            return cipher;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // </editor-folder>

    // <editor-folder desc="DES加解密">

    /**
     * DES加密。
     *
     * @param secretKey 密码
     * @param transformation 加密模式
     * @param iv 偏移量
     * @param data 要加密的数据
     */
    public static String encryptDES(String secretKey, String transformation, IvParameterSpec iv, String data) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(transformation);
            // 初始化为加密密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(secretKey, DES), iv);
            byte[] encryptByte = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            // 将加密以后的数据进行 Base64 编码
            return base64Encode(encryptByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DES加密，使用默认的方式。
     *
     * @param secretKey 密码
     * @param data 要加密的数据
     */
    public static String encryptDES(String secretKey, String data) {
        // 不设置偏移量
        IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
        return encryptAES(secretKey, DES_ECB_PKCS5, zeroIv, data);
    }

    /**
     * DES解密。
     *
     * @param secretKey 密码
     * @param transformation 加密模式
     * @param iv 偏移量
     * @param base64Data 要解密的base64数据
     */
    public static String decryptDES(String secretKey, String transformation, IvParameterSpec iv, String base64Data) {
        try {
            byte[] data = base64Decode(base64Data);
            Cipher cipher = Cipher.getInstance(transformation);
            // 设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(secretKey, DES), iv);
            // 执行解密操作
            byte[] result = cipher.doFinal(data);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DES解密，使用默认的方式。
     *
     * @param secretKey 密码
     * @param base64Data 要解密的base64数据
     */
    public static String decryptDES(String secretKey, String base64Data) {
        // 不设置偏移量
        IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
        return decryptAES(secretKey, DES_ECB_PKCS5, zeroIv, base64Data);
    }

    /**
     * 对文件进行DES加密。
     *
     * @param srcFile 待加密文件
     * @param dir        加密后的文件存储路径
     * @param dstName 加密后的文件名称
     * @param secretKey  密钥
     * @param transformation 加密模式
     * @return 加密后的文件
     */
    public static File encryptFileDES(File srcFile, String dir, String dstName, String secretKey, String transformation) {
        try {
            // 创建加密后的文件
            File encryptFile = new File(dir, dstName);
            // 根据文件创建输出流
            FileOutputStream outputStream = new FileOutputStream(encryptFile);
            // 初始化 Cipher
            Cipher cipher = initFileDESCipher(secretKey, transformation, Cipher.ENCRYPT_MODE);
            // 以加密流写入文件
            CipherInputStream cipherInputStream = new CipherInputStream(
                    new FileInputStream(srcFile), cipher);
            // 创建缓存字节数组
            byte[] buffer = new byte[1024 * 2];
            // 读取
            int len;
            // 读取加密并写入文件
            while ((len = cipherInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
            // 关闭加密输入流
            cipherInputStream.close();
            IoUtils.close(outputStream);
            return encryptFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DES加密文件，默认方式。
     *
     * @param srcFile 待加密文件
     * @param dir        加密后的文件存储路径
     * @param dstName 加密后的文件名称
     * @param secretKey  密钥
     */
    public static File encryptFileDES(File srcFile, String dir, String dstName, String secretKey) {
        return encryptFileDES(srcFile, dir, dstName, secretKey, DES_CFB_PKCS5);
    }

    /**
     * DES解密文件。
     *
     * @param srcFile 源加密文件
     * @param dir        解密后的文件存储路径
     * @param dstName 解密后的文件名称
     * @param secretKey  密钥
     * @param transformation 加密模式
     */
    public static File decryptFileDES(File srcFile, String dir, String dstName, String secretKey, String transformation) {
        try {
            // 创建解密文件
            File decryptFile = new File(dir, dstName);
            // 初始化Cipher
            Cipher cipher = initFileDESCipher(secretKey, transformation, Cipher.DECRYPT_MODE);
            // 根据源文件创建输入流
            FileInputStream inputStream = new FileInputStream(srcFile);
            // 获取解密输出流
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    new FileOutputStream(decryptFile), cipher);
            // 创建缓冲字节数组
            byte[] buffer = new byte[1024 * 2];
            int len;
            // 读取解密并写入
            while ((len = inputStream.read(buffer)) >= 0) {
                cipherOutputStream.write(buffer, 0, len);
                cipherOutputStream.flush();
            }
            // 关闭流
            IoUtils.close(cipherOutputStream, inputStream);
            return decryptFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DES解密文件，默认方式。
     *
     * @param srcFile 源加密文件
     * @param dir        解密后的文件存储路径
     * @param dstName 解密后的文件名称
     * @param secretKey  密钥
     */
    public static File decryptFileDES(File srcFile, String dir, String dstName, String secretKey) {
        return decryptFileDES(srcFile, dir, dstName, secretKey, DES_CFB_PKCS5);
    }

    /**
     * 初始化DES Cipher。
     *
     * @param secretKey  密钥
     * @param transformation 加密模式
     * @param cipherMode 加密模式
     * @return 密钥
     */
    private static Cipher initFileDESCipher(String secretKey, String transformation, int cipherMode) {
        try {
            // 创建密钥规格
            SecretKeySpec secretKeySpec = getSecretKey(secretKey, DES);
            // 获取密钥
            Cipher cipher = Cipher.getInstance(transformation);
            // 初始化
            cipher.init(cipherMode, secretKeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            return cipher;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // </editor-folder>

    // <editor-folder desc="RSA encryption and decryption">

    /**
     * Generate a pair of RSA public and private keys.
     * 简体中文：生成一对RSA公钥和私钥。
     *
     * @param keySize The length of the key, such as 1024
     */
    public static Map<String, String> generateRSAKeyPair(int keySize) {
        // Create a KeyPairGenerator object for the RSA algorithm.
        // 简体中文：为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[RSA]");
        }
        // Initialize the KeyPairGenerator object with the key length.
        // 简体中文：初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        // Translation: Generate a key pair.
        // 简体中文：生成密钥对
        KeyPair keyPair = kpg.generateKeyPair();
        // Get the public key.
        // 简体中文：得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = base64Encode(publicKey.getEncoded());
        // Get the private key.
        // 简体中文：得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = base64Encode(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);
        return keyPairMap;
    }

    /**
     * Encrypt using the public key.
     * 简体中文：使用公钥加密。
     *
     * @param rsaPublic RSA public key string
     * @param content The content to be encrypted
     */
    public static String encryptByPublic(String rsaPublic, String content) {
        try {
            RSAPublicKey publicKey = getPublicKey(rsaPublic);
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, content.getBytes(StandardCharsets.UTF_8),
                    publicKey.getModulus().bitLength()), Base64.NO_WRAP);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Decrypt using the private key.
     * 简体中文：使用公钥加密。
     *
     * @param rsaPrivate RSA private key string
     * @param content The content to be decrypted
     */
    public static String decryptByPrivate(String rsaPrivate, String content) {
        try {
            RSAPrivateKey privateKey = getPrivateKey(rsaPrivate);
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, base64Decode(content), privateKey.getModulus().bitLength()),
                    StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encrypt using the private key.
     * 简体中文：使用私钥加密。
     *
     * @param rsaPrivate RSA private key string
     * @param content The content to be decrypted
     */
    public static String encryptByPrivate(String rsaPrivate, String content) {
        try {
            RSAPrivateKey privateKey = getPrivateKey(rsaPrivate);
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, content.getBytes(StandardCharsets.UTF_8), privateKey.getModulus().bitLength()),
                    Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Decrypt using the public key.
     * 简体中文：使用公钥解密。
     *
     * @param rsaPublic RSA public key string
     * @param content The content to be encrypted
     */
    public static String decryptByPublic(String rsaPublic, String content) {
        try {
            RSAPublicKey publicKey = getPublicKey(rsaPublic);
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, base64Decode(content),
                    publicKey.getModulus().bitLength()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Obtain the RSA public key from the public key string.
     * 简体中文：通过公钥字符串获取RSA公钥。
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Obtain the public key object from the Key instruction encoded in X509 format.
        // 简体中文：通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(base64Decode(publicKey));
        return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
    }

    /**
     * Obtain the RSA private key from the private key string.
     * 简体中文：通过私钥字符串获取RSA私钥。
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Obtain a private key object through a Key instruction encoded in PKCS#8 format; PKCS#1
        // is not currently considered in Android.
        // 简体中文：通过PKCS#8编码的Key指令获得私钥对象，安卓中暂不考虑PKCS#1
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(base64Decode(privateKey));
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }

    /**
     * Used for splitting large data blocks.
     * 简体中文：用于拆分大的数据块。
     */
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
            throw new RuntimeException("An exception occurred while encrypting or decrypting data" +
                    " with a threshold of [" + maxBlock + "].", e);
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
