package ${packageName};

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
	<#list messageInfos as message>
	public static final int ${message.name} = ${message.id?c};
	</#list>
	
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
		<#list messageInfos as message>
		classToId.put(${message.className}.${message.name}.class, ${message.name});
		</#list>
	}
	
	/**
	 * 初始化消息ID与消息CLASS的对应关系
	 */
	private static void initIdToClass() {
		<#list messageInfos as message>
		idToClass.put(${message.name},${message.className}.${message.name}.class);
		</#list>
	}
	/**
	 * 根据消息id解析消息
	 */
	public static GeneratedMessage parseFrom(int msgId, CodedInputStream s) throws IOException{
		switch (msgId) {
		<#list messageInfos as message>
			case ${message.name}:
				return ${message.className}.${message.name}.parseFrom(s);
		</#list>
		}
		return null;
	}
}

