package com.ken.expressquery.management.bean;

import com.ken.expressquery.model.User;

import cn.bmob.v3.BmobObject;

/**
 * 地址信息实体类
 *
 * @author by ken on 2018/5/27
 * */
public class AddressBook extends BmobObject {
    String name;
    String phone;
    String address;
    User user;
    String id;
    Boolean addressType;
    Boolean defaultAddress;
    /** CheckBox 是否被选中*/
    boolean select;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public boolean isAddressType() {
        return addressType;
    }

    public void setAddressType(boolean addressType) {
        this.addressType = addressType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AddressBook(String id, String name, String phone, String address,
            boolean addressType, boolean defaultAddress, boolean select) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.addressType = addressType;
        this.defaultAddress = defaultAddress;
        this.select = select;
    }

    public AddressBook() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
