package com.ken.base.utils;

/**
 * 常量类
 *
 * @author by ken on 2018/5/20
 */
public class BaseConstant {

    public final static String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    /**
     * Bugly 应用升级appid
     */
    public final static String BUGLY_APP_ID = "09af291644";
    /**
     * Bmob APP_KEY
     */
    public final static String BMOB_APP_KEY = "ec859510598bf688d0ab051f3d60d52c";
    /**
     * 阿里云云市场API 通用 APP_CODE
     */
    public final static String APP_CODE = "9ba9b3c6ecc6459cbb3b8ed04dceecad";
    public final static String HEADER = "Authorization";
    public final static String VALUES = "APPCODE ";

    /***
     * 阿里云快递查询API相关
     * 请求方式GET
     * * */
    public final static String HOST = "https://cexpress.market.alicloudapi.com";
    public final static String PATH = "/cexpress";


    /**
     * 阿里云天气查询API相关
     * 请求方式POST
     */
    public final static String WEATHER_HOST = "http://freecityid.market.alicloudapi.com";
    /**
     * 精简预报3天
     */

    public final static String WEATHER_PATH_3_DAYS = "/whapi/json/alicityweather/briefforecast3days";

    /**
     * 精简实况
     */
    public final static String WEATHER_PATH = "/whapi/json/alicityweather/briefcondition";

    /**
     * 精简AQI
     */
    public final static String WEATHER_PATH_AQI = "/whapi/json/alicityweather/briefaqi";

    /**
     * UI相关
     */
    public static final int SHOW_DIALOG = 0x0002;
    public static final int DISMISS_DIALOG = 0x0003;
    public static final int SHOW_TOAST = 0x0004;

    /**
     * 扫码回调code
     */

    public static final int REQUEST_CODE = 0x0001;
    /**
     * 电商ID
     */
    public final static String EBusinessID = "1304075";


    /**
     *
     * 快递鸟在线下单接口
     *
     * @技术QQ: 4009633321
     * @技术QQ群: 200121393
     * @see: http://www.kdniao.com/api-order
     * @copyright: 深圳市快金数据技术服务有限公司
     *
     * ID和Key请到官网申请：http://www.kdniao.com/ServiceApply.aspx
     */
    /**
     * 电商加密私钥，快递鸟提供，注意保管，不要泄漏
     */
    public final static String AppKey = "4cd04985-b87b-4628-b47c-6fab09a9bd82";
    /**
     * 测试请求url
     */
    public final static String ReqURL = "http://testapi.kdniao.cc:8081/api/oorderservice";
    /**
     * 快递鸟支持预约取件的快递公司：顺丰、百世、中通、圆通、韵达、优速、德邦、邮政国内标快。
     */
    public final static String[] COM = {
            "顺丰速递",
            "百世快递",
            "中通快递",
            "圆通速递",
            "优速快递",
            "德邦快递",
            "邮政国内标快"};
    /**正式请求url*/
    //public final static String ReqURL = "http://api.kdniao.cc/api/OOrderService";

    /**
     * 程序退出
     */
    public static class RunTime {
        public static final int TIME = 2000;
        public static long EXIT_TIME = 0;
    }
}
