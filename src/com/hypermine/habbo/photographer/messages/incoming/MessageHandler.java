package com.hypermine.habbo.photographer.messages.incoming;

import com.hypermine.habbo.photographer.Session;
import com.hypermine.habbo.photographer.messages.ClientMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */
public abstract class MessageHandler {
    public Session ctx;
    public ClientMessage packet;

    public abstract void handle() throws Exception;
}
