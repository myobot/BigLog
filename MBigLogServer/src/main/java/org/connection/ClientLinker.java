package org.connection;

import com.google.protobuf.Message;
import org.gen.MsgIds;
import org.gen.MsgTrans;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.reader.MainFileManager;
import org.support.observer.IDApplyer;

/**
 * @author wangzhanwei
 */
public class ClientLinker {

    private final Channel channel;
    private MainFileManager mainFileManager;
    private long id;
    private String version = "";
    private String annotation = "";

    public ClientLinker(Channel channel,MainFileManager mainFileManager) {
        this.channel = channel;
        this.mainFileManager = mainFileManager;
        this.id = IDApplyer.applyId();
        mainFileManager.addLinker(id,this);
    }

    public void sendLog(String line){
        if(!channel.isActive()) {
			return;
		}

		if(!channel.isWritable()) {
			return;
		}
        MsgTrans.SCLogPush.Builder msg = MsgTrans.SCLogPush.newBuilder();
        msg.setLine(line);
        int msgId = MsgIds.getIdByClass(MsgTrans.SCLogPush.class);
        Chunk msgbuf = new Chunk(msg);

        ByteBuf byteBuf = this.channel.alloc().buffer(msgbuf.length+8);
        byteBuf.writeInt(msgbuf.length+8);
        byteBuf.writeInt(msgId);
        byteBuf.writeBytes(msgbuf.buffer);
        this.channel.write(byteBuf);

        channel.flush();

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
    public void sendMsg(int msgId,Chunk msgbuf) {
        if (!channel.isActive()) {
            return;
        }

        if (!channel.isWritable()) {
            return;
        }
        ByteBuf byteBuf = this.channel.alloc().buffer(msgbuf.length+8);
        byteBuf.writeInt(msgbuf.length+8);
        byteBuf.writeInt(msgId);
        byteBuf.writeBytes(msgbuf.buffer);
        this.channel.write(byteBuf);
        this.channel.flush();
    }

    public void onChannelClosed(){
        mainFileManager.delLinker(id);
    }

    public void onChannelDataComing(byte[] bytes){
        MsgHandler.handle(bytes,"clientLinker",this);
    }

    public Channel getChannel() {
        return channel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
}
