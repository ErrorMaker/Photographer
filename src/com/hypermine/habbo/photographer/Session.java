package com.hypermine.habbo.photographer;

import com.hypermine.habbo.photographer.messages.ServerMessage;
import com.hypermine.habbo.photographer.util.crypto.HabboEncryption;
import io.netty.channel.Channel;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/26/2017.
 */
public class Session {
    public HabboEncryption habboEncryption = new HabboEncryption();
    public Channel ch;

    public Session(Channel ch) {
        this.ch = ch;
    }

    public void sendMessage(ServerMessage msg) {
        System.out.println(String.format("OUTGOING [%d] - %s", msg.getHeader(), msg.getBodyString()));
        if (habboEncryption.canEncrypt) {
            ch.write(habboEncryption.rc4.decipher(msg.get()));
        } else {
            ch.write(msg.get());
        }
        ch.flush();
    }
}
