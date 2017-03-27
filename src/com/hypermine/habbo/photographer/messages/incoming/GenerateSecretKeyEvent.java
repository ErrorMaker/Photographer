package com.hypermine.habbo.photographer.messages.incoming;

import com.hypermine.habbo.photographer.messages.outgoing.IdkNameComposer;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */
public class GenerateSecretKeyEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        String prime = this.packet.readString();
        String gen = this.packet.readString();

        client.habboEncryption.VerifyDHPrimes(prime, gen);

        IdkNameComposer composer = new IdkNameComposer(client.habboEncryption.GetDHPublic());
        client.sendMessage(composer.compose());
    }
}
