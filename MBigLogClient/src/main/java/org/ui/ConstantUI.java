package org.ui;


import java.awt.Image;
import java.awt.Toolkit;

/**
 * @author wangzhanwei
 */
public class ConstantUI {
    public final static String APP_NAME = "BigLog";
    public final static String APP_VERSION = "1.0.14.20171220";
    public final static String ERROR_FROM_NAME = "Error Toolkit";
    public final static int ERROR_NOTICE_WIDTH = 200;
    public final static int ERROR_NOTICE_HEIGHT = 150;
    public final static int ERROR_NOTICE_WIDTH_FIX = 5;
    public final static int ERROR_NOTICE_HEIGHT_FIX = -25;

    public final static String CLOSE_DIALOT_NAME = "Close Toolkit";
    public final static int CLOSE_DIALOG_WIDTH = 300;
    public final static int CLOSE_DIALOG_HEIGHT = 220;

    public final static String NEW_HINT_DIALOG_NAME = "New Hint!";
    public final static int NEW_VERSION_DIALOG_WIDTH = 300;
    public final static int NEW_VERSION_DIALOG_HEIGHT = 220;


    public final static String MIN_BUTTON_TEXT = "最小化系统托盘";
    public final static String CLOSE_BUTTON_TEXT = "关闭Big Log";

    public final static String ORDER_MODEL_1 = "从前向后";
    public final static String ORDER_MODEL_2 = "从后向前";

    public final static Image IMAGE_ICON = Toolkit.getDefaultToolkit().getImage(BigLogClientMainForm.class.getResource("/icon/logo3.png"));

}
