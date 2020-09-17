package com.conagra.hardware.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by yedongqin on 16/10/8.
 */
public class UpDownViewLinearLayout extends LinearLayout {


    public UpDownViewLinearLayout(Context context) {
        this(context,null,0);
    }

    public UpDownViewLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public UpDownViewLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scrollTo(0,0);
    }
}
