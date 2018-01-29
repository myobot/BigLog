package org.support.gen.observer;

import java.util.*;

/**
 * @author wangzhanwei
 */
public abstract class GenBase {
    protected String packageName;
    protected String targetDir;
    protected String topPackageName;
    protected List<Map<String, Object>> rootMaps = new ArrayList();
    public boolean canGen;

    public GenBase(String packageName, String targetDir,String topPackageName) {
        this.packageName = packageName;
        this.targetDir = targetDir;
        this.topPackageName = topPackageName;
    }

    public void genFiles() throws Exception {
        if(!this.isCanGen()) {
            System.out.println("代码生成失败，请检查错误后重试！");
        } else {
            this.genGlobalFile();
            Iterator var1 = this.rootMaps.iterator();

            while(var1.hasNext()) {
                Map<String, Object> rootMap = (Map)var1.next();
                this.genFileHandler(rootMap);
            }
        }

    }

    private void genGlobalFile() throws Exception {
        if(!this.rootMaps.isEmpty()) {
            Map<String, Object> data = new HashMap();
            data.put("rootPackageName", this.packageName);
            data.put("methodsList", this.rootMaps);
            data.put("topPackageName",this.topPackageName);
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
