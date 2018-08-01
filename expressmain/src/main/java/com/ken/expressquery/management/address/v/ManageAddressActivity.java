package com.ken.expressquery.management.address.v;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.ken.expressquery.R;
import com.ken.expressquery.base.BaseActivity;
import com.ken.expressquery.base.BaseRecyclerAdapter;
import com.ken.expressquery.base.BaseRecyclerHolder;
import com.ken.expressquery.management.AddAddressActivity;
import com.ken.expressquery.management.address.p.AddressPre;
import com.ken.expressquery.management.bean.AddressBook;
import com.ken.expressquery.model.User;
import com.ken.expressquery.network.NetworkUtils;
import com.ken.expressquery.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import es.dmoral.toasty.Toasty;

/**
 * 地址管理UI界面
 *
 * @author by ken on 2018/5/30
 */
public class ManageAddressActivity extends BaseActivity implements AddressView {
    private final static int REQUEST_CODE_1 = 1;
    private final static int REQUEST_CODE_2 = 1;
    @BindView(R.id.rv_address)
    RecyclerView rvAddress;
    @BindView(R.id.btn_add_address)
    Button btnAddAddress;
    LinearLayoutManager layoutManager;
    AddressPre addressPre = new AddressPre(this);
    List<AddressBook> mList = new ArrayList<>();
    BaseRecyclerAdapter<AddressBook> adapter;
    LoadingDialog dialog;
    int type;
    boolean addressType = false;
    int itemP = 0;
    String defaultObjectId = null;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_manage_address;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        type = getIntent().getIntExtra("AddressType", 0);
        initToolBar(type);
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

    /**
     * 初始化Toolbar
     */
    private void initToolBar(int type) {
        if (type == 1) {
            addressType = true;
            setTitle("管理寄件人地址");
            btnAddAddress.setText("添加寄件人地址");
        } else if (type == 2) {
            addressType = false;
            setTitle("管理收件人地址");
            btnAddAddress.setText("添加收件人地址");
        }
        setTopLeftButton(R.drawable.ic_return, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }


    private void setAdapter() {
        adapter = new BaseRecyclerAdapter<AddressBook>(mContext, mList, R.layout.item_manage_address) {
            @Override
            public void convert(BaseRecyclerHolder holder, final AddressBook item, final int position, boolean isScrolling) {
                holder.setText(R.id.tv_name, item.getName());
                holder.setText(R.id.tv_phone, item.getPhone());
                holder.setText(R.id.tv_address, item.getAddress());
                final CheckBox cb = holder.getView(R.id.cb_default);
                if (item.isDefaultAddress()) {
//                    设置默认地址的id
                    defaultObjectId = item.getId();
                    cb.setChecked(true);
                    cb.setText("默认地址");
                    cb.setTextColor(ContextCompat.getColor(mContext, R.color.primary));
                } else {
                    cb.setChecked(false);
                    cb.setText("设为默认");
                    cb.setTextColor(ContextCompat.getColor(mContext, R.color.gray500));
                }
                if (!cb.isChecked()) {
                    cb.setEnabled(true);
//复选框非选中状态才允许点击
                    cb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog();
                            for (int item = 0; item < mList.size(); item++) {
                                mList.get(item).setDefaultAddress(false);
                            }
                            // TODO: 2018/5/31 后台数据中地址类型会跟着变动，待处理
//                        设置之前的默认地址为非默认
                            AddressBook b1 = new AddressBook();
                            b1.setObjectId(defaultObjectId);
                            b1.setDefaultAddress(false);
//设置当前点击的复选框为默认地址
                            AddressBook b2 = new AddressBook();
                            b2.setObjectId(item.getId());
                            b2.setDefaultAddress(true);
                            addressPre.updateBatch(b1, b2);
                            itemP = position;
                        }
                    });
                } else {
                    cb.setEnabled(false);
                }


                TextView tvEdit = holder.getView(R.id.tv_edit);
                TextView tvDelete = holder.getView(R.id.tv_delete);
                tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//    编辑 传递地址信息到添加地址活动界面
                        Intent intent = new Intent(mContext, AddAddressActivity.class);
                        if (type == 1) {
                            intent.putExtra("AddressType", 3);
                        } else if (type == 2) {
                            intent.putExtra("AddressType", 4);
                        }
                        intent.putExtra("ObjectId", item.getId());
                        intent.putExtra("Default", item.isDefaultAddress());
                        intent.putExtra("Name", item.getName());
                        intent.putExtra("Phone", item.getPhone());
                        intent.putExtra("Address", item.getAddress());
                        startActivityForResult(intent, REQUEST_CODE_1);

                    }
                });

                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog();
                        itemP = position;
                        addressPre.delete(mList.get(position).getId());
                    }
                });


            }
        };

        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvAddress.setLayoutManager(layoutManager);
        rvAddress.setAdapter(adapter);

    }

    /**
     * 加载地址列表
     */
    private void loadData() {
        mList.clear();
        showDialog();
        addressPre.query(BmobUser.getCurrentUser(User.class), addressType);
    }

    @Override
    public void showDialog() {
        dialog = new LoadingDialog(ManageAddressActivity.this, "努力加载中...");
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    public void onInsertSuccess(Object obj) {

    }

    @Override
    public void onInsertFailure(String error) {

    }

    @Override
    public void onQuerySuccess(List<AddressBook> list) {
        for (AddressBook book : list) {
            mList.add(new AddressBook(
                    book.getObjectId(),
                    book.getName(),
                    book.getPhone(),
                    book.getAddress(),
                    book.isAddressType(),
                    book.isDefaultAddress(),
                    false));
        }
        adapter.notifyDataSetChanged();
        dismissDialog();
    }

    @Override
    public void onQueryFailure(String error) {
        dismissDialog();
        Log.e("ManageAddressActivity", error);
        Toasty.error(mContext, error, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onDeleteSuccess(String str) {
        dismissDialog();
        adapter.delete(itemP);
    }

    @Override
    public void onDeleteFailure(String error) {
        dismissDialog();
        Toasty.error(mContext, error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpdateSuccess(String str) {
        dismissDialog();
        mList.get(itemP).setDefaultAddress(true);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onUpdateFailure(String error) {
        dismissDialog();
    }

    @OnClick({R.id.btn_add_address})
    public void onListener(View view) {
        switch (view.getId()) {
            case R.id.btn_add_address:
                Intent intent = new Intent(mContext, AddAddressActivity.class);
                intent.putExtra("AddressType", type);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_1 == requestCode) {
            if (resultCode == 3) {
                loadData();
            }
        }

    }
}
