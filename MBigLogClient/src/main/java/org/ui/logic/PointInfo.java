package org.ui.logic;

/**
 * @author wangzhanwei
 */
public class PointInfo {
    private String typeId;
    private int length;

    public PointInfo(String typeId, int length) {
        this.typeId = typeId;
        this.length = length;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}