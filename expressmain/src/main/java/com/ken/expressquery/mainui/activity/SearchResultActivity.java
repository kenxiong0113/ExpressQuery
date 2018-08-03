package com.ken.expressquery.mainui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.ken.expressquery.R;
import com.ken.base.BaseActivity;
import com.ken.base.utils.BaseRecyclerAdapter;
import com.ken.base.utils.BaseRecyclerHolder;
import com.ken.expressquery.MyApplication;
import com.ken.expressquery.dbgreendao.express.ExpressCallBack;
import com.ken.expressquery.dbgreendao.express.ExpressInsert;
import com.ken.expressquery.dbgreendao.express.ExpressQuery;
import com.ken.expressquery.dbgreendao.express.ExpressUpdate;
import com.ken.base.greendao.ExpressInfoDao;
import com.ken.base.model.ExpressInfo;
import com.ken.base.network.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author by ken on 2017/9/17.
 * 查询快递展示查询结果ui
 */

public class SearchResultActivity extends BaseActivity {
    @BindView(R.id.rv_result)
    RecyclerView rvResult;
    @BindView(R.id.rl_top)
    RelativeLayout rlResult;
    @BindView(R.id.img_type_logo)
    ImageView imgTypeLogo;
    @BindView(R.id.tv_type_and_no)
    TextView tvTypeAndNo;
    @BindView(R.id.tv_state1)
    TextView tvState1;
    @BindView(R.id.iv_point1)
    ImageView ivPoint1;
    @BindView(R.id.tv_state2)
    TextView tvState2;
    @BindView(R.id.iv_point2)
    ImageView ivPoint2;
    @BindView(R.id.tv_state3)
    TextView tvState3;
    @BindView(R.id.iv_point3)
    ImageView ivPoint3;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.tv_company)
    TextView tvState;
    @BindView(R.id.view_1)
    View view1;
    @BindView(R.id.view_2)
    View view2;
    @BindView(R.id.view_3)
    View view3;
    @BindView(R.id.view_4)
    View view4;
    @BindView(R.id.tv_customer_service)
    TextView tvCustomerService;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    LinearLayoutManager layoutManager;
    private List<ExpressInfo.LogisticsTrack> listArray = new ArrayList<>();
    /**
     * 物流状态
     */
    private String state;
    private List<ExpressInfo.LogisticsTrack> mTracesList = new ArrayList<>();
    private BaseRecyclerAdapter<ExpressInfo.LogisticsTrack> mAdapter;
    private static final String TAG = "SearchResultActivity";
    private String result;
    private Context mContext;
    private int state_code = -2;
    private Long id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initToolBar();
        mContext = getApplicationContext();
        result = getIntent().getStringExtra("Result");
//        在数据填充前，设置好适配器
        setAdapter();
