package com.hypermine.habbo.photographer.messages.incoming;

import com.hypermine.habbo.photographer.messages.ServerMessage;
import com.hypermine.habbo.photographer.messages.outgoing.IdkNameComposer;
import com.hypermine.habbo.photographer.util.crypto.HabboEncryption;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */
public class GenerateSecretKeyEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        String prime = this.packet.readString();
        String gen = this.packet.readString();

        String sharedKey = new HabboEncryption().GetSharedKey(prime, gen);

        ctx.sharedKey = sharedKey;

//        ServerMessage msg = new ServerMessage(539);
//        msg.appendString(sharedKey);
//        IdkNameComposer composer = new IdkNameComposer(sharedKey);
//        ctx.writeAndFlush(composer.compose().get());
//        ctx.sendMessage(msg);
    }
}
