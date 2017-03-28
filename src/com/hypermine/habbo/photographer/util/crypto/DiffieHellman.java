package com.hypermine.habbo.photographer.util.crypto;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */
import java.math.BigInteger;
import java.util.Random;

public class DiffieHellman
{
    private int BITLENGTH = 30;
    private static BigInteger Prime;
    private static BigInteger Generator;
    private BigInteger PrivateKey;
    BigInteger PublicKey;
    private BigInteger PublicClientKey;
    BigInteger SharedKey;

    DiffieHellman(BigInteger prime, BigInteger generator)
    {
        Prime = prime;
        Generator = generator;

        this.PrivateKey = new BigInteger(GenerateRandomHexString(BITLENGTH), 16);
        if (Generator.intValue() > Prime.intValue())
        {
            BigInteger temp = Prime;
            Prime = Generator;
            Generator = temp;
        }
        this.PublicKey = Generator.modPow(this.PrivateKey, Prime);
    }

    void GenerateSharedKey(byte[] ckey)
    {
        this.PublicClientKey = new BigInteger(ckey);

        this.SharedKey = this.PublicClientKey.modPow(this.PrivateKey, Prime);
    }

    private static String GenerateRandomHexString(int len)
    {
        int rand = 0;
        String result = "";

        Random rnd = new Random();
        for (int i = 0; i < len; i++)
        {
            rand = 1 + (int)(rnd.nextDouble() * 254.0D);
            result = result + Integer.toString(rand, 16);
        }
        return result;
    }
}
