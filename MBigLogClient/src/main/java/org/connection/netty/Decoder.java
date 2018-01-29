package org.connection.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author wangzhanwei
 */
public class Decoder extends LengthFieldBasedFrameDecoder{
   public Decoder() {
        super(2147483647, 0, 4, -4, 0);
    }

    protected Object decode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        ByteBuf buffs;
        if((buffs = (ByteBuf)super.decode(ctx, buf)) == null) {
            return null;
        } else {
            byte[] decoded = new byte[buffs.readableBytes()];
            buffs.readBytes(decoded);
            buffs.release();
            return decoded;
        }
    }
}
