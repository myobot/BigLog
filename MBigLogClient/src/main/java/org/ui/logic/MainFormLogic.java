package org.ui.logic;

import java.awt.Color;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import org.support.Config;
import org.support.LogCore;
import org.support.LogLoader;
import org.ui.BigLogClientMainForm;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author wangzhanwei
 */
public class MainFormLogic {
    public static volatile boolean autoUpdateScroll = true;
    public static volatile boolean selectAll = true;
    public static volatile boolean filter = false;
    public static List<Integer> defaultFilters = new ArrayList<Integer>();
    public static List<Integer> noticeDialog = new ArrayList<Integer>();
    public static String[] keys={".*(\\[info\\]).*",".*(\\[error\\]).*",".*(\\[warn\\]).*",".*(exception).*",".*(\\[debug\\]).*",".*(\\[trace\\]).*"};
    public static Pattern[] patterns;
    public static int nowPointIndex = -1;
    private static List<PointInfo> pointInfos = new ArrayList<PointInfo>();
    private static String ipCharge = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    private static Pattern pattern = Pattern.compile(ipCharge);
    private static SimpleAttributeSet defaultAttrset = new SimpleAttributeSet();
    private static SimpleAttributeSet colorAttrset = new SimpleAttributeSet();
    private static  int resetCount = 0;
    private static JTextPane pane = BigLogClientMainForm.mainWindow.getLogArea();
    private static List<LogInfo> logCache = new CopyOnWriteArrayList();
    private static List<LogInfo> errorCache = new CopyOnWriteArrayList<>();
    private static List<LogInfo> datasFilter = new ArrayList();
    private static List<String> filterKeys = new ArrayList();
    private static List<LogInfo> errorDataFilter = new ArrayList();
    private static List<String> errorFilterKeys = new ArrayList<>();
    private static Color[] colors={new Color(72,187,49),new Color(255,0,6),new Color(187,187,35),new Color(0,112,187)};
    private static Color filterWroldColor = new Color(33,66,131);
    private static Color background = new Color(60,63,65);
    private static LogLoader logLoader;
    private static boolean openFile = false;
    @SuppressWarnings("unchecked")
    public static void matchAndChangeColor(String log) {
        int index = -1;
        for (int i = 0; i < keys.length; i++) {
            Matcher m = patterns[i].matcher(log.toLowerCase());
            if (m.find()) {
                index = i;
                break;
            }
        }
        LogInfo info = new LogInfo(log, index);
        logCache.add(info);
        if (info.isProtocol && !MainFormLogic.isFilter(info.protocolName) && !isOpenFile()) {
            MainFormLogic.getDatasFilter().add(info);
            DefaultListModel<String> listModel = (DefaultListModel<String>) BigLogClientMainForm.mainWindow.getListLog().getModel();
            listModel.add(listModel.size(), info.protocolName);
        }
        if (info.isError && !MainFormLogic.isErrorFilter(info.errorName)) {
            errorCache.add(info);
            DefaultListModel<String> listModel = (DefaultListModel<String>) BigLogClientMainForm.mainWindow.getListError().getModel();
            if (info.isError) {
                listModel.add(listModel.size(), info.errorName);
                MainFormLogic.getErrorDataFilter().add(info);
            }
        }
        if (logCache.size() >= Config.MAX_COUNT) {
            cleanCacheTimely();
        }
        if (errorCache.size() >= Config.ERROR_MAX_COUNT) {
            cleanErrorCacheTimely();
        }
        if (MainFormLogic.noticeDialog.contains(index)) {
            ErrorNoticeLogic.openNewDialog(log, index, pane.getDocument().getLength(), MainFormLogic.pointInfos.size());
            MainFormLogic.pointInfos.add(new PointInfo(log, pane.getDocument().getLength()));
        }
        if (MainFormLogic.selectAll) {
            String s = BigLogClientMainForm.mainWindow.getFilterTextField().getText();
            if (s.equals("") || s == null) {
                MainFormLogic.filter = false;
            }
            if (MainFormLogic.filter) {
                filterInsertLine(s, log, index, true);
            } else {
                insertLast(log, index, false);
            }
        } else {
            if (MainFormLogic.defaultFilters.contains(index)) {
                insertLast(log, index, false);
            }
        }
        BigLogClientMainForm.mainWindow.getLogCountLabel().setText(logCache.size() + "");
    }



