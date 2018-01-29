package org.ui.logic;

import org.support.Config;
import org.support.Utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;

/**
 * @author wangzhanwei
 */
public class LogInfo {
    public String log;
    public int keyIndex;
    public long timestamp;
    public boolean isProtocol;
    public String protocolName;
    public boolean isError;
    public String errorName;

    public LogInfo(String log, int keyIndex) {
        this.log = log;
        this.keyIndex = keyIndex;
        Matcher matcher = Utils.logProtocolPattern.matcher(log);
        if (matcher.find()) {
            this.timestamp = this.getTimestamp(matcher);
            this.isProtocol = true;
            this.protocolName = this.getProtocolName(matcher);
        } else {
            isProtocol = false;
        }
        Matcher matcher1 = Utils.LogErrorPattern.matcher(log);
        if (matcher1.find()) {
            this.isError = true;
            this.errorName = this.getErrorName(matcher1);
        } else {
            isError = false;
        }
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public int getKeyIndex() {
        return keyIndex;
    }

    public void setKeyIndex(int keyIndex) {
        this.keyIndex = keyIndex;
    }

    private long getTimestamp(Matcher matcher) {
        String time = matcher.group(Config.PROTOCOL_TIME_STAMP_INDEX);

        try {
            return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS")).parse(time).getTime();
        } catch (ParseException var4) {
            var4.printStackTrace();
            return 0L;
        }
    }

    private String getProtocolName(Matcher matcher) {
        return matcher.group(Config.PROTOCOL_INDEX);
    }

    private String getErrorName(Matcher matcher) {
        return matcher.group(Config.ERROR_INDEX);
    }
}
