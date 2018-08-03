package com.ken.base.utils;

/**
 * 判断字符串是否为全数字
 */
public class Numeric {

    /**
     * 方法五：用ascii码
     */

    public static boolean isNumeric5(String str) {
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57) {
                return false;
            }
        }
        return true;
    }

    /**
     * Java中判断字符串是否为数字的五种方法
     * <p>
     * 方法一：用JAVA自带的函数
     *
     * @param str 字符串
     */
    public boolean isNumeric1(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
