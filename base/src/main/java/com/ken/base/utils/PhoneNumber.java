package com.ken.base.utils;

/**
 * 判断手机号码是否正确
 *
 * @author by ken on 2017/9/23.
 */

public class PhoneNumber {
    static boolean pn = false;

    public static boolean PhoneNumber(int phone3) {
        if (phone3 == 130 || phone3 == 132 || phone3 == 133 || phone3 == 134 ||
                phone3 == 135 || phone3 == 136 || phone3 == 137 || phone3 == 138 || phone3 == 139 ||
                phone3 == 150 || phone3 == 151 || phone3 == 152 || phone3 == 153 || phone3 == 155 ||
                phone3 == 156 || phone3 == 157 || phone3 == 158 || phone3 == 159 || phone3 == 147 ||
                phone3 == 145 || phone3 == 180 || phone3 == 185 || phone3 == 186 || phone3 == 187 ||
                phone3 == 188 || phone3 == 189) {
            pn = true;
        } else {
            pn = false;
        }
        return pn;
    }
}
