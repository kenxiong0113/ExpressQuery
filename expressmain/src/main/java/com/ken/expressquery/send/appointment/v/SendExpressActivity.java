package com.ken.expressquery.send.appointment.v;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ken.expressquery.R;
import com.ken.base.BaseActivity;
import com.ken.base.utils.BaseConstant;
import com.ken.base.utils.BaseRecyclerAdapter;
import com.ken.base.utils.BaseRecyclerHolder;
import com.ken.base.bean.PopList;
import com.ken.expressquery.management.AddAddressActivity;
import com.ken.expressquery.management.AddressBookActivity;
import com.ken.expressquery.management.address.p.AddressPre;
import com.ken.expressquery.management.address.v.AddressView;
import com.ken.expressquery.management.bean.AddressBook;
import com.ken.base.bean.User;
import com.ken.base.network.NetworkUtils;
import com.ken.expressquery.send.appointment.p.SendExpressPre;
import com.ken.expressquery.send.bean.ResultData;
import com.ken.expressquery.send.bean.SendExpressOrder;
import com.ken.expressquery.send.myorder.p.OrderPer;
import com.ken.expressquery.send.myorder.v.IViewOrder;
import com.ken.expressquery.send.result.p.PResult;
import com.ken.expressquery.send.result.v.ResultV;
import com.ken.base.threadpool.ThreadPoolProxyFactory;
import com.ken.base.utils.CalculateShippingCosts;
import com.ken.base.utils.Transform;
import com.ken.base.view.CustomPopWindow;
import com.ken.base.view.LoadingDialog;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import es.dmoral.toasty.Toasty;

/**
 * 寄快递活动界面
 *
 * @author by ken on 2018/5/18
 */
