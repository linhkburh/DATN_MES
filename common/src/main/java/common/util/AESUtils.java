package common.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class AESUtils {
    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue =
        new byte[] { 'a', 'v', '#', '%', 'x', '*', '1', 'V', '1', 'o', 'p', '$', '(', '$', '%', 'z' };

    public static String encrypt(String input) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(input.getBytes());
        String encryptedValue = java.util.Base64.getEncoder().encodeToString(encValue);
        return encryptedValue;
    }

    public static String decrypt(String input) throws Exception {        
        if(Formater.isNull(input))
            return null; 
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = java.util.Base64.getDecoder().decode(input);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        return key;
    }
}
