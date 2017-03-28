package com.hypermine.habbo.photographer.util.crypto;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import sun.security.mscapi.RSAKeyPairGenerator;
import sun.security.rsa.RSAKeyFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/27/2017.
 */
public class RSAProper {
    private Cipher encipher;
    private Cipher decipher;
    public RSAProper() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger("e052808c1abef69a1a62c396396b85955e2ff522f5157639fa6a19a98b54e0e4d6e44f44c4c0390fee8ccf642a22b6d46d7228b10e34ae6fffb61a35c11333780af6dd1aaafa7388fa6c65b51e8225c6b57cf5fbac30856e896229512e1f9af034895937b2cb6637eb6edf768c10189df30c10d8a3ec20488a198063599ca6ad", 16), BigInteger.valueOf(66537));
            RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(keySpec);

            encipher = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
            encipher.init(Cipher.ENCRYPT_MODE, key);
            decipher = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
            decipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] decrypt(BigInteger data) {
        try {
            return decipher.doFinal(data.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] decrypt(String text) {
        try {
            return decipher.doFinal(hexToBytes(text));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String encrypt(String text) {
        try {
            return bytesToHex(encipher.doFinal(hexToBytes(text)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
