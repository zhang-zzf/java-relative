package com.github.learn;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyPair;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/11/3
 */
@Slf4j
public class CipherUtilTest {

    private final String origin = "Hello, World!\n你好，世界！";


    @Test
    public void given_whenHexAndDeHex_then() {
        final byte[] data = origin.getBytes(UTF_8);
        final CipherUtil util = CipherUtil.getInstance();
        then(util.deHex(util.hex(data))).isEqualTo(data);
        then(util.deHex(util.hex(data).toUpperCase())).isEqualTo(data);
    }

    /**
     * RSA 私钥签名 -> 公钥验证
     */
    @Test
    public void givenRSA_whenSignAndVerify_then() {
        final CipherUtil instance = CipherUtil.getInstance();
        final KeyPair keyPair = instance.generateRSAKey();
        final byte[] data = origin.getBytes(UTF_8);
        // 私钥签名
        final byte[] signature = instance.md5WithRSASign(data,
            instance.base64Encode(keyPair.getPrivate().getEncoded()));
        // 公钥验证
        final boolean verify = instance.md5WithRSAVerify(data, signature,
            instance.base64Encode(keyPair.getPublic().getEncoded()));
        then(verify).isTrue();
    }

    /**
     * RSA 公钥加密 -> 私钥解密
     */
    @Test
    public void givenRSA_whenEncryptAndDecrypt_then() {
        final CipherUtil instance = CipherUtil.getInstance();
        final KeyPair keyPair = instance.generateRSAKey();
        // 公钥加密
        final byte[] encrypt = instance.rsaEncrypt(origin.getBytes(UTF_8),
            instance.base64Encode(keyPair.getPublic().getEncoded()));
        final byte[] decrypt = instance.rsaDecrypt(encrypt,
            instance.base64Encode(keyPair.getPrivate().getEncoded()));
        then(new String(decrypt)).isEqualTo(origin);
    }

    /**
     * sm4 加解密
     */
    @Test
    public void givenSm4_when_then() {
        final CipherUtil instance = CipherUtil.getInstance();
        final String sm4Key = instance.generateSm4Key();
        final byte[] encrypt = instance.sm4Encrypt(origin.getBytes(UTF_8), sm4Key);
        final String str = new String(instance.sm4Decrypt(encrypt, sm4Key), UTF_8);
        then(str).isEqualTo(origin);
    }

    /**
     * md5 大文件加密
     */
    @Test
    @Ignore
    public void givenFile_whenMd5_then() {
        final File file = new File("/Volumes/RamDisk/20211103/id_rsa");
        // > md5 id_rsa
        // MD5 (id_rsa) = 449d5e230d4d77a50229ea878e81c4a3
        final String fileMd5 = "449d5e230d4d77a50229ea878e81c4a3";
        final CipherUtil instance = CipherUtil.getInstance();
        final String md5 = instance.calcFileMd5(file);
        then(fileMd5).isEqualTo(md5);
    }

    /**
     * 生成 AES key
     */
    @Test
    public void given_whenGenerateAESKey_then() {
        final String aesKey = CipherUtil.getInstance().generateAESKey();
        log.info("AES Key: {}", aesKey);
    }

    /**
     * AES 加密 / 解密
     */
    @Test
    public void givenAES_whenEncryptAndDecrypt_then() {
        String origin = "Hello, World!\n你好，世界！";
        final CipherUtil instance = CipherUtil.getInstance();
        final String key = instance.generateAESKey();
        log.info("AES key: {}", key);
        final String aesEncodeStr = instance.aesEncode(origin.getBytes(UTF_8), key);
        final byte[] decryptBytes = instance.aesDecode(aesEncodeStr, key);
        final String actual = new String(decryptBytes, UTF_8);
        then(actual).isEqualTo(origin);
    }

    /**
     * 使用系统命令对 文件加密 时特别注意纯文本文件文件最后有没有 '\n' 字符
     */

    /**
     * Java 读 vim 等系统生成的文件
     * <p>小心文件结尾最后一个 '\n' 字符到文件结尾</p>
     */
    @Test
    public void givenVimFile_readAllBytes_then() throws IOException {
        /**
         * vim test2.txt
         * 只写入一个 H 并保存
         */
        final File file = new File("/Volumes/RamDisk/test/test2.txt");
        // 注意：文件一共有 'H' '\n' 2个字符
        then(Files.readAllBytes(file.toPath())).hasSize(2);
        // 读行
        final List<String> strings = Files.readAllLines(file.toPath());
        then(strings).contains("H");
        then(strings.get(0).getBytes(UTF_8)).hasSize(1);
    }


    /**
     * Java 读写文件
     * <p>字符串写入文件时不会额外加 '\n' 字符到文件结尾</p>
     */
    @Test
    public void givenJavaStringFile_readAllBytes_then() throws IOException {
        final File file = new File("/Volumes/RamDisk/test/test.txt");
        try (final BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            writer.write("H");
            writer.newLine();
            writer.write("o");
            writer.newLine();
            writer.flush();
        }
        then(Files.readAllBytes(file.toPath())).hasSize(4);
        // 读行
        final List<String> strings = Files.readAllLines(file.toPath());
        then(strings).contains("H", "o");
        then(strings.get(0).getBytes(UTF_8)).hasSize(1);
    }

    /**
     * Java 读写文件
     * <p>字符串写入文件时不会额外加 '\n' 字符到文件结尾</p>
     */
    @Test
    public void givenJavaWriteByteFile_readAllBytes_then() throws IOException {
        final File file = new File("/Volumes/RamDisk/test/test.txt");
        Files.write(file.toPath(), "H".getBytes(UTF_8));
        then(Files.readAllBytes(file.toPath())).hasSize(1);
        // 读行
        final List<String> strings = Files.readAllLines(file.toPath());
        then(strings).contains("H");
        final byte[] bytes1 = strings.get(0).getBytes(UTF_8);
        then(bytes1).hasSize(1);
    }

    @Test
    public void givenRSAKeyPair_whenWriteFile_then() throws IOException {
        final KeyPair keyPair = CipherUtil.getInstance().generateRSAKey();
        final String privateKey = "/Volumes/RamDisk/security/java_rsa_1024_pkcs8.der";
        final String publicKey = "/Volumes/RamDisk/security/java_rsa_1024_pkcs8_pub.der";
        Files.write(new File(privateKey).toPath(), keyPair.getPrivate().getEncoded());
        Files.write(new File(publicKey).toPath(), keyPair.getPublic().getEncoded());
    }

}