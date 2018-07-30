package com.ken.expressquery.utils;


import android.util.Log;

/**
 * 截取地址信息工具类
 *
 * @author by ken on 2018/5/31
 */
public class InterceptAddressInfo {
    /**
     * 获取省
     */
    static public String getAddressInfo(String address, int index) {
        String province = null, city = null, area = null, addr = null;
        int[] j = {0, 0, 0, 0};
        try {
            int m = 0;
            int len = address.length();
            for (int i = 0; i < 4; i++) {
                m = address.indexOf(" ", m + 1);
                j[i] = m;
            }
            province = address.substring(0, j[0]);
            city = address.substring(j[0] + 1, j[1]);
            area = address.substring(j[1] + 1, j[2]);
            addr = address.substring(j[2] + 1, len);

        } catch (Exception ex) {
            Log.e("InterceptAddressInfo", ex.getMessage());
        } finally {
            if (index == 1) {
                return province;
            } else if (index == 2) {
                return city;
            } else if (index == 3) {
                return area;
            } else if (index == 4) {
                return addr;
            } else {
                return null;
            }
        }
    }
}
