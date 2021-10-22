package com.feng.learn.encrypt;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.util.OpenSSHPrivateKeyUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;
import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author zhanfeng.zhang
 * @date 2020/05/19
 */
@Slf4j
public class EncryptTest {

    /**
     *
     */
    @Test
    void generateRSAKeyPair() throws NoSuchAlgorithmException {
        final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        final KeyPair keyPair = generator.generateKeyPair();
        final PrivateKey aPrivate = keyPair.getPrivate();
        final PublicKey aPublic = keyPair.getPublic();

        // RSA
        final String algorithm = aPrivate.getAlgorithm();
        final byte[] encoded = aPrivate.getEncoded();
        // PKCS8#8
        final String format = aPrivate.getFormat();

        // RSA
        final String algorithm1 = aPublic.getAlgorithm();
        final byte[] encoded1 = aPublic.getEncoded();
        // X.509
        final String format1 = aPublic.getFormat();

        final String privateBase64 = Base64.getEncoder().encodeToString(aPrivate.getEncoded());
        final String publicBase64 = Base64.getEncoder().encodeToString(aPublic.getEncoded());
    }


    @Test
    void givenSshKeyGenRsaPair_whenDecodePublicKey_then() throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // public key 转换
        final String publicKeyFileContent = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzoSduyq6Yw1HLnv5M28l\n" +
                "mHcBf24LnGGC5P6/mWQUgSCrmYnkIYcGCYtPeeRlC+0yQAqAio0jZPZ+TewQ+QtU\n" +
                "zlny9yfzDCj7mfbVDWDE6Oe141zwH4o6LP3MQSAYTAXMwQWGckr9qGNRUVGUzrQi\n" +
                "4zPnk4gSfJPjbNKpv+SzfNozEghIWvdBqY1GCGGbIu3uxcPhLW97cpL9wGj2g3ei\n" +
                "IOGQ2AzWZzRBFo/F4E5fE64iDucKl061Yinx0xcqkVy95a/xTmOus204eB+iHLKD\n" +
                "mtq6AMbq+3q1htF5dd97Fw8LL/pE0Q4FMEkqI62x2hsQ2OQ0i5SZlGJ9Nw31kcxP\n" +
                "WwIDAQAB";
        final String publicKeyBase64 = publicKeyFileContent.replaceAll("\r\n|\r|\n", "");
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyBase64)));

        String origin = "Hello, World!\n你好，世界。！";

        final Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(ENCRYPT_MODE, publicKey);
        final byte[] encrypt = encryptCipher.doFinal(origin.getBytes(StandardCharsets.UTF_8));
        final String base64Encrypt = Base64.getEncoder().encodeToString(encrypt);
        log.info("origin: {}, encrypt: {}", origin, base64Encrypt);
    }

    @Test
    void givenSshKeyGenRsaPair_whenDecodePrivateKey_then() throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        Security.addProvider(new BouncyCastleProvider());
        // public key 转换
        String privateKeyFileContent = "-----BEGIN OPENSSH PRIVATE KEY-----\n" +
                "b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAABFwAAAAdzc2gtcn\n" +
                "NhAAAAAwEAAQAAAQEAzoSduyq6Yw1HLnv5M28lmHcBf24LnGGC5P6/mWQUgSCrmYnkIYcG\n" +
                "CYtPeeRlC+0yQAqAio0jZPZ+TewQ+QtUzlny9yfzDCj7mfbVDWDE6Oe141zwH4o6LP3MQS\n" +
                "AYTAXMwQWGckr9qGNRUVGUzrQi4zPnk4gSfJPjbNKpv+SzfNozEghIWvdBqY1GCGGbIu3u\n" +
                "xcPhLW97cpL9wGj2g3eiIOGQ2AzWZzRBFo/F4E5fE64iDucKl061Yinx0xcqkVy95a/xTm\n" +
                "Ous204eB+iHLKDmtq6AMbq+3q1htF5dd97Fw8LL/pE0Q4FMEkqI62x2hsQ2OQ0i5SZlGJ9\n" +
                "Nw31kcxPWwAAA8gGa7BtBmuwbQAAAAdzc2gtcnNhAAABAQDOhJ27KrpjDUcue/kzbyWYdw\n" +
                "F/bgucYYLk/r+ZZBSBIKuZieQhhwYJi0955GUL7TJACoCKjSNk9n5N7BD5C1TOWfL3J/MM\n" +
                "KPuZ9tUNYMTo57XjXPAfijos/cxBIBhMBczBBYZySv2oY1FRUZTOtCLjM+eTiBJ8k+Ns0q\n" +
                "m/5LN82jMSCEha90GpjUYIYZsi7e7Fw+Etb3tykv3AaPaDd6Ig4ZDYDNZnNEEWj8XgTl8T\n" +
                "riIO5wqXTrViKfHTFyqRXL3lr/FOY66zbTh4H6IcsoOa2roAxur7erWG0Xl133sXDwsv+k\n" +
                "TRDgUwSSojrbHaGxDY5DSLlJmUYn03DfWRzE9bAAAAAwEAAQAAAQAB5ATtqb57GawFKO1n\n" +
                "IyVc9/nziLZigCIKpqpqZRgq9nmbtj94bxwHCld37jdenJVRMMdsfU5NplQRso+VOB1Wpk\n" +
                "wrJxp6igWDq1bYAILM/xITOs6X+1P9BXXRQtH2IDd3rO1GLpmT6RTdgbmzCsMnAVqlZyGE\n" +
                "MnZqkKMtNpLOGc0TqPUXxr3+Eb8Dvq3s+3dqmH6H+7sagBLYNwU6sEp0qLWVXHi5J1J5rR\n" +
                "6gUX0XMwWn6rfKJW1VhGn3gRGVJPq1CI29+H3hDnSwhDbeqPx1dl3Ln0T5ElNHsVlz7Ejm\n" +
                "TCxXg4h+EOOLUHIw7OnTgcUGLAk8ZZjCw+XmUSCjaEJBAAAAgQC1smlXTVobh5b7iSA+FU\n" +
                "IajTTqFNY/DmGDPa5HFSwnm5RDat8YLOToaDQPGsS/kasjl3LlZR+pW1VyqLiyWsjQdBtr\n" +
                "Aelp7pE4QxlYnU8wBg2VxdRhVN2+MGbV7XPK9QiH8F6QImi55NhhhzmnDmmbeWQEUSqXx4\n" +
                "WjiSIaQnsOzwAAAIEA+77kGH6U8QVJVEHhWck7K63mnujI3k4HdVv8W4MsuSpxjSJhDEZG\n" +
                "L6WSi3go65FWZTssb9uOYSyLdIOnAGAAi5+47QxzZNWG7gsZXemsofK+InZLqeO2M3gsPq\n" +
                "DojHmx0DE2B1ZzpLuIZfs8Q18oQqvir74Fpqi6c9H4HsTruDUAAACBANICD2K/cOuaIw4n\n" +
                "DucKmP3yWxJHGVmhe5lV+aSDDrA8xGQbMboTQdpm7AhMymWP8q3rdnaGU1L0BRwQs/fBVW\n" +
                "7awqHmiD73W05HF5hOZB/CNoI2ZoU2yXpelxZy8XYL1vv7aBPWLHaOW249Th/LVZDCvYE0\n" +
                "dNrfbbnxcnHg+XtPAAAAEWZlbmdAbWFjYm9va3BybzE1AQ==\n" +
                "-----END OPENSSH PRIVATE KEY-----\n";
        PemReader pemReader = new PemReader(new InputStreamReader(new ByteArrayInputStream(
                privateKeyFileContent.getBytes(StandardCharsets.UTF_8))));
        PemObject pemObject = pemReader.readPemObject();
        final byte[] content = pemObject.getContent();
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(content));

        String origin = "Hello, World!\n你好，世界。！";
        String encrypt = "x3slC/O3RAQX1+lf7M7iYU753KGyyjCDdOwv1squykVcLVPgvTbYcjA+UvFjI9aJCj/Q8tbJ7egjJdR8X7mzEXnkmFZxmOBskunfX4DigREYw9xib2al3BxcyWFxBHOyP82gEv4VpPHPdY7M8emBpG2bx8OfzwNwU/MInM7ROpYE3IlcocrADbrmUeGfkt4UBaBQTMRzp0EJRi6CVxcywx624RQHYO4nLltTYK/X2nmLqovbCaJjSJIMEEEGTP5Q6+fqpDFOPNS2Tu2YY5D1+7S4CBAVn7ne0u8CViBMmytQz7hYZAzO4PQxwgellqcX6LTOKUegs5EZ+R89VV9o3g==";

        final Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(DECRYPT_MODE, privateKey);
        final byte[] originBytes = encryptCipher.doFinal(Base64.getDecoder().decode(encrypt));
        String decrypt = new String(originBytes, StandardCharsets.UTF_8);
        log.info("encrypt: {}, decrypt: {}", encrypt, decrypt);
        then(origin).isEqualTo(decrypt);
    }


}