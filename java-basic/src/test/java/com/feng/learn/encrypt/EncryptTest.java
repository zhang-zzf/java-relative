package com.feng.learn.encrypt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;
import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author zhanfeng.zhang
 * @date 2020/05/19
 */
@Slf4j
public class EncryptTest {

    @Test
    void givenFile_readAllBytes_then() throws IOException {
        final File file = new File("/Volumes/RamDisk/test/test.txt");
        Files.write(file.toPath(), "H".getBytes(UTF_8));
        final byte[] bytes = Files.readAllBytes(file.toPath());
        then(bytes).hasSize(1);
        // 读行
        final List<String> strings = Files.readAllLines(file.toPath());
        then(strings).contains("H");
        final byte[] bytes1 = strings.get(0).getBytes(UTF_8);
        then(bytes1).hasSize(1);
    }

    /**
     *
     */
    @Test
    void generateRSAKeyPair() throws NoSuchAlgorithmException, IOException {
        final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        final KeyPair keyPair = generator.generateKeyPair();
        final PrivateKey aPrivate = keyPair.getPrivate();
        final PublicKey aPublic = keyPair.getPublic();
        // PKCS8#8
        final String privateBase64 = Base64.getEncoder().encodeToString(aPrivate.getEncoded());
        log.info("privateKey: {}", privateBase64);
        Files.write(new File("/Volumes/RamDisk/test/java_rsa_2048_pkcs8.der").toPath(), aPrivate.getEncoded());
        // X.509
        final String publicBase64 = Base64.getEncoder().encodeToString(aPublic.getEncoded());
        log.info("publicKey: {}", publicBase64);
    }

    public byte[] encryptWithPrivate(byte[] bytes, String privateKeyBase64) {
        try {
            final Cipher encryptCipher = Cipher.getInstance("RSA");
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            final byte[] content = Base64.getDecoder().decode(privateKeyBase64);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(content);
            final PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            encryptCipher.init(ENCRYPT_MODE, privateKey);
            final byte[] decryptBytes = encryptCipher.doFinal(bytes);
            return decryptBytes;
        } catch (Exception e) {
            log.info("unExpectedException", e);
            throw new IllegalArgumentException();
        }
    }

    public byte[] decryptWithPublic(byte[] bytes, String publicKeyBase64) {
        try {
            final Cipher encryptCipher = Cipher.getInstance("RSA");
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            final byte[] content = Base64.getDecoder().decode(publicKeyBase64);
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(content);
            final PublicKey publicKey = keyFactory.generatePublic(keySpec);
            encryptCipher.init(DECRYPT_MODE, publicKey);
            final byte[] encryptBytes = encryptCipher.doFinal(bytes);
            return encryptBytes;
        } catch (Exception e) {
            log.info("unExpectedException", e);
            throw new IllegalArgumentException();
        }
    }


    public byte[] encrypt(byte[] bytes, String publicKeyBase64) {
        try {
            final Cipher encryptCipher = Cipher.getInstance("RSA");
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            final byte[] content = Base64.getDecoder().decode(publicKeyBase64);
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(content);
            final PublicKey publicKey = keyFactory.generatePublic(keySpec);
            encryptCipher.init(ENCRYPT_MODE, publicKey);
            final byte[] encryptBytes = encryptCipher.doFinal(bytes);
            return encryptBytes;
        } catch (Exception e) {
            log.info("unExpectedException", e);
            throw new IllegalArgumentException();
        }
    }

    public byte[] decrypt(byte[] bytes, String privateKeBase64) {
        try {
            final Cipher encryptCipher = Cipher.getInstance("RSA");
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            final byte[] content = Base64.getDecoder().decode(privateKeBase64);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(content);
            final PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            encryptCipher.init(DECRYPT_MODE, privateKey);
            final byte[] decryptBytes = encryptCipher.doFinal(bytes);
            return decryptBytes;
        } catch (Exception e) {
            log.info("unExpectedException", e);
            throw new IllegalArgumentException();
        }
    }

    public String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public byte[] base64Decode(String base64Str) {
        return Base64.getDecoder().decode(base64Str);
    }

