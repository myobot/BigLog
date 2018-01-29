package org.ui.logic;

import java.awt.Color;
import javax.swing.JTextPane;
import org.support.LogCore;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangzhanwei
 */
public class InsertLogLogic {
    public static SimpleAttributeSet defaultAttrset = new SimpleAttributeSet();
    public static SimpleAttributeSet colorAttrset = new SimpleAttributeSet();
    public static String[] keys = {".*(\\[info\\]).*", ".*(\\[error\\]).*", ".*(\\[warn\\]).*", ".*(exception).*", ".*(\\[debug\\]).*", ".*(\\[trace\\]).*"};
    public static Pattern[] patterns = {Pattern.compile(keys[0]), Pattern.compile(keys[1]), Pattern.compile(keys[2]), Pattern.compile(keys[3]), Pattern.compile(keys[4]), Pattern.compile(keys[5])};
    public static Color[] colors = {new Color(72, 187, 49), new Color(255, 0, 6), new Color(187, 187, 35), new Color(0, 112, 187)};
    public static Color filterWroldColor = new Color(33, 66, 131);
    public static Color background = new Color(60, 63, 65);

    public static void matchAndChangeColor(String log, JTextPane pane, String filterWorld, boolean filter, boolean matchCase) {
        if (filter) {
            filterInsertLine(filterWorld, log, matchCase, getIndex(log), true, pane);
        } else {
            insertLast(log, getIndex(log), false, pane);
        }
    }
    public static void setTextChangeColor(String log,JTextPane pane){
        setText(log,getIndex(log),pane);
    }

    private static void setText(String line,int index,JTextPane pane) {
        Document docs = pane.getDocument();//获得文本对象
        try {
            docs.remove(0, docs.getLength());
            docs.insertString(docs.getLength(), line, getSimpleAttributeSet(index));//对文本进行追加
        } catch (BadLocationException e) {
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        }
        if (MainFormLogic.autoUpdateScroll) {
            pane.setCaretPosition(pane.getDocument().getLength());
        }
    }

    public static void filterInsertLine(String filter, String line, boolean matchCase, int keyIndex, boolean flag, JTextPane pane) {
        int index = -1;
        if (matchCase) {
            line.indexOf(filter);
        } else {
            String tempFilter = filter.toLowerCase();
            index = line.toLowerCase().indexOf(tempFilter);
        }
        if (index != -1) {
            String s1 = line.substring(0, index);
            insertLast(s1, keyIndex, false, pane);
            insertLast(filter, keyIndex, true, pane);
            String s2 = line.substring(index + filter.length(), line.length());
            filterInsertLine(filter, s2, matchCase, keyIndex, false, pane);
        } else {
            if (!flag) {
                insertLast(line, keyIndex, false, pane);
            }
        }
    }
    private static int getIndex(String log){
        int index = -1;
        for (int i = 0; i < keys.length; i++) {
            Matcher m = patterns[i].matcher(log.toLowerCase());
            if (m.find()) {
                index = i;
                break;
            }
        }
        return index;
    }
    private static SimpleAttributeSet getSimpleAttributeSet(int index){
        SimpleAttributeSet simpleAttributeSet = colorAttrset;
        if (index == -1) {
            simpleAttributeSet = defaultAttrset;
        } else {
            switch (index) {
                case 0:
                    StyleConstants.setForeground(simpleAttributeSet, colors[0]);
                    break;
                case 1:
                    StyleConstants.setForeground(simpleAttributeSet, colors[1]);

                    break;
                case 2:
                    StyleConstants.setForeground(simpleAttributeSet, colors[2]);

                    break;
                case 3:
                    StyleConstants.setForeground(simpleAttributeSet, colors[1]);
                    break;
                case 4:
                    StyleConstants.setForeground(simpleAttributeSet, colors[3]);
            }
        }
        return simpleAttributeSet;
    }

    public static void insertLast(String line, int index, boolean filter, JTextPane pane) {
        if (filter) {
            StyleConstants.setBackground(getSimpleAttributeSet(index), filterWroldColor);
        } else {
            StyleConstants.setBackground(getSimpleAttributeSet(index), background);
        }
        insertLast(line, getSimpleAttributeSet(index), pane);
    }

    public static void insertLast(String line, SimpleAttributeSet simpleAttributeSet, JTextPane pane) {
        Document docs = pane.getDocument();//获得文本对象
        try {
            docs.insertString(docs.getLength(), line, simpleAttributeSet);//对文本进行追加
        } catch (BadLocationException e) {
            LogCore.core.error(e.getMessage());
            e.printStackTrace();
        }
        if (MainFormLogic.autoUpdateScroll) {
            pane.setCaretPosition(pane.getDocument().getLength());
        }
    }
}
