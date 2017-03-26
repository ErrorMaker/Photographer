package com.hypermine.habbo.photographer.threading.runnables;

import com.hypermine.habbo.photographer.Main;
import com.hypermine.habbo.photographer.Session;
import com.hypermine.habbo.photographer.messages.ClientMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */
public class ChannelReadHandler implements Runnable {
    private final ChannelHandlerContext ctx;
    private final Object msg;
    private final Session s;

    public ChannelReadHandler(ChannelHandlerContext ctx, Object msg)
    {
        this.ctx = ctx;
        this.msg = msg;
        this.s = (Session) ctx.attr(AttributeKey.valueOf("session")).get();
    }

    public void run()
    {
        ByteBuf m = (ByteBuf) msg;
        int length = m.readInt();
        short header = m.readShort();
        ByteBuf body = Unpooled.wrappedBuffer(m.readBytes(m.readableBytes()));
        Main.packetManager.handlePacket(s, new ClientMessage(header, body));
    }
}
