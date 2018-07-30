package com.ken.expressquery.management.address.m;

import android.util.Log;

import com.ken.expressquery.management.address.OnAddressFinishCallBack;
import com.ken.expressquery.management.bean.AddressBook;
import com.ken.expressquery.model.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 地址管理的model层实现类
 *
 * @author by ken on 2018/5/31
 */
public class AddressImpl implements AddressModel {
    private static AddressImpl instances = null;

    private AddressImpl() {
    }

    public static AddressImpl getInstances() {
        if (instances == null) {
            synchronized (AddressImpl.class) {
                if (instances == null) {
                    instances = new AddressImpl();
                }
            }
        }
        return instances;
    }

    @Override
    public void insert(User user, String name, String phone, String address, boolean type, final OnAddressFinishCallBack onAddressFinishCallBack) {
        AddressBook book = new AddressBook();
        book.setName(name);
        book.setPhone(phone);
        book.setAddress(address);
        book.setAddressType(type);
        book.setUser(user);
        book.setDefaultAddress(false);
        book.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    onAddressFinishCallBack.onFinishSuccess(s);
                } else {
                    onAddressFinishCallBack.onFinishFailure(e.getErrorCode() + e.getMessage());
                }
            }
        });
    }

    @Override
    public void delete(String objectId, final OnAddressFinishCallBack onAddressFinishCallBack) {
        AddressBook book = new AddressBook();
        book.setObjectId(objectId);
        book.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    onAddressFinishCallBack.onFinishSuccess("删除成功");
                } else {
                    onAddressFinishCallBack.onFinishFailure(e.getErrorCode() + e.getMessage());
                }
            }
        });
    }

    @Override
    public void update(String objectId, String name, String phone, String address, final OnAddressFinishCallBack onAddressFinishCallBack) {
        AddressBook book = new AddressBook();
        book.setName(name);
        book.setPhone(phone);
        book.setAddress(address);
        book.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    onAddressFinishCallBack.onFinishSuccess("更新成功");
                } else {
                    onAddressFinishCallBack.onFinishFailure(e.getErrorCode() + e.getMessage());
                }
            }
        });
    }

    @Override
    public void query(User user, boolean type, final OnAddressFinishCallBack onAddressFinishCallBack) {
        BmobQuery<AddressBook> query = new BmobQuery<AddressBook>();
        query.addWhereEqualTo("user", user);
        query.addWhereEqualTo("addressType", type);
        query.findObjects(new FindListener<AddressBook>() {
            @Override
            public void done(List<AddressBook> list, BmobException e) {
                if (e == null) {
                    onAddressFinishCallBack.onQuerySuccess(list);
                } else {
                    onAddressFinishCallBack.onFinishFailure(e.getErrorCode() + e.getMessage());
                }
            }
        });
    }

    @Override
    public void updateBatch(AddressBook b1, AddressBook b2, final OnAddressFinishCallBack onAddressFinishCallBack) {
        // TODO: 2018/6/1 批量更新数据有异常，待处理
        List<BmobObject> list = new ArrayList<>();
        list.add(b1);
        list.add(b2);
        new BmobBatch().updateBatch(list).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> o, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < o.size(); i++) {
                        BatchResult result = o.get(i);
                        BmobException ex = result.getError();
                        if (ex == null) {
                            onAddressFinishCallBack.onFinishSuccess("批量更新成功");
                        } else {
                            onAddressFinishCallBack.onFinishSuccess("第" + i + "个数据更新失败");
                        }
                    }
                } else {
                    onAddressFinishCallBack.onFinishFailure(e.getErrorCode() + e.getMessage());
                    Log.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


}
