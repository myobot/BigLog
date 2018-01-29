package org.ui;

import com.install4j.api.launcher.ApplicationLauncher;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import org.connection.netty.Client;
import org.connection.ServerLinker;
import org.support.Config;
import org.support.LogCore;
import org.ui.listener.MainFrameListener;
import org.ui.listener.MainPaneListener;
import org.ui.listener.MainSrollPaneListener;
import org.ui.listener.TrayIconListener;

/**
 * @author wangzhanwei
 */
public class BigLogClientMainForm {
    private JPanel MainPanel;
    private JTabbedPane tabbedPane1;
    private JTextPane logArea;
    private JTextField ipField;
    private JButton Button;
    private JScrollPane logAreaScroll;
    private JScrollPane scrollPane1;
    private JCheckBox infoCheckBox;
    private JCheckBox errorCheckBox;
    private JCheckBox warnCheckBox;
    private JCheckBox exceptionCheckBox;
    private JCheckBox debugCheckBox;
    private JCheckBox traceCheckBox;
    private JCheckBox allCheckBox;
    private JLabel info;
    private JLabel error;
    private JLabel warn;
    private JLabel exception;
    private JLabel debug;
    private JLabel trace;
    private JLabel all;
    private JPanel panel;
    private JCheckBox autoFlushCheckBox;
    private JButton clearCacheButton;
    private JCheckBox errorNoticeCheckBox;
    private JCheckBox exceptionNoticeCheckBox;
    private JCheckBox debugNoticeCheckBox;
    private JCheckBox traceNoticeCheckBox;
    private JCheckBox warnNoticeCheckBox;
    private JLabel logCountLabel;
    private JButton lastButton;
    private JButton nextButton;
    private JButton nowButton;
    private JLabel autoCleanLabel;
    private JTextField portField;
    private JButton serverSettingConfirm;
    private JLabel ipErrorLabel;
    private JLabel portErrorLabel;
    private JLabel reconnectMessage;
    private JTextField filterTextField;
    private JButton filterConfirmButton;
    private JTextField sentenceTextField;
    private JTextPane answerTextPane;
    private JButton confirmButton;
    private JRadioButton endToTopRadioButton;
    private JRadioButton topToEndRadioButton;
    private JTextField countTextPane;
    private JLabel finderErrorCountLabel;
    private JCheckBox CaseCheckBox;
    private JCheckBox WholeWrodsCheckBox;
    private JCheckBox showRowNumCheckBox;
    private JCheckBox usePatternCheckBox;
    private JPanel FlushLog;
    private JPanel Finder;
    private JPanel Settings;
    private JTextField counttextField1;
    private JTextPane logDetail;
    private JList<String> listLog;
    private JList<String> listLogFilter;
    private JList<String> listError;
    private JList<String> listErrorFilter;
    private JTextPane ErrorPanel;
    private JScrollPane ListLogScrollPane;
    private ButtonGroup buttonGroup;
    public static JFrame frame;
    public static BigLogClientMainForm mainWindow;
    public static ServerLinker serverLinker;
    public static TrayIcon trayIcon;
    public static SystemTray systemTray;
    public static PopupMenu popupMenu;
    public static MenuItem show;
    public static MenuItem exit;
    public static final JPopupMenu pmLog = new JPopupMenu();
    public static JMenuItem miAddFilter = new JMenuItem("增加屏蔽字");
    public static JMenuItem miDelFilter = new JMenuItem("删除屏蔽字");
    public static JMenuItem openFile = new JMenuItem("打开文件");
    public static JMenuItem backFlush = new JMenuItem("返回自动刷新");
    public static final JPopupMenu pmLogFilter = new JPopupMenu();
    public static final JPopupMenu pmError = new JPopupMenu();
    public static JMenuItem miAddErrorFilter = new JMenuItem("增加屏蔽字");
    public static JMenuItem saveFile = new JMenuItem("保存到文件");
    public static JMenuItem miDelErrorFilter = new JMenuItem("删除屏蔽字");
    public static final JPopupMenu pmErrorFilter = new JPopupMenu();

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public BigLogClientMainForm() {
        buttonGroup = new ButtonGroup();
        buttonGroup.add(this.topToEndRadioButton);
        buttonGroup.add(this.endToTopRadioButton);
    }

