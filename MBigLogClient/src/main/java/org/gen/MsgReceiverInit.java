package org.gen;
import org.support.observer.*;
import org.support.function.*;
import org.support.gen.GenFile;

@GenFile
public final class MsgReceiverInit {
	public static void init(Observer ob) {
		ob.reg("106", (Function1<org.support.observer.MsgParam>)org.msglogic.MsgHandler::onSCFindLogFinish, 1);
		ob.reg("105", (Function1<org.support.observer.MsgParam>)org.msglogic.MsgHandler::onSCFindLogPush, 1);
		ob.reg("101", (Function1<org.support.observer.MsgParam>)org.msglogic.MsgHandler::onSCLogPush, 1);
		ob.reg("102", (Function1<org.support.observer.MsgParam>)org.msglogic.MsgHandler::onSCNewVersionPush, 1);
	}
}

