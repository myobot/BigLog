package org.support.gen.observer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author wangzhanwei
 */
public abstract class GenBase {
    protected String packageName;
    protected String targetDir;
    protected List<Map<String, Object>> rootMaps = new ArrayList();
    public boolean canGen;

    public GenBase(String packageName, String targetDir) {
        this.packageName = packageName;
        this.targetDir = targetDir;
    }

    public void genFiles() throws Exception {
        if (!this.isCanGen()) {
            System.out.println("代码生成失败，请检查错误后重试！");
        } else {
            this.genGlobalFile();
            Iterator var1 = this.rootMaps.iterator();

            while (var1.hasNext()) {
                Map<String, Object> rootMap = (Map) var1.next();
                this.genFileHandler(rootMap);
            }
        }

    }

    private void genGlobalFile() throws Exception {
        if (!this.rootMaps.isEmpty()) {
            Map<String, Object> data = new HashMap();
            data.put("rootPackageName", this.packageName);
            data.put("methodsList", this.rootMaps);
            this.genGlobalFileHandler(data);
        }
    }

    protected void genGlobalFileHandler(Map<String, Object> rootMaps) throws Exception {
    }

    protected abstract void genFileHandler(Map<String, Object> var1) throws Exception;

    public boolean isCanGen() {
        return this.canGen;
    }
}