//        加载快递基本信息
        loadNetworkData(result);
    }

    @Override
    protected void onNetworkConnected(NetworkUtils.NetType type) {
        showNetErrorView();
    }

    @Override
    protected void onNetworkDisConnected() {
        dismissNetErrorView();
    }

    /**
     * 初始化控件
     */
    private void initToolBar() {
//        物流详情
        setTitle(getString(R.string.logistics_details));
        setTopLeftButton(R.drawable.ic_return, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    /**
     * 设置adapter
     */
    private void setAdapter() {
        mAdapter = new BaseRecyclerAdapter<ExpressInfo.LogisticsTrack>(mContext, mTracesList, R.layout.item_logistics_data) {
            @Override
            public void convert(BaseRecyclerHolder holder, ExpressInfo.LogisticsTrack item, int position, boolean isScrolling) {
                holder.setText(R.id.tv_express_text, item.getContent());
                holder.setText(R.id.tv_express_time, item.getTime());

            }
        };
        //设置布局管理器，实现横向或竖直滚动的列表布局
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        //列表再底部开始展示，反转后由上面开始展示
//        layoutManager.setStackFromEnd(true);
//        /*列表翻转*/
//        layoutManager.setReverseLayout(true);
        rvResult.setLayoutManager(layoutManager);
        rvResult.setAdapter(mAdapter);
    }

    /**
     * 加载网络数据,获取快递信息
     *
     * @param result 后台返回的完整json格式的String类型数据
     */
    private void loadNetworkData(String result) {
        Log.e("SearchResultActivity", result);
        ExpressInfoDao mDao = MyApplication.getInstances().getDaoSession().getExpressInfoDao();
        Gson gson = new Gson();
        try {
            ExpressInfo info = gson.fromJson(result, ExpressInfo.class);
            state = info.getState();
            state_code = Integer.valueOf(state);
            /**
             * state物流状态值
             * -1：单号或代码错误；
             * 0：暂无轨迹；
             * 1: 快递收件；
             * 2：在途中；
             * 3：签收；
             * 4：问题件
             */
            if (state_code == -1) {
                tvTypeAndNo.setText(getIntent().getStringExtra("No"));
                tvState.setText("单号或代码错误");
            } else if (state_code == 0) {
                tvState.setText("暂无轨迹信息");
            } else if (state_code == 1) {
                change1();
            } else if (state_code == 2) {
                change2();
            } else if (state_code == 3) {
                change3();
            } else if (state_code == 4) {
                tvState.setText("问题件");
            }
            if (state_code != -2) {
                if (info.getType() == null || info.getName() == null) {
//                    能查询，单无此单号的物流信息，即查不出是哪家快递公司的
                    tvState.setText("暂无此单号物流信息");
                    tvTypeAndNo.setText(info.getNo());
                    imgTypeLogo.setImageResource(R.drawable.url_error);
                    tvCustomerService.setVisibility(View.GONE);
                } else {
                    tvTypeAndNo.setText(info.getName() + ":" + info.getNo());
                    //                有快递信息则显示客服电话
                    tvCustomerService.setVisibility(View.VISIBLE);
                    tvPhone.setText(info.getPhone());

                    Glide.with(mContext)
                            .load(info.getLogo())
                            .apply(RequestOptions.placeholderOf(R.drawable.pic_loading))
                            //                        设置圆形图片
                            //                        .apply(RequestOptions.circleCropTransform())
                            .apply(RequestOptions.errorOf(R.drawable.url_error))
                            .into(imgTypeLogo);
                    listArray = info.getList();
                    for (ExpressInfo.LogisticsTrack track : listArray) {
                        mTracesList.add(new ExpressInfo.LogisticsTrack(track.getContent(), track.getTime()));
                    }

                }
                //                查询记录插入前先检查本地数据库是否有本条数据记录
                //                有记录则更新数据没有则插入新数据
                boolean isHistory = ExpressQuery.getInstances().query(mDao, info.getNo(), new ExpressCallBack() {
                    @Override
                    public void trajectoryInformation(List<ExpressInfo> mList) {
                        if (mList.size() != 0) {
                            id = mList.get(0).getId();
                        }
                        // TODO: 2018/5/18  返回的本地数据库中的快递信息，此处无需处理，如果要在没网的时候加载本地数据，可以在这里处理相关信息
                    }
                });
                if (isHistory) {
                    //                    查询的单号在本地数据库中有记录，
                    //                    此处做更新操作
                    ExpressUpdate.getInstances().update(mDao, id, info);

                } else {
                    //                    查询的单号在本地数据库中没有记录，
                    //                    此处做插入新数据操作
                    ExpressInsert.getInstances().insert(mDao, info);

                }


            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SearchResultActivity", e.getMessage());
        } finally {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 已发货的进度状态图
     */
    private void change1() {
        tvState1.setTextColor(ContextCompat.getColor(mContext, R.color.primary));
        ivPoint1.setImageResource(R.drawable.ic_point_bule);

    }

    /**
     * 快递运输中的进度状态图
     */
    private void change2() {
        tvState2.setTextColor(ContextCompat.getColor(mContext, R.color.primary));
        ivPoint1.setImageResource(R.drawable.ic_point_bule);
        ivPoint2.setImageResource(R.drawable.ic_point_bule);
        view1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.primary));
        view2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.primary));
    }

    /**
     * 已签收的进度状态图
     */
    private void change3() {
        tvState3.setTextColor(ContextCompat.getColor(mContext, R.color.primary));
        ivPoint1.setImageResource(R.drawable.ic_point_bule);
        ivPoint2.setImageResource(R.drawable.ic_point_bule);
        ivPoint3.setImageResource(R.drawable.ic_point_bule);
        view1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.primary));
        view2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.primary));
        view3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.primary));
        view4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.primary));
    }

}