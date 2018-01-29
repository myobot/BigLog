package org.support.observer;

/**
 * @author wangzhanwei
 */
public class MsgSender extends Observer {
    public static final MsgSender instance = new MsgSender();

    public MsgSender() {
    }

    public static void fire(int msgId, MsgParamBase param) {
        instance.fireHandler(Integer.valueOf(msgId),null, param);
    }
}
