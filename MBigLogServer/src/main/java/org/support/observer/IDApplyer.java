package org.support.observer;

/**
 * @author wangzhanwei
 */
public class IDApplyer {
    public static long id = 0;
    public static long applyId(){
        return ++id;
    }
}
