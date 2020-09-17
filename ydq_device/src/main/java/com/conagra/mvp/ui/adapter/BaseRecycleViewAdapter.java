package com.conagra.mvp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conagra.R;
import com.conagra.mvp.ui.adapter.viewholder.ViewDataHolder;

import java.util.List;


/**
 * Created by yedongqin on 2017/7/14.
 */

public abstract class BaseRecycleViewAdapter<T, M extends ViewDataBinding> extends RecyclerView.Adapter<ViewDataHolder<M>> {

    protected List<T> mDataList;
    protected OnClickListener<T> mOnClickListener;
    protected RecyclerView mRecycleView;
    protected Context mContext;
    public static final int TYPE_NORMAL = -3;
    public static final int TYPE_FOOTER = -1;
    public static final int TYPE_HEADER = -2;
    private static final int LIST_SPAN = 2;
    private static final int ITEM_SPACE_GAP = 12;
    private boolean hasHeader;
    private boolean hasFooter;
    private OnPullUpRefreshListener mOnPullUpRefreshListener;
    protected Object mHeaderData;
    protected Object mFooterData;

//    private View footer;
//    private View header;
//    private FooterEndBinding footerBinding;
//    private HeaderHisPublishBinding headerBinding;

    protected OnLongClickListener<T> mOnLongClickListener;

    public void setOnPullUpRefreshListener(OnPullUpRefreshListener onPullUpRefreshListener) {
        this.mOnPullUpRefreshListener = onPullUpRefreshListener;
    }

    public interface OnPullUpRefreshListener {
        void onPullUpListener();
    }


    public interface OnClickListener<T> {
        /**
         * 单击
         *
         * @param position
         * @param data
         */
        void onClick(int position, T data);
    }

    public interface OnLongClickListener<T> {
        /**
         * 长按
         *
         * @param position
         * @param data
         * @return
         */
        boolean onLongClick(int position, T data);
    }


    public BaseRecycleViewAdapter(RecyclerView recyclerView, List<T> mDataList) {
        this.mDataList = mDataList;
        this.mRecycleView = recyclerView;
        this.mContext = mRecycleView.getContext();
    }

    public BaseRecycleViewAdapter(RecyclerView recyclerView, List<T> mDataList,boolean hasHeader,boolean hasFooter) {
        this.mDataList = mDataList;
        this.mRecycleView = recyclerView;
        this.mContext = mRecycleView.getContext();
        this.hasFooter = hasFooter;
        this.hasHeader = hasHeader;
    }



    public void setOnClickListener(OnClickListener<T> mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }



    public void setOnLongClickListener(OnLongClickListener<T> mOnLongClickListener) {
        this.mOnLongClickListener = mOnLongClickListener;
    }

    public abstract  @LayoutRes
    int setLayout(int viewType);

    private ViewDataBinding inflate(ViewGroup parent, @LayoutRes int layoutId, int viewType) {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false);
    }

    public
    @LayoutRes
    int setFootLayout() {
        return R.layout.footer_end;
    }

    public
    @LayoutRes
    int setHeaderLayout() {
        return R.layout.header_default;
    }

    @Override
    public ViewDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding mBinding;
        if (viewType == TYPE_HEADER) {
            mBinding = inflate(parent, setHeaderLayout(), viewType);
        } else if (viewType == TYPE_FOOTER) {
            mBinding = inflate(parent, setFootLayout(), viewType);
        } else {
            mBinding = inflate(parent, setLayout(viewType), viewType);
        }
        return new ViewDataHolder(mBinding);
    }


    public void onBindViewHolder(M holder, int position) {

    }

    public void onBindViewHolder(M holder, T data, int position) {

    }

    @Override
    public void onBindViewHolder(ViewDataHolder holder, int position) {

        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                bindHeaderView(holder.getBinding(), mHeaderData);
                break;
            case TYPE_FOOTER:
                bindFooterView(holder.getBinding(), mFooterData);
                break;
            default:
                onBindViewHolder((M) holder.getBinding(), hasHeader ? mDataList.get(position - 1) : mDataList.get(position), hasHeader ? position - 1 : position);
                onBindViewHolder((M) holder.getBinding(), hasHeader ? position - 1 : position);
                break;
        }
    }



    @Override
    public int getItemCount() {
        return  (hasHeader&&hasFooter)?(getCount()+2):((hasHeader||hasFooter)?(getCount()+1):getCount());
    }


    @Override
    public int getItemViewType(int position) {
        if (hasFooter&&position == getItemCount() - 1) {
            return getFooterViewType();
        } else if (hasHeader&&position == 0) {
            return getHeaderViewType();
        }
        return position;
    }

    /**
     * 返回总数量
     *
     * @return
     */
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }


    public void addAll(List<T> list) {
        if (mDataList != null && list != null && list.size() > 0) {
            mDataList.addAll(list);
//            notifyItemRangeInserted(mDataList.size() - list.size(), list.size());
//            notifyItemRangeChanged(mDataList.size() - list.size(), mDataList.size());
            notifyDataSetChanged();
        }
    }

    public void add(T item){
        if(mDataList != null && item != null){
            mDataList.add(item);
//            notifyItemInserted(mDataList.size());
//            notifyItemChanged(mDataList.size());
            notifyDataSetChanged();

        }
    }

    public void setDataList(List<T> dataList) {
        if (dataList != null) {
            this.mDataList = dataList;
            notifyDataSetChanged();
        }
    }

    public void updateData(T data, int position) {
        if (data != null && position >= 0 && mDataList != null && position < mDataList.size()) {
            synchronized (BaseRecycleViewAdapter.class) {
                if (data != null && position >= 0 && mDataList != null && position < mDataList.size()) {
                    mDataList.set(position, data);
                }
            }
        }
        notifyItemChanged(position);
    }


    public void clear() {
        if (mDataList != null) {
            synchronized (BaseRecycleViewAdapter.class) {
                if (mDataList != null) {
                    mDataList.clear();
                }
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 添加事件监听器
     *
     * @param holder
     * @param position
     */
    public void addEventListener(M holder, final int position) {
        View view = holder.getRoot();
        if (view == null)
            return;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(position, mDataList.get(position));
                }

            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnLongClickListener != null) {
                    return mOnLongClickListener.onLongClick(position, mDataList.get(position));
                }
                return false;
            }
        });
    }

    //---------------header --------footer--------------


    public boolean isHasHeader() {
        return hasHeader;
    }


    public boolean isHasFooter() {
        return hasFooter;
    }

    public int getHeaderViewType(){
        return TYPE_HEADER;
    }

    public int getFooterViewType(){
        return TYPE_FOOTER;
    }




    public void setHeaderData(Object data) {
        if (data != null) {
            synchronized (BaseRecycleViewAdapter.class) {
                if (data != null) {
                    this.mHeaderData = data;
                    notifyItemChanged(0);
                }
            }
        }
    }

    public void setFooterData(Object data) {
        if (data != null) {
            synchronized (BaseRecycleViewAdapter.class) {
                if (data != null) {
                    this.mFooterData = data;
                    notifyItemChanged(getItemCount() - 1);
                }
            }
        }
    }

    protected void bindFooterView(ViewDataBinding binding, Object data) {
    }

    protected void bindHeaderView(ViewDataBinding binding, Object data) {
    }

}
