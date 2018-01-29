package org.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import org.ui.logic.ErrorNoticeLogic;


public class ErrorNotice extends JDialog {
    private JPanel contentPane;
    private JButton locate;
    private JPanel informationPanel;
    private JTextPane informationTextPanel;
    private int x, y;
    private static Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    private long id;
    private int length;
    private int pointIndex;
    private String message;
    private Timer timer = new Timer(8000, e -> {
        ErrorNoticeLogic.closeDialog(ErrorNotice.this.id);
        ErrorNotice.this.dispose();
    });

    public ErrorNotice(long id, String message, int pointIndex, JFrame owner) {
        super(owner);
        this.id = id;
        this.pointIndex = pointIndex;
        setContentPane(contentPane);
        getRootPane().setDefaultButton(locate);

        locate.addActionListener(e -> onLocate());


        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                timer.restart();
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.message = message;
        this.informationTextPanel.setEditorKit(new WarpEditorKit());
        this.informationTextPanel.setText(message);
        this.timer.start();
    }

    private void onLocate() {
        // add your code here
        ErrorNoticeLogic.onClickLocate(this.length, this.pointIndex);
        ErrorNoticeLogic.closeDialog(this.id);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        ErrorNoticeLogic.closeDialog(this.id);
        dispose();
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private class WarpEditorKit extends StyledEditorKit {

        private ViewFactory defaultFactory = new WarpColumnFactory();

        @Override
        public ViewFactory getViewFactory() {
            return defaultFactory;
        }
    }

    private class WarpColumnFactory implements ViewFactory {

        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new WarpLabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new ParagraphView(elem);
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new BoxView(elem, View.Y_AXIS);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }

            // default to text display
            return new LabelView(elem);
        }
    }

    private class WarpLabelView extends LabelView {

        public WarpLabelView(Element elem) {
            super(elem);
        }

        @Override
        public float getMinimumSpan(int axis) {
            switch (axis) {
                case View.X_AXIS:
                    return 0;
                case View.Y_AXIS:
                    return super.getMinimumSpan(axis);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
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
        JFrame jFrame = new JFrame();
        jFrame.setSize(600, 600);
        jFrame.setVisible(true);
        ErrorNotice dialog = new ErrorNotice(1, "abc11111111111 11111 1111111 11111 11111111 1111111 11111 111111 adfa  32123", 1, jFrame);

        dialog.pack();
        dialog.setSize(200, 150);
        dialog.setIconImage(ConstantUI.IMAGE_ICON);
        int x = (int) (dimension.getWidth() - dialog.getWidth() + 5);
        int y = (int) (dimension.getHeight() - dialog.getHeight() - 25);
        dialog.setLocation(x, y);
        dialog.setVisible(true);
        System.exit(0);
    }
}
