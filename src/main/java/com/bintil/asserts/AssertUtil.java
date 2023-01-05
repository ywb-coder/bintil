package com.bintil.asserts;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author ：ywb
 * @date ：Created in 2023/1/5 10:02
 */
@SuppressWarnings({"unused", "uncheck"})
public class AssertUtil {

    /**
     * 真断言
     *
     * @param expression 表达式 ： 2 == 2
     * @param exception  RuntimeException以及所有自定义子类
     */
    public static void isTure(boolean expression, RuntimeException exception) {
        if (expression) {
            throw exception;
        }
    }

    /**
     * 是否属于
     *
     * @param superType 父类型
     * @param subType   子类型
     * @param exception RuntimeException以及所有自定义子类
     */
    public static void isInstanceOf(Class<?> superType, Class<?> subType, RuntimeException exception) {
        isEmpty(superType, exception);
        isEmpty(subType, exception);
        if (subType != superType) {
            throw exception;
        }
    }

    /**
     * 是否没有数据
     *
     * @param object    数据
     * @param exception RuntimeException以及所有自定义子类
     */
    public static void isEmpty(Object object, RuntimeException exception) {
        if (object == null) {
            throw exception;
        } else if (object instanceof Optional) {
            if (!((Optional<?>) object).isPresent()) {
                throw exception;
            }
        } else if (object instanceof CharSequence) {
            if (((CharSequence) object).length() == 0) {
                throw exception;
            }
        } else if (object.getClass().isArray()) {
            if (Array.getLength(object) == 0) {
                throw exception;
            }
        } else if (object instanceof Collection) {
            if (((Collection<?>) object).isEmpty()) {
                throw exception;
            }
        } else if (object instanceof Map) {
            if (((Map<?, ?>) object).isEmpty()) {
                throw exception;
            }
        }
    }
}
