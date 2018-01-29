package org.connection.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.support.LogCore;
import org.ui.BigLogClientMainForm;
import java.awt.Color;

/**
 * @author wangzhanwei
 */
public class Client extends Thread {


    int port;
    String host;

    public Client(int port, String host) {

        this.port = port;
        this.host = host;
    }

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 20480)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ChannelHandler[]{new Decoder(), new Encoder(), new ClientHandler()});
                        }
                    });
            b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
            BigLogClientMainForm.mainWindow.getReconnectMessage().setText("连接状态：失败 (请检查ip,port是否输入正确，服务器端服务是否开启，防火墙等)");
            BigLogClientMainForm.mainWindow.getReconnectMessage().setForeground(new Color(255, 0, 6));
            BigLogClientMainForm.mainWindow.getLogArea().setText("连接失败！(请检查ip,port是否输入正确，服务器端服务是否开启，防火墙等) 请在Settings中重新连接");
            BigLogClientMainForm.mainWindow.getAnswerTextPane().setText("连接失败！(请检查ip,port是否输入正确，服务器端服务是否开启，防火墙等) 请在Settings中重新连接");
            BigLogClientMainForm.mainWindow.getLogDetail().setText("连接失败！(请检查ip,port是否输入正确，服务器端服务是否开启，防火墙等) 请在Settings中重新连接");
            BigLogClientMainForm.mainWindow.getErrorPanel().setText("连接失败！(请检查ip,port是否输入正确，服务器端服务是否开启，防火墙等) 请在Settings中重新连接");
            BigLogClientMainForm.frame.setVisible(true);
        } finally {
            group.shutdownGracefully();
        }
    }
}
