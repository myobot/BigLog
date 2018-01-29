package org.support.observer;

import com.google.protobuf.GeneratedMessage;
import org.connection.ServerLinker;

/**
 * @author wangzhanwei
 */
public class MsgParam extends MsgParamBase {
   /** 发送消息的玩家 */
	private ServerLinker serverLinker;

	public MsgParam(GeneratedMessage msg) {
		super(msg);
	}

    public ServerLinker getServerLinker() {
        return serverLinker;
    }

    public void setServerLinker(ServerLinker serverLinker) {
        this.serverLinker = serverLinker;
    }
}
