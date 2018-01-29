package org.reader;

import org.support.Config;
import org.support.LogCore;
import org.support.Utils;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author wangzhanwei
 */
public class FileChecker extends Thread {
    private String currentLine = "";
    private MainFileManager mainFileManager;
    private RandomAccessFile fileReader;
    boolean run = true;

    public FileChecker(RandomAccessFile fileReader, MainFileManager mainFileManager) {
        this.fileReader = fileReader;
        this.mainFileManager = mainFileManager;
    }

    public FileChecker(MainFileManager mainFileManager) {
        this.mainFileManager = mainFileManager;
    }

    @Override
    public void run() {
        try {
//            StringBuffer stringBuffer = new StringBuffer();
            while (run) {
//                boolean eco = false;
//                while (!eco) {
//                    boolean flag = false;
//                    while (!flag) {
//                        int c = fileReader.read();
//                        switch (c) {
//                            case -1:
//                                flag = true;
//                                eco = true;
//                                break;
//                            default:
//                                stringBuffer.append((char) c);
//                                break;
//                        }
//                    }
//
//                }
//                if (stringBuffer.length() > 0) {
//                    mainFileManager.update(new String(stringBuffer.toString().getBytes("8859_1"), "utf-8"));
//                    stringBuffer.delete(0, stringBuffer.length());
//                }
//                try {
//                    Thread.sleep(Config.PULSE);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                String log;
                while ((log = getNextLog()) != null) {
                    mainFileManager.update(new String(log.getBytes("8859_1"), "utf-8"));
                }
                Thread.sleep(Config.PULSE);
            }
        } catch (Exception e) {
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getNextLog() throws IOException {
        if (!this.isLog(this.currentLine)) {
            while ((this.currentLine = fileReader.readLine()) != null && !this.isLog(this.currentLine)) {
            }
        }
        if (!this.isLog(this.currentLine)) {
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder(this.currentLine).append("\n");
            while ((this.currentLine = fileReader.readLine()) != null && !this.isLog(this.currentLine)) {
                stringBuilder.append(this.currentLine).append("\n");
            }
            return stringBuilder.toString();
        }
    }

    private boolean isLog(String line) {
        return line == null ? false : Utils.logPattern.matcher(line).find();
    }

    public void shutdown() {
        this.run = false;
    }

}
