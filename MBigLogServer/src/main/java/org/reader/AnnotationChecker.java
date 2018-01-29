package org.reader;

import org.connection.ClientLinker;
import org.gen.MsgTrans;
import org.support.Config;
import org.support.LogCore;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentMap;

/**
 * @author wangzhanwei
 */
public class AnnotationChecker extends Thread {
    private boolean run = true;
    public static String annotation;
    private Properties properties = new Properties();
    private ConcurrentMap<Long, ClientLinker> linkers;
    public AnnotationChecker(ConcurrentMap<Long, ClientLinker> linkers){
        this.linkers = linkers;
    }
    @Override
    public void run() {
        try {
            properties.load(new InputStreamReader(new FileInputStream("annotation.properties"), "GBK"));
            annotation = properties.getProperty("annotation");
            Thread.sleep(Config.checkAnnoationDelay);
            while (run) {
                properties.load(new InputStreamReader(new FileInputStream("annotation.properties"), "GBK"));
                String newAnnotation = properties.getProperty("annotation");
                if (!annotation.equals(newAnnotation)){
                    MsgTrans.SCNewVersionPush.Builder msg = MsgTrans.SCNewVersionPush.newBuilder();
                    msg.setAnnotation(newAnnotation);
                    msg.setVersion(newAnnotation);
                    Iterator<Map.Entry<Long, ClientLinker>> iterator = linkers.entrySet().iterator();
                    while(iterator.hasNext()){
                        Map.Entry<Long,ClientLinker> entry = iterator.next();
                        ClientLinker clientLinker = entry.getValue();
                        if(!clientLinker.getVersion().equals(newAnnotation)){
                            clientLinker.sendMsg(msg);
                        }
                    }
                    annotation = newAnnotation;
                    System.out.println("send new annotation");
                }
                Thread.sleep(Config.checkAnnoationDelay);
            }
        } catch (Exception e) {
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
