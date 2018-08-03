package com.ken.expressquery.search.m;

import android.content.Context;
import android.util.Log;

import com.ken.expressquery.search.SearchFinishCallback;
import com.ken.base.threadpool.ThreadPoolProxyFactory;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.ken.base.utils.BaseConstant.APP_CODE;
import static com.ken.base.utils.BaseConstant.HEADER;
import static com.ken.base.utils.BaseConstant.HOST;
import static com.ken.base.utils.BaseConstant.PATH;
import static com.ken.base.utils.BaseConstant.VALUES;

/**
 * 阿里云应用市场，全国物流快递查询（单号识别） 接口
 * 请求方式 GET
 *
 * @author by ken on 2018/5/23
 */

public class SearchImpl implements SearchModel {
    private static SearchImpl searchImpl = null;
    private String TAG = SearchImpl.class.getName();
    private StringBuffer buffer;

    private SearchImpl() {
    }

    public static SearchImpl getSearchImpl() {
        if (searchImpl == null) {
            synchronized (SearchImpl.class) {
                if (searchImpl == null) {
                    searchImpl = new SearchImpl();
                }
            }
        }
        return searchImpl;
    }

    /**
     * GET 方式请求
     *
     * @param mContext 上下文
     * @param vrg      查询单号
     * @param callback 回调
     */
    @Override
    public void search(Context mContext, final String vrg, final SearchFinishCallback callback) {

            buffer = new StringBuffer();
//            清空buffer
            buffer.setLength(0);
//         no参数必填
            buffer.append("?no=" + vrg);
//         网络请求不能再主线程中进行
//         不能再非UI线程中创建Handler
//         使用线程池
            Runnable query = new Runnable() {
                @Override
                public void run() {
                    OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
                    Request request = new Request.Builder()
                            .url(HOST + PATH + buffer)
                            .header(HEADER, VALUES + APP_CODE)
                            .build();
                    Call call = okHttpClient.newCall(request);
                    try {
                        Response response = call.execute();
//                     原因为OkHttp请求回调中response.body().string()只能有效调用一次
                        if (response.isSuccessful()) {
                            String content = response.body().string();
                            callback.onSuccess(content);
                            response.body().close();
                        } else {
                            throw new Exception("请求失败" + response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                        callback.onFailure(e.getMessage());
                    } finally {

                    }
                }
            };
            ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(query);

    }


}
