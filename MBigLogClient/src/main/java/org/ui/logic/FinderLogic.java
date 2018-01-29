package org.ui.logic;

import org.gen.MsgTrans;
import org.support.ApplyId;
import org.ui.BigLogClientMainForm;

/**
 * @author wangzhanwei
 */
public class FinderLogic {
    public static long nowid = 0;
    public static String sentenceCache = "";
    public static boolean matchCaseCache = false;
    public static boolean rowNumCache = false;
    public static int rowCache = 0;
    private static boolean flag = false;

    public static int checkCount() {
        String tcount = BigLogClientMainForm.mainWindow.getCounttextField1().getText();
        int count;
        try {
            count = Integer.parseInt(tcount);
        } catch (Exception e) {
            return -1;
        }
        return count;
    }

    public static int checkTotalCount() {
        String tcount = BigLogClientMainForm.mainWindow.getCountTextPane().getText();
        int count;
        try {
            count = Integer.parseInt(tcount);
        } catch (Exception e) {
            return -1;
        }
        return count;
    }

    public static void sendCSFindLog(String sentence, int orderModel, int count, int totalCount, int matchCase, int wholeWords, int showRowNum, int usePattern) {
        long id = ApplyId.applyId();
        MsgTrans.CSFindLog.Builder msg = MsgTrans.CSFindLog.newBuilder();
        msg.setId(id);
        msg.setCount(count);
        msg.setTotalCount(totalCount);
        msg.setSentence(sentence);
        msg.setOrderModel(orderModel);
        msg.setMatchCase(matchCase);
        msg.setWholeWords(wholeWords);
        msg.setShowRowNum(showRowNum);
        msg.setUsePattern(usePattern);
        if (BigLogClientMainForm.serverLinker != null) {
            BigLogClientMainForm.serverLinker.sendMsg(msg);
            nowid = id;
            sentenceCache = sentence;
            if (BigLogClientMainForm.mainWindow.getCaseCheckBox().isSelected()) {
                matchCaseCache = true;
            }
            if (BigLogClientMainForm.mainWindow.getShowRowNumCheckBox().isSelected()) {
                rowNumCache = true;
            }
            rowCache = 0;
            BigLogClientMainForm.mainWindow.getAnswerTextPane().setText("查询中...");
            flag = true;
        }
    }

    public static void onSCFindLogPush(long id, String answer) {
        if (id != nowid) {
            return;
        }
        if (answer.equals("") || answer == null) {
            BigLogClientMainForm.mainWindow.getAnswerTextPane().setText("服务器查询失败！请稍后再试");
            return;
        }
        if (flag) {
            BigLogClientMainForm.mainWindow.getAnswerTextPane().setText("");
            flag = false;
        }
        if (rowNumCache) {
            StringBuilder sb = new StringBuilder(answer);
            sb.insert(0, ++rowCache + ": \t");
            int index = 0;
            while ((index = sb.indexOf("\n", index)) != -1) {
                if (index == sb.length() - 1) {
                    break;
                }
                String s = ++rowCache + ": \t";
                sb.insert(index + 1, s);
                index += s.length() + 1;
            }
            answer = sb.toString();
        }
        InsertLogLogic.matchAndChangeColor("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n", BigLogClientMainForm.mainWindow.getAnswerTextPane(), "", false, matchCaseCache);
        InsertLogLogic.matchAndChangeColor(answer.endsWith("\n") ? answer : answer + "\n", BigLogClientMainForm.mainWindow.getAnswerTextPane(), "", false, matchCaseCache);
    }

    public static void onSCFindLogFinish(long id) {
        if (id != nowid) {
            return;
        }
        InsertLogLogic.matchAndChangeColor("查询完成!", BigLogClientMainForm.mainWindow.getAnswerTextPane(), "", false, matchCaseCache);
    }
}
