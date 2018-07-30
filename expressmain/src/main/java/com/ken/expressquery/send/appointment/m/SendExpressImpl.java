package com.ken.expressquery.send.appointment.m;

import android.util.Log;

import com.ken.expressquery.send.appointment.OnSendFinishCallBack;
import com.ken.expressquery.threadpool.ThreadPoolProxyFactory;
import com.ken.expressquery.utils.InterceptAddressInfo;
import com.ken.expressquery.utils.Transform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import static com.ken.expressquery.base.BaseConstant.AppKey;
import static com.ken.expressquery.base.BaseConstant.EBusinessID;
import static com.ken.expressquery.base.BaseConstant.ReqURL;

/**
 * 预约寄件实现类model层
 *
 * @author by ken on 2018/5/23
 */
public class SendExpressImpl implements SendExpressModel {
    private static SendExpressImpl instances = null;
    private static char[] base64EncodeChars = new char[]{
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/'};
    OutputStreamWriter out = null;
    BufferedReader in = null;
    StringBuilder result;
    OnSendFinishCallBack callBack;
    private String TAG = SendExpressImpl.class.getName();

    private SendExpressImpl() {
    }

    public static SendExpressImpl getInstances() {
        if (instances == null) {
            synchronized (SendExpressImpl.class) {
                if (instances == null) {
                    instances = new SendExpressImpl();
                }
            }
        }
        return instances;
    }

    public static String base64Encode(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }

    @Override
    public void send(
            String orderCode,
            String sendName,
            String sendPhone,
            String sendAddress,
            String receiveName,
            String receivePhone,
            String receiveAddress,
            String goodsType,
            String weight,
            String packageNum,
            String cost,
            String leave,
            String company,
            OnSendFinishCallBack onSendFinishCallBack) {
        this.callBack = onSendFinishCallBack;
        try {
            orderOnlineByJson(
                    orderCode, sendName,
                    sendPhone,
                    sendAddress,
                    receiveName,
                    receivePhone,
                    receiveAddress,
                    goodsType,
                    weight,
                    packageNum,
                    cost,
                    leave,
                    company);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "请求异常" + e.getMessage());
            onSendFinishCallBack.onFinishFailure(e.getMessage());
        }
    }

    /**
     * Json方式 在线下单
     *
     * @throws Exception
     */
    public String orderOnlineByJson(
            String orderCode,
            String sendName,
            String sendPhone,
            String sendAddress,
            String receiveName,
            String receivePhone,
            String receiveAddress,
            String goodsType,
            String weight,
            String packageNum,
            String cost,
            String leave,
            String company) throws Exception {

        String sProvince = InterceptAddressInfo.getAddressInfo(sendAddress, 1);
        String sCity = InterceptAddressInfo.getAddressInfo(sendAddress, 2);
        String sArea = InterceptAddressInfo.getAddressInfo(sendAddress, 3);
        String sAddress = InterceptAddressInfo.getAddressInfo(sendAddress, 4);

        String rProvince = InterceptAddressInfo.getAddressInfo(receiveAddress, 1);
        String rCity = InterceptAddressInfo.getAddressInfo(receiveAddress, 2);
        String rArea = InterceptAddressInfo.getAddressInfo(receiveAddress, 3);
        String rAddress = InterceptAddressInfo.getAddressInfo(receiveAddress, 4);

        String code = Transform.transform(company);
        double c = (double) Integer.valueOf(cost.substring(0, 1));
        double w = (double) Integer.valueOf(weight);
        int q = Integer.valueOf(packageNum);
        if (leave.length() == 0) {
            leave = "默认";
        }
        String requestData = "{'OrderCode': '" + orderCode + "'," +
                "'ShipperCode':'" + code + "'," +
                "'PayType':1," +
                "'ExpType':1," +
                "'Cost':" + c + "," +
                "'OtherCost':0.0," +
                "'Sender':" +
                "{" +
                "'Name':'" + sendName + "','Mobile':'" + sendPhone + "','ProvinceName':'" + sProvince + "','CityName':'" + sCity + "','ExpAreaName':'" + sArea + "','Address':'" + sAddress + "'}," +
                "'Receiver':" +
                "{" +
                "'Name':'" + receiveName + "','Mobile':'" + receivePhone + "','ProvinceName':'" + rProvince + "','CityName':'" + rCity + "','ExpAreaName':'" + rArea + "','Address':'" + rAddress + "'}," +
                "'Commodity':" +
                "[{" +
                "'GoodsName':'" + goodsType + "'}]," +
                "'Weight':" + w + "," +
                "'Quantity':" + q + "," +
                "'Volume':0.3," +
                "'Remark':'" + leave + "'}";
        Log.e("SendExpressImpl", requestData);
        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBusinessID);
        params.put("RequestType", "1001");
        String dataSign = encrypt(requestData, AppKey, "UTF-8");
        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");
        String result = sendPost(ReqURL, params);

