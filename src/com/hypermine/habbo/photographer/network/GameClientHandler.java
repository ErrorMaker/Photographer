package com.hypermine.habbo.photographer.network;

import com.hypermine.habbo.photographer.Main;
import com.hypermine.habbo.photographer.Session;
import com.hypermine.habbo.photographer.messages.ServerMessage;
import com.hypermine.habbo.photographer.messages.outgoing.IdkNameComposer;
import com.hypermine.habbo.photographer.messages.outgoing.InitCryptoComposer;
import com.hypermine.habbo.photographer.messages.outgoing.ReleaseVersionComposer;
import com.hypermine.habbo.photographer.threading.runnables.ChannelReadHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */

@ChannelHandler.Sharable
public class GameClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        System.out.println("Connected, I think?");

        Session s = new Session(ctx.channel());
        ctx.attr(AttributeKey.valueOf("session")).set(s);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        s.sendMessage(new ReleaseVersionComposer().compose());
                        s.sendMessage(new InitCryptoComposer().compose());
                    }
                },
                2000
        );

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        s.sendMessage(new IdkNameComposer(s.sharedKey).compose());
                    }
                }, 6000
        );
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            Main.threadPooling.run(new ChannelReadHandler(ctx, msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
