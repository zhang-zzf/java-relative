package com.github.zzf.dd.utils;

import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Set;

import static com.github.zzf.dd.utils.LogUtils.json;

@Slf4j
public class Objects {

    public static boolean equals(Object a, Object b) {
        boolean equals = java.util.Objects.equals(a, b);
        if (!equals) {
            String type = a != null ? a.getClass().getName() : b.getClass().getName();
            // 获取调用栈信息
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            String callerMethod = "unknown";
            if (stackTrace.length > 2) {
                StackTraceElement caller = stackTrace[2]; // 索引2是调用equals的方法
                callerMethod = caller.getClassName() + "." + caller.getMethodName() + ":" + caller.getLineNumber();
            }
            log.info("Objects.equals false -> caller: {}, type: {}, val1: {}, val2: {}",
                callerMethod, type, a, b);
        }
        return equals;
    }

    public static boolean equalsFast(Object obj1, Object obj2, @NotNull Set<String> ignoreFields) {
        return doEquals(obj1, obj2, ignoreFields, false, false);
    }

    /**
     * 比较两个对象的字段是否相等，支持忽略指定字段
     *
     * @param obj1 第一个对象
     * @param obj2 第二个对象
     * @param ignoreFields 要忽略的字段集合
     * @return 如果所有参与比对的字段都相等，则返回 true；否则返回 false
     */
    public static boolean equals(Object obj1, Object obj2, @NotNull Set<String> ignoreFields) {
        return doEquals(obj1, obj2, ignoreFields, true, true);
    }

    private static boolean doEquals(Object obj1, Object obj2,
                                    Set<String> ignoreFields,
                                    boolean checkAll,
                                    boolean logNotEqual) {
        if (obj1 == null) {
            return obj2 == null;
        }
        // obj1 != null
        if (obj2 == null) {
            return false;
        }
        Class<?> obj1Clazz = obj1.getClass();
        if (!obj1Clazz.equals(obj2.getClass())) {
            throw new IllegalArgumentException("两个对象必须是同一类型");
        }
        Field[] fields = obj1Clazz.getDeclaredFields();
        boolean same = true;
        for (Field field : fields) {
            boolean fieldEqual;
            // 如果字段在忽略列表中，跳过比对
            if (ignoreFields.contains(field.getName())) {
                continue;
            }
            try {
                field.setAccessible(true); // 确保可以访问私有字段
                Object value1 = field.get(obj1);
                Object value2 = field.get(obj2);
                if (value1 instanceof BigDecimal && value2 instanceof BigDecimal) {/* value1 / value2 都必须 != null */
                    fieldEqual = (0 == ((BigDecimal) value1).compareTo((BigDecimal) value2));
                }
                // 如果字段值不同，返回 false
                else {
                    fieldEqual = java.util.Objects.equals(value1, value2);
                }
                if (!fieldEqual && logNotEqual) {
                    log.info("Objects.equals false -> field: {}, objectId: {}, val: {}, {}",
                            obj1Clazz.getName() + "#" + field.getName(), objectId(obj1), json(value1), json(value2));
                }
                same &= fieldEqual;
                if (!same && !checkAll) {
                    break;
                }
            } catch (Exception e) {
                log.error("Objects.equals error -> field: {}", fieldName(obj1Clazz.getName(), field), e);
                // throw new RuntimeException("Objects.equals error -> field: " + field.getName(), e);
                // 在异步线程中， 异常可能被忽略。
                same = false;
            }
        }
        return same;
    }

    private static String objectId(Object obj) {
        try {
            Class<?> objClazz = obj.getClass();
            Method toIdString = objClazz.getMethod("toIdString");
            return (String) toIdString.invoke(obj);
        } catch (Exception e) {// ignore
        }
        return "";
    }

    private static String fieldName(String obj1Clazz, Field field) {
        return obj1Clazz + "#" + field.getName();
    }

}
