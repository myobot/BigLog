package org.gen;
import org.support.observer.*;
import org.support.function.*;
import org.support.gen.GenFile;

@GenFile
public final class MsgReceiverInit {
	public static void init(Observer ob) {
		ob.reg("104", (Function1<org.support.observer.MsgParam>)org.reader.MsgHandler::onCSFindLog, 1);
		ob.reg("103", (Function1<org.support.observer.MsgParam>)org.reader.MsgHandler::onVersionSend, 1);
	}
}

