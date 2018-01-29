package org.support.function;

/**
 * @author wangzhanwei
 */
public class CommonFunction {
    private int paramCount = 0;
    private Object function;
    public CommonFunction(Object function,int paramCount){
        this.function = function;
        this.paramCount = paramCount;
    }
    public int getParamCount(){
        return this.paramCount;
    }
    public void apply(Object... m){
        assert m.length == this.paramCount;
        switch (m.length) {
            case 0:
                ((Function0) function).apply();
                break;
            case 1:
                ((Function1) function).apply(m[0]);
                break;
            case 2:
                ((Function2) function).apply(m[0], m[1]);
                break;
        }
    }
    public <T> T getFunc(){
        return (T) this.function;
    }
}
