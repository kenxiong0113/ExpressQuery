package com.ken.base.utils;

/**
 * @author by ken on 2017/9/21.
 * 快递公司字符与代码的转换
 */

public class Transform {
    private static String[] com = {
            "EMS",
            "中通快递",
            "申通快递",
            "圆通速递",
            "韵达快递",
            "天天快递",
            "快捷速递",
            "顺丰速递",
            "宅急送",
            "京东快递",
            "顺丰速运",
            "百世快递",
            "优速快递",
            "德邦快递",
            "邮政国内标快"};
    private static String[] no = {
            "EMS",
            "ZTO",
            "STO",
            "YTO",
            "YD",
            "HHTT",
            "FAST",
            "SF",
            "ZJS",
            "JD",
            "SF",
            "HTKY",
            "UC",
            "DBL",
            "YZBK"};
    private static String code;
    private static String company;

    /**
     * 快递公司名字转换成代码
     *
     * @param company 快递公司名字
     * @return 快递公司代码
     */
    public static String transform(String company) {
        for (int i = 0; i < com.length; i++) {
            if (company.equals(com[i])) {
                code = no[i];
            }
        }
        return code;
    }

    /**
     * 快递公司名字转换成代码
     *
     * @param code 快递公司代码
     * @return 快递公司名字
     */
    public static String codeToCom(String code) {
        for (int j = 0; j < no.length; j++) {
            if (code.equals(no[j])) {
                Transform.company = com[j];
            }
        }
        return company;
    }


}
