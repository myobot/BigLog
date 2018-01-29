package org.support.exception;

import org.apache.logging.log4j.message.ParameterizedMessage;

/**
 * @author wangzhanwei
 */
public class SysException extends Throwable {
    public SysException(Exception e,String msg, Object... params){
        super(ParameterizedMessage.format(msg,params),e);
    }
}
