package org.ui.listener;

import org.support.Config;
import org.ui.BigLogClientMainForm;
import org.ui.logic.CloseLogic;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author wangzhanwei
 */
public class MainFrameListener {
    @SuppressWarnings("unchecked")
    public static void addListener() {
        BigLogClientMainForm.frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (Config.NO_MORE_NOTICE == 1) {
                    switch (Config.CLOSE_MODEL) {
                        case 0:
                            BigLogClientMainForm.frame.dispose();
                            break;
                        case 1:
                            if (BigLogClientMainForm.serverLinker != null) {
                                BigLogClientMainForm.serverLinker.close();
                            }
                            BigLogClientMainForm.systemTray.remove(BigLogClientMainForm.trayIcon);
                            System.exit(0);
                            break;
                    }
                } else {
                    CloseLogic.openNewCloseDialog();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

                BigLogClientMainForm.frame.dispose();
            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

    }
}
