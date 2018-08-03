package com.ken.expressquery.send.myorder.v;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ken.expressquery.R;
import com.ken.base.BaseActivity;
import com.ken.base.utils.BaseRecyclerAdapter;
import com.ken.base.utils.BaseRecyclerHolder;
import com.ken.base.bean.User;
import com.ken.base.network.NetworkUtils;
import com.ken.expressquery.send.bean.ResultData;
import com.ken.expressquery.send.myorder.p.OrderPer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * 我的寄件订单界面
 *
 * @author by ken on 2018/5/23
 */
public class MyOrderActivity extends BaseActivity implements IViewOrder {
    @BindView(R.id.rv_order)
    RecyclerView rvOrder;
    private LinearLayoutManager layoutManager;
    private BaseRecyclerAdapter<ResultData> adapter;
    private List<ResultData> mList = new ArrayList<>();
    private Context mContext;
    private OrderPer orderPer = new OrderPer(this);
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        user = BmobUser.getCurrentUser(User.class);
        initToolbar();
        setAdapter();
        loadData();
    }

    @Override
    protected void onNetworkConnected(NetworkUtils.NetType type) {
        showNetErrorView();
    }

    @Override
    protected void onNetworkDisConnected() {
        dismissNetErrorView();
    }

    private void initToolbar() {
        setTitle("我的寄件订单");
        setTopLeftButton(R.drawable.ic_return, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    private void setAdapter() {
        adapter = new BaseRecyclerAdapter<ResultData>(mContext, mList, R.layout.item_send_order) {
            @Override
            public void convert(BaseRecyclerHolder holder, ResultData item, int position, boolean isScrolling) {
//                快递单号
                if (item.getLogistsicCode() == null) {
                    holder.setText(R.id.tv_no, "快递单号：下单失败，无单号");
                } else {
                    holder.setText(R.id.tv_no, "快递单号：" + item.getLogistsicCode());
                }

//                物流状态
                holder.setText(R.id.tv_company, item.getShipperCode());
                holder.setText(R.id.tv_order_no, "订单号：" + String.valueOf(mList.get(position).getSendId().getOrderNumber()));
                holder.setText(R.id.tv_time, "下单时间：" + String.valueOf(mList.get(position).getSendId().getCreatedAt()));
                holder.setText(R.id.tv_receive_name, "收件人：" + mList.get(position).getSendId().getrName() + "    " + mList.get(position).getSendId().getrPhone());

            }
        };

        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvOrder.setLayoutManager(layoutManager);
        rvOrder.setAdapter(adapter);
    }

    private void loadData() {
        orderPer.query(user);
    }

    @Override
    public void onOrderSuccess(String str) {

    }

    @Override
    public void onOrderFailure(String error) {

    }

    @Override
    public void onOrderQuerySuccess(List<ResultData> list) {
        if (list.size() != 0) {
            for (ResultData data : list) {
                mList.add(new ResultData(data.getSendId(), data.getUserId(), data.getShipperCode(), data.getLogistsicCode()));
            }
            adapter.notifyDataSetChanged();
        } else {
//没有寄件订单


        }
    }
}
