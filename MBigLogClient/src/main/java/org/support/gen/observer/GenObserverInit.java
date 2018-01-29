package org.support.gen.observer;

import org.apache.commons.lang3.StringUtils;
import org.support.*;
import org.support.observer.MsgReceiver;
import org.support.gen.*;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.*;
import org.support.observer.Observer;

/**
 * @author wangzhanwei
 */
public class GenObserverInit extends GenBase {
    private String genClassName = "MsgReceiverInit";
    private String ftlFileName = "MsgReceiverInit.ftl";
    private Class<? extends Annotation> annoClass = MsgReceiver.class;

    public GenObserverInit(String packageName, String targetDir, String topPackageName, String genClassName, String ftlFileName, Class<? extends Annotation> annoClass) throws Exception {
        super(packageName, targetDir, topPackageName);
        this.genClassName = genClassName;
        this.ftlFileName = ftlFileName;
        this.annoClass = annoClass;
        this.init();
    }

    private void init() throws Exception {
        canGen = true;
        Map<Class<?>, List<Method>> classes = GenUtils.findMethodNeedToGen(packageName, (Class) null, this.annoClass);
        Iterator iterator = classes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Class<?>, List<Method>> entry = (Map.Entry) iterator.next();
            Class<?> iclass = entry.getKey();
            List<Method> methodList = entry.getValue();
            try {
                this.rootMaps.add(getRootMap(iclass, methodList));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void genFileHandler(Map<String, Object> var1) throws Exception {

    }
    protected void genGlobalFileHandler(Map<String, Object> rootMaps) throws Exception {
        rootMaps.put("rootClassName", this.genClassName);
        GenUtils.freeMarker("./src/main/java/org/support/gen/template/",this.ftlFileName,rootMaps,targetDir, this.genClassName + ".java");
    }

    private Map<String, Object> getRootMap(Class<?> iclass, List<Method> methodList) throws Exception {
        String packageName = iclass.getPackage().getName();
        String className = iclass.getSimpleName();
        Map<String, Object> rootMap = new HashMap();
        List<Map<String, Object>> methodInfos = new ArrayList();
        rootMap.put("packageName", packageName);
        rootMap.put("className", className);
        rootMap.put("methods", methodInfos);
        Iterator var7 = methodList.iterator();

        while (var7.hasNext()) {
            Method m = (Method) var7.next();
            String name = m.getName();
            String callerStr = packageName + "." + className + ":" + name;
            String paramsCall = "";
            String params = "";
            String functionTypes = "";
            boolean hasException = m.getExceptionTypes().length > 0;
            Map<String, Object> method = new LinkedHashMap();
            Map<String, Object> paramInfo = new LinkedHashMap();
            Set<String> keys = this.getListenerKey(m);
            method.put("keys", keys);
            Parameter[] paramList = m.getParameters();

            int j;
            String callerStrTmp;
            String enumCall;
            for (j = 0; j < paramList.length; ++j) {
                callerStrTmp = paramList[j].getType().getCanonicalName();
                enumCall = paramList[j].getName();
                paramInfo.put(enumCall, callerStrTmp);
            }

            j = 0;
            callerStrTmp = "";

            for (Iterator var25 = paramInfo.entrySet().iterator(); var25.hasNext(); ++j) {
                Map.Entry<String, Object> info = (Map.Entry) var25.next();
                String pname = (String) info.getKey();
                String ptype = (String) info.getValue();
                if (j > 0) {
                    params = params + ", ";
                    callerStrTmp = callerStrTmp + ", ";
                    paramsCall = paramsCall + ", ";
                    functionTypes = functionTypes + ", ";
                }

                callerStrTmp = callerStrTmp + ptype;
                paramsCall = paramsCall + pname;
                params = params + ptype.replaceAll("\\[\\]", "...") + " " + pname;
                functionTypes = functionTypes + GenUtils.primitiveTowrapper(ptype);
            }

            callerStr = callerStr + "(" + callerStrTmp + ")";
            if (StringUtils.isNotBlank(functionTypes)) {
                functionTypes = "<" + functionTypes + ">";
            }

            method.put("name", name);
            method.put("params", params);
            method.put("hasException", Boolean.valueOf(hasException));
            method.put("callerStr", callerStr);
            method.put("paramsCall", paramsCall);
            method.put("functionTypes", functionTypes);
            method.put("paramsSize", Integer.valueOf(j));
            enumCall = callerStr.replace("()", "").replace("[]", "s").replaceAll("[.:(,]", "_").replaceAll("[ )]", "").toUpperCase();
            method.put("enumCall", enumCall);
            method.put("enumCallHashCode", String.valueOf(enumCall.hashCode()));
            methodInfos.add(method);
        }

        return rootMap;
    }

    private Set<String> getListenerKey(Method method) throws Exception {
        Annotation anno = method.getAnnotation(this.annoClass);
        Method mKey = this.annoClass.getMethod("value", new Class[0]);
        Object oKey = mKey.invoke(anno, new Object[0]);
        Object[] vKey;
        if (oKey instanceof int[]) {
            int[] keys = (int[]) ((int[]) oKey);
            vKey = new Object[keys.length];

            for (int i = 0; i < keys.length; ++i) {
                vKey[i] = Integer.valueOf(keys[i]);
            }
        } else {
            vKey = (Object[]) ((Object[]) oKey);
        }

        String[] vSubStr = new String[0];
        int[] vSubInt = new int[0];
        long[] vSubLong = new long[0];
        Method[] var9 = this.annoClass.getMethods();
        int var10 = var9.length;

        for (int var11 = 0; var11 < var10; ++var11) {
            Method m = var9[var11];
            String mName = m.getName();
            if ("subStr".equals(mName)) {
                vSubStr = (String[]) ((String[]) m.invoke(anno, new Object[0]));
            } else if ("subInt".equals(mName)) {
                vSubInt = (int[]) ((int[]) m.invoke(anno, new Object[0]));
            } else if ("subLong".equals(mName)) {
                vSubLong = (long[]) ((long[]) m.invoke(anno, new Object[0]));
            }
        }

        int vSubCount = 0;
        if (vSubStr.length > 0) {
            ++vSubCount;
        }

        if (vSubInt.length > 0) {
            ++vSubCount;
        }

        if (vSubLong.length > 0) {
            ++vSubCount;
        }

        if (vSubCount > 1) {
            LogCore.core.error("Observer监听参数设置错误");
            throw new Exception("Observer监听参数设置错误");
        } else {
            String[] vSubKey = getSubKeysFromValue(vSubStr, vSubInt, vSubLong);
            Set<String> results = new HashSet();
            Object[] var26 = vKey;
            int var27 = vKey.length;

            for (int var14 = 0; var14 < var27; ++var14) {
                Object k = var26[var14];
                String[] var16 = vSubKey;
                int var17 = vSubKey.length;

                for (int var18 = 0; var18 < var17; ++var18) {
                    String sk = var16[var18];
                    String smk = Observer.createMethodKey(k, sk);
                    results.add(smk);
                }

                if (vSubKey.length == 0) {
                    String smk = Observer.createMethodKey(k, (Object) null);
                    results.add(smk);
                }
            }

            return results;
        }
    }

    private static String[] getSubKeysFromValue(String[] vSubStr, int[] vSubInt, long[] vSubLong) {
        String[] vSubKey = new String[0];
        int i;
        if (vSubStr.length > 0) {
            vSubKey = new String[vSubStr.length];

            for (i = 0; i < vSubStr.length; ++i) {
                vSubKey[i] = vSubStr[i];
            }
        } else if (vSubInt.length > 0) {
            vSubKey = new String[vSubInt.length];

            for (i = 0; i < vSubInt.length; ++i) {
                vSubKey[i] = String.valueOf(vSubInt[i]);
            }
        } else if (vSubLong.length > 0) {
            vSubKey = new String[vSubLong.length];

            for (i = 0; i < vSubLong.length; ++i) {
                vSubKey[i] = String.valueOf(vSubLong[i]);
            }
        }

        return vSubKey;
    }

    public static void main(String[] args) throws Exception {
        String packageName;
        String targetDir;
        String topPackageName;
        if (args.length < 3) {
            packageName = "org.msglogic";
            targetDir = "./src/main/java/org/gen/";
            topPackageName = "org.gen";
        } else {
            packageName = args[0];
            targetDir = System.getProperty("user.dir") + args[1];
            topPackageName = args[2];
        }
        System.setProperty("logFileName", "GenObserverInit");


        GenBase genMsgReceiverInit = new GenObserverInit(packageName, targetDir, topPackageName,"MsgReceiverInit", "ObserverInit.ftl", MsgReceiver.class);
        if (!genMsgReceiverInit.isCanGen()) {
            System.err.println("不能生成MsgReceiverInit，请检查重试。。");
        } else {
            genMsgReceiverInit.genFiles();
        }
    }
}
