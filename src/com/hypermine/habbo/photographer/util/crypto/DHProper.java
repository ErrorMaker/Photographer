package com.hypermine.habbo.photographer.util.crypto;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/27/2017.
 */
public class DHProper {
    private DHPrivateKey privateKey;
    DHPublicKey publicKey;

    public DHProper(BigInteger prime, BigInteger generator) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH", "BC");
            DHParameterSpec dhSpec = new DHParameterSpec(prime, generator);
            keyGen.initialize(dhSpec);
            KeyPair keyPair = keyGen.generateKeyPair();

            privateKey = (DHPrivateKey) keyPair.getPrivate();
            publicKey = (DHPublicKey) keyPair.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] GenerateSharedKey(byte[] publicKeyBytes) {
        try {
            DHPublicKeySpec keySpec = new DHPublicKeySpec(
                    new BigInteger(publicKeyBytes),
                    publicKey.getParams().getP(),
                    publicKey.getParams().getG()
            );

            KeyFactory keyFactory = KeyFactory.getInstance("DH");
            publicKey = (DHPublicKey) keyFactory.generatePublic(keySpec);

            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH", "BC");
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(publicKey, true);

            return keyAgreement.generateSecret();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