    public static void main(String args[]) {
        try {
            ApplicationLauncher.launchApplication("64", null, false, new ApplicationLauncher.Callback() {
                @Override
                public void exited(int i) {
//                    System.exit(1);
                }

                @Override
                public void prepareShutdown() {
//                    System.exit(1);
                }
            });
        } catch (Exception e) {
        }
        LogCore.core.info("main start");
        Init.initGlobalFont();
        Init.initMsgReserver();
        try {
            UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
        } catch (Exception e) {
        }
        frame = new JFrame(ConstantUI.APP_NAME + "   " + ConstantUI.APP_VERSION);
        if (SystemTray.isSupported()) {
            systemTray = SystemTray.getSystemTray();
            try {
                popupMenu = new PopupMenu();
                show = new MenuItem("显示主界面");
                exit = new MenuItem("退出");
                popupMenu.add(show);
                popupMenu.add(exit);
                trayIcon = new TrayIcon(ConstantUI.IMAGE_ICON, "Big Log", popupMenu);
                trayIcon.setImageAutoSize(true);
                systemTray.add(trayIcon);
            } catch (Exception e) {
                LogCore.core.error(e.getMessage());
                e.printStackTrace();
            }
        }
        frame.setIconImage(ConstantUI.IMAGE_ICON);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //得到屏幕的尺寸
        frame.setBounds((int) (screenSize.width * 0.1), (int) (screenSize.height * 0.08), (int) (screenSize.width * 0.8), (int) (screenSize.height * 0.8));

        Dimension preferSize = new Dimension((int) (screenSize.width * 0.8), (int) (screenSize.height * 0.8));
        frame.setPreferredSize(preferSize);
        mainWindow = new BigLogClientMainForm();
        frame.setContentPane(mainWindow.MainPanel);
        frame.pack();
        frame.setVisible(true);


        if (!Config.loadConfig()) {
            mainWindow.getLogArea().setText("配置文件加载错误！");
        } else {
            Init.initController();
            openNewConnect(Config.PORT, Config.IP);
        }
        pmLog.add(miAddFilter);
        pmLog.add(openFile);
        pmLog.add(backFlush);
        pmLogFilter.add(miDelFilter);
        pmError.add(miAddErrorFilter);
        pmError.add(saveFile);
        pmErrorFilter.add(miDelErrorFilter);
        MainFrameListener.addListener();
        MainPaneListener.addListener();
        TrayIconListener.addListener();
        MainSrollPaneListener.addListener();
    }

    public static void openNewConnect(int port, String host) {
        Client client = new Client(port, host);
        client.start();
        LogCore.core.info("客户端开始连接服务器");
    }


    public JList<String> getListError() {
        return listError;
    }

    public void setListError(JList<String> listError) {
        this.listError = listError;
    }

    public JList<String> getListErrorFilter() {
        return listErrorFilter;
    }

    public void setListErrorFilter(JList<String> listErrorFilter) {
        this.listErrorFilter = listErrorFilter;
    }

    public JTextPane getErrorPanel() {
        return ErrorPanel;
    }

    public void setErrorPanel(JTextPane errorPanel) {
        ErrorPanel = errorPanel;
    }

    public JTextPane getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(JTextPane logDetail) {
        this.logDetail = logDetail;
    }

    public JList<String> getListLog() {
        return listLog;
    }

    public void setListLog(JList<String> listLog) {
        this.listLog = listLog;
    }

    public JList<String> getListLogFilter() {
        return listLogFilter;
    }

    public void setListLogFilter(JList<String> listLogFilter) {
        this.listLogFilter = listLogFilter;
    }

    public JTextField getCounttextField1() {
        return counttextField1;
    }

    public void setCounttextField1(JTextField counttextField1) {
        this.counttextField1 = counttextField1;
    }

    public JPanel getFlushLog() {
        return FlushLog;
    }

    public void setFlushLog(JPanel flushLog) {
        FlushLog = flushLog;
    }

    public JPanel getFinder() {
        return Finder;
    }

    public void setFinder(JPanel finder) {
        Finder = finder;
    }

    public JPanel getSettings() {
        return Settings;
    }

    public void setSettings(JPanel settings) {
        Settings = settings;
    }

    public JCheckBox getShowRowNumCheckBox() {
        return showRowNumCheckBox;
    }

    public void setShowRowNumCheckBox(JCheckBox showRowNumCheckBox) {
        this.showRowNumCheckBox = showRowNumCheckBox;
    }

    public JCheckBox getUsePatternCheckBox() {
        return usePatternCheckBox;
    }

    public void setUsePatternCheckBox(JCheckBox usePatternCheckBox) {
        this.usePatternCheckBox = usePatternCheckBox;
    }

    public JCheckBox getCaseCheckBox() {
        return CaseCheckBox;
    }

    public void setCaseCheckBox(JCheckBox caseCheckBox) {
        CaseCheckBox = caseCheckBox;
    }

    public JCheckBox getWholeWrodsCheckBox() {
        return WholeWrodsCheckBox;
    }

    public void setWholeWrodsCheckBox(JCheckBox wholeWrodsCheckBox) {
        WholeWrodsCheckBox = wholeWrodsCheckBox;
    }

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    public void setButtonGroup(ButtonGroup buttonGroup) {
        this.buttonGroup = buttonGroup;
    }

