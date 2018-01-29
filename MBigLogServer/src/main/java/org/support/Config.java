package org.support;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author wangzhanwei
 */
public class Config {
    public static final long SEC = 1000L;
    public static final long MIN = 60000L;
    public static final long HOUR = 3600000L;
    public static final long DAY = 86400000L;
    public static final long YEAR = 31536000000L;
    public static int PORT;
    public static String PATH;
    public static String CHECK_ANNOATION_DELAY;
    public static long PULSE = 20;
    public static long checkAnnoationDelay;
    public static Properties properties = new Properties();

    public static boolean loadConfig() {
        try {
            properties.load(new InputStreamReader(new FileInputStream("config.properties"), "GBK"));
            PORT = Integer.parseInt(properties.getProperty("port"));
            PATH = properties.getProperty("path");
            CHECK_ANNOATION_DELAY = properties.getProperty("checkAnnoationDelay");
            PULSE = Long.parseLong(properties.getProperty("pulse"));
            String[] s = CHECK_ANNOATION_DELAY.split("_");
            if (s.length != 2) {
                return false;
            }
            long value = Long.parseLong(s[0]);
            switch (s[1].toLowerCase()) {
                case "ms":
                    checkAnnoationDelay = value;
                    break;
                case "sec":
                    checkAnnoationDelay = value * SEC;
                    break;
                case "min":
                    checkAnnoationDelay = value * MIN;
                    break;
                case "hour":
                    checkAnnoationDelay = value * HOUR;
                    break;
                case "day":
                    checkAnnoationDelay = value * DAY;
                    break;
                case "year":
                    checkAnnoationDelay = value * YEAR;
                    break;
                default:
                    checkAnnoationDelay = 5 * MIN;
                    break;
            }
            return true;

        } catch (Exception e) {
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
