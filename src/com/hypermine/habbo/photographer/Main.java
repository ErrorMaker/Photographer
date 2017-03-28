package com.hypermine.habbo.photographer;

import com.hypermine.habbo.photographer.messages.PacketManager;
import com.hypermine.habbo.photographer.network.GameByteDecoder;
import com.hypermine.habbo.photographer.network.GameClientHandler;
import com.hypermine.habbo.photographer.threading.ThreadPooling;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */
public class Main {
    public static PacketManager packetManager;
    public static ThreadPooling threadPooling;

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        String host = "game-us.habbo.com";
        int port = 38101;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            packetManager = new PacketManager();
            threadPooling = new ThreadPooling(4);

            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.TCP_NODELAY, true);
            b.option(ChannelOption.SO_REUSEADDR, true);
            b.option(ChannelOption.SO_RCVBUF, 5120);
            b.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(5120));
            b.option(ChannelOption.ALLOCATOR, new UnpooledByteBufAllocator(false));
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("logger", new LoggingHandler());
//                    ch.pipeline().addLast("bytesDecoder", new GameByteDecoder());
                    ch.pipeline().addLast(new GameClientHandler());
                    ch.pipeline().addLast("idleHandler", new IdleStateHandler(0,0,60) {
                        @Override
                        protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
                            ctx.close();
                        }
                    });
                }
            });

            ChannelFuture f = b.connect(host, port).sync();

            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
