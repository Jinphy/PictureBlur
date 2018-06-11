package com.example.jinphy.pictureblur.utils;

import com.example.jinphy.pictureblur.custom.SChain;

/**
 * DESC:
 * Created by jinphy on 2018/6/11.
 */

public class StringUtils {


    /**
     * DESC: 格式化角度字符串
     * Created by jinphy, on 2018/1/10, at 20:49
     */
    public static CharSequence formatDegree(Object value) {
        return SChain.with(value).append("o").superscript(0.5f).make();
    }
}
