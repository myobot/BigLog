package org.support.observer;

import com.google.protobuf.GeneratedMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzhanwei
 */
public class MsgParamBase {
    private GeneratedMessage msg;
    private Map<String, Object> param = new HashMap();

    public MsgParamBase(GeneratedMessage msg) {
        this.msg = msg;
    }

    public <T> T getMsg() {
        return (T) this.msg;
    }

    public void setMsg(GeneratedMessage msg) {
        this.msg = msg;
    }

    public <T> T get(String key) {
        return (T) this.param.get(key);
    }

    public void put(String key, Object value) {
        this.param.put(key, value);
    }
}
