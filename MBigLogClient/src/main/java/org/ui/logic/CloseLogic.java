package org.ui.logic;

import org.ui.BigLogClientMainForm;
import org.ui.CloseDialog;
import org.ui.ConstantUI;

/**
 * @author wangzhanwei
 */
public class CloseLogic {
    public static void openNewCloseDialog() {
        CloseDialog dialog = new CloseDialog(BigLogClientMainForm.frame);
        dialog.setIconImage(ConstantUI.IMAGE_ICON);
        dialog.pack();
        dialog.setSize(ConstantUI.CLOSE_DIALOG_WIDTH, ConstantUI.CLOSE_DIALOG_HEIGHT);
        int fX = dialog.getParent().getX();
        int fY = dialog.getParent().getY();
        int fWidth = dialog.getParent().getWidth();
        int fHeight = dialog.getParent().getHeight();
        int mFixWidth = (fWidth - ConstantUI.CLOSE_DIALOG_WIDTH) / 2;
        int mFixHeight = (fHeight - ConstantUI.CLOSE_DIALOG_HEIGHT) / 2;
        dialog.setLocation(fX + mFixWidth, fY + mFixHeight);
        dialog.setTitle(ConstantUI.CLOSE_DIALOT_NAME);
        dialog.setVisible(true);
    }
}
