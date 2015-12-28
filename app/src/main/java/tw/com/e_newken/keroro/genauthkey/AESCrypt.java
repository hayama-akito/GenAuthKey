package tw.com.e_newken.keroro.genauthkey;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by keroro on 2015/12/21.
 */
public class AESCrypt {
    //region static final parameters
    private static final String TAG = "AESCrypt";

    //AESCrypt-ObjC uses CBC and NoPadding
    private static final String AES_MODE = "AES/CBC/NoPadding";

    //AESCrypt-ObjC uses MD5
    private static final String HASH_ALGORITHM = "MD5";

    private static final int AES_CBC_128_LEN = 16;


    //togglable log option (please turn off in live!)
    public static boolean DEBUG_LOG_ENABLED = false;
    //endregion

    //region private variable
    private byte[] keyBytes;
    private byte[] ivBytes;

    private SecretKeySpec secretKeySpec;
    private IvParameterSpec ivSpec;

    //endregion

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static void log(String what, byte[] bytes) {
        if (DEBUG_LOG_ENABLED)
            Log.d(TAG, what + "[" + bytes.length + "] [" + bytesToHex(bytes) + "]");
    }

    private static void log(String what, String value) {
        if (DEBUG_LOG_ENABLED)
            Log.d(TAG, what + "[" + value.length() + "] [" + value + "]");
    }

    public boolean set_ivBytes(final String ivString) throws UnsupportedEncodingException {
        ivBytes = ivString.getBytes("UTF-8");
        if (ivBytes.length != AES_CBC_128_LEN) {
            ivBytes = null;
            ivSpec = null;
            return false;
        }
        ivSpec = new IvParameterSpec(ivBytes);
        log("AES_IV", ivBytes);
        return true;
    }

    public boolean set_keyBytes(final String keyString) throws UnsupportedEncodingException {
        keyBytes = keyString.getBytes("UTF-8");
        if (keyBytes.length != AES_CBC_128_LEN) {
            keyBytes = null;
            secretKeySpec = null;
            return false;
        }

        secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        log("AES_KS", keyBytes);
        return true;
    }

    public boolean set_key(final byte[] key, final byte[] iv) {
        keyBytes = key;
        ivBytes = iv;

        if (key == null || iv == null)
            return false;

        if ((keyBytes.length != AES_CBC_128_LEN) || (ivBytes.length != AES_CBC_128_LEN))
            return false;

        secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        ivSpec = new IvParameterSpec(ivBytes);

        return true;
    }

    public byte[] encrypt(byte[] data)
            throws GeneralSecurityException {

        if (ivSpec == null || secretKeySpec == null) {
            log("encrypt", "AES initial isn't completed");
            return null;
        }

        final Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
        byte[] cipherText = cipher.doFinal(data);

        log("cipherText", cipherText);
        return cipherText;
    }

    public byte[] decrypt(final byte[] decodedCipherText)
            throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance(AES_MODE);

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
        byte[] decryptedBytes = cipher.doFinal(decodedCipherText);

        log("decryptedBytes", decryptedBytes);

        return decryptedBytes;
    }

}
