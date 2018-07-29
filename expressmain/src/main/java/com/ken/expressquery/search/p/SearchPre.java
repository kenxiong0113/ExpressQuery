package com.ken.expressquery.search.p;


import com.ken.expressquery.search.SearchFinishCallback;
import com.ken.expressquery.search.m.SearchImpl;
import com.ken.expressquery.search.m.SearchModel;
import com.ken.expressquery.search.v.SearchExpressView;
import com.orhanobut.logger.Logger;


/**
 * 快递查询Presenter层
 *
 * @author by ken on 2018/5/23
 * */
public class SearchPre {
    private SearchExpressView searchExpressView;
    private SearchModel searchModel;
    private String TAG = SearchPre.class.getName();
    public SearchPre(SearchExpressView searchExpressView){
        this.searchExpressView = searchExpressView;
        this.searchModel =SearchImpl.getSearchImpl();
    }

    public void searchExpress(String str){
        searchModel.search(searchExpressView.getContextView(),str , new SearchFinishCallback() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
                searchExpressView.onSuccess(result);
            }

            @Override
            public void onFailure(String error) {
                Logger.e(error);
                searchExpressView.onFailure(error);
            }
        });
    }
}
