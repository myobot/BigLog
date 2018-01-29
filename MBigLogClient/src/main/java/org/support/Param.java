package org.support;

/**
 * @author wangzhanwei
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Param {
    private static final String KEY_SINGLE = "KEY_SINGLE_PARAM";
    private final Map<String, Object> datas;

    public Param() {
        this(new Object[0]);
    }

    public Param(Param param) {
        this.datas = new HashMap();
        this.datas.putAll(param.datas);
    }

    public Param(Object... params) {
        this.datas = new HashMap();
        if (params != null && params.length != 0) {
            if (params.length == 1) {
                this.put("KEY_SINGLE_PARAM", params[0]);
            } else {
                int len = params.length;

                for (int i = 0; i < len; i += 2) {
                    String key = (String) params[i];
                    Object val = params[i + 1];
                    this.put(key, val);
                }
            }

        }
    }

    public Param put(String key, Object value) {
        this.datas.put(key, value);
        return this;
    }

    public <K> K get(String key) {
        return (K) this.datas.get(key);
    }

    public <K> K getOrElse(String key, K defaultValue) {
        K result = (K) this.datas.get(key);
        if (result == null) {
            result = defaultValue;
        }

        return result;
    }

    public <K> K get() {
        return this.get("KEY_SINGLE_PARAM");
    }

    public boolean getBoolean(String key) {
        return ((Boolean) this.get(key)).booleanValue();
    }

    public boolean getBoolean() {
        return ((Boolean) this.get()).booleanValue();
    }

    public int getInt(String key) {
        return ((Integer) this.get(key)).intValue();
    }

    public int getInt() {
        return ((Integer) this.get()).intValue();
    }

    public long getLong(String key) {
        return ((Long) this.get(key)).longValue();
    }

    public long getLong() {
        return ((Long) this.get()).longValue();
    }

    public String getString(String key) {
        return (String) this.get(key);
    }

    public String getString() {
        return (String) this.get();
    }

    public <K> K remove(String key) {
        return (K) this.datas.remove(key);
    }

    public int size() {
        return this.datas.size();
    }

    public boolean containsKey(String key) {
        return this.datas.containsKey(key);
    }

    public Object[] toArray() {
        Object[] arr;
        if (this.datas.isEmpty()) {
            arr = new Object[0];
        } else if (this.datas.size() == 1 && this.datas.containsKey("KEY_SINGLE_PARAM")) {
            arr = new Object[]{this.datas.get("KEY_SINGLE_PARAM")};
        } else {
            arr = new Object[this.datas.size() * 2];
            int index = 0;

            Entry e;
            for (Iterator var3 = this.datas.entrySet().iterator(); var3.hasNext(); arr[index++] = e.getValue()) {
                e = (Entry) var3.next();
                arr[index++] = e.getKey();
            }
        }

        return arr;
    }

    public Set<String> keySet() {
        return this.datas.keySet();
    }

    public String toString() {
        return this.datas.toString();
    }

}
