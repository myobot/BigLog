package org.connection;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.gen.MsgIds;
import org.support.Chunk;
import org.ui.logic.MainFormLogic;

/**
 * @author wangzhanwei
 */
public class ServerLinker {
    private final Channel channel;


    public ServerLinker(Channel channel) {
        this.channel = channel;
    }

    public void close() {
        channel.close();
    }

    public void onChannelDataComming(byte[] bytes) {
        MsgHandler.handle(bytes, "serverLinker", this);
    }

    public void onLogDataComing(String log) {
        MainFormLogic.matchAndChangeColor(log);
    }

    public void sendMsg(Message.Builder builder) {
        if (builder != null) {
            sendMsg(builder.build());
        }
    }

    public void sendMsg(Message msg) {
        if (msg != null) {
            int msgId = MsgIds.getIdByClass(msg.getClass());
            sendMsg(msgId, new Chunk(msg));
        }
    }

    public void sendMsg(int msgId, Chunk msgbuf) {
        if (!channel.isActive()) {
            return;
        }

        if (!channel.isWritable()) {
            return;
        }
        ByteBuf byteBuf = this.channel.alloc().buffer(msgbuf.length + 8);
        byteBuf.writeInt(msgbuf.length + 8);
        byteBuf.writeInt(msgId);
        byteBuf.writeBytes(msgbuf.buffer);
        this.channel.write(byteBuf);
        this.channel.flush();
    }


}
