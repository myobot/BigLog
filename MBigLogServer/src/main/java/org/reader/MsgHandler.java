package org.reader;

import org.connection.ClientLinker;
import org.gen.MsgIds;
import org.gen.MsgTrans;
import org.support.observer.MsgParam;
import org.support.observer.MsgReceiver;

/**
 * @author wangzhanwei
 */
public class MsgHandler {
    @MsgReceiver(MsgIds.CSVersionSend)
    public static void onVersionSend(MsgParam msg){
//        MsgTrans.CSVersionSend csVersionSend = msg.getMsg();
//        ClientLinker clientLinker = msg.getClientLinker();
//        clientLinker.setVersion(csVersionSend.getVersion());
//        if(!csVersionSend.getVersion().equals(VersionChecker.version)) {
//            MsgTrans.SCNewVersionPush.Builder msg1 = MsgTrans.SCNewVersionPush.newBuilder();
//            msg1.setAnnotation(VersionChecker.annotation);
//            msg1.setVersion(VersionChecker.version);
//            clientLinker.sendMsg(msg1);
//        }
    }
    @MsgReceiver(MsgIds.CSFindLog)
    public static void onCSFindLog(MsgParam msg){
        MsgTrans.CSFindLog csFindLog = msg.getMsg();
        ClientLinker clientLinker = msg.getClientLinker();
        LogFinder logFinder = new LogFinder(csFindLog.getId(),csFindLog.getOrderModel(),csFindLog.getSentence(),csFindLog.getCount(),csFindLog.getTotalCount(),csFindLog.getMatchCase(),csFindLog.getWholeWords(),csFindLog.getShowRowNum(),csFindLog.getUsePattern(),clientLinker);
        logFinder.setName("LogFinder");
        logFinder.start();
    }
}
