package org.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.reader.MainFileManager;
import org.support.LogCore;

/**
 * @author wangzhanwei
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private MainFileManager mainFileManager;
    // 当前连接的客户端
    private ClientLinker linker;

    public ServerHandler(MainFileManager mainFileManager){
        this.mainFileManager = mainFileManager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        super.channelActive(ctx);
        Channel channel = ctx.channel();
        linker = new ClientLinker(channel,mainFileManager);
    }
    @Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        linker.onChannelClosed();
        System.out.println(ctx.channel().remoteAddress()+"断开连接");
    }
    @Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            byte[] msgs = (byte[]) msg;
            linker.onChannelDataComing(msgs);
        }catch (Exception e){
            LogCore.core.error(e.getMessage());
            linker.onChannelClosed();
            e.printStackTrace();
        }
    }
    @Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LogCore.core.error("发生异常：cause={} ",cause);
        System.out.println("发生异常"+cause.getMessage());
        linker.onChannelClosed();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }
}
