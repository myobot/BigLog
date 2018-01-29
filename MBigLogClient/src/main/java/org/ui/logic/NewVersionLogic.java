package org.ui.logic;

import javax.swing.JFrame;
import org.ui.BigLogClientMainForm;
import org.ui.ConstantUI;
import org.ui.NewVersionDialog;

/**
 * @author wangzhanwei
 */
public class NewVersionLogic {
    public static void openNewDialog(String annontaion) {
        BigLogClientMainForm.frame.setExtendedState(JFrame.NORMAL);
        BigLogClientMainForm.getFrame().setVisible(true);
        NewVersionDialog dialog = new NewVersionDialog(BigLogClientMainForm.frame);
        dialog.setIconImage(ConstantUI.IMAGE_ICON);
        dialog.pack();
        dialog.setSize(ConstantUI.NEW_VERSION_DIALOG_WIDTH, ConstantUI.NEW_VERSION_DIALOG_HEIGHT);
        int fX = dialog.getParent().getX();
        int fY = dialog.getParent().getY();
        int fWidth = dialog.getParent().getWidth();
        int fHeight = dialog.getParent().getHeight();
        int mFixWidth = (fWidth - ConstantUI.NEW_VERSION_DIALOG_WIDTH) / 2;
        int mFixHeight = (fHeight - ConstantUI.NEW_VERSION_DIALOG_HEIGHT) / 2;
        dialog.setLocation(fX + mFixWidth, fY + mFixHeight);
        dialog.getAnnontionTextPane().setText("内容：\n" + annontaion + "\n");
        dialog.setTitle(ConstantUI.NEW_HINT_DIALOG_NAME);
        dialog.setVisible(true);
    }
}
