package com.hypermine.habbo.photographer.util.crypto;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */

import java.math.BigInteger;
import java.util.Arrays;

public class HabboEncryption
{
    public DiffieHellman dh;
    public RSAKey rsaKey;
    public RC4 rc4;
    public Boolean canEncrypt = false;

    public HabboEncryption() {
        rsaKey = RSAKey.parsePublicKey("e052808c1abef69a1a62c396396b85955e2ff522f5157639fa6a19a98b54e0e4d6e44f44c4c0390fee8ccf642a22b6d46d7228b10e34ae6fffb61a35c11333780af6dd1aaafa7388fa6c65b51e8225c6b57cf5fbac30856e896229512e1f9af034895937b2cb6637eb6edf768c10189df30c10d8a3ec20488a198063599ca6ad", 65537);
    }

    public void VerifyDHPrimes(String prime, String generator)
    {
        BigInteger primeDec = new BigInteger(rsaKey.decrypt(prime));
        BigInteger generatorDec = new BigInteger(rsaKey.decrypt(generator));

        dh = new DiffieHellman(primeDec, generatorDec);
    }

    public String GetDHPublic() {
        return rsaKey.encrypt(dh.PublicKey.toString());
    }

    public void InitCrypto(String publicKey) {
        dh.GenerateSharedKey(rsaKey.decrypt(publicKey));
        rc4 = new RC4(dh.SharedKey.toByteArray());
        canEncrypt = true;
    }
}