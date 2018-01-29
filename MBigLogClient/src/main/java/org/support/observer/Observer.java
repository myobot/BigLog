package org.support.observer;

import org.support.LogCore;
import org.support.function.CommonFunction;

import java.util.*;

/**
 * @author wangzhanwei
 */
 @SuppressWarnings("unchecked")
public abstract class Observer {
    private final Map<String, List<CommonFunction>> eventMethod = new HashMap<>();

    public Observer() {
    }

    public final void reg(String key, Object function, int paramSize) {
        List<CommonFunction> commonFunctions = eventMethod.get(key);
        if (null == commonFunctions) {
            commonFunctions = new ArrayList<>();
            eventMethod.put(key, commonFunctions);
        }
        commonFunctions.add(new CommonFunction(function, paramSize));
    }

    private void _fireHandler(String fullKey, Object param) {
        List<CommonFunction> funcs = (List) this.eventMethod.get(fullKey);
        if (null != funcs) {
            Iterator var4 = funcs.iterator();

            while (var4.hasNext()) {
                CommonFunction f = (CommonFunction) var4.next();
                f.apply(new Object[]{param});
            }
        }

    }

    public final void fireHandler(Object key, Object subKey, Object param) {
        try {
            this._fireHandler(createMethodKey(key, (Object) null), param);
            if (subKey != null) {
                this._fireHandler(createMethodKey(key, subKey), param);
            }

        } catch (Exception e) {
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static String createMethodKey(Object key, Object subKey) {
        String mk = key.toString();
        if (subKey != null && !subKey.toString().equals("")) {
            mk = mk + "$" + subKey.toString();
        }

        return mk;
    }
}
