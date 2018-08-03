package com.ken.expressquery.send.result.p;

import com.ken.base.bean.User;
import com.ken.expressquery.send.bean.SendExpressOrder;
import com.ken.expressquery.send.result.OnResultFinishCallBack;
import com.ken.expressquery.send.result.m.IResult;
import com.ken.expressquery.send.result.m.ResultM;
import com.ken.expressquery.send.result.v.ResultV;

import java.util.List;

/**
 * 预约寄件返回后的结果保存到bmob后的presenter层
 *
 * @author by ken on 2018/5/31
 */
public class PResult {
    ResultM resultM;
    ResultV resultV;

    public PResult(ResultV resultV) {
        this.resultM = IResult.getInstances();
        this.resultV = resultV;
    }

    public void save(String sendName,
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
                     String company) {
        resultM.insertOrder(sendName,
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
                company, new OnResultFinishCallBack() {
                    @Override
                    public void onBmobSuccess(String str) {
                        resultV.bSuccess(str);
                    }

                    @Override
                    public void onBmobFailure(String error) {
                        resultV.bFailure(error);
                    }

                    @Override
                    public void onQuerySuccess(List<SendExpressOrder> list) {

                    }
                });

    }

    /**
     * @param vrg 查询类型，1->查询自增订单单号，2->查询关联整行的数据
     */
    public void query(User user, final int vrg) {
        resultM.queryOrder(user, new OnResultFinishCallBack() {
            @Override
            public void onBmobSuccess(String str) {

            }

            @Override
            public void onBmobFailure(String error) {
                resultV.bFailure(error);
            }

            @Override
            public void onQuerySuccess(List<SendExpressOrder> list) {
                resultV.bQuerySuccess(list, vrg);
            }
        });
    }
}
