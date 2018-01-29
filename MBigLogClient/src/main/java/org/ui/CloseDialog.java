package org.ui;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.support.Config;
import org.support.LogCore;
import java.util.Enumeration;

public class CloseDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JCheckBox CheckBox;
    private JRadioButton minButton;
    private JRadioButton closeButton;
    private ButtonGroup buttonGroup;

    public CloseDialog(JFrame owner) {
        super(owner, true);
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());


        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onOK(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(minButton);
        buttonGroup.add(closeButton);
        minButton.setSelected(true);
    }

    private void onOK() {
        // add your code here
        String enable = "false";
        int index = -1;
        Enumeration<AbstractButton> radioBtns = buttonGroup.getElements();
        while (radioBtns.hasMoreElements()) {
            AbstractButton btn = radioBtns.nextElement();
            if (btn.isSelected()) {
                enable = btn.getText();
                break;
            }
        }

        try {
            switch (enable) {
                case ConstantUI.MIN_BUTTON_TEXT:
                    if (CheckBox.isSelected()) {
                        Config.NO_MORE_NOTICE = 1;
                    }
                    break;
                case ConstantUI.CLOSE_BUTTON_TEXT:
                    if (BigLogClientMainForm.serverLinker != null) {
                        BigLogClientMainForm.serverLinker.close();
                    }
                    BigLogClientMainForm.systemTray.remove(BigLogClientMainForm.trayIcon);
                    if (CheckBox.isSelected()) {
                        Config.NO_MORE_NOTICE = 1;
                    }
                    System.exit(0);
                    break;
            }
        } catch (Exception e) {
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        Init.initGlobalFont();


        CloseDialog dialog = new CloseDialog(new JFrame());

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
        dialog.setVisible(true);
        System.exit(0);
    }
}
