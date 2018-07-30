package com.ken.expressquery.send.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ken.expressquery.R;

import java.util.List;
import java.util.Map;

/**
 * 寄快递模块 常用功能适配器
 *
 * @author by ken on 2018/5/29
 */
public class CommonFunctionsAdapter extends BaseAdapter {
    private List<Map<String, Object>> mItemList;
    private Context mContext;
    private int clickItem = -1;


    public CommonFunctionsAdapter(Context context, List<Map<String, Object>> listItem) {
        this.mContext = context;
        this.mItemList = listItem;
    }

    public void setSelection(int position) {
        this.clickItem = position;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_commom, null);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_order);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_my_order);

        Map<String, Object> map = mItemList.get(position);
        imageView.setImageResource((Integer) map.get("ItemImage"));
        textView.setText(map.get("ItemText") + "");
        notifyDataSetChanged();

        return convertView;
    }
}
