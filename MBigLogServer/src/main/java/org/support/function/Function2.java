package org.support.function;

/**
 * @author wangzhanwei
 */
@FunctionalInterface
public interface Function2<T1,T2> {
    void apply(T1 var1,T2 var2);
}
