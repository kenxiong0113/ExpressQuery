package com.ken.expressquery.utils;


import android.util.Log;

/**
 * 计算运费实现类
 *
 * @author by ken on 2018/5/30
 * */
public class CalculateShippingCosts {
        public static String calculate(String str){
            double cast = 12;
            try {
                int k = Integer.valueOf(str);
                cast = 12.00+2.50*(k-1);
            }catch (Exception ex){
                Log.e("CalculateShippingCosts", ex.getMessage());
            }


            return String.valueOf(cast);
        }
}
