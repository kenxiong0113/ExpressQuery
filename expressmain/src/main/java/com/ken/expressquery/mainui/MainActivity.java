package com.ken.expressquery.mainui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ken.expressquery.R;
import com.ken.expressquery.mainui.fragment.SearchExpressFragment;
import com.ken.expressquery.mainui.fragment.HomeFragment;
import com.ken.expressquery.mainui.fragment.MyFragment;
import com.ken.expressquery.mainui.fragment.SendFragment;
import com.ken.expressquery.utils.ExitPressed;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author by ken on 2018/4/25
 *         活动主界面
 */
public class MainActivity extends AppCompatActivity implements
        BottomNavigationBar.OnTabSelectedListener {
    @BindView(R.id.lay_frame) FrameLayout layFrame;
    @BindView(R.id.bottom_navigation_bar) BottomNavigationBar bottomNavigationBar;
    private HomeFragment homeFragment;
    private SearchExpressFragment searchExpressFragment;
    private MyFragment myFragment;
    private SendFragment sendFragment;
    private ArrayList<Fragment> fragments;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        initWidget();
    }

    private void initWidget() {
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.bottom_bar_ic_home, getString(R.string.shouye))
                        .setActiveColorResource(R.color.bottom_bar))
                .addItem(new BottomNavigationItem(R.drawable.bottom_bar_ic_express, getString(R.string.find))
                        .setActiveColorResource(R.color.bottom_bar))
                .addItem(new BottomNavigationItem(R.drawable.bottom_bar_ic_my, getString(R.string.my))
                        .setActiveColorResource(R.color.bottom_bar))
                        .setBarBackgroundColor(R.color.background_bar)
                .initialise();
        setDefaultFragment();
        bottomNavigationBar.setTabSelectedListener(this);
    }


    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        homeFragment = HomeFragment.newInstance(getString(R.string.shouye));
        transaction.replace(R.id.lay_frame, homeFragment);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        FragmentManager fm = getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                homeFragment = HomeFragment.newInstance(getString(R.string.shouye));
                transaction.replace(R.id.lay_frame, homeFragment);
                break;
            case 1:
                searchExpressFragment = SearchExpressFragment.newInstance(getString(R.string.find));
                transaction.replace(R.id.lay_frame, searchExpressFragment);
                break;
//            case 2:
//                sendFragment = SendFragment.newInstance(getString(R.string.iiiegal_inquiry));
//                transaction.replace(R.id.lay_frame, sendFragment);
//                break;
            case 2:
                myFragment = MyFragment.newInstance(getString(R.string.my));
                transaction.replace(R.id.lay_frame, myFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onBackPressed() {
        if (ExitPressed.exitPressed(mContext)){
            finish();
        }
    }
}
