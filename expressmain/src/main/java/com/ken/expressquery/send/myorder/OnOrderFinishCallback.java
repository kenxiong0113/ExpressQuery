package com.ken.expressquery.send.myorder;

import com.ken.expressquery.send.bean.ResultData;

import java.util.List;

public interface OnOrderFinishCallback {
    void onSuccess(String str);

    void onFailure(String error);

    void onQuerySuccess(List<ResultData> list);
}
