package com.hypermine.habbo.photographer;

import com.hypermine.habbo.photographer.messages.ServerMessage;
import io.netty.channel.Channel;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/26/2017.
 */
public class Session {
    public String prime;
    public String gen;
    public Channel ch;
    public String sharedKey;

    public Session(Channel ch) {
        this.ch = ch;
    }

    public void sendMessage(ServerMessage msg) {
        System.out.println(String.format("OUTGOING [%d] - %s", msg.getHeader(), msg.getBodyString()));
        ch.write(msg.get());
        ch.flush();
    }
}
