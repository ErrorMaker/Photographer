package com.hypermine.habbo.photographer.util.crypto;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.Security;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */
public class RC4 {
    private final byte[] S = new byte[256];
    private final byte[] T = new byte[256];
    private final byte[] key;
    private final int keylen;

    public RC4(final byte[] key) {
        this.key = key;
        if (key.length < 1 || key.length > 256) {
            throw new IllegalArgumentException(
                    "key must be between 1 and 256 bytes");
        } else {
            keylen = key.length;
            for (int i = 0; i < 256; i++) {
                S[i] = (byte) i;
                T[i] = key[i % keylen];
            }
            int j = 0;
            byte tmp;
            for (int i = 0; i < 256; i++) {
                j = (j + S[i] + T[i]) & 0xFF;
                tmp = S[j];
                S[j] = S[i];
                S[i] = tmp;
            }
        }
    }

    public byte[] encrypt(final byte[] plaintext) {
        final byte[] ciphertext = new byte[plaintext.length];
        int i = 0, j = 0, k, t;
        byte tmp;
        for (int counter = 0; counter < plaintext.length; counter++) {
            i = (i + 1) & 0xFF;
            j = (j + S[i]) & 0xFF;
            tmp = S[j];
            S[j] = S[i];
            S[i] = tmp;
            t = (S[i] + S[j]) & 0xFF;
            k = S[t];
            ciphertext[counter] = (byte) (plaintext[counter] ^ k);
        }
        return ciphertext;
    }

    public byte[] decrypt(final byte[] ciphertext) {
        return encrypt(ciphertext);
    }

    public ByteBuf decipher(ByteBuf bytes) {
        ByteBuf result = Unpooled.buffer();
        byte[] byteArray = new byte[bytes.readableBytes()];
        int i = 0;
        while (bytes.isReadable()) {
            byteArray[i] = bytes.readByte();
            i++;
        }

        result.writeBytes(encipher(byteArray));
        return result;
    }

    public byte[] encipher(byte[] data) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            SecretKey sk = new SecretKeySpec(key, 0, key.length, "ARC4");
            Cipher cipher = Cipher.getInstance("ARC4");
            cipher.init(Cipher.ENCRYPT_MODE, sk);
            byte[] encrypted = cipher.doFinal(data);

            return encrypted;
        } catch (Exception e) {
            return new byte[0];
        }
    }
}
