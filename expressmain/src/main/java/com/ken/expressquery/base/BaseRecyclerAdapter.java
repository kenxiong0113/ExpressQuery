package com.ken.expressquery.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author by ken on 2017/11/3.
 * 泛型封装RecyclerView适配器
 */

public abstract class BaseRecyclerAdapter <T> extends RecyclerView.Adapter<BaseRecyclerHolder> {
    /**
     * 上下文
     */
    private Context context;
    /**
     * 数据源
     */
    private List<T> list;
    /**
     * 布局器
     */
    private LayoutInflater inflater;
    /**
     * 布局id
     */
    private int itemLayoutId;
    /**
     * 是否在滚动
     */
    private boolean isScrolling;
    /**
     * 点击事件监听器
     */
    private OnItemClickListener listener;
    /**
     * 长按监听器
     */
    private OnItemLongClickListener longClickListener;
    /**
     * 在RecyclerView提供数据的时候调用
     */
    private RecyclerView recyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }


    public interface OnItemClickListener {
        /**
         * 定义一个点击事件接口回调
         * @param parent
         * @param view
         * @param position
         */
        void onItemClick(RecyclerView parent, View view, int position);
    }

    public interface OnItemLongClickListener {
        /**
         * 定义一个长按点击事件接口回调
         * @param parent
         * @param view
         * @param position
         * @return
         */
        boolean onItemLongClick(RecyclerView parent, View view, int position);
    }

    /**
     * 插入一项
     * @param item
     * @param position
     */
    public void insert(T item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
        /**
         * RecyclerView notifyItemInserted(0)在RecycleView未满的时候，
         * 会有Insert的动画效果。但是，当RecycleView已满（需要滑动才能看到全部数据）时，
         * 就不再有Insert的动画效果了。
         */
        if (position == 0){
            recyclerView.scrollToPosition(0);
        }
    }

    /**
     * 删除一项
     * @param position 删除位置
     */
    public void delete(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public BaseRecyclerAdapter(Context context, List<T> list, int itemLayoutId) {
        this.context = context;
        this.list = list;
        this.itemLayoutId = itemLayoutId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(itemLayoutId, parent, false);
        return BaseRecyclerHolder.getRecyclerHolder(context, view);
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null && view != null && recyclerView != null) {
                    int position = recyclerView.getChildAdapterPosition(view);
                    listener.onItemClick(recyclerView, view, position);
                }
            }
        });

/**
 * 长按
 */
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (longClickListener != null && view != null && recyclerView != null) {
                    int position = recyclerView.getChildAdapterPosition(view);
                    longClickListener.onItemLongClick(recyclerView, view, position);
                    return true;
                }
                return false;
            }
        });
        convert(holder, list.get(position), position, isScrolling);

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    /**
     * 填充RecyclerView适配器的方法，子类需要重写
     * @param holder      ViewHolder
     * @param item        子项
     * @param position    位置
     * @param isScrolling 是否在滑动
     */
    public abstract void convert(BaseRecyclerHolder holder, T item, int position, boolean isScrolling);


}
