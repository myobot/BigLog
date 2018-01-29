package org.connection.netty;


import org.connection.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.reader.MainFileManager;
import org.support.Config;
import org.support.LogCore;

/**
 * @author wangzhanwei
 */
public class Server extends Thread {
    private MainFileManager mainFileManager;
    public Server(MainFileManager mainFileManager){
        this.mainFileManager = mainFileManager;
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 20480)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            LogCore.core.info("new connect ip={} port={}",ch.remoteAddress().getAddress().getHostAddress(),ch.remoteAddress().getPort());
                            System.out.println("new connect ip={} port={}"+ch.remoteAddress().getAddress().getHostAddress()+ch.remoteAddress().getPort());
                            p.addLast(new ChannelHandler[]{new Decoder(),new Encoder(),new ServerHandler(mainFileManager)});
                        }
                    });

            // 启动
			Channel ch = b.bind(Config.PORT).sync().channel();
			ch.closeFuture().sync();
        }catch (Exception e){

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
