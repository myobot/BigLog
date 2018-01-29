package org.ui.logic;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import org.support.ApplyId;
import org.support.Config;
import org.support.LogCore;
import org.support.Template;
import org.ui.BigLogClientMainForm;
import org.ui.ConstantUI;
import org.ui.ErrorNotice;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author wangzhanwei
 */
public class ErrorNoticeLogic {
    private static Set<Long> openedDialog = new HashSet<Long>();
    private static Map<String, Long> exceptionsId = new HashMap<String, Long>();
    private static Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

    public static void openNewDialog(String Name, int templateID, int length, int pointIndex) {
        int index = Name.indexOf(Config.END_FLAG) + Config.END_FLAG.length();
        String key = Name.substring(index, Name.length());
        Long id = exceptionsId.get(key);
        if (id == null) {
            id = ApplyId.applyId();
            exceptionsId.put(key, id);
        }
        if (!openedDialog.contains(id)) {
            StringBuilder s = new StringBuilder(Template.templates[templateID]);
            s.replace(s.indexOf("{}"), s.indexOf("{}") + 1, Name);
            ErrorNotice dialog = new ErrorNotice(id, s.toString(), pointIndex, BigLogClientMainForm.frame);
            dialog.setIconImage(ConstantUI.IMAGE_ICON);
            dialog.pack();
            dialog.setSize(ConstantUI.ERROR_NOTICE_WIDTH, ConstantUI.ERROR_NOTICE_HEIGHT);
            int x = (int) (dimension.getWidth() - dialog.getWidth() + ConstantUI.ERROR_NOTICE_WIDTH_FIX);
            int y = (int) (dimension.getHeight() - dialog.getHeight() + ConstantUI.ERROR_NOTICE_HEIGHT_FIX);
            dialog.setTitle(ConstantUI.ERROR_FROM_NAME);
            dialog.setLength(length);
            dialog.setLocation(x, y);
            dialog.setAlwaysOnTop(true);
            dialog.setFocusableWindowState(false);
            dialog.setFocusable(false);
            dialog.setVisible(true);
            openedDialog.add(id);
        }
    }

    public static void closeDialog(long id) {
        openedDialog.remove(id);
    }

    public static void clearExceptionsId() {
        exceptionsId.clear();
    }

    public static void onClickLocate(int length, int pointIndex) {
        BigLogClientMainForm.mainWindow.getTabbedPane1().setSelectedIndex(0);
        MainFormLogic.autoUpdateScroll = false;
        if (BigLogClientMainForm.mainWindow.getAutoFlushCheckBox().isSelected()) {
            BigLogClientMainForm.mainWindow.getAutoFlushCheckBox().setSelected(false);
        }
        if (!BigLogClientMainForm.mainWindow.getAllCheckBox().isSelected()) {
            BigLogClientMainForm.mainWindow.getAllCheckBox().setSelected(true);
        }
        BigLogClientMainForm.frame.setExtendedState(JFrame.NORMAL);
        BigLogClientMainForm.frame.setVisible(true);
        JTextPane pane = BigLogClientMainForm.mainWindow.getLogArea();
        try {
            pane.setCaretPosition(length);
        } catch (Exception e) {
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        }
        MainFormLogic.nowPointIndex = pointIndex;
    }

    public static void linkClose() {
        exceptionsId.clear();
    }
}
