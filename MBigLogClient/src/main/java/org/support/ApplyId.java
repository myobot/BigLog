package org.support;

/**
 * @author wangzhanwei
 */
public class ApplyId {
    private static long id = 0;

    public static long applyId() {
        return ++id;
    }
}
