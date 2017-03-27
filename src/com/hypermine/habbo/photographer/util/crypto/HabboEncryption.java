package com.hypermine.habbo.photographer.util.crypto;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */

import java.math.BigInteger;

public class HabboEncryption
{
    public String GetHandshakePublic(String prime, String generator)
    {
        //BigInteger exp = exponent;
        RSAKey key = RSAKey.parsePublicKey("e052808c1abef69a1a62c396396b85955e2ff522f5157639fa6a19a98b54e0e4d6e44f44c4c0390fee8ccf642a22b6d46d7228b10e34ae6fffb61a35c11333780af6dd1aaafa7388fa6c65b51e8225c6b57cf5fbac30856e896229512e1f9af034895937b2cb6637eb6edf768c10189df30c10d8a3ec20488a198063599ca6ad", "65537");
        BigInteger primeDec = new BigInteger(key.decrypt(prime));
        BigInteger generatorDec = new BigInteger(key.decrypt(generator));

        DiffieHellman dh = new DiffieHellman(primeDec, generatorDec);

        System.out.println(dh.PublicKey.toString(16));

        return dh.PublicKey.toString(16);

//        BigInteger shared = GenerateDHKeys(primeDec, generatorDec);
//        BigInteger DHPublic = GenerateDHKeys(primeDec, generatorDec);
//        System.out.println(DHPublic.toString(16));
//        String shared = key.encrypt(DHPublic);
//
//        System.out.println(shared);
//
//        return shared;

//        try {
//            BigInteger DHPrime = Decrypt(prime);
//            BigInteger DHGen = Decrypt(generator);
//            BigInteger shared = GenerateDHKeys(DHPrime, DHGen);
//
//            return bytesToHex(shared.toByteArray());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return "";
    }

    public BigInteger GenerateDHKeys(BigInteger p, BigInteger g) {
        //BigInteger random = new BigInteger(256, 90, new Random());
        BigInteger random = new BigInteger("0084ac6944a524b933732c84e9c23ec1a2015fb97c46595352996f18899812dfc9", 16); // lol random
        String rnd = bytesToHex(random.toByteArray());
        return g.modPow(random, p);
    }

    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    /*public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public BigInteger GenerateDHKeys(BigInteger p, BigInteger g) {
        BigInteger random = new BigInteger(512, 15, new Random());
        return g.modPow(random, p);
    }

    public BigInteger Decrypt(String value)
    {
        BigInteger signed = new BigInteger(value, 16);
        BigInteger paddedInteger = CalculatePublic(signed);

        byte[] valueData = paddedInteger.toByteArray();
//        reverse(valueData);

        valueData = pkcs1unpad(paddedInteger, (int)Math.floor((modulus.bitLength()+7)/8));
        return new BigInteger(valueData);
    }

    public void reverse(byte[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    public BigInteger CalculatePublic(BigInteger value) {
        return value.modPow(exponent, modulus);
    }

    public byte[] PKCSUnpad(byte[] value) {
        int position = 0;
        while (value[position++] != 0) ;

        byte[] buffer = new byte[value.length - position];
        System.arraycopy(value, position, buffer, 0, buffer.length);

        return buffer;
    }

    public static byte[] pkcs1unpad(final BigInteger src, int n) {
        final byte[] b = src.toByteArray();

        int i = 0;
        while ((i < b.length) && (b[i] == 0)) {
            i++;
        }

        if (((b.length - i) != (n - 1)) || (b[i] != 0x2))
            return null;

        i++;

        while (b[i] != 0) {
            if (++i >= b.length)
                return null;
        }
        final byte[] out = new byte[b.length - (i + 1)];
        int p = 0;
        while (++i < b.length) {
            out[p] = b[i];
            p++;
        }
        return out;
    }*/
}