        //根据公司业务处理返回的信息......

        return result;
    }

    /**
     * MD5加密
     *
     * @param str     内容
     * @param charset 编码方式
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private String MD5(String str, String charset) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(charset));
        byte[] result = md.digest();
        StringBuffer sb = new StringBuffer(32);
        for (int i = 0; i < result.length; i++) {
            int val = result[i] & 0xff;
            if (val <= 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }

    /**
     * base64编码
     *
     * @param str     内容
     * @param charset 编码方式
     * @throws UnsupportedEncodingException
     */
    private String base64(String str, String charset) throws UnsupportedEncodingException {
        String encoded = base64Encode(str.getBytes(charset));
        return encoded;
    }

    @SuppressWarnings("unused")
    private String urlEncoder(String str, String charset) throws UnsupportedEncodingException {
        String result = URLEncoder.encode(str, charset);
        return result;
    }

    /**
     * 电商Sign签名生成
     *
     * @param content  内容
     * @param keyValue Appkey
     * @param charset  编码方式
     * @return DataSign签名
     * @throws UnsupportedEncodingException ,Exception
     */
    @SuppressWarnings("unused")
    private String encrypt(String content, String keyValue, String charset) throws UnsupportedEncodingException, Exception {
        if (keyValue != null) {
            return base64(MD5(content + keyValue, charset), charset);
        }
        return base64(MD5(content, charset), charset);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url    发送请求的 URL
     * @param params 请求的参数集合
     * @return 远程资源的响应结果
     */
    @SuppressWarnings("unused")
    private String sendPost(final String url, final Map<String, String> params) {

        result = new StringBuilder();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    URL realUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
                    // 发送POST请求必须设置如下两行
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    // POST方法
                    conn.setRequestMethod("POST");
                    // 设置通用的请求属性
                    conn.setRequestProperty("accept", "*/*");
                    conn.setRequestProperty("connection", "Keep-Alive");
                    conn.setRequestProperty("user-agent",
                            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.connect();
                    // 获取URLConnection对象对应的输出流
                    out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                    // 发送请求参数
                    if (params != null) {
                        StringBuilder param = new StringBuilder();
                        for (Map.Entry<String, String> entry : params.entrySet()) {
                            if (param.length() > 0) {
                                param.append("&");
                            }
                            param.append(entry.getKey());
                            param.append("=");
                            param.append(entry.getValue());
                            System.out.println(entry.getKey() + ":" + entry.getValue());
                        }
                        System.out.println("param:" + param.toString());
                        out.write(param.toString());
                    }
                    // flush输出流的缓冲
                    out.flush();
                    // 定义BufferedReader输入流来读取URL的响应
                    in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    String line;
                    while ((line = in.readLine()) != null) {
                        SendExpressImpl.this.result.append(line);
                    }
//            打印请求输出结果
                    String data = SendExpressImpl.this.result.toString();
                    Log.e("SendExpressImpl", data);
                    callBack.resultSuccess(data);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    callBack.resultFailure(e.getMessage());
                    e.printStackTrace();
                }
                //使用finally块来关闭输出流、输入流
                finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(runnable);

        return result.toString();
    }
}
