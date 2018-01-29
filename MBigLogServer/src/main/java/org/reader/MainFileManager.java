package org.reader;

import org.connection.ClientLinker;
import org.connection.netty.Server;
import org.gen.MsgReceiverInit;
import org.support.Config;
import org.support.LogCore;
import org.support.observer.MsgSender;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author wangzhanwei
 */
public class MainFileManager{

    private ConcurrentMap<Long, ClientLinker> linkers = new ConcurrentHashMap<Long, ClientLinker>();

    private FileChecker fileChecker;

    private RandomAccessFile fileReaderForFileChecker;

    public MainFileManager(){
        try {
            fileReaderForFileChecker = new RandomAccessFile(new File(Config.PATH), "r");
        }catch (Exception e){
            LogCore.core.error("服务器启动失败 error={}",e.getMessage());
            e.printStackTrace();
        }
    }

    public void addLinker(long id,ClientLinker clientLinker){
        this.linkers.put(id,clientLinker);
    }

    public void delLinker(long id){
        this.linkers.remove(id);
    }


    public void startup(){
        if(!Config.loadConfig()){
            LogCore.core.error("服务器配置错误");
            System.out.println("服务器配置错误");
            return;
        }
        Server server = new Server(this);
        server.start();
        try {
            long length = fileReaderForFileChecker.length();
            fileReaderForFileChecker.seek(length);

            fileChecker = new FileChecker(fileReaderForFileChecker,this);
            fileChecker.setName("FileChecker");
            fileChecker.start();
            System.out.println("BigLogServer Started!");

        }catch (Exception e){
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        }
    }



    public void update(String line) {
        for(ClientLinker linker:linkers.values()){
            linker.sendLog(line);
        }
    }


    public static void main(String args[]){
        Config.loadConfig();
        MainFileManager mainFileManager = new MainFileManager();
        MsgReceiverInit.init(MsgSender.instance);
        mainFileManager.startup();
        AnnotationChecker versionChecker = new AnnotationChecker(mainFileManager.linkers);
        versionChecker.setName("VersionChecker");
        versionChecker.start();
    }
}
