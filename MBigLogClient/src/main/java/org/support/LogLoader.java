package org.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.ui.logic.LogInfo;

/**
 * @author wangzhanwei
 */
public class LogLoader {
    private List<LogInfo> datas = new ArrayList<>();
    private String currentLine = "";
    private List<LogInfo> datasFilter = new ArrayList<>();
    private List<String> filterKeys = new ArrayList();

    public LogLoader(File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String log = null;
            while ((log = getNextLog(bufferedReader)) != null) {
                LogInfo info = new LogInfo(log, 0);
                if (info.isProtocol) {
                    datas.add(info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clean() {
        this.datas.clear();
        this.datasFilter.clear();
        this.filterKeys.clear();
    }

    public boolean isFilter(String context) {
        Iterator var3 = this.filterKeys.iterator();

        while (var3.hasNext()) {
            String key = (String) var3.next();
            if (context.contains(key)) {
                return true;
            }
        }

        return false;
    }

    public void addFilterKey(List<String> keys) {
        Iterator var3 = keys.iterator();

        while (var3.hasNext()) {
            String k = (String) var3.next();
            if (!this.filterKeys.contains(k)) {
                this.filterKeys.add(k);
            }
        }
    }

    public void delFilterKey(List<String> keys) {
        this.filterKeys.removeAll(keys);
    }

    public void filter() {
        datasFilter = datas.stream().filter((data) -> !isFilter(data.protocolName)).collect(Collectors.toList());
    }

    private String getNextLog(BufferedReader bufferedReader) throws IOException {
        if (!this.isLog(this.currentLine)) {
            while ((this.currentLine = bufferedReader.readLine()) != null && !this.isLog(this.currentLine)) {
            }
        }
        if (!this.isLog(this.currentLine)) {
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder(this.currentLine).append("\n");
            while ((this.currentLine = bufferedReader.readLine()) != null && !this.isLog(this.currentLine)) {
                stringBuilder.append(this.currentLine).append("\n");
            }
            return stringBuilder.toString();
        }
    }

    private boolean isLog(String line) {
        return line == null ? false : Utils.logPattern.matcher(line).find();
    }

    public List<LogInfo> getDatas() {
        return datas;
    }

    public String getCurrentLine() {
        return currentLine;
    }

    public List<LogInfo> getDatasFilter() {
        return datasFilter;
    }

    public List<String> getFilterKeys() {
        return filterKeys;
    }
}
