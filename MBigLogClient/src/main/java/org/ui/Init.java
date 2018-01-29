package org.ui;

import java.awt.Font;
import javax.swing.DefaultListModel;
import javax.swing.UIManager;
import org.gen.MsgReceiverInit;
import org.support.Config;
import org.support.observer.MsgSender;
import org.ui.logic.EnumDefaultFilter;
import org.ui.logic.MainFormLogic;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * @author wangzhanwei
 */
public class Init {
    public static void initGlobalFont() {
        Font fnt = new Font("Microsoft YaHei", Font.PLAIN, 12);
        FontUIResource fontRes = new FontUIResource(fnt);
        for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource)
                UIManager.put(key, fontRes);
        }
        BigLogClientMainForm.miDelFilter.setFont(fnt);
        BigLogClientMainForm.miAddFilter.setFont(fnt);
        BigLogClientMainForm.openFile.setFont(fnt);
        BigLogClientMainForm.backFlush.setFont(fnt);
        BigLogClientMainForm.miAddErrorFilter.setFont(fnt);
        BigLogClientMainForm.miDelErrorFilter.setFont(fnt);
        BigLogClientMainForm.saveFile.setFont(fnt);
    }

    public static void initController() {

        BigLogClientMainForm.mainWindow.getIpErrorLabel().setVisible(false);
        BigLogClientMainForm.mainWindow.getPortErrorLabel().setVisible(false);
        BigLogClientMainForm.mainWindow.getFinderErrorCountLabel().setVisible(false);
        BigLogClientMainForm.mainWindow.getEndToTopRadioButton().setSelected(true);
        BigLogClientMainForm.mainWindow.getCountTextPane().setText("20000");
        BigLogClientMainForm.mainWindow.getCounttextField1().setText("50");
        BigLogClientMainForm.mainWindow.getIpField().setText(Config.IP);
        BigLogClientMainForm.mainWindow.getPortField().setText(Config.PORT + "");
        BigLogClientMainForm.mainWindow.getListLog().setBorder(new TitledBorder("日志列表"));
        BigLogClientMainForm.mainWindow.getListLogFilter().setBorder(new TitledBorder("屏蔽列表"));
        BigLogClientMainForm.mainWindow.getLogDetail().setBorder(new TitledBorder("日志详情"));
        BigLogClientMainForm.mainWindow.getListError().setBorder(new TitledBorder("错误列表"));
        BigLogClientMainForm.mainWindow.getListErrorFilter().setBorder(new TitledBorder("屏蔽列表"));
        BigLogClientMainForm.mainWindow.getErrorPanel().setBorder(new TitledBorder("错误详情"));
        DefaultListModel<String> defaultListModel1 = new DefaultListModel<>();

        DefaultListModel<String> defaultListModel2 = new DefaultListModel<>();

        DefaultListModel<String> defaultListModel3 = new DefaultListModel<>();

        DefaultListModel<String> defaultListModel4 = new DefaultListModel<>();

        BigLogClientMainForm.mainWindow.getListLog().setModel(defaultListModel1);
        BigLogClientMainForm.mainWindow.getListLogFilter().setModel(defaultListModel2);
        BigLogClientMainForm.mainWindow.getListErrorFilter().setModel(defaultListModel3);
        BigLogClientMainForm.mainWindow.getListError().setModel(defaultListModel4);
        MainFormLogic.noticeDialog.add(EnumDefaultFilter.ERROR.getIndex());
        MainFormLogic.noticeDialog.add(EnumDefaultFilter.WARN.getIndex());
        MainFormLogic.noticeDialog.add(EnumDefaultFilter.EXCEPTION.getIndex());
        MainFormLogic.patterns = new Pattern[MainFormLogic.keys.length];
        for (int i = 0; i < MainFormLogic.patterns.length; i++) {
            MainFormLogic.patterns[i] = Pattern.compile(MainFormLogic.keys[i]);
        }
    }

    public static void initMsgReserver() {
        MsgReceiverInit.init(MsgSender.instance);
    }
}
