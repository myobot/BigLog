package org.ui.logic;
/**
 * @author wangzhanwei
 */
public enum EnumDefaultFilter {
    INFO(0),
    ERROR(1),
    WARN(2),
    EXCEPTION(3),
    DEBUG(4),
    TRACE(5),;

    private int index;

    EnumDefaultFilter(int i) {
        this.index = i;
    }

    /**
     * 通过属性索引，得到属性枚举
     *
     * @param index
     * @return
     */
    public static EnumDefaultFilter getByIndex(int index) {
        EnumDefaultFilter key = null;
        for (EnumDefaultFilter k : EnumDefaultFilter.values()) {
            if (k.index == index) {
                key = k;
                break;
            }
        }
        return key;
    }

    public int getIndex() {
        return index;
    }
}
