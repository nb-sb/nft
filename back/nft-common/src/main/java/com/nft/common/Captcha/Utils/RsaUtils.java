package com.nft.common.Captcha.Utils;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaUtils {
    static {
        CryptoRuntime.enableBouncyCastle();
    }

    public static final String KEY_WRAP_ALGORITHM = "RSA/ECB/PKCS1Padding";

    @SneakyThrows
    public static byte[] encrypt(Key key, byte[] data) {
        Cipher cipher = Cipher.getInstance(KEY_WRAP_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, new SecureRandom());
        byte[] encryptBytes = cipher.doFinal(data);
        return encryptBytes;
    }


    @SneakyThrows
    public static byte[] decrypt(Key key, byte[] encryptedData) {
        Cipher cipher = Cipher.getInstance(KEY_WRAP_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptData = cipher.doFinal(encryptedData);
        return decryptData;
    }


    public static RSAPublicKey getPublicKeyFromPemX509(final String publicKeyStr) {
        try {
            String adjustStr = StringUtils.replace(publicKeyStr, "-----BEGIN PUBLIC KEY-----", "");
            adjustStr = StringUtils.replace(adjustStr, "-----BEGIN RSA PUBLIC KEY-----", "");
            adjustStr = StringUtils.replace(adjustStr, "-----END PUBLIC KEY-----", "");
            adjustStr = StringUtils.replace(adjustStr, "-----END RSA PUBLIC KEY-----", "");
            adjustStr = adjustStr.replace("\n", "");
            adjustStr = adjustStr.replace("\r", "");

            byte[] buffer = Base64.getDecoder().decode(adjustStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);

            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Get public key from X509 pem String error." + e.getMessage(), e);
        }
    }

    public static RSAPrivateKey getPrivateKeyFromPemPKCS8(final String privateKeyStr) {
        try {
            String adjustStr = StringUtils.replace(privateKeyStr, "-----BEGIN PRIVATE KEY-----", "");
            adjustStr = StringUtils.replace(adjustStr, "-----BEGIN RSA PRIVATE KEY-----", "");
            adjustStr = StringUtils.replace(adjustStr, "-----END PRIVATE KEY-----", "");
            adjustStr = StringUtils.replace(adjustStr, "-----END RSA PRIVATE KEY-----", "");
            adjustStr = adjustStr.replace("\n", "");
            adjustStr = adjustStr.replace("\r", "");

            byte[] buffer = Base64.getDecoder().decode(adjustStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Get private key from PKCS8 pem String error: " + e.getMessage(), e);
        }
    }

    public static RSAPrivateKey getPrivateKeyFromPemPKCS1(final String privateKeyStr) {
        try {
            String adjustStr = StringUtils.replace(privateKeyStr, "-----BEGIN PRIVATE KEY-----", "");
            adjustStr = StringUtils.replace(adjustStr, "-----BEGIN RSA PRIVATE KEY-----", "");
            adjustStr = StringUtils.replace(adjustStr, "-----END PRIVATE KEY-----", "");
            adjustStr = StringUtils.replace(adjustStr, "-----END RSA PRIVATE KEY-----", "");
            adjustStr = adjustStr.replace("\n", "");
            adjustStr = adjustStr.replace("\r", "");
            CryptoRuntime.enableBouncyCastle();
            byte[] buffer = Base64.getDecoder().decode(adjustStr);
            RSAPrivateKeySpec keySpec = CryptoRuntime.convertPemPKCS1ToPrivateKey(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("get private key from PKCS1 pem String error." + e.getMessage(), e);
        }
    }

    /**
     * 生成公私钥
     *
     * @param keySize key大小
     * @return String[]
     */
    public static String[] genKeyPair(int keySize) {
        byte[][] keyPairBytes = genKeyPairBytes(keySize);
        String[] keyPairs = new String[2];
        keyPairs[0] = Base64.getEncoder().encodeToString(keyPairBytes[0]);
        keyPairs[1] = Base64.getEncoder().encodeToString(keyPairBytes[1]);
        return keyPairs;
    }

    public static byte[][] genKeyPairBytes(int keySize) {
        byte[][] keyPairBytes = new byte[2][];
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA", "SunRsaSign");
            gen.initialize(keySize, new SecureRandom());
            KeyPair pair = gen.generateKeyPair();
            keyPairBytes[0] = pair.getPrivate().getEncoded();
            keyPairBytes[1] = pair.getPublic().getEncoded();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        return keyPairBytes;
    }

}
