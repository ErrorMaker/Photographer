package com.hypermine.habbo.photographer.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */
public class GameByteDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        in.markReaderIndex();

        if (in.readableBytes() > 4)
        {
            in.resetReaderIndex();
            return;
        }

        int length = in.readInt();

        if (in.readableBytes() < length || length < 0)
        {
            in.resetReaderIndex();
            return;
        }

        in.resetReaderIndex();
        ByteBuf read = in.readBytes(length + 4);
        out.add(read);
    }
}
