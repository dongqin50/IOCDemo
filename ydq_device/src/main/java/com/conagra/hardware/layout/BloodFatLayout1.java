package com.conagra.hardware.layout;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.conagra.R;
import com.conagra.databinding.ItemBloodfat1Binding;

public class BloodFatLayout1 extends LinearLayout {

    private ItemBloodfat1Binding mBinding;

    public BloodFatLayout1(Context context) {
        this(context,null,0);
    }

    public BloodFatLayout1(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BloodFatLayout1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_bloodfat1,this,true);
    }


}
