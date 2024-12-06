package com.feng.insure.protocol.insureserver.controller;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;


/**
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/11/3
 */
@Slf4j
public final class CipherUtil {

    public static final String AES = "AES";
    public static final String AES_ECB_PKCS_5_PADDING = "AES/ECB/PKCS5Padding";
    public static final String MD5 = "MD5";
    public static final String SM4 = "SM4";
    public static final String RSA = "RSA";
    public static final String MD5_WITH_RSA = "MD5withRSA";
    private static final CipherUtil INSTANCE = new CipherUtil();
    private static final String BC = "BC";

    public static CipherUtil getInstance() {
        return INSTANCE;
    }

    public KeyPair generateRSAKey() {
        try {
            final KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
            generator.initialize(1024);
            final KeyPair keyPair = generator.generateKeyPair();
            // PKCS8
            log.info("privateKeyBase64: {}", base64Encode(keyPair.getPrivate().getEncoded()));
            // X.509
            log.info("publicKeyBase64: {}", base64Encode(keyPair.getPublic().getEncoded()));
            return keyPair;
        } catch (Exception e) {
            logUnexpectedException(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * md5 摘要 + RSA 签名验证
     *
     * @param data            待验证数据
     * @param signature       签名
     * @param base64PublicKey 公钥
     * @return true 验证过
     */
    public boolean md5WithRSAVerify(byte[] data, byte[] signature, String base64PublicKey) {
        try {
            final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(base64Decode(base64PublicKey));
            final PublicKey publicKey = keyFactory.generatePublic(keySpec);
            final Signature md5withRSA = Signature.getInstance(MD5_WITH_RSA);
            md5withRSA.initVerify(publicKey);
            md5withRSA.update(data);
            final boolean verify = md5withRSA.verify(signature);
            return verify;
        } catch (Exception e) {
            logUnexpectedException(e);
            throw new IllegalArgumentException();
        }
    }

    /**
     * md5 摘要 + RSA 私钥签名
     *
     * @param data             待签名的数据
     * @param base64PrivateKey 私钥
     * @return 签名
     */
    public byte[] md5WithRSASign(byte[] data, String base64PrivateKey) {
        try {
            final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(base64Decode(base64PrivateKey));
            final PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            final Signature md5withRSA = Signature.getInstance(MD5_WITH_RSA);
            md5withRSA.initSign(privateKey);
            md5withRSA.update(data);
            final byte[] signature = md5withRSA.sign();
            return signature;
        } catch (Exception e) {
            logUnexpectedException(e);
            throw new IllegalArgumentException();
        }
    }

    /**
     * RSA 非对称使用私钥加密
     *
     * @param data             待解密数据
     * @param base64PrivateKey base64PrivateKey
     * @return 解密后的数据
     */
    public byte[] rsaDecrypt(byte[] data, String base64PrivateKey) {
        try {
            final Cipher encryptCipher = Cipher.getInstance(RSA);
            final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(base64Decode(base64PrivateKey));
            final PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            encryptCipher.init(DECRYPT_MODE, privateKey);
            final byte[] decryptBytes = encryptCipher.doFinal(data);
            return decryptBytes;
        } catch (Exception e) {
            logUnexpectedException(e);
            throw new IllegalArgumentException();
        }
    }

    /**
     * RSA 非对称使用公钥加密
     *
     * @param data            待加密数据
     * @param base64PublicKey base64PublicKey
     * @return 加密后的数据
     */
    public byte[] rsaEncrypt(byte[] data, String base64PublicKey) {
        try {
            final Cipher encryptCipher = Cipher.getInstance(RSA);
            final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(base64Decode(base64PublicKey));
            final PublicKey publicKey = keyFactory.generatePublic(keySpec);
            encryptCipher.init(ENCRYPT_MODE, publicKey);
            final byte[] encryptBytes = encryptCipher.doFinal(data);
            return encryptBytes;
        } catch (Exception e) {
            logUnexpectedException(e);
            throw new IllegalArgumentException();
        }
    }

    /**
     * sm4 对称加密
     *
     * @param data 待加密数据
     * @return 加密后的数据
     */
    public byte[] sm4Encrypt(byte[] data, String base64Key) {
        try {
            Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS7Padding", BC);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(base64Decode(base64Key), SM4));
            return cipher.doFinal(data);
        } catch (Exception e) {
            logUnexpectedException(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * sm4 对称解密
     *
     * @param data 待解密数据
     * @return 解密后的数据
     */
    public byte[] sm4Decrypt(byte[] data, String base64Key) {
        try {
            Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS7Padding", BC);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(base64Decode(base64Key), SM4));
            return cipher.doFinal(data);
        } catch (Exception e) {
            logUnexpectedException(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成 sm4 密钥
     */
    public String generateSm4Key() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(SM4, BC);
            kg.init(128);
            final byte[] keyBytes = kg.generateKey().getEncoded();
            return base64Encode(keyBytes);
        } catch (Exception e) {
            logUnexpectedException(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * base64 encode
     *
     * @param data data
     * @return String
     */
    public String base64Encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * base64 decode
     *
     * @param data data
     * @return byte[]
     */
    public byte[] base64Decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    /**
     * 生成 AES key
     *
     * @return base64 格式的密钥
     */
    public String generateAESKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            keyGenerator.init(new SecureRandom());
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] byteKey = secretKey.getEncoded();
            return base64Encode(byteKey);
        } catch (NoSuchAlgorithmException e) {
            // ignore
        }
        return null;
    }

    /**
     * aes 加密
     *
     * @param data      待加密的数据
     * @param base64Key base64格式的key
     * @return base64 格式的字符串
     */
    public String aesEncode(byte[] data, String base64Key) {
        try {
            Key key = new SecretKeySpec(base64Decode(base64Key), AES);
            Cipher cipher = Cipher.getInstance(AES_ECB_PKCS_5_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(data);
            return base64Encode(result);
        } catch (Exception e) {
            logUnexpectedException(e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * aes 解密
     *
     * @param base64Data 待解密的数据
     * @param base64Key  base64格式的key
     * @return base64 格式的字符串
     */
    public byte[] aesDecode(String base64Data, String base64Key) {
        try {
            Key key = new SecretKeySpec(base64Decode(base64Key), AES);
            Cipher cipher = Cipher.getInstance(AES_ECB_PKCS_5_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(base64Decode(base64Data));
            return result;
        } catch (Exception e) {
            logUnexpectedException(e);
            throw new IllegalArgumentException(e);
        }
    }

    private void logUnexpectedException(Exception e) {
        log.error("unExpectedException.", e);
    }

    /**
     * sm2 签名并base64
     *
     * @param data             data
     * @param base64PrivateKey 私钥
     * @return data
     */
    public String sm2SignAndBase64(byte[] data, String base64PrivateKey) {
        return base64Encode(sm2Sign(data, base64PrivateKey));
    }

    public byte[] sm2Sign(byte[] data, String base64PrivateKey) {
        // todo
        return data;
    }

    public boolean sm2Verify(byte[] data, String base64Signature, String base64PublicKey) {
        return false;
    }

}
