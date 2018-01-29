package org.support.observer;

import com.google.protobuf.GeneratedMessage;
import org.connection.ClientLinker;

/**
 * @author wangzhanwei
 */
public class MsgParam extends MsgParamBase {
   /** 发送消息的玩家 */
	private ClientLinker clientLinker;

	public MsgParam(GeneratedMessage msg) {
		super(msg);
	}

    public ClientLinker getClientLinker() {
        return clientLinker;
    }

    public void setClientLinker(ClientLinker clientLinker) {
        this.clientLinker = clientLinker;
    }
}
