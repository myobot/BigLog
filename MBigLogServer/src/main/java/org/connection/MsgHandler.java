package org.connection;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.GeneratedMessage;
import org.gen.MsgIds;
import org.support.LogCore;
import org.support.Param;
import org.support.Utils;
import org.support.observer.MsgParam;
import org.support.observer.MsgSender;

/**
 * @author wangzhanwei
 */
public class MsgHandler {
	public static boolean handle(byte[] buffer, Object...params) {
		/* 取出消息头 */
		// 消息长度
		@SuppressWarnings("unused")
		int len = Utils.bytesToInt(buffer, 0);
		// 消息ID
		int msgId = Utils.bytesToInt(buffer, 4);

		// 取出消息体
		CodedInputStream in = CodedInputStream.newInstance(buffer, 8, buffer.length - 8);

		try {
			GeneratedMessage msg = MsgIds.parseFrom(msgId, in);
			Param param = new Param(params);
			param.put("msgId",msgId);

			fire(msg,param);
			return true;
		} catch (Exception e) {
			LogCore.core.error(e.getMessage());
			e.printStackTrace();
		}
		return true;
	}
	public static void fire(GeneratedMessage msg,Param param){
		MsgParam msgParam = new MsgParam(msg);
		msgParam.setClientLinker(param.get("clientLinker"));
		MsgSender.fire(param.get("msgId"),msgParam);
	}
}
