package com.ken.expressquery.search.m;

import android.content.Context;
import com.ken.expressquery.search.SearchFinishCallback;


/**
 * 快递查询Model层
 * @author by ken on 2018/5/23
 * */
public interface SearchModel {
    void search(Context context, String str, SearchFinishCallback searchFinishCallback);

}