    public static void insertLast(String line,SimpleAttributeSet simpleAttributeSet){
        Document docs = pane.getDocument();//获得文本对象
        try {
            docs.insertString(docs.getLength(), line,simpleAttributeSet);//对文本进行追加
        } catch (BadLocationException e) {
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        }
        if(MainFormLogic.autoUpdateScroll) {
            pane.setCaretPosition(pane.getDocument().getLength());
        }
    }


    public static void insertLast(String line , int index,boolean filter){
        SimpleAttributeSet simpleAttributeSet = colorAttrset;
        if(index == -1){
            simpleAttributeSet = defaultAttrset;
        }else{
            switch (index){
                case 0:
                StyleConstants.setForeground(simpleAttributeSet, colors[0]);
                break;
            case 1:
                StyleConstants.setForeground(simpleAttributeSet, colors[1]);

                break;
            case 2:
                StyleConstants.setForeground(simpleAttributeSet, colors[2]);

                break;
            case 3:
                StyleConstants.setForeground(simpleAttributeSet, colors[1]);
                break;
            case 4:
                StyleConstants.setForeground(simpleAttributeSet, colors[3]);
            }
        }
        if(filter){
            StyleConstants.setBackground(simpleAttributeSet,filterWroldColor);
        }else{
            StyleConstants.setBackground(simpleAttributeSet,background);
        }
        insertLast(line,simpleAttributeSet);
    }

