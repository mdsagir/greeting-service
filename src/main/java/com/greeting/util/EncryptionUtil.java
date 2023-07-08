package com.greeting.util;

import com.greeting.exception.BadRequestException;
import com.greeting.exception.DataNotFoundException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

public class EncryptionUtil {

    private static final byte[] SALT = {(byte) 0x21, (byte) 0x21, (byte) 0xF0, (byte) 0x55, (byte) 0xC3, (byte) 0x9F, (byte) 0x5A, (byte) 0x75};
    private static final int ITERATION_COUNT = 31;
    private static final String INVALID = "INVALID ID";

    private EncryptionUtil() {
    }

    public static String encode(Long id) {
        String formatted = String.format("%020d", id);
        return encoding(formatted);
    }

    public static Long decode(String id) {
        var decoding = decoding(id);
        if (!StringUtils.hasText(decoding)) {

            throw new DataNotFoundException(INVALID);
        }
        return Long.parseLong(decoding);
    }

    public static String encoding(String input) {
        try {
            KeySpec keySpec = new PBEKeySpec(null, SALT, ITERATION_COUNT);
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            byte[] enc = ecipher.doFinal(input.getBytes());
            String res = new String(Base64.encodeBase64(enc));
            res = res.replace('+', '-').replace('/', '_').replace("%", "%25").replace("\n", "%0A");
            return res;

        } catch (Exception e) {
            throw new BadRequestException(INVALID);
        }
    }

    public static String decoding(String token) {
        try {
            String input = token.replace("%0A", "\n").replace("%25", "%").replace('_', '/').replace('-', '+');
            byte[] dec = Base64.decodeBase64(input.getBytes());
            KeySpec keySpec = new PBEKeySpec(null, SALT, ITERATION_COUNT);
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
            byte[] decoded = dcipher.doFinal(dec);
            return new String(decoded);
        } catch (Exception e) {
            throw new DataNotFoundException(INVALID);
        }
    }
}
