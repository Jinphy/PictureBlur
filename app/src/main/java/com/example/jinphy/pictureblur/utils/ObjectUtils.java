package com.example.jinphy.pictureblur.utils;

import java.lang.ref.Reference;
import java.util.Collection;

/**
 * Created by jinphy on 2017/12/5.
 */
public class ObjectUtils {


    /**
     * DESC: 检测非空
     * Created by jinphy, on 2017/12/5, at 22:48
     */
    public static void requireNonNull(Object object, String msg) {
        if (object == null) {
            throw new NullPointerException(msg);
        }
    }

    /**
     * DESC: 判断引用是否可用
     * Created by jinphy, on 2017/12/30, at 16:51
     */
    public static boolean reference(Reference reference) {
        return reference != null && reference.get() != null;
    }


    /**
     * DESC:
     * Created by jinphy, on 2017/12/30, at 16:52
     */
    public static void throwRuntime(String msg) {
        throw new RuntimeException(msg);
    }

    /**
     * DESC:
     * Created by jinphy, on 2017/12/30, at 17:20
     */
    public static void throwIlleagalArgs(String msg) {
        throw new IllegalArgumentException(msg);
    }



    /**
     * DESC: 判断对象是否为空
     * Created by jinphy, on 2017/12/30, at 16:55
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * DESC: 判断对象是否不为空
     * Created by jinphy, on 2017/12/30, at 16:56
     */
    public static boolean isNoNull(Object object) {
        return object != null;
    }

    /**
     * DESC: 判读数组是否为空
     * Created by jinphy, on 2017/12/30, at 16:58
     */
    public static <T> boolean isEmpty(T[] values) {
        return values == null || values.length == 0;
    }

    /**
     * DESC: 判断字符串是否为空
     * Created by jinphy, on 2017/12/30, at 17:04
     */
    public static boolean isEmpty(CharSequence value) {
        return value == null || value.length() == 0;
    }

    /**
     * DESC: 判断字符串是否为空或者只有空格
     * Created by jinphy, on 2017/12/30, at 17:06
     */
    public static boolean isTrimEmpty(CharSequence value) {
        return value == null || value.toString().trim().length() == 0;
    }

    /**
     * DESC: 判断集合是否为空
     * Created by jinphy, on 2017/12/30, at 17:09
     */
    public static boolean isEmpty(Collection values) {
        return values == null || values.isEmpty();
    }

}











































