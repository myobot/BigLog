package org.connection.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.awt.Color;
import org.connection.ServerLinker;
import org.gen.MsgTrans;
import org.support.LogCore;
import org.ui.BigLogClientMainForm;
import org.ui.ConstantUI;
import org.ui.logic.MainFormLogic;



/**
 * @author wangzhanwei
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private ServerLinker serverLinker;

    public ClientHandler() {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] msgs = (byte[]) msg;
        serverLinker.onChannelDataComming(msgs);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.serverLinker = new ServerLinker(ctx.channel());
        BigLogClientMainForm.serverLinker = this.serverLinker;
        LogCore.core.info("成功连接服务器！ip.port:", ctx.channel().remoteAddress());
        BigLogClientMainForm.mainWindow.getReconnectMessage().setText("连接状态：成功");
        BigLogClientMainForm.mainWindow.getReconnectMessage().setForeground(new Color(72, 187, 49));
        BigLogClientMainForm.mainWindow.getLogArea().getDocument().remove(0, BigLogClientMainForm.mainWindow.getLogArea().getDocument().getLength());
        BigLogClientMainForm.mainWindow.getAnswerTextPane().setText("");
        BigLogClientMainForm.mainWindow.getLogDetail().setText("");
        BigLogClientMainForm.mainWindow.getErrorPanel().setText("");
        MsgTrans.CSVersionSend.Builder msg = MsgTrans.CSVersionSend.newBuilder();
        msg.setVersion(ConstantUI.APP_VERSION);
        serverLinker.sendMsg(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ctx.close();
        MainFormLogic.linkClose();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
        MainFormLogic.linkClose();
    }


}
