package com.ken.expressquery.mainui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ken.expressquery.R;
import com.ken.expressquery.mainui.activity.LoginActivity;

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
    @BindView(R.id.tv_my_often_address)
    TextView tvMyOftenAddress;
    @BindView(R.id.view_2)
    View view2;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    Unbinder unbinder;
    @BindView(R.id.tv_up_version)
    TextView tvUpVersion;
    private View view;
    private BmobUser bmobUser;

    public static MyFragment newInstance(String param1) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public MyFragment() {
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
        initInfo();
        return view;
    }

    private void initInfo() {
        if (bmobUser != null) {
            tvExit.setText("退出账号");
            tvUsername.setText(bmobUser.getUsername());
        }
    }

    @OnClick({R.id.tv_my_often_address, R.id.tv_exit, R.id.tv_up_version})
    public void onListenter(View view) {
        int msg = view.getId();
        switch (msg) {
            case R.id.tv_my_often_address:
                //我的常用地址

                break;
            case R.id.tv_up_version:

                break;
            case R.id.tv_exit:
                //清除缓存用户对象
                if (bmobUser != null) {
                    BmobUser.logOut();
                }
                startActivity(new Intent(getActivity(), LoginActivity.class));
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
