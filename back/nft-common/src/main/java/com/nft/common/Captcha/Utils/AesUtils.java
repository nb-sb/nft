package com.nft.common.Captcha.Utils;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtils {

    public static final String CONTENT_CIPHER_ALGORITHM = "AES/CTR/NoPadding";

    @SneakyThrows
    public static byte[] decrypt(byte[] key, byte[] iv, byte[] data) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(CONTENT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }
    @SneakyThrows
    public static byte[] encrypt(byte[] key, byte[] iv, byte[] data) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(CONTENT_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }

}
