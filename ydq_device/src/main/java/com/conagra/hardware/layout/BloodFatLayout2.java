package com.conagra.hardware.layout;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.conagra.R;
import com.conagra.databinding.ItemBloodfat2Binding;


/**
 * Created by yedongqin on 2016/10/28.
 */

public class BloodFatLayout2 extends LinearLayout {

    private ItemBloodfat2Binding mBinding;

    public BloodFatLayout2(Context context) {
        this(context,null,0);
    }

    public BloodFatLayout2(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BloodFatLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_bloodfat2,this,true);
    }


}
