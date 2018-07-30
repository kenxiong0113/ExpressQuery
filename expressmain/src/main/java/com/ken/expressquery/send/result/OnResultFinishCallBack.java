package com.ken.expressquery.send.result;

import com.ken.expressquery.send.bean.SendExpressOrder;

import java.util.List;

/**
 * 保存数据到Bmob的回调
 *
 * @author by ken on 2018/5/29
 */
public interface OnResultFinishCallBack {
    void onBmobSuccess(String str);

    void onBmobFailure(String error);

    void onQuerySuccess(List<SendExpressOrder> list);
}