    public JLabel getFinderErrorCountLabel() {
        return finderErrorCountLabel;
    }

    public void setFinderErrorCountLabel(JLabel finderErrorCountLabel) {
        this.finderErrorCountLabel = finderErrorCountLabel;
    }

    public JTextField getSentenceTextField() {
        return sentenceTextField;
    }

    public void setSentenceTextField(JTextField sentenceTextField) {
        this.sentenceTextField = sentenceTextField;
    }

    public JTextPane getAnswerTextPane() {
        return answerTextPane;
    }

    public void setAnswerTextPane(JTextPane answerTextPane) {
        this.answerTextPane = answerTextPane;
    }

    public JButton getConfirmButton() {
        return confirmButton;
    }

    public void setConfirmButton(JButton confirmButton) {
        this.confirmButton = confirmButton;
    }

    public JRadioButton getEndToTopRadioButton() {
        return endToTopRadioButton;
    }

    public void setEndToTopRadioButton(JRadioButton endToTopRadioButton) {
        this.endToTopRadioButton = endToTopRadioButton;
    }

    public JRadioButton getTopToEndRadioButton() {
        return topToEndRadioButton;
    }

    public void setTopToEndRadioButton(JRadioButton topToEndRadioButton) {
        this.topToEndRadioButton = topToEndRadioButton;
    }

    public JTextField getCountTextPane() {
        return countTextPane;
    }

    public void setCountTextPane(JTextField countTextPane) {
        this.countTextPane = countTextPane;
    }

    public JTextField getFilterTextField() {
        return filterTextField;
    }

    public void setFilterTextField(JTextField filterTextField) {
        this.filterTextField = filterTextField;
    }

    public JButton getFilterConfirmButton() {
        return filterConfirmButton;
    }

    public void setFilterConfirmButton(JButton filterConfirmButton) {
        this.filterConfirmButton = filterConfirmButton;
    }

    public JLabel getReconnectMessage() {
        return reconnectMessage;
    }

    public void setReconnectMessage(JLabel reconnectMessage) {
        this.reconnectMessage = reconnectMessage;
    }

    public JLabel getIpErrorLabel() {
        return ipErrorLabel;
    }

    public void setIpErrorLabel(JLabel ipErrorLabel) {
        this.ipErrorLabel = ipErrorLabel;
    }

    public JLabel getPortErrorLabel() {
        return portErrorLabel;
    }

    public void setPortErrorLabel(JLabel portErrorLabel) {
        this.portErrorLabel = portErrorLabel;
    }

    public JTextField getPortField() {
        return portField;
    }

    public void setPortField(JTextField portField) {
        this.portField = portField;
    }

    public JButton getServerSettingConfirm() {
        return serverSettingConfirm;
    }

    public void setServerSettingConfirm(JButton serverSettingConfirm) {
        this.serverSettingConfirm = serverSettingConfirm;
    }

    public void setClearCacheButton(JButton clearCacheButton) {
        this.clearCacheButton = clearCacheButton;
    }

    public void setErrorNoticeCheckBox(JCheckBox errorNoticeCheckBox) {
        this.errorNoticeCheckBox = errorNoticeCheckBox;
    }

    public void setExceptionNoticeCheckBox(JCheckBox exceptionNoticeCheckBox) {
        this.exceptionNoticeCheckBox = exceptionNoticeCheckBox;
    }

    public void setDebugNoticeCheckBox(JCheckBox debugNoticeCheckBox) {
        this.debugNoticeCheckBox = debugNoticeCheckBox;
    }

    public void setTraceNoticeCheckBox(JCheckBox traceNoticeCheckBox) {
        this.traceNoticeCheckBox = traceNoticeCheckBox;
    }

    public void setLastButton(JButton lastButton) {
        this.lastButton = lastButton;
    }

    public void setNextButton(JButton nextButton) {
        this.nextButton = nextButton;
    }

    public void setNowButton(JButton nowButton) {
        this.nowButton = nowButton;
    }

    public JLabel getAutoCleanLabel() {
        return autoCleanLabel;
    }

    public void setAutoCleanLabel(JLabel autoCleanLabel) {
        this.autoCleanLabel = autoCleanLabel;
    }

    public void setLogCountLabel(JLabel logCountLabel) {
        this.logCountLabel = logCountLabel;
    }

    public void setWarnNoticeCheckBox(JCheckBox warnNoticeCheckBox) {
        this.warnNoticeCheckBox = warnNoticeCheckBox;
    }

    public void setMainPanel(JPanel mainPanel) {
        MainPanel = mainPanel;
    }

    public void setTabbedPane1(JTabbedPane tabbedPane1) {
        this.tabbedPane1 = tabbedPane1;
    }

    public void setLogArea(JTextPane logArea) {
        this.logArea = logArea;
    }

    public void setIpField(JTextField ipField) {
        this.ipField = ipField;
    }