    @Test
    void givenKeyPair_whenSign_then() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException, SignatureException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        final byte[] bytes = IOUtils.toByteArray(ClassLoader.getSystemResourceAsStream("security/some.txt"));
        log.info("origin: {}\nhex: {}", new String(bytes, UTF_8), Hex.encodeHexString(bytes));
        final Signature md5withRSA = Signature.getInstance("MD5withRSA");
        final PrivateKey privateKey = getPrivateKeyFrom("security/java_rsa_2048_pkcs8.der");
        md5withRSA.initSign(privateKey);
        md5withRSA.update(bytes);
        final byte[] sign = md5withRSA.sign();
        Files.write(new File("/tmp/some.txt.md5.rsa.sign").toPath(), sign);
        log.info("sign base64:{}\nhex: {}", base64Encode(sign), Hex.encodeHexString(sign));
    }

    @Test
    void givenKeyPair_whenVerify_then() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException,
        InvalidKeyException, SignatureException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        final byte[] bytes = IOUtils.toByteArray(ClassLoader.getSystemResourceAsStream("security/some.txt"));
        log.info("origin: {}\nhex: {}", new String(bytes, UTF_8), Hex.encodeHexString(bytes));
        final byte[] signature = Files.readAllBytes(new File("/tmp/some.txt.md5.rsa.sign").toPath());
        final PublicKey publicKey = getPublicKeyFrom("security/java_rsa_2048_pkcs8_pub.der");
        final Signature md5withRSA = Signature.getInstance("MD5withRSA");
        md5withRSA.initVerify(publicKey);
        md5withRSA.update(bytes);
        final boolean verify = md5withRSA.verify(signature);
        log.info("verify: {}", verify);
    }


    private byte[] encrypt(byte[] bytes, Key key) throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        final Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(ENCRYPT_MODE, key);
        final byte[] encryptData = encryptCipher.doFinal(bytes);
        return encryptData;
    }


    public PublicKey getPublicKeyFrom(String classPathFile) throws NoSuchAlgorithmException, InvalidKeySpecException,
        IOException {
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final byte[] content = IOUtils.toByteArray(ClassLoader.getSystemResourceAsStream(classPathFile));
        final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(content);
        final PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }


    public PrivateKey getPrivateKeyFrom(String classPathFile) throws NoSuchAlgorithmException, InvalidKeySpecException,
        IOException {
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final byte[] bytes = IOUtils.toByteArray(ClassLoader.getSystemResourceAsStream(classPathFile));
        final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        final PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }


    public byte[] signUseMd5withRSA(byte[] bytes, PrivateKey privateKey) {
        try {
            final Signature md5withRSA = Signature.getInstance("MD5withRSA");
            md5withRSA.initSign(privateKey);
            md5withRSA.update(bytes);
            final byte[] sign = md5withRSA.sign();
            return sign;
        } catch (Exception e) {
            log.info("unExpectedException", e);
            throw new IllegalArgumentException();
        }
    }

    public boolean signVerifyUseMd5withRSA(byte[] bytes, byte[] signature, PublicKey publicKey) {
        try {
            final Signature md5withRSA = Signature.getInstance("MD5withRSA");
            md5withRSA.initVerify(publicKey);
            md5withRSA.update(bytes);
            final boolean verify = md5withRSA.verify(signature);
            return verify;
        } catch (Exception e) {
            log.info("unExpectedException", e);
            throw new IllegalArgumentException();
        }
    }


    @Test
    void givenSshKeyGenRsaPair_when_then() throws NoSuchAlgorithmException, InvalidKeySpecException,
        NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        String origin = "Hello, World!\n你好，世界。！";
        // 公钥加密
        final List<String> strings = Files.readAllLines(new File("/Volumes/RamDisk/rsa_pkcs8.pub").toPath());
        final String publicKeyBase64 = strings.stream().filter(s -> !s.startsWith("----")).collect(Collectors.joining(""));
        final byte[] encrypt = encrypt(origin.getBytes(UTF_8), publicKeyBase64);
        log.info("origin: {}, encrypt: {}", origin, base64Encode(encrypt));
        log.info("origin: {}, encrypt hex: {}", origin, Hex.encodeHexString(encrypt));
        // 私钥解密
        final byte[] privateKeyFileContent = Files.readAllBytes(new File("/Volumes/RamDisk/pkcs8.der").toPath());
        final byte[] decryptBytes = decrypt(encrypt, base64Encode(privateKeyFileContent));
        String decrypt = new String(decryptBytes, UTF_8);
        log.info("decrypt: {}", decrypt);
        then(origin).isEqualTo(decrypt);
    }

}