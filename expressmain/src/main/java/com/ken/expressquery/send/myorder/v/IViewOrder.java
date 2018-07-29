package com.ken.expressquery.send.myorder.v;

import com.ken.expressquery.send.bean.ResultData;

import java.util.List;

public interface IViewOrder {
    void onOrderSuccess(String str);
    void onOrderFailure(String error);
    void onOrderQuerySuccess(List<ResultData> list);
}
