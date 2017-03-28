package com.hypermine.habbo.photographer.util.crypto;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/27/2017.
 */
public class RC4Proper {
    private Cipher ecipher;

    RC4Proper(SecretKey key) {
        try {
            ecipher = Cipher.getInstance("RC4");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] encrypt(byte[] data) {
        try {
            return ecipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ByteBuf encipher(ByteBuf bytes) {
        ByteBuf result = Unpooled.buffer();
        byte[] byteArray = new byte[bytes.readableBytes()];
        int i = 0;
        while (bytes.isReadable()) {
            byteArray[i] = bytes.readByte();
            i++;
        }

        result.writeBytes(encrypt(byteArray));
        return result;
    }
}
