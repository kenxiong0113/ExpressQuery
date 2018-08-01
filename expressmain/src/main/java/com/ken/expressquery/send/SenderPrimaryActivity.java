package com.ken.expressquery.send;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ken.expressquery.R;
import com.ken.expressquery.base.BaseActivity;
import com.ken.expressquery.management.address.v.ManageAddressActivity;
import com.ken.expressquery.map.p.LocationPre;
import com.ken.expressquery.map.v.LocationView;
import com.ken.expressquery.network.NetworkUtils;
import com.ken.expressquery.send.adapter.CommonFunctionsAdapter;
import com.ken.expressquery.send.appointment.v.SendExpressActivity;
import com.ken.expressquery.send.myorder.v.MyOrderActivity;
import com.ken.expressquery.view.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 寄件模块的主界面
 *
 * @author by ken on 2018/5/23
 */
public class SenderPrimaryActivity extends BaseActivity implements
        LocationView, AdapterView.OnItemClickListener {
    private static final String TAG = "SenderPrimaryActivity";
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.img_order)
    ImageView imgOrder;
    @BindView(R.id.tv_my_order)
    TextView tvMyOrder;
    @BindView(R.id.img_id)
    ImageView imgId;
    @BindView(R.id.tv_my_id)
    TextView tvMyId;
    @BindView(R.id.gv_hot)
    GridView gvHot;
    @BindView(R.id.rl_my_order)
    RelativeLayout rlMyOrder;
    @BindView(R.id.rl_id)
    RelativeLayout rlId;
    private Context mContext;
    private CommonFunctionsAdapter adapter;
    private LocationPre locationPre = new LocationPre(this);
    private LoadingDialog dialog;
    private int[] icon = {
            R.drawable.ic_send_express,
            R.drawable.com_address,
            R.drawable.com_receipt,
            R.drawable.com_shipment};
    private String[] iconName = {
            "预约寄件",
            "附近网点",
            "收件地址",
            "寄件地址"};
    private List<Map<String, Object>> commomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_sender_primary;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        initToolbar();
        setAdapter();
        dialog = new LoadingDialog(SenderPrimaryActivity.this, "努力加载中...");
        //        先定位，把定位信息存到本地持久化数据库SP，然后根据SP获取定位信息
        locationPre.locationPre();
        getLocation();
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
        setTitle("寄快递");
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
        adapter = new CommonFunctionsAdapter(mContext, initData());
        gvHot.setAdapter(adapter);
        gvHot.setOnItemClickListener(this);

    }

    /**
     * 处理数据
     */
    private List<Map<String, Object>> initData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", icon[i]);
            map.put("ItemText", iconName[i]);
            list.add(map);
        }
        return list;

    }


    /**
     * 获取定位信息并赋值在文本textView中
     */
    private void getLocation() {
        SharedPreferences preferences = getSharedPreferences("Location", MODE_PRIVATE);
        String province = preferences.getString("province", "选择当前位置");
        String city = preferences.getString("city", "");
        String district = preferences.getString("district", "");
        tvLocation.setText(province + " " + city + " " + district);

    }


    @OnClick({R.id.tv_location, R.id.rl_my_order, R.id.rl_id})
    public void onListener(View view) {
        switch (view.getId()) {
            case R.id.tv_location:
                break;
            case R.id.rl_my_order:
                startActivity(new Intent(mContext, MyOrderActivity.class));
                break;
            case R.id.rl_id:
                Toast.makeText(mContext, "实名认证待完善，敬请期待", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;

        }
    }

    @Override
    public Activity getActivity() {
        return this;
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
    public void onSuccess(String province, String city, String district, String street, String streetNum) {
        SharedPreferences.Editor editor = getSharedPreferences("Location", MODE_PRIVATE).edit();
        editor.putString("province", province);
        editor.putString("city", city);
        editor.putString("district", district);
        editor.putString("street", street);
        editor.putString("streetNum", streetNum);
        editor.apply();
    }

    @Override
    public void onFailure(String str) {
        Log.e(TAG, "onFailure: " + str);
        Toast.makeText(mContext, "定位失败，请检查应用权限！", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "销毁定位");
        locationPre.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(mContext, SendExpressActivity.class));
                break;
            case 1:
                Toast.makeText(mContext, "附近网点", Toast.LENGTH_SHORT).show();
                break;
            case 2:
//                    收件地址
                Intent intent = new Intent(SenderPrimaryActivity.this, ManageAddressActivity.class);
                intent.putExtra("AddressType", 2);
                startActivity(intent);
                break;
            case 3:
//                    寄件地址
                Intent intent1 = new Intent(SenderPrimaryActivity.this, ManageAddressActivity.class);
                intent1.putExtra("AddressType", 1);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}
