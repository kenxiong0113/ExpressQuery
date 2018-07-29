package com.ken.expressquery.management;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ken.expressquery.R;
import com.ken.expressquery.management.address.p.AddressPre;
import com.ken.expressquery.management.address.v.AddressView;
import com.ken.expressquery.management.bean.AddressBook;
import com.ken.expressquery.base.BaseActivity;
import com.ken.expressquery.model.User;
import com.ken.expressquery.utils.InterceptAddressInfo;
import com.ken.expressquery.view.LoadingDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import es.dmoral.toasty.Toasty;
import me.leefeng.citypicker.CityPicker;
import me.leefeng.citypicker.CityPickerListener;

/**
 * 填写寄件、收件地址信息ui界面
 *
 * @author by ken on 2018/5/27
 */
public class AddAddressActivity extends BaseActivity implements CityPickerListener,AddressView{
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_area1)
    TextView tvArea1;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.cb_address)
    CheckBox cbAddress;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    private Context mContext;
    CityPicker cityPicker;
    private AddressPre pre;
    String objectId,name,phone,area,address;
    /** 存储的地址类型*/
    boolean addressType;
    User user;
    LoadingDialog dialog;
    int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_address;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        cityPicker = new CityPicker(this,this);
        user = BmobUser.getCurrentUser(User.class);
        type = getIntent().getIntExtra("AddressType", 0);
        initUI(type);
        pre = new AddressPre(this);
    }

    /**
     * 初始化toolbar
     */
    private void initUI(int type) {
        if (type == 1) {
            SharedPreferences preferences = getSharedPreferences("Location",MODE_PRIVATE);
            String province = preferences.getString("province", "选择当前位置");
            String city = preferences.getString("city", "");
            String district = preferences.getString("district", "");
            tvArea1.setText(province+" "+city+" "+district+" ");
            cbAddress.setText("保存寄件人到地址簿");
            setTitle("填写寄件人地址");
            addressType = true;
        } else if (type == 2) {
            addressType = false;
            cbAddress.setText("保存收件人到地址簿");
            setTitle("填写收件人地址");
        }else {
            String name = getIntent().getStringExtra("Name");
            String phone = getIntent().getStringExtra("Phone");
            String address = getIntent().getStringExtra("Address");
            objectId = getIntent().getStringExtra("ObjectId");
            String p,c,a,addr;
            p = InterceptAddressInfo.getAddressInfo(address,1);
            c = InterceptAddressInfo.getAddressInfo(address,2);
            a = InterceptAddressInfo.getAddressInfo(address,3);
            addr = InterceptAddressInfo.getAddressInfo(address,4);

            etName.setText(name);
            etPhone.setText(phone);
            tvArea1.setText(p+"\t"+c+"\t"+a);
            etAddress.setText(addr);
            if (type == 3){
               setTitle("修改寄件人地址");
                cbAddress.setText("保存寄件人到地址簿");
//                复选框不可点击
                cbAddress.setEnabled(false);
                addressType = true;

            }else if (type == 4){
                setTitle("修改收件人地址");
                cbAddress.setText("保存收件人到地址簿");
//                复选框不可点击
                cbAddress.setEnabled(false);
                addressType = false;
            }
        }
        setTopLeftButton(R.drawable.ic_return, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    @OnClick({R.id.tv_area1,R.id.btn_finish})
    public void onListener(View view){
        switch (view.getId()){
            case R.id.tv_area1:
                hintKeyboard();
                cityPicker.show();
                break;
            case R.id.btn_finish:
                name = etName.getText().toString();
                phone = etPhone.getText().toString();
                area = tvArea1.getText().toString();
                address = etAddress.getText().toString();


//                    新增地址信息
                    if (phone.length() != 11){
                        Toasty.error(mContext,"请填写正确的手机号",Toast.LENGTH_SHORT,true).show();
                    }else if (area.length() == 0|| address.length() == 0){
                        Toasty.error(mContext,"请填写正确的地址信息",Toast.LENGTH_SHORT,true).show();
                    }else {
                        showDialog();
                        if (type == 1 || type == 2) {
                            if (cbAddress.isChecked()) {

//                         添加地址信息
                                pre.insert(user, name, phone, area + address, addressType);
                            } else {
//                            地址仅使用一次，不存入地址簿，获取数据传递到上一个界面
                                resultAddress(2);
                            }

                        }
                        else if (type == 3||type == 4){
//                    修改地址信息,回调code -->3
                        pre.update(objectId,name,phone,area+address);
                        }
                    }

                break;
                default:
                    break;
        }

    }

    @Override
    public void getCity(String s) {
        tvArea1.setText(s+" ");
    }

    @Override
    public void showDialog() {
        dialog = new LoadingDialog(AddAddressActivity.this,"正在添加...");
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    public void onInsertFailure(String error) {
        dialog.dismiss();
        Toasty.error(mContext,error,Toast.LENGTH_SHORT,true).show();
        Log.e("AddAddressActivity", error);
    }

    @Override
    public void onQuerySuccess(List<AddressBook> list) {

    }

    @Override
    public void onQueryFailure(String error) {

    }

    @Override
    public void onDeleteSuccess(String str) {

    }

    @Override
    public void onDeleteFailure(String error) {

    }

    @Override
    public void onUpdateSuccess(String str) {
//        更新数据成功
        resultAddress(3);
    }

    @Override
    public void onUpdateFailure(String error) {
        dismissDialog();
        Toasty.error(mContext,error,Toast.LENGTH_SHORT,true).show();
    }

    @Override
    public void onInsertSuccess(Object mList) {
        Log.e("AddAddressActivity", "mList:" + mList);
        resultAddress(2);
    }
/**
 * 关闭软键盘
 * */
    private void hintKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus() != null){
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private void resultAddress(int i){
        dismissDialog();
        Intent intent = new Intent();
        intent.putExtra("Name",name);
        intent.putExtra("Phone",phone);
        intent.putExtra("Area",area);
        intent.putExtra("Address",address);
        setResult(i,intent);
        finish();
    }

}
