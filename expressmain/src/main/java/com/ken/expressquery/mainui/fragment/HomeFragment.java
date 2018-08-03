package com.ken.expressquery.mainui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ken.expressquery.R;
import com.ken.base.utils.BaseRecyclerAdapter;
import com.ken.base.utils.BaseRecyclerHolder;
import com.ken.expressquery.MyApplication;
import com.ken.expressquery.dbgreendao.express.ExpressCallBack;
import com.ken.expressquery.dbgreendao.express.ExpressQuery;
import com.ken.base.greendao.ExpressInfoDao;
import com.ken.expressquery.mainui.activity.SearchResultActivity;
import com.ken.base.model.ExpressInfo;
import com.ken.expressquery.search.p.SearchPre;
import com.ken.expressquery.search.v.SearchExpressView;
import com.ken.base.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ken.base.utils.BaseConstant.DISMISS_DIALOG;
import static com.ken.base.utils.BaseConstant.SHOW_DIALOG;
import static com.ken.base.utils.BaseConstant.SHOW_TOAST;

/**
 * @author by ken on 2017/9/10.
 * 主页UI
 */

public class HomeFragment extends Fragment implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, SearchExpressView {
    @BindView(R.id.rcy_home)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    Unbinder unbinder;
    @BindView(R.id.rl_bg_ad)
    RelativeLayout rlBgAd;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    private LoadingDialog dialog;
    private String no;
    private View view;
    private LinearLayoutManager layoutManager;
    private BaseRecyclerAdapter<ExpressInfo> adapter;
    private List<ExpressInfo> mExpressInfoList = new ArrayList<>();
    private Context mContext;
    /**
     * 更新UI
     * 在UI线程中
     */
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_DIALOG:
                    dialog = new LoadingDialog(mContext, "正在查询...");
                    dialog.setCancelable(true);
                    dialog.show();
                    break;
                case DISMISS_DIALOG:
                    dismissDialog();
                    break;
                case SHOW_TOAST:
                    Toast.makeText(mContext, "查询超时，请重试", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
        }
    };
    private ExpressInfoDao mDao;
    private SearchPre searchPre = new SearchPre(this);

    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        mDao = MyApplication.getInstances().getDaoSession().getExpressInfoDao();
        initView();
        setAdapter();
        loadData();
        return view;
    }

    private void initView() {
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#BBFFFF"));
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setAdapter() {
        adapter = new BaseRecyclerAdapter<ExpressInfo>(mContext, mExpressInfoList, R.layout.item_home) {
            @Override
            public void convert(BaseRecyclerHolder holder, ExpressInfo item, int position, boolean isScrolling) {
//               判断物流轨迹信息是否为空数据
                if (item.getList().size() != 0 && item.getList() != null) {
                    holder.setText(R.id.tv_content, item.getList().get(0).getContent());
                } else {
                    holder.setText(R.id.tv_content, "暂无物流轨迹信息");
                }
                holder.setText(R.id.tv_name, item.getName());
                holder.setText(R.id.tv_no, item.getNo());
                Glide.with(mContext)
                        .load(item.getLogo())
                        .apply(RequestOptions.placeholderOf(R.drawable.pic_loading))
                        .apply(RequestOptions.errorOf(R.drawable.url_error))
                        .into((ImageView) holder.getView(R.id.img_logo));
            }
        };

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                no = mExpressInfoList.get(position).getNo();
                Message message = new Message();
                message.what = SHOW_DIALOG;
                handler.sendMessage(message);
                searchPre.searchExpress(no);
            }
        });


        layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
//      layoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
//      layoutManager.setReverseLayout(true);//列表翻转
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        ExpressQuery.getInstances().query(mDao, null, new ExpressCallBack() {
            @Override
            public void trajectoryInformation(List<ExpressInfo> mList) {
                for (ExpressInfo info : mList) {
                    if (info.getName() != null) {
                        mExpressInfoList.add(new ExpressInfo(
                                info.getId(),
                                info.getNo(),
                                info.getName(),
                                info.getLogo(),
                                info.getList()));
                    }
                }

                if (mExpressInfoList.size() <= 0) {
                    tvEmpty.setVisibility(View.VISIBLE);
                }else {
                    tvEmpty.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
//                收起下拉刷新
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
//        下拉刷新
        mExpressInfoList.clear();
        loadData();

    }

    @Override
    public Context getContextView() {
        return mContext;
    }

    @Override
    public void showDialog() {
        dialog.show();
    }

    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    public void onSuccess(String str) {
        Intent intent = new Intent(mContext, SearchResultActivity.class);
        intent.putExtra("Result", str);
        intent.putExtra("No", no);
        startActivity(intent);
//     UI更新不能在子线程中进行
        Message message = new Message();
        message.what = DISMISS_DIALOG;
        handler.sendMessage(message);
    }

    @Override
    public void onFailure(String str) {
//      UI更新不能在子线程中进行
        Message message = new Message();
        message.what = DISMISS_DIALOG;
        handler.sendMessage(message);
        if ("timeout".equals(str)) {
            showDialog();
        }
    }

    @Override
    public void showToast(String str) {
        Message message = new Message();
        message.what = SHOW_TOAST;
        handler.sendMessage(message);

    }
}