    public void setButton(JButton button) {
        Button = button;
    }

    public void setLogAreaScroll(JScrollPane logAreaScroll) {
        this.logAreaScroll = logAreaScroll;
    }

    public void setScrollPane1(JScrollPane scrollPane1) {
        this.scrollPane1 = scrollPane1;
    }

    public void setInfoCheckBox(JCheckBox infoCheckBox) {
        this.infoCheckBox = infoCheckBox;
    }

    public void setErrorCheckBox(JCheckBox errorCheckBox) {
        this.errorCheckBox = errorCheckBox;
    }

    public void setWarnCheckBox(JCheckBox warnCheckBox) {
        this.warnCheckBox = warnCheckBox;
    }

    public void setExceptionCheckBox(JCheckBox exceptionCheckBox) {
        this.exceptionCheckBox = exceptionCheckBox;
    }

    public void setDebugCheckBox(JCheckBox debugCheckBox) {
        this.debugCheckBox = debugCheckBox;
    }

    public void setTraceCheckBox(JCheckBox traceCheckBox) {
        this.traceCheckBox = traceCheckBox;
    }

    public void setAllCheckBox(JCheckBox allCheckBox) {
        this.allCheckBox = allCheckBox;
    }

    public void setInfo(JLabel info) {
        this.info = info;
    }

    public void setError(JLabel error) {
        this.error = error;
    }

    public void setWarn(JLabel warn) {
        this.warn = warn;
    }

    public void setException(JLabel exception) {
        this.exception = exception;
    }

    public void setDebug(JLabel debug) {
        this.debug = debug;
    }

    public void setTrace(JLabel trace) {
        this.trace = trace;
    }

    public void setAll(JLabel all) {
        this.all = all;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public static void setFrame(JFrame frame) {
        BigLogClientMainForm.frame = frame;
    }

    public static void setMainWindow(BigLogClientMainForm mainWindow) {
        BigLogClientMainForm.mainWindow = mainWindow;
    }

    public static void setServerLinker(ServerLinker serverLinker) {
        BigLogClientMainForm.serverLinker = serverLinker;
    }

    public JLabel getLogCountLabel() {
        return logCountLabel;
    }

    public JButton getLastButton() {
        return lastButton;
    }

    public JButton getNextButton() {
        return nextButton;
    }

    public JButton getNowButton() {
        return nowButton;
    }

    public JButton getClearCacheButton() {
        return clearCacheButton;
    }

    public JCheckBox getErrorNoticeCheckBox() {
        return errorNoticeCheckBox;
    }

    public JCheckBox getExceptionNoticeCheckBox() {
        return exceptionNoticeCheckBox;
    }

    public JCheckBox getDebugNoticeCheckBox() {
        return debugNoticeCheckBox;
    }

    public JCheckBox getTraceNoticeCheckBox() {
        return traceNoticeCheckBox;
    }

    public JCheckBox getWarnNoticeCheckBox() {
        return warnNoticeCheckBox;
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

    public JTabbedPane getTabbedPane1() {
        return tabbedPane1;
    }

    public JTextPane getLogArea() {
        return logArea;
    }

    public JTextField getIpField() {
        return ipField;
    }

    public JButton getButton() {
        return Button;
    }

    public JScrollPane getLogAreaScroll() {
        return logAreaScroll;
    }

    public JScrollPane getScrollPane1() {
        return scrollPane1;
    }

    public JCheckBox getInfoCheckBox() {
        return infoCheckBox;
    }

    public JCheckBox getErrorCheckBox() {
        return errorCheckBox;
    }

    public JCheckBox getWarnCheckBox() {
        return warnCheckBox;
    }

    public JCheckBox getExceptionCheckBox() {
        return exceptionCheckBox;
    }

    public JCheckBox getDebugCheckBox() {
        return debugCheckBox;
    }

    public JCheckBox getTraceCheckBox() {
        return traceCheckBox;
    }

    public JCheckBox getAllCheckBox() {
        return allCheckBox;
    }

    public JLabel getInfo() {
        return info;
    }

    public JLabel getError() {
        return error;
    }

    public JLabel getWarn() {
        return warn;
    }

    public JLabel getException() {
        return exception;
    }

    public JLabel getDebug() {
        return debug;
    }

    public JLabel getTrace() {
        return trace;
    }

    public JLabel getAll() {
        return all;
    }

    public JPanel getPanel() {
        return panel;
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static BigLogClientMainForm getMainWindow() {
        return mainWindow;
    }

    public static ServerLinker getServerLinker() {
        return serverLinker;
    }

    public JCheckBox getAutoFlushCheckBox() {
        return autoFlushCheckBox;
    }

    public void setAutoFlushCheckBox(JCheckBox autoFlushCheckBox) {
        this.autoFlushCheckBox = autoFlushCheckBox;
    }
}
