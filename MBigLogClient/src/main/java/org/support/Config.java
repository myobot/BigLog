package org.support;

import java.io.*;
import java.util.Properties;
import java.util.regex.Pattern;
import sun.security.krb5.internal.PAData;

/**
 * @author wangzhanwei
 */
public class Config {
    public static int MAX_COUNT;
    public static int MIN_COUNT;
    public static int PORT;
    public static String IP;
    public static int CLOSE_MODEL;
    public static int NO_MORE_NOTICE;
    public static String END_FLAG;
    public static Properties properties = new Properties();
    public static InputStream in;
    public static OutputStream out;
    public static int ERROR_MAX_COUNT;
    public static int ERROR_MIN_COUNT;
    public static String LOG_PATTERN;
    public static String LOG_PROTOCOL_PATTERN;
    public static int PROTOCOL_TIME_STAMP_INDEX;
    public static int PROTOCOL_INDEX;
    public static String LOG_ERROR_PATTERN;
    public static int ERROR_INDEX;

    public static boolean loadConfig() {
        try {
            properties = new Properties();
            properties.load(new InputStreamReader(new FileInputStream("config.properties"), "GBK"));
            MAX_COUNT = Integer.parseInt(properties.getProperty("maxCount"));
            MIN_COUNT = Integer.parseInt(properties.getProperty("minCount"));
            PORT = Integer.parseInt(properties.getProperty("port"));
            IP = properties.getProperty("ip");
            CLOSE_MODEL = Integer.parseInt(properties.getProperty("closeModel"));
            NO_MORE_NOTICE = Integer.parseInt(properties.getProperty("nomoreNotice"));
            END_FLAG = properties.getProperty("endFlag");
            ERROR_MAX_COUNT = Integer.parseInt(properties.getProperty("errorMaxCount"));
            ERROR_MIN_COUNT = Integer.parseInt(properties.getProperty("errorMinCount"));
            LOG_PATTERN = properties.getProperty("logPattern");
            LOG_PROTOCOL_PATTERN = properties.getProperty("logProtocolPattern");
            LOG_ERROR_PATTERN = properties.getProperty("LogErrorPattern");
            PROTOCOL_TIME_STAMP_INDEX = Integer.parseInt(properties.getProperty("protocolTimestampIndex"));
            PROTOCOL_INDEX = Integer.parseInt(properties.getProperty("protocolIndex"));
            ERROR_INDEX = Integer.parseInt(properties.getProperty("errorIndex"));
            Utils.logPattern = Pattern.compile(LOG_PATTERN);
            Utils.logProtocolPattern = Pattern.compile(LOG_PROTOCOL_PATTERN);
            Utils.LogErrorPattern = Pattern.compile(LOG_ERROR_PATTERN);
            return true;
        } catch (FileNotFoundException e) {
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static void setProperties(String key, String value) {
        properties.setProperty(key, value);
    }

    public static void store(String common) {
        try {
            out = new BufferedOutputStream(new FileOutputStream("config.properties"));
            properties.store(out, common);
        } catch (Exception e) {
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
