package org.support.gen;



import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.support.ClassFinder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author wangzhanwei
 */
public class GenUtils {
    public static Map<Class<?>,List<Method>> findMethodNeedToGen(String packageName, Class<? extends Annotation> classAnno, Class<? extends Annotation> methodAnno) throws Exception{
        Map<Class<?>,List<Method>> result = new HashMap<>();
        List<Class<?>> sources = ClassFinder.getAllClass(packageName);
        Iterator<Class<?>> iterator = sources.iterator();
        while(true){
            Class iclass;
            do{
                if(!iterator.hasNext()){
                    return result;
                }
                iclass = iterator.next();
            }while(classAnno != null && !iclass.isAnnotationPresent(classAnno));
            Method[] methods = iclass.getMethods();
            List<Method> methodList = new ArrayList<>();
            int length = methods.length;
            for(int i = 0; i < length; ++i){
                Method method = methods[i];
                if(method.isAnnotationPresent(methodAnno)){
                    methodList.add(method);
                }
            }
            if(!methodList.isEmpty()){
                Collections.sort(methodList,Comparator.comparing(Method::toString));
            }
            result.put(iclass,methodList);
        }
    }
     public static String primitiveTowrapper(String primitive) {
        String wrapper = primitive;
        byte var3 = -1;
        switch(primitive.hashCode()) {
        case -1325958191:
            if(primitive.equals("double")) {
                var3 = 2;
            }
            break;
        case 104431:
            if(primitive.equals("int")) {
                var3 = 0;
            }
            break;
        case 3327612:
            if(primitive.equals("long")) {
                var3 = 1;
            }
            break;
        case 64711720:
            if(primitive.equals("boolean")) {
                var3 = 4;
            }
            break;
        case 97526364:
            if(primitive.equals("float")) {
                var3 = 3;
            }
        }

        switch(var3) {
        case 0:
            wrapper = "Integer";
            break;
        case 1:
            wrapper = "Long";
            break;
        case 2:
            wrapper = "Double";
            break;
        case 3:
            wrapper = "Float";
            break;
        case 4:
            wrapper = "Boolean";
        }

        return wrapper;
    }
    public static void freeMarker(String path, String tempFile, Object rootMap, String targetDir, String targetFile) throws Exception {
        Configuration configuration = new Configuration();
        TemplateLoader loader = new FileTemplateLoader(new File(path));
        configuration.setTemplateLoader(loader);
        configuration.setEncoding(Locale.getDefault(), "UTF-8");
        Template temp = configuration.getTemplate(tempFile, "UTF-8");
        File dir = new File(targetDir);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        String fileFullName = targetDir + targetFile;
        System.out.println("---------开始生成" + fileFullName + "文件......---------");
        File target = new File(fileFullName);
        Writer out = new OutputStreamWriter(new FileOutputStream(target), "UTF-8");
        temp.process(rootMap, out);
        out.flush();
        out.close();
        System.out.println("---------" + targetFile + "文件生成完毕！---------\n");
    }
}
