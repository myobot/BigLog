package org.support.observer;

import org.support.LogCore;
import org.support.function.CommonFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author wangzhanwei
 */
public abstract class Observer {
    private final Map<String,List<CommonFunction>> eventMethod = new HashMap<>();
    public Observer(){}
    public final void reg(String key,Object function,int paramSize){
        List<CommonFunction> commonFunctions = eventMethod.get(key);
        if(null == commonFunctions){
            commonFunctions = new ArrayList<>();
            eventMethod.put(key,commonFunctions);
        }
        commonFunctions.add(new CommonFunction(function,paramSize));
    }
    private void _fireHandler(String fullKey, Object param) {
        List<CommonFunction> funcs = (List)this.eventMethod.get(fullKey);
        if(null != funcs) {
            Iterator iterator = funcs.iterator();

            while(iterator.hasNext()) {
                CommonFunction f = (CommonFunction)iterator.next();
                f.apply(new Object[]{param});
            }
        }

    }

    public final void fireHandler(Object key, Object subKey, Object param) {
        try {
            this._fireHandler(createMethodKey(key, (Object)null), param);
            if(subKey != null) {
                this._fireHandler(createMethodKey(key, subKey), param);
            }

        } catch (Exception e) {
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static String createMethodKey(Object key, Object subKey) {
        String mk = key.toString();
        if(subKey != null && !subKey.toString().equals("")) {
            mk = mk + "$" + subKey.toString();
        }

        return mk;
    }
}
