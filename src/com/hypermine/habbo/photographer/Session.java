package com.hypermine.habbo.photographer;

import com.hypermine.habbo.photographer.messages.ServerMessage;
import com.hypermine.habbo.photographer.util.crypto.HabboEncryption;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
        ByteBuf buffer = msg.get().copy();
        byte[] message = new byte[buffer.readableBytes()];
        int i = 0;
        while (buffer.isReadable()) {
            message[i]= buffer.readByte();
            i++;
        }
        if (habboEncryption.canEncrypt) {
            ByteBuf crypted = Unpooled.buffer();
            crypted.writeBytes(habboEncryption.rc4.parse(message));
            ch.write(crypted);
        } else {
            ch.write(msg.get());
        }
        ch.flush();
    }
}
