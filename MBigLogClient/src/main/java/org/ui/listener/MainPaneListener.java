package org.ui.listener;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import org.support.Config;
import org.ui.BigLogClientMainForm;
import org.ui.ConstantUI;
import org.ui.logic.EnumDefaultFilter;
import org.ui.logic.FinderLogic;
import org.ui.logic.InsertLogLogic;
import org.ui.logic.LogInfo;
import org.ui.logic.MainFormLogic;

/**
 * @author wangzhanwei
 */
public class MainPaneListener {
    private static JCheckBox infoCheckBox = BigLogClientMainForm.mainWindow.getInfoCheckBox();
    private static JCheckBox errorCheckBox = BigLogClientMainForm.mainWindow.getErrorCheckBox();
    private static JCheckBox warnCheckBox = BigLogClientMainForm.mainWindow.getWarnCheckBox();
    private static JCheckBox exceptionCheckBox = BigLogClientMainForm.mainWindow.getExceptionCheckBox();
    private static JCheckBox debugCheckBox = BigLogClientMainForm.mainWindow.getDebugCheckBox();
    private static JCheckBox traceCheckBox = BigLogClientMainForm.mainWindow.getTraceCheckBox();
    private static JCheckBox allCheckBox = BigLogClientMainForm.mainWindow.getAllCheckBox();
    private static JCheckBox autoFlush = BigLogClientMainForm.mainWindow.getAutoFlushCheckBox();
    private static JCheckBox errorNoticeCheckBox = BigLogClientMainForm.mainWindow.getErrorNoticeCheckBox();
    private static JCheckBox warnNoticeCheckBox = BigLogClientMainForm.mainWindow.getWarnNoticeCheckBox();
    private static JCheckBox exceptionNoticeCheckBox = BigLogClientMainForm.mainWindow.getExceptionNoticeCheckBox();
    private static JCheckBox debugNoticeCheckBox = BigLogClientMainForm.mainWindow.getDebugNoticeCheckBox();
    private static JCheckBox traceNoticeCheckBox = BigLogClientMainForm.mainWindow.getTraceNoticeCheckBox();
    private static JButton clearCachaeButton = BigLogClientMainForm.mainWindow.getClearCacheButton();
    private static JButton lastButton = BigLogClientMainForm.mainWindow.getLastButton();
    private static JButton nextButton = BigLogClientMainForm.mainWindow.getNextButton();
    private static JButton nowButtion = BigLogClientMainForm.mainWindow.getNowButton();
    private static JButton serverSettingConfirm = BigLogClientMainForm.mainWindow.getServerSettingConfirm();
    private static JButton filterWrodConfirmButton = BigLogClientMainForm.mainWindow.getFilterConfirmButton();
    private static JTextField filterTextField = BigLogClientMainForm.mainWindow.getFilterTextField();
    private static JButton confirmButton = BigLogClientMainForm.mainWindow.getConfirmButton();
    private static JCheckBox usePatternCheckBox = BigLogClientMainForm.mainWindow.getUsePatternCheckBox();

