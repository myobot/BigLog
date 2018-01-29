package org.ui.listener;

import javax.swing.JFrame;
import org.ui.BigLogClientMainForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author wangzhanwei
 */
public class TrayIconListener {
    public static void addListener() {
        BigLogClientMainForm.trayIcon.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    BigLogClientMainForm.frame.setExtendedState(JFrame.NORMAL);
                    BigLogClientMainForm.frame.setVisible(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        BigLogClientMainForm.show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == BigLogClientMainForm.show) {
                    BigLogClientMainForm.frame.setExtendedState(JFrame.NORMAL);
                    BigLogClientMainForm.frame.setVisible(true);
                }
            }
        });

        BigLogClientMainForm.exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == BigLogClientMainForm.exit) {
                    if (BigLogClientMainForm.serverLinker != null) {
                        BigLogClientMainForm.serverLinker.close();
                    }
                    BigLogClientMainForm.systemTray.remove(BigLogClientMainForm.trayIcon);
                    System.exit(0);
                }
            }
        });
    }
}