public class SendExpressActivity extends BaseActivity implements SendExpressView,
        View.OnTouchListener, View.OnClickListener, AddressView, ResultV, IViewOrder {
    private static final int CUT_WEIGHT = 0x001;
    private static final int ADD_WEIGHT = 0x002;
    private static final int SHOW_SEND_DIALOG = 0x003;
    private static final int DISMISS_SEND_DIALOG = 0x004;
    private static final int REQUEST_CODE_1 = 0x005;
    private static final int REQUEST_CODE_2 = 0x006;
    private static final int REQUEST_CODE_3 = 0x007;
    private static final int REQUEST_CODE_4 = 0x008;
    @SuppressLint("HandlerLeak")
    public Handler handler;
    @BindView(R.id.img_ic_send)
    ImageView imgIcSend;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_send_address)
    TextView tvSendAddress;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.address_book1)
    TextView addressBook1;
    @BindView(R.id.img_ic_receive)
    ImageView imgIcReceive;
    @BindView(R.id.tv_receive_name)
    TextView tvReceiveName;
    @BindView(R.id.tv_receive_phone)
    TextView tvReceivePhone;
    @BindView(R.id.tv_receive_address)
    TextView tvReceiveAddress;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.address_book2)
    TextView addressBook2;
    @BindView(R.id.tv_goods_type)
    TextView tvGoodsType;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.img_cut_weight)
    ImageView imgCutWeight;
    @BindView(R.id.et_weight)
    EditText etWeight;
    @BindView(R.id.img_add_weight)
    ImageView imgAddWeight;
    @BindView(R.id.img_cut_package)
    ImageView imgCutPackage;
    @BindView(R.id.et_package)
    EditText etPackage;
    @BindView(R.id.img_add_package)
    ImageView imgAddPackage;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.et_note)
    EditText etNote;
    @BindView(R.id.btn_reservation)
    Button btnReservation;
    @BindView(R.id.tv_select_com)
    TextView tvSelectCom;
    @BindView(R.id.ll_other)
    LinearLayout llOther;
    @BindView(R.id.tv_please_select)
    TextView tvPleaseSelect;
    @BindView(R.id.view5)
    View view5;
    @BindView(R.id.tv_cost)
    TextView tvCost;
    List<PopList> dataList = new ArrayList<>();
    BaseRecyclerAdapter<PopList> adapter;
    CustomPopWindow mListPopWindow;
    CustomPopWindow popBottomWindow;
    TextView tv1, tv2, tv3, tv4, tv5, tv6;
    String selectGoodsType = null;
    boolean onClickAdd = false;
    /**
     * 重量
     */
    int we;
    LoadingDialog dialog;
    User user;
    String sendName, sendPhone, sendAddress;
    String receiveName, receivePhone, receiveAddress;
    String weight, packageNum, leave, goodsType, expressCompany;
    String cost;
    String orderCode;
    String com, no;
    LoadingDialog sendDialog;
    TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            showCost(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            showCost(s.toString());
        }
    };
    private Activity activity;
    private Context mContext;
    private String TAG = SendExpressActivity.class.getName();
    private SendExpressPre sendExpressPre = new SendExpressPre(this);
    private AddressPre pre = new AddressPre(this);
    private PResult pResult = new PResult(this);
    private OrderPer orderPer = new OrderPer(this);

    {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case ADD_WEIGHT:
                        etWeight.setText(String.valueOf(we));
                        showCost(etWeight.getText().toString());
                        break;
                    case CUT_WEIGHT:
                        etWeight.setText(String.valueOf(we));
                        showCost(etWeight.getText().toString());
                        break;
                    case SHOW_SEND_DIALOG:
                        showSendDialog();
                        break;
                    case DISMISS_SEND_DIALOG:
                        dismissSendDialog();
                        AlertDialog.Builder builder = new AlertDialog.Builder(SendExpressActivity.this);
                        builder.setCancelable(false);
                        if (msg.arg1 == 1) {
                            builder.setTitle("预约成功");
                            builder.setIcon(R.drawable.ic_success);
                        } else {
                            builder.setTitle("预约失败");
                            builder.setIcon(R.drawable.ic_failure);
                            builder.setNegativeButton("取消", null);
                        }

                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_send_express;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initToolbar();
        activity = SendExpressActivity.this;
        mContext = getApplicationContext();
        user = BmobUser.getCurrentUser(User.class);
        dialog = new LoadingDialog(SendExpressActivity.this, "加载中...");
        dialog.setCancelable(true);
        initView();
        getSendAddressInfo();
        getReceiveAddressInfo();
    }

    @Override
    protected void onNetworkConnected(NetworkUtils.NetType type) {
        showNetErrorView();
    }

    @Override
    protected void onNetworkDisConnected() {

    }

    /**
     * 设置加减重量长按监听
     */
    private void initView() {
//        输入框中的光标移到最右侧
        etWeight.setSelection(etWeight.getText().toString().length());
        etPackage.setSelection(etPackage.getText().toString().length());
        etWeight.addTextChangedListener(watcher);
        showCost(etWeight.getText().toString());

        imgAddWeight.setOnTouchListener(this);
        imgCutWeight.setOnTouchListener(this);
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        setTitle("预约寄件");
        setTopLeftButton(R.drawable.ic_return, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    /**
     * 各个控件的监听事件
     */
    @OnClick({R.id.btn_reservation, R.id.tv_please_select,
            R.id.img_cut_weight, R.id.img_add_weight,
            R.id.tv_select_com, R.id.img_cut_package,
            R.id.img_add_package, R.id.tv_receive_address,
            R.id.tv_send_address, R.id.address_book1,
            R.id.address_book2})
    public void onListener(View view) {
        switch (view.getId()) {
            case R.id.btn_reservation:
//点击按钮获取控件文本框中的内容
                sendName = tvName.getText().toString();
                sendPhone = tvPhone.getText().toString();
                sendAddress = tvSendAddress.getText().toString();
                receiveName = tvReceiveName.getText().toString();
                receivePhone = tvReceivePhone.getText().toString();
                receiveAddress = tvReceiveAddress.getText().toString();

                goodsType = tvPleaseSelect.getText().toString();
                weight = etWeight.getText().toString();
                packageNum = etPackage.getText().toString();
                cost = tvCost.getText().toString();
                leave = etNote.getText().toString();
                expressCompany = tvSelectCom.getText().toString();
//查询自增订单号
                pResult.query(user, 1);

                break;
//                选择寄送物品类型
            case R.id.tv_please_select:
                showPopBottom();
                break;
//                减少重量
            case R.id.img_cut_weight:
                String wNum = etWeight.getText().toString();
                int weight = Integer.valueOf(wNum);
                weight--;
                if (weight >= 1) {
                    etWeight.setText(String.valueOf(weight));
                }
                showCost(etWeight.getText().toString());
                break;
//                增加重量
            case R.id.img_add_weight:
                String wNumAdd = etWeight.getText().toString();
                int weightAdd = Integer.valueOf(wNumAdd);
                weightAdd++;
                etWeight.setText(String.valueOf(weightAdd));
                showCost(etWeight.getText().toString());
                break;
            case R.id.img_cut_package:
                String pNum = etPackage.getText().toString();
                int p = Integer.valueOf(pNum);
                p--;
                if (p >= 1) {
                    etPackage.setText(String.valueOf(p));
                }
                break;
            case R.id.img_add_package:
                String pNumAdd = etPackage.getText().toString();
                int pAdd = Integer.valueOf(pNumAdd);
                pAdd++;
                etPackage.setText(String.valueOf(pAdd));
                break;
//                选择快递公司
            case R.id.tv_select_com:
                showPopListView();
                break;
            case R.id.tv_send_address:
                Intent intent = new Intent(mContext, AddAddressActivity.class);
                intent.putExtra("AddressType", 1);
                startActivityForResult(intent, REQUEST_CODE_1);
                break;
            case R.id.tv_receive_address:
                Intent intent1 = new Intent(mContext, AddAddressActivity.class);
                intent1.putExtra("AddressType", 2);
                startActivityForResult(intent1, REQUEST_CODE_2);
                break;
            case R.id.address_book1:
                Intent intent2 = new Intent(mContext, AddressBookActivity.class);
                intent2.putExtra("AddressType", 1);
                if (tvName.getText().length() != 0) {
//                    Info 传递地址信息的收寄类型
                    intent2.putExtra("AddressInfo", 1);
                    intent2.putExtra("Name", tvName.getText().toString());
                    intent2.putExtra("Phone", tvPhone.getText().toString());
                    intent2.putExtra("Address", tvSendAddress.getText().toString());
                }
                startActivityForResult(intent2, REQUEST_CODE_3);
                break;
            case R.id.address_book2:
                Intent intent3 = new Intent(mContext, AddressBookActivity.class);
                intent3.putExtra("AddressType", 2);
                if (tvReceiveName.getText().length() != 0) {
                    intent3.putExtra("AddressInfo", 1);
                    intent3.putExtra("Name", tvReceiveName.getText().toString());
                    intent3.putExtra("Phone", tvReceivePhone.getText().toString());
                    intent3.putExtra("Address", tvReceiveAddress.getText().toString());
                }
                startActivityForResult(intent3, REQUEST_CODE_4);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showSendDialog() {
        sendDialog = new LoadingDialog(SendExpressActivity.this, "预约中...");
        sendDialog.setCancelable(true);
        sendDialog.show();
    }

    @Override
    public void dismissSendDialog() {
        sendDialog.dismiss();
    }

    @Override
    public void onSendSuccess(String str) {
        Logger.e(TAG + "成功", str);
    }

    @Override
    public void onSendFailure(String error) {
        Logger.e(TAG + "发送寄件请求失败", error);
    }

    @Override
    public void resultSuccess(String data) {
//        解析预约寄件后的返回json格式的数据
        try {
            JSONObject object = new JSONObject(data);
            boolean isSuccess = object.getBoolean("Success");
            String order = object.getString("Order");
            // TODO: 2018/6/8 返回的寄件数据待解析
            JSONObject json = new JSONObject(order);
            if (isSuccess) {
                no = json.getString("LogisticCode");
            }
            com = Transform.codeToCom(json.getString("ShipperCode"));

//        预约寄件成功
            pResult.save(sendName,
                    sendPhone,
                    sendAddress,
                    receiveName,
                    receivePhone,
                    receiveAddress,
                    goodsType,
                    weight,
                    packageNum,
                    cost,
                    leave,
                    expressCompany);

//            startActivity(new Intent(mContext, MyOrderActivity.class));

        } catch (JSONException e) {
            Log.e("SendExpressActivity", e.getMessage());
            e.printStackTrace();
        }

        Message message = new Message();
        message.what = DISMISS_SEND_DIALOG;
        // 1--预约成功 2--预约失败
        message.arg1 = 1;
        handler.sendMessage(message);
    }

    @Override
    public void resultFailure(String error) {
//          预约寄件失败
        Message message = new Message();
        message.what = DISMISS_SEND_DIALOG;
        message.arg1 = 2;
        handler.sendMessage(message);

    }

    /**
     * 弹出View底部pop
     */
    private void showPopBottom() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_layout_bottom, null);
        popBottomWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .setFocusable(true)
                .setOutsideTouchable(true)
                //弹出popWindow时，背景是否变暗
                .enableBackgroundDark(true)
                // 控制亮度
                .setBgDarkAlpha(0.5f)
                .create();
        popBottomWindow.showAsDropDown(tvPleaseSelect, -200, 0);
        setGoodsTypeListener(view);

    }

    /**
     * 弹出listPOPWindow
     */
    private void showPopListView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_list, null);
        //处理popWindow 显示内容
        handleListView(contentView);
        //创建并显示popWindow
        mListPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setBgDarkAlpha(0.7f)
                .setView(contentView)
                //显示大小
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .create()
                .showAsDropDown(view5, 0, 20);

    }

    /**
     * 设置list类型的popWindows 的适配器，并填充数据
     */
    private void handleListView(View contentView) {
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new BaseRecyclerAdapter<PopList>(mContext, dataList, R.layout.pop_list_item) {
            @Override
            public void convert(BaseRecyclerHolder holder, PopList item, int position, boolean isScrolling) {
                holder.setText(R.id.tv_content, item.getContent());
            }
        };
        recyclerView.setAdapter(adapter);
        mockData();

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                tvSelectCom.setText(dataList.get(position).getContent());
                mListPopWindow.dissmiss();
            }
        });
    }

    /**
     * 底部popwindows的数据加载
     */
    private void mockData() {
//        清理数据列表
        dataList.clear();
        for (int i = 0; i < BaseConstant.COM.length; i++) {
            dataList.add(new PopList(BaseConstant.COM[i]));
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 重量加减的长按监听事件处理
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.img_add_weight:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onClickAdd = true;
                    ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(new Runnable() {
                        @Override
                        public void run() {
                            while (onClickAdd) {
                                String w = etWeight.getText().toString();
                                we = Integer.valueOf(w);
                                we++;
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } finally {
                                    Log.e("SendExpressActivity", "长按加重量");
                                    handler.sendEmptyMessage(ADD_WEIGHT);
                                }
                            }
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    onClickAdd = false;
                }
                break;
            case R.id.img_cut_weight:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onClickAdd = true;
                    ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(new Runnable() {
                        @Override
                        public void run() {
                            while (onClickAdd) {
                                String w = etWeight.getText().toString();
                                we = Integer.valueOf(w);
                                /** 重量小于1将不再变化*/
                                if (we > 1) {
                                    we--;
                                }
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } finally {
                                    Log.e("SendExpressActivity", "长按减重量");
                                    handler.sendEmptyMessage(CUT_WEIGHT);
                                }
                            }
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    onClickAdd = false;
                }

                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 初始化物品类型的popWindow的控件，并设置监听
     */
    public void setGoodsTypeListener(View view) {
        tv1 = (TextView) view.findViewById(R.id.tv1);
        tv2 = (TextView) view.findViewById(R.id.tv2);
        tv3 = (TextView) view.findViewById(R.id.tv3);
        tv4 = (TextView) view.findViewById(R.id.tv4);
        tv5 = (TextView) view.findViewById(R.id.tv5);
        tv6 = (TextView) view.findViewById(R.id.tv6);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
    }

    /***
     * 物品类型的pop监听事件
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv1:
                selectGoodsType = tv1.getText().toString();
                tvPleaseSelect.setText(selectGoodsType);
                popBottomWindow.dissmiss();
                break;
            case R.id.tv2:
                selectGoodsType = tv2.getText().toString();
                tvPleaseSelect.setText(selectGoodsType);
                popBottomWindow.dissmiss();
                break;
            case R.id.tv3:
                selectGoodsType = tv3.getText().toString();
                tvPleaseSelect.setText(selectGoodsType);
                popBottomWindow.dissmiss();
                break;
            case R.id.tv4:
                selectGoodsType = tv4.getText().toString();
                tvPleaseSelect.setText(selectGoodsType);
                popBottomWindow.dissmiss();
                break;
            case R.id.tv5:
                selectGoodsType = tv5.getText().toString();
                tvPleaseSelect.setText(selectGoodsType);
                popBottomWindow.dissmiss();
                break;
            case R.id.tv6:
                selectGoodsType = tv6.getText().toString();
                tvPleaseSelect.setText(selectGoodsType);
                popBottomWindow.dissmiss();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("SendExpressActivity", "requestCode:" + requestCode);
        Log.e("SendExpressActivity", "resultCode:" + resultCode);
//        从AddAddressActivity.class 活动的回调
        if (resultCode == 2) {
//               回调添加寄件地址信息
            if (requestCode == REQUEST_CODE_1) {
                tvName.setText(data.getStringExtra("Name"));
                tvPhone.setText(data.getStringExtra("Phone"));
                tvSendAddress.setText(data.getStringExtra("Area") + data.getStringExtra("Address"));
            } else if (requestCode == REQUEST_CODE_2) {
//                回调添加收件地址信息
                tvReceiveName.setText(data.getStringExtra("Name"));
                tvReceivePhone.setText(data.getStringExtra("Phone"));
                tvReceiveAddress.setText(data.getStringExtra("Area") + data.getStringExtra("Address"));
            }
        } else if (resultCode == 3) {
//                从地址簿（AddressBookActivity.class）活动界面的回调
            if (requestCode == REQUEST_CODE_3) {
//                    寄件地址簿
                tvName.setText(data.getStringExtra("Name"));
                tvPhone.setText(data.getStringExtra("Phone"));
                tvSendAddress.setText(data.getStringExtra("Address"));

            } else if (requestCode == REQUEST_CODE_4) {
//                    收件地址簿
                tvReceiveName.setText(data.getStringExtra("Name"));
                tvReceivePhone.setText(data.getStringExtra("Phone"));
                tvReceiveAddress.setText(data.getStringExtra("Address"));

            }
        }
    }

    /**
     * 获取寄件地址信息
     */
    private void getSendAddressInfo() {
        pre.query(user, true);
    }

    /**
     * 获取收件地址信息
     */
    private void getReceiveAddressInfo() {
        pre.query(user, false);
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
    public void onInsertFailure(String error) {
    }

    @Override
    public void onQuerySuccess(List<AddressBook> list) {
        if (list.size() != 0) {
            for (AddressBook book : list) {
//            寄件地址初始化信息
                if (book.isAddressType()) {
                    if (book.isDefaultAddress()) {
                        tvName.setText(book.getName());
                        tvPhone.setText(book.getPhone());
                        tvSendAddress.setText(book.getAddress());
                    }
                } else {
//            收件地址初始化信息
                    if (book.isDefaultAddress()) {
                        tvReceiveName.setText(book.getName());
                        tvReceivePhone.setText(book.getPhone());
                        tvReceiveAddress.setText(book.getAddress());
                    }
                }
            }
        } else {
//      空数据
        }
    }

    @Override
    public void onQueryFailure(String error) {
        Log.e("SendExpressActivity", error);
        Toasty.error(mContext, error, Toast.LENGTH_SHORT, true).show();
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

    @Override
    public void onInsertSuccess(Object obj) {

    }

    @Override
    public void bSuccess(String str) {
//        查询刚才插入预约寄件信息的数据
        queryResult(2);

        Log.e("SendExpressActivity", "保存到后台成功" + str);
    }

    @Override
    public void bFailure(String error) {
        Log.e("SendExpressActivity", "保存到后台失败" + error);
    }

    @Override
    public void bQuerySuccess(List<SendExpressOrder> list, int vrg) {
        if (vrg == 1) {
            if (list.size() != 0) {
                orderCode = String.valueOf(list.get(0).getOrderNumber() + 1);
            } else {
                Toasty.error(mContext, "获取订单号失败", Toast.LENGTH_SHORT, true).show();
                return;
            }
            boolean com = false;
//                判断快递公司是否选择正确
            for (int i = 0; i < BaseConstant.COM.length; i++) {
                if (expressCompany.equals(BaseConstant.COM[i])) {
                    com = true;
                }
            }
            if (sendName.length() == 0) {
                Toasty.warning(mContext, "请选择寄件地址", Toast.LENGTH_SHORT, true).show();
            } else if (receiveName.length() == 0) {
                Toasty.warning(mContext, "请选择收件地址", Toast.LENGTH_SHORT, true).show();
            } else if (goodsType.length() == 3) {
                Toasty.warning(mContext, "请选择物品类型", Toast.LENGTH_SHORT, true).show();
            } else if (!com) {
                Toasty.warning(mContext, "请选择快递公司", Toast.LENGTH_SHORT, true).show();
            } else {
                Message message = new Message();
                message.what = SHOW_SEND_DIALOG;
                handler.sendMessage(message);
                sendExpressPre.send(
                        orderCode,
                        sendName,
                        sendPhone,
                        sendAddress,
                        receiveName,
                        receivePhone,
                        receiveAddress,
                        goodsType,
                        weight,
                        packageNum,
                        cost,
                        leave,
                        expressCompany);
            }
        } else if (vrg == 2) {
            if (list.size() != 0) {
                orderPer.insert(com, no, list.get(0), user);

            }
        }

    }

    /**
     * 计算运费
     */
    private void showCost(String str) {
//        模拟计算运费，因为每个快递公司运费不一致，这里简化处理
        tvCost.setText(CalculateShippingCosts.calculate(str));
    }

    /**
     * 查询预约成功后添加进bmob后台的预约寄件信息的数据
     */
    private void queryResult(int vrg) {
        pResult.query(user, vrg);
    }

    @Override
    public void onOrderSuccess(String str) {
        Log.e("SendExpressActivity", str);
    }

    @Override
    public void onOrderFailure(String error) {

    }

    @Override
    public void onOrderQuerySuccess(List<ResultData> list) {

    }
}
