package org.ui.listener;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import org.ui.BigLogClientMainForm;
import org.ui.logic.MainFormLogic;
/**
 * @author wangzhanwei
 */
public class MainSrollPaneListener {
    static JScrollPane pane = BigLogClientMainForm.mainWindow.getLogAreaScroll();

    public static void addListener() {
        JScrollBar bar = BigLogClientMainForm.mainWindow.getLogAreaScroll().getVerticalScrollBar();
        bar.addAdjustmentListener(e -> {
//                if(e.getValue() >= bar.getMaximum()-20){
//                    MainFormLogic.autoUpdateScroll = true;
//                }else{
//                    MainFormLogic.autoUpdateScroll = false;
//                }
//                System.out.println("viewPintY:"+pane.getViewport().getViewPosition().getY());
        });
        pane.addMouseWheelListener(e -> {
            if (MainFormLogic.autoUpdateScroll) {
                MainFormLogic.autoUpdateScroll = false;
                if (BigLogClientMainForm.mainWindow.getAutoFlushCheckBox().isSelected()) {
                    BigLogClientMainForm.mainWindow.getAutoFlushCheckBox().setSelected(false);
                }
            }
        });
    }
}