    public static synchronized void resetText() {
        pane.getUI().getRootView(pane).removeAll();
        Document docs = pane.getDocument();//获得文本对象
        try {
            docs.remove(0, docs.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        int count = 0;

        for (LogInfo LogInfo : logCache) {
            if (MainFormLogic.selectAll) {
                String s = BigLogClientMainForm.mainWindow.getFilterTextField().getText();
                if(s.equals("")|| s == null){
                    MainFormLogic.filter =false;
                }
                if (MainFormLogic.filter) {
                    filterInsertLine(s, LogInfo.log, LogInfo.keyIndex, true);
                } else {
                    insertLast(LogInfo.log, LogInfo.keyIndex, false);
                }
                ++count;
            } else {
                if (MainFormLogic.defaultFilters.contains(LogInfo.keyIndex)) {
                    insertLast(LogInfo.log, LogInfo.keyIndex, false);
                    ++count;
                }
            }
        }
        BigLogClientMainForm.mainWindow.getLogCountLabel().setText(count + "");
    }

    public static void filterInsertLine(String filter,String line,int keyIndex,boolean flag){

        int index = line.indexOf(filter);
        if(index != -1){
            String s1 = line.substring(0,index);
            insertLast(s1,keyIndex,false);
            insertLast(filter,keyIndex,true);
            String s2 = line.substring(index+filter.length(),line.length());
            filterInsertLine(filter,s2,keyIndex,false);
        }else{
            if(!flag){
                insertLast(line,keyIndex,false);
            }
        }
    }
    @SuppressWarnings("unchecked")
    public static void cleanCacheTimely() {
        Iterator<LogInfo> LogInfoIterator = logCache.iterator();
        int countNum = 0;
        int countLength = 0;
        while (LogInfoIterator.hasNext()) {
            LogInfo s = LogInfoIterator.next();
            countLength += s.getLog().length();
            logCache.remove(s);
            countNum++;
            if (countNum >= Config.MIN_COUNT) {
                break;
            }
        }
        Iterator<PointInfo> pointInfoIterator = MainFormLogic.pointInfos.iterator();
        while (pointInfoIterator.hasNext()) {
            PointInfo pointInfo = pointInfoIterator.next();
            pointInfo.setLength(pointInfo.getLength() - countLength);
            if (pointInfo.getLength() < 0) {
                pointInfoIterator.remove();
            }
        }
        MainFormLogic.nowPointIndex = MainFormLogic.pointInfos.size() - 1;
        ErrorNoticeLogic.clearExceptionsId();
        resetText();
        resetCount++;
        BigLogClientMainForm.mainWindow.getAutoCleanLabel().setText(resetCount + "次自动清除");
        if(!isOpenFile()) {
            filter();
            DefaultListModel<String> defaultListModel1 = new DefaultListModel<>();
            for (int i = 0; i < MainFormLogic.getDatasFilter().size(); i++) {
                defaultListModel1.add(i, MainFormLogic.getDatasFilter().get(i).protocolName);
            }
            BigLogClientMainForm.mainWindow.getListLog().setModel(defaultListModel1);
        }
        System.gc();
    }
    @SuppressWarnings("unchecked")
    public static void cleanErrorCacheTimely() {
        Iterator<LogInfo> LogInfoIterator = errorCache.iterator();
        int countNum = 0;
        while (LogInfoIterator.hasNext()) {
            LogInfo s = LogInfoIterator.next();
            errorCache.remove(s);
            countNum++;
            if (countNum >= Config.ERROR_MIN_COUNT) {
                break;
            }
        }
        errorFilter();
        DefaultListModel<String> defaultListModel1 = new DefaultListModel<>();
        for (int i = 0; i < MainFormLogic.getErrorDataFilter().size(); i++) {
            defaultListModel1.add(i, MainFormLogic.getErrorDataFilter().get(i).errorName);
        }
        BigLogClientMainForm.mainWindow.getListError().setModel(defaultListModel1);
        System.gc();
    }

    public static synchronized void cleanCache(){
        logCache.clear();
        MainFormLogic.pointInfos.clear();
        MainFormLogic.nowPointIndex = 0;
        ErrorNoticeLogic.clearExceptionsId();
        pane.setText("");
    }

    public static void jumpToLastPoint() {
        if (nowPointIndex == -1) {
            return;
        }
        --nowPointIndex;
        if (nowPointIndex < pointInfos.size() && nowPointIndex > -1) {
            try {
                BigLogClientMainForm.mainWindow.getLogArea().setCaretPosition(pointInfos.get(nowPointIndex).getLength());
            }catch (Exception e){
                LogCore.core.error(e.getMessage());
                e.printStackTrace();
            }

        }
    }

    public static void jumpToNextPoint() {
        if (nowPointIndex > pointInfos.size() - 2) {
            return;
        }
        ++nowPointIndex;
        try {
            BigLogClientMainForm.mainWindow.getLogArea().setCaretPosition(pointInfos.get(nowPointIndex).getLength());
        }catch (Exception e){
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        }

    }

    public static void jumpToNowPoint(){
        if(nowPointIndex == -1 || nowPointIndex > pointInfos.size() - 1){
            return;
        }
        try {
            BigLogClientMainForm.mainWindow.getLogArea().setCaretPosition(pointInfos.get(nowPointIndex).getLength());
        }catch (Exception e){
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        }

    }

    public static boolean serverSettingCharge(){
        String ip = BigLogClientMainForm.mainWindow.getIpField().getText();
        String sport = BigLogClientMainForm.mainWindow.getPortField().getText();
        Matcher matcher = pattern.matcher(ip);
        if(!matcher.matches()){
            BigLogClientMainForm.mainWindow.getIpErrorLabel().setText("请输入正确的ip地址");
            BigLogClientMainForm.mainWindow.getIpErrorLabel().setVisible(true);
            return false;
        }
        int port = -1;
        try{
            port = Integer.parseInt(sport);
        }catch (Exception e){
            BigLogClientMainForm.mainWindow.getPortErrorLabel().setText("请输入正确的端口号");
            BigLogClientMainForm.mainWindow.getPortErrorLabel().setVisible(true);
            return false;
        }
        if(port == -1 || port > 65535){
            BigLogClientMainForm.mainWindow.getPortErrorLabel().setText("请输入正确的端口号");
            BigLogClientMainForm.mainWindow.getPortErrorLabel().setVisible(true);
            return false;
        }
        return true;
    }
    public static boolean isFilter(String context) {
        if(context == null){
            return true;
        }
        Iterator var3 = filterKeys.iterator();

        while(var3.hasNext()) {
            String key = (String)var3.next();
            if(context.contains(key)) {
                return true;
            }
        }

        return false;
    }
    public static boolean isErrorFilter(String error) {
        if (error == null) {
            return true;
        }
        Iterator var = errorFilterKeys.iterator();
        while (var.hasNext()) {
            String key = (String) var.next();
            if (error.contains(key)) {
                return true;
            }
        }
        return false;
    }
    public static void addErrorFilterKey(List<String> keys){
        Iterator var = keys.iterator();
        while(var.hasNext()){
            String k = (String) var.next();
            if(!errorFilterKeys.contains(k)){
                errorFilterKeys.add(k);
            }
        }
    }

    public static void delErrorFilterKey(List<String> keys){
        errorFilterKeys.removeAll(keys);
    }

    public static void addFilterKey(List<String> keys) {
        Iterator var3 = keys.iterator();

        while(var3.hasNext()) {
            String k = (String)var3.next();
            if(!filterKeys.contains(k)) {
                filterKeys.add(k);
            }
        }
    }

    public static void delFilterKey(List<String> keys) {
        filterKeys.removeAll(keys);
    }
    public static void filter() {
        datasFilter = logCache.stream().filter((data) -> !isFilter(data.protocolName)).collect(Collectors.toList());
    }
    public static void errorFilter(){
        errorDataFilter = errorCache.stream().filter((data) -> !isErrorFilter(data.errorName)).collect(Collectors.toList());
    }
    public static void loadFile(File file){
        logLoader = new LogLoader(file);
        logLoader.filter();
        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
        for(int i=0;i<logLoader.getDatasFilter().size();i++){
            defaultListModel.add(i,logLoader.getDatasFilter().get(i).protocolName);
        }
        BigLogClientMainForm.mainWindow.getListLog().setModel(defaultListModel);
        DefaultListModel<String> defaultListModel1 = new DefaultListModel<>();
        BigLogClientMainForm.mainWindow.getListLogFilter().setModel(defaultListModel1);
    }
    public static void cleanFile(){
        if(logLoader != null){
            logLoader.clean();
            logLoader = null;
        }
    }

    public static void linkClose() {
        BigLogClientMainForm.mainWindow.getReconnectMessage().setText("连接状态：失败 (请检查ip,port是否输入正确，服务器端服务是否开启，防火墙等)");
        BigLogClientMainForm.mainWindow.getReconnectMessage().setForeground(new Color(255, 0, 6));
        BigLogClientMainForm.mainWindow.getLogArea().setText("连接已断开！请在Settings中重新连接");
        BigLogClientMainForm.mainWindow.getAnswerTextPane().setText("连接已断开！请在Settings中重新连接");
        BigLogClientMainForm.mainWindow.getLogDetail().setText("连接已断开！请在Settings中重新连接");
        BigLogClientMainForm.mainWindow.getErrorPanel().setText("连接已断开！请在Settings中重新连接");
        BigLogClientMainForm.mainWindow.getAutoCleanLabel().setText(0 + "次自动清除");
        BigLogClientMainForm.mainWindow.getLogCountLabel().setText("0");
        DefaultListModel<String> model = (DefaultListModel<String>) BigLogClientMainForm.mainWindow.getListLog().getModel();
        model.removeAllElements();
        DefaultListModel<String> model1 = (DefaultListModel<String>) BigLogClientMainForm.mainWindow.getListLogFilter().getModel();
        model1.removeAllElements();
        DefaultListModel<String> model2 = (DefaultListModel<String>) BigLogClientMainForm.mainWindow.getListError().getModel();
        model2.removeAllElements();
        DefaultListModel<String> model3 = (DefaultListModel<String>) BigLogClientMainForm.mainWindow.getListErrorFilter().getModel();
        model3.removeAllElements();
        logCache.clear();
        filterKeys.clear();
        datasFilter.clear();
        errorCache.clear();
        errorFilterKeys.clear();
        errorDataFilter.clear();
        nowPointIndex = -1;
        pointInfos.clear();
        ErrorNoticeLogic.linkClose();
        BigLogClientMainForm.frame.setExtendedState(JFrame.NORMAL);
        BigLogClientMainForm.frame.setVisible(true);
        BigLogClientMainForm.mainWindow.getTabbedPane1().setSelectedIndex(4);
    }

    public static LogLoader getLogLoader() {
        return logLoader;
    }

    public static void setLogLoader(LogLoader logLoader) {
        MainFormLogic.logLoader = logLoader;
    }

    public static List<LogInfo> getDatasFilter() {
        return datasFilter;
    }

    public static List<String> getFilterKeys() {
        return filterKeys;
    }

    public static void setFilterKeys(List<String> filterKeys) {
        MainFormLogic.filterKeys = filterKeys;
    }

    public static List<LogInfo> getErrorDataFilter() {
        return errorDataFilter;
    }

    public static void setErrorDataFilter(List errorDataFilter) {
        MainFormLogic.errorDataFilter = errorDataFilter;
    }

    public static List<String> getErrorFilterKeys() {
        return errorFilterKeys;
    }

    public static void setErrorFilterKeys(List<String> errorFilterKeys) {
        MainFormLogic.errorFilterKeys = errorFilterKeys;
    }

    public static boolean isOpenFile() {
        return openFile;
    }

    public static void setOpenFile(boolean openFile) {
        MainFormLogic.openFile = openFile;
    }
}
