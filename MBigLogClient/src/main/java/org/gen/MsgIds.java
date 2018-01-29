package org.gen;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;
import org.support.LogCore;
import org.support.gen.GenFile;

@GenFile
public class MsgIds {
	public static final int SCLogPush = 101;
	public static final int SCNewVersionPush = 102;
	public static final int CSVersionSend = 103;
	public static final int CSFindLog = 104;
	public static final int SCFindLogPush = 105;
	public static final int SCFindLogFinish = 106;
	
	//消息CLASS与消息ID的对应关系<消息class, 消息ID>
	private static final Map<Class<? extends Message>, Integer> classToId = new HashMap<Class<? extends Message>, Integer>();
	//消息ID与消息CLASS的对应关系<消息ID, 消息class>
	private static final Map<Integer, Class<? extends Message>> idToClass = new HashMap<Integer, Class<? extends Message>>();
	
	static {
		//初始化消息CLASS与消息ID的对应关系
		initClassToId();
		//初始化消息ID与消息CLASS的对应关系
		initIdToClass();
	}
	
	/**
	 * 获取消息ID
	 * @param clazz
	 * @return
	 */
	public static int getIdByClass(Class<? extends Message> clazz) {
		return classToId.get(clazz);
	}
	
	/**
	 * 获取消息CLASS
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getClassById(int msgId) {
		return (T) idToClass.get(msgId);
	}
	
	/**
	 * 获取消息名称
	 * @param clazz
	 * @return
	 */
	public static String getNameById(int msgId) {
		try {
			return idToClass.get(msgId).getSimpleName();
		} catch (Exception e) {
			LogCore.core.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 初始化消息CLASS与消息ID的对应关系
	 */
	private static void initClassToId() {
		classToId.put(MsgTrans.SCLogPush.class, SCLogPush);
		classToId.put(MsgTrans.SCNewVersionPush.class, SCNewVersionPush);
		classToId.put(MsgTrans.CSVersionSend.class, CSVersionSend);
		classToId.put(MsgTrans.CSFindLog.class, CSFindLog);
		classToId.put(MsgTrans.SCFindLogPush.class, SCFindLogPush);
		classToId.put(MsgTrans.SCFindLogFinish.class, SCFindLogFinish);
	}
	
	/**
	 * 初始化消息ID与消息CLASS的对应关系
	 */
	private static void initIdToClass() {
		idToClass.put(SCLogPush,MsgTrans.SCLogPush.class);
		idToClass.put(SCNewVersionPush,MsgTrans.SCNewVersionPush.class);
		idToClass.put(CSVersionSend,MsgTrans.CSVersionSend.class);
		idToClass.put(CSFindLog,MsgTrans.CSFindLog.class);
		idToClass.put(SCFindLogPush,MsgTrans.SCFindLogPush.class);
		idToClass.put(SCFindLogFinish,MsgTrans.SCFindLogFinish.class);
	}
	/**
	 * 根据消息id解析消息
	 */
	public static GeneratedMessage parseFrom(int msgId, CodedInputStream s) throws IOException{
		switch (msgId) {
			case SCLogPush:
				return MsgTrans.SCLogPush.parseFrom(s);
			case SCNewVersionPush:
				return MsgTrans.SCNewVersionPush.parseFrom(s);
			case CSVersionSend:
				return MsgTrans.CSVersionSend.parseFrom(s);
			case CSFindLog:
				return MsgTrans.CSFindLog.parseFrom(s);
			case SCFindLogPush:
				return MsgTrans.SCFindLogPush.parseFrom(s);
			case SCFindLogFinish:
				return MsgTrans.SCFindLogFinish.parseFrom(s);
		}
		return null;
	}
}

