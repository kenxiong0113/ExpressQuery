package com.ken.expressquery.management;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ken.expressquery.R;
import com.ken.expressquery.base.BaseActivity;
import com.ken.expressquery.base.BaseRecyclerAdapter;
import com.ken.expressquery.base.BaseRecyclerHolder;
import com.ken.expressquery.management.address.p.AddressPre;
import com.ken.expressquery.management.address.v.AddressView;
import com.ken.expressquery.management.address.v.ManageAddressActivity;
import com.ken.expressquery.management.bean.AddressBook;
import com.ken.expressquery.model.User;
import com.ken.expressquery.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

/**
 * 地址簿管理ui
 *
 * @author by ken on 2018/5/27
 */
public class AddressBookActivity extends BaseActivity implements AddressView {
    @BindView(R.id.rv_address_book)
    RecyclerView rvAddressBook;
    @BindView(R.id.btn_add_address)
    Button btnAddAddress;
    User user;
    boolean type;
    ;
    LoadingDialog dialog;
    String name = null, phone = null, address = null;
    /**
     * book值1--寄件
     * book值2--收件
     */
    int book, info;
    private Context mContext;
    private AddressPre pre = new AddressPre(this);
    private BaseRecyclerAdapter<AddressBook> adapter;
    private List<AddressBook> mList = new ArrayList<>();
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_address_book;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        dialog = new LoadingDialog(AddressBookActivity.this, "加载中...");
        dialog.setCancelable(true);
        user = BmobUser.getCurrentUser(User.class);
        //                    book 传递地址信息的收寄类型
        book = getIntent().getIntExtra("AddressType", 0);
        //                    Info 传递地址信息不为空 --1
        info = getIntent().getIntExtra("AddressInfo", 0);
        initData(info);
        setAdapter();
        initToolbar(book);
    }

    private void initToolbar(final int book) {
        if (book == 1) {
            setTitle("选择寄件人地址");
            type = true;
            loadData(type);

        } else if (book == 2) {
            setTitle("选择收件人地址");
            type = false;
            loadData(type);
        }

        setTopLeftButton(R.drawable.ic_return, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        setTopRightButton("管理", new OnClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(AddressBookActivity.this, ManageAddressActivity.class);
                intent.putExtra("AddressType", book);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData(int info) {
        if (info == 1) {
            name = getIntent().getStringExtra("Name");
            phone = getIntent().getStringExtra("Phone");
            address = getIntent().getStringExtra("Address");
        }

    }

    private void setAdapter() {
        adapter = new BaseRecyclerAdapter<AddressBook>(mContext, mList, R.layout.item_address_book) {
            @Override
            public void convert(BaseRecyclerHolder holder, AddressBook item, int position, boolean isScrolling) {
//                holder.setText(R.id.tv_name,item.getName());
                holder.setText(R.id.tv_phone, item.getPhone());
                holder.setText(R.id.cb_address, item.getAddress());
                CheckBox cb = holder.getView(R.id.cb_address);
                if (item.isSelect()) {
                    cb.setChecked(true);
                }
                TextView tv = holder.getView(R.id.tv_name);
                if (item.isDefaultAddress()) {

                    SpannableStringBuilder builder = new SpannableStringBuilder("[默认地址]" + item.getName());
                    ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
                    builder.setSpan(redSpan, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(builder);
//                    String str = "<font color='#FF0000'>[默认地址]</font>";
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        tv.setText(Html.fromHtml(str,Html.FROM_HTML_MODE_LEGACY)+item.getName());
//                    }else {
//                        tv.setText(Html.fromHtml(str)+item.getName());
//                    }
                } else {
                    holder.setText(R.id.tv_name, item.getName());
                }

            }
        };

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {

                Intent intent = new Intent();
                intent.putExtra("Name", mList.get(position).getName());
                intent.putExtra("Phone", mList.get(position).getPhone());
                intent.putExtra("Address", mList.get(position).getAddress());
                setResult(3, intent);
                finish();
            }
        });
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvAddressBook.setLayoutManager(layoutManager);
        rvAddressBook.setAdapter(adapter);

    }

    /**
     * 加载地址薄信息
     */
    private void loadData(boolean type) {
        showDialog();
        pre.query(user, type);
    }

    @OnClick({R.id.btn_add_address})
    public void onListener(View view) {
        switch (view.getId()) {
//                添加地址
            case R.id.btn_add_address:
                Intent intent = new Intent(mContext, AddAddressActivity.class);
                intent.putExtra("AddressType", book);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }

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
    public void onInsertSuccess(Object obj) {

    }

    @Override
    public void onInsertFailure(String error) {

    }

    @Override
    public void onQuerySuccess(List<AddressBook> list) {
        Log.e("AddressBookActivity", "list.size():" + list.size());
        boolean select;
        for (AddressBook book : list) {
            if (name.equals(book.getName()) && phone.equals(book.getPhone()) && address.equals(book.getAddress())) {
                select = true;
            } else {
                select = false;
            }
            mList.add(new AddressBook(
                    book.getObjectId(),
                    book.getName(),
                    book.getPhone(),
                    book.getAddress(),
                    book.isAddressType(),
                    book.isDefaultAddress(),
                    select));
        }
        adapter.notifyDataSetChanged();
        dismissDialog();
    }

    @Override
    public void onQueryFailure(String error) {
        dismissDialog();
        Log.e("AddressBookActivity", error);
    }

    @Override
    public void onDeleteSuccess(String str) {

    }

    @Override
    public void onDeleteFailure(String error) {

    }

    @Override
    public void onUpdateSuccess(String str) {

    }

    @Override
    public void onUpdateFailure(String error) {

    }
}
