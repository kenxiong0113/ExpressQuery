package com.ken.expressquery.management.address.p;

import android.util.Log;

import com.ken.expressquery.management.address.OnAddressFinishCallBack;
import com.ken.expressquery.management.address.m.AddressImpl;
import com.ken.expressquery.management.address.m.AddressModel;
import com.ken.expressquery.management.address.v.AddressView;
import com.ken.expressquery.management.bean.AddressBook;
import com.ken.expressquery.model.User;

import java.util.List;

public class AddressPre {
    private AddressView addressView;
    private AddressModel addressModel;
    public AddressPre(AddressView addressView){
        this.addressView = addressView;
        this.addressModel = AddressImpl.getInstances();
    }

    /**
     * 插入数据
     * */
    public void insert(User user, String name, String phone, String address, boolean type){
        addressModel.insert(user, name, phone, address, type, new OnAddressFinishCallBack() {
            @Override
            public void onFinishFailure(String error) {
                addressView.onInsertFailure(error);
            }

            @Override
            public void onFinishSuccess(String obj) {
                addressView.onInsertSuccess(obj);
            }

            @Override
            public void onQuerySuccess(List<AddressBook> list) {

            }
        });
    }

    /**
     * 查询数据
     * */
    public void query(User user, boolean type){
        addressModel.query(user, type, new OnAddressFinishCallBack() {
            @Override
            public void onFinishFailure(String error) {
                addressView.onQueryFailure(error);
            }

            @Override
            public void onFinishSuccess(String obj) {
            }

            @Override
            public void onQuerySuccess(List<AddressBook> list) {
                Log.e("AddressPre", "list.size():" + list.size());
                addressView.onQuerySuccess(list);
            }
        });

    }

    public void delete(String objectId){
        addressModel.delete(objectId, new OnAddressFinishCallBack() {
            @Override
            public void onFinishFailure(String error) {
                addressView.onDeleteFailure(error);
            }

            @Override
            public void onFinishSuccess(String obj) {
                addressView.onDeleteSuccess(obj);
            }

            @Override
            public void onQuerySuccess(List<AddressBook> list) {

            }
        });

    }

    public void update(String objectId, String name, String phone, String address){
        addressModel.update(objectId,name,phone,address,new OnAddressFinishCallBack() {
            @Override
            public void onFinishFailure(String error) {
                addressView.onUpdateFailure(error);
            }

            @Override
            public void onFinishSuccess(String obj) {
                addressView.onUpdateSuccess(obj);
            }

            @Override
            public void onQuerySuccess(List<AddressBook> list) {

            }
        });
    }

    public void updateBatch(AddressBook b1,AddressBook b2){
        addressModel.updateBatch(b1, b2, new OnAddressFinishCallBack() {
            @Override
            public void onFinishFailure(String error) {
                addressView.onUpdateSuccess(error);
            }

            @Override
            public void onFinishSuccess(String obj) {
                addressView.onUpdateSuccess(obj);
            }

            @Override
            public void onQuerySuccess(List<AddressBook> list) {

            }
        });

    }

}
