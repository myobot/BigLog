package org.ui;

import javax.swing.*;
import java.awt.event.*;

public class NewVersionDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;

    private JTextPane annontionTextPane;

    public NewVersionDialog(JFrame owner) {
        super(owner);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public JTextPane getAnnontionTextPane() {
        return annontionTextPane;
    }

    public void setAnnontionTextPane(JTextPane annontionTextPane) {
        this.annontionTextPane = annontionTextPane;
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
        NewVersionDialog dialog = new NewVersionDialog(new JFrame());
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
