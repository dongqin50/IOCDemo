package com.conagra.mvp.ui.adapter.viewholder;


import android.databinding.ViewDataBinding;


public class ViewDataHolder<T extends ViewDataBinding> extends BindingViewHolder<T> {
    public ViewDataHolder(T binding) {
        super(binding);
    }
}
