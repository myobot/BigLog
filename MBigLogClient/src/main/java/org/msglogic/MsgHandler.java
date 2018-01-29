package org.msglogic;

import org.connection.ServerLinker;
import org.gen.MsgIds;
import org.gen.MsgTrans;
import org.support.observer.MsgParam;
import org.support.observer.MsgReceiver;
import org.ui.ConstantUI;
import org.ui.logic.FinderLogic;
import org.ui.logic.NewVersionLogic;

/**
 * @author wangzhanwei
 */
public class MsgHandler {
    @MsgReceiver(MsgIds.SCLogPush)
    public static void onSCLogPush(MsgParam msgParam) {
        MsgTrans.SCLogPush msg = msgParam.getMsg();
        ServerLinker serverLinker = msgParam.getServerLinker();
        serverLinker.onLogDataComing(msg.getLine());
    }

    @MsgReceiver(MsgIds.SCNewVersionPush)
    public static void onSCNewVersionPush(MsgParam msgParam) {
        MsgTrans.SCNewVersionPush msg = msgParam.getMsg();
        String newVersion = msg.getVersion();
        if (!newVersion.equals(ConstantUI.APP_VERSION)) {
            NewVersionLogic.openNewDialog(msg.getAnnotation());
        }
    }

    @MsgReceiver(MsgIds.SCFindLogPush)
    public static void onSCFindLogPush(MsgParam msgParam) {
        MsgTrans.SCFindLogPush msg = msgParam.getMsg();
        long id = msg.getId();
        String answer = msg.getAnswer();
        FinderLogic.onSCFindLogPush(id, answer);
    }

    @MsgReceiver(MsgIds.SCFindLogFinish)
    public static void onSCFindLogFinish(MsgParam msgParam) {
        MsgTrans.SCFindLogFinish msg = msgParam.getMsg();
        FinderLogic.onSCFindLogFinish(msg.getId());
    }
}
