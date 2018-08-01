package com.ken.expressquery.mainui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ken.expressquery.R;
import com.ken.expressquery.base.BaseRecyclerAdapter;
import com.ken.expressquery.base.BaseRecyclerHolder;
import com.ken.expressquery.base.MyApplication;
import com.ken.expressquery.dbgreendao.express.ExpressCallBack;
import com.ken.expressquery.dbgreendao.express.ExpressDelete;
import com.ken.expressquery.dbgreendao.express.ExpressQuery;
import com.ken.expressquery.greendao.ExpressInfoDao;
import com.ken.expressquery.mainui.activity.LoginActivity;
import com.ken.expressquery.mainui.activity.SearchResultActivity;
import com.ken.expressquery.model.ExpressInfo;
import com.ken.expressquery.search.p.SearchPre;
import com.ken.expressquery.search.v.SearchExpressView;
import com.ken.expressquery.send.SenderPrimaryActivity;
import com.ken.expressquery.view.LoadingDialog;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;
import es.dmoral.toasty.Toasty;

import static com.ken.expressquery.base.BaseConstant.DISMISS_DIALOG;
import static com.ken.expressquery.base.BaseConstant.REQUEST_CODE;
import static com.ken.expressquery.base.BaseConstant.SHOW_DIALOG;
import static com.ken.expressquery.base.BaseConstant.SHOW_TOAST;

/**
 * 查询快递ui界面
 * 阿里云应用市场，全国物流快递查询（单号识别） 接口
 * 网络请求地址{&link https://cexpress.market.alicloudapi.com/cexpress?no=pram}
 * 请求方式 GET
 *
 * @author by ken on 2017/9/10.
 */

public class SearchExpressFragment extends Fragment implements SearchExpressView {
    public final static String TAG = SearchExpressFragment.class.getName();
    /*** 快递单号*/
    public String no;
    @BindView(R.id.et_Code)
    EditText etCode;
    @BindView(R.id.img_scan)
    ImageView imgScan;
    @BindView(R.id.btn_search)
    Button btnCheck;
    Context mContext;
    Unbinder unbinder;
    @BindView(R.id.tv_history)
    TextView tvHistory;
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    @BindView(R.id.img_send_express)
    ImageView imgSendExpress;
    @BindView(R.id.img_run)
    ImageView imgRun;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    private View view;
    private BaseRecyclerAdapter<ExpressInfo> adapter;
    private List<ExpressInfo> mExpressInfoList = new ArrayList<>();
    private ExpressInfoDao mDao;
    private LinearLayoutManager layoutManager;
    private LoadingDialog dialog;
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
    private SearchPre searchPre = new SearchPre(this);

    public static SearchExpressFragment newInstance(String param1) {
        SearchExpressFragment fragment = new SearchExpressFragment();
        Bundle args = new Bundle();
        args.putString(TAG, param1);
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
        view = inflater.inflate(R.layout.fragment_search_express, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        mDao = MyApplication.getInstances().getDaoSession().getExpressInfoDao();
        setAdapter();
        loadHistory();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*** 处理二维码扫描结果*/
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    etCode.setText(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @OnClick({R.id.img_scan, R.id.btn_search, R.id.img_send_express})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                no = etCode.getText().toString();
//                通过扫码可能获取到不是数字的运单号，这里判断字符串是否为全数字
                // TODO: 2018/5/18 如发现快递单号有字母组成，只需要更改这里即可
//                宅急送快递单号前面有字母，即这里不做字符串全数字判断
//                || !Numeric.isNumeric5(no)
                if (no.length() < 10) {
                    Toast.makeText(getActivity(), "快递单号有误", Toast.LENGTH_SHORT).show();
                } else {
                    Message message = new Message();
                    message.what = SHOW_DIALOG;
                    handler.sendMessage(message);
                    searchPre.searchExpress(no);
                }
                break;
            case R.id.img_scan:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.img_send_express:
                if (BmobUser.getCurrentUser() != null) {
                    startActivity(new Intent(getActivity(), SenderPrimaryActivity.class));
                } else {
                    Toasty.warning(getActivity(), "请先登录！", Toast.LENGTH_SHORT, false).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHistory();
    }

    /**
     * 设置适配器Adapter
     */
    private void setAdapter() {
        adapter = new BaseRecyclerAdapter<ExpressInfo>(mContext, mExpressInfoList, R.layout.item_history_notes) {
            @Override
            public void convert(BaseRecyclerHolder holder, final ExpressInfo item, final int position, boolean isScrolling) {
                holder.setText(R.id.tv_com, item.getName());
                holder.setText(R.id.tv_no, item.getNo());

                holder.getView(R.id.img_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ExpressDelete.getInstances().delete(mDao, item.getId());
                        adapter.delete(position);
                    }
                });
            }
        };
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Message message = new Message();
                message.what = SHOW_DIALOG;
                handler.sendMessage(message);
                searchPre.searchExpress(mExpressInfoList.get(position).getNo());
            }
        });

        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvHistory.setLayoutManager(layoutManager);
        rvHistory.setAdapter(adapter);

    }

    /**
     * 加载历史查询记录
     */
    private void loadHistory() {
        mExpressInfoList.clear();
        ExpressQuery.getInstances().query(mDao, null, new ExpressCallBack() {
            @Override
            public void trajectoryInformation(List<ExpressInfo> mList) {
                if (mList.size() <= 0){
                    tvEmpty.setVisibility(View.VISIBLE);
                }else {
                    tvEmpty.setVisibility(View.GONE);
                }
                for (ExpressInfo info : mList) {
                    String name = info.getName();
                    if (name == null) {
                        name = "未知";
                    }
                    mExpressInfoList.add(new ExpressInfo(
                            info.getId(),
                            info.getNo(),
                            name,
                            null,
                            null));
                }
                adapter.notifyDataSetChanged();
            }
        });
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
