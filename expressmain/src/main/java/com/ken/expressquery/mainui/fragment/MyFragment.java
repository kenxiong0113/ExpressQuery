package com.ken.expressquery.mainui.fragment;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ken.expressquery.MyApplication;
import com.ken.expressquery.R;
import com.ken.expressquery.management.address.v.ManageAddressActivity;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;

/**
 * @author by ken on 2017/9/10.
 * 我的界面ui
 */

public class MyFragment extends Fragment {
    private static final String TAG = "MyFragment";
    @BindView(R.id.img_head)
    ImageView imgHead;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.user_layout)
    RelativeLayout userLayout;
    @BindView(R.id.tv_often_r_address)
    TextView tvMyOftenAddress;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    Unbinder unbinder;
    @BindView(R.id.tv_up_version)
    TextView tvUpVersion;
    @BindView(R.id.tv_often_s_address)
    TextView tvOftenSAddress;
    private View view;
    private BmobUser bmobUser;
    MyApplication mContext;
    public MyFragment() {
    }

    public static MyFragment newInstance(String param1) {
        MyFragment fragment = new MyFragment();
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
        view = inflater.inflate(R.layout.fragment_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        bmobUser = BmobUser.getCurrentUser();
        mContext = new MyApplication();
        initInfo();
        return view;
    }

    private void initInfo() {
        if (bmobUser != null) {
            tvExit.setText("退出账号");
            tvUsername.setText(bmobUser.getUsername());
        }
    }

    @OnClick({R.id.tv_often_r_address, R.id.tv_exit, R.id.tv_up_version, R.id.tv_often_s_address})
    public void onListener(View view) {
        int msg = view.getId();
        switch (msg) {
            case R.id.tv_often_r_address:
                //管理收件地址
                Intent intent = new Intent(getActivity(), ManageAddressActivity.class);
                intent.putExtra("AddressType", 2);
                startActivity(intent);
                break;
            case R.id.tv_often_s_address:
//                    寄件地址
                Intent intent1 = new Intent(getActivity(), ManageAddressActivity.class);
                intent1.putExtra("AddressType", 1);
                startActivity(intent1);
                break;
            case R.id.tv_up_version:
//                参数1：isManual 用户手动点击检查，非用户点击操作请传false
//                参数2：isSilence 是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
                Beta.checkUpgrade(true, false);
                break;
            case R.id.tv_exit:
                //清除缓存用户对象
                if (bmobUser != null) {
                    BmobUser.logOut();
                }

                ARouter.getInstance().build("/login/login").navigation();
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