    @SuppressWarnings("unchecked")
    public static void addListener() {
        infoCheckBox.addItemListener(e -> {
            if (e.getSource() == infoCheckBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!MainFormLogic.defaultFilters.contains(EnumDefaultFilter.INFO.getIndex())) {
                        MainFormLogic.defaultFilters.add(EnumDefaultFilter.INFO.getIndex());
                        if (MainFormLogic.selectAll) {
                            MainFormLogic.selectAll = false;
                            BigLogClientMainForm.mainWindow.getAllCheckBox().setSelected(false);
                        } else {
                            if (BigLogClientMainForm.serverLinker != null) {
                                MainFormLogic.resetText();
                            }
                        }
                    }
                } else {
                    if (MainFormLogic.defaultFilters.contains(EnumDefaultFilter.INFO.getIndex())) {
                        MainFormLogic.defaultFilters.remove((Object) EnumDefaultFilter.INFO.getIndex());
                        if (BigLogClientMainForm.serverLinker != null) {
                            MainFormLogic.resetText();
                        }
                    }
                }
            }
        });

        errorCheckBox.addItemListener(e -> {
            if (e.getSource() == errorCheckBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!MainFormLogic.defaultFilters.contains(EnumDefaultFilter.ERROR.getIndex())) {
                        MainFormLogic.defaultFilters.add(EnumDefaultFilter.ERROR.getIndex());
                        if (MainFormLogic.selectAll) {
                            MainFormLogic.selectAll = false;
                            BigLogClientMainForm.mainWindow.getAllCheckBox().setSelected(false);
                        } else {
                            if (BigLogClientMainForm.serverLinker != null) {
                                MainFormLogic.resetText();
                            }
                        }
                    }
                } else {
                    if (MainFormLogic.defaultFilters.contains(EnumDefaultFilter.ERROR.getIndex())) {
                        MainFormLogic.defaultFilters.remove((Object) EnumDefaultFilter.ERROR.getIndex());
                        if (BigLogClientMainForm.serverLinker != null) {
                            MainFormLogic.resetText();
                        }
                    }
                }
            }
        });

        warnCheckBox.addItemListener(e -> {
            if (e.getSource() == warnCheckBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!MainFormLogic.defaultFilters.contains(EnumDefaultFilter.WARN.getIndex())) {
                        MainFormLogic.defaultFilters.add(EnumDefaultFilter.WARN.getIndex());
                        if (MainFormLogic.selectAll) {
                            MainFormLogic.selectAll = false;
                            BigLogClientMainForm.mainWindow.getAllCheckBox().setSelected(false);
                        } else {
                            if (BigLogClientMainForm.serverLinker != null) {
                                MainFormLogic.resetText();
                            }
                        }
                    }
                } else {
                    if (MainFormLogic.defaultFilters.contains(EnumDefaultFilter.WARN.getIndex())) {
                        MainFormLogic.defaultFilters.remove((Object) EnumDefaultFilter.WARN.getIndex());
                        if (BigLogClientMainForm.serverLinker != null) {
                            MainFormLogic.resetText();
                        }
                    }
                }
            }
        });

        exceptionCheckBox.addItemListener(e -> {
            if (e.getSource() == exceptionCheckBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!MainFormLogic.defaultFilters.contains(EnumDefaultFilter.EXCEPTION.getIndex())) {
                        MainFormLogic.defaultFilters.add(EnumDefaultFilter.EXCEPTION.getIndex());
                        if (MainFormLogic.selectAll) {
                            MainFormLogic.selectAll = false;
                            BigLogClientMainForm.mainWindow.getAllCheckBox().setSelected(false);
                        } else {
                            if (BigLogClientMainForm.serverLinker != null) {
                                MainFormLogic.resetText();
                            }
                        }
                    }
                } else {
                    if (MainFormLogic.defaultFilters.contains(EnumDefaultFilter.EXCEPTION.getIndex())) {
                        MainFormLogic.defaultFilters.remove((Object) EnumDefaultFilter.EXCEPTION.getIndex());
                        if (BigLogClientMainForm.serverLinker != null) {
                            MainFormLogic.resetText();
                        }
                    }
                }
            }
        });

        debugCheckBox.addItemListener(e -> {
            if (e.getSource() == debugCheckBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!MainFormLogic.defaultFilters.contains(EnumDefaultFilter.DEBUG.getIndex())) {
                        MainFormLogic.defaultFilters.add(EnumDefaultFilter.DEBUG.getIndex());
                        if (MainFormLogic.selectAll) {
                            MainFormLogic.selectAll = false;
                            BigLogClientMainForm.mainWindow.getAllCheckBox().setSelected(false);
                        } else {
                            if (BigLogClientMainForm.serverLinker != null) {
                                MainFormLogic.resetText();
                            }
                        }
                    }
                } else {
                    if (MainFormLogic.defaultFilters.contains(EnumDefaultFilter.DEBUG.getIndex())) {
                        MainFormLogic.defaultFilters.remove((Object) EnumDefaultFilter.DEBUG.getIndex());
                        if (BigLogClientMainForm.serverLinker != null) {
                            MainFormLogic.resetText();
                        }
                    }
                }
            }
        });

        traceCheckBox.addItemListener(e -> {
            if (e.getSource() == traceCheckBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!MainFormLogic.defaultFilters.contains(EnumDefaultFilter.TRACE.getIndex())) {
                        MainFormLogic.defaultFilters.add(EnumDefaultFilter.TRACE.getIndex());
                        if (MainFormLogic.selectAll) {
                            MainFormLogic.selectAll = false;
                            BigLogClientMainForm.mainWindow.getAllCheckBox().setSelected(false);
                        } else {
                            if (BigLogClientMainForm.serverLinker != null) {
                                MainFormLogic.resetText();
                            }
                        }
                    }
                } else {
                    if (MainFormLogic.defaultFilters.contains(EnumDefaultFilter.TRACE.getIndex())) {
                        MainFormLogic.defaultFilters.remove((Object) EnumDefaultFilter.TRACE.getIndex());
                        if (BigLogClientMainForm.serverLinker != null) {
                            MainFormLogic.resetText();
                        }
                    }
                }
            }
        });

        allCheckBox.addItemListener(e -> {
            if (e.getSource() == allCheckBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
//                        infoCheckBox.setSelected(false);
//                        errorCheckBox.setSelected(false);
//                        warnCheckBox.setSelected(false);
//                        exceptionCheckBox.setSelected(false);
//                        debugCheckBox.setSelected(false);
//                        traceCheckBox.setSelected(false);
//                        MainFormLogic.defaultFilters.clear();
                    MainFormLogic.selectAll = true;
                } else {
                    MainFormLogic.selectAll = false;
                }
            }
            if (BigLogClientMainForm.serverLinker != null) {
                MainFormLogic.resetText();
            }
        });
        autoFlush.addItemListener(e -> {
            if (e.getSource() == autoFlush) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    MainFormLogic.autoUpdateScroll = true;
                    BigLogClientMainForm.mainWindow.getLogArea().setCaretPosition(BigLogClientMainForm.mainWindow.getLogArea().getDocument().getLength());
                } else {
                    MainFormLogic.autoUpdateScroll = false;
                }
            }
        });
        errorNoticeCheckBox.addItemListener(e -> {
            if (e.getSource() == errorNoticeCheckBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!MainFormLogic.noticeDialog.contains(EnumDefaultFilter.ERROR.getIndex())) {
                        MainFormLogic.noticeDialog.add(EnumDefaultFilter.ERROR.getIndex());
                    }
                } else {
                    if (MainFormLogic.noticeDialog.contains(EnumDefaultFilter.ERROR.getIndex())) {
                        MainFormLogic.noticeDialog.remove((Object) EnumDefaultFilter.ERROR.getIndex());
                    }
                }
            }
        });
        warnNoticeCheckBox.addItemListener(e -> {
            if (e.getSource() == warnNoticeCheckBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!MainFormLogic.noticeDialog.contains(EnumDefaultFilter.WARN.getIndex())) {
                        MainFormLogic.noticeDialog.add(EnumDefaultFilter.WARN.getIndex());
                    }
                } else {
                    if (MainFormLogic.noticeDialog.contains(EnumDefaultFilter.WARN.getIndex())) {
                        MainFormLogic.noticeDialog.remove((Object) EnumDefaultFilter.WARN.getIndex());
                    }
                }
            }
        });
        exceptionNoticeCheckBox.addItemListener(e -> {
            if (e.getSource() == exceptionNoticeCheckBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!MainFormLogic.noticeDialog.contains(EnumDefaultFilter.EXCEPTION.getIndex())) {
                        MainFormLogic.noticeDialog.add(EnumDefaultFilter.EXCEPTION.getIndex());
                    }
                } else {
                    if (MainFormLogic.noticeDialog.contains(EnumDefaultFilter.EXCEPTION.getIndex())) {
                        MainFormLogic.noticeDialog.remove((Object) EnumDefaultFilter.EXCEPTION.getIndex());
                    }
                }
            }
        });
        debugNoticeCheckBox.addItemListener(e -> {
            if (e.getSource() == debugNoticeCheckBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!MainFormLogic.noticeDialog.contains(EnumDefaultFilter.DEBUG.getIndex())) {
                        MainFormLogic.noticeDialog.add(EnumDefaultFilter.DEBUG.getIndex());
                    }
                } else {
                    if (MainFormLogic.noticeDialog.contains(EnumDefaultFilter.DEBUG.getIndex())) {
                        MainFormLogic.noticeDialog.remove((Object) EnumDefaultFilter.DEBUG.getIndex());
                    }
                }
            }
        });
        traceNoticeCheckBox.addItemListener(e -> {
            if (e.getSource() == traceCheckBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!MainFormLogic.noticeDialog.contains(EnumDefaultFilter.TRACE.getIndex())) {
                        MainFormLogic.noticeDialog.add(EnumDefaultFilter.TRACE.getIndex());
                    }
                } else {
                    if (MainFormLogic.noticeDialog.contains(EnumDefaultFilter.TRACE.getIndex())) {
                        MainFormLogic.noticeDialog.remove((Object) EnumDefaultFilter.TRACE.getIndex());
                    }
                }
            }
        });
        clearCachaeButton.addActionListener(e -> {
            if (e.getSource() == clearCachaeButton) {
                if (BigLogClientMainForm.serverLinker != null) {
                    MainFormLogic.cleanCache();
                }
            }
        });
        lastButton.addActionListener(e -> {
            if (e.getSource() == lastButton) {
                MainFormLogic.jumpToLastPoint();
                if (BigLogClientMainForm.mainWindow.getAutoFlushCheckBox().isSelected()) {
                    BigLogClientMainForm.mainWindow.getAutoFlushCheckBox().setSelected(false);
                }
            }
        });
        nextButton.addActionListener(e -> {
            if (e.getSource() == nextButton) {
                MainFormLogic.jumpToNextPoint();
                if (BigLogClientMainForm.mainWindow.getAutoFlushCheckBox().isSelected()) {
                    BigLogClientMainForm.mainWindow.getAutoFlushCheckBox().setSelected(false);
                }
            }
        });
        nowButtion.addActionListener(e -> {
            if (e.getSource() == nowButtion) {
                MainFormLogic.jumpToNowPoint();
                if (BigLogClientMainForm.mainWindow.getAutoFlushCheckBox().isSelected()) {
                    BigLogClientMainForm.mainWindow.getAutoFlushCheckBox().setSelected(false);
                }
            }
        });

        serverSettingConfirm.addActionListener(e -> {
            if (e.getSource() == serverSettingConfirm) {
                if (MainFormLogic.serverSettingCharge()) {
                    if (BigLogClientMainForm.serverLinker != null) {
                        BigLogClientMainForm.serverLinker.close();
                    }
                    String ip = BigLogClientMainForm.mainWindow.getIpField().getText();
                    int port = Integer.parseInt(BigLogClientMainForm.mainWindow.getPortField().getText());
                    BigLogClientMainForm.openNewConnect(port, ip);
                    Config.setProperties("ip", ip);
                    Config.setProperties("port", port + "");
                    BigLogClientMainForm.mainWindow.getPortErrorLabel().setVisible(false);
                    BigLogClientMainForm.mainWindow.getIpErrorLabel().setVisible(false);
                } else {
                    BigLogClientMainForm.mainWindow.getReconnectMessage().setText("连接状态：失败 (请检查ip,port是否输入正确，服务器端服务是否开启，防火墙等)");
                    BigLogClientMainForm.mainWindow.getReconnectMessage().setForeground(new Color(255, 0, 6));
                }
            }
        });

        filterWrodConfirmButton.addActionListener(e -> {
            if (e.getSource() == filterWrodConfirmButton) {
                String s = filterTextField.getText();
                if (s.length() == 0 || s.equals("")) {
                    MainFormLogic.filter = false;
                    if (BigLogClientMainForm.serverLinker != null) {
                        MainFormLogic.resetText();
                    }
                } else {
                    MainFormLogic.filter = true;
                    if (BigLogClientMainForm.serverLinker != null) {
                        MainFormLogic.resetText();
                    }
                }
            }
        });
        filterTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getSource() != filterTextField) {
                    return;
                }
                try {
                    super.keyTyped(e);
                    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        String s = filterTextField.getText();
                        if (s.length() == 0 || s.equals("")) {
                            MainFormLogic.filter = false;
                            if (BigLogClientMainForm.serverLinker != null) {
                                MainFormLogic.resetText();
                            }
                        } else {
                            MainFormLogic.filter = true;
                            if (BigLogClientMainForm.serverLinker != null) {
                                MainFormLogic.resetText();
                            }
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        confirmButton.addActionListener((ActionEvent e) -> {
            if (e.getSource() == confirmButton) {
                find();
            }
        });
        BigLogClientMainForm.mainWindow.getSentenceTextField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getSource() != BigLogClientMainForm.mainWindow.getSentenceTextField()) {
                    return;
                }
                super.keyTyped(e);
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    find();
                }
            }
        });

        usePatternCheckBox.addItemListener(e -> {
            if (e.getSource() == usePatternCheckBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    BigLogClientMainForm.mainWindow.getWholeWrodsCheckBox().setSelected(false);
                    BigLogClientMainForm.mainWindow.getCaseCheckBox().setSelected(false);
                }
            }
        });
        BigLogClientMainForm.mainWindow.getListError().addListSelectionListener(e -> {
            int selectedIndex = BigLogClientMainForm.mainWindow.getListError().getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < MainFormLogic.getErrorDataFilter().size()) {
                LogInfo info = MainFormLogic.getErrorDataFilter().get(selectedIndex);
                InsertLogLogic.setTextChangeColor(info.getLog(), BigLogClientMainForm.mainWindow.getErrorPanel());
                BigLogClientMainForm.mainWindow.getErrorPanel().setCaretPosition(0);
            }
        });
        BigLogClientMainForm.mainWindow.getListError().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 3) {
                    BigLogClientMainForm.pmError.show(e.getComponent(), e.getX(), e.getY());
                } else {
                    BigLogClientMainForm.pmError.setVisible(false);
                }
            }
        });
        BigLogClientMainForm.mainWindow.getListErrorFilter().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 3) {
                    BigLogClientMainForm.pmErrorFilter.show(e.getComponent(), e.getX(), e.getY());
                } else {
                    BigLogClientMainForm.pmErrorFilter.setVisible(false);
                }
            }
        });
        BigLogClientMainForm.miAddErrorFilter.addActionListener(e -> {
            java.util.List<String> selectedList = BigLogClientMainForm.mainWindow.getListError().getSelectedValuesList();
            if (!selectedList.isEmpty()) {
                MainFormLogic.addErrorFilterKey(selectedList);
                DefaultListModel<String> defaultListModel = (DefaultListModel<String>) BigLogClientMainForm.mainWindow.getListErrorFilter().getModel();
                defaultListModel.removeAllElements();
                for (int i = 0; i < MainFormLogic.getErrorFilterKeys().size(); i++) {
                    defaultListModel.add(i, MainFormLogic.getErrorFilterKeys().get(i));
                }
                MainFormLogic.errorFilter();
                DefaultListModel<String> defaultListModel1 = (DefaultListModel<String>) BigLogClientMainForm.mainWindow.getListError().getModel();
                defaultListModel1.removeAllElements();
                for (int i = 0; i < MainFormLogic.getErrorDataFilter().size(); i++) {
                    defaultListModel1.add(i, MainFormLogic.getErrorDataFilter().get(i).errorName);
                }
            }
        });
        BigLogClientMainForm.miDelErrorFilter.addActionListener(e -> {
            java.util.List<String> selectedList = BigLogClientMainForm.mainWindow.getListErrorFilter().getSelectedValuesList();
            if (!selectedList.isEmpty()) {
                MainFormLogic.delErrorFilterKey(selectedList);
                DefaultListModel<String> defaultListModel = (DefaultListModel<String>) BigLogClientMainForm.mainWindow.getListErrorFilter().getModel();
                defaultListModel.removeAllElements();
                for (int i = 0; i < MainFormLogic.getErrorFilterKeys().size(); i++) {
                    defaultListModel.add(i, MainFormLogic.getErrorFilterKeys().get(i));
                }

                MainFormLogic.errorFilter();
                DefaultListModel<String> defaultListModel1 = (DefaultListModel<String>) BigLogClientMainForm.mainWindow.getListError().getModel();
                defaultListModel1.removeAllElements();
                for (int i = 0; i < MainFormLogic.getErrorDataFilter().size(); i++) {
                    defaultListModel1.add(i, MainFormLogic.getErrorDataFilter().get(i).errorName);
                }
            }
        });


        BigLogClientMainForm.mainWindow.getListLog().addListSelectionListener(e -> {
            int selectedIndex = BigLogClientMainForm.mainWindow.getListLog().getSelectedIndex();
            if (selectedIndex >= 0) {
                LogInfo info;
                if (MainFormLogic.isOpenFile()) {
                    info = MainFormLogic.getLogLoader().getDatasFilter().get(selectedIndex > MainFormLogic.getLogLoader().getDatasFilter().size() ? MainFormLogic.getLogLoader().getDatasFilter().size() - 1 : selectedIndex);
                } else {
                    info = MainFormLogic.getDatasFilter().get(selectedIndex > MainFormLogic.getDatasFilter().size() ? MainFormLogic.getDatasFilter().size() - 1 : selectedIndex);
                }
                InsertLogLogic.setTextChangeColor(info.getLog(), BigLogClientMainForm.mainWindow.getLogDetail());
                BigLogClientMainForm.mainWindow.getLogDetail().setCaretPosition(0);
            }
        });
        BigLogClientMainForm.mainWindow.getListLog().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 3) {
                    BigLogClientMainForm.pmLog.show(e.getComponent(), e.getX(), e.getY());
                } else {
                    BigLogClientMainForm.pmLog.setVisible(false);
                }
            }
        });
        BigLogClientMainForm.mainWindow.getListLogFilter().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 3) {
                    BigLogClientMainForm.pmLogFilter.show(e.getComponent(), e.getX(), e.getY());
                } else {
                    BigLogClientMainForm.pmLogFilter.setVisible(false);
                }
            }
        });

        BigLogClientMainForm.miAddFilter.addActionListener(e -> {
            java.util.List<String> selectedList = BigLogClientMainForm.mainWindow.getListLog().getSelectedValuesList();
            if (!selectedList.isEmpty()) {
                DefaultListModel<String> defaultListModel = new DefaultListModel<>();
                DefaultListModel<String> defaultListModel1 = new DefaultListModel<>();
                defaultListModel.removeAllElements();
                defaultListModel1.removeAllElements();
                List<String> fileterKeys;
                List<LogInfo> datasFilter;
                if (MainFormLogic.isOpenFile()) {
                    MainFormLogic.getLogLoader().addFilterKey(selectedList);
                    MainFormLogic.getLogLoader().filter();
                    fileterKeys = MainFormLogic.getLogLoader().getFilterKeys();
                    datasFilter = MainFormLogic.getLogLoader().getDatasFilter();
                } else {
                    MainFormLogic.addFilterKey(selectedList);
                    MainFormLogic.filter();
                    fileterKeys = MainFormLogic.getFilterKeys();
                    datasFilter = MainFormLogic.getDatasFilter();
                }
                for (int i = 0; i < fileterKeys.size(); i++) {
                    defaultListModel.add(i, fileterKeys.get(i));
                }
                for (int i = 0; i < datasFilter.size(); i++) {
                    defaultListModel1.add(i, datasFilter.get(i).protocolName);
                }
                BigLogClientMainForm.mainWindow.getListLog().setModel(defaultListModel1);
                BigLogClientMainForm.mainWindow.getListLogFilter().setModel(defaultListModel);
            }
        });
        BigLogClientMainForm.miDelFilter.addActionListener(e -> {
            java.util.List<String> selectedList = BigLogClientMainForm.mainWindow.getListLogFilter().getSelectedValuesList();
            if (!selectedList.isEmpty()) {
                DefaultListModel<String> defaultListModel = new DefaultListModel<>();
                DefaultListModel<String> defaultListModel1 = new DefaultListModel<>();
                List<String> fileterKeys;
                List<LogInfo> datasFilter;
                if (MainFormLogic.isOpenFile()) {
                    MainFormLogic.getLogLoader().delFilterKey(selectedList);
                    MainFormLogic.getLogLoader().filter();
                    fileterKeys = MainFormLogic.getLogLoader().getFilterKeys();
                    datasFilter = MainFormLogic.getLogLoader().getDatasFilter();
                } else {
                    MainFormLogic.delFilterKey(selectedList);
                    MainFormLogic.filter();
                    fileterKeys = MainFormLogic.getFilterKeys();
                    datasFilter = MainFormLogic.getDatasFilter();
                }
                for (int i = 0; i < fileterKeys.size(); i++) {
                    defaultListModel.add(i, fileterKeys.get(i));
                }
                ;
                for (int i = 0; i < datasFilter.size(); i++) {
                    defaultListModel1.add(i, datasFilter.get(i).protocolName);
                }
                BigLogClientMainForm.mainWindow.getListLog().setModel(defaultListModel1);
                BigLogClientMainForm.mainWindow.getListLogFilter().setModel(defaultListModel);
            }
        });
        BigLogClientMainForm.openFile.addActionListener(e -> {
            if (e.getSource() == BigLogClientMainForm.openFile) {
                FileDialog fileDialog = new FileDialog(BigLogClientMainForm.frame, "打开文件", 0);
                fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".log"));
                fileDialog.setVisible(true);
                String fileName = fileDialog.getFile();
                String fileDir = fileDialog.getDirectory();
                if (fileName != null && fileDir != null) {
                    if (MainFormLogic.isOpenFile()) {
                        MainFormLogic.cleanFile();
                    }
                    File file = new File(fileDir, fileName);
                    MainFormLogic.loadFile(file);
                    MainFormLogic.setOpenFile(true);
                }
            }
        });
        BigLogClientMainForm.backFlush.addActionListener(e -> {
            if (e.getSource() == BigLogClientMainForm.backFlush) {
                if (MainFormLogic.isOpenFile()) {
                    MainFormLogic.cleanFile();
                    MainFormLogic.setOpenFile(false);
                    MainFormLogic.filter();
                    DefaultListModel<String> defaultListModel = new DefaultListModel<>();
                    DefaultListModel<String> defaultListModel1 = new DefaultListModel<>();
                    List<String> fileterKeys = MainFormLogic.getFilterKeys();
                    List<LogInfo> datasFilter = MainFormLogic.getDatasFilter();
                    for (int i = 0; i < fileterKeys.size(); i++) {
                        defaultListModel.add(i, fileterKeys.get(i));
                    }
                    for (int i = 0; i < datasFilter.size(); i++) {
                        defaultListModel1.add(i, datasFilter.get(i).protocolName);
                    }
                    BigLogClientMainForm.mainWindow.getListLog().setModel(defaultListModel1);
                    BigLogClientMainForm.mainWindow.getListLogFilter().setModel(defaultListModel);
                }
            }
        });
        BigLogClientMainForm.saveFile.addActionListener(e -> {
            if (e.getSource() == BigLogClientMainForm.saveFile) {

                javax.swing.filechooser.FileSystemView fsv = javax.swing.filechooser.FileSystemView.getFileSystemView();
                File file = new File(fsv.getHomeDirectory(), "Errors.Log");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
                    try {
                        Iterator<LogInfo> iterator = MainFormLogic.getErrorDataFilter().iterator();
                        while (iterator.hasNext()) {
                            LogInfo info = iterator.next();
                            bufferedWriter.write(info.log);
                            bufferedWriter.newLine();
                        }
                    } finally {
                        bufferedWriter.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }


            }
        });
    }

    private static void find() {
        int count = FinderLogic.checkCount();
        if (count == -1) {
            BigLogClientMainForm.mainWindow.getFinderErrorCountLabel().setVisible(true);
            return;
        }
        int totalCount = FinderLogic.checkTotalCount();
        if (totalCount == -1) {
            BigLogClientMainForm.mainWindow.getFinderErrorCountLabel().setVisible(true);
            return;
        }
        BigLogClientMainForm.mainWindow.getFinderErrorCountLabel().setVisible(false);
        int orderModel = 2;
        String enable = ConstantUI.ORDER_MODEL_2;
        Enumeration<AbstractButton> radioBtns = BigLogClientMainForm.mainWindow.getButtonGroup().getElements();
        while (radioBtns.hasMoreElements()) {
            AbstractButton btn = radioBtns.nextElement();
            if (btn.isSelected()) {
                enable = btn.getText();
                break;
            }
        }
        switch (enable) {
            case ConstantUI.ORDER_MODEL_1:
                orderModel = 1;
                break;
            case ConstantUI.ORDER_MODEL_2:
                orderModel = 2;
                break;
            default:
                orderModel = 2;
                break;
        }
        String sentence = BigLogClientMainForm.mainWindow.getSentenceTextField().getText();
        int matchCase = 2;
        if (BigLogClientMainForm.mainWindow.getCaseCheckBox().isSelected()) {
            matchCase = 1;
        }
        int wholeWords = 2;
        if (BigLogClientMainForm.mainWindow.getWholeWrodsCheckBox().isSelected()) {
            wholeWords = 1;
        }
        int showRowNum = 2;
        if (BigLogClientMainForm.mainWindow.getShowRowNumCheckBox().isSelected()) {
            showRowNum = 1;
        }
        int usePattern = 2;
        if (BigLogClientMainForm.mainWindow.getUsePatternCheckBox().isSelected()) {
            usePattern = 1;
        }
        FinderLogic.sendCSFindLog(sentence, orderModel, count, totalCount, matchCase, wholeWords, showRowNum, usePattern);
    }
}